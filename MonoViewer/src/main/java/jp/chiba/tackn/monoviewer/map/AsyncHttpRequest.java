package jp.chiba.tackn.monoviewer.map;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import jp.chiba.tackn.monoviewer.InformationHolder;


/**
 * herokuで公開している運行情報を取得するための非同期タスク
 */
class AsyncHttpRequest extends AsyncTask<Uri.Builder, Void, String> {

    /** デバッグ用タグ */
    private static final String TAG = "AsyncHttpRequest";
    /** デバッグ用フラグ */
    private static final boolean DEBUG = false;

    /** 呼び出し元コンテキスト */
    private Context context;
    /** 呼び出し元Activity */
    private Activity mainActivity;

    /** 休日・平日フラグ */
    private String holiday;
    /** モノちゃん号の運行情報 */
    private String Service0;
    /** アーバンフライ０ 1-2号の運行情報 */
    private String Service1;
    /** アーバンフライ０ 3-4号の運行情報 */
    private String Service2;
    /** アーバンフライ０ 5-6号の運行情報 */
    private String Service3;
    /** アーバンフライ０ 7-8号の運行情報 */
    private String Service4;

    /** 運行情報管理クラス */
    InformationHolder informationHolder = InformationHolder.getInstance();

    public AsyncHttpRequest(Context context) {

    }

    /**
     * 非同期処理本体
     * @param builders ハンドラ
     * @return onPostExecuteへ伝える引数
     */
    @Override
    protected String doInBackground(Uri.Builder... builders) {
        int n =0;
        while (!informationHolder.isReady()){
            try {
                Thread.sleep(1000);
                n++;
                if(n==60)break;
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
                return "false";
            }
        }
        holiday = informationHolder.getHoliday();
        Service0 = informationHolder.getService0();
        Service1 = informationHolder.getService1();
        Service2 = informationHolder.getService2();
        Service3 = informationHolder.getService3();
        Service4 = informationHolder.getService4();

        return informationHolder.isReady()?"ture":"false";
    }

    /**
     * 非同期処理の結果を処理
     * @param result 非同期処理の結果の引数　成否判定
     */
    @Override
    protected void onPostExecute(String result) {
        if (result.equals("false")) MonoViewFragment.setHoliday(1);
        //休日・平日判定
        if (holiday.equals("true")) {
            MonoViewFragment.setHoliday(0);
        } else if (holiday.equals("false")) {
            MonoViewFragment.setHoliday(1);
        } else {
            //取得失敗
            MonoViewFragment.setHoliday(1);
        }
        MonoViewFragment.setService0(Service0.matches("[0-9]+") ? Integer.valueOf(Service0) : -1);
        MonoViewFragment.setService1(Service1.matches("[0-9]+") ? Integer.valueOf(Service1) : -1);
        MonoViewFragment.setService2(Service2.matches("[0-9]+") ? Integer.valueOf(Service2) : -1);
        MonoViewFragment.setService3(Service3.matches("[0-9]+") ? Integer.valueOf(Service3) : -1);
        MonoViewFragment.setService4(Service4.matches("[0-9]+") ? Integer.valueOf(Service4) : -1);
    }
}
