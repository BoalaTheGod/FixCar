package com.boala.fixcar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VehAdapterEx extends RecyclerView.Adapter<VehAdapterEx.VehHolder> {

    private Context context;
    private ArrayList<VehiculoExpandable> content;

    VehAdapterEx(Context context, ArrayList<VehiculoExpandable> content){
        this.context = context;
        this.content = content;
    }

    @NonNull
    @Override
    public VehAdapterEx.VehHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car_expandable,parent,false);
        return new VehAdapterEx.VehHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehAdapterEx.VehHolder holder, final int position) {
        final VehiculoExpandable data = content.get(position);
        holder.setData(data);
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
                Intent intent = new Intent(context,EditVehicleActivity.class);
                intent.putExtra("idVeh",position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class VehHolder extends RecyclerView.ViewHolder{
        private TextView marca, modelo, matricula, motor, color, kilometraje, itv, neumaticos, aceite, revision;
        LinearLayout expandable;

        public VehHolder(@NonNull View itemView) {
            super(itemView);
            marca = itemView.findViewById(R.id.marca);
            modelo = itemView.findViewById(R.id.modelo);
            matricula = itemView.findViewById(R.id.matricula);
            motor = itemView.findViewById(R.id.motor);
            color = itemView.findViewById(R.id.color);
            kilometraje = itemView.findViewById(R.id.kilometraje);
            itv = itemView.findViewById(R.id.itv);
            neumaticos = itemView.findViewById(R.id.neumaticos);
            aceite = itemView.findViewById(R.id.aceite);
            revision = itemView.findViewById(R.id.revision);
            expandable = itemView.findViewById(R.id.expandableTab);
        }

        public void setData(VehiculoExpandable data) {
            boolean expanded = data.isExpanded();
            expandable.setVisibility(expanded ? View.VISIBLE : View.GONE);
            marca.setText(data.getMarca());
            modelo.setText(data.getModelo());
            matricula.setText(data.getMatricula());
            color.setText(data.getColor());
            motor.setText(data.getMotor());
            kilometraje.setText(data.getKmVehiculo()+"km");
            itv.setText(Vehiculo.dateToString(data.getFechaItv()));
            neumaticos.setText(Vehiculo.dateToString(data.getFechaRuedas()));
            aceite.setText(Vehiculo.dateToString(data.getFechaAceite()));
            revision.setText(Vehiculo.dateToString(data.getFechaRevision()));

        }
    }
}

