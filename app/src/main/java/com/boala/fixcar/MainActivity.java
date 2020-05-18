package com.boala.fixcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Button btTest;
    private RecyclerView rvCars;
    private VehAdapterEx adapter;
    private FloatingActionButton fabAddCar;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> muserItems = new ArrayList<>();
    static ArrayList<VehiculoExpandable> vehData;
    EditText ets;

    FixCarApi fixCarApi;

    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigation_view);
        ets = findViewById(R.id.etBus);
        rvCars = findViewById(R.id.rvCars);
        vehData = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ((SimpleItemAnimator) rvCars.getItemAnimator()).setSupportsChangeAnimations(false);
        rvCars.setLayoutManager(linearLayoutManager);
        adapter = new VehAdapterEx(this, vehData);
        rvCars.setAdapter(adapter);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new
                OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder().header("Authorization", Credentials.basic("Cesur","FixCar"));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).addInterceptor(loggingInterceptor).build();
        Gson gson = new GsonBuilder().setLenient().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fixcarcesur.herokuapp.com/model/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        fixCarApi = retrofit.create(FixCarApi.class);

        /**vehData.add(new VehiculoExpandable(758645374,Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                "corsa","opel","V6","mutua","rojo","DC3453",""));**/

        getVehicles();

        /**vehData.get(0).setExpanded(true);
        adapter.notifyDataSetChanged();
         **/

        fabAddCar = findViewById(R.id.fabNewCar);
        fabAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EditVehicleActivity.class));
            }
        });

        /**Dialog de seleccion de filtros**/
        listItems = getResources().getStringArray(R.array.filtros);
        checkedItems = new boolean[listItems.length];
        btTest = findViewById(R.id.testBT);
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Filtro");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked){
                            muserItems.add(position);
                        }else {
                            muserItems.remove(Integer.valueOf(position));
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i<muserItems.size(); i++){
                            item = item + listItems[muserItems.get(i)];
                        }
                        ets.setText(item);
                    }
                });
                mBuilder.setNeutralButton("clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i<checkedItems.length; i++){
                            checkedItems[i] = false;
                            muserItems.clear();
                            ets.setText("");
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();**/
                startActivity(new Intent(getApplicationContext(),UserTest.class));
            }
        });

        rvCars.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView,newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy<0 && !fabAddCar.isShown())
                    fabAddCar.show();
                else if(dy>0 && fabAddCar.isShown())
                    fabAddCar.hide();
            }
        });
    }

    public void getVehicles(){
        Call<List<VehiculoExpandable>> call = fixCarApi.getVehicles();
        call.enqueue(new Callback<List<VehiculoExpandable>>() {
            @Override
            public void onResponse(Call<List<VehiculoExpandable>> call, retrofit2.Response<List<VehiculoExpandable>> response) {
                if (!response.isSuccessful()){
                    Log.e("Code: ",String.valueOf(response.code()));
                }

                List<VehiculoExpandable> vehiculos = response.body();

                for (Vehiculo vehiculo : vehiculos){
                    vehData.add(new VehiculoExpandable(vehiculo));
                }

                vehData.get(0).setExpanded(true);
                adapter.notifyDataSetChanged();
                Log.d("exito","se han cargado los vehiculos");
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<VehiculoExpandable>> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
   private void setNavigationViewListener(){
        navigationView.setNavigationItemSelectedListener(this);
   }
}
