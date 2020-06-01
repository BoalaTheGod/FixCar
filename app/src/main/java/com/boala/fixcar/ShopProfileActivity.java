package com.boala.fixcar;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ShopProfileActivity extends AppCompatActivity {
private int ID;
private RecyclerView chipRV;
private ChipAdapter adapter;
private ArrayList<String> chipList;
private Toolbar toolbar;
private TextView addressTextView, numberTextView, descTextView, emailTextView;
private CollapsingToolbarLayout toolbarLayout;
private RatingBar ratingBar;
private MapView mapview;
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

        mapview = findViewById(R.id.mapview);

        mapview.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

            }
        });


        chipRV = findViewById(R.id.chipRV);
        chipList = new ArrayList<>();
        adapter = new ChipAdapter(this, chipList);
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        chipRV.setLayoutManager(linearLayoutManager);
        chipRV.setAdapter(adapter);

        chipList.add("cambio de aceite");
        chipList.add("chapa y pintura");
        chipList.add("cambio de neumáticos");
        chipList.add("revisiones");
        chipList.add("ITV");

        adapter.notifyDataSetChanged();

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
                WorkShop workShop = response.body();
                addressTextView.setText(workShop.getAdress());
                numberTextView.setText("986425742");
                emailTextView.setText(workShop.getEmail());
                toolbarLayout.setTitle(workShop.getName());
                toolbar.setTitle(workShop.getName());
                ratingBar.setRating(4);
            }

            @Override
            public void onFailure(Call<WorkShop> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }
}
