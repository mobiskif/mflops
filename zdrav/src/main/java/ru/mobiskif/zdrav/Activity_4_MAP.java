package ru.mobiskif.zdrav;

import android.database.DataSetObserver;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ru.healthy.R;

import static java.lang.Math.random;

public class Activity_4_MAP extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng spb = new LatLng(59.94, 30.29);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 12));
        addMarkers(googleMap);
    }

    LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        try {
            List<Address> address;
            address = coder.getFromLocationName(strAddress, 5);
            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            //Log.d("jop", lat+" "+lng);
            return new LatLng(lat, lng);
        }
        catch (Exception e) { return new LatLng(0, 0); }
    }

    LatLng addMarkers(final GoogleMap googleMap) {
        LatLng latLng = new LatLng(59.94, 30.29);
        int i = (int) (random() * 10000);
        final DataAdapter adapter = new DataAdapter(this, i, "GetOrgList");
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                LatLng spb = new LatLng(59.94, 30.29);
                for (int j=0; j<adapter.getCount(); j++) {
                    if (j>20) break;
                    String[] item = (String[]) adapter.getItem(j);
                    spb = getLocationFromAddress(item[2]);
                    googleMap.addMarker(new MarkerOptions()
                            .position(spb)
                            .title(item[1])
                            .snippet(item[0])
                    ).showInfoWindow();
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 12));
            }
        });
        return latLng;
    }

}
