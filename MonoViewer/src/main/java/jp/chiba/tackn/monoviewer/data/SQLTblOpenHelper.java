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
     * assetsフォルダにあるdbのファイル名
     */
    private static final String SRC_DATABASE_NAME = "sampletest.sqlite3";
    /**
     * コピー先のDB名
     */
    static final String DATABASE_NAME = "TimeTable";

    /**
     * アプリケーションコンテキスト
     */
    private final Context context;
    /**
     * 書き換え先のデータベースパス
     */
    private final File databasePath;
    /**
     * データベースコピーフラグ
     */
    private boolean createDatabase = false;

    /**
     * SQLiteのDBバージョン
     */
    private static final int DB_VERSION = 7;
    /**
     * SQLiteのテーブル名
     */
    static String TBL_NAME = "TimeTable";

    /**
     * コンストラクタ
     *
     * @param context アプリケーションコンテキスト
     */
    SQLTblOpenHelper(Context context) {
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
        SQLiteDatabase db = super.getWritableDatabase();

        if (createDatabase) {
            try {
                if (DEBUG) Log.d(TAG, "copyDatabase before");
                db = copyDatabase(db);
                if (DEBUG)Log.d(TAG, "copyDatabase after");
            } catch (IOException e) {
                //Log.wtf(TAG, e);
                return null;
            }
        }

        return db;
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * DBオブジェクトの取得
     * assertフォルダからコピーを行う
     * @return assetsフォルダからコピーしたDB
     */
    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = super.getReadableDatabase();
        if (DEBUG) Log.d(TAG, "ReadableDatabase");
        if (DEBUG) Log.d(TAG, "createDatabase: " + createDatabase);

        if (createDatabase) {
            try {
                if (DEBUG) Log.d(TAG, "copyDatabase before");
                db = copyDatabase(db);
                if (DEBUG)Log.d(TAG, "copyDatabase after");
            } catch (IOException e) {
                //Log.wtf(TAG, e);
                return null;
            }
        }

        return db;
    }

    /**
     * データベースのコピーを行う
     * @param db コピー先DB
     * @return コピー済みのDBを開いたオブジェクト
     * @throws IOException ファイルコピー時の例外
     */
    private SQLiteDatabase copyDatabase(SQLiteDatabase db) throws IOException {
        // 開いていいるDBをいったん閉じる
        if(db.isOpen())db.close();

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
     * @param db アプリで利用するDB
     * @param oldVersion 古いバージョン
     * @param newVersion 新しいバージョン
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion!=newVersion) {
            // 開いていいるDBをいったん閉じる
            if (db.isOpen()) db.close();
            //DBファイルを削除
            //noinspection ResultOfMethodCallIgnored
            databasePath.delete();
            //初回コピー時と同じ手順を行う
            onCreate(db);
        }
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
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
