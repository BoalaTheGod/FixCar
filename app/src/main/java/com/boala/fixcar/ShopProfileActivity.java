package com.boala.fixcar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopProfileActivity extends AppCompatActivity implements OnMapReadyCallback{
private int ID;
private RecyclerView chipRV;
private ChipAdapter adapter;
private ArrayList<String> chipList;
private Toolbar toolbar;
private TextView addressTextView, numberTextView, descTextView, emailTextView;
private CollapsingToolbarLayout toolbarLayout;
private RatingBar ratingBar;

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 69;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private WorkShop workShop;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ID = getIntent().getIntExtra("id",-1);

        addressTextView = findViewById(R.id.addressTextView);
        numberTextView = findViewById(R.id.numberTextView);
        descTextView = findViewById(R.id.descTextView);
        emailTextView = findViewById(R.id.emailTextView);
        ratingBar = findViewById(R.id.ratingBar);

        toolbarLayout = findViewById(R.id.toolbar_layout);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapview);


        chipRV = findViewById(R.id.chipRV);
        chipList = new ArrayList<>();
        adapter = new ChipAdapter(this, chipList);
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        chipRV.setLayoutManager(linearLayoutManager);
        chipRV.setAdapter(adapter);

        if (ID != -1){
            getWorkShop();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "añadido a favoritos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getWorkShop(){
        Call<WorkShop> call = FixCarClient.getInstance().getApi().getTaller(ID);
        call.enqueue(new Callback<WorkShop>() {
            @Override
            public void onResponse(Call<WorkShop> call, Response<WorkShop> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                workShop = response.body();
                addressTextView.setText(workShop.getAdress());
                numberTextView.setText(workShop.getPhone());
                emailTextView.setText(workShop.getEmail());
                descTextView.setText(workShop.getDescription());
                toolbarLayout.setTitle(workShop.getName());
                toolbar.setTitle(workShop.getName());
                setChips();
                ratingBar.setRating(4);
                mapFragment.getMapAsync(ShopProfileActivity.this);
            }

            @Override
            public void onFailure(Call<WorkShop> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void setChips(){
       String raw = workShop.getType();
       String[] array = raw.split(",");
       for (String string : array){
           chipList.add(string);
       }
       adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        goToLocation();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void goToLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (mMap != null){
                Context context;
                Geocoder gc = new Geocoder(this);
                try {
                    List<Address> list = gc.getFromLocationName(workShop.getLocation()+", "+workShop.getAdress(), 1);
                    if (list.size() > 0) {
                        Address address = list.get(0);
                        Log.d("location: ", address.toString());

                        double lat = address.getLatitude();
                        double lng = address.getLongitude();
                        Log.d("coords: ", lat+","+lng);

                        LatLng latLng = new LatLng(lat, lng);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                        MarkerOptions marker = new MarkerOptions().position(latLng).title(workShop.getName()).snippet(workShop.getDescription()+"\n "+workShop.getIdtaller());
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                String raw = marker.getSnippet();
                                String[] array = raw.split(" ");
                                int id = Integer.valueOf(array[array.length-1]);
                                Log.e("tag",""+id);
                            }
                        });
                        mMap.addMarker(marker);
                        mMap.moveCamera(cameraUpdate);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }else {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
        }
    }

}
