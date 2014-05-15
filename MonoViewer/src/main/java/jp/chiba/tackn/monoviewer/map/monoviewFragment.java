package jp.chiba.tackn.monoviewer.map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jp.chiba.tackn.monoviewer.R;
import jp.chiba.tackn.monoviewer.data.SQLTblContract;
import jp.chiba.tackn.monoviewer.table.TimeTable;
import jp.chiba.tackn.monoviewer.table.TrainTable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link monoviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link monoviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class monoviewFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener
        , LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "monoviewFragment";
    private static final boolean DEBUG = true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MapFragment mapFragment;
    private Context context;
    private TrainHandler trainHandler;
    private LoaderManager.LoaderCallbacks callbacks;
    private int intHoliday;
    private int updown;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment monoview.
     */
    // TODO: Rename and change types and number of parameters
    public static monoviewFragment newInstance(String param1, String param2) {
        monoviewFragment fragment = new monoviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public monoviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context = getActivity().getApplicationContext();
        //SyncTaskでherokuから情報取得
        Uri.Builder builder = new Uri.Builder();
        AsyncHttpRequest task = new AsyncHttpRequest(context);
        task.execute(builder);

        callbacks = (LoaderManager.LoaderCallbacks) this;
        setUpMapIfNeeded();
        setUpHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monoview, container, false);
        if (DEBUG) Log.d(TAG, "onCreateView " + view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpHandler();
    }

    private void setUpHandler() {
        if (trainHandler == null) {
            trainHandler = new TrainHandler();
            trainHandler.sleep(0);
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
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

            mMap = mapFragment.getMap();

            if (DEBUG) Log.d(TAG, "setUpMapIfNeeded()$mapFragment " + mapFragment);
            if (DEBUG) Log.d(TAG, "setUpMapIfNeeded()$mMap " + mMap);

            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // 千葉駅を表示
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Station.STATION_CHIBA));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
        mMap.setOnInfoWindowClickListener(this);

        // 現在位置表示の有効化
        mMap.setMyLocationEnabled(true);

        //駅に時刻表のリンクしたマーカーを設置
        mMap.addMarker(new MarkerOptions().position(Station.STATION_TISHIRODAI).title("千城台駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_TISHIRODAIKITA).title("千城台北駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_OGURADAI).title("小倉台駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_SAKURAGI).title("桜木駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_TUGA).title("都賀駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_MITUWADAI).title("みつわ台駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_DOUBUTUKOUEN).title("動物公園駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_SPORTSCENTER).title("スポーツセンター駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_ANAGAWA).title("穴川駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_TENDAI).title("天台駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_SAKUSABE).title("作草部駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_CHIBAKOUEN).title("千葉公園駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_KENTYOMAE).title("県庁前駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_YOSHIKAWAKOUEN).title("葭川公園駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_SAKAETYOU).title("栄町駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_CHIBA).title("千葉駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_SHIYAKUSYOMAE).title("市役所前駅").alpha(0.3f));
        mMap.addMarker(new MarkerOptions().position(Station.STATION_CHIBAMINATO).title("千葉みなと駅").alpha(0.3f));

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    Pattern patternStation = Pattern.compile("^[^駅]+駅$");
    Pattern patternTrain = Pattern.compile("^(上り|下り) [^ ]+ ([0-9]+):([0-9]+) ([0-9]+)$");

    @Override
    public void onInfoWindowClick(Marker marker) {
        Matcher matcherStation = patternStation.matcher(marker.getTitle());
        Matcher matcherTrain = patternTrain.matcher(marker.getTitle());

        if(matcherStation.find()) {
            Intent intent = new Intent(context, TimeTable.class);
            if (marker.getTitle().equals("千葉みなと駅")) {
                intent.putExtra("updown", 1);
            } else {
                intent.putExtra("updown", 0);
            }
            intent.putExtra("station", marker.getTitle());
            intent.putExtra("holiday", intHoliday);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else if(matcherTrain.find()){
            int tableNo = Integer.parseInt(matcherTrain.group(4));

            Intent intent = new Intent(context, TrainTable.class);
            intent.putExtra("TableNo", tableNo);
            intent.putExtra("holiday", intHoliday);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        /** 取得データのソート */
        String orderBy = " TABLE_NO ASC,TIMES ASC";

        switch (i) {
            case 0:
                return new CursorLoader(context, SQLTblContract.CONTENT_NOW_TRAIN_SERVICE_INFOMATION_HOLIDAY, null, null, null, orderBy);
            case 1:
                return new CursorLoader(context, SQLTblContract.CONTENT_NOW_TRAIN_SERVICE_INFOMATION_WEEKEND, null, null, null, orderBy);
            default:
                if (DEBUG) {
                    Log.d(TAG, "id:" + i);
                }
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H1, null, null, null, orderBy);
        }
    }

    private class NowTrainData {
        public String Station;
        public int UpDown;
        public int Table_No;
        public int Hour;
        public int Minute;

        public NowTrainData(String Station, int UpDown, int Hour, int Minute, int Table_No) {
            this.Station = Station;
            this.UpDown = UpDown;
            this.Table_No = Table_No;
            this.Hour = Hour;
            this.Minute = Minute;
        }
    }

    private List<Marker> trainMakers = new ArrayList<Marker>();

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int table_no = 0;
        int new_table_no = 0;
        List<NowTrainData> group = new ArrayList<NowTrainData>();
        NowTrainData old = null;

        for (Marker marker : trainMakers) marker.remove();
        trainMakers.clear();

        if(DEBUG)Log.d(TAG,"cursor.getCount() " + cursor.getCount());
        if (cursor.moveToFirst()) {
            table_no = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_TABLE_NO));
            do {
                new_table_no = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_TABLE_NO));
                if (table_no != new_table_no) {
                    group.add(old);
                    table_no = new_table_no;
                }
                old = new NowTrainData(
                        cursor.getString(cursor.getColumnIndex(SQLTblContract.COLUMN_STATION))
                        , cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_UPDOWN))
                        , cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_HOUR))
                        , cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_MINUTE))
                        , table_no
                );
                if(DEBUG)Log.d(TAG,old.UpDown + " " + old.Table_No + "/" + old.Hour + ":" + old.Minute + " " + old.Station);
            } while (cursor.moveToNext());
            group.add(old);
        }

        if(DEBUG)Log.d(TAG,"group.size() " + group.size());
        if (group.size() > 0) {
            for (NowTrainData cur : group) {
                if (mMap != null) {
                    if(DEBUG)Log.d(TAG,cur.UpDown + " " + cur.Table_No +  "/" + cur.Hour + ":" + cur.Minute + " " + cur.Station);
                    String station = cur.Station;
                    int updonw = cur.UpDown;
                    MarkerOptions markerOptions = new MarkerOptions();
                    if (updonw == 0) {
                        markerOptions.title("上り " + station + " " + cur.Hour + ":" + cur.Minute + " " + cur.Table_No);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_up));
                    } else {
                        markerOptions.title("下り " + station + " " + cur.Hour + ":" + cur.Minute + " " + cur.Table_No);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_down));
                    }

                    if (station.equals("千城台駅")) markerOptions.position(Station.STATION_TISHIRODAI);
                    if (station.equals("千城台北駅"))markerOptions.position(Station.STATION_TISHIRODAIKITA);
                    if (station.equals("小倉台駅")) markerOptions.position(Station.STATION_OGURADAI);
                    if (station.equals("桜木駅")) markerOptions.position(Station.STATION_SAKURAGI);
                    if (station.equals("都賀駅")) markerOptions.position(Station.STATION_TUGA);
                    if (station.equals("みつわ台駅")) markerOptions.position(Station.STATION_MITUWADAI);
                    if (station.equals("動物公園駅"))markerOptions.position(Station.STATION_DOUBUTUKOUEN);
                    if (station.equals("スポーツセンター駅"))markerOptions.position(Station.STATION_SPORTSCENTER);
                    if (station.equals("穴川駅")) markerOptions.position(Station.STATION_ANAGAWA);
                    if (station.equals("天台駅")) markerOptions.position(Station.STATION_TENDAI);
                    if (station.equals("作草部駅")) markerOptions.position(Station.STATION_SAKUSABE);
                    if (station.equals("千葉公園駅")) markerOptions.position(Station.STATION_CHIBAKOUEN);
                    if (station.equals("県庁前駅")) markerOptions.position(Station.STATION_KENTYOMAE);
                    if (station.equals("葭川公園駅"))markerOptions.position(Station.STATION_YOSHIKAWAKOUEN);
                    if (station.equals("栄町駅")) markerOptions.position(Station.STATION_SAKAETYOU);
                    if (station.equals("千葉駅")) markerOptions.position(Station.STATION_CHIBA);
                    if (station.equals("市役所前駅"))markerOptions.position(Station.STATION_SHIYAKUSYOMAE);
                    if (station.equals("千葉みなと駅"))markerOptions.position(Station.STATION_CHIBAMINATO);

                    trainMakers.add(mMap.addMarker(markerOptions));
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class AsyncHttpRequest extends AsyncTask<Uri.Builder, Void, String> {

        /**
         * デバッグ用タグ
         */
        private static final String TAG = "AsyncHttpRequest";
        /**
         * デバッグ用フラグ
         */
        private static final boolean DEBUG = false;

        /**
         * 呼び出し元コンテキスト
         */
        private Context context;
        /**
         * 呼び出し元Activity
         */
        private Activity mainActivity;

        /**
         * XML 扱うためのファクトリ
         */
        private DocumentBuilderFactory dbFactory;
        /**
         * XML 扱うためのビルダー
         */
        private DocumentBuilder xmlbuilder;

        /**
         * 休日・平日フラグ
         */
        private String holiday;

        public AsyncHttpRequest(Context context) {

        }

        @Override
        protected String doInBackground(Uri.Builder... builders) {
            try {
                dbFactory = DocumentBuilderFactory.newInstance();
                xmlbuilder = dbFactory.newDocumentBuilder();
                Document document = xmlbuilder.parse("http://monoview.herokuapp.com/today.xml");

                XPathFactory factory = XPathFactory.newInstance();
                XPath xpath = factory.newXPath();

                holiday = xpath.evaluate("//holiday[1]/text()", document);

            } catch (SAXException e) {
                return "false";
            } catch (IOException e) {
                return "false";
            } catch (ParserConfigurationException e) {
                return "false";
            } catch (XPathExpressionException e) {
                return "false";
            }

            if (DEBUG) Log.d(TAG, "result: " + holiday);

            return "true";

        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("false")) intHoliday = 0;
            //休日・平日判定
            if (holiday.equals("true")) {
                intHoliday = 0;
            } else if (holiday.equals("false")) {
                intHoliday = 1;
            } else {
                //取得失敗
                intHoliday = 0;
            }
        }
    }

    private class TrainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            getLoaderManager().restartLoader(intHoliday, null, callbacks);
            if (trainHandler != null) trainHandler.sleep(10 * 1000);  //3.
        }

        //スリープメソッド
        public void sleep(long delayMills) {
            //使用済みメッセージの削除
            removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMills);  //4.
        }
    }


}
