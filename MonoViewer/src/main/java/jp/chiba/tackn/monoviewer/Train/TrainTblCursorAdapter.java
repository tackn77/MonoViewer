package jp.chiba.tackn.monoviewer.train;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;

import jp.chiba.tackn.monoviewer.R;
import jp.chiba.tackn.monoviewer.TabletHolder;
import jp.chiba.tackn.monoviewer.data.SQLTblContract;
import jp.chiba.tackn.monoviewer.InformationHolder;
import jp.chiba.tackn.monoviewer.map.Station;
import jp.chiba.tackn.monoviewer.time.TimeTable;

/**
 * TrainNoのリスト表示時にViewを提供する
 * @author Takumi Ito
 * @since 2014/05/12.
 */
public class TrainTblCursorAdapter extends SimpleCursorAdapter {

    /** デバッグフラグ */
    private static final boolean DEBUG = false;
    /** デバッグタグ */
    private static final String TAG = "TrainTblCursorAdapter";
    /** 運行情報管理クラス */
    private InformationHolder informationHolder = InformationHolder.getInstance();
    /** タブレットモードの保持 */
    private TabletHolder tabletHolder = TabletHolder.getInstance();

    /**
     * ListViewのカラムの要素を保存するクラス
     */
    private class ViewHolder{
        public ImageView icon;
        public TextView hour;
        public TextView minute;
        public TextView station;
    }

    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param layout 表示するレイアウトファイルのID
     * @param c 表示するカーソル
     * @param from 表示に使うカラム
     * @param to バインド先のレイアウトのID
     * @param flags CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER フラグ
     */
    public TrainTblCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    /**
     * 独自にレイアウトファイル読み込み
     * @param context アプリケーションコンテキスト
     * @param cursor 紐づけるカーソル
     * @param parent 上位のリスト
     * @return 生成したView
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        //カラムのView取得
        View v = super.newView(context,cursor,parent);

        //bindView で紐付する要素取得
        ViewHolder holder = new ViewHolder();
        holder.icon = (ImageView) v.findViewById(R.id.train_no_iconeView);
        holder.hour = (TextView) v.findViewById(R.id.train_no_hour);
        holder.minute = (TextView) v.findViewById(R.id.train_no_minute);
        holder.station = (TextView) v.findViewById(R.id.train_no_station);

        //bindView へ送る
        v.setTag(holder);

        return v;
    }

    /**
     * 独自にビューへのデータを紐付ける
     * @param view 紐づけ先の対象のView
     * @param context アプリケーションコンテキスト
     * @param cursor 紐づけ元のデータ
     */
    @Override
    public void bindView(@SuppressWarnings("NullableProblems") View view, final Context context, @SuppressWarnings("NullableProblems") final Cursor cursor) {
        super.bindView(view,context,cursor);
        /** newView から受取 */
        final ViewHolder holder = (ViewHolder)view.getTag();

        /** 表示するテーブルNo */
        final int tableNo = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_TABLE_NO));

        /** 上り・下り */
        final int updown = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_UPDOWN));

        int Service0 = (informationHolder.getService0().matches("^[0-9]+$"))?Integer.valueOf(informationHolder.getService0()):0;
        int Service1 = (informationHolder.getService1().matches("^[0-9]+$"))?Integer.valueOf(informationHolder.getService1()):0;
        int Service2 = (informationHolder.getService2().matches("^[0-9]+$"))?Integer.valueOf(informationHolder.getService2()):0;
        int Service3 = (informationHolder.getService3().matches("^[0-9]+$"))?Integer.valueOf(informationHolder.getService3()):0;
        int Service4 = (informationHolder.getService4().matches("^[0-9]+$"))?Integer.valueOf(informationHolder.getService4()):0;

        if(updown==0){
            if(Service0==tableNo){holder.icon.setImageResource(R.drawable.monochan_up);}
            else if(Service1==tableNo){holder.icon.setImageResource(R.drawable.urbanflyer_up);}
            else if(Service2==tableNo){holder.icon.setImageResource(R.drawable.urbanflyer_up);}
            else if(Service3==tableNo){holder.icon.setImageResource(R.drawable.urbanflyer_up);}
            else if(Service4==tableNo){holder.icon.setImageResource(R.drawable.urbanflyer_up);}
            else{holder.icon.setImageResource(R.drawable.mono_up);}
        }else{
            if(Service0==tableNo){holder.icon.setImageResource(R.drawable.monochan_down);}
            else if(Service1==tableNo){holder.icon.setImageResource(R.drawable.urbanflyer_down);}
            else if(Service2==tableNo){holder.icon.setImageResource(R.drawable.urbanflyer_down);}
            else if(Service3==tableNo){holder.icon.setImageResource(R.drawable.urbanflyer_down);}
            else if(Service4==tableNo){holder.icon.setImageResource(R.drawable.urbanflyer_down);}
            else{holder.icon.setImageResource(R.drawable.mono_down);}
        }

        //時間（時）
        int hour = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_HOUR));
        holder.hour.setText(String.format("%1$02d",hour));
        //時間（分)
        int minute = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_MINUTE));
        holder.minute.setText(String.format("%1$02d",minute));
        //駅名
        final String station = cursor.getString(cursor.getColumnIndex(SQLTblContract.COLUMN_STATION));
        holder.station.setText(station);

        //平日・休日
        final int holiday = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_HOLIDAY));
        //行き先
        final String destination = cursor.getString(cursor.getColumnIndex(SQLTblContract.COLUMN_DESTINATION));

        //カラムがクリックされたら駅の時刻表を起動
        //noinspection ConstantConditions
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DEBUG){
                    Log.d(TAG,"ボタンが押されたよ");
                    Log.d(TAG,"station: " + station );
                    Log.d(TAG,"updown : " + updown);
                    Log.d(TAG,"holiday: " + holiday);
                }

                if(tabletHolder.isTablet()) {
                    if(station.equals("千城台駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_TISHIRODAI));
                    if(station.equals("千城台北駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_TISHIRODAIKITA));
                    if(station.equals("小倉台駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_OGURADAI));
                    if(station.equals("桜木駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_SAKURAGI));
                    if(station.equals("都賀駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_TUGA));
                    if(station.equals("みつわ台駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_MITUWADAI));
                    if(station.equals("動物公園駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_DOUBUTUKOUEN));
                    if(station.equals("スポーツセンター駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_SPORTSCENTER));
                    if(station.equals("穴川駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_ANAGAWA));
                    if(station.equals("天台駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_TENDAI));
                    if(station.equals("作草部駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_SAKUSABE));
                    if(station.equals("千葉公園駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_CHIBAKOUEN));
                    if(station.equals("県庁前駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_KENTYOMAE));
                    if(station.equals("葭川公園駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_YOSHIKAWAKOUEN));
                    if(station.equals("栄町駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_SAKAETYOU));
                    if(station.equals("千葉駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_CHIBA));
                    if(station.equals("市役所前駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_SHIYAKUSYOMAE));
                    if(station.equals("千葉みなと駅"))tabletHolder.getMap().animateCamera(CameraUpdateFactory.newLatLng(Station.STATION_CHIBAMINATO));
                }else{
                    Intent intent = new Intent(context,TimeTable.class);

                    if(station.equals("千葉駅")&&destination.indexOf("県庁前")>0){
                        intent.putExtra("station", "千葉駅1号線");
                    }else if(station.equals("千葉駅")&&destination.indexOf("千城台")>0){
                        intent.putExtra("station", "千葉駅2号線");
                    }else {
                        intent.putExtra("station", station);
                    }

                    intent.putExtra("updown",updown);
                    intent.putExtra("holiday",holiday);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });

    }
}
