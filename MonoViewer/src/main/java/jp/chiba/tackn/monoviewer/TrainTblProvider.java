package jp.chiba.tackn.monoviewer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

/**
 * 時刻表DBの検索を提供するProvider
 * @author Takumi Ito
 * @since 2014/05/12.
 */
public class TrainTblProvider extends ContentProvider {
    /** デバッグフラグ */
    private static final boolean DEBUG =false;
    /** デバッグタグ */
    private static final String TAG = "TrainTblProvider ";
    /** SQLite3を取り扱うヘルパー */
    private TrainTblOpenHelper mHelper;
    /** URI判定機 */
    private static final UriMatcher mUriMatcher;

    /** URIのスイッチ用ID */

    private static final int TRAIN_K1 = 1;
    private static final int TRAIN_K2 = 2;
    private static final int TRAIN_K3 = 3;
    private static final int TRAIN_K4 = 4;
    private static final int TRAIN_K5 = 5;
    private static final int TRAIN_K6 = 6;
    private static final int TRAIN_K11 = 8;
    private static final int TRAIN_K12 = 9;
    private static final int TRAIN_K21 = 10;
    private static final int TRAIN_K22 = 11;
    private static final int TRAIN_K25 = 12;
    private static final int TRAIN_K26 = 13;

    private static final int TRAIN_H1 = 14;
    private static final int TRAIN_H2 = 15;
    private static final int TRAIN_H3 = 16;
    private static final int TRAIN_H4 = 17;
    private static final int TRAIN_H5 = 18;
    private static final int TRAIN_H6 = 19;
    private static final int TRAIN_H7 = 20;
    private static final int TRAIN_H11 = 21;
    private static final int TRAIN_H12 = 22;
    private static final int TRAIN_H21 = 23;
    private static final int TRAIN_H22 = 24;
    private static final int TRAIN_H25 = 25;
    private static final int TRAIN_H26 = 26;

    private static final int STATION_H1U = 27;
    private static final int STATION_H2U = 28;
    private static final int STATION_H3U = 29;
    private static final int STATION_H4U = 30;
    private static final int STATION_H5U = 31;
    private static final int STATION_H6U = 32;
    private static final int STATION_H7U = 33;
    private static final int STATION_H8U = 34;
    private static final int STATION_H9U = 35;
    private static final int STATION_H10U = 36;
    private static final int STATION_H11U = 37;
    private static final int STATION_H12U = 38;
    private static final int STATION_H13U = 39;
    private static final int STATION_H14U = 40;
    private static final int STATION_H15U = 41;
    private static final int STATION_H16U= 42;
    private static final int STATION_H17U = 43;

    private static final int STATION_H2D = 44;
    private static final int STATION_H3D = 45;
    private static final int STATION_H4D = 46;
    private static final int STATION_H5D = 47;
    private static final int STATION_H6D = 48;
    private static final int STATION_H7D = 49;
    private static final int STATION_H8D = 50;
    private static final int STATION_H9D = 51;
    private static final int STATION_H10D = 52;
    private static final int STATION_H11D = 53;
    private static final int STATION_H12D = 54;
    private static final int STATION_H14D = 55;
    private static final int STATION_H15D = 56;
    private static final int STATION_H16D1= 57;
    private static final int STATION_H16D2= 58;
    private static final int STATION_H17D = 59;
    private static final int STATION_H18D = 60;

    private static final int STATION_K1U = 61;
    private static final int STATION_K2U = 62;
    private static final int STATION_K3U = 63;
    private static final int STATION_K4U = 64;
    private static final int STATION_K5U = 65;
    private static final int STATION_K6U = 66;
    private static final int STATION_K7U = 67;
    private static final int STATION_K8U = 68;
    private static final int STATION_K9U = 69;
    private static final int STATION_K10U = 70;
    private static final int STATION_K11U = 71;
    private static final int STATION_K12U = 72;
    private static final int STATION_K13U = 73;
    private static final int STATION_K14U = 74;
    private static final int STATION_K15U = 75;
    private static final int STATION_K16U= 76;
    private static final int STATION_K17U = 77;

    private static final int STATION_K2D = 78;
    private static final int STATION_K3D = 79;
    private static final int STATION_K4D = 80;
    private static final int STATION_K5D = 81;
    private static final int STATION_K6D = 82;
    private static final int STATION_K7D = 83;
    private static final int STATION_K8D = 84;
    private static final int STATION_K9D = 85;
    private static final int STATION_K10D = 86;
    private static final int STATION_K11D = 87;
    private static final int STATION_K12D = 88;
    private static final int STATION_K14D = 89;
    private static final int STATION_K15D = 90;
    private static final int STATION_K16D1= 91;
    private static final int STATION_K16D2= 92;
    private static final int STATION_K17D = 93;
    private static final int STATION_K18D = 94;

    /** 時刻表ノード詳細 */
    private static final int TRAIN_TIMETABLE_ITEM = 95;

    /** DBから取得する列一覧 */
    private static HashMap mProjectionMap;

    static { // UriMatcherの定義
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_1, TRAIN_K1);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_2, TRAIN_K2);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_3, TRAIN_K3);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_4, TRAIN_K4);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_5, TRAIN_K5);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_6, TRAIN_K6);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_11, TRAIN_K11);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_12, TRAIN_K12);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_21, TRAIN_K21);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_22, TRAIN_K22);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_25, TRAIN_K25);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_TRAIN_26, TRAIN_K26);

        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_1, TRAIN_H1);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_2, TRAIN_H2);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_3, TRAIN_H3);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_4, TRAIN_H4);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_5, TRAIN_H5);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_6, TRAIN_H6);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_7, TRAIN_H7);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_11, TRAIN_H11);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_12, TRAIN_H12);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_21, TRAIN_H21);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_22, TRAIN_H22);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_25, TRAIN_H25);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_TRAIN_26, TRAIN_H26);

        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_1U,STATION_H1U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_2U,STATION_H2U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_3U,STATION_H3U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_4U,STATION_H4U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_5U,STATION_H5U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_6U,STATION_H6U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_7U,STATION_H7U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_8U,STATION_H8U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_9U,STATION_H9U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_10U,STATION_H10U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_11U,STATION_H11U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_12U,STATION_H12U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_13U,STATION_H13U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_14U,STATION_H14U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_15U,STATION_H15U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_16U,STATION_H16U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_17U,STATION_H17U);

        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_2D,STATION_H2D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_3D,STATION_H3D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_4D,STATION_H4D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_5D,STATION_H5D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_6D,STATION_H6D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_7D,STATION_H7D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_8D,STATION_H8D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_9D,STATION_H9D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_10D,STATION_H10D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_11D,STATION_H11D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_12D,STATION_H12D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_14D,STATION_H14D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_15D,STATION_H15D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_16D1,STATION_H16D1);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_16D2,STATION_H16D2);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_17D,STATION_H17D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_WEEKDAY_STATION_18D,STATION_H18D);

        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_1U,STATION_K1U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_2U,STATION_K2U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_3U,STATION_K3U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_4U,STATION_K4U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_5U,STATION_K5U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_6U,STATION_K6U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_7U,STATION_K7U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_8U,STATION_K8U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_9U,STATION_K9U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_10U,STATION_K10U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_11U,STATION_K11U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_12U,STATION_K12U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_13U,STATION_K13U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_14U,STATION_K14U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_15U,STATION_K15U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_16U,STATION_K16U);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_17U,STATION_K17U);

        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_2D,STATION_K2D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_3D,STATION_K3D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_4D,STATION_K4D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_5D,STATION_K5D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_6D,STATION_K6D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_7D,STATION_K7D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_8D,STATION_K8D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_9D,STATION_K9D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_10D,STATION_K10D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_11D,STATION_K11D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_12D,STATION_K12D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_14D,STATION_K14D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_15D,STATION_K15D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_16D1,STATION_K16D1);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_16D2,STATION_K16D2);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_17D,STATION_K17D);
        mUriMatcher.addURI(TrainTblContract.AUTHORITY, TrainTblContract.PATH_HOLIDAY_STATION_18D,STATION_K18D);

        mUriMatcher.addURI(TrainTblContract.AUTHORITY,  "Item/#", TRAIN_TIMETABLE_ITEM);

        // ProjectionMapの定義
        // テーブルに_IDがないので、rowidを使う
        mProjectionMap = new HashMap();
        mProjectionMap.put(TrainTblContract._ID, "rowid as " + TrainTblContract._ID);
        mProjectionMap.put(TrainTblContract.TIME, TrainTblContract.TIME);
        mProjectionMap.put(TrainTblContract.UPDOWN, TrainTblContract.UPDOWN);
        mProjectionMap.put(TrainTblContract.HOUR, TrainTblContract.HOUR);
        mProjectionMap.put(TrainTblContract.MINUTE, TrainTblContract.MINUTE);
        mProjectionMap.put(TrainTblContract.HOLIDAY, TrainTblContract.HOLIDAY);
        mProjectionMap.put(TrainTblContract.STATION, TrainTblContract.STATION);
        mProjectionMap.put(TrainTblContract.DESTINATION, TrainTblContract.DESTINATION);
        mProjectionMap.put(TrainTblContract.TABLE_NO, TrainTblContract.TABLE_NO);
    }

    /**
     * プロバイダの作成時作業 SQLiteの時刻表DBのヘルパーを生成
     *
     * @return false //TODO 何故falseを返す？
     */
    @Override
    public boolean onCreate() {
        mHelper = new TrainTblOpenHelper(this.getContext());
        return false;
    }

    /**
     * 検索用メソッド
     *
     * @param uri Provider呼び出しURI
     * @param projection 取得するカラム
     * @param selection 選択行
     * @param selectionArgs 選択行の引数
     * @param sortOrder ソートパラメータ
     * @return 抽出した行Cursor
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        /** SQLクエリ作成用 */
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        if(DEBUG) Log.d(TAG, "mUriMatcher.match(uri): " + mUriMatcher.match(uri));
        //呼び出しURIで検索条件を変更
        switch (mUriMatcher.match(uri)) {
            case TRAIN_H1:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=1");
                break;
            case TRAIN_H2:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=2");
                break;
            case TRAIN_H3:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=3");
                break;
            case TRAIN_H4:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=4");
                break;
            case TRAIN_H5:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=5");
                break;
            case TRAIN_H6:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=6");
                break;
            case TRAIN_H7:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=7");
                break;
            case TRAIN_H11:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=11");
                break;
            case TRAIN_H12:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=12");
                break;
            case TRAIN_H21:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=21");
                break;
            case TRAIN_H22:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=22");
                break;
            case TRAIN_H25:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=25");
                break;
            case TRAIN_H26:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=26");
                break;
            case TRAIN_K1:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=1");
                break;
            case TRAIN_K2:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=2");
                break;
            case TRAIN_K3:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=3");
                break;
            case TRAIN_K4:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=4");
                break;
            case TRAIN_K5:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=5");
                break;
            case TRAIN_K6:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=6");
                break;
            case TRAIN_K11:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=11");
                break;
            case TRAIN_K12:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=12");
                break;
            case TRAIN_K21:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=21");
                break;
            case TRAIN_K22:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=22");
                break;
            case TRAIN_K25:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=25");
                break;
            case TRAIN_K26:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=26");
                break;

            case STATION_H1U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='千城台駅' AND UPDOWN=0");
                break;
            case STATION_H2U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='千城台北駅' AND UPDOWN=0");
                break;
            case STATION_H3U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='小倉台駅' AND UPDOWN=0");
                break;
            case STATION_H4U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='桜木駅' AND UPDOWN=0");
                break;
            case STATION_H5U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='都賀駅' AND UPDOWN=0");
                break;
            case STATION_H6U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='みつわ台駅' AND UPDOWN=0");
                break;
            case STATION_H7U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='動物公園駅' AND UPDOWN=0");
                break;
            case STATION_H8U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='スポーツセンター駅' AND UPDOWN=0");
                break;
            case STATION_H9U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='穴川駅' AND UPDOWN=0");
                break;
            case STATION_H10U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='天台駅' AND UPDOWN=0");
                break;
            case STATION_H11U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='作草部駅' AND UPDOWN=0");
                break;
            case STATION_H12U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉公園駅' AND UPDOWN=0");
                break;
            case STATION_H13U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='県庁前駅' AND UPDOWN=0");
                break;
            case STATION_H14U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='葭川公園駅' AND UPDOWN=0");
                break;
            case STATION_H15U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='栄町駅' AND UPDOWN=0");
                break;
            case STATION_H16U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉駅' AND UPDOWN=0");
                break;
            case STATION_H17U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='市役所前駅' AND UPDOWN=0");
                break;

            case STATION_H2D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='千城台北駅' AND UPDOWN=1");
                break;
            case STATION_H3D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='小倉台駅' AND UPDOWN=1");
                break;
            case STATION_H4D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='桜木駅' AND UPDOWN=1");
                break;
            case STATION_H5D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='都賀駅' AND UPDOWN=1");
                break;
            case STATION_H6D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='みつわ台駅' AND UPDOWN=1");
                break;
            case STATION_H7D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='動物公園駅' AND UPDOWN=1");
                break;
            case STATION_H8D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='スポーツセンター駅' AND UPDOWN=1");
                break;
            case STATION_H9D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='穴川駅' AND UPDOWN=1");
                break;
            case STATION_H10D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='天台駅' AND UPDOWN=1");
                break;
            case STATION_H11D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='作草部駅' AND UPDOWN=1");
                break;
            case STATION_H12D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉公園駅' AND UPDOWN=1");
                break;
            case STATION_H14D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='葭川公園駅' AND UPDOWN=1");
                break;
            case STATION_H15D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='栄町駅' AND UPDOWN=1");
                break;
            case STATION_H16D1:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉駅' AND UPDOWN=1 AND DESTINATION='県庁前方面'");
                break;
            case STATION_H16D2:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉駅' AND UPDOWN=1 AND NOT DESTINATION='県庁前方面'");
                break;
            case STATION_H17D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='市役所前駅' AND UPDOWN=1");
                break;
            case STATION_H18D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉みなと駅' AND UPDOWN=1");
                break;

            case STATION_K1U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='千城台駅' AND UPDOWN=0");
                break;
            case STATION_K2U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='千城台北駅' AND UPDOWN=0");
                break;
            case STATION_K3U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='小倉台駅' AND UPDOWN=0");
                break;
            case STATION_K4U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='桜木駅' AND UPDOWN=0");
                break;
            case STATION_K5U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='都賀駅' AND UPDOWN=0");
                break;
            case STATION_K6U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='みつわ台駅' AND UPDOWN=0");
                break;
            case STATION_K7U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='動物公園駅' AND UPDOWN=0");
                break;
            case STATION_K8U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='スポーツセンター駅' AND UPDOWN=0");
                break;
            case STATION_K9U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='穴川駅' AND UPDOWN=0");
                break;
            case STATION_K10U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='天台駅' AND UPDOWN=0");
                break;
            case STATION_K11U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='作草部駅' AND UPDOWN=0");
                break;
            case STATION_K12U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉公園駅' AND UPDOWN=0");
                break;
            case STATION_K13U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='県庁前駅' AND UPDOWN=0");
                break;
            case STATION_K14U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='葭川公園駅' AND UPDOWN=0");
                break;
            case STATION_K15U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='栄町駅' AND UPDOWN=0");
                break;
            case STATION_K16U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉駅' AND UPDOWN=0");
                break;
            case STATION_K17U:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='市役所前駅' AND UPDOWN=0");
                break;

            case STATION_K2D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='千城台北駅' AND UPDOWN=1");
                break;
            case STATION_K3D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='小倉台駅' AND UPDOWN=1");
                break;
            case STATION_K4D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='桜木駅' AND UPDOWN=1");
                break;
            case STATION_K5D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='都賀駅' AND UPDOWN=1");
                break;
            case STATION_K6D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='みつわ台駅' AND UPDOWN=1");
                break;
            case STATION_K7D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='動物公園駅' AND UPDOWN=1");
                break;
            case STATION_K8D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='スポーツセンター駅' AND UPDOWN=1");
                break;
            case STATION_K9D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='穴川駅' AND UPDOWN=1");
                break;
            case STATION_K10D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='天台駅' AND UPDOWN=1");
                break;
            case STATION_K11D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='作草部駅' AND UPDOWN=1");
                break;
            case STATION_K12D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉公園駅' AND UPDOWN=1");
                break;
            case STATION_K14D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='葭川公園駅' AND UPDOWN=1");
                break;
            case STATION_K15D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='栄町駅' AND UPDOWN=1");
                break;
            case STATION_K16D1:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉駅' AND UPDOWN=1 AND DESTINATION ='県庁前方面'");
                break;
            case STATION_K16D2:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉駅' AND UPDOWN=1 AND NOT DESTINATION = '県庁前方面'");
                break;
            case STATION_K17D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='市役所前駅' AND UPDOWN=1");
                break;
            case STATION_K18D:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉みなと駅' AND UPDOWN=1");
                break;

            case TRAIN_TIMETABLE_ITEM:
                qb.setTables(TrainTblOpenHelper.TBL_NAME);
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere("rowid="+ uri.getPathSegments().get(1));
//                qb.appendWhere(TrainTblContract._ID + "=" + uri.getPathSegments().get(1));
                if(DEBUG)Log.d(TAG,"uri.getPathSegments().get(1)" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        /** 検索するSQLite3 のdbオブジェクト */
        SQLiteDatabase db = mHelper.getWritableDatabase();
        /** 検索結果のカーソル */
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        if (DEBUG) {
            Log.d(TAG, "query after Cursor " + c.getColumnName(0));
//            Log.d(TAG, "query after Cursor " + c.getColumnName(1));
//            Log.d(TAG, "query after Cursor " + c.getColumnName(2));
//            Log.d(TAG, "query after Cursor " + c.getColumnName(3));
//            Log.d(TAG, "query after Cursor " + c.getColumnName(4));
            Log.d(TAG, "query after Cursor  Count" + c.getCount());
        }

        // 更新対象uriの登録
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    /**
     * MIME Typeの取得 戻り値が単一行か複数行かを取得
     *
     * @param uri 走査対象URI
     * @return 単一行/複数行のMIME Type URI
     */
    @Override
    public String getType(Uri uri) {
        if (DEBUG) {
            Log.d(TAG, "getType  " + uri);
        }
        switch (mUriMatcher.match(uri)) {
            case TRAIN_K1:
                return TrainTblContract.CONTENT_TYPE;
            case TRAIN_TIMETABLE_ITEM:
                return TrainTblContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * インサート処理（利用しない）
     * @param uri
     * @param values
     * @return
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new SQLException("Failed to insert row into " + uri);
    }

    /**
     * 削除処理（利用しない）
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new SQLException("Failed to delete row into " + uri);
    }

    /**
     * アップデート処理（利用しない）
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new SQLException("Failed to update row into " + uri);
    }
}
