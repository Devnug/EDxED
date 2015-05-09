package edxed.nug.devnug.edxed;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TAG = "AboutFragment";
    static FragmentActivity mActivity;
    private GoogleMap map;
    public View rootView = null;
    private static final LatLng HUDSON_VIEW = new LatLng(40.743248, -74.002695);

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
        } catch (InflateException e) {
            //Log.d(TAG, e.getStackTrace() + "");
        }
        //rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //SupportMapFragment mapFragment = new SupportMapFragment();
        SupportMapFragment suppMap = getMapFragment();
        map = suppMap.getMap();
        // Check if we were successful in obtaining the map.

        if (map != null) {
            // Setup your map...
            //map = suppMap.getMap();
            map.moveCamera( CameraUpdateFactory.newLatLngZoom(HUDSON_VIEW, 15.0f) );
            map.addMarker(new MarkerOptions().position(HUDSON_VIEW));
            //ViewGroup.LayoutParams params = rootView.findViewById(R.id.map).getLayoutParams();
            //params.height = rootView.findViewById(R.id.map).getLayoutParams().width;
            //rootView.findViewById(R.id.map).setLayoutParams(params);
            //Log.d(TAG, "Height: " + rootView.findViewById(R.id.map).getLayoutParams().height);
            //Log.d(TAG, "Width: " + rootView.findViewById(R.id.map).getLayoutParams().width);
            rootView.findViewById(R.id.map).getLayoutParams().height = 600;
            map.getUiSettings().setScrollGesturesEnabled(false);
            map.getUiSettings().setZoomControlsEnabled(false);
            map.getUiSettings().setZoomGesturesEnabled(false);
            map.getUiSettings().setAllGesturesEnabled(false);

            // Trying to cover all the bases to ensure that the map is clickable and opens the map application
            map.setOnMapClickListener(this);
            map.setOnMapLongClickListener(this);
            map.setOnMarkerClickListener(this);
            //Log.d(TAG, "Linear width: " + rootView.findViewById(R.id.map_container).getLayoutParams().width);
        } else {
            int isEnabled = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
            if (isEnabled != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(isEnabled, mActivity, 0);
            }
        }
        //map.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(40.743248, -74.002695), 14.0f) );
        return rootView;
    }

    public SupportMapFragment getMapFragment() {
        FragmentManager fm = null;

        //Log.d(TAG, "sdk: " + Build.VERSION.SDK_INT);
        //Log.d(TAG, "release: " + Build.VERSION.RELEASE);

        //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && Build.MANUFACTURER.toLowerCase().indexOf("samsung") == -1) {
        //    Log.d(TAG, "using getFragmentManager");
        //    fm = getFragmentManager();
        //} else {
        //    Log.d(TAG, "using getChildFragmentManager");
            fm = getChildFragmentManager();
        //}

        return (SupportMapFragment) fm.findFragmentById(R.id.map);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
        this.mActivity = (FragmentActivity)activity;
    }

    private static final String FRAGMENT_TITLE = "About";

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(FRAGMENT_TITLE);
        //map.clear();
    }

    @Override
    public void onPause() {
        killOldMap();
        super.onPause();
    }

    private void killOldMap() {
        SupportMapFragment mapFragment = ((SupportMapFragment) getActivity()
                .getSupportFragmentManager().findFragmentById(R.id.map));

        if(mapFragment != null) {
            FragmentManager fM = getFragmentManager();
            fM.beginTransaction().remove(mapFragment).commit();
        }

    }

    @Override
    public void onDetach() {
        killOldMap();
        super.onDestroy();
    }

    public void onDestroyView() {
        killOldMap();
        super.onDestroyView();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //Log.d(TAG, "onMapClick: tap");
        String name = "HHSLT";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:40.743248,-74.002695?q=40.743248,-74.002695 (" + name + ")"));
        startActivity(intent);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Log.d(TAG, "onLongMapClick: tap");
        String name = "HHSLT";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:40.743248,-74.002695?q=40.743248,-74.002695 (" + name + ")"));
        startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //Log.d(TAG, "onMarkerClick: tap");
        String name = "HHSLT";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:40.743248,-74.002695?q=40.743248,-74.002695 (" + name + ")"));
        startActivity(intent);
        return false;
    }
}

