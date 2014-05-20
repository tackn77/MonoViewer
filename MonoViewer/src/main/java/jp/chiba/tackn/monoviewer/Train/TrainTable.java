package jp.chiba.tackn.monoviewer.train;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.res.Configuration;
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

import jp.chiba.tackn.monoviewer.MainActivity;
import jp.chiba.tackn.monoviewer.Menus;
import jp.chiba.tackn.monoviewer.R;
import jp.chiba.tackn.monoviewer.man.DisclaimerActivity;
import jp.chiba.tackn.monoviewer.map.MapsActivity;

/**
 * SQLiteに格納済みの時刻表データの表示を行う
 * @author Takumi Ito
 * @since 2014/05/12.
 */
public class TrainTable extends Activity
        implements AdapterView.OnItemSelectedListener{

    /** デバッグフラグ */
    private static final boolean DEBUG = false;
    /** デバッグタグ */
    private static final String TAG = "TrainTable";

    /** 時刻表(TableNo.)選択用スピナー */
    private Spinner trainNo;
    /** ラジオボタンのグループ */
    private RadioGroup radioGroup;
    /** 休日のラジオボタン */
    private RadioButton holiday;
    /** 平日のラジオボタン */
    private RadioButton weekday;
    /** スピナーの選択を保持 */
    private int selectSpinner=0;

    /** FragmentのLoadManagerに通知 */
    private LoaderManager.LoaderCallbacks callbacks;
    /** LoaderManager.LoaderCallbacksの為 */
    public TrainTableFragment train_table;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_table);
        //ビューの取得
        findViews();

        //スピナー用設定
        final ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        setSpinnerAdapter(spAdapter);
        trainNo.setAdapter(spAdapter);
        trainNo.setOnItemSelectedListener(this);

        //ラジオボタン用設定
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                initLoader(selectSpinner);
//                trainNo.setSelection(selectSpinner);
            }
        });


        //外部から起動された場合のスピナーの選択処理
        int fromIntent0 = 0;
        int fromIntent1 = 0;
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            fromIntent0 = extras.getInt("TableNo");
            fromIntent1 = extras.getInt("holiday");
        }
        if(DEBUG){
            Log.d(TAG, "fromIntent trainNo: " + fromIntent0);
            Log.d(TAG,"fromIntent holiday: " + fromIntent1);
        }
        int position =0 ;
        for(int i=0;i<13;i++){
            String adapter = spAdapter.getItem(i);
            if(adapter.equals(String.valueOf(fromIntent0))){
                position=i;
            }
        }
        if(fromIntent1==0){
            holiday.setChecked(true);
            weekday.setChecked(false);
        }
        if(fromIntent1==1){
            holiday.setChecked(false);
            weekday.setChecked(true);
        }
        trainNo.setSelection(position);
        if(DEBUG)Log.d(TAG,"position() " + position);
    }


    /**
     * 画面回転時のActivityの破棄を回避するため
     * AndroidManifestにて有効化
     *
     * @param newConfig 画面回転などのフラグ
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //画面方向の検出
        switch(newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                // "縦方向";
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                // "横方向";
                break;
            default :
                // "デフォルト";
        }
    }

    /**
     * onCreate()時にレイアウト済みオブジェクトの取得
     */
    private void findViews() {
        trainNo = (Spinner) findViewById(R.id.Spinner);
        radioGroup=(RadioGroup) findViewById(R.id.radiogroup);
        holiday=(RadioButton)radioGroup.findViewById(R.id.holiday);
        weekday=(RadioButton)radioGroup.findViewById(R.id.weekday);

        FragmentManager fragmentManager = getFragmentManager();
        train_table = (TrainTableFragment)fragmentManager.findFragmentById(R.id.traintablelist);
        callbacks =  (LoaderManager.LoaderCallbacks)train_table;
    }

    /**
     * スピナーで選択した際に呼び出し
     * @param parent アダプタを登録しているView(スピナー)オブジェクト
     * @param view 選択されたView
     * @param position 選択した位置
     * @param id 選択したレコードのID
     */
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (DEBUG) {
            Log.d(TAG, "position:" + position);
        }
        selectSpinner = position;
        //KA7は存在しない為
        if(position==6){
            weekday.setChecked(true);
            holiday.setChecked(false);
            holiday.setEnabled(false);
        }else{
            holiday.setEnabled(true);
        }
        if (parent == trainNo) {
            //Listに紐付けていたプロバイダURIの切替
            initLoader(position);
        }
    }

    /**
     * スピナーで選択が解除された時に呼び出し
     * @param parent スピナー
     */
    public void onNothingSelected(AdapterView<?> parent) {
        if (DEBUG) {
            Log.d(TAG, "onNothingSelected :");
        }
        //初期化
        initLoader(0);
    }

    /**
     * スピナーで選択したときのCursolLoaderの初期化
     * @param position Loaderの初期化時の通知パラメータ
     */
    private void initLoader(int position){
//        position = (radioGroup.getCheckedRadioButtonId()==R.id.weekday)?position:position+trainNo.getAdapter().getCount();
        if(weekday.isChecked()){ //平日はスピナー通り
            getLoaderManager().initLoader(position, null, callbacks);
        }else { //休日のスピナーとのズレを正す
            switch (position) {
                case 0:
                    getLoaderManager().restartLoader(13, null, callbacks);
                    break;
                case 1:
                    getLoaderManager().restartLoader(14, null, callbacks);
                    break;
                case 2:
                    getLoaderManager().restartLoader(15, null, callbacks);
                    break;
                case 3:
                    getLoaderManager().restartLoader(16, null, callbacks);
                    break;
                case 4:
                    getLoaderManager().restartLoader(17, null, callbacks);
                    break;
                case 5:
                    getLoaderManager().restartLoader(18, null, callbacks);
                    break;
                case 6:
                    break; //存在しない
                case 7:
                    getLoaderManager().restartLoader(19, null, callbacks);
                    break;
                case 8:
                    getLoaderManager().restartLoader(20, null, callbacks);
                    break;
                case 9:
                    getLoaderManager().restartLoader(21, null, callbacks);
                    break;
                case 10:
                    getLoaderManager().restartLoader(22, null, callbacks);
                    break;
                case 11:
                    getLoaderManager().restartLoader(23, null, callbacks);
                    break;
                case 12:
                    getLoaderManager().restartLoader(24, null, callbacks);
                    break;
                default:
                    if (DEBUG) Log.d(TAG, "position() " + position);
                    break; //存在しない
            }
        }
    }

    /**
     * スピナーに値を登録
     * onCreateLoaderで呼び出し順に並べてある
     * @param spAdapter 車両選択用スピナー
     */
    private void setSpinnerAdapter(ArrayAdapter<String> spAdapter) {
        spAdapter.add("1");
        spAdapter.add("2");
        spAdapter.add("3");
        spAdapter.add("4");
        spAdapter.add("5");
        spAdapter.add("6");
        spAdapter.add("7");
        spAdapter.add("11");
        spAdapter.add("12");
        spAdapter.add("21");
        spAdapter.add("22");
        spAdapter.add("25");
        spAdapter.add("26");
    }

    /**
     * オプションメニュー作成
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
     * オプションメニューの選択
     * @param item 選択されたアイテム
     * @return イベント通知
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return Menus.actionMenu(this,item);
    }
}
