package jp.chiba.tackn.monoviewer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * assetsフォルダからDataBaseをコピーする
 * @author Takumi Ito
 * @since 2014/06/05
 */
public class SQLTblCopy {
    /**
     * デバッグフラグ
     *
     */
    private static final boolean DEBUG = true;
    /**
     * デバッグタグ
     */
    private static final String TAG = "SQLTblCopy";

    /**
     * assetsフォルダにあるdbのファイル名
     */
    private static final String SRC_DATABASE_NAME = "sampletest.sqlite3";

    /**
     * データベースのコピーを行う
     *
     * @param context アプリケーションコンテキスト
     * @throws java.io.IOException ファイルコピー時の例外
     */
    public synchronized static void copyDatabase(Context context) throws IOException {
        //DB格納用フォルダの準備
        new OpenHelper(context).getWritableDatabase();

        //DB格納用ファイルパスの準備
        File databasePath = context.getDatabasePath(SQLTblOpenHelper.DATABASE_NAME);

        // assetsフォルダからコピー
        InputStream input = context.getAssets().open(SRC_DATABASE_NAME);
        OutputStream output = new FileOutputStream(databasePath);
        copy(input, output);
    }

    /**
     * DBファイルのファイルコピー
     *
     * @param input  コピー元
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

    /**
     * DB格納用フォルダ作成用
     */
    private static class OpenHelper extends SQLiteOpenHelper{
        public OpenHelper(Context context) {
            super(context, SQLTblOpenHelper.DATABASE_NAME, null, SQLTblOpenHelper.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        }
    }
}
