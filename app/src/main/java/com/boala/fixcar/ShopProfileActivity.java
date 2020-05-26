package com.boala.fixcar;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

public class ShopProfileActivity extends AppCompatActivity {
private int ID;
private RecyclerView chipRV;
private ChipAdapter adapter;
private ArrayList<String> chipList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ID = getIntent().getIntExtra("id",-1);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
