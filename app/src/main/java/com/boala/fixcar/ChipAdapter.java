package com.boala.fixcar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ChipHolder> {

    private Context context;
    private ArrayList<String> content;

    public ChipAdapter(Context context, ArrayList<String> content) {
        this.context = context;
        this.content = content;
    }

    @NonNull
    @Override
    public ChipHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chip, parent, false);
        return new ChipAdapter.ChipHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipHolder holder, int position) {
        final String data = content.get(position);
        holder.setData(data);
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    private TextView chipTextView;

    public class ChipHolder extends RecyclerView.ViewHolder {
        public ChipHolder(@NonNull View itemView) {
            super(itemView);
            chipTextView = itemView.findViewById(R.id.chipTextView);
        }

        public void setData(String data) {
            chipTextView.setText(data);
        }
    }
}
