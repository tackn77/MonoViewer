package jp.chiba.tackn.monoviewer.table;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.Calendar;

import jp.chiba.tackn.monoviewer.MainActivity;
import jp.chiba.tackn.monoviewer.R;
import jp.chiba.tackn.monoviewer.data.SQLTblContract;

/**
 * SQLiteに格納済みの時刻表データの表示を行う
 * @author Takumi Ito
 * @since 2014/05/12.
 */
public class TrainTable extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemSelectedListener{

    /** デバッグフラグ */
    private static final boolean DEBUG = false;
    /** デバッグタグ */
    private static final String TAG = "TrainTable";

    /** プロバイダからのデータとListの仲立ち */
    private SimpleCursorAdapter mAdapter;
    /** 時刻表表示用リスト */
    private ListView itemListView;
    /** 時刻表(TableNo.)選択用スピナー */
    private Spinner trainNo;
    /** 画面回転時にonCreateを起動させるための引数一時保存 */
    private Bundle savedInstanceState;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_table);
        //ビューの取得
        findViews();

        //リスト用設定
        mAdapter = new TrainTblCursorAdapter(
                this,
                R.layout.list_item_train_no,
                null,
                new String[]{SQLTblContract.COLUMN_TIME, SQLTblContract.COLUMN_STATION},
                new int[]{R.id.train_no_minute, R.id.train_no_station},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        itemListView.setAdapter(mAdapter);
        itemListView.setFastScrollEnabled(true);

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

        this.savedInstanceState = savedInstanceState;
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
                onCreate(savedInstanceState);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                // "横方向";
                onCreate(savedInstanceState);
                break;
            default :
                // "デフォルト";
        }
    }

    /**
     * データ読み込み時の処理
     * 取得したデータをアダプタに交換する
     * @param loader 取得したローダ
     * @param cursor 表示するCursorデータ
     */
    public void onLoadFinished(Loader loader, Cursor cursor) {

        mAdapter.swapCursor(cursor);

        int count = 0;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        hour = (hour==0)?24:hour;
        int now = Integer.valueOf(String.format("%1$02d",hour) + String.format("%1$02d",minute));

        if(cursor.moveToFirst()){
            do{
                int time = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_TIME));
                if(now <= time)break;
                count++;
            }while (cursor.moveToNext());

        }
        count = (count==0)?0:count-1;
        //開始位置指定 1行目の空白行の次の行から表示
        itemListView.setSelectionFromTop(count, 0);
    }

    /**
     * ロードがリセットされた時の処理
     * @param loader
     */
    public void onLoaderReset(Loader loader) {
        mAdapter.swapCursor(null);
    }

    /**
     * onCreate()時にレイアウト済みオブジェクトの取得
     */
    private void findViews() {
        itemListView = (ListView) findViewById(R.id.listview);
        trainNo = (Spinner) findViewById(R.id.Spinner);
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
//            Log.d(TAG,"view:" + (parent==trainNo));
            Log.d(TAG, "position:" + position);
        }
        if (parent == trainNo) {
            //Listに紐付けていたプロバイダURIの切替
            getLoaderManager().initLoader(position, null, (LoaderManager.LoaderCallbacks) this);
        }
    }

    /**
     * スピナーで選択が解除された時に呼び出し
     * @param parent
     */
    public void onNothingSelected(AdapterView<?> parent) {
        if (DEBUG) {
            Log.d(TAG, "onNothingSelected :");
        }
        //初期化
        getLoaderManager().initLoader(0, null, (LoaderManager.LoaderCallbacks) this);
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
     *
     * getLoaderManager().initLoader()で呼び出される
     * @param id 呼び出しID
     * @param args 呼び出し引数
     * @return プロバイダから取得したデータ
     */
    public Loader onCreateLoader(int id, Bundle args) {
        if (DEBUG) {
            Log.d(TAG, "id: " + id);
       }
        /** 取得データのソート */
        String orderBy = "TIMES ASC";
        //スピナーで選択したデータ毎にプロバイダの呼び出しURIを変更
        switch (id) {
            case 0:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H1, null, null, null, orderBy);
            case 1:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H2, null, null, null, orderBy);
            case 2:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H3, null, null, null, orderBy);
            case 3:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H4, null, null, null, orderBy);
            case 4:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H5, null, null, null, orderBy);
            case 5:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H6, null, null, null, orderBy);
            case 6:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H7, null, null, null, orderBy);
            case 7:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H11, null, null, null, orderBy);
            case 8:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H12, null, null, null, orderBy);
            case 9:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H21, null, null, null, orderBy);
            case 10:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H22, null, null, null, orderBy);
            case 11:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H25, null, null, null, orderBy);
            case 12:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H26, null, null, null, orderBy);
            case 13:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K1, null, null, null, orderBy);
            case 14:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K2, null, null, null, orderBy);
            case 15:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K3, null, null, null, orderBy);
            case 16:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K4, null, null, null, orderBy);
            case 17:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K5, null, null, null, orderBy);
            case 18:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K6, null, null, null, orderBy);
            case 19:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K11, null, null, null, orderBy);
            case 20:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K12, null, null, null, orderBy);
            case 21:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K21, null, null, null, orderBy);
            case 22:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K22, null, null, null, orderBy);
            case 23:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K25, null, null, null, orderBy);
            case 24:
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_K26, null, null, null, orderBy);
            default:
                if (DEBUG) {
                    Log.d(TAG, "id:" + id);
                }
                return new CursorLoader(this, SQLTblContract.CONTENT_URI_H1, null, null, null, orderBy);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_back_home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            default:
                return true;
        }
        return false;
    }

}
