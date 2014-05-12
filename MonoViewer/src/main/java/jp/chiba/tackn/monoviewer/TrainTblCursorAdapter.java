package jp.chiba.tackn.monoviewer;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
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
     * @param c 表示するカーソル //TODO:よく分からん
     * @param from 表示に使うカラム
     * @param to バインド先のレイアウトのID
     * @param flags フラグ //TODO:よく分からん
     */
    public TrainTblCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    /**
     * 独自にレイアウトファイル読み込み
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v= inflater.inflate(R.layout.list_item_train_no,null,true);

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
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        int updown = cursor.getInt(cursor.getColumnIndex(TrainTblContract.UPDOWN));
        if(updown==0){
            holder.icon.setImageResource(R.drawable.ic_up);
        }else{
            holder.icon.setImageResource(R.drawable.ic_down);
        }

        int hour = cursor.getInt(cursor.getColumnIndex(TrainTblContract.HOUR));
        holder.hour.setText(String.format("%1$02d",hour));
        int minute = cursor.getInt(cursor.getColumnIndex(TrainTblContract.MINUTE));
        holder.minute.setText(String.format("%1$02d",minute));
        String station = cursor.getString(cursor.getColumnIndex(TrainTblContract.STATION));
        holder.station.setText(station);

    }
}
