package jp.chiba.tackn.monoviewer.man;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.analytics.tracking.android.EasyTracker;

import jp.chiba.tackn.monoviewer.Menus;
import jp.chiba.tackn.monoviewer.R;
import jp.chiba.tackn.monoviewer.TabletHolder;

public class ManualActivity extends Activity {

    private WebView webView;
    /** タブレットモードの保持 */
    private TabletHolder tabletHolder = TabletHolder.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        findViews();
        webView.loadUrl("file:///android_asset/html/manual/manual.xhtml");
    }
    /**
     * 要素の取得
     */
    private void findViews() {
        webView = (WebView) findViewById(R.id.manual);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(tabletHolder.isTablet()){
            getMenuInflater().inflate(R.menu.tablet,menu);
        }else{
            getMenuInflater().inflate(R.menu.main,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return Menus.actionMenu(this,item);
    }
}
