package com.boala.fixcar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TallerAdapter extends RecyclerView.Adapter<TallerAdapter.TallerHolder> {

    private Context context;
    private ArrayList<Taller> content;

    public TallerAdapter(Context context, ArrayList<Taller> content) {
        this.context = context;
        this.content = content;
    }

    @NonNull
    @Override
    public TallerAdapter.TallerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_taller, parent, false);
        return new TallerAdapter.TallerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TallerAdapter.TallerHolder holder, int position) {
        final Taller data = content.get(position);
        holder.setData(data);
        holder.shopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ShopProfileActivity.class);
                intent.putExtra("id",data.getIdtaller());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class TallerHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvTipo;
        private CardView shopCard;
        public TallerHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.nombreTaller);
            tvTipo = itemView.findViewById(R.id.tipoTaller);
            shopCard = itemView.findViewById(R.id.shopCard);
        }

        public void setData(Taller data) {
            tvNombre.setText(data.getName());
            tvTipo.setText(data.getEmail());
        }
    }
}
