package jp.chiba.tackn.monoviewer.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 車両時刻表を取り扱うプロバイダの規約
 *
 * @author Takumi Ito
 * @since 2014/05/12.
 */
public class SQLTblContract implements BaseColumns {

    /** パッケージ名 */
    public static final String AUTHORITY = "jp.chiba.tackn.monoviewer.data";
    /** プロバイダの提供するコマンド*/
    /** 休日 列車ダイヤ1 */
    public static final String PATH_HOLIDAY_TRAIN_1 = "KA1";
    /** 休日 列車ダイヤ2 */
    public static final String PATH_HOLIDAY_TRAIN_2 = "KA2";
    /** 休日 列車ダイヤ3 */
    public static final String PATH_HOLIDAY_TRAIN_3 = "KA3";
    /** 休日 列車ダイヤ4 */
    public static final String PATH_HOLIDAY_TRAIN_4 = "KA4";
    /** 休日 列車ダイヤ5 */
    public static final String PATH_HOLIDAY_TRAIN_5 = "KA5";
    /** 休日 列車ダイヤ6 */
    public static final String PATH_HOLIDAY_TRAIN_6 = "KA6";
    /** 休日 列車ダイヤ11 */
    public static final String PATH_HOLIDAY_TRAIN_11 = "KA11";
    /** 休日 列車ダイヤ12 */
    public static final String PATH_HOLIDAY_TRAIN_12 = "KA12";
    /** 休日 列車ダイヤ21 */
    public static final String PATH_HOLIDAY_TRAIN_21 = "KA21";
    /** 休日 列車ダイヤ22 */
    public static final String PATH_HOLIDAY_TRAIN_22 = "KA22";
    /** 休日 列車ダイヤ25 */
    public static final String PATH_HOLIDAY_TRAIN_25 = "KA23";
    /** 休日 列車ダイヤ26 */
    public static final String PATH_HOLIDAY_TRAIN_26 = "KA26";

    /** 平日 列車ダイヤ1 */
    public static final String PATH_WEEKDAY_TRAIN_1 = "HA1";
    /** 平日 列車ダイヤ2 */
    public static final String PATH_WEEKDAY_TRAIN_2 = "HA2";
    /** 平日 列車ダイヤ3 */
    public static final String PATH_WEEKDAY_TRAIN_3 = "HA3";
    /** 平日 列車ダイヤ4 */
    public static final String PATH_WEEKDAY_TRAIN_4 = "HA4";
    /** 平日 列車ダイヤ5 */
    public static final String PATH_WEEKDAY_TRAIN_5 = "HA5";
    /** 平日 列車ダイヤ6 */
    public static final String PATH_WEEKDAY_TRAIN_6 = "HA6";
    /** 平日 列車ダイヤ7 */
    public static final String PATH_WEEKDAY_TRAIN_7 = "HA7";
    /** 平日 列車ダイヤ11 */
    public static final String PATH_WEEKDAY_TRAIN_11 = "HA11";
    /** 平日 列車ダイヤ12 */
    public static final String PATH_WEEKDAY_TRAIN_12 = "HA12";
    /** 平日 列車ダイヤ21 */
    public static final String PATH_WEEKDAY_TRAIN_21 = "HA21";
    /** 平日 列車ダイヤ22 */
    public static final String PATH_WEEKDAY_TRAIN_22 = "HA22";
    /** 平日 列車ダイヤ25 */
    public static final String PATH_WEEKDAY_TRAIN_25 = "HA25";
    /** 平日 列車ダイヤ26 */
    public static final String PATH_WEEKDAY_TRAIN_26 = "HA26";

    /** 平日 千城台駅 上り */
    public static final String PATH_WEEKDAY_STATION_1U = "HS1U";
    /** 平日 千城台北駅 上り */
    public static final String PATH_WEEKDAY_STATION_2U = "HS2U";
    /** 平日 小倉台駅 上り */
    public static final String PATH_WEEKDAY_STATION_3U = "HS3U";
    /** 平日 桜木駅 上り */
    public static final String PATH_WEEKDAY_STATION_4U = "HS4U";
    /** 平日 都賀駅 上り */
    public static final String PATH_WEEKDAY_STATION_5U = "HS5U";
    /** 平日 みつわ台駅 上り */
    public static final String PATH_WEEKDAY_STATION_6U = "HS6U";
    /** 平日 動物公園駅 上り */
    public static final String PATH_WEEKDAY_STATION_7U = "HS7U";
    /** 平日 スポーツセンター駅 上り */
    public static final String PATH_WEEKDAY_STATION_8U = "HS8U";
    /** 平日 穴川駅 上り */
    public static final String PATH_WEEKDAY_STATION_9U = "HS9U";
    /** 平日 天台駅 上り */
    public static final String PATH_WEEKDAY_STATION_10U = "HS10U";
    /** 平日 作草部駅 上り */
    public static final String PATH_WEEKDAY_STATION_11U = "HS11U";
    /** 平日 千葉公園駅 上り */
    public static final String PATH_WEEKDAY_STATION_12U = "HS12U";
    /** 平日 県庁前駅 上り */
    public static final String PATH_WEEKDAY_STATION_13U = "HS13U";
    /** 平日 葭川公園駅 上り */
    public static final String PATH_WEEKDAY_STATION_14U = "HS14U";
    /** 平日 栄町駅 上り */
    public static final String PATH_WEEKDAY_STATION_15U = "HS15U";
    /** 平日 千葉駅 上り */
    public static final String PATH_WEEKDAY_STATION_16U = "HS16U";
    /** 平日 市役所前駅 上り */
    public static final String PATH_WEEKDAY_STATION_17U = "HS17U";

    /** 平日 千城台北駅 下り */
    public static final String PATH_WEEKDAY_STATION_2D = "HS2D";
    /** 平日 小倉台駅 下り */
    public static final String PATH_WEEKDAY_STATION_3D = "HS3D";
    /** 平日 桜木駅 下り */
    public static final String PATH_WEEKDAY_STATION_4D = "HS4D";
    /** 平日 都賀駅 下り */
    public static final String PATH_WEEKDAY_STATION_5D = "HS5D";
    /** 平日 みつわ台駅 下り */
    public static final String PATH_WEEKDAY_STATION_6D = "HS6D";
    /** 平日 動物公園駅 下り */
    public static final String PATH_WEEKDAY_STATION_7D = "HS7D";
    /** 平日 スポーツセンター駅 下り */
    public static final String PATH_WEEKDAY_STATION_8D = "HS8D";
    /** 平日 穴川駅 下り */
    public static final String PATH_WEEKDAY_STATION_9D = "HS9D";
    /** 平日 天台駅 下り */
    public static final String PATH_WEEKDAY_STATION_10D = "HS10D";
    /** 平日 作草部駅 下り */
    public static final String PATH_WEEKDAY_STATION_11D = "HS11D";
    /** 平日 千葉公園駅 下り */
    public static final String PATH_WEEKDAY_STATION_12D = "HS12D";
    /** 平日 葭川公園駅 下り */
    public static final String PATH_WEEKDAY_STATION_14D = "HS14D";
    /** 平日 栄町駅 下り */
    public static final String PATH_WEEKDAY_STATION_15D = "HS15D";
    /** 平日 千葉駅1号線 下り */
    public static final String PATH_WEEKDAY_STATION_16D1 = "HS16D1";
    /** 平日 千葉駅2号線 下り */
    public static final String PATH_WEEKDAY_STATION_16D2 = "HS16D2";
    /** 平日 市役所前駅 下り */
    public static final String PATH_WEEKDAY_STATION_17D = "HS17D";
    /** 平日 千葉みなと駅 下り */
    public static final String PATH_WEEKDAY_STATION_18D = "HS18D";

    /** 休日 千城台駅 上り */
    public static final String PATH_HOLIDAY_STATION_1U = "KS1U";
    /** 休日 千城台北駅 上り */
    public static final String PATH_HOLIDAY_STATION_2U = "KS2U";
    /** 休日 小倉台駅 上り */
    public static final String PATH_HOLIDAY_STATION_3U = "KS3U";
    /** 休日 桜木駅 上り */
    public static final String PATH_HOLIDAY_STATION_4U = "KS4U";
    /** 休日 都賀駅 上り */
    public static final String PATH_HOLIDAY_STATION_5U = "KS5U";
    /** 休日 みつわ台駅 上り */
    public static final String PATH_HOLIDAY_STATION_6U = "KS6U";
    /** 休日 動物公園駅 上り */
    public static final String PATH_HOLIDAY_STATION_7U = "KS7U";
    /** 休日 スポーツセンター駅 上り */
    public static final String PATH_HOLIDAY_STATION_8U = "KS8U";
    /** 休日 穴川駅 上り */
    public static final String PATH_HOLIDAY_STATION_9U = "KS9U";
    /** 休日 天台駅 上り */
    public static final String PATH_HOLIDAY_STATION_10U = "KS10U";
    /** 休日 作草部駅 上り */
    public static final String PATH_HOLIDAY_STATION_11U = "KS11U";
    /** 休日 千葉公園駅 上り */
    public static final String PATH_HOLIDAY_STATION_12U = "KS12U";
    /** 休日 県庁前駅 上り */
    public static final String PATH_HOLIDAY_STATION_13U = "KS13U";
    /** 休日 葭川公園駅 上り */
    public static final String PATH_HOLIDAY_STATION_14U = "KS14U";
    /** 休日 栄町駅 上り */
    public static final String PATH_HOLIDAY_STATION_15U = "KS15U";
    /** 休日 千葉駅 上り */
    public static final String PATH_HOLIDAY_STATION_16U = "KS16U";
    /** 休日 市役所前駅 上り */
    public static final String PATH_HOLIDAY_STATION_17U = "KS17U";

    /** 休日 千城台北駅 下り */
    public static final String PATH_HOLIDAY_STATION_2D = "KS2D";
    /** 休日 小倉台駅 下り */
    public static final String PATH_HOLIDAY_STATION_3D = "KS3D";
    /** 休日 桜木駅 下り */
    public static final String PATH_HOLIDAY_STATION_4D = "KS4D";
    /** 休日 都賀駅 下り */
    public static final String PATH_HOLIDAY_STATION_5D = "KS5D";
    /** 休日 みつわ台駅 下り */
    public static final String PATH_HOLIDAY_STATION_6D = "KS6D";
    /** 休日 動物公園駅 下り */
    public static final String PATH_HOLIDAY_STATION_7D = "KS7D";
    /** 休日 スポーツセンター駅 下り */
    public static final String PATH_HOLIDAY_STATION_8D = "KS8D";
    /** 休日 穴川駅 下り */
    public static final String PATH_HOLIDAY_STATION_9D = "KS9D";
    /** 休日 天台駅 下り */
    public static final String PATH_HOLIDAY_STATION_10D = "KS10D";
    /** 休日 作草部駅 下り */
    public static final String PATH_HOLIDAY_STATION_11D = "KS11D";
    /** 休日 千葉公園駅 下り */
    public static final String PATH_HOLIDAY_STATION_12D = "KS12D";
    /** 休日 葭川公園駅 下り */
    public static final String PATH_HOLIDAY_STATION_14D = "KS14D";
    /** 休日 栄町駅 下り */
    public static final String PATH_HOLIDAY_STATION_15D = "KS15D";
    /** 休日 千葉駅1号線 下り */
    public static final String PATH_HOLIDAY_STATION_16D1 = "KS16D1";
    /** 休日 千葉駅2号線 下り */
    public static final String PATH_HOLIDAY_STATION_16D2 = "KS16D2";
    /** 休日 市役所前駅 下り */
    public static final String PATH_HOLIDAY_STATION_17D = "KS17D";
    /** 休日 千葉みなと駅 下り */
    public static final String PATH_HOLIDAY_STATION_18D = "KS18D";


    /** コンテンツプロバイダが提供するデータベースを示すURI */

    /** 平日 列車ダイヤ1 */
    public static final Uri CONTENT_URI_H1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_1);
    /** 平日 列車ダイヤ2 */
    public static final Uri CONTENT_URI_H2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_2);
    /** 平日 列車ダイヤ3 */
    public static final Uri CONTENT_URI_H3 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_3);
    /** 平日 列車ダイヤ4 */
    public static final Uri CONTENT_URI_H4 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_4);
    /** 平日 列車ダイヤ5 */
    public static final Uri CONTENT_URI_H5 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_5);
    /** 平日 列車ダイヤ6 */
    public static final Uri CONTENT_URI_H6 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_6);
    /** 平日 列車ダイヤ7 */
    public static final Uri CONTENT_URI_H7 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_7);
    /** 平日 列車ダイヤ11 */
    public static final Uri CONTENT_URI_H11 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_11);
    /** 平日 列車ダイヤ12 */
    public static final Uri CONTENT_URI_H12 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_12);
    /** 平日 列車ダイヤ21 */
    public static final Uri CONTENT_URI_H21 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_21);
    /** 平日 列車ダイヤ22 */
    public static final Uri CONTENT_URI_H22 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_22);
    /** 平日 列車ダイヤ25 */
    public static final Uri CONTENT_URI_H25 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_25);
    /** 平日 列車ダイヤ26 */
    public static final Uri CONTENT_URI_H26 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_26);

    /** 休日 列車ダイヤ1 */
    public static final Uri CONTENT_URI_K1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_1);
    /** 休日 列車ダイヤ2 */
    public static final Uri CONTENT_URI_K2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_2);
    /** 休日 列車ダイヤ3 */
    public static final Uri CONTENT_URI_K3 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_3);
    /** 休日 列車ダイヤ4 */
    public static final Uri CONTENT_URI_K4 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_4);
    /** 休日 列車ダイヤ5 */
    public static final Uri CONTENT_URI_K5 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_5);
    /** 休日 列車ダイヤ6 */
    public static final Uri CONTENT_URI_K6 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_6);
    /** 休日 列車ダイヤ11 */
    public static final Uri CONTENT_URI_K11 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_11);
    /** 休日 列車ダイヤ12 */
    public static final Uri CONTENT_URI_K12 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_12);
    /** 休日 列車ダイヤ21 */
    public static final Uri CONTENT_URI_K21 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_21);
    /** 休日 列車ダイヤ22 */
    public static final Uri CONTENT_URI_K22 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_22);
    /** 休日 列車ダイヤ25 */
    public static final Uri CONTENT_URI_K25 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_25);
    /** 休日 列車ダイヤ26 */
    public static final Uri CONTENT_URI_K26 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_26);

    /** 休日 千城台駅 上り */
    public static final Uri CONTENT_URI_KS1U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_1U);
    /** 休日 千城台北駅 上り */
    public static final Uri CONTENT_URI_KS2U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_2U);
    /** 休日 小倉台駅 上り */
    public static final Uri CONTENT_URI_KS3U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_3U);
    /** 休日 桜木駅 上り */
    public static final Uri CONTENT_URI_KS4U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_4U);
    /** 休日 都賀駅 上り */
    public static final Uri CONTENT_URI_KS5U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_5U);
    /** 休日 みつわ台駅 上り */
    public static final Uri CONTENT_URI_KS6U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_6U);
    /** 休日 動物公園駅 上り */
    public static final Uri CONTENT_URI_KS7U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_7U);
    /** 休日 スポーツセンター駅 上り */
    public static final Uri CONTENT_URI_KS8U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_8U);
    /** 休日 穴川駅 上り */
    public static final Uri CONTENT_URI_KS9U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_9U);
    /** 休日 天台駅 上り */
    public static final Uri CONTENT_URI_KS10U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_10U);
    /** 休日 作草部駅 上り */
    public static final Uri CONTENT_URI_KS11U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_11U);
    /** 休日 千葉公園駅 上り */
    public static final Uri CONTENT_URI_KS12U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_12U);
    /** 休日 県庁前駅 上り */
    public static final Uri CONTENT_URI_KS13U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_13U);
    /** 休日 葭川公園駅 上り */
    public static final Uri CONTENT_URI_KS14U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_14U);
    /** 休日 栄町駅 上り */
    public static final Uri CONTENT_URI_KS15U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_15U);
    /** 休日 千葉駅 上り */
    public static final Uri CONTENT_URI_KS16U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_16U);
    /** 休日 市役所前駅 上り */
    public static final Uri CONTENT_URI_KS17U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_17U);

    /** 休日 千城台北駅 下り */
    public static final Uri CONTENT_URI_KS2D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_2D);
    /** 休日 小倉台駅 下り */
    public static final Uri CONTENT_URI_KS3D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_3D);
    /** 休日 桜木駅 下り */
    public static final Uri CONTENT_URI_KS4D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_4D);
    /** 休日 都賀駅 下り */
    public static final Uri CONTENT_URI_KS5D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_5D);
    /** 休日 みつわ台駅 下り */
    public static final Uri CONTENT_URI_KS6D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_6D);
    /** 休日 動物公園駅 下り */
    public static final Uri CONTENT_URI_KS7D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_7D);
    /** 休日 スポーツセンター駅 下り */
    public static final Uri CONTENT_URI_KS8D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_8D);
    /** 休日 穴川駅 下り */
    public static final Uri CONTENT_URI_KS9D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_9D);
    /** 休日 天台駅 下り */
    public static final Uri CONTENT_URI_KS10D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_10D);
    /** 休日 作草部駅 下り */
    public static final Uri CONTENT_URI_KS11D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_11D);
    /** 休日 千葉公園駅 下り */
    public static final Uri CONTENT_URI_KS12D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_12D);
    /** 休日 葭川公園駅 下り */
    public static final Uri CONTENT_URI_KS14D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_14D);
    /** 休日 栄町駅 下り */
    public static final Uri CONTENT_URI_KS15D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_15D);
    /** 休日 千葉駅1号線 下り */
    public static final Uri CONTENT_URI_KS16D1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_16D1);
    /** 休日 千葉駅2号線 下り */
    public static final Uri CONTENT_URI_KS16D2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_16D2);
    /** 休日 市役所前駅 下り */
    public static final Uri CONTENT_URI_KS17D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_17D);
    /** 休日 千葉みなと駅 下り */
    public static final Uri CONTENT_URI_KS18D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_18D);

    /** 平日 千城台駅 上り */
    public static final Uri CONTENT_URI_HS1U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_1U);
    /** 平日 千城台北駅 上り */
    public static final Uri CONTENT_URI_HS2U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_2U);
    /** 平日 小倉台駅 上り */
    public static final Uri CONTENT_URI_HS3U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_3U);
    /** 平日 桜木駅 上り */
    public static final Uri CONTENT_URI_HS4U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_4U);
    /** 平日 都賀駅 上り */
    public static final Uri CONTENT_URI_HS5U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_5U);
    /** 平日 みつわ台駅 上り */
    public static final Uri CONTENT_URI_HS6U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_6U);
    /** 平日 動物公園駅 上り */
    public static final Uri CONTENT_URI_HS7U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_7U);
    /** 平日 スポーツセンター駅 上り */
    public static final Uri CONTENT_URI_HS8U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_8U);
    /** 平日 穴川駅 上り */
    public static final Uri CONTENT_URI_HS9U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_9U);
    /** 平日 天台駅 上り */
    public static final Uri CONTENT_URI_HS10U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_10U);
    /** 平日 作草部駅 上り */
    public static final Uri CONTENT_URI_HS11U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_11U);
    /** 平日 千葉公園駅 上り */
    public static final Uri CONTENT_URI_HS12U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_12U);
    /** 平日 県庁前駅 上り */
    public static final Uri CONTENT_URI_HS13U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_13U);
    /** 平日 葭川公園駅 上り */
    public static final Uri CONTENT_URI_HS14U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_14U);
    /** 平日 栄町駅 上り */
    public static final Uri CONTENT_URI_HS15U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_15U);
    /** 平日 千葉駅 上り */
    public static final Uri CONTENT_URI_HS16U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_16U);
    /** 平日 市役所前駅 上り */
    public static final Uri CONTENT_URI_HS17U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_17U);

    /** 平日 千城台北駅 下り */
    public static final Uri CONTENT_URI_HS2D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_2D);
    /** 平日 小倉台駅 下り */
    public static final Uri CONTENT_URI_HS3D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_3D);
    /** 平日 桜木駅 下り */
    public static final Uri CONTENT_URI_HS4D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_4D);
    /** 平日 都賀駅 下り */
    public static final Uri CONTENT_URI_HS5D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_5D);
    /** 平日 みつわ台駅 下り */
    public static final Uri CONTENT_URI_HS6D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_6D);
    /** 平日 動物公園駅 下り */
    public static final Uri CONTENT_URI_HS7D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_7D);
    /** 平日 スポーツセンター 下り */
    public static final Uri CONTENT_URI_HS8D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_8D);
    /** 平日 穴川駅 下り */
    public static final Uri CONTENT_URI_HS9D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_9D);
    /** 平日 天台駅 下り */
    public static final Uri CONTENT_URI_HS10D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_10D);
    /** 平日 作草部駅 下り */
    public static final Uri CONTENT_URI_HS11D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_11D);
    /** 平日 千葉公園駅 下り */
    public static final Uri CONTENT_URI_HS12D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_12D);
    /** 平日 葭川公園駅 下り */
    public static final Uri CONTENT_URI_HS14D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_14D);
    /** 平日 栄町駅 下り */
    public static final Uri CONTENT_URI_HS15D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_15D);
    /** 平日 千葉駅1号線 下り */
    public static final Uri CONTENT_URI_HS16D1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_16D1);
    /** 平日 千葉駅2号線 下り */
    public static final Uri CONTENT_URI_HS16D2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_16D2);
    /** 平日 市役所前駅 下り */
    public static final Uri CONTENT_URI_HS17D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_17D);
    /** 平日 千葉みなと駅 下り */
    public static final Uri CONTENT_URI_HS18D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_18D);

    /** 時刻表のアイテム詳細 */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/Item");

    /** プロバイダ仕様での名前 */
    private static final String PROVIDER_SPECIFIC_NAME = AUTHORITY;
    /** プロバイダ仕様でのコマンド名 */
    private static final String PROVIDER_SPECIFIC_TYPE = PATH_HOLIDAY_TRAIN_1;

    /** 単一行のTableのMIME type */
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd." + PROVIDER_SPECIFIC_NAME + "." + PROVIDER_SPECIFIC_TYPE;
    /** 複数行のTableのMIME Type */
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd." + PROVIDER_SPECIFIC_NAME + "." + PROVIDER_SPECIFIC_TYPE;

    /** 発車時刻 */
    public static final String COLUMN_TIME = "TIMES";
    /** 発車駅 */
    public static final String COLUMN_STATION = "STATION";
    /** 行き先 */
    public static final String COLUMN_DESTINATION = "DESTINATION";
    /** 祝日/平日 時刻表フラグ */
    public static final String COLUMN_HOLIDAY = "HOLIDAY";
    /** 車両NO */
    public static final String COLUMN_TABLE_NO ="TABLE_NO";
    /** 上り/下り */
    public static final String COLUMN_UPDOWN ="UPDOWN";
    /** 発車時刻(時) */
    public static final String COLUMN_HOUR ="HOURS";
    /** 発車時刻(分) */
    public static final String COLUMN_MINUTE = "MINUTES";
    /** 注記記号 */
    public static final String COLUMN_ANNOTATION = "ANNOTATION";
}
