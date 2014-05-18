package jp.chiba.tackn.monoviewer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import jp.chiba.tackn.monoviewer.man.DisclaimerActivity;
import jp.chiba.tackn.monoviewer.map.MapsActivity;

/**
 * 時刻表アプリの起動画面
 * 本日の運行情報を表示
 * 列車ダイヤと時刻表を開くことが出来るランチャ
 * @author Takumi Ito
 * @since 2014/05/14
 */
public class MainActivity extends Activity implements View.OnClickListener {

    /** デバッグ用タグ */
    private static final String TAG = "MainActivity";
    /** デバッグ用フラグ */
    private static final boolean DEBUG = false;

    /** 時刻表を開くボタン */
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //要素の取得
        findViews();

        //ボタンを押したら時刻表表示
        button.setOnClickListener(this);

        //SyncTaskでherokuから情報取得
        Uri.Builder builder = new Uri.Builder();
        AsyncHttpRequest task = new AsyncHttpRequest(this);
        task.execute(builder);

    }

    /**
     * 要素の取得
     */
    private void findViews() {
        button = (Button) findViewById(R.id.button);
    }

    /**
     * ボタンを押したときの動作
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(view==button) {
            //マップを開く
            Intent intent = new Intent(this, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_back_home:
                Intent home = new Intent(this,MainActivity.class);
//                startActivity(home);
                break;
            case R.id.action_disclaimer:
                Intent disclaimer = new Intent(this,DisclaimerActivity.class);
                startActivity(disclaimer);
                break;
            default:
                return true;
        }
        return false;
    }
}
