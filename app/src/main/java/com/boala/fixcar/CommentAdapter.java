package com.boala.fixcar;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private Context context;
    private ArrayList<Commentary> content;

    public CommentAdapter(Context context, ArrayList<Commentary> content) {
        this.context = context;
        this.content = content;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_commentary, parent, false);
        return new CommentAdapter.CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {
        final Commentary data = content.get(position);
        holder.setData(data);
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        private CircleImageView commentPfp;
        private TextView commentDate, commentName, commentText;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            commentPfp = itemView.findViewById(R.id.commentPfp);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentName = itemView.findViewById(R.id.commentName);
            commentText = itemView.findViewById(R.id.commentText);

        }

        public void setData(Commentary data) {

            Call<Usuario> call = FixCarClient.getInstance().getApi().getUser(data.getIduser());
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (!response.isSuccessful()) {
                        Log.e("Code: ", String.valueOf(response.code()));
                        return;
                    }
                    Usuario user = response.body();
                    if (user.getImage()!=null) {
                        Picasso.get().load("https://fixcarcesur.herokuapp.com" + user.getImage().substring(2)).
                                placeholder(R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).into(commentPfp);
                    }
                    commentDate.setText(data.getCreate_date().toString());
                    commentName.setText(user.getName());
                    commentText.setText(data.getComentary());
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Log.e("error", t.getMessage());
                }
            });

        }
    }
}
