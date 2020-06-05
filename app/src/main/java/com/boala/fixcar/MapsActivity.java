package com.boala.fixcar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
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
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, LocationListener {

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 69;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Context context;
        mapFragment.getMapAsync(this);
        search = getIntent().getStringExtra("search");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        goToLocation();
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        Geocoder gc = new Geocoder(this);
        Call<List<WorkShop>> call = FixCarClient.getInstance().getApi().getTalleres();
        call.enqueue(new Callback<List<WorkShop>>() {
            @Override
            public void onResponse(Call<List<WorkShop>> call, Response<List<WorkShop>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                ArrayList<WorkShop> workShops = (ArrayList<WorkShop>) response.body();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (WorkShop workShop : workShops) {
                            try {
                                List<Address> list = gc.getFromLocationName(workShop.getLocation() + ", " + workShop.getAdress(), 1);
                                if (list.size() > 0) {
                                    Address address = list.get(0);
                                    Log.d("location: ", address.toString());
                                    if (search.split(" ").length==1 && search.toLowerCase().equals(address.getLocality().toLowerCase()) || search.equals("")) {

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                if (search.equals("")) {
                    mMap.setMyLocationEnabled(true);
                    locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else if (search.split(" ").length > 1) {
                    Geocoder gc = new Geocoder(this);
                    try {
                        List<Address> list = gc.getFromLocationName(search, 1);
                        if (list.size() > 0) {
                            Address address = list.get(0);
                            Log.d("location: ", address.toString());

                            double lat = address.getLatitude();
                            double lng = address.getLongitude();
                            Log.d("coords: ", lat + "," + lng);

                            LatLng latLng = new LatLng(lat, lng);
                            MarkerOptions hereMarker = new MarkerOptions().position(latLng);
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
                            Address address = list.get(0);
                            Log.d("location: ", address.toString());

                            double lat = address.getLatitude();
                            double lng = address.getLongitude();
                            Log.d("coords: ", lat + "," + lng);

                            LatLng latLng = new LatLng(lat, lng);
                            MarkerOptions hereMarker = new MarkerOptions().position(latLng);
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
}
