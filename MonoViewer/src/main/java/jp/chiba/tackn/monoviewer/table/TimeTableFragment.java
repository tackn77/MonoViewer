package jp.chiba.tackn.monoviewer.table;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.chiba.tackn.monoviewer.R;
import jp.chiba.tackn.monoviewer.data.SQLTblContract;
import jp.chiba.tackn.monoviewer.map.InformationHolder;


/**
 * 駅時刻表を表示するためのFragment
 * @author Takumi Ito
 * @since 2014/05/19
 */
public class TimeTableFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    /** デバッグフラグ*/
    private static final boolean DEBUG = false;
    /** デバッグタグ */
    private static final String TAG = "TimeTable";

    private Context context;
    /** プロバイダからのデータとListの仲立ち */
    private ArrayAdapter mAdapter;

    /** 時刻表表示用リスト */
    private ListView itemListView;

    /** Listに登録する情報 */
    private List<ListItem> listItems = new ArrayList<ListItem>();

    public TimeTableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        itemListView = (ListView)view.findViewById(R.id.listview);
        //リストの紐付
        mAdapter = new TimeTableLIstArrayAdapter(context,listItems);

        //アダプタの登録
        itemListView.setAdapter(mAdapter);
        //スクロールバー表示
        itemListView.setFastScrollEnabled(true);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * getLoaderManager().initLoader()で呼び出される
     *
     * @param id 呼び出しID
     * @param args 呼び出し引数
     * @return プロバイダから取得したデータ
     */
    public Loader onCreateLoader(int id, Bundle args) {
        if (DEBUG) {
            Log.d(TAG, "id: " + id);
//            Log.d(TAG, "Bundle: " + (args==null));
        }
        /** 取得データのソート */
        String orderBy = "TIMES ASC";

        switch (id) {//スピナーで選択したデータ毎にプロバイダの呼び出しURIを変更
            case 0:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS1U, null, null, null, orderBy);
            case 1:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS2U, null, null, null, orderBy);
            case 2:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS3U, null, null, null, orderBy);
            case 3:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS4U, null, null, null, orderBy);
            case 4:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS5U, null, null, null, orderBy);
            case 5:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS6U, null, null, null, orderBy);
            case 6:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS7U, null, null, null, orderBy);
            case 7:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS8U, null, null, null, orderBy);
            case 8:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS9U, null, null, null, orderBy);
            case 9:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS10U, null, null, null, orderBy);
            case 10:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS11U, null, null, null, orderBy);
            case 11:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS12U, null, null, null, orderBy);
            case 12:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS13U, null, null, null, orderBy);
            case 13:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS14U, null, null, null, orderBy);
            case 14:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS15U, null, null, null, orderBy);
            case 15:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS16U, null, null, null, orderBy);
            case 16:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS17U, null, null, null, orderBy);
            case 17:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS2D, null, null, null, orderBy);
            case 18:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS3D, null, null, null, orderBy);
            case 19:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS4D, null, null, null, orderBy);
            case 20:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS5D, null, null, null, orderBy);
            case 21:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS6D, null, null, null, orderBy);
            case 22:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS7D, null, null, null, orderBy);
            case 23:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS8D, null, null, null, orderBy);
            case 24:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS9D, null, null, null, orderBy);
            case 25:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS10D, null, null, null, orderBy);
            case 26:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS11D, null, null, null, orderBy);
            case 27:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS12D, null, null, null, orderBy);
            case 28:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS14D, null, null, null, orderBy);
            case 29:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS15D, null, null, null, orderBy);
            case 30:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS16D1, null, null, null, orderBy);
            case 31:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS16D2, null, null, null, orderBy);
            case 32:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS17D, null, null, null, orderBy);
            case 33:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_HS18D, null, null, null, orderBy);
            case 34:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS1U, null, null, null, orderBy);
            case 35:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS2U, null, null, null, orderBy);
            case 36:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS3U, null, null, null, orderBy);
            case 37:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS4U, null, null, null, orderBy);
            case 38:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS5U, null, null, null, orderBy);
            case 39:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS6U, null, null, null, orderBy);
            case 40:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS7U, null, null, null, orderBy);
            case 41:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS8U, null, null, null, orderBy);
            case 42:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS9U, null, null, null, orderBy);
            case 43:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS10U, null, null, null, orderBy);
            case 44:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS11U, null, null, null, orderBy);
            case 45:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS12U, null, null, null, orderBy);
            case 46:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS13U, null, null, null, orderBy);
            case 47:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS14U, null, null, null, orderBy);
            case 48:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS15U, null, null, null, orderBy);
            case 49:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS16U, null, null, null, orderBy);
            case 50:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS17U, null, null, null, orderBy);
            case 51:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS2D, null, null, null, orderBy);
            case 52:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS3D, null, null, null, orderBy);
            case 53:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS4D, null, null, null, orderBy);
            case 54:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS5D, null, null, null, orderBy);
            case 55:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS6D, null, null, null, orderBy);
            case 56:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS7D, null, null, null, orderBy);
            case 57:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS8D, null, null, null, orderBy);
            case 58:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS9D, null, null, null, orderBy);
            case 59:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS10D, null, null, null, orderBy);
            case 60:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS11D, null, null, null, orderBy);
            case 61:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS12D, null, null, null, orderBy);
            case 62:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS14D, null, null, null, orderBy);
            case 63:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS15D, null, null, null, orderBy);
            case 64:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS16D1, null, null, null, orderBy);
            case 65:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS16D2, null, null, null, orderBy);
            case 66:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS17D, null, null, null, orderBy);
            case 67:
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_KS18D, null, null, null, orderBy);
            default:
                if (DEBUG) {
                    Log.d(TAG, "id:" + id);
                }
                return new CursorLoader(context, SQLTblContract.CONTENT_URI_H1, null, null, null, orderBy);
        }
    }

    /**
     * カーソルの読込が終わったら実行
     * CursorAdapterを使わずにArrayAdapterを使うため自分で実装
     * @param cursorLoader カーソルローダー
     * @param cursor 紐付するカーソル
     */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if(DEBUG)Log.d(TAG,"onLoadFinished");

        listItems.clear();
        //ListViewの上位にHorizontalScrollViewを入れることに
        //但し1行目の幅になってしまうので最大幅分に合わせるための1行目ダミーデータを入れる
        //いくつかパターン試したけれど現段階ではこの方式に落ち着く
        List<TimeTableItem> damySpace = new ArrayList<TimeTableItem>();
        for(int k=0;k<14;k++)damySpace.add(new TimeTableItem("　","　","　",2));
        listItems.add(new ListItem("　",damySpace));

        //5時から開始
        int hour=5;
        //時間単位でカーソルの結果をまとめる
        List<TimeTableItem> items = new ArrayList<TimeTableItem>();
        if (cursor.moveToFirst()) {
            do {
                //時間の取得
                int newHour = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_HOUR));
                if(hour!=newHour){//時刻が変わったら新しいカラムを作成
                    if(DEBUG)Log.d(TAG,"hour change : " + hour +"->" + newHour);
                    //hour時のViewの追加
                    listItems.add(new ListItem(String.format("%1$02d", hour),items));
                    //次の時間用
                    hour=newHour;
                    items = new ArrayList<TimeTableItem>();
                }

                //表示要素の取得
                int minutes = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_MINUTE));
                String annotation = cursor.getString(cursor.getColumnIndex(SQLTblContract.COLUMN_ANNOTATION));
                int table_no = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_TABLE_NO));
                int isHoliday = cursor.getInt(cursor.getColumnIndex(SQLTblContract.COLUMN_HOLIDAY));

                //蓄積
                items.add(new TimeTableItem(String.format("%1$02d",minutes),annotation,String.valueOf(table_no),isHoliday));

            } while (cursor.moveToNext());
            if(items.size()>0)listItems.add(new ListItem(String.format("%1$02d", hour),items));
        }
        //更新完了通知
        mAdapter.notifyDataSetChanged();

        Calendar calendar = Calendar.getInstance();
        int hourIndex = calendar.get(Calendar.HOUR_OF_DAY);
        hourIndex = (hourIndex==0)?24:hourIndex;
        hourIndex = (hourIndex<5)?1:hourIndex - 5 + 1; //5時からだから-5で1行目がいらないから+1
        //開始位置指定 1行目の空白行の次の行から表示
        itemListView.setSelectionFromTop(hourIndex,0);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /**
     * 発車時刻を保持するクラス
     */
    private class TimeTableItem {
        public String minute;
        public String annotation;
        public String tableno;
        public int isHoliday;

        public TimeTableItem(String minute,String annotation,String tableno,int isHoliday){
            this.minute=minute;
            this.annotation=annotation;
            this.tableno=tableno;
            this.isHoliday=isHoliday;
        }
    }

    /**
     * ListViewにバインドする時間単位の時刻表
     */
    private class ListItem {
        /** ListViewにバインドするview */
        public View view;

        /** layoutXMLの読込用Inflater */
        LayoutInflater inflater = getActivity().getLayoutInflater();

        /** 運行情報管理クラス */
        InformationHolder informationHolder = InformationHolder.getInstance();

        /** コンストラクタ */
        public ListItem(String hour,List<TimeTableItem> items){
            //ListViewに貼り付けるViewの親となるLayout
            LinearLayout layout = new LinearLayout(context);
            view = layout;

            //時間表示
            TextView textView = new TextView(context);
            textView.setText(hour);
            textView.setTextSize(36f);

            layout.addView(textView);

            //分表示
            for(TimeTableItem item:items){
                //xmlから読込
                View v = inflater.inflate(R.layout.list_item_time,null);
                TextView txMinute = (TextView) v.findViewById(R.id.minute);
                TextView txAnnotation = (TextView) v.findViewById(R.id.annotation);
                TextView txTrainNo = (TextView) v.findViewById(R.id.tableno);
                ImageView imageView = (ImageView) v.findViewById(R.id.train_no_iconeView);

                //ひも付ける
                txMinute.setText(item.minute);
                txAnnotation.setText(item.annotation);

                if(item.isHoliday==0){
                    txTrainNo.setText("KA\n" + item.tableno);
                }else if (item.isHoliday==1){
                    txTrainNo.setText("HA\n" + item.tableno);
                }else{
                    txTrainNo.setText(item.tableno);
                }

                if(informationHolder.getService0().equals(item.tableno)){imageView.setImageResource(R.drawable.monochan_s);}
                else if(informationHolder.getService1().equals(item.tableno)){imageView.setImageResource(R.drawable.urbanflyer_s);}
                else if(informationHolder.getService2().equals(item.tableno)){imageView.setImageResource(R.drawable.urbanflyer_s);}
                else if(informationHolder.getService3().equals(item.tableno)){imageView.setImageResource(R.drawable.urbanflyer_s);}
                else if(informationHolder.getService4().equals(item.tableno)){imageView.setImageResource(R.drawable.urbanflyer_s);}
                else{imageView.setImageResource(R.drawable.blank);}

                layout.addView(v);

                //Listが縮まないように一行目に入れている空白以外
                if(item.minute!="　") {
                    //各分の表示に列車ダイヤの起動をひも付ける
//                    final Context context = context;
                    final Intent intent = new Intent(context, TrainTable.class);

                    final int horiday = item.isHoliday;
                    final int tableNo = Integer.valueOf(item.tableno);


                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent.putExtra("TableNo", tableNo);
                            intent.putExtra("holiday", horiday);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }
                    });
                }

            }

        }
    }

    /**
     * ListViewにバインド作業を行う
     */
    private class TimeTableLIstArrayAdapter extends ArrayAdapter<ListItem> {
        private List<ListItem> listItems;

        public TimeTableLIstArrayAdapter(Context context, List<ListItem> items) {
            super(context, R.layout.list_item_time, items);
            this.listItems = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return listItems.get(position).view;
        }
    }

}
