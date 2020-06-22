package com.boala.fixcar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, LocationListener {

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 69;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private String search;
    private Address addressAux;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView rvMaps;
    private WorkShopAdapter adapter;
    private ArrayList<WorkShop> workShopList;
    private ImageView arrow, searchBtMap;
    private EditText searchMap;
    private CheckBox mechanicsC,repairsC,bodyworkC,electricityC,reviewC,creditcardC,premiumC;
    private LinearLayout filters;
    private LinearLayout btTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Context context;
        bottomSheet = findViewById(R.id.BottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        arrow = findViewById(R.id.arrow);
        searchMap = findViewById(R.id.searchMap);
        searchBtMap = findViewById(R.id.searchBtMap);
        mechanicsC = findViewById(R.id.mechanicsC);
        repairsC = findViewById(R.id.repairsC);
        bodyworkC = findViewById(R.id.bodyworkC);
        electricityC = findViewById(R.id.electricityC);
        creditcardC = findViewById(R.id.creditcardC);
        premiumC = findViewById(R.id.premiumC);
        reviewC = findViewById(R.id.reviewC);
        filters = findViewById(R.id.filters);
        repairsC.setChecked(getIntent().getBooleanExtra("repairs",false));
        bodyworkC.setChecked(getIntent().getBooleanExtra("bodywork",false));
        electricityC.setChecked(getIntent().getBooleanExtra("electricity",false));
        creditcardC.setChecked(getIntent().getBooleanExtra("creditcard",false));
        premiumC.setChecked(getIntent().getBooleanExtra("premium",false));
        reviewC.setChecked(getIntent().getBooleanExtra("review",false));
        mechanicsC.setChecked(getIntent().getBooleanExtra("mechanics",false));


        rvMaps = findViewById(R.id.rvMaps);
        workShopList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMaps.setLayoutManager(linearLayoutManager);
        adapter = new WorkShopAdapter(this, workShopList);
        rvMaps.setAdapter(adapter);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBottomSheet();
            }
        });
        btTest = findViewById(R.id.testBT);
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    setVisibility(filters, !isVisible(filters));
            }
        });
        

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        arrow.setImageResource(R.drawable.chevron_down);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        arrow.setImageResource(R.drawable.chevron_up);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        searchMap.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    search = searchMap.getText().toString();
                    mapFragment.getMapAsync(MapsActivity.this::onMapReady);
                    return true;
                }
                return false;
            }
        });

        search = getIntent().getStringExtra("search");
        mapFragment.getMapAsync(this);
        searchMap.setText(search);

        searchBtMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = searchMap.getText().toString();
                mapFragment.getMapAsync(MapsActivity.this::onMapReady);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.clear();
        goToLocation();

    }

    private void toggleBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void getWorkshops(){
        Geocoder gc = new Geocoder(this);
        Call<List<WorkShop>> call = FixCarClient.getInstance().getApi().getFitleredShops(boolToInt(mechanicsC),boolToInt(repairsC),boolToInt(electricityC),boolToInt(bodyworkC),boolToInt(reviewC),boolToInt(creditcardC),isPremium(premiumC));
        call.enqueue(new Callback<List<WorkShop>>() {
            @Override
            public void onResponse(Call<List<WorkShop>> call, Response<List<WorkShop>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                ArrayList<WorkShop> workShops = (ArrayList<WorkShop>) response.body();
                workShopList.clear();
                adapter.notifyDataSetChanged();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (workShops != null) {
                            for (WorkShop workShop : workShops) {
                                try {
                                    List<Address> list = gc.getFromLocationName(workShop.getLocation() + ", " + workShop.getAdress(), 1);
                                    if (list.size() > 0) {
                                        Address address = list.get(0);
                                        Log.d("location: ", address.toString());
                                        if (addressAux == null) {
                                            addressAux = new Address(Locale.getDefault());
                                            addressAux.setLocality(" ");
                                        }
                                        if (addressAux.getLocality().toLowerCase().equals(address.getLocality().toLowerCase()) || search.isEmpty()) {
                                            workShopList.add(workShop);

                                            double lat = address.getLatitude();
                                            double lng = address.getLongitude();
                                            Log.d("coords: ", lat + "," + lng);

                                            LatLng latLng = new LatLng(lat, lng);
                                            MarkerOptions marker = new MarkerOptions().position(latLng).title(workShop.getName()).snippet(workShop.getDescription() +
                                                    "\n " + workShop.getIdtaller());

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                        @Override
                                                        public void onInfoWindowClick(Marker marker) {
                                                            String raw = marker.getSnippet();
                                                            String[] array = raw.split(" ");
                                                            int id = Integer.valueOf(array[array.length - 1]);
                                                            Intent intent = new Intent(getApplicationContext(), ShopProfileActivity.class);
                                                            intent.putExtra("id", id);
                                                            startActivity(intent);
                                                            Log.e("tag", "" + id);
                                                        }
                                                    });
                                                    mMap.addMarker(marker);
                                                }
                                            });
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<List<WorkShop>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToLocation();
                } else {
                    finish();
                }
                return;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Location button clicked", Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void goToLocation() {
        MarkerOptions hereMarker;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                if (search.isEmpty()) {
                    getWorkshops();
                    mMap.setMyLocationEnabled(true);
                    locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else if (search.contains(" ")) {
                    Geocoder gc = new Geocoder(this);
                    try {
                        List<Address> list = gc.getFromLocationName(search, 1);
                        if (list.size() > 0) {
                            addressAux = list.get(0);
                            Log.d("location: ", addressAux.toString());
                            getWorkshops();

                            double lat = addressAux.getLatitude();
                            double lng = addressAux.getLongitude();
                            Log.d("coords: ", lat + "," + lng);

                            LatLng latLng = new LatLng(lat, lng);
                            hereMarker = new MarkerOptions().position(latLng);
                            Bitmap drawableBitmap = getBitmap(R.drawable.human_handsup);
                            hereMarker.icon(BitmapDescriptorFactory.fromBitmap(drawableBitmap));
                            mMap.addMarker(hereMarker);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                            mMap.animateCamera(cameraUpdate);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Geocoder gc = new Geocoder(this);
                    try {
                        List<Address> list = gc.getFromLocationName(search, 1);
                        if (list.size() > 0) {
                            addressAux = list.get(0);
                            Log.d("location: ", addressAux.toString());
                            getWorkshops();

                            double lat = addressAux.getLatitude();
                            double lng = addressAux.getLongitude();
                            Log.d("coords: ", lat + "," + lng);

                            LatLng latLng = new LatLng(lat, lng);
                            hereMarker = new MarkerOptions().position(latLng);
                            Bitmap drawableBitmap = getBitmap(R.drawable.human_handsup);
                            hereMarker.icon(BitmapDescriptorFactory.fromBitmap(drawableBitmap));
                            mMap.addMarker(hereMarker);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
                            mMap.animateCamera(cameraUpdate);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }
    }
    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    private boolean isVisible(View view){
        if (view.getVisibility() == View.GONE){
            return false;
        }else{
            return true;
        }
    }
    private void setVisibility(View v,boolean visibility){
        if (visibility){
            v.setVisibility(View.VISIBLE);
        }else{
            v.setVisibility(View.GONE);
        }
    }
    private int boolToInt(CheckBox cb){
        if (cb.isChecked()){
            return 1;
        }else {
            return 0;
        }
    }
    private String isPremium(CheckBox cb){
        if (cb.isChecked()){
            return "Premium";
        }else {
            return "";
        }
    }
}
