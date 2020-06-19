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
import retrofit2.Response;

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
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private VehAdapterEx adapter;
    static FloatingActionButton fabAddCar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    boolean dialogShown = false;//variable auxiliar para que solo se muestre el dialogo de vehiculos vacios una vez
    static ArrayList<VehiculoExpandable> vehData;//arrayList de vehiculos auxiliar
    private EditText ets;//edittext para la busqueda por calle
    private ShimmerFrameLayout mShimmerViewContainer;
    private CircleImageView circleImageView;
    private DrawerLayout drawerLayout;
    private ImageButton searchBT;
    private TextView name,email;
    private LinearLayout filters;
    private CheckBox mechanicsC,repairsC,bodyworkC,electricityC,reviewC,creditcardC,premiumC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        /**Se comprueba si la sesion esta iniciada, y si no, se va al login**/
        if (!pref.getBoolean("loggedIn", false)){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        mechanicsC = findViewById(R.id.mechanicsC);
        repairsC = findViewById(R.id.repairsC);
        bodyworkC = findViewById(R.id.bodyworkC);
        electricityC = findViewById(R.id.electricityC);
        creditcardC = findViewById(R.id.creditcardC);
        premiumC = findViewById(R.id.premiumC);
        reviewC = findViewById(R.id.reviewC);
        filters = findViewById(R.id.filters);
        searchBT = findViewById(R.id.searchBT);
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

        searchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("search",ets.getText().toString());
                intent.putExtra("repairs",repairsC.isChecked());
                intent.putExtra("electricity",electricityC.isChecked());
                intent.putExtra("bodywork",bodyworkC.isChecked());
                intent.putExtra("review",reviewC.isChecked());
                intent.putExtra("creditcard",creditcardC.isChecked());
                intent.putExtra("mechanics",mechanicsC.isChecked());
                intent.putExtra("premium",premiumC.isChecked());
                startActivity(intent);
            }
        });

        name = navigationView.getHeaderView(0).findViewById(R.id.hName);
        email = navigationView.getHeaderView(0).findViewById(R.id.hMail);

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
        /**seleccion de filtros**/
        LinearLayout btTest = findViewById(R.id.testBT);
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(filters,!isVisible(filters));
            }
        });
        ets.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("search",ets.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
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
        setProfileInfo();
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
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    return true;
    }
    private void setNavigationViewListener(){
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void setProfileInfo(){
        Call<Usuario> call = FixCarClient.getInstance().getApi().getUser(pref.getInt("userId",-1));
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                Usuario user = response.body();
                if (user.getImage()!=null && user.getImage().length()>1) {
                    Picasso.get().load("https://fixcarcesur.herokuapp.com" + user.getImage().substring(2)).
                            placeholder(R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).
                            into(circleImageView);
                    Log.e("Imagen: ","https://fixcarcesur.herokuapp.com" + user.getImage().substring(2));
                }
                name.setText(user.getName());
                email.setText(user.getEmail());
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }


}

