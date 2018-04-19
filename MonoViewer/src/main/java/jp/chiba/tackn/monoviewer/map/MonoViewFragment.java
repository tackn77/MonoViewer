package jp.chiba.tackn.monoviewer.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.chiba.tackn.monoviewer.R;
import jp.chiba.tackn.monoviewer.TabletHolder;
import jp.chiba.tackn.monoviewer.data.SQLTblContract;
import jp.chiba.tackn.monoviewer.time.TimeTable;
import jp.chiba.tackn.monoviewer.train.TrainTable;

/**
 * 列車の運行状況を見るためのMapFragment
 * GoogleMapのMapFragmnetを内包したFragmnet
 * @author Takumi Ito
 * @since 2014/05/16
 */
public class MonoViewFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener
        , GoogleMap.OnCameraChangeListener
        , LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback {
    /** デバッグ用タグ */
    private static final String TAG = "MonoViewFragment";
    /** デバッグ用フラグ */
    private static final boolean DEBUG =false;

    /** Mapオブジェクト */
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    /** MapFragment */
    private MapFragment mapFragment;
    /** 呼び出し元Activityのcontext */
    private Context context;
    /** 定期実行用ハンドラ */
    private TrainHandler trainHandler;
    /** CursorLoader用コールバック */
    private LoaderManager.LoaderCallbacks callbacks;
    /** 非同期処理で取得した休日フラグ */
    private static int intHoliday;
    /** モノちゃん号の運行情報 */
    static private int intService0;
    /** アーバンフライ０ 1-2号の運行情報 */
    static private int intService1;
    /** アーバンフライ０ 3-4号の運行情報 */
    static private int intService2;
    /** アーバンフライ０ 5-6号の運行情報 */
    static private int intService3;
    /** アーバンフライ０ 7-8号の運行情報 */
    static private int intService4;
    /** 現在時刻クエリ用セット用Date */
    private java.util.Date Date = new Date();
    /** 時刻取得用カレンダ */
    private Calendar calendar = Calendar.getInstance();
    /** 列車マーカーを保持するList */
    private final Map<NowTrainData,Marker> trainMakers = new HashMap<NowTrainData, Marker>();
    /** 駅マーカー判定用正規表現 */
    private static final Pattern patternStation = Pattern.compile("^[^駅]+駅$");
    /** 列車マーカー判定用正規表現 */
    private static final Pattern patternTrain = Pattern.compile("^(上り|下り) [^ ]+ ([0-9]+):([0-9]+) ([0-9]+)");
    /** Google Mapの中心座標 */
    private static LatLng mapCenter = Station.STATION_CHIBA;
    /** タブレットモードの保持 */
    private TabletHolder tabletHolder = TabletHolder.getInstance();
    /** モノレールのインフォウィンドウをクリック時のリスナ */
    private OnFragmentInteractionListener mListener;
    /** モノレールの駅のマーカーを保持 */
    private List<Marker> stationMarker = new ArrayList<Marker>();
    /** マーカーのリソースファイル読み込みサンプリングレート */
    private int SampleRate = 1;
    /** Zoom変更対応のマーカーのリライトフラグ */
    private boolean markerRewrite = false;
    /** Zoom変更時検出用変更前値 */
    private float oldZoom;

    public MonoViewFragment() {
        // Required empty public constructor
    }

    /**
     * 作成時
     * パラメータの受け処理
     * 初期化処理
     * @param savedInstanceState 環境保存
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        //SyncTaskでherokuから情報取得
        Uri.Builder builder = new Uri.Builder();
        AsyncHttpRequest task = new AsyncHttpRequest();
        task.execute(builder);
        callbacks = this;
        setUpMapIfNeeded();
        setUpHandler();
        if(savedInstanceState!=null){
            mapCenter = new LatLng(savedInstanceState.getDouble("centerLat"),savedInstanceState.getDouble("centerLong"));
        }
    }

    /**
     * Viewの作成
     * @param inflater レイアウト読み込みオブジェクト
     * @param container viewを配置するコンテナ
     * @param savedInstanceState 引数
     * @return 整形したFragmentのView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monoview, container, false);
        if (DEBUG) Log.d(TAG, "onCreateView " + view);
        return view;
    }

    /**
     * リジューム時
     * Mapとハンドラの作成
     */
    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpHandler();
    }

    /**
     * 環境保存
     * @param outState 保存するBundle
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapCenter = mMap.getCameraPosition().target;
        outState.putDouble("centerLat",mapCenter.latitude);
        outState.putDouble("centerLong",mapCenter.longitude);
    }

    /**
     * 定期実行用のハンドラのセットアップ
     */
    private void setUpHandler() {
        if (trainHandler == null) {
            trainHandler = new TrainHandler();
            trainHandler.sleep(500); //0.5s
        }
    }

    /**
     * GoogleMapオブジェクトの取得
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // 新しいフラグメントとトランザクションを作成する
            if (mapFragment == null) {
                mapFragment = MapFragment.newInstance();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.maps_fragment_container, mapFragment);
                transaction.commit();
            }

            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        tabletHolder.setMap(mMap);

        if (DEBUG) Log.d(TAG, "setUpMapIfNeeded()$mapFragment " + mapFragment);
        if (DEBUG) Log.d(TAG, "setUpMapIfNeeded()$mMap " + mMap);

        if (mMap != null) {
            setUpMap();
        }
    }

    /**
     * マップの初期設定
     */
    @SuppressLint("MissingPermission")
    private void setUpMap() {
        //駅に時刻表のリンクしたマーカーを設置
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_TISHIRODAI).title("千城台駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_TISHIRODAIKITA).title("千城台北駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_OGURADAI).title("小倉台駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_SAKURAGI).title("桜木駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_TUGA).title("都賀駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_MITUWADAI).title("みつわ台駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_DOUBUTUKOUEN).title("動物公園駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_SPORTSCENTER).title("スポーツセンター駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_ANAGAWA).title("穴川駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_TENDAI).title("天台駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_SAKUSABE).title("作草部駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_CHIBAKOUEN).title("千葉公園駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_KENTYOMAE).title("県庁前駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_YOSHIKAWAKOUEN).title("葭川公園駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_SAKAETYOU).title("栄町駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_CHIBA).title("千葉駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_SHIYAKUSYOMAE).title("市役所前駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        stationMarker.add(mMap.addMarker(new MarkerOptions().position(Station.STATION_CHIBAMINATO).title("千葉みなと駅").icon(BitmapDescriptorFactory.fromResource(R.drawable.station)).anchor(0.5f, 0.5f)));
        // 千葉駅を表示
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.5f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapCenter));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnCameraChangeListener(this);

        // 現在位置表示の有効化
//        mMap.setMyLocationEnabled(true);

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }

    /**
     * アタッチ時
     * リスナの登録
     * @param activity 呼び出し元アクティビティ
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * デタッチ時
     * リスナの解除
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        trainHandler = null;
    }


    /**
     * マーカークリック時の動作
     * 駅時刻表と列車時刻表を開く
     * @param marker クリックしたマーカー
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        //判定用正規表現の結果
        /** 駅マーカー用判定結果 */
        Matcher matcherStation = patternStation.matcher(marker.getTitle());
        /** 列車マーカー用判定結果 */
        Matcher matcherTrain = patternTrain.matcher(marker.getTitle());

        //駅マーカーだった場合
        if(matcherStation.find()) {
            Intent intent = new Intent(context, TimeTable.class);
            if (marker.getTitle().equals("千葉みなと駅")) {
                intent.putExtra("updown", Station.DOWN); //千葉みなとは上りがないので下り
            } else {
                intent.putExtra("updown", Station.UP);
            }
            intent.putExtra("station", marker.getTitle());
            intent.putExtra("holiday", intHoliday);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        //列車マーカーだった場合
        }else if(matcherTrain.find()){
            int tableNo = Integer.parseInt(matcherTrain.group(4));

            if(tabletHolder.isTablet()){
                if (mListener != null) {
                    mListener.onFragmentInteraction(tableNo,intHoliday);
                }
            }else{
                Intent intent;
                intent = new Intent(context, TrainTable.class);
                intent.putExtra("TableNo", tableNo);
                intent.putExtra("holiday", intHoliday);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    /**
     * CursorLoaderの切り替え処理
     * getLoaderManager().restartLoaderで呼ばれる
     * @param id ID
     * @param bundle 引数
     * @return ローダー
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        /** 取得データのソート */
        String orderBy = "TABLE_NO ASC,TIMES ASC";

        switch (id) {
            case 0: //休日
                return new CursorLoader(context, SQLTblContract.CONTENT_NOW_TRAIN_SERVICE_INFOMATION_HOLIDAY, null, null, null, orderBy);
            case 1: //平日
                return new CursorLoader(context, SQLTblContract.CONTENT_NOW_TRAIN_SERVICE_INFOMATION_WEEKEND, null, null, null, orderBy);
            default:
                if (DEBUG) {
                    Log.d(TAG, "id:" + id);
                }
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H1, null, null, null, orderBy);
        }
    }


    /**
     * CursorLoaderの結果取得後の処理
     * 列車ダイヤ(tableNo)単位で最新の物を取得する
     * @param cursorLoader ローダー
     * @param cursor 結果のカーソル
     */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        /** 作業変数 tableNoの一時保持用 */
        int table_no;
        /** 作業変数 tableNoの比較用 */
        int new_table_no;
        /** 取得結果のデータ配列保持用 */
        List<NowTrainData> group = new ArrayList<NowTrainData>();
        /** 作業変数 列車データの一時保存用 */
        NowTrainData old;

        /** 現在時刻 */
        long nowLong = System.currentTimeMillis();

        //現在時刻の取得
        Date.setTime(nowLong);
        calendar.setTime(Date);
        /** 24時表記の現在時刻 */
        int nowHour = (calendar.get(Calendar.HOUR_OF_DAY)==0)?24:calendar.get(Calendar.HOUR_OF_DAY);
        /** 現在の分 */
        int nowMinute = calendar.get(Calendar.MINUTE);
        /** 頭ゼロで連結してint値とした現在時刻 */
        int nowTIME = Integer.valueOf(String.format("%1$02d",nowHour) + String.format("%1$02d",nowMinute));
        /** カーソルの時刻 */
        int workTIME;

        if(DEBUG)Log.d(TAG,"cursor.getCount() " + cursor.getCount());

        NowTrainData work=null;

        //取得データの名寄せ作業
        if (cursor.moveToFirst()) {
            //初期化
            table_no = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_TABLE_NO));
            do {
                new_table_no = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_TABLE_NO));
                workTIME = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_TIME));
//                if(DEBUG)if(workTIME <= nowTIME)Log.d(TAG,"before " + new_table_no + " : " + workTIME + " / " + nowTIME);
//                if(DEBUG)if(nowTIME < workTIME) Log.d(TAG," after " + new_table_no + " : " + nowTIME + " / " + workTIME);

                //table_noが変化したときの値を取得
                if (table_no != new_table_no) {
                    if(work!=null){
                        group.add(work);
                    }else{
                        if(DEBUG)Log.d(TAG,"work==null at table_no != new_table_no " + table_no + " " + new_table_no);
                    }
                    table_no = new_table_no;
                    work=null;
                }
                //一時保持
                old = new NowTrainData(
                        cursor.getString(cursor.getColumnIndex(SQLTblContract.COLUMN_STATION))
                        , cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_UPDOWN))
                        , cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_HOUR))
                        , cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_MINUTE))
                        , cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_TABLE_NO))
                        , null
                );

                if(workTIME <= nowTIME){
                    work=old;
                }else if(nowTIME < workTIME){
                    if(work!=null&&work.after==null)work.after=old;
                }

//                if(DEBUG)Log.d(TAG,old.UpDown + " " + old.Table_No + "/" + old.Hour + ":" + old.Minute + " " + old.Station);
            } while (cursor.moveToNext());
            //最後の１つ
            if(work!=null)group.add(work);
        }

        if(DEBUG)for(NowTrainData cur:group){
            if(cur!=null && cur.after!=null){
                Log.d(TAG,cur.UpDown + " " + cur.Table_No +  "/" + cur.Hour + ":" + cur.Minute + "-" +cur.after.Hour +":" + cur.after.Minute + " " + cur.Station +" - " + cur.after.Station);
            }else if(cur!=null){
                Log.d(TAG,cur.UpDown + " " + cur.Table_No +  "/" + cur.Hour + ":" + cur.Minute + " " + cur.Station);
            }
        }

        if(DEBUG)Log.d(TAG,"group.size() " + group.size());

        // 既に検索対象から外れたマーカーを削除する
        if(DEBUG)Log.d(TAG,"trainMakers.size() :" + trainMakers.size());
        Set<NowTrainData> removeableKeys = new HashSet<NowTrainData>();
        for(NowTrainData item:trainMakers.keySet()){
            removeableKeys.add(item);
        }
        for (NowTrainData temp : group) {
            removeableKeys.remove(temp);
            if (DEBUG) Log.d(TAG, "removeableKeys.remove :" + temp.Station);
        }
        if(DEBUG)Log.d(TAG,"trainMakers.size():" + trainMakers.size());
        if (DEBUG) Log.d(TAG, "removeableKeys.size() :" + removeableKeys.size());
        for (NowTrainData remove : removeableKeys) {
            trainMakers.get(remove).remove();
            trainMakers.remove(remove);
            if (DEBUG) Log.d(TAG, "remove :" + remove.Station);
        }
        if(DEBUG)Log.d(TAG,"trainMakers.size():" + trainMakers.size());

        //マーカーの描写
        if(DEBUG)Log.d(TAG,"size :" + trainMakers.size());
        if (group.size() > 0) {
            for (NowTrainData cur:group) {
                if (mMap != null) {
                    Marker move = trainMakers.get(cur);
                    if(markerRewrite && move!=null) { //カメラのZoomが変更された時マーカ再作成
                        trainMakers.remove(cur);
                        move.remove();
                        move=null;
                    }
                    if(move!=null) {
                        move.setPosition(getPositon(cur));
                    }else{
//                    if(DEBUG)Log.d(TAG,cur.UpDown + " " + cur.Table_No +  "/" + cur.Hour + ":" + cur.Minute + " " + cur.Station);
                        MarkerOptions markerOptions = new MarkerOptions();
                        String Train = "";
                        if (cur.Table_No == intService0) Train = " モノちゃん号";
                        if (cur.Table_No == intService1) Train = " アーバンフライ0型 001-002号";
                        if (cur.Table_No == intService2) Train = " アーバンフライ0型 003-004号";
                        if (cur.Table_No == intService3) Train = " アーバンフライ0型 005-006号";
                        if (cur.Table_No == intService4) Train = " アーバンフライ0型 007-008号";
                        if (cur.UpDown == 0) {
                            markerOptions.title("上り " + cur.Station + " " + cur.Hour + ":" + cur.Minute + " " + cur.Table_No + Train);
                            if (cur.Table_No == intService0){markerOptions.anchor(0.5f,0.7f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.monochan_up, SampleRate))));}
                            else if (cur.Table_No == intService1){markerOptions.anchor(0f,0.5f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.urbanflyer_up, SampleRate))));}
                            else if (cur.Table_No == intService2){markerOptions.anchor(0f,0.5f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.urbanflyer_up, SampleRate))));}
                            else if (cur.Table_No == intService3){markerOptions.anchor(0f,0.5f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.urbanflyer_up, SampleRate))));}
                            else if (cur.Table_No == intService4){markerOptions.anchor(0f,0.5f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.urbanflyer_up, SampleRate))));}
                            else {markerOptions.anchor(0f,0.5f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.mono_up, SampleRate))));}

                    } else {
                            markerOptions.title("下り " + cur.Station + " " + cur.Hour + ":" + cur.Minute + " " + cur.Table_No + Train);
                            if (cur.Table_No == intService0){markerOptions.anchor(0.5f,0.7f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.monochan_down, SampleRate))));}
                            else if (cur.Table_No == intService1){markerOptions.anchor(1f,0.5f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.urbanflyer_down, SampleRate))));}
                            else if (cur.Table_No == intService2){markerOptions.anchor(1f,0.5f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.urbanflyer_down, SampleRate))));}
                            else if (cur.Table_No == intService3){markerOptions.anchor(1f,0.5f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.urbanflyer_down, SampleRate))));}
                            else if (cur.Table_No == intService4){markerOptions.anchor(1f,0.5f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.urbanflyer_down, SampleRate))));}
                            else {markerOptions.anchor(1f,0.5f).icon(
                                    BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.mono_down, SampleRate))));}
                        }
                        if (getPositon(cur) != null) {
                            markerOptions.position(getPositon(cur));
                            trainMakers.put(cur, mMap.addMarker(markerOptions));
                            if(DEBUG)Log.d(TAG,"trainMakers.put " + cur.Station);
                        }
                    }
                }
            }
            //マーカーの再描写が終わったのでフラグを初期化
            markerRewrite=false;
        }
    }

    /**
     * NaowTrainData情報によって駅座標系を取得する
     */
    private LatLng getPositon(NowTrainData Train){
        if(Train.after!=null){
            if(Train.Station.equals(Train.after.Station)){
                if (Train.Station.equals("千城台駅")) return Station.STATION_TISHIRODAI;
                if (Train.Station.equals("千城台北駅"))return Station.STATION_TISHIRODAIKITA;
                if (Train.Station.equals("小倉台駅")) return Station.STATION_OGURADAI;
                if (Train.Station.equals("桜木駅")) return Station.STATION_SAKURAGI;
                if (Train.Station.equals("都賀駅")) return Station.STATION_TUGA;
                if (Train.Station.equals("みつわ台駅")) return Station.STATION_MITUWADAI;
                if (Train.Station.equals("動物公園駅"))return Station.STATION_DOUBUTUKOUEN;
                if (Train.Station.equals("スポーツセンター駅"))return Station.STATION_SPORTSCENTER;
                if (Train.Station.equals("穴川駅")) return Station.STATION_ANAGAWA;
                if (Train.Station.equals("天台駅")) return Station.STATION_TENDAI;
                if (Train.Station.equals("作草部駅")) return Station.STATION_SAKUSABE;
                if (Train.Station.equals("千葉公園駅")) return Station.STATION_CHIBAKOUEN;
                if (Train.Station.equals("県庁前駅")) return Station.STATION_KENTYOMAE;
                if (Train.Station.equals("葭川公園駅"))return Station.STATION_YOSHIKAWAKOUEN;
                if (Train.Station.equals("栄町駅")) return Station.STATION_SAKAETYOU;
                if (Train.Station.equals("千葉駅")) return Station.STATION_CHIBA;
                if (Train.Station.equals("市役所前駅"))return Station.STATION_SHIYAKUSYOMAE;
                if (Train.Station.equals("千葉みなと駅"))return Station.STATION_CHIBAMINATO;
            }
            if(Train.Station.equals("千城台駅") && Train.after.Station.equals("千城台北駅")){
                return getNowLatLng(Station.LINE_TISHIRODAI_TISHIRODAIKITA,Train);
            }
            if(Train.Station.equals("千城台北駅") && Train.after.Station.equals("小倉台駅")){
                return getNowLatLng(Station.LINE_TISHIRODAIKITA_OGURADAI,Train);
            }
            if(Train.Station.equals("小倉台駅") &&  Train.after.Station.equals("桜木駅")){
                return getNowLatLng(Station.LINE_OGURADAI_SAKURAGI,Train);
            }
            if(Train.Station.equals("桜木駅") &&  Train.after.Station.equals("都賀駅")){
                return getNowLatLng(Station.LINE_SAKURAGI_TUGA,Train);
            }
            if(Train.Station.equals("都賀駅") && Train.after.Station.equals("みつわ台駅")){
                return getNowLatLng(Station.LINE_TUGA_MITUWADAI,Train);
            }
            if(Train.Station.equals("みつわ台駅") && Train.after.Station.equals("動物公園駅")){
                return getNowLatLng(Station.LINE_MITUWADAI_DOUBUTUKOUEN,Train);
            }
            if(Train.Station.equals("動物公園駅") && Train.after.Station.equals("スポーツセンター駅")){
                return getNowLatLng(Station.LINE_DOUBUTUKOUEN_SPORTSCENTER,Train);
            }
            if(Train.Station.equals("スポーツセンター駅") && Train.after.Station.equals("穴川駅")){
                return getNowLatLng(Station.LINE_SPORTSCENTER_ANAGAWA,Train);
            }
            if(Train.Station.equals("穴川駅") && Train.after.Station.equals("天台駅")){
                return getNowLatLng(Station.LINE_ANAGAWA_TENDAI,Train);
            }
            if(Train.Station.equals("天台駅") && Train.after.Station.equals("作草部駅")){
                return getNowLatLng(Station.LINE_TENDAI_SAKUSABE,Train);
            }
            if(Train.Station.equals("作草部駅") && Train.after.Station.equals("千葉公園駅")){
                return getNowLatLng(Station.LINE_SAKUSABE_CHIBAKOUEN,Train);
            }
            if(Train.Station.equals("千葉公園駅") && Train.after.Station.equals("千葉駅")){
                return getNowLatLng(Station.LINE_CHIBAKOUEN_CHIBA,Train);
            }
            if(Train.Station.equals("県庁前駅") && Train.after.Station.equals("葭川公園駅")){
                return getNowLatLng(Station.LINE_KENTYOUMAE_YOSHIKAWAKOUEN,Train);
            }
            if(Train.Station.equals("葭川公園駅") &&  Train.after.Station.equals("栄町駅")){
                return getNowLatLng(Station.LINE_YOSHIKAWAKOUEN_SAKAETYOU,Train);
            }
            if(Train.Station.equals("栄町駅") &&  Train.after.Station.equals("千葉駅")){
                return getNowLatLng(Station.LINE_SAKAETYOU_CHIBA,Train);
            }
            if(Train.Station.equals("千葉駅") &&  Train.after.Station.equals("市役所前駅")){
                return getNowLatLng(Station.LINE_CHIBA_SHIYAKUSYO,Train);
            }
            if(Train.Station.equals("市役所前駅") && Train.after.Station.equals("千葉みなと駅")){
                return getNowLatLng(Station.LINE_SHIYAKUSYO_CHIBAMINATO,Train);
            }

            if(Train.Station.equals("千城台北駅") && Train.after.Station.equals("千城台駅")){
                return getNowLatLng(Station.LINE_TISHIRODAI_TISHIRODAIKITA,Train);
            }
            if(Train.Station.equals("小倉台駅") && Train.after.Station.equals("千城台北駅")){
                return getNowLatLng(Station.LINE_TISHIRODAIKITA_OGURADAI,Train);
            }
            if(Train.Station.equals("桜木駅") && Train.after.Station.equals("小倉台駅")){
                return getNowLatLng(Station.LINE_OGURADAI_SAKURAGI,Train);
            }
            if(Train.Station.equals("都賀駅") && Train.after.Station.equals("桜木駅")){
                return getNowLatLng(Station.LINE_SAKURAGI_TUGA,Train);
            }
            if(Train.Station.equals("みつわ台駅") &&  Train.after.Station.equals("都賀駅")){
                return getNowLatLng(Station.LINE_TUGA_MITUWADAI,Train);
            }
            if(Train.Station.equals("動物公園駅") && Train.after.Station.equals("みつわ台駅")){
                return getNowLatLng(Station.LINE_MITUWADAI_DOUBUTUKOUEN,Train);
            }
            if(Train.Station.equals("スポーツセンター駅") &&  Train.after.Station.equals("動物公園駅")){
                return getNowLatLng(Station.LINE_DOUBUTUKOUEN_SPORTSCENTER,Train);
            }
            if(Train.Station.equals("穴川駅") &&  Train.after.Station.equals("スポーツセンター駅")){
                return getNowLatLng(Station.LINE_SPORTSCENTER_ANAGAWA,Train);
            }
            if(Train.Station.equals("天台駅") &&  Train.after.Station.equals("穴川駅")){
                return getNowLatLng(Station.LINE_ANAGAWA_TENDAI,Train);
            }
            if(Train.Station.equals("作草部駅") && Train.after.Station.equals("天台駅")){
                return getNowLatLng(Station.LINE_TENDAI_SAKUSABE,Train);
            }
            if(Train.Station.equals("千葉公園駅") && Train.after.Station.equals("作草部駅")){
                return getNowLatLng(Station.LINE_SAKUSABE_CHIBAKOUEN,Train);
            }
            if(Train.Station.equals("千葉駅") &&  Train.after.Station.equals("千葉公園駅")){
                return getNowLatLng(Station.LINE_CHIBAKOUEN_CHIBA,Train);
            }
            if(Train.Station.equals("葭川公園駅") && Train.after.Station.equals("県庁前駅")){
                return getNowLatLng(Station.LINE_KENTYOUMAE_YOSHIKAWAKOUEN,Train);
            }
            if(Train.Station.equals("栄町駅") && Train.after.Station.equals("葭川公園駅")){
                return getNowLatLng(Station.LINE_YOSHIKAWAKOUEN_SAKAETYOU,Train);
            }
            if(Train.Station.equals("千葉駅") && Train.after.Station.equals("栄町駅")){
                return getNowLatLng(Station.LINE_SAKAETYOU_CHIBA,Train);
            }
            if(Train.Station.equals("市役所前駅") &&  Train.after.Station.equals("千葉駅")){
                return getNowLatLng(Station.LINE_CHIBA_SHIYAKUSYO,Train);
            }
            if(Train.Station.equals("千葉みなと駅") && Train.after.Station.equals("市役所前駅")){
                return getNowLatLng(Station.LINE_SHIYAKUSYO_CHIBAMINATO,Train);
            }

        }
        if(DEBUG)Log.d(TAG,"getPositon : " + Train.Station);
        return null;
    }

    /**
     * 現時点での列車位置を取得する
     * @param line KMLラインデータ
     * @param train 列車データ
     * @return 列車マーカー用緯度経度
     */
    private LatLng getNowLatLng(String line, NowTrainData train) {
        /** テキスト処理の作業変数 セット単位*/
        String[] cells;
        /** テキスト処理の作業変数 個別要素*/
        String[] word;
        /** rKMLラインデータの保存 */
        List <LatLng> array = new ArrayList<LatLng>();
        cells = line.split(" ");
        for(String cell:cells) {
            word = cell.split(",");
            array.add(new LatLng(Double.valueOf(word[1]), Double.valueOf(word[0])));
        }
        /** 発車駅の時刻を管理 */
        Date fromDate = new Date();
        /** 次の発車駅の時刻を管理 */
        Date toDate = new Date();

        //ミリタイムを取得
        //noinspection deprecation
        fromDate.setHours(train.Hour);
        //noinspection deprecation
        fromDate.setMinutes(train.Minute);
        //noinspection deprecation
        fromDate.setSeconds(0);
        //noinspection deprecation
        toDate.setHours(train.after.Hour);
        //noinspection deprecation
        toDate.setMinutes(train.after.Minute);
        //noinspection deprecation
        toDate.setSeconds(0);

        Long fromLong = fromDate.getTime() + 30 * 1000; //乗降の為に30s遅く出発すると仮定
        Long toLong = toDate.getTime() + 10 * 1000; //乗降のために10s遅くに付くと仮定

        //現時点でKML線路長のどの割合かを計算
        int index = (int) ((((double)System.currentTimeMillis() - (double)fromLong) / ((double)toLong - (double)fromLong)) * (double)(array.size()-1));
        //計算結果でindexが0よりも小さいのは不正なので初期化
        index = (index<0)?0:index;
        //到着時刻を早めた分インデックスを超えることがないようにリミット
        index = (index > array.size()-1)?array.size()-1:index;

        //上り下りで配列の順序を切替
        if(train.UpDown==Station.DOWN) {
            return array.get(index);
        }else{
            return array.get(array.size()-1 - index);
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /** 今日の休日平日フラグのセット */
    static void setHoliday(int holiday){intHoliday=holiday;}
    /** モノちゃん号の運行情報のセット */
    static void setService0(int service){intService0=service;}
    /** アーバンフライ０ 1-2号の運行情報のセット */
    static void setService1(int service){intService1=service;}
    /** アーバンフライ０ 3-4号の運行情報のセット */
    static void setService2(int service){intService2=service;}
    /** アーバンフライ０ 5-6号の運行情報のセット */
    static void setService3(int service){intService3=service;}
    /** アーバンフライ０ 7-8号の運行情報のセット */
    static void setService4(int service){intService4=service;}

    /**
     * 列車のインフォWindowをクリック時の動作をActivityへ伝える
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(int TrainNo,int intHoliday);
    }

    /**
     * カメラが移動したらアイコンサイズ変更
     * Zoomのみ評価。
     * @param cameraPosition カメラポジション
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        float zoom = cameraPosition.zoom;
        if (DEBUG) Log.i(TAG, "zoom:" + zoom);
        if (DEBUG) Log.i(TAG, "SampleRate:" + SampleRate);

        if(oldZoom!=zoom){
            oldZoom = zoom;
            markerRewrite = true;
            if      (15.5f <= zoom) {
                SampleRate =1;
            }else if(13f   <= zoom && zoom < 15.5f) {
                SampleRate = 2;
            }else if(11f   <= zoom && zoom < 13f) {
                SampleRate = 4;
            }else {
                SampleRate = 8;
            }
            for (Marker marker : stationMarker) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(readBitmap(context, R.drawable.station, SampleRate))));
            }
        }
    }

    /**
     * リソースファイルからBitmapを生成
     * リソースの縮小機能付き
     * @param context アプリのコンテキスト
     * @param resID リソースID
     * @param SampleSize 読込サンプリングレート(2の倍数)
     * @return サンプリング(縮小)済みBitmap画像
     */
    private static Bitmap readBitmap(Context context,int resID,int SampleSize){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize =  SampleSize;
        return BitmapFactory.decodeResource(context.getResources(),resID,options);
    }

    /**
     * マップ画面に定期的1s毎に画面更新をするためのハンドラ
     */
    private class TrainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (trainHandler != null){
                getLoaderManager().restartLoader(intHoliday, null, callbacks);
                trainHandler.sleep(1000); //1s
            }
        }

        //スリープメソッド
        public void sleep(long delayMills) {
            //使用済みメッセージの削除
            removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMills);
        }
    }

}
