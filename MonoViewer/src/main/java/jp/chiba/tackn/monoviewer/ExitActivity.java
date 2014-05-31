package jp.chiba.tackn.monoviewer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.analytics.tracking.android.EasyTracker;

/**
 * アプリケーションを終了させるためのアクティビティ
 *
 * アプリケーションの終了が上手くいかなかったため
 * アプリをバックグラウンドへ移すのみ
 * @author Takumi Ito
 * @since 2014/05/22
 */
public class ExitActivity extends Activity{
    private static final boolean DEBUG = false;
    private static final String TAG = "ExitActivity";

    public ExitActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(DEBUG) Log.d(TAG,TAG + "#onCreate");
        this.moveTaskToBack(true);
        this.finish();
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
}
