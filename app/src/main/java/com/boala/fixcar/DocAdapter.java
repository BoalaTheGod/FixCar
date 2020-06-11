package com.boala.fixcar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
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
    @Override
    public void onBindViewHolder(@NonNull DocHolder holder, int position) {
        final DocumentFixCar data = content.get(position);
        holder.setData(data);
        holder.itemView.findViewById(R.id.docImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://fixcarcesur.herokuapp.com"+data.getDocuments().substring(2);
                Intent galleryIntent = new Intent(context,FullScreenImageActivity.class);
                galleryIntent.putExtra("imgLink",url);
                context.startActivity(galleryIntent);
            }
        });
        holder.itemView.findViewById(R.id.shareBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri bmpUri = getLocalBitmapUri(holder.itemView.findViewById(R.id.docImageView));
                if (bmpUri != null) {

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");

                    context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                } else {

                }
            }
        });
        holder.itemView.findViewById(R.id.downloadBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocalBitmapUri(holder.itemView.findViewById(R.id.docImageView));
                Toast.makeText(context,"Se ha descargado el documento",Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.findViewById(R.id.delDoc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrato = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrato.vibrate(50);
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.delete_empty)
                        .setTitle("Eliminar documento")
                        .setMessage("¿deseas eliminar este documento?,\nse eliminará de manera permanente.")
                        .setPositiveButton("eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delDocument(data.getIddocuments(),data,position);
                            }
                        })
                        .setNegativeButton("no eliminar", null)
                        .show();
            }
        });
    }
    public Uri getLocalBitmapUri(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(context,"com.boala.fixcar.provider",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    private void delDocument(int id,DocumentFixCar data,int position) {
        if (id >= 0) {
            Call<Boolean> call = FixCarClient.getInstance().getApi().delDocument(id);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                    if (!response.isSuccessful()) {
                        Log.e("error", String.valueOf(response.code()));
                    }
                    content.remove(content.get(position));
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,content.size());
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
        private ImageView docImage,shareBT,downloadBT;
        private TextView docType, docNotes;
        public DocHolder(@NonNull View itemView) {
            super(itemView);
            docImage = itemView.findViewById(R.id.docImageView);
            docType = itemView.findViewById(R.id.docTypeTV);
            docNotes = itemView.findViewById(R.id.docNotesTV);
            shareBT = itemView.findViewById(R.id.shareBT);
            downloadBT = itemView.findViewById(R.id.downloadBT);
        }

        public void setData(DocumentFixCar data) {
            if (data.getDocuments() != null) {
                Picasso.get().load("https://fixcarcesur.herokuapp.com" + data.getDocuments().substring(2)).into(docImage);
                shareBT.setVisibility(View.VISIBLE);
                downloadBT.setVisibility(View.VISIBLE);
            }else {
                shareBT.setVisibility(View.GONE);
                downloadBT.setVisibility(View.GONE);
            }
            docType.setText(data.getType_document());
            docNotes.setText(data.getNotes());
            Log.e("new doc: ",data.getIddocuments()+"");
        }
    }
}
