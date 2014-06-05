package jp.chiba.tackn.monoviewer;

import com.google.android.gms.maps.GoogleMap;

/**
 * 画面モードの判定保持用
 * @author Takumi Ito
 * @since 2014/06/04
 */
public class TabletHolder {

    /** デバッグ用タグ */
    private static final String TAG = "TabletHolder";
    /** デバッグ用フラグ */
    private static final boolean DEBUG = false;

    /** タブレットモードフラグ */
    private boolean isTablet = false;

    /**  */
    private GoogleMap map;

    /** シングルトン用 */
    private static TabletHolder singleton;

    /** シングルトンコンストラクタ */
    private TabletHolder(){
    }

    /** シングルトンインスタンス */
    public static synchronized TabletHolder getInstance(){
        if(singleton==null){
            singleton = new TabletHolder();
        }
        return singleton;
    }

    /**
     * タブレットかどうか
     * @return タブレットモードの場合True
     */
    public boolean isTablet(){
        return isTablet;
    }

    /**
     * タブレットかどうか
     */
    public void isTablet(boolean isTablet){
        this.isTablet = isTablet;
    }

    /**
     * GoogleMapオブジェクト保持
     */
    public void setMap(GoogleMap map) {
        this.map = map;
    }

    /**
     * GoogleMapオブジェクトの取得
     * @return GoogleMapオブジェクト
     */
    public GoogleMap getMap(){
        return this.map;
    }
}
