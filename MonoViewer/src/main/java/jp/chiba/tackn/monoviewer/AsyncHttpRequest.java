package jp.chiba.tackn.monoviewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
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

    /**
     * コンストラクタ
     * @param activity 呼び出し元
     */
    public AsyncHttpRequest(Activity activity) {

        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
        this.context = activity.getApplicationContext();
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


        //休日・平日判定
        if(holiday.equals("true")){
            Holiday.setText("休日");
            intHoliday=Station.HOLIDAY;
        }else if(holiday.equals("false")){
            Holiday.setText("平日");
            intHoliday=Station.WEEKDAY;
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
                    Intent intent = new Intent(context, TrainTable.class);
                    intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service0));
                    intent.putExtra("holiday", Integer.valueOf(intHoliday));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            train0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TrainTable.class);
                    intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service0));
                    intent.putExtra("holiday", Integer.valueOf(intHoliday));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
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
                    Intent intent = new Intent(context, TrainTable.class);
                    intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service1));
                    intent.putExtra("holiday", Integer.valueOf(intHoliday));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            train1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TrainTable.class);
                    intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service1));
                    intent.putExtra("holiday", Integer.valueOf(intHoliday));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
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
                    Intent intent = new Intent(context, TrainTable.class);
                    intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service2));
                    intent.putExtra("holiday", Integer.valueOf(intHoliday));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            train2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TrainTable.class);
                    intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service2));
                    intent.putExtra("holiday", Integer.valueOf(intHoliday));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
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
                    Intent intent = new Intent(context, TrainTable.class);
                    intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service3));
                    intent.putExtra("holiday", Integer.valueOf(intHoliday));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            train3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TrainTable.class);
                    intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service3));
                    intent.putExtra("holiday", Integer.valueOf(intHoliday));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
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
                    Intent intent = new Intent(context, TrainTable.class);
                    intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service4));
                    intent.putExtra("holiday", Integer.valueOf(intHoliday));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            train4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TrainTable.class);
                    intent.putExtra("TableNo", Integer.valueOf(AsyncHttpRequest.this.Service4));
                    intent.putExtra("holiday", Integer.valueOf(intHoliday));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }else{
            Service4.setText("運休");
        }
   }
}