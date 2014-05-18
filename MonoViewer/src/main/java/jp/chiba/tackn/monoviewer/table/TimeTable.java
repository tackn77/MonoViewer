package jp.chiba.tackn.monoviewer.table;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.chiba.tackn.monoviewer.MainActivity;
import jp.chiba.tackn.monoviewer.R;
import jp.chiba.tackn.monoviewer.data.SQLTblContract;
import jp.chiba.tackn.monoviewer.man.DisclaimerActivity;
import jp.chiba.tackn.monoviewer.map.InformationHolder;

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

        int position =0 ;
        for(int i=0;i<spAdapter.getCount();i++){
            String item = spAdapter.getItem(i);
            if(fromIntent2==0 && item.startsWith("休日") || fromIntent2==1 && item.startsWith("平日")){
                if(fromIntent1==1 && item.endsWith("下り") || fromIntent1==0 && item.endsWith("上り"))  {
                    if(item.indexOf(fromIntent0)>0) {
                        position = i;
                        break;
                    }
                }
            }
        }
        spinner.setSelection(position);
    }



    /**
     * ロードがリセットされた時の処理
     *
     * @param loader ローダー
     */
    public void onLoaderReset(Loader loader) {
    }

    /**
     * onCreate()時にレイアウト済みオブジェクトの取得
     */
    private void findViews() {
        spinner = (Spinner) findViewById(R.id.Spinner);
        FragmentManager fragmentManager = getFragmentManager();
        time_table = (TimeTableFragment)fragmentManager.findFragmentById(R.id.timetablelist);
        callbacks =  (LoaderManager.LoaderCallbacks)time_table;
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
            getLoaderManager().initLoader(position, null, callbacks);
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
        getLoaderManager().initLoader(0, null, callbacks);
    }

    /**
     * スピナーに値を登録
     *
     * @param spAdapter
     */
    private void setSpinnerAdapter(ArrayAdapter<String> spAdapter) {
        spAdapter.add("平日 千城台駅 上り");
        spAdapter.add("平日 千城台北駅 上り");
        spAdapter.add("平日 小倉台駅 上り");
        spAdapter.add("平日 桜木駅 上り");
        spAdapter.add("平日 都賀駅 上り");
        spAdapter.add("平日 みつわ台駅 上り");
        spAdapter.add("平日 動物公園駅 上り");
        spAdapter.add("平日 スポーツセンター駅 上り");
        spAdapter.add("平日 穴川駅 上り");
        spAdapter.add("平日 天台駅 上り");
        spAdapter.add("平日 作草部駅 上り");
        spAdapter.add("平日 千葉公園駅 上り");
        spAdapter.add("平日 県庁前駅 上り");
        spAdapter.add("平日 葭川公園駅 上り");
        spAdapter.add("平日 栄町駅 上り");
        spAdapter.add("平日 千葉駅 上り");
        spAdapter.add("平日 市役所前駅 上り");

        spAdapter.add("平日 千城台北駅 下り");
        spAdapter.add("平日 小倉台駅 下り");
        spAdapter.add("平日 桜木駅 下り");
        spAdapter.add("平日 都賀駅 下り");
        spAdapter.add("平日 みつわ台駅 下り");
        spAdapter.add("平日 動物公園駅 下り");
        spAdapter.add("平日 スポーツセンター駅 下り");
        spAdapter.add("平日 穴川駅 下り");
        spAdapter.add("平日 天台駅 下り");
        spAdapter.add("平日 作草部駅 下り");
        spAdapter.add("平日 千葉公園駅 下り");
        spAdapter.add("平日 葭川公園駅 下り");
        spAdapter.add("平日 栄町駅 下り");
        spAdapter.add("平日 千葉駅1号線 下り");
        spAdapter.add("平日 千葉駅2号線 下り");
        spAdapter.add("平日 市役所前駅 下り");
        spAdapter.add("平日 千葉みなと駅 下り");

        spAdapter.add("休日 千城台駅 上り");
        spAdapter.add("休日 千城台北駅 上り");
        spAdapter.add("休日 小倉台駅 上り");
        spAdapter.add("休日 桜木駅 上り");
        spAdapter.add("休日 都賀駅 上り");
        spAdapter.add("休日 みつわ台駅 上り");
        spAdapter.add("休日 動物公園駅 上り");
        spAdapter.add("休日 スポーツセンター駅 上り");
        spAdapter.add("休日 穴川駅 上り");
        spAdapter.add("休日 天台駅 上り");
        spAdapter.add("休日 作草部駅 上り");
        spAdapter.add("休日 千葉公園駅 上り");
        spAdapter.add("休日 県庁前駅 上り");
        spAdapter.add("休日 葭川公園駅 上り");
        spAdapter.add("休日 栄町駅 上り");
        spAdapter.add("休日 千葉駅 上り");
        spAdapter.add("休日 市役所前駅 上り");

        spAdapter.add("休日 千城台北駅 下り");
        spAdapter.add("休日 小倉台駅 下り");
        spAdapter.add("休日 桜木駅 下り");
        spAdapter.add("休日 都賀駅 下り");
        spAdapter.add("休日 みつわ台駅 下り");
        spAdapter.add("休日 動物公園駅 下り");
        spAdapter.add("休日 スポーツセンター駅 下り");
        spAdapter.add("休日 穴川駅 下り");
        spAdapter.add("休日 天台駅 下り");
        spAdapter.add("休日 作草部駅 下り");
        spAdapter.add("休日 千葉公園駅 下り");
        spAdapter.add("休日 葭川公園駅 下り");
        spAdapter.add("休日 栄町駅 下り");
        spAdapter.add("休日 千葉駅1号線 下り");
        spAdapter.add("休日 千葉駅2号線 下り");
        spAdapter.add("休日 市役所前駅 下り");
        spAdapter.add("休日 千葉みなと駅 下り");
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
