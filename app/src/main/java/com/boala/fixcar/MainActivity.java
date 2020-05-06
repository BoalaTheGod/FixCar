package com.boala.fixcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Button btTest;
    private RecyclerView rvCars;
    private VehAdapter adapter;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> muserItems = new ArrayList<>();
    ArrayList<Vehiculo> vehData;
    EditText ets;
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
        setNavigationViewListener();
        rvCars = findViewById(R.id.rvCars);
        vehData = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCars.setLayoutManager(linearLayoutManager);
        adapter = new VehAdapter(this, vehData);
        rvCars.setAdapter(adapter);
        vehData.add(new Vehiculo(6786434,Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
        "corsa","opel","V6","mutua","rojo","DC3453"));
        vehData.add(new Vehiculo(4563487,Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                "corsa","opel","V6","mutua","rojo","DC3453"));
        vehData.add(new Vehiculo(36896453,Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                "corsa","opel","V6","mutua","rojo","DC3453"));
        vehData.add(new Vehiculo(58645387,Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                "corsa","opel","V6","mutua","rojo","DC3453"));
        vehData.add(new Vehiculo(88687645,Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                "corsa","opel","V6","mutua","rojo","DC3453"));
        vehData.add(new Vehiculo(758645374,Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                Vehiculo.stringToDate("12/05/2015"),Vehiculo.stringToDate("12/05/2015"),
                "corsa","opel","V6","mutua","rojo","DC3453"));

        adapter.notifyDataSetChanged();

        /**Dialog de seleccion de filtros**/
        listItems = getResources().getStringArray(R.array.filtros);
        checkedItems = new boolean[listItems.length];
        btTest = findViewById(R.id.testBT);
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
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
                mDialog.show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
   private void setNavigationViewListener(){
        navigationView.setNavigationItemSelectedListener(this);
   }
}
