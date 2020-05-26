package com.boala.fixcar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ListaTalleresActivity extends AppCompatActivity {
private RecyclerView rvTalleres;
private SwipeRefreshLayout refreshLayout;
private TallerAdapter adapter;
private ArrayList<Taller> tallerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_talleres);
        rvTalleres = findViewById(R.id.rvTalleres);
        refreshLayout = findViewById(R.id.refreshTalleres);
        tallerList = new ArrayList<>();
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTalleres.setLayoutManager(linearLayoutManager);
        adapter = new TallerAdapter(this, tallerList);
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
        Call<List<Taller>> call = FixCarClient.getInstance().getApi().getTalleres();
        call.enqueue(new Callback<List<Taller>>() {
            @Override
            public void onResponse(Call<List<Taller>> call, Response<List<Taller>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                ArrayList<Taller> talleres = (ArrayList) response.body();
                tallerList.clear();
                for (Taller taller : talleres){
                    tallerList.add(taller);
                }
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Taller>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

}
