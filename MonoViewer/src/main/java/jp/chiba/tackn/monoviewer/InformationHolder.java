package jp.chiba.tackn.monoviewer;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * 当時の運行情報を保持するクラス
 * @author Takumi Ito
 * @since 2014/05/18
 */
public class InformationHolder extends AsyncTask<Uri.Builder, Void, String> {

    /** デバッグ用タグ */
    private static final String TAG = "TodayInformation";
    /** デバッグ用フラグ */
    private static final boolean DEBUG = false;

    /** 休日・平日フラグ */
    private String holiday;
    /** モノちゃん号の運行情報 */
    private String Service0;
    /** アーバンフライ０ 1-2号の運行情報 */
    private String Service1;
    /** アーバンフライ０ 3-4号の運行情報 */
    private String Service2;
    /** アーバンフライ０ 5-6号の運行情報 */
    private String Service3;
    /** アーバンフライ０ 7-8号の運行情報 */
    private String Service4;
    /** シングルトン用 */
    private static InformationHolder singleton;
    /** データ読み込み完了フラグ */
    private boolean isReady;
    /** 情報を取得した時間 */
    private long updateTime;
    /** 取得時間のチェック用Date */
    private Date updateDate = new Date();
    /** 取得時間のチェック用Calendar */
    private Calendar updateCalendar = Calendar.getInstance();
    /** 現在時刻取得用 */
    Date nowDate = new Date();
    /** 現在時刻取得用 */
    Calendar nowCalendar = Calendar.getInstance();

    /** シングルトンコンストラクタ */
    private InformationHolder(){
        this.isReady=false;
    }

    /** シングルトンインスタンス */
    public static synchronized InformationHolder getInstance(){
        if(singleton==null){
            singleton = new InformationHolder();
            //SyncTaskでherokuから情報取得
            Uri.Builder builder = new Uri.Builder();
            singleton.execute(builder);
        }
        return singleton;
    }

    /**
     * 非同期処理
     * @param builders 引数
     * @return 成功したか失敗したか
     */
    @Override
    protected String doInBackground(Uri.Builder... builders) {

        try {
            /* XML 扱うためのファクトリ */
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            /* XML 扱うためのビルダー */
            DocumentBuilder xmlbuilder = dbFactory.newDocumentBuilder();
            Document document = xmlbuilder.parse("http://monoview.herokuapp.com/today.xml");

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();

            holiday = xpath.evaluate("//holiday[1]/text()", document);
            Service0 = xpath.evaluate("/tables/table[train ='0']/table/text()", document);
            Service1 = xpath.evaluate("/tables/table[train ='1']/table/text()", document);
            Service2 = xpath.evaluate("/tables/table[train ='2']/table/text()", document);
            Service3 = xpath.evaluate("/tables/table[train ='3']/table/text()", document);
            Service4 = xpath.evaluate("/tables/table[train ='4']/table/text()", document);

        } catch (SAXException e) {
            return "false";
        } catch (IOException e) {
            return "false";
        } catch (ParserConfigurationException e) {
            return "false";
        } catch (XPathExpressionException e) {
            return "false";
        }

        if(DEBUG) Log.d(TAG, "result(holiday): " + holiday);

        return "true";
    }

    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {
        updateTime = System.currentTimeMillis();
        updateDate.setTime(updateTime);
        updateCalendar.setTime(updateDate);
        isReady=result.equals("true");
    }

    /**
     * 非同期処理が完了しているかどうか返すメソッド
     * @return 完了していればTrue,完了していない場合はfalse
     */
    public boolean isReady(){

        return this.isReady;
    }

    /**
     * 平日・休日を取得
     * 準備が出来ていない場合は空文字を返す
     * @return 休日ならば"True",平日ならば"false"
     */
    public String getHoliday(){
        checkUpdate();
        return (isReady)?this.holiday:"";
    }

    /**
     * モノちゃん号の運行情報
     * 準備が出来ていない場合は空文字を返す
     * @return 列車運行ダイヤ番号
     */
    public String getService0(){
        checkUpdate();
        return (isReady)?this.Service0:"";
    }

    /**
     * アーバンフライ０ 1-2号の運行情報
     * 準備が出来ていない場合は空文字を返す
     * @return 列車運行ダイヤ番号
     */
    public String getService1(){
        checkUpdate();
        return (isReady)?this.Service1:"";
    }

    /**
     * アーバンフライ０ 3-4号の運行情報
     * 準備が出来ていない場合は空文字を返す
     * @return 列車運行ダイヤ番号
     */
    public String getService2(){
        checkUpdate();
        return (isReady)?this.Service2:"";
    }

    /**
     * アーバンフライ０ 5-6号の運行情報
     * 準備が出来ていない場合は空文字を返す
     * @return 列車運行ダイヤ番号
     */
    public String getService3(){
        checkUpdate();
        return (isReady)?this.Service3:"";
    }

    /**
     * アーバンフライ０ 7-8号の運行情報
     * 準備が出来ていない場合は空文字を返す
     * @return 列車運行ダイヤ番号
     */
    public String getService4(){
        checkUpdate();
        return (isReady)?this.Service4:"";
    }

    /**
     * インスタンスを破棄して再度非同期処理を実行する場合に利用
     */
    public void reload(){
        singleton = null;
        singleton = InformationHolder.getInstance();
    }

    /**
     * 運行情報を取得した時刻
     */
    public long getUpdateTime(){
        return this.updateTime;
    }

    /**
     * データが最新のモノかチェックを行う
     */
    private synchronized void checkUpdate(){
        nowDate.setTime(System.currentTimeMillis());
        nowCalendar.setTime(nowDate);

        //同じ日ならキャッシュでOK
        if(updateCalendar.get(Calendar.DAY_OF_YEAR)==nowCalendar.get(Calendar.DAY_OF_YEAR))return;
        //0時の場合で前日のデータがあればOK
        if(nowCalendar.get(Calendar.HOUR_OF_DAY)==0
                && updateCalendar.get(Calendar.DAY_OF_YEAR
        ) == nowCalendar.get(Calendar.DAY_OF_YEAR)-1)return;

        //データが古いのでリロード
        reload();
    }
}
