package jp.chiba.tackn.monoviewer.time;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.analytics.tracking.android.EasyTracker;

import jp.chiba.tackn.monoviewer.Menus;
import jp.chiba.tackn.monoviewer.R;
import jp.chiba.tackn.monoviewer.map.Station;

/**
 * SQLiteに格納済みの時刻表データの表示を行う
 *
 * @author Takumi Ito
 */
public class TimeTable extends Activity
        implements AdapterView.OnItemSelectedListener{


    /** デバッグフラグ*/
    private static final boolean DEBUG = false;
    /** デバッグタグ */
    private static final String TAG = "TimeTable";

    /** 時刻表(COLUMN_STATION)選択用スピナー */
    private Spinner spinner;

    /** 休日・平日のラジオボタン用 */
    private RadioGroup holidayGroup;
    /** 休日ラジオボタン */
    private RadioButton holiday;
    /** 平日ラジオボタン */
    private RadioButton weekday;

    /** 上り・下りのラジオボタン用 */
    private RadioGroup updown;
    /** 上り */
    private RadioButton up;
    /** 共通下り */
    private RadioButton down1;
    /** 千葉駅用下り */
    private RadioButton down2;
    /** スピナーの選択位置の保持用 */
    private int selectSpinner=0;

    /** FragmentのLoadManagerに通知 */
    private LoaderManager.LoaderCallbacks callbacks;
    /** LoaderManager.LoaderCallbacksの為 */
    public TimeTableFragment time_table;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table);

        //要素の取得
        findViews();

        //ラジオボタン用設定
        holidayGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                initLoader(selectSpinner);
            }
        });
        updown.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                initLoader(selectSpinner);
            }
        });

        //外部から起動された時のスピナーの選択処理
        String fromIntent0 = "";
        int fromIntent1 = 0;
        int fromIntent2 = 0;
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            fromIntent0 = extras.getString("station");
            fromIntent1 = extras.getInt("updown");
            fromIntent2 = extras.getInt("holiday");
        }
        if(DEBUG){
            Log.d(TAG,"fromIntent station: " + fromIntent0);
            Log.d(TAG,"fromIntent updown: " + fromIntent1);
            Log.d(TAG, "fromIntent holiday: " + fromIntent2);
        }

        //スピナー用設定
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        setSpinnerAdapter(spAdapter);
        spinner.setAdapter(spAdapter);
        spinner.setOnItemSelectedListener(this);

        //休日・平日の設定
        if(fromIntent2== Station.HOLIDAY){
            holiday.setChecked(true);
            weekday.setChecked(false);
        }else{
            holiday.setChecked(false);
            weekday.setChecked(true);
        }

        //フラグチェック
        checkflag(fromIntent0,fromIntent1);

        //選択位置検索
        int position =0;
        for(int i=0;i<spAdapter.getCount();i++) {
            String item = spAdapter.getItem(i);
            if (item.equals(fromIntent0)) {
                position = i;
                break;
            }
        }

        //千葉駅対応
        if(fromIntent0.equals("千葉駅1号線")){
            position= 15;
            down1.setEnabled(true);
            down2.setEnabled(true);
            down1.setChecked(true);
            down2.setChecked(false);
        }
        if(fromIntent0.equals("千葉駅2号線")){
            position= 15;
            down1.setEnabled(true);
            down2.setEnabled(true);
            down1.setChecked(false);
            down2.setChecked(true);
        }

        //スピナー設定
        spinner.setSelection(position);
    }

    /** 端の駅で上り・下りのチェック */
    private void checkflag(String station,int updown){
        //不活性初期化
        up.setEnabled(true);
        down1.setEnabled(true);
        down2.setEnabled(true);

        //上り・下りチェック本体
        if(updown==Station.DOWN2) {
            up.setChecked(false);
            down1.setChecked(false);
            down2.setChecked(true);
        }else if(updown==Station.DOWN){
            up.setChecked(false);
            down1.setChecked(true);
            down2.setChecked(false);
        }else{
            up.setChecked(true);
            down1.setChecked(false);
            down2.setChecked(false);
        }
        //駅固有の設定
        setChibaStationDown(station.equals("千葉駅"));
        if(station.equals("千城台駅"))setChishirodaiStationDown();
        if(station.equals("県庁前駅"))setKentyoumaeStationDown();
        if(station.equals("千葉みなと駅"))setChibaMinatoStationUp();
    }

    /**
     * 千葉みなと駅は上りがないので切替
     */
    private void setChibaMinatoStationUp() {
            up.setChecked(false);
            up.setEnabled(false);
            down1.setChecked(true);
            down2.setChecked(false);
            down2.setEnabled(false);
    }

    /**
     * 県庁前駅は下りがないので切替
     */
    private void setKentyoumaeStationDown() {
            up.setChecked(true);
            down1.setChecked(false);
            down1.setEnabled(false);
            down2.setChecked(false);
            down2.setEnabled(false);
    }

    /**
     * 千城台駅は下りがないので切替
     */
    private void setChishirodaiStationDown() {
            up.setChecked(true);
            down1.setChecked(false);
            down1.setEnabled(false);
            down2.setChecked(false);
            down2.setEnabled(false);
    }

    /**
     * 千葉駅だけ下りを2選択にする
     * @param enable 千葉駅用にするかどうか
     */
    private void setChibaStationDown(boolean enable){
        if(enable){
            down2.setEnabled(true);
            down1.setText("１号線下り");
            down2.setText("２号線下り");
        }else{
            down2.setEnabled(false);
            down1.setText("下り");
            down2.setText("　　");
        }
    }

    /**
     * onCreate()時にレイアウト済みオブジェクトの取得
     */
    private void findViews() {
        spinner = (Spinner) findViewById(R.id.Spinner);
        holidayGroup = (RadioGroup) findViewById(R.id.holidayGroup);
        holiday = (RadioButton) findViewById(R.id.holiday);
        weekday = (RadioButton) findViewById(R.id.weekday);
        updown = (RadioGroup) findViewById(R.id.updown);
        up = (RadioButton) findViewById(R.id.up);
        down1 = (RadioButton) findViewById(R.id.down1);
        down2 = (RadioButton) findViewById(R.id.down2);
        FragmentManager fragmentManager = getFragmentManager();
        time_table = (TimeTableFragment)fragmentManager.findFragmentById(R.id.timetablelist);
        callbacks =  time_table;//LoaderManager.LoaderCallbacks
    }
    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }
    /**
     * スピナーで選択した際に呼び出し
     *
     * @param parent アダプタを登録しているView(スピナー)オブジェクト
     * @param view 選択されたview
     * @param position 選択した位置
     * @param id 選択したID
     */
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (DEBUG) {
//            Log.d(TAG,"view:" + (parent==spinner));
            Log.d(TAG, "position:" + position);
        }
        if (parent == spinner) {
            //Listに紐付けていたプロバイダURIの切替
            String station = (String)spinner.getAdapter().getItem(position);
            int way;
            if(down1.isChecked()){
                way = Station.DOWN;
            }else if(down2.isChecked()){
                way = Station.DOWN2;
            }else{
                way = Station.UP;
            }
            selectSpinner=position;
            checkflag(station,way);
            initLoader(position);
        }
    }

    /**
     * スピナーで選択が解除された時に呼び出し
     *
     * @param parent アダプタを登録しているView(スピナー)オブジェクト
     */
    public void onNothingSelected(AdapterView<?> parent) {
        if (DEBUG) {
            Log.d(TAG, "onNothingSelected :");
        }
        //初期化
//        initLoader(0);
    }

    /**
     * スピナーで選択したときのCursolLoaderの初期化
     * @param position Loaderの初期化時の通知パラメータ
     */
    private void initLoader(int position){

        //平日チェック
        if(weekday.isChecked()){
            if(up.isChecked()){ //平日上りはスピナー通り
                getLoaderManager().initLoader(position, null, callbacks);
            }else{
                switch (position+18){
                    case 33:
                        if(down1.isChecked()){
                            getLoaderManager().initLoader(TimeTableContract.HS16D1, null, callbacks);
                        }else{
                            getLoaderManager().initLoader(TimeTableContract.HS16D2, null, callbacks);
                        }
                        break;
                    case 34:
                        getLoaderManager().initLoader(TimeTableContract.HS17D, null, callbacks);
                        break;
                    case 35:
                        getLoaderManager().initLoader(TimeTableContract.HS18D, null, callbacks);
                        break;
                    default:
                        getLoaderManager().initLoader(position+18, null, callbacks);
                        break;
                }
            }
        }else { //休日のスピナーとのズレを正す
            if(up.isChecked()){ //休日上りはスピナー+100
                getLoaderManager().initLoader(position+100, null, callbacks);
            }else{
                switch (position+118){
                    case 133:
                        if(down1.isChecked()){
                            getLoaderManager().initLoader(TimeTableContract.KS16D1, null, callbacks);
                        }else{
                            getLoaderManager().initLoader(TimeTableContract.KS16D2, null, callbacks);
                        }
                        break;
                    case 134:
                        getLoaderManager().initLoader(TimeTableContract.KS17D, null, callbacks);
                        break;
                    case 135:
                        getLoaderManager().initLoader(TimeTableContract.KS18D, null, callbacks);
                        break;
                    default:
                        getLoaderManager().initLoader(position+118, null, callbacks);
                        break;
                }
            }
        }
    }



    /**
     * スピナーに値を登録
     *
     * @param spAdapter
     */
    private void setSpinnerAdapter(ArrayAdapter<String> spAdapter) {
        spAdapter.add("千城台駅");
        spAdapter.add("千城台北駅");
        spAdapter.add("小倉台駅");
        spAdapter.add("桜木駅");
        spAdapter.add("都賀駅");
        spAdapter.add("みつわ台駅");
        spAdapter.add("動物公園駅");
        spAdapter.add("スポーツセンター駅");
        spAdapter.add("穴川駅");
        spAdapter.add("天台駅");
        spAdapter.add("作草部駅");
        spAdapter.add("千葉公園駅");
        spAdapter.add("県庁前駅");
        spAdapter.add("葭川公園駅");
        spAdapter.add("栄町駅");
        spAdapter.add("千葉駅");
        spAdapter.add("市役所前駅");
        spAdapter.add("千葉みなと駅");
    }

    /**
     * オプションメニュー作成時
     * @param menu メニューオブジェクト
     * @return イベント通知
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    /**
     * オプションメニュークリック時
     * @param item クリックされたアイテム
     * @return イベント通知
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return Menus.actionMenu(this,item);
    }
}
