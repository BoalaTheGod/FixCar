package com.boala.fixcar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkShopAdapter extends RecyclerView.Adapter<WorkShopAdapter.TallerHolder> {

    private Context context;
    private ArrayList<WorkShop> content;

    public WorkShopAdapter(Context context, ArrayList<WorkShop> content) {
        this.context = context;
        this.content = content;
    }

    @NonNull
    @Override
    public WorkShopAdapter.TallerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_taller, parent, false);
        return new WorkShopAdapter.TallerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkShopAdapter.TallerHolder holder, int position) {
        final WorkShop data = content.get(position);
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
        private ImageView imageView;
        private RatingBar ratingBar;
        public TallerHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.nombreTaller);
            tvTipo = itemView.findViewById(R.id.tipoTaller);
            shopCard = itemView.findViewById(R.id.shopCard);
            imageView = itemView.findViewById(R.id.workshopImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        public void setData(WorkShop data) {
            Call<Float> call1 = FixCarClient.getInstance().getApi().getAvgRank(data.getIdtaller());
            call1.enqueue(new Callback<Float>() {
                @Override
                public void onResponse(Call<Float> call, Response<Float> response) {
                    if (!response.isSuccessful()) {
                        Log.e("Code: ", String.valueOf(response.code()));
                        return;
                    }
                    if (response.body() != null) {
                        ratingBar.setRating(response.body());
                    }else{
                        ratingBar.setRating(0);
                    }
                }

                @Override
                public void onFailure(Call<Float> call, Throwable t) {
                    Log.e("error", t.getMessage());
                }
            });
            tvNombre.setText(data.getName());
            tvTipo.setText(data.getDescription());
            if (data.getImage()!=null && data.getImage().length()>1) {
                Picasso.get().load("https://fixcarcesur.herokuapp.com" + data.getImage().substring(2)).into(imageView);
            }
        }
    }
}
