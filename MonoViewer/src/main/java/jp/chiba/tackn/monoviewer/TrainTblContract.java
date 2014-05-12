package jp.chiba.tackn.monoviewer;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 車両時刻表を取り扱うプロバイダの規約
 *
 * @author Takumi Ito
 * @since 2014/05/12.
 */
public class TrainTblContract implements BaseColumns {

    /** パッケージ名 */
    public static final String AUTHORITY = "jp.chiba.tackn.monoviewer";
    /** プロバイダの提供するコマンド*/

    public static final String PATH_HOLIDAY_TRAIN_1 = "KA1";
    public static final String PATH_HOLIDAY_TRAIN_2 = "KA2";
    public static final String PATH_HOLIDAY_TRAIN_3 = "KA3";
    public static final String PATH_HOLIDAY_TRAIN_4 = "KA4";
    public static final String PATH_HOLIDAY_TRAIN_5 = "KA5";
    public static final String PATH_HOLIDAY_TRAIN_6 = "KA6";
    public static final String PATH_HOLIDAY_TRAIN_11 = "KA11";
    public static final String PATH_HOLIDAY_TRAIN_12 = "KA12";
    public static final String PATH_HOLIDAY_TRAIN_21 = "KA21";
    public static final String PATH_HOLIDAY_TRAIN_22 = "KA22";
    public static final String PATH_HOLIDAY_TRAIN_25 = "KA23";
    public static final String PATH_HOLIDAY_TRAIN_26 = "KA26";

    public static final String PATH_WEEKDAY_TRAIN_1 = "HA1";
    public static final String PATH_WEEKDAY_TRAIN_2 = "HA2";
    public static final String PATH_WEEKDAY_TRAIN_3 = "HA3";
    public static final String PATH_WEEKDAY_TRAIN_4 = "HA4";
    public static final String PATH_WEEKDAY_TRAIN_5 = "HA5";
    public static final String PATH_WEEKDAY_TRAIN_6 = "HA6";
    public static final String PATH_WEEKDAY_TRAIN_7 = "HA7";
    public static final String PATH_WEEKDAY_TRAIN_11 = "HA11";
    public static final String PATH_WEEKDAY_TRAIN_12 = "HA12";
    public static final String PATH_WEEKDAY_TRAIN_21 = "HA21";
    public static final String PATH_WEEKDAY_TRAIN_22 = "HA22";
    public static final String PATH_WEEKDAY_TRAIN_25 = "HA25";
    public static final String PATH_WEEKDAY_TRAIN_26 = "HA26";

    public static final String PATH_WEEKDAY_STATION_1U = "HS1U";
    public static final String PATH_WEEKDAY_STATION_2U = "HS2U";
    public static final String PATH_WEEKDAY_STATION_3U = "HS3U";
    public static final String PATH_WEEKDAY_STATION_4U = "HS4U";
    public static final String PATH_WEEKDAY_STATION_5U = "HS5U";
    public static final String PATH_WEEKDAY_STATION_6U = "HS6U";
    public static final String PATH_WEEKDAY_STATION_7U = "HS7U";
    public static final String PATH_WEEKDAY_STATION_8U = "HS8U";
    public static final String PATH_WEEKDAY_STATION_9U = "HS9U";
    public static final String PATH_WEEKDAY_STATION_10U = "HS10U";
    public static final String PATH_WEEKDAY_STATION_11U = "HS11U";
    public static final String PATH_WEEKDAY_STATION_12U = "HS12U";
    public static final String PATH_WEEKDAY_STATION_13U = "HS13U";
    public static final String PATH_WEEKDAY_STATION_14U = "HS14U";
    public static final String PATH_WEEKDAY_STATION_15U = "HS15U";
    public static final String PATH_WEEKDAY_STATION_16U = "HS16U";
    public static final String PATH_WEEKDAY_STATION_17U = "HS17U";

    public static final String PATH_WEEKDAY_STATION_1D = "HS1D";
    public static final String PATH_WEEKDAY_STATION_2D = "HS2D";
    public static final String PATH_WEEKDAY_STATION_3D = "HS3D";
    public static final String PATH_WEEKDAY_STATION_4D = "HS4D";
    public static final String PATH_WEEKDAY_STATION_5D = "HS5D";
    public static final String PATH_WEEKDAY_STATION_6D = "HS6D";
    public static final String PATH_WEEKDAY_STATION_7D = "HS7D";
    public static final String PATH_WEEKDAY_STATION_8D = "HS8D";
    public static final String PATH_WEEKDAY_STATION_9D = "HS9D";
    public static final String PATH_WEEKDAY_STATION_10D = "HS10D";
    public static final String PATH_WEEKDAY_STATION_11D = "HS11D";
    public static final String PATH_WEEKDAY_STATION_12D = "HS12D";
    public static final String PATH_WEEKDAY_STATION_14D = "HS14D";
    public static final String PATH_WEEKDAY_STATION_15D = "HS15D";
    public static final String PATH_WEEKDAY_STATION_16D1 = "HS16D1";
    public static final String PATH_WEEKDAY_STATION_16D2 = "HS16D2";
    public static final String PATH_WEEKDAY_STATION_17D = "HS17D";
    public static final String PATH_WEEKDAY_STATION_18D = "HS18D";

    public static final String PATH_HOLIDAY_STATION_1U = "KS1U";
    public static final String PATH_HOLIDAY_STATION_2U = "KS2U";
    public static final String PATH_HOLIDAY_STATION_3U = "KS3U";
    public static final String PATH_HOLIDAY_STATION_4U = "KS4U";
    public static final String PATH_HOLIDAY_STATION_5U = "KS5U";
    public static final String PATH_HOLIDAY_STATION_6U = "KS6U";
    public static final String PATH_HOLIDAY_STATION_7U = "KS7U";
    public static final String PATH_HOLIDAY_STATION_8U = "KS8U";
    public static final String PATH_HOLIDAY_STATION_9U = "KS9U";
    public static final String PATH_HOLIDAY_STATION_10U = "KS10U";
    public static final String PATH_HOLIDAY_STATION_11U = "KS11U";
    public static final String PATH_HOLIDAY_STATION_12U = "KS12U";
    public static final String PATH_HOLIDAY_STATION_13U = "KS13U";
    public static final String PATH_HOLIDAY_STATION_14U = "KS14U";
    public static final String PATH_HOLIDAY_STATION_15U = "KS15U";
    public static final String PATH_HOLIDAY_STATION_16U = "KS16U";
    public static final String PATH_HOLIDAY_STATION_17U = "KS17U";

    public static final String PATH_HOLIDAY_STATION_1D = "KS1D";
    public static final String PATH_HOLIDAY_STATION_2D = "KS2D";
    public static final String PATH_HOLIDAY_STATION_3D = "KS3D";
    public static final String PATH_HOLIDAY_STATION_4D = "KS4D";
    public static final String PATH_HOLIDAY_STATION_5D = "KS5D";
    public static final String PATH_HOLIDAY_STATION_6D = "KS6D";
    public static final String PATH_HOLIDAY_STATION_7D = "KS7D";
    public static final String PATH_HOLIDAY_STATION_8D = "KS8D";
    public static final String PATH_HOLIDAY_STATION_9D = "KS9D";
    public static final String PATH_HOLIDAY_STATION_10D = "KS10D";
    public static final String PATH_HOLIDAY_STATION_11D = "KS11D";
    public static final String PATH_HOLIDAY_STATION_12D = "KS12D";
    public static final String PATH_HOLIDAY_STATION_14D = "KS14D";
    public static final String PATH_HOLIDAY_STATION_15D = "KS15D";
    public static final String PATH_HOLIDAY_STATION_16D1 = "KS16D1";
    public static final String PATH_HOLIDAY_STATION_16D2 = "KS16D2";
    public static final String PATH_HOLIDAY_STATION_17D = "KS17D";
    public static final String PATH_HOLIDAY_STATION_18D = "KS18D";


    /** コンテンツプロバイダが提供するデータベースを示すURI */

    public static final Uri CONTENT_URI_H1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_1);
    public static final Uri CONTENT_URI_H2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_2);
    public static final Uri CONTENT_URI_H3 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_3);
    public static final Uri CONTENT_URI_H4 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_4);
    public static final Uri CONTENT_URI_H5 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_5);
    public static final Uri CONTENT_URI_H6 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_6);
    public static final Uri CONTENT_URI_H7 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_7);
    public static final Uri CONTENT_URI_H11 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_11);
    public static final Uri CONTENT_URI_H12 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_12);
    public static final Uri CONTENT_URI_H21 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_21);
    public static final Uri CONTENT_URI_H22 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_22);
    public static final Uri CONTENT_URI_H25 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_25);
    public static final Uri CONTENT_URI_H26 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_TRAIN_26);

    public static final Uri CONTENT_URI_K1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_1);
    public static final Uri CONTENT_URI_K2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_2);
    public static final Uri CONTENT_URI_K3 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_3);
    public static final Uri CONTENT_URI_K4 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_4);
    public static final Uri CONTENT_URI_K5 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_5);
    public static final Uri CONTENT_URI_K6 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_6);
    public static final Uri CONTENT_URI_K11 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_11);
    public static final Uri CONTENT_URI_K12 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_12);
    public static final Uri CONTENT_URI_K21 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_21);
    public static final Uri CONTENT_URI_K22 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_22);
    public static final Uri CONTENT_URI_K25 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_25);
    public static final Uri CONTENT_URI_K26 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_TRAIN_26);

    public static final Uri CONTENT_URI_KS1U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_1U);
    public static final Uri CONTENT_URI_KS2U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_2U);
    public static final Uri CONTENT_URI_KS3U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_3U);
    public static final Uri CONTENT_URI_KS4U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_4U);
    public static final Uri CONTENT_URI_KS5U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_5U);
    public static final Uri CONTENT_URI_KS6U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_6U);
    public static final Uri CONTENT_URI_KS7U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_7U);
    public static final Uri CONTENT_URI_KS8U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_8U);
    public static final Uri CONTENT_URI_KS9U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_9U);
    public static final Uri CONTENT_URI_KS10U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_10U);
    public static final Uri CONTENT_URI_KS11U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_11U);
    public static final Uri CONTENT_URI_KS12U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_12U);
    public static final Uri CONTENT_URI_KS13U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_13U);
    public static final Uri CONTENT_URI_KS14U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_14U);
    public static final Uri CONTENT_URI_KS15U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_15U);
    public static final Uri CONTENT_URI_KS16U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_16U);
    public static final Uri CONTENT_URI_KS17U = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_17U);

    public static final Uri CONTENT_URI_KS1D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_1D);
    public static final Uri CONTENT_URI_KS2D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_2D);
    public static final Uri CONTENT_URI_KS3D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_3D);
    public static final Uri CONTENT_URI_KS4D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_4D);
    public static final Uri CONTENT_URI_KS5D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_5D);
    public static final Uri CONTENT_URI_KS6D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_6D);
    public static final Uri CONTENT_URI_KS7D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_7D);
    public static final Uri CONTENT_URI_KS8D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_8D);
    public static final Uri CONTENT_URI_KS9D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_9D);
    public static final Uri CONTENT_URI_KS10D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_10D);
    public static final Uri CONTENT_URI_KS11D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_11D);
    public static final Uri CONTENT_URI_KS12D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_12D);
    public static final Uri CONTENT_URI_KS14D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_14D);
    public static final Uri CONTENT_URI_KS15D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_15D);
    public static final Uri CONTENT_URI_KS16D1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_16D1);
    public static final Uri CONTENT_URI_KS16D2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_16D2);
    public static final Uri CONTENT_URI_KS17D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_17D);
    public static final Uri CONTENT_URI_KS18D = Uri.parse("content://" + AUTHORITY + "/" + PATH_HOLIDAY_STATION_18D);

    public static final Uri CONTENT_URI_HS1U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_1U);
    public static final Uri CONTENT_URI_HS2U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_2U);
    public static final Uri CONTENT_URI_HS3U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_3U);
    public static final Uri CONTENT_URI_HS4U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_4U);
    public static final Uri CONTENT_URI_HS5U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_5U);
    public static final Uri CONTENT_URI_HS6U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_6U);
    public static final Uri CONTENT_URI_HS7U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_7U);
    public static final Uri CONTENT_URI_HS8U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_8U);
    public static final Uri CONTENT_URI_HS9U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_9U);
    public static final Uri CONTENT_URI_HS10U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_10U);
    public static final Uri CONTENT_URI_HS11U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_11U);
    public static final Uri CONTENT_URI_HS12U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_12U);
    public static final Uri CONTENT_URI_HS13U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_13U);
    public static final Uri CONTENT_URI_HS14U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_14U);
    public static final Uri CONTENT_URI_HS15U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_15U);
    public static final Uri CONTENT_URI_HS16U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_16U);
    public static final Uri CONTENT_URI_HS17U = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_17U);

    public static final Uri CONTENT_URI_HS1D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_1D);
    public static final Uri CONTENT_URI_HS2D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_2D);
    public static final Uri CONTENT_URI_HS3D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_3D);
    public static final Uri CONTENT_URI_HS4D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_4D);
    public static final Uri CONTENT_URI_HS5D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_5D);
    public static final Uri CONTENT_URI_HS6D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_6D);
    public static final Uri CONTENT_URI_HS7D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_7D);
    public static final Uri CONTENT_URI_HS8D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_8D);
    public static final Uri CONTENT_URI_HS9D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_9D);
    public static final Uri CONTENT_URI_HS10D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_10D);
    public static final Uri CONTENT_URI_HS11D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_11D);
    public static final Uri CONTENT_URI_HS12D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_12D);
    public static final Uri CONTENT_URI_HS14D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_14D);
    public static final Uri CONTENT_URI_HS15D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_15D);
    public static final Uri CONTENT_URI_HS16D1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_16D1);
    public static final Uri CONTENT_URI_HS16D2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_16D2);
    public static final Uri CONTENT_URI_HS17D = Uri.parse("content://" + AUTHORITY + "/" + PATH_WEEKDAY_STATION_17D);
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
    public static final String TIME = "TIMES";
    /** 発車駅 */
    public static final String STATION = "STATION";
    /** 行き先 */
    public static final String DESTINATION = "DESTINATION";
    /** 祝日/平日 時刻表フラグ */
    public static final String HOLIDAY = "HOLIDAY";
    /** 車両NO */
    public static final String TABLE_NO="TABLE_NO";
    /** 上り/下り */
    public static final String UPDOWN="UPDOWN";

    public static final String HOUR ="HOURS";

    public static final String MINUTE = "MINUTES";
}
