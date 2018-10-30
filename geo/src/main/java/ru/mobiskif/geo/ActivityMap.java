package ru.mobiskif.geo;

import android.content.Intent;
import android.database.DataSetObserver;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityMap extends ActivityCustom implements OnMapReadyCallback {
    private String TAG = "jop";
    private GoogleMap map;


    public ActivityMap() {
        contentView = R.layout.activity_map;
        nextClass = ActivityDistrict.class;
        action = "GetOrgList";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(contentView);
        MapView mv = findViewById(R.id.map);
        //progressView = findViewById(R.id.progressView);
        //progressView.setVisibility(View.VISIBLE);
        mv.onCreate(savedInstanceState);
        mv.onPause();
        mv.getMapAsync(this);
        mv.onResume();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        MapsInitializer.initialize(this);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String str1 = "Пошла муха на базар";
                String str2 = "Пошла муха на базас";
                Toast.makeText(getApplicationContext(), str1.hashCode()+" "+str2.hashCode(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), nextClass));
            }
        });
        /*
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View lay = getLayoutInflater().inflate(R.layout.card_view, null);
                ((TextView) lay.findViewById(R.id.textView0)).setText("");
                ((TextView) lay.findViewById(R.id.textView1)).setText(marker.getTitle());
                ((TextView) lay.findViewById(R.id.textView2)).setText("");
                ((TextView) lay.findViewById(R.id.textView3)).setText(marker.getSnippet());
                //Bitmap b = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.redcross_small);
                //((ImageView) lay.findViewById(R.id.circleImageView)).setImageBitmap(b);
                //((ImageView) lay.findViewById(R.id.circleImageView)).setVisibility(View.GONE);
                return lay;
            }
        });
        */
        addMarkers2(googleMap);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(60, 30), 11));
    }

    void callback(BaseAdapter adapter) {
        progressView.setVisibility(View.VISIBLE);
        //final DataAdapter adapter = new DataAdapter(this, "GetOrgList");
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                LatLng spb = new LatLng(59.94, 30.29);
                for (int j=0; j<adapter.getCount(); j++) {
                    if (j>10) break;
                    String[] item = (String[]) adapter.getItem(j);
                    //Log.d("jop", item[0]+" "+item[1]+" "+item[2]+" "+item[3]+" ");
                    spb = getLocationFromAddress(item[2]);
                    /*
                    googleMap.addMarker(new MarkerOptions()
                            .position(spb)
                            .title(item[3])
                            //.title("asdsa")
                            .snippet(item[2])
                    ).showInfoWindow();
                    */
                }
                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 12));
                progressView.setVisibility(View.GONE);
            }
        });

    }

    void addMarkers2(GoogleMap googleMap) {
        /*
        progressView.setVisibility(View.VISIBLE);
        //final DataAdapter adapter = new DataAdapter(this, "GetOrgList");
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                LatLng spb = new LatLng(59.94, 30.29);
                for (int j=0; j<adapter.getCount(); j++) {
                    if (j>10) break;
                    String[] item = (String[]) adapter.getItem(j);
                    //Log.d("jop", item[0]+" "+item[1]+" "+item[2]+" "+item[3]+" ");
                    spb = getLocationFromAddress(item[2]);
                    googleMap.addMarker(new MarkerOptions()
                            .position(spb)
                            .title(item[3])
                            //.title("asdsa")
                            .snippet(item[2])
                    ).showInfoWindow();
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 12));
                progressView.setVisibility(View.GONE);
            }
        });
        */
    }

    LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        try {
            List<Address> address;
            address = coder.getFromLocationName(strAddress, 5);
            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            Log.d("jop", lat+" "+lng);
            return new LatLng(lat, lng);
        }
        catch (Exception e) { return new LatLng(0, 0); }
    }

}

