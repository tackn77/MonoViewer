package jp.chiba.tackn.monoviewer.table;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;

import jp.chiba.tackn.monoviewer.R;

import jp.chiba.tackn.monoviewer.data.SQLTblContract;

/**
 * 列車運行時刻表を表示するフラグメント
 * @author Takumi Ito
 * @since 2014/05/19
 */
public class TrainTableFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    /** デバッグフラグ */
    private static final boolean DEBUG = true;
    /** デバッグタグ */
    private static final String TAG = "TrainTableFragment";

    /** プロバイダからのデータとListの仲立ち */
    private SimpleCursorAdapter mAdapter;

    /** 時刻表表示用リスト */
    private AbsListView itemListView;

    /** 呼び出し元コンテキスト */
    private Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrainTableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         *  レイアウトファイル読み込み
         *  valuesフォルダで画面サイズによってレイアウトの変更が可能
         *  現在は7インチポートレートまではシングル
         *  7インチランドスケープ以上は2ラインで表示
         */
        View view = inflater.inflate(R.layout.fragment_traintablefragment, container, false);
        itemListView = (AbsListView) view.findViewById(android.R.id.list);
        //リスト用設定
        mAdapter = new TrainTblCursorAdapter(
                context,
                R.layout.list_item_train_no,
                null,
                new String[]{SQLTblContract.COLUMN_TIME, SQLTblContract.COLUMN_STATION},
                new int[]{R.id.train_no_minute, R.id.train_no_station},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        itemListView.setAdapter(mAdapter);
        itemListView.setFastScrollEnabled(true);

        return view;
    }

    /**
     * Activityに呼ばれた時
     * @param activity 呼び出し元Activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    /**
     * Activityとの接続が切れたとき
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = itemListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
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
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H1, null, null, null, orderBy);
            case 1:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H2, null, null, null, orderBy);
            case 2:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H3, null, null, null, orderBy);
            case 3:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H4, null, null, null, orderBy);
            case 4:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H5, null, null, null, orderBy);
            case 5:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H6, null, null, null, orderBy);
            case 6:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H7, null, null, null, orderBy);
            case 7:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H11, null, null, null, orderBy);
            case 8:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H12, null, null, null, orderBy);
            case 9:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H21, null, null, null, orderBy);
            case 10:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H22, null, null, null, orderBy);
            case 11:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H25, null, null, null, orderBy);
            case 12:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H26, null, null, null, orderBy);
            case 13:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K1, null, null, null, orderBy);
            case 14:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K2, null, null, null, orderBy);
            case 15:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K3, null, null, null, orderBy);
            case 16:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K4, null, null, null, orderBy);
            case 17:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K5, null, null, null, orderBy);
            case 18:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K6, null, null, null, orderBy);
            case 19:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K11, null, null, null, orderBy);
            case 20:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K12, null, null, null, orderBy);
            case 21:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K21, null, null, null, orderBy);
            case 22:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K22, null, null, null, orderBy);
            case 23:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K25, null, null, null, orderBy);
            case 24:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_K26, null, null, null, orderBy);
            default:
                if (DEBUG) {
                    Log.d(TAG, "id:" + id);
                }
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H1, null, null, null, orderBy);
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
//        itemListView.setSelectionFromTop(count, 0);
        itemListView.setSelection(count);
        //TODO うまく動いているか、要確認
    }

    /**
     * ロードがリセットされた時の処理
     * @param loader
     */
    public void onLoaderReset(Loader loader) {
        mAdapter.swapCursor(null);
    }
}
