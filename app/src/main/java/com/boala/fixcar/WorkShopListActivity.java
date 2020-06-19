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
import java.util.List;

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
        getTalleres();

    }
    private void getTalleres(){
        Call<List<WorkShop>> call = FixCarClient.getInstance().getApi().getTalleres();
        call.enqueue(new Callback<List<WorkShop>>() {
            @Override
            public void onResponse(Call<List<WorkShop>> call, Response<List<WorkShop>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                ArrayList<WorkShop> talleres = (ArrayList) response.body();
                workShopList.clear();
                adapter.notifyDataSetChanged();
                for (WorkShop workShop : talleres) {
                    Call<Boolean> call2 = FixCarClient.getInstance().getApi().isFav(pref.getInt("userId", -1), workShop.getIdtaller());
                    call2.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (!response.isSuccessful()) {
                                Log.e("Code: ", String.valueOf(response.code()));
                                return;
                            }
                            if (response.body()) {
                                workShopList.add(workShop);
                                adapter.notifyItemInserted(workShopList.size()-1);
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Log.e("error", t.getMessage());
                        }
                    });
                }

                refreshLayout.setRefreshing(false);
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
