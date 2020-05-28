package com.boala.fixcar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private VehAdapterEx adapter;
    private FloatingActionButton fabAddCar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String[] listItems;//variables para la busqueda por filtros
    boolean[] checkedItems;
    private ArrayList<Integer> muserItems = new ArrayList<>();
    boolean dialogShown = false;//variable auxiliar para que solo se muestre el dialogo de vehiculos vacios una vez
    static ArrayList<VehiculoExpandable> vehData;//arrayList de vehiculos auxiliar
    private EditText ets;//edittext para la busqueda por calle
    private ShimmerFrameLayout mShimmerViewContainer;
    private CircleImageView circleImageView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        /**Se comprueba si la sesion esta iniciada, y si no, se va al login**/
        if (!pref.getBoolean("loggedIn", false) && pref.getInt("userId", -1) == -1) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }




        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigation_view);
        setNavigationViewListener();
        ets = findViewById(R.id.etBus);
        RecyclerView rvCars = findViewById(R.id.rvCars);
        Button signOut = findViewById(R.id.signOut);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        /**inicializacion del recyclerview**/
        vehData = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ((SimpleItemAnimator) rvCars.getItemAnimator()).setSupportsChangeAnimations(false);
        rvCars.setLayoutManager(linearLayoutManager);
        adapter = new VehAdapterEx(this, vehData);
        rvCars.setAdapter(adapter);

        circleImageView = navigationView.getHeaderView(0).findViewById(R.id.circleImage);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
            }
        });

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        /**Se comprueba si la sesion esta iniciada**/
        if (pref.getBoolean("loggedIn", false) && pref.getInt("userId", -1) != -1) {
            getVehicles();
        }
        /**Boton para añadir vehiculo**/
        fabAddCar = findViewById(R.id.fabNewCar);
        fabAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditVehicleActivity.class));
            }
        });

        /**Dialog de seleccion de filtros**/

        listItems = getResources().getStringArray(R.array.filtros);
        checkedItems = new boolean[listItems.length];
        Button btTest = findViewById(R.id.testBT);
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Filtro");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            muserItems.add(position);
                        } else {
                            muserItems.remove(Integer.valueOf(position));
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < muserItems.size(); i++) {
                            item = item + listItems[muserItems.get(i)];
                        }
                        ets.setText(item);
                    }
                });
                mBuilder.setNeutralButton("clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            muserItems.clear();
                            ets.setText("");
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();**/
                //startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                scheduleJob();
            }
        });
        /**Boton de cerrar sesion**/
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.apply();
                recreate();
            }
        });

        /**Listeners para ocultar el boton de añadir vehiculo en scroll**/
        rvCars.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fabAddCar.isShown())
                    fabAddCar.show();
                else if (dy > 0 && fabAddCar.isShown())
                    fabAddCar.hide();
            }
        });
        /**Listener para actualizar la lista de vehiculos**/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVehicles();
            }
        });
    }

    /**
     * Se recogen los vehiculos del usuario, y se meten en el recyclerview, si no tiene, le pregunta si quiere añadir uno
     **/
    public void getVehicles() {
        swipeRefreshLayout.setRefreshing(true);
        Call<List<VehiculoExpandable>> call = FixCarClient.getInstance().getApi().getVehiclesUID(pref.getInt("userId", -1));
        call.enqueue(new Callback<List<VehiculoExpandable>>() {
            @Override
            public void onResponse(Call<List<VehiculoExpandable>> call, retrofit2.Response<List<VehiculoExpandable>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }

                List<VehiculoExpandable> vehiculos = response.body();

                vehData.clear();

                if (vehiculos == null && vehData.isEmpty()) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    if (!dialogShown) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Lista de vehiculos vacia")
                                .setMessage("¿deseas añadir un vehiculo?")
                                .setPositiveButton("añadir", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(getApplicationContext(), EditVehicleActivity.class));
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton("no añadir", null)
                                .show();
                        dialogShown = true;
                    }
                }
                if (vehiculos != null) {
                    for (Vehiculo vehiculo : vehiculos) {
                        vehData.add(new VehiculoExpandable(vehiculo));
                    }
                    vehData.get(0).setExpanded(true);
                    Log.d("exito", "se han cargado los vehiculos");
                }
                adapter.notifyDataSetChanged();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);


            }

            @Override
            public void onFailure(Call<List<VehiculoExpandable>> call, Throwable t) {
                Log.e("error", t.getMessage());
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                vehData.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVehicles();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.talleresList:
                startActivity(new Intent(this, WorkShopListActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    return true;
    }
    private void setNavigationViewListener(){
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void scheduleJob(){
        ComponentName componentName = new ComponentName(this, AlarmSchedulerJobService.class);
        JobInfo info = new JobInfo.Builder(123,componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(3600*24*1000)
                .setPersisted(true)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int rescode = scheduler.schedule(info);
        if (rescode == JobScheduler.RESULT_SUCCESS){
            Log.d("tag?","success");
        }else{
            Log.d("tag?","failed");
        }
    }
    public void cancellJob(){
        Context context;
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d("tag2","job cancelled");
    }

}

