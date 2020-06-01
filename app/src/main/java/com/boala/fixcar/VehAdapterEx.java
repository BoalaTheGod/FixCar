package com.boala.fixcar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class VehAdapterEx extends RecyclerView.Adapter<VehAdapterEx.VehHolder> {
    /**
     * Asignacion de datos al recyclerview de vehiculos
     **/
    private Context context;
    private ArrayList<VehiculoExpandable> content;

    VehAdapterEx(Context context, ArrayList<VehiculoExpandable> content) {
        this.context = context;
        this.content = content;
    }

    @NonNull
    @Override
    public VehAdapterEx.VehHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car_expandable, parent, false);
        return new VehAdapterEx.VehHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehAdapterEx.VehHolder holder, final int position) {
        final VehiculoExpandable data = content.get(position);
        holder.setData(data);
/**
 AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

 final int id = (int) System.currentTimeMillis();
 Intent intentAlarm = new Intent(context, MainActivity.class);
 PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intentAlarm,PendingIntent.FLAG_UPDATE_CURRENT);
 alarmManager.setExact(AlarmManager.RTC_WAKEUP,data.getFechaItv().getTime(),pendingIntent);
 **/
        LinearLayout LLCard = holder.itemView.findViewById(R.id.LLCard);
        LLCard.setOnClickListener(new View.OnClickListener() {
            boolean expanded = data.isExpanded();

            @Override
            public void onClick(View view) {
                data.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });
        holder.expandable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditVehicleActivity.class);
                intent.putExtra("pos", position);
                intent.putExtra("idVeh", data.getIdVehiculo());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class VehHolder extends RecyclerView.ViewHolder {
        private TextView marca, modelo, matricula, motor, kilometraje, itv, neumaticos, aceite, revision, ensurance;
        LinearLayout expandable;
        ImageView itvIcon, aceiteIcon, neumaticosIcon, revisionIcon, ensuranceIcon;

        public VehHolder(@NonNull View itemView) {
            super(itemView);
            marca = itemView.findViewById(R.id.marca);
            modelo = itemView.findViewById(R.id.modelo);
            matricula = itemView.findViewById(R.id.matricula);
            motor = itemView.findViewById(R.id.motor);
            kilometraje = itemView.findViewById(R.id.kilometraje);
            itv = itemView.findViewById(R.id.itv);
            neumaticos = itemView.findViewById(R.id.neumaticos);
            aceite = itemView.findViewById(R.id.aceite);
            revision = itemView.findViewById(R.id.revision);
            expandable = itemView.findViewById(R.id.expandableTab);
            itvIcon = itemView.findViewById(R.id.itvIcon);
            neumaticosIcon = itemView.findViewById(R.id.neumaticosIcon);
            aceiteIcon = itemView.findViewById(R.id.aceiteIcon);
            revisionIcon = itemView.findViewById(R.id.revisionIcon);
            ensuranceIcon = itemView.findViewById(R.id.ensuranceIcon);
            ensurance = itemView.findViewById(R.id.ensurance);
        }

        public void setData(VehiculoExpandable data) {
            boolean expanded = data.isExpanded();
            expandable.setVisibility(expanded ? View.VISIBLE : View.GONE);
            marca.setText(data.getBrand());
            modelo.setText(data.getModel());
            matricula.setText(data.getLicencePlate());
            motor.setText(data.getEngine());
            kilometraje.setText(data.getKmVehicle() + "km");
            itv.setText(Vehiculo.dateToString(data.getItvDate()));
            neumaticos.setText(Vehiculo.dateToString(data.getTiresDate()));
            aceite.setText(Vehiculo.dateToString(data.getOilDate()));
            revision.setText(Vehiculo.dateToString(data.getRevisionDate()));
            ensurance.setText(Vehiculo.dateToString(data.getEnsuranceDate()));
            Log.e("time", String.valueOf(data.getItvDate().getTime()));
            Log.e("tiempos","itv: "+data.getItvDate().getTime()+",actual: "+System.currentTimeMillis());
            if (data.getItvDate().getTime()-System.currentTimeMillis() < 432000000){
                itvIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_logo_itv_red));
            }else {
                itvIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_logo_itv));
            }
            if (data.getOilDate().getTime()-System.currentTimeMillis() < 432000000){
                aceiteIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.oil_red));
            }else {
                aceiteIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.oil));
            }
            if (data.getTiresDate().getTime()-System.currentTimeMillis() < 432000000){
                neumaticosIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.car_tire_alert_red));
            }else {
                neumaticosIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.car_tire_alert));
            }
            if (data.getRevisionDate().getTime()-System.currentTimeMillis() < 432000000){
                revisionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.car_cog_red));
            }else{
                revisionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.car_cog));
            }
            if (data.getEnsuranceDate().getTime()-System.currentTimeMillis() < 432000000){
                ensuranceIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shield_car_red));
            }else{
                ensuranceIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shield_car));
            }

        }
    }
}

