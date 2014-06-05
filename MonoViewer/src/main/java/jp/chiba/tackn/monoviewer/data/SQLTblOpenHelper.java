package jp.chiba.tackn.monoviewer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 時刻表SQLiteを取り扱うヘルパー
 *
 * @author Takumi Ito
 * @since 2014/05/12.
 */
class SQLTblOpenHelper extends SQLiteOpenHelper {
    /**
     * デバッグフラグ
     */
    private static final boolean DEBUG = false;
    /**
     * デバッグタグ
     */
    private static final String TAG = "TrainTblOpenHelper";

    /**
     * DB名
     */
    static final String DATABASE_NAME = "TimeTable";

    /**
     * SQLiteのDBバージョン
     */
    static final int DB_VERSION = 7;
    /**
     * SQLiteのテーブル名
     */
    static String TBL_NAME = "TimeTable";

    /**
     * コンストラクタ
     *
     * @param context アプリケーションコンテキスト
     */
    public SQLTblOpenHelper(Context context) {
        super(context, TBL_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    /**
     * DBバージョンが上がった際の作業
     * @param db アプリで利用するDB
     * @param oldVersion 古いバージョン
     * @param newVersion 新しいバージョン
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
