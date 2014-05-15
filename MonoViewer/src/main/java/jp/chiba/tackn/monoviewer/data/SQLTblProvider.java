package jp.chiba.tackn.monoviewer.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 時刻表DBの検索を提供するProvider
 * @author Takumi Ito
 * @since 2014/05/12.
 */
public class SQLTblProvider extends ContentProvider {
    /** デバッグフラグ */
    private static final boolean DEBUG =false;
    /** デバッグタグ */
    private static final String TAG = "TrainTblProvider ";
    /** SQLite3を取り扱うヘルパー */
    private SQLTblOpenHelper mHelper;
    /** URI判定機 */
    private static final UriMatcher mUriMatcher;

    // URIのスイッチ用ID
    /** 休日 列車ダイヤ1 */
    private static final int TRAIN_K1 = 1;
    /** 休日 列車ダイヤ2 */
    private static final int TRAIN_K2 = 2;
    /** 休日 列車ダイヤ3 */
    private static final int TRAIN_K3 = 3;
    /** 休日 列車ダイヤ4 */
    private static final int TRAIN_K4 = 4;
    /** 休日 列車ダイヤ5 */
    private static final int TRAIN_K5 = 5;
    /** 休日 列車ダイヤ6 */
    private static final int TRAIN_K6 = 6;
    /** 休日 列車ダイヤ11 */
    private static final int TRAIN_K11 = 8;
    /** 休日 列車ダイヤ12 */
    private static final int TRAIN_K12 = 9;
    /** 休日 列車ダイヤ21 */
    private static final int TRAIN_K21 = 10;
    /** 休日 列車ダイヤ22 */
    private static final int TRAIN_K22 = 11;
    /** 休日 列車ダイヤ25 */
    private static final int TRAIN_K25 = 12;
    /** 休日 列車ダイヤ26 */
    private static final int TRAIN_K26 = 13;

    /** 平日 列車ダイヤ1 */
    private static final int TRAIN_H1 = 14;
    /** 平日 列車ダイヤ2 */
    private static final int TRAIN_H2 = 15;
    /** 平日 列車ダイヤ3 */
    private static final int TRAIN_H3 = 16;
    /** 平日 列車ダイヤ4 */
    private static final int TRAIN_H4 = 17;
    /** 平日 列車ダイヤ5 */
    private static final int TRAIN_H5 = 18;
    /** 平日 列車ダイヤ6 */
    private static final int TRAIN_H6 = 19;
    /** 平日 列車ダイヤ7 */
    private static final int TRAIN_H7 = 20;
    /** 平日 列車ダイヤ11 */
    private static final int TRAIN_H11 = 21;
    /** 平日 列車ダイヤ12 */
    private static final int TRAIN_H12 = 22;
    /** 平日 列車ダイヤ21 */
    private static final int TRAIN_H21 = 23;
    /** 平日 列車ダイヤ22 */
    private static final int TRAIN_H22 = 24;
    /** 平日 列車ダイヤ25 */
    private static final int TRAIN_H25 = 25;
    /** 平日 列車ダイヤ26 */
    private static final int TRAIN_H26 = 26;

    /** 平日 千城台駅 上り */
    private static final int STATION_H1U = 27;
    /** 平日 千城台北駅 上り */
    private static final int STATION_H2U = 28;
    /** 平日 小倉台駅 上り */
    private static final int STATION_H3U = 29;
    /** 平日 桜木駅 上り */
    private static final int STATION_H4U = 30;
    /** 平日 都賀駅 上り */
    private static final int STATION_H5U = 31;
    /** 平日 みつわ台駅 上り */
    private static final int STATION_H6U = 32;
    /** 平日 動物公園駅 上り */
    private static final int STATION_H7U = 33;
    /** 平日 スポーツセンター駅 上り */
    private static final int STATION_H8U = 34;
    /** 平日 穴川駅 上り */
    private static final int STATION_H9U = 35;
    /** 平日 天台駅 上り */
    private static final int STATION_H10U = 36;
    /** 平日 作草部駅 上り */
    private static final int STATION_H11U = 37;
    /** 平日 千葉公園駅 上り */
    private static final int STATION_H12U = 38;
    /** 平日 県庁前駅 上り */
    private static final int STATION_H13U = 39;
    /** 平日 葭川公園駅 上り */
    private static final int STATION_H14U = 40;
    /** 平日 栄町駅 上り */
    private static final int STATION_H15U = 41;
    /** 平日 千葉駅 上り */
    private static final int STATION_H16U= 42;
    /** 平日 市役所前駅 上り */
    private static final int STATION_H17U = 43;

    /** 平日 千城台北駅 下り */
    private static final int STATION_H2D = 44;
    /** 平日 小倉台駅 下り */
    private static final int STATION_H3D = 45;
    /** 平日 桜木駅 下り */
    private static final int STATION_H4D = 46;
    /** 平日 都賀駅 下り */
    private static final int STATION_H5D = 47;
    /** 平日 みつわ台駅 下り */
    private static final int STATION_H6D = 48;
    /** 平日 動物公園駅 下り */
    private static final int STATION_H7D = 49;
    /** 平日 スポーツセンター駅 下り */
    private static final int STATION_H8D = 50;
    /** 平日 穴川駅 下り */
    private static final int STATION_H9D = 51;
    /** 平日 天台駅 下り */
    private static final int STATION_H10D = 52;
    /** 平日 作草部駅 下り */
    private static final int STATION_H11D = 53;
    /** 平日 千葉公園駅 下り */
    private static final int STATION_H12D = 54;
    /** 平日 葭川公園駅 下り */
    private static final int STATION_H14D = 55;
    /** 平日 栄町駅 下り */
    private static final int STATION_H15D = 56;
    /** 平日 千葉駅1号線 下り */
    private static final int STATION_H16D1= 57;
    /** 平日 千葉駅2号線 下り */
    private static final int STATION_H16D2= 58;
    /** 平日 市役所前駅 下り */
    private static final int STATION_H17D = 59;
    /** 平日 千葉みなと駅 下り */
    private static final int STATION_H18D = 60;

    /** 休日 千城台駅 上り */
    private static final int STATION_K1U = 61;
    /** 休日 千城台北駅 上り */
    private static final int STATION_K2U = 62;
    /** 休日 小倉台駅 上り */
    private static final int STATION_K3U = 63;
    /** 休日 桜木駅 上り */
    private static final int STATION_K4U = 64;
    /** 休日 都賀駅 上り */
    private static final int STATION_K5U = 65;
    /** 休日 みつわ台駅 上り */
    private static final int STATION_K6U = 66;
    /** 休日 動物公園駅 上り */
    private static final int STATION_K7U = 67;
    /** 休日 スポーツセンター駅 上り */
    private static final int STATION_K8U = 68;
    /** 休日 穴川駅 上り */
    private static final int STATION_K9U = 69;
    /** 休日 天台駅 上り */
    private static final int STATION_K10U = 70;
    /** 休日 作草部駅 上り */
    private static final int STATION_K11U = 71;
    /** 休日 千葉公園駅 上り */
    private static final int STATION_K12U = 72;
    /** 休日 県庁前駅 上り */
    private static final int STATION_K13U = 73;
    /** 休日 葭川公園駅 上り */
    private static final int STATION_K14U = 74;
    /** 休日 栄町駅 上り */
    private static final int STATION_K15U = 75;
    /** 休日 千葉駅 上り */
    private static final int STATION_K16U= 76;
    /** 休日 市役所前駅 上り */
    private static final int STATION_K17U = 77;

    /** 休日 千城台北駅 下り */
    private static final int STATION_K2D = 78;
    /** 休日 小倉台駅 下り */
    private static final int STATION_K3D = 79;
    /** 休日 桜木駅 下り */
    private static final int STATION_K4D = 80;
    /** 休日 都賀駅 下り */
    private static final int STATION_K5D = 81;
    /** 休日 みつわ台駅 下り */
    private static final int STATION_K6D = 82;
    /** 休日 動物公園駅 下り */
    private static final int STATION_K7D = 83;
    /** 休日 スポーツセンター駅 下り */
    private static final int STATION_K8D = 84;
    /** 休日 穴川駅 下り */
    private static final int STATION_K9D = 85;
    /** 休日 天台駅 下り */
    private static final int STATION_K10D = 86;
    /** 休日 作草部駅 下り */
    private static final int STATION_K11D = 87;
    /** 休日 千葉公園駅 下り */
    private static final int STATION_K12D = 88;
    /** 休日 葭川公園駅 下り */
    private static final int STATION_K14D = 89;
    /** 休日 栄町駅 下り */
    private static final int STATION_K15D = 90;
    /** 休日 千葉駅1号線 下り */
    private static final int STATION_K16D1= 91;
    /** 休日 千葉駅2号線 下り */
    private static final int STATION_K16D2= 92;
    /** 休日 市役所前駅 下り */
    private static final int STATION_K17D = 93;
    /** 休日 千葉みなと駅 下り */
    private static final int STATION_K18D = 94;

    /** 時刻表ノード詳細 */
    private static final int TRAIN_TIMETABLE_ITEM = 95;

    /** 現在走行中の列車一覧 */
    private static final int NOW_INFOMATION_WEEKEND =96;

    /** 現在走行中の列車一覧 */
    private static final int NOW_INFOMATION_HOLIDAY =97;

    /** DBから取得する列一覧 */
    private static HashMap<String,String> mProjectionMap;

    static {
        // UriMatcherの定義
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_1, TRAIN_K1);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_2, TRAIN_K2);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_3, TRAIN_K3);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_4, TRAIN_K4);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_5, TRAIN_K5);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_6, TRAIN_K6);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_11, TRAIN_K11);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_12, TRAIN_K12);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_21, TRAIN_K21);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_22, TRAIN_K22);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_25, TRAIN_K25);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_TRAIN_26, TRAIN_K26);

        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_1, TRAIN_H1);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_2, TRAIN_H2);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_3, TRAIN_H3);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_4, TRAIN_H4);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_5, TRAIN_H5);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_6, TRAIN_H6);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_7, TRAIN_H7);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_11, TRAIN_H11);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_12, TRAIN_H12);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_21, TRAIN_H21);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_22, TRAIN_H22);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_25, TRAIN_H25);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_TRAIN_26, TRAIN_H26);

        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_1U,STATION_H1U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_2U,STATION_H2U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_3U,STATION_H3U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_4U,STATION_H4U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_5U,STATION_H5U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_6U,STATION_H6U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_7U,STATION_H7U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_8U,STATION_H8U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_9U,STATION_H9U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_10U,STATION_H10U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_11U,STATION_H11U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_12U,STATION_H12U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_13U,STATION_H13U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_14U,STATION_H14U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_15U,STATION_H15U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_16U,STATION_H16U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_17U,STATION_H17U);

        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_2D,STATION_H2D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_3D,STATION_H3D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_4D,STATION_H4D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_5D,STATION_H5D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_6D,STATION_H6D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_7D,STATION_H7D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_8D,STATION_H8D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_9D,STATION_H9D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_10D,STATION_H10D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_11D,STATION_H11D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_12D,STATION_H12D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_14D,STATION_H14D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_15D,STATION_H15D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_16D1,STATION_H16D1);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_16D2,STATION_H16D2);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_17D,STATION_H17D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_WEEKDAY_STATION_18D,STATION_H18D);

        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_1U,STATION_K1U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_2U,STATION_K2U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_3U,STATION_K3U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_4U,STATION_K4U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_5U,STATION_K5U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_6U,STATION_K6U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_7U,STATION_K7U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_8U,STATION_K8U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_9U,STATION_K9U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_10U,STATION_K10U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_11U,STATION_K11U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_12U,STATION_K12U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_13U,STATION_K13U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_14U,STATION_K14U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_15U,STATION_K15U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_16U,STATION_K16U);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_17U,STATION_K17U);

        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_2D,STATION_K2D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_3D,STATION_K3D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_4D,STATION_K4D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_5D,STATION_K5D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_6D,STATION_K6D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_7D,STATION_K7D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_8D,STATION_K8D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_9D,STATION_K9D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_10D,STATION_K10D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_11D,STATION_K11D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_12D,STATION_K12D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_14D,STATION_K14D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_15D,STATION_K15D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_16D1,STATION_K16D1);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_16D2,STATION_K16D2);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_17D,STATION_K17D);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_HOLIDAY_STATION_18D,STATION_K18D);

        mUriMatcher.addURI(SQLTblContract.AUTHORITY,  "Item/#", TRAIN_TIMETABLE_ITEM);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_NOW_TRAIN_SERVICE_INFOMATION_WEEKEND, NOW_INFOMATION_WEEKEND);
        mUriMatcher.addURI(SQLTblContract.AUTHORITY, SQLTblContract.PATH_NOW_TRAIN_SERVICE_INFOMATION_HOLIDAY, NOW_INFOMATION_HOLIDAY);

        // ProjectionMapの定義
        // テーブルに_IDがないので、rowidを使う
        mProjectionMap = new HashMap<String,String>();
        mProjectionMap.put(SQLTblContract._ID, "rowid as " + SQLTblContract._ID);
        mProjectionMap.put(SQLTblContract.COLUMN_TIME, SQLTblContract.COLUMN_TIME);
        mProjectionMap.put(SQLTblContract.COLUMN_UPDOWN, SQLTblContract.COLUMN_UPDOWN);
        mProjectionMap.put(SQLTblContract.COLUMN_HOUR, SQLTblContract.COLUMN_HOUR);
        mProjectionMap.put(SQLTblContract.COLUMN_MINUTE, SQLTblContract.COLUMN_MINUTE);
        mProjectionMap.put(SQLTblContract.COLUMN_HOLIDAY, SQLTblContract.COLUMN_HOLIDAY);
        mProjectionMap.put(SQLTblContract.COLUMN_STATION, SQLTblContract.COLUMN_STATION);
        mProjectionMap.put(SQLTblContract.COLUMN_DESTINATION, SQLTblContract.COLUMN_DESTINATION);
        mProjectionMap.put(SQLTblContract.COLUMN_TABLE_NO, SQLTblContract.COLUMN_TABLE_NO);
        mProjectionMap.put(SQLTblContract.COLUMN_ANNOTATION, SQLTblContract.COLUMN_ANNOTATION);
    }

    /**
     * プロバイダの作成時作業 SQLiteの時刻表DBのヘルパーを生成
     *
     * @return false onCreateの再実行の必要なし
     */
    @Override
    public boolean onCreate() {
        mHelper = new SQLTblOpenHelper(this.getContext());
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

        //現在時刻と5分前の取得
        Date Date = new Date();
        Calendar calendar = Calendar.getInstance();
        long nowLong = System.currentTimeMillis();
        long beforeLong = nowLong - (5 * 60 * 1000);

        Date.setTime(nowLong);
        calendar.setTime(Date);
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);

        Date.setTime(beforeLong);
        calendar.setTime(Date);
        int beforeHour = calendar.get(Calendar.HOUR_OF_DAY);
        int beforeMinute = calendar.get(Calendar.MINUTE);


        if(DEBUG) Log.d(TAG, "mUriMatcher.match(uri): " + mUriMatcher.match(uri));
        //呼び出しURIで検索条件を変更
        qb.setTables(SQLTblOpenHelper.TBL_NAME);
        qb.setProjectionMap(mProjectionMap);

        switch (mUriMatcher.match(uri)) {
            case TRAIN_H1:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=1");
                break;
            case TRAIN_H2:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=2");
                break;
            case TRAIN_H3:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=3");
                break;
            case TRAIN_H4:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=4");
                break;
            case TRAIN_H5:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=5");
                break;
            case TRAIN_H6:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=6");
                break;
            case TRAIN_H7:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=7");
                break;
            case TRAIN_H11:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=11");
                break;
            case TRAIN_H12:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=12");
                break;
            case TRAIN_H21:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=21");
                break;
            case TRAIN_H22:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=22");
                break;
            case TRAIN_H25:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=25");
                break;
            case TRAIN_H26:
                qb.appendWhere("HOLIDAY=1 AND TABLE_NO=26");
                break;
            case TRAIN_K1:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=1");
                break;
            case TRAIN_K2:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=2");
                break;
            case TRAIN_K3:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=3");
                break;
            case TRAIN_K4:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=4");
                break;
            case TRAIN_K5:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=5");
                break;
            case TRAIN_K6:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=6");
                break;
            case TRAIN_K11:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=11");
                break;
            case TRAIN_K12:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=12");
                break;
            case TRAIN_K21:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=21");
                break;
            case TRAIN_K22:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=22");
                break;
            case TRAIN_K25:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=25");
                break;
            case TRAIN_K26:
                qb.appendWhere("HOLIDAY=0 AND TABLE_NO=26");
                break;

            case STATION_H1U:
                qb.appendWhere("HOLIDAY=1 AND STATION='千城台駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H2U:
                qb.appendWhere("HOLIDAY=1 AND STATION='千城台北駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H3U:
                qb.appendWhere("HOLIDAY=1 AND STATION='小倉台駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H4U:
                qb.appendWhere("HOLIDAY=1 AND STATION='桜木駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H5U:
                qb.appendWhere("HOLIDAY=1 AND STATION='都賀駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H6U:
                qb.appendWhere("HOLIDAY=1 AND STATION='みつわ台駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H7U:
                qb.appendWhere("HOLIDAY=1 AND STATION='動物公園駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H8U:
                qb.appendWhere("HOLIDAY=1 AND STATION='スポーツセンター駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H9U:
                qb.appendWhere("HOLIDAY=1 AND STATION='穴川駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H10U:
                qb.appendWhere("HOLIDAY=1 AND STATION='天台駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H11U:
                qb.appendWhere("HOLIDAY=1 AND STATION='作草部駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H12U:
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉公園駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H13U:
                qb.appendWhere("HOLIDAY=1 AND STATION='県庁前駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H14U:
                qb.appendWhere("HOLIDAY=1 AND STATION='葭川公園駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H15U:
                qb.appendWhere("HOLIDAY=1 AND STATION='栄町駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H16U:
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_H17U:
                qb.appendWhere("HOLIDAY=1 AND STATION='市役所前駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;

            case STATION_H2D:
                qb.appendWhere("HOLIDAY=1 AND STATION='千城台北駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H3D:
                qb.appendWhere("HOLIDAY=1 AND STATION='小倉台駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H4D:
                qb.appendWhere("HOLIDAY=1 AND STATION='桜木駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H5D:
                qb.appendWhere("HOLIDAY=1 AND STATION='都賀駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H6D:
                qb.appendWhere("HOLIDAY=1 AND STATION='みつわ台駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H7D:
                qb.appendWhere("HOLIDAY=1 AND STATION='動物公園駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H8D:
                qb.appendWhere("HOLIDAY=1 AND STATION='スポーツセンター駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H9D:
                qb.appendWhere("HOLIDAY=1 AND STATION='穴川駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H10D:
                qb.appendWhere("HOLIDAY=1 AND STATION='天台駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H11D:
                qb.appendWhere("HOLIDAY=1 AND STATION='作草部駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H12D:
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉公園駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H14D:
                qb.appendWhere("HOLIDAY=1 AND STATION='葭川公園駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H15D:
                qb.appendWhere("HOLIDAY=1 AND STATION='栄町駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H16D1:
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉駅' AND UPDOWN=1 AND DESTINATION='県庁前方面'");
                break;
            case STATION_H16D2:
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉駅' AND UPDOWN=1 AND NOT DESTINATION='県庁前方面' AND NOT DESTINATION=''");
                break;
            case STATION_H17D:
                qb.appendWhere("HOLIDAY=1 AND STATION='市役所前駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_H18D:
                qb.appendWhere("HOLIDAY=1 AND STATION='千葉みなと駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;

            case STATION_K1U:
                qb.appendWhere("HOLIDAY=0 AND STATION='千城台駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K2U:
                qb.appendWhere("HOLIDAY=0 AND STATION='千城台北駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K3U:
                qb.appendWhere("HOLIDAY=0 AND STATION='小倉台駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K4U:
                qb.appendWhere("HOLIDAY=0 AND STATION='桜木駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K5U:
                qb.appendWhere("HOLIDAY=0 AND STATION='都賀駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K6U:
                qb.appendWhere("HOLIDAY=0 AND STATION='みつわ台駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K7U:
                qb.appendWhere("HOLIDAY=0 AND STATION='動物公園駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K8U:
                qb.appendWhere("HOLIDAY=0 AND STATION='スポーツセンター駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K9U:
                qb.appendWhere("HOLIDAY=0 AND STATION='穴川駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K10U:
                qb.appendWhere("HOLIDAY=0 AND STATION='天台駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K11U:
                qb.appendWhere("HOLIDAY=0 AND STATION='作草部駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K12U:
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉公園駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K13U:
                qb.appendWhere("HOLIDAY=0 AND STATION='県庁前駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K14U:
                qb.appendWhere("HOLIDAY=0 AND STATION='葭川公園駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K15U:
                qb.appendWhere("HOLIDAY=0 AND STATION='栄町駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K16U:
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;
            case STATION_K17U:
                qb.appendWhere("HOLIDAY=0 AND STATION='市役所前駅' AND UPDOWN=0 AND NOT DESTINATION=''");
                break;

            case STATION_K2D:
                qb.appendWhere("HOLIDAY=0 AND STATION='千城台北駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K3D:
                qb.appendWhere("HOLIDAY=0 AND STATION='小倉台駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K4D:
                qb.appendWhere("HOLIDAY=0 AND STATION='桜木駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K5D:
                qb.appendWhere("HOLIDAY=0 AND STATION='都賀駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K6D:
                qb.appendWhere("HOLIDAY=0 AND STATION='みつわ台駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K7D:
                qb.appendWhere("HOLIDAY=0 AND STATION='動物公園駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K8D:
                qb.appendWhere("HOLIDAY=0 AND STATION='スポーツセンター駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K9D:
                qb.appendWhere("HOLIDAY=0 AND STATION='穴川駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K10D:
                qb.appendWhere("HOLIDAY=0 AND STATION='天台駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K11D:
                qb.appendWhere("HOLIDAY=0 AND STATION='作草部駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K12D:
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉公園駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K14D:
                qb.appendWhere("HOLIDAY=0 AND STATION='葭川公園駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K15D:
                qb.appendWhere("HOLIDAY=0 AND STATION='栄町駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K16D1:
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉駅' AND UPDOWN=1 AND DESTINATION ='県庁前方面'");
                break;
            case STATION_K16D2:
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉駅' AND UPDOWN=1 AND NOT DESTINATION = '県庁前方面' AND NOT DESTINATION=''");
                break;
            case STATION_K17D:
                qb.appendWhere("HOLIDAY=0 AND STATION='市役所前駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;
            case STATION_K18D:
                qb.appendWhere("HOLIDAY=0 AND STATION='千葉みなと駅' AND UPDOWN=1 AND NOT DESTINATION=''");
                break;

            case TRAIN_TIMETABLE_ITEM:
                qb.appendWhere("rowid="+ uri.getPathSegments().get(1));
//                qb.appendWhere(TrainTblContract._ID + "=" + uri.getPathSegments().get(1));
                if(DEBUG)Log.d(TAG,"uri.getPathSegments().get(1)" + uri.getPathSegments().get(1));
                break;

            case NOW_INFOMATION_WEEKEND:
                qb.appendWhere("TIMES BETWEEN " + getQueryTimes(beforeHour,beforeMinute)
                        + " AND " + getQueryTimes(nowHour,nowMinute)
                        + " AND HOLIDAY = " + 1);
                break;

            case NOW_INFOMATION_HOLIDAY:
                qb.appendWhere("TIMES BETWEEN " + getQueryTimes(beforeHour,beforeMinute)
                        + " AND " + getQueryTimes(nowHour,nowMinute)
                        + " AND HOLIDAY = " + 0);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        /** 検索するSQLite3 のdbオブジェクト */
        SQLiteDatabase db = mHelper.getReadableDatabase();
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

    private int getQueryTimes(int hour,int minutes){
        if(hour==0)hour=24;
        return Integer.valueOf(String.format("%1$02d", hour) + String.format("%1$02d", minutes));
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
                return SQLTblContract.CONTENT_TYPE;
            case TRAIN_TIMETABLE_ITEM:
                return SQLTblContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * インサート処理（利用しない）
     * @param uri コンテンツプロバイダのURI
     * @param values インサートする値
     * @return 識別子？
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new SQLException("Failed to insert row into " + uri);
    }

    /**
     * 削除処理（利用しない）
     * @param uri コンテンツプロバイダのURI
     * @param selection クエリ
     * @param selectionArgs 引数
     * @return 件数
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new SQLException("Failed to delete row into " + uri);
    }

    /**
     * アップデート処理（利用しない）
     * @param uri コンテンツプロバイダのURI
     * @param values アップデート値
     * @param selection クエリ
     * @param selectionArgs 引数
     * @return 件数
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new SQLException("Failed to update row into " + uri);
    }
}
