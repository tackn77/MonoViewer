package jp.chiba.tackn.monoviewer.table;

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
import android.widget.Spinner;

import jp.chiba.tackn.monoviewer.MainActivity;
import jp.chiba.tackn.monoviewer.R;
import jp.chiba.tackn.monoviewer.man.DisclaimerActivity;

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
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        setSpinnerAdapter(spAdapter);
        trainNo.setAdapter(spAdapter);
        trainNo.setOnItemSelectedListener(this);


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
        for(int i=0;i<spAdapter.getCount();i++){
            String adapter = spAdapter.getItem(i);
            if(fromIntent1==0 &&  adapter.equals("休日"+fromIntent0) || fromIntent1==1 && adapter.equals("平日"+fromIntent0)){
                position=i;
            }
        }
        trainNo.setSelection(position);
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
        getLoaderManager().initLoader(position, null, callbacks);
    }

    /**
     * スピナーに値を登録
     * onCreateLoaderで呼び出し順に並べてある
     * @param spAdapter 車両選択用スピナー
     */
    private void setSpinnerAdapter(ArrayAdapter<String> spAdapter) {
        spAdapter.add("平日1");
        spAdapter.add("平日2");
        spAdapter.add("平日3");
        spAdapter.add("平日4");
        spAdapter.add("平日5");
        spAdapter.add("平日6");
        spAdapter.add("平日7");
        spAdapter.add("平日11");
        spAdapter.add("平日12");
        spAdapter.add("平日21");
        spAdapter.add("平日22");
        spAdapter.add("平日25");
        spAdapter.add("平日26");
        spAdapter.add("休日1");
        spAdapter.add("休日2");
        spAdapter.add("休日3");
        spAdapter.add("休日4");
        spAdapter.add("休日5");
        spAdapter.add("休日6");
        spAdapter.add("休日11");
        spAdapter.add("休日12");
        spAdapter.add("休日21");
        spAdapter.add("休日22");
        spAdapter.add("休日25");
        spAdapter.add("休日26");
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
        switch (item.getItemId()){
            case R.id.action_back_home:
                Intent home = new Intent(this,MainActivity.class);
                startActivity(home);
                break;
            case R.id.action_disclaimer:
                Intent disclaimer = new Intent(this,DisclaimerActivity.class);
                startActivity(disclaimer);
                break;
            default:
                return true;
        }
        return false;
    }
}
