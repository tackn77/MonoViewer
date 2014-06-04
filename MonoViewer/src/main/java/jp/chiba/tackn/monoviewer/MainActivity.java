package jp.chiba.tackn.monoviewer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.maps.GoogleMap;

import jp.chiba.tackn.monoviewer.map.MapsActivity;
import jp.chiba.tackn.monoviewer.map.MonoViewFragment;
import jp.chiba.tackn.monoviewer.map.Station;
import jp.chiba.tackn.monoviewer.train.TrainTableFragment;

/**
 * 時刻表アプリの起動画面
 * 本日の運行情報を表示
 * 列車ダイヤと時刻表を開くことが出来るランチャ
 * @author Takumi Ito
 * @since 2014/05/14
 */
public class MainActivity extends Activity
        implements View.OnClickListener
                    ,AdapterView.OnItemSelectedListener{

    /** デバッグ用タグ */
    private static final String TAG = "MainActivity";
    /** デバッグ用フラグ */
    private static final boolean DEBUG = false;

    /** 時刻表を開くボタン */
    private Button button;
    /** FragmentのLoadManagerに通知 */
    private LoaderManager.LoaderCallbacks callbacks;
    /** LoaderManager.LoaderCallbacksの為 */
    public TrainTableFragment train_table;

    /** 時刻表(TableNo.)選択用スピナー */
    private Spinner trainNo;
    /** スピナーの選択を保持 */
    private int selectSpinner=0;

    /** ラジオボタンのグループ */
    private RadioGroup radioGroup;
    /** 休日のラジオボタン */
    private RadioButton holiday;
    /** 平日のラジオボタン */
    private RadioButton weekday;

    /** スピナー通知用 */
    private AsyncHttpRequest task;
    /** タブレットモードの保持 */
    private TabletHolder tabletHolder = TabletHolder.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //要素の取得
        findViews();

        //buttonがあるかどうかでタブレット判定
        tabletHolder.isTablet(button==null);

        //SyncTaskでherokuから情報取得
        Uri.Builder builder = new Uri.Builder();
        task = new AsyncHttpRequest(this,callbacks);
        task.execute(builder);

        if(tabletHolder.isTablet()){
            //スピナー用設定
            final ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
            setSpinnerAdapter(spAdapter);
            trainNo.setAdapter(spAdapter);
            trainNo.setOnItemSelectedListener(this);

            //ラジオボタン用設定
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    task.initLoader(selectSpinner,checkedId==R.id.holiday);
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
            if(fromIntent1== Station.HOLIDAY){
                holiday.setChecked(true);
                weekday.setChecked(false);
            }
            if(fromIntent1==Station.WEEKDAY){
                holiday.setChecked(false);
                weekday.setChecked(true);
            }
            trainNo.setSelection(position);
        }else{
            button.setOnClickListener(this);
        }
    }

    /**
     * 要素の取得
     */
    private void findViews() {
        button = (Button) findViewById(R.id.button);
        trainNo = (Spinner) findViewById(R.id.Spinner);
        FragmentManager fragmentManager = getFragmentManager();
        train_table = (TrainTableFragment)fragmentManager.findFragmentById(R.id.traintablelist);
        callbacks =  train_table;//LoaderManager.LoaderCallbacks
        radioGroup=(RadioGroup) findViewById(R.id.radiogroup);
        holiday=(RadioButton)findViewById(R.id.holiday);
        weekday=(RadioButton)findViewById(R.id.weekday);
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
     * ボタンを押したときの動作
     * @param view クリックされたオブジェクト
     */
    @Override
    public void onClick(View view) {
        if(view==button) {
            //マップを開く
            Intent intent = new Intent(this, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(tabletHolder.isTablet()){
            getMenuInflater().inflate(R.menu.tablet,menu);
        }else{
            getMenuInflater().inflate(R.menu.main,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return Menus.actionMenu(this,item);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == trainNo) {
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
                task.initLoader(position,holiday.isChecked());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
