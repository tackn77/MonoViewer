package jp.chiba.tackn.monoviewer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * TrainNoのリスト表示時にViewを提供する
 * @author Takumi Ito
 * @since 2014/05/12.
 */
public class TrainTblCursorAdapter extends SimpleCursorAdapter {

    /** デバッグフラグ */
    private static final boolean DEBUG = false;
    private static final String TAG = "TrainTblCursorAdapter";


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

        View v = super.newView(context,cursor,parent);

        ViewHolder holder = new ViewHolder();
        holder.icon = (ImageView) v.findViewById(R.id.train_no_iconeView);
        holder.hour = (TextView) v.findViewById(R.id.train_no_hour);
        holder.minute = (TextView) v.findViewById(R.id.train_no_minute);
        holder.station = (TextView) v.findViewById(R.id.train_no_station);

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
    public void bindView(View view, final Context context,final Cursor cursor) {
        super.bindView(view,context,cursor);

        final ViewHolder holder = (ViewHolder)view.getTag();
        final int updown = cursor.getInt(cursor.getColumnIndex(TrainTblContract.UPDOWN));
        if(updown==0){
            holder.icon.setImageResource(R.drawable.ic_up);
        }else{
            holder.icon.setImageResource(R.drawable.ic_down);
        }

        int hour = cursor.getInt(cursor.getColumnIndex(TrainTblContract.HOUR));
        holder.hour.setText(String.format("%1$02d",hour));
        int minute = cursor.getInt(cursor.getColumnIndex(TrainTblContract.MINUTE));
        holder.minute.setText(String.format("%1$02d",minute));
        final String station = cursor.getString(cursor.getColumnIndex(TrainTblContract.STATION));
        holder.station.setText(station);
        final int holiday = cursor.getInt(cursor.getColumnIndex(TrainTblContract.HOLIDAY));

        final String destination = cursor.getString(cursor.getColumnIndex(TrainTblContract.DESTINATION));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DEBUG){
                    Log.d(TAG,"ボタンが押されたよ");
                    Log.d(TAG,"station: " + station );
                    Log.d(TAG,"updown : " + updown);
                    Log.d(TAG,"holiday: " + holiday);
                }


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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            }
        });

    }
}
