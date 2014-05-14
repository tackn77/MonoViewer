package jp.chiba.tackn.monoviewer.table;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import jp.chiba.tackn.monoviewer.R;
import jp.chiba.tackn.monoviewer.data.SQLTblContract;

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
    public void bindView(View view, final Context context,final Cursor cursor) {
        super.bindView(view,context,cursor);

        //newView から受取
        final ViewHolder holder = (ViewHolder)view.getTag();

        //上り・下り
        final int updown = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_UPDOWN));
        if(updown==0){
            holder.icon.setImageResource(R.drawable.ic_up);
        }else{
            holder.icon.setImageResource(R.drawable.ic_down);
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
