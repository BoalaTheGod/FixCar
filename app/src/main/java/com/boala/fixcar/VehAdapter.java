package com.boala.fixcar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VehAdapter extends RecyclerView.Adapter<VehAdapter.VehHolder> {
    private Context context;
    private ArrayList<Vehiculo> content;

    VehAdapter(Context context, ArrayList<Vehiculo> content){
        this.context = context;
        this.content = content;
    }

    @NonNull
    @Override
    public VehHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car_card,parent,false);
        return new VehHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehHolder holder, int position) {
        Vehiculo data = content.get(position);
        holder.setData(data);

    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class VehHolder extends RecyclerView.ViewHolder{
        private TextView marca, modelo, matricula, motor, color, kilometraje, itv, neumaticos, aceite, revision;

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
        }

        public void setData(Vehiculo data) {
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
