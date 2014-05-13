package jp.chiba.tackn.monoviewer;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

/**
 * SQLiteに格納済みの時刻表データの表示を行う
 *
 * @author Takumi Ito
 */
public class TimeTable extends Activity
        implements LoaderManager.LoaderCallbacks,
        AdapterView.OnItemSelectedListener{

    /** デバッグフラグ*/
    private static final boolean DEBUG = false;
    /** デバッグタグ */
    private static final String TAG = "TimeTable";

    /** プロバイダからのデータとListの仲立ち */
    private SimpleCursorAdapter mAdapter;
    /** 時刻表表示用リスト */
    private ListView itemListView;
    /** 時刻表(STATION)選択用スピナー */
    private Spinner trainNo;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_table);
        findViews();

        mAdapter = new TimeTblCursorAdapter(
                this,
                R.layout.list_item_train_no,
                null,
                new String[]{TrainTblContract.TIME, TrainTblContract.STATION},
                new int[]{R.id.train_no_minute, R.id.train_no_station},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // Bind to our new adapter.
        itemListView.setAdapter(mAdapter);
        itemListView.setFastScrollEnabled(true);


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
        trainNo.setAdapter(spAdapter);
        trainNo.setOnItemSelectedListener(this);

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

        trainNo.setSelection(position);
//        //クエリーハンドラ
//        mQueryHandler = new MyAsyncQueryHandler(this.getContentResolver());
    }

    /**
     * getLoaderManager().initLoader()で呼び出される
     *
     * @param id 呼び出しID
     * @param args 呼び出し引数
     * @return プロバイダから取得したデータ
     */
    public Loader onCreateLoader(int id, Bundle args) {
        if (DEBUG) {
            Log.d(TAG, "id: " + id);
//            Log.d(TAG, "Bundle: " + (args==null));
        }
        /** 取得データのソート */
        String orderBy = "TIMES ASC";

        switch (id) {//スピナーで選択したデータ毎にプロバイダの呼び出しURIを変更
            case 0:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS1U, null, null, null, orderBy);
            case 1:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS2U, null, null, null, orderBy);
            case 2:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS3U, null, null, null, orderBy);
            case 3:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS4U, null, null, null, orderBy);
            case 4:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS5U, null, null, null, orderBy);
            case 5:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS6U, null, null, null, orderBy);
            case 6:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS7U, null, null, null, orderBy);
            case 7:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS8U, null, null, null, orderBy);
            case 8:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS9U, null, null, null, orderBy);
            case 9:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS10U, null, null, null, orderBy);
            case 10:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS11U, null, null, null, orderBy);
            case 11:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS12U, null, null, null, orderBy);
            case 12:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS13U, null, null, null, orderBy);
            case 13:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS14U, null, null, null, orderBy);
            case 14:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS15U, null, null, null, orderBy);
            case 15:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS16U, null, null, null, orderBy);
            case 16:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS17U, null, null, null, orderBy);
            case 17:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS2D, null, null, null, orderBy);
            case 18:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS3D, null, null, null, orderBy);
            case 19:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS4D, null, null, null, orderBy);
            case 20:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS5D, null, null, null, orderBy);
            case 21:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS6D, null, null, null, orderBy);
            case 22:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS7D, null, null, null, orderBy);
            case 23:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS8D, null, null, null, orderBy);
            case 24:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS9D, null, null, null, orderBy);
            case 25:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS10D, null, null, null, orderBy);
            case 26:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS11D, null, null, null, orderBy);
            case 27:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS12D, null, null, null, orderBy);
            case 28:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS14D, null, null, null, orderBy);
            case 29:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS15D, null, null, null, orderBy);
            case 30:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS16D1, null, null, null, orderBy);
            case 31:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS16D2, null, null, null, orderBy);
            case 32:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS17D, null, null, null, orderBy);
            case 33:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_HS18D, null, null, null, orderBy);
            case 34:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS1U, null, null, null, orderBy);
            case 35:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS2U, null, null, null, orderBy);
            case 36:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS3U, null, null, null, orderBy);
            case 37:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS4U, null, null, null, orderBy);
            case 38:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS5U, null, null, null, orderBy);
            case 39:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS6U, null, null, null, orderBy);
            case 40:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS7U, null, null, null, orderBy);
            case 41:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS8U, null, null, null, orderBy);
            case 42:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS9U, null, null, null, orderBy);
            case 43:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS10U, null, null, null, orderBy);
            case 44:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS11U, null, null, null, orderBy);
            case 45:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS12U, null, null, null, orderBy);
            case 46:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS13U, null, null, null, orderBy);
            case 47:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS14U, null, null, null, orderBy);
            case 48:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS15U, null, null, null, orderBy);
            case 49:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS16U, null, null, null, orderBy);
            case 50:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS17U, null, null, null, orderBy);
            case 51:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS2D, null, null, null, orderBy);
            case 52:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS3D, null, null, null, orderBy);
            case 53:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS4D, null, null, null, orderBy);
            case 54:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS5D, null, null, null, orderBy);
            case 55:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS6D, null, null, null, orderBy);
            case 56:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS7D, null, null, null, orderBy);
            case 57:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS8D, null, null, null, orderBy);
            case 58:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS9D, null, null, null, orderBy);
            case 59:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS10D, null, null, null, orderBy);
            case 60:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS11D, null, null, null, orderBy);
            case 61:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS12D, null, null, null, orderBy);
            case 62:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS14D, null, null, null, orderBy);
            case 63:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS15D, null, null, null, orderBy);
            case 64:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS16D1, null, null, null, orderBy);
            case 65:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS16D2, null, null, null, orderBy);
            case 66:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS17D, null, null, null, orderBy);
            case 67:
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_KS18D, null, null, null, orderBy);
            default:
                if (DEBUG) {
                    Log.d(TAG, "id:" + id);
                }
                return new CursorLoader(this, TrainTblContract.CONTENT_URI_H1, null, null, null, orderBy);
        }
    }

    /**
     * データ読み込み時の処理 取得したデータをアダプタに交換する
     *
     * @param loader 取得したローダ
     * @param data 表示するCursorデータ
     */
    public void onLoadFinished(Loader loader, Object data) {
        mAdapter.swapCursor((Cursor) data);
    }

    /**
     * ロードがリセットされた時の処理
     *
     * @param loader ローダー
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
     *
     * @param parent アダプタを登録しているView(スピナー)オブジェクト
     * @param view 選択されたview
     * @param position 選択した位置
     * @param id 選択したID
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
     *
     * @param parent アダプタを登録しているView(スピナー)オブジェクト
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
}
