package jp.chiba.tackn.monoviewer;

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
public class TrainTblOpenHelper extends SQLiteOpenHelper {
    /** デバッグフラグ */
    private static final boolean DEBUG = false;
    /** デバッグタグ */
    private static final String TAG = "TrainTblOpenHelper";

    /** assetsフォルダにあるdbのファイル名 */
    private static final String SRC_DATABASE_NAME = "sampletest.sqlite3";
    /** コピー先のDB名 */
    static final String DATABASE_NAME = "TimeTable";

    /** アプリケーションコンテキスト */
    private final Context context;
    /** 書き換え先のデータベースパス */
    private final File databasePath;
    /** データベースコピーフラグ */
    private boolean createDatabase = false;

    /** SQLiteのDBバージョン */
    private static final int DB_VERSION = 1;
    /** SQLiteのテーブル名 */
    static String TBL_NAME = "TimeTable";

    /**
     * コンストラクタ
     * @param context アプリケーションコンテキスト
     */
    TrainTblOpenHelper(Context context) {
        super(context, TBL_NAME, null, DB_VERSION);

        this.context = context;
        this.databasePath = context.getDatabasePath(DATABASE_NAME);
    }

    /**
     * DBオブジェクトの取得
     * assertフォルダからコピーを行う
     * @return assetsフォルダからコピーしたDB
     */
    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase database = super.getWritableDatabase();
        if (DEBUG) Log.d(TAG, "getWritableDatabase");
        if (DEBUG) Log.d(TAG, "createDatabase: " + createDatabase);

        if (createDatabase) {
            try {
                if (DEBUG) Log.d(TAG, "copyDatabase before");
                database = copyDatabase(database);
                if (DEBUG)Log.d(TAG, "copyDatabase after");
            } catch (IOException e) {
                //Log.wtf(TAG, e);
                return null;
            }
        }

        return database;
    }

    /**
     * データベースのコピーを行う
     * @param database コピー先DB
     * @return コピー済みのDBを開いたオブジェクト
     * @throws IOException ファイルコピー時の例外
     */
    private SQLiteDatabase copyDatabase(SQLiteDatabase database) throws IOException {
        // 開いていいるDBをいったん閉じる
        database.close();

        // assetsフォルダからコピー
        InputStream input = context.getAssets().open(SRC_DATABASE_NAME);
        OutputStream output = new FileOutputStream(databasePath);
        copy(input, output);

        createDatabase = false;
        // コピーが終ったので開きなおす
        return super.getWritableDatabase();
    }

    /**
     * 初回起動時のDB作成
     * フラグを立て、getWritableDatabase()の時に上書きコピーする
     * @param db アプリで利用するDB
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        if (DEBUG) Log.d(TAG, "onCreate bofre");
        super.onOpen(db);
        this.createDatabase = true;
        if (DEBUG) Log.d(TAG, "onCreate after");
    }

    /**
     * DBバージョンが上がった際の作業
     * //TODO 未対応
     * @param db アプリで利用するDB
     * @param oldVersion 古いバージョン
     * @param newVersion 新しいバージョン
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * DBファイルのファイルコピー
     * @param input コピー元
     * @param output コピー先
     * @return コピーバイト数
     * @throws IOException ファイルコピー時例外
     */
    private static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
