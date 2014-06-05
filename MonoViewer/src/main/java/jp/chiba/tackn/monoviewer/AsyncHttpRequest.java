package jp.chiba.tackn.monoviewer;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import jp.chiba.tackn.monoviewer.map.Station;
import jp.chiba.tackn.monoviewer.train.TrainTable;

/**
 * heroku から運行情報を取得するAsyncTask
 *
 * @author Takumi Ito
 * @since 2014/05/14.
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

    /** 休日・平日フラグ */
    private static int intHoliday;

    /** 運行情報管理クラス */
    private InformationHolder informationHolder = InformationHolder.getInstance();
    /** FragmentのLoadManagerに通知 */
    private LoaderManager.LoaderCallbacks callbacks;
    /** タブレットモードの保持 */
    private TabletHolder tabletHolder = TabletHolder.getInstance();
    /**
     * コンストラクタ
     * @param activity 呼び出し元
     * @param callbacks
     */
    public AsyncHttpRequest(Activity activity, LoaderManager.LoaderCallbacks callbacks) {

        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
        this.context = activity.getApplicationContext();
        this.callbacks = callbacks;
    }

    /**
     * 非同期処理実行本体
     * @param builder 引数
     * @return 戻り値
     */
    @Override
    protected String doInBackground(Uri.Builder... builder) {
        int n =0;
        while (!informationHolder.isReady()){
        try {
                Thread.sleep(1000);
                n++;
                if(n==60)break;
            } catch (InterruptedException e) {
                Log.e(TAG,e.toString());
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


    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {

        if(result.equals(false)) Toast.makeText(context,"データの取得に失敗しました",Toast.LENGTH_SHORT).show();

        // 取得した結果をテキストビューに入れちゃったり
        TextView Holiday = (TextView) mainActivity.findViewById(R.id.HOLIDAY);
        TextView Service0 = (TextView) mainActivity.findViewById(R.id.Service0);
        TextView Service1 = (TextView) mainActivity.findViewById(R.id.Service1);
        TextView Service2 = (TextView) mainActivity.findViewById(R.id.Service2);
        TextView Service3 = (TextView) mainActivity.findViewById(R.id.Service3);
        TextView Service4 = (TextView) mainActivity.findViewById(R.id.Service4);
        TextView train0 = (TextView) mainActivity.findViewById(R.id.trainNo0);
        TextView train1 = (TextView) mainActivity.findViewById(R.id.trainNo1);
        TextView train2 = (TextView) mainActivity.findViewById(R.id.trainNo2);
        TextView train3 = (TextView) mainActivity.findViewById(R.id.trainNo3);
        TextView train4 = (TextView) mainActivity.findViewById(R.id.trainNo4);

        final Spinner trainNo = (Spinner) mainActivity.findViewById(R.id.Spinner);
        RadioButton radioHoliday = (RadioButton) mainActivity.findViewById(R.id.holiday);
        RadioButton radioWeekday = (RadioButton) mainActivity.findViewById(R.id.weekday);

        //休日・平日判定
        if(holiday.equals("true")){
            Holiday.setText("休日");
            intHoliday=Station.HOLIDAY;
            if(radioHoliday!=null)radioHoliday.setChecked(true);
            if(radioWeekday!=null)radioWeekday.setChecked(false);
        }else if(holiday.equals("false")){
            Holiday.setText("平日");
            intHoliday=Station.WEEKDAY;
            if(radioHoliday!=null)radioHoliday.setChecked(false);
            if(radioWeekday!=null)radioWeekday.setChecked(true);
        }else{
            //取得失敗
            Holiday.setText("失敗");
            Service0.setText("失敗");
            Service1.setText("失敗");
            Service2.setText("失敗");
            Service3.setText("失敗");
            Service4.setText("失敗");
        }

        //モノちゃん号
        if(this.Service0.length()>0){
            Service0.setText(this.Service0);
            Service0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tabletHolder.isTablet()) {
                        initLoader(Integer.valueOf(AsyncHttpRequest.this.Service0));
                        trainNo.setSelection(getSpinnerValue(AsyncHttpRequest.this.Service0));
                    }else{
                        Intent intent = new Intent(context, TrainTable.class);
                        intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service0));
                        intent.putExtra("holiday", Integer.valueOf(intHoliday));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
            train0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tabletHolder.isTablet()) {
                        initLoader(getSpinnerValue(AsyncHttpRequest.this.Service0));
                        trainNo.setSelection(getSpinnerValue(AsyncHttpRequest.this.Service0));
                    }else{
                        Intent intent = new Intent(context, TrainTable.class);
                        intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service0));
                        intent.putExtra("holiday", Integer.valueOf(intHoliday));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else{
            Service0.setText("運休");
        }

        //アーバンフライ０ 1-2号
        if(this.Service1.length()>0){
            Service1.setText(this.Service1);
            Service1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tabletHolder.isTablet()) {
                        initLoader(getSpinnerValue(AsyncHttpRequest.this.Service1));
                        trainNo.setSelection(getSpinnerValue(AsyncHttpRequest.this.Service1));
                    }else{
                        Intent intent = new Intent(context, TrainTable.class);
                        intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service1));
                        intent.putExtra("holiday", Integer.valueOf(intHoliday));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
            train1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tabletHolder.isTablet()) {
                        initLoader(getSpinnerValue(AsyncHttpRequest.this.Service1));
                        trainNo.setSelection(getSpinnerValue(AsyncHttpRequest.this.Service1));
                    }else{
                        Intent intent = new Intent(context, TrainTable.class);
                        intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service1));
                        intent.putExtra("holiday", Integer.valueOf(intHoliday));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else{
            Service1.setText("運休");
        }

        //アーバンフライ０ 3-4号
        if(this.Service2.length()>0){
            Service2.setText(this.Service2);
            Service2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tabletHolder.isTablet()) {
                        initLoader(getSpinnerValue(AsyncHttpRequest.this.Service2));
                        trainNo.setSelection(getSpinnerValue(AsyncHttpRequest.this.Service2));
                    }else{
                        Intent intent = new Intent(context, TrainTable.class);
                        intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service2));
                        intent.putExtra("holiday", Integer.valueOf(intHoliday));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
            train2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tabletHolder.isTablet()) {
                        initLoader(getSpinnerValue(AsyncHttpRequest.this.Service2));
                        trainNo.setSelection(getSpinnerValue(AsyncHttpRequest.this.Service2));
                    }else{

                        Intent intent = new Intent(context, TrainTable.class);
                        intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service2));
                        intent.putExtra("holiday", Integer.valueOf(intHoliday));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

        }else{
            Service2.setText("運休");
        }

        //アーバンフライ０ 5-6号
        if(this.Service3.length()>0){
            Service3.setText(this.Service3);
            Service3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tabletHolder.isTablet()){
                        initLoader(getSpinnerValue(AsyncHttpRequest.this.Service3));
                        trainNo.setSelection(getSpinnerValue(AsyncHttpRequest.this.Service3));
                    }else{
                        Intent intent = new Intent(context, TrainTable.class);
                        intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service3));
                        intent.putExtra("holiday", Integer.valueOf(intHoliday));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
            train3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tabletHolder.isTablet()){
                        initLoader(getSpinnerValue(AsyncHttpRequest.this.Service3));
                        trainNo.setSelection(getSpinnerValue(AsyncHttpRequest.this.Service3));
                    }else{
                        Intent intent = new Intent(context, TrainTable.class);
                        intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service3));
                        intent.putExtra("holiday", Integer.valueOf(intHoliday));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

        }else{
            Service3.setText("運休");
        }

        //アーバンフライ０ 7-8号
        if(this.Service4.length()>0){
            Service4.setText(this.Service4);
            Service4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tabletHolder.isTablet()) {
                        initLoader(getSpinnerValue(AsyncHttpRequest.this.Service4));
                        trainNo.setSelection(getSpinnerValue(AsyncHttpRequest.this.Service4));
                    }else{
                        Intent intent = new Intent(context, TrainTable.class);
                        intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service4));
                        intent.putExtra("holiday", Integer.valueOf(intHoliday));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
            train4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tabletHolder.isTablet()){
                        initLoader(getSpinnerValue(AsyncHttpRequest.this.Service4));
                        trainNo.setSelection(getSpinnerValue(AsyncHttpRequest.this.Service4));
                    }else{
                        Intent intent = new Intent(context, TrainTable.class);
                        intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service4));
                        intent.putExtra("holiday", Integer.valueOf(intHoliday));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

        }else{
            Service4.setText("運休");
        }
   }


    private Integer getSpinnerValue(String string){
        int i = Integer.valueOf(string);
        switch (i){
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            case 11:
                return 7;
            case 12:
                return 8;
            case 21:
                return 9;
            case 22:
                return 10;
            case 25:
                return 11;
            case 26:
                return 12;
            default:
                return 1;
        }
    }

    /**
     * スピナーで選択したときのCursolLoaderの初期化
     * @param position Loaderの初期化時の通知パラメータ
     */
    void initLoader(int position){
        initLoader(position,intHoliday==Station.HOLIDAY);
    }

    /**
     * スピナーで選択したときのCursolLoaderの初期化
     * @param position Loaderの初期化時の通知パラメータ
     * @param isHoriday 休日ダイヤフラグ
     */
    void initLoader(int position,boolean isHoriday) {
        if (!isHoriday) { //平日はスピナー通り
            mainActivity.getLoaderManager().initLoader(position, null, callbacks);
        } else { //休日のスピナーとのズレを正す
            switch (position) {
                case 0:
                    mainActivity.getLoaderManager().restartLoader(13, null, callbacks);
                    break;
                case 1:
                    mainActivity.getLoaderManager().restartLoader(14, null, callbacks);
                    break;
                case 2:
                    mainActivity.getLoaderManager().restartLoader(15, null, callbacks);
                    break;
                case 3:
                    mainActivity.getLoaderManager().restartLoader(16, null, callbacks);
                    break;
                case 4:
                    mainActivity.getLoaderManager().restartLoader(17, null, callbacks);
                    break;
                case 5:
                    mainActivity.getLoaderManager().restartLoader(18, null, callbacks);
                    break;
                case 6:
                    break; //存在しない
                case 7:
                    mainActivity.getLoaderManager().restartLoader(19, null, callbacks);
                    break;
                case 8:
                    mainActivity.getLoaderManager().restartLoader(20, null, callbacks);
                    break;
                case 9:
                    mainActivity.getLoaderManager().restartLoader(21, null, callbacks);
                    break;
                case 10:
                    mainActivity.getLoaderManager().restartLoader(22, null, callbacks);
                    break;
                case 11:
                    mainActivity.getLoaderManager().restartLoader(23, null, callbacks);
                    break;
                case 12:
                    mainActivity.getLoaderManager().restartLoader(24, null, callbacks);
                    break;
                default:
                    if (DEBUG) Log.d(TAG, "position() " + position);
                    break; //存在しない
            }

        }
    }

}