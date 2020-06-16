package com.boala.fixcar;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class ShopProfileActivity extends AppCompatActivity implements OnMapReadyCallback{
    public static int ID;
    private RecyclerView chipRV;
    private ChipAdapter adapter;
    private ArrayList<String> chipList;
    private Toolbar toolbar;
    private TextView addressTextView, numberTextView, descTextView, emailTextView;
    private CollapsingToolbarLayout toolbarLayout;
    private RatingBar ratingBar, userRatingBar;
    private ImageView header;
    private CardView vidCard,mapCard;
    public static SharedPreferences pref;
    public static CardView userReviewCard;

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 69;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private WorkShop workShop;
    private MapFragment mapFragment;
    private YouTubePlayerFragment youTubePlayerView;
    private RecyclerView commentsRV;
    public static ArrayList<Commentary> comments;
    public static CommentAdapter commentAdapter;
    private EditText commentET;
    private ImageView commentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        ID = getIntent().getIntExtra("id",-1);

        addressTextView = findViewById(R.id.addressTextView);
        numberTextView = findViewById(R.id.numberTextView);
        descTextView = findViewById(R.id.descTextView);
        emailTextView = findViewById(R.id.emailTextView);
        ratingBar = findViewById(R.id.ratingBar);
        userRatingBar = findViewById(R.id.userRatingBar);
        userReviewCard = findViewById(R.id.userReviewCard);

        mapCard = findViewById(R.id.mapCard);
        vidCard = findViewById(R.id.vidCard);

        youTubePlayerView = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubeVid);

        header = findViewById(R.id.header);

        toolbarLayout = findViewById(R.id.toolbar_layout);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapview);


        chipRV = findViewById(R.id.chipRV);
        chipList = new ArrayList<>();
        adapter = new ChipAdapter(this, chipList);
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        chipRV.setLayoutManager(linearLayoutManager);
        chipRV.setAdapter(adapter);

        commentET = findViewById(R.id.commentET);
        commentPost = findViewById(R.id.commentPost);
        commentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Boolean> call = FixCarClient.getInstance().getApi().postCommentary(commentET.getText().toString(),String.valueOf(pref.getInt("userId",-1)),String.valueOf(ID));
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Code: ", String.valueOf(response.code()));
                            return;
                        }
                        Call call1 = FixCarClient.getInstance().getApi().postRank(String.valueOf(userRatingBar.getRating()),String.valueOf(pref.getInt("userId",-1)),String.valueOf(ID));
                        call1.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (!response.isSuccessful()) {
                                    Log.e("Code: ", String.valueOf(response.code()));
                                    return;
                                }
                                userRatingBar.setRating(0);
                                commentET.setText("");
                                getComments();
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.e("error", t.getMessage());
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });
            }
        });
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

    public static void getComments() {
        TreeMap<Integer,Rank> ranks = new TreeMap<>();
        Call<List<Commentary>> call = FixCarClient2.getInstance().getApi().getCommentarys(ID);
        call.enqueue(new Callback<List<Commentary>>() {
            @Override
            public void onResponse(Call<List<Commentary>> call, Response<List<Commentary>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                ArrayList<Commentary> comments2 = (ArrayList<Commentary>) response.body();
                comments.clear();
                TreeMap<Integer,Commentary> auxList = new TreeMap<>();
                for (Commentary commentary : comments2){
                    commentary.setReplyList(new ArrayList<>());
                }

                Call call1 = FixCarClient.getInstance().getApi().getRanks(ID);
                call1.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (!response.isSuccessful()) {
                            Log.e("Code: ", String.valueOf(response.code()));
                            return;
                        }
                        ArrayList<Rank> ranks1 = (ArrayList<Rank>) response.body();
                        for (Rank rank : ranks1){
                            ranks.put(rank.getId_users(),rank);
                        }
                        for (Commentary commentary : comments2){
                            if (commentary.getResponse() == 0) {
                                commentary.setRank(ranks.get(commentary.getIduser()));
                                auxList.put(commentary.getIdcomentary(),commentary);
                                if (commentary.getIduser() == pref.getInt("userId",-1)){
                                    userReviewCard.setVisibility(View.GONE);
                                }else{
                                    userReviewCard.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        for (Commentary commentary : comments2){
                            if (commentary.getResponse() != 0) {
                                try {
                                    auxList.get(commentary.getResponse()).addReply(commentary);
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                        for (Map.Entry<Integer, Commentary> entry : auxList.entrySet()){
                            comments.add(entry.getValue());
                        }
                        Collections.sort(comments, new Comparator<Commentary>() {
                            @Override
                            public int compare(Commentary commentary, Commentary t1) {
                                return commentary.getCreate_date().compareTo(t1.getCreate_date());
                            }
                        });
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Commentary>> call, Throwable t) {
                Log.e("error", t.getMessage());
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
                Call<Float> call1 = FixCarClient.getInstance().getApi().getAvgRank(ID);
                call1.enqueue(new Callback<Float>() {
                    @Override
                    public void onResponse(Call<Float> call, Response<Float> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Code: ", String.valueOf(response.code()));
                            return;
                        }
                        ratingBar.setRating(response.body());
                    }

                    @Override
                    public void onFailure(Call<Float> call, Throwable t) {
                            Log.e("error", t.getMessage());
                    }
                });
                commentsRV = findViewById(R.id.commentsRV);
                comments = new ArrayList<>();
                commentAdapter = new CommentAdapter(getApplicationContext(), comments,workShop);
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager1.setReverseLayout(true);
                commentsRV.setLayoutManager(linearLayoutManager1);
                commentsRV.setAdapter(commentAdapter);
                getComments();


                if (workShop.getImage()!=null && workShop.getImage().length()>1) {
                    Picasso.get().load("https://fixcarcesur.herokuapp.com/" + workShop.getImage().substring(2)).into(header);
                }
                if (workShop.getState().equals("Premium")) {
                    mapFragment.getMapAsync(ShopProfileActivity.this);
                    if (!workShop.getVideo().equals("")){
                        youTubePlayerView.initialize("AIzaSyDtl-8-48v0x-YHEyENZ-tW8PL52dnszU0", new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                youTubePlayer.cueVideo(workShop.getVideo());
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                            }
                        });
                    }else{
                        vidCard.setVisibility(View.GONE);
                    }
                }else {
                    mapCard.setVisibility(View.GONE);
                    vidCard.setVisibility(View.GONE);
                }
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
