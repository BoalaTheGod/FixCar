package com.boala.fixcar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WorkShopListActivity extends AppCompatActivity {
private RecyclerView rvTalleres;
private SwipeRefreshLayout refreshLayout;
private WorkShopAdapter adapter;
private ArrayList<WorkShop> workShopList;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_list);
        rvTalleres = findViewById(R.id.rvTalleres);
        refreshLayout = findViewById(R.id.refreshTalleres);
        workShopList = new ArrayList<>();
        Context context;
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTalleres.setLayoutManager(linearLayoutManager);
        adapter = new WorkShopAdapter(this, workShopList);
        rvTalleres.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTalleres();
            }
        });

    }
    private void getTalleres(){
        workShopList.clear();
        Call<List<WorkShop>> call = FixCarClient.getInstance().getApi().getTalleres();
        call.enqueue(new Callback<List<WorkShop>>() {
            @Override
            public void onResponse(Call<List<WorkShop>> call, Response<List<WorkShop>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                ArrayList<WorkShop> talleres = (ArrayList) response.body();
                Call<List<Fav>> call1 = FixCarClient.getInstance().getApi().getUserFav(pref.getInt("userId",-1));
                call1.enqueue(new Callback<List<Fav>>() {
                    @Override
                    public void onResponse(Call<List<Fav>> call, Response<List<Fav>> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Code: ", String.valueOf(response.code()));
                            return;
                        }
                        TreeMap<Integer,Fav> favs = new TreeMap<>();
                        for (Fav fav : response.body()){
                            favs.put(fav.getIdworkshop(),fav);
                        }
                        for (WorkShop workShop : talleres) {
                            if (favs.containsKey(workShop.getIdtaller())) {
                                workShopList.add(workShop);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onFailure(Call<List<Fav>> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });

            }
            @Override
            public void onFailure(Call<List<WorkShop>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.setRefreshing(true);
        getTalleres();
    }
}
