package jp.chiba.tackn.monoviewer.man;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import jp.chiba.tackn.monoviewer.MainActivity;
import jp.chiba.tackn.monoviewer.R;

public class DisclaimerActivity extends Activity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        findViews();
        webView.loadUrl("file:///android_asset/html/disclaimer.xhtml");
    }
    /**
     * 要素の取得
     */
    private void findViews() {
        webView = (WebView) findViewById(R.id.disclaimer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_back_home:
                Intent home = new Intent(this,MainActivity.class);
                startActivity(home);
                break;
            case R.id.action_disclaimer:
                Intent disclaimer = new Intent(this,DisclaimerActivity.class);
//                startActivity(disclaimer);
                break;
            default:
                return true;
        }
        return false;
    }
}
