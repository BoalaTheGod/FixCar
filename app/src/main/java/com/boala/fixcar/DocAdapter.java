package com.boala.fixcar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.DocHolder> {
    private Context context;
    private ArrayList<DocumentFixCar> content;

    public DocAdapter(Context context, ArrayList<DocumentFixCar> content) {
        this.context = context;
        this.content = content;
    }

    @NonNull
    @Override
    public DocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
        return new DocAdapter.DocHolder(view);
    }
private int position2;
    @Override
    public void onBindViewHolder(@NonNull DocHolder holder, int position) {
        final DocumentFixCar data = content.get(position);
        holder.setData(data);
        position2 = position;
        holder.itemView.findViewById(R.id.docCard).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Vibrator vibrato = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrato.vibrate(50);
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.delete_empty)
                        .setTitle("Eliminar documento")
                        .setMessage("¿deseas eliminar este documento?,\nse eliminará de manera permanente.")
                        .setPositiveButton("eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delDocument(data.getIddocuments(),data);
                            }
                        })
                        .setNegativeButton("no eliminar", null)
                        .show();
                return false;
            }
        });
    }
    private void delDocument(int id,DocumentFixCar data) {
        if (id >= 0) {
            Call<Boolean> call = FixCarClient.getInstance().getApi().delDocument(id);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                    if (!response.isSuccessful()) {
                        Log.e("error", String.valueOf(response.code()));
                    }
                    EditVehicleActivity.docsData.remove(data);
                    notifyItemRemoved(position2);
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e("error2:", t.getMessage());
                }
            });
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class DocHolder extends RecyclerView.ViewHolder {
        private ImageView docImage;
        private TextView docType, docNotes;
        public DocHolder(@NonNull View itemView) {
            super(itemView);
            docImage = itemView.findViewById(R.id.docImageView);
            docType = itemView.findViewById(R.id.docTypeTV);
            docNotes = itemView.findViewById(R.id.docNotesTV);
        }

        public void setData(DocumentFixCar data) {
            if (data.getDocuments() != null) {
                Picasso.get().load("https://fixcarcesur.herokuapp.com" + data.getDocuments().substring(2)).into(docImage);
            }
            docType.setText(data.getType_document());
            docNotes.setText(data.getNotes());
            Log.e("new doc: ",data.getIddocuments()+"");
        }
    }
}
