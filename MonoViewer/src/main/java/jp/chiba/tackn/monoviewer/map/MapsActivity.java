package jp.chiba.tackn.monoviewer.map;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;

import jp.chiba.tackn.monoviewer.R;

public class MapsActivity extends Activity implements monoviewFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
