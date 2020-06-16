package com.boala.fixcar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.ReadableInstant;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder>{
    private Context context;
    private ArrayList<Commentary> content;
    private SharedPreferences pref;
    WorkShop workShop;

    public CommentAdapter(Context context, ArrayList<Commentary> content,WorkShop workShop) {
        this.context = context;
        this.content = content;
        pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        this.workShop = workShop;
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
        LinearLayout replyWindow = holder.itemView.findViewById(R.id.replyWindow);
        ImageView replyPost = holder.itemView.findViewById(R.id.replyPost);
        EditText replyET = holder.itemView.findViewById(R.id.replyET);
        RatingBar userRatingBar = holder.itemView.findViewById(R.id.userRatingBar);
        if (data.getResponse() != 0){
            holder.itemView.findViewById(R.id.replyComment).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.commentCard).setElevation(0);
        }
        if (pref.getInt("userId",-1) == data.getIduser()) {
            holder.itemView.findViewById(R.id.deleteComment).setVisibility(View.VISIBLE);
            holder.itemView.findViewById(R.id.deleteComment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.delete)
                            .setTitle("Eliminando comentario")
                            .setMessage("¿deseas eliminar este comentario?")
                            .setPositiveButton("eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Call<Boolean> call = FixCarClient.getInstance().getApi().delComment(data.getIdcomentary());
                                    call.enqueue(new Callback<Boolean>() {
                                        @Override
                                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                            if (!response.isSuccessful()) {
                                                Log.e("Code: ", String.valueOf(response.code()));
                                                return;
                                            }
                                            if (data.getRank() != null) {
                                                Call call1 = FixCarClient.getInstance().getApi().delRanking(data.getRank().getIdranking());
                                                call1.enqueue(new Callback() {
                                                    @Override
                                                    public void onResponse(Call call, Response response) {
                                                        if (!response.isSuccessful()) {
                                                            Log.e("Code: ", String.valueOf(response.code()));
                                                            return;
                                                        }
                                                        try {
                                                            content.remove(position);
                                                            notifyItemRemoved(position);
                                                            ShopProfileActivity.userReviewCard.setVisibility(View.VISIBLE);
                                                        } catch (IndexOutOfBoundsException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call call, Throwable t) {
                                                        Log.e("error", t.getMessage());
                                                    }
                                                });
                                            }else{
                                                try {
                                                    content.remove(position);
                                                    notifyItemRemoved(position);
                                                    ShopProfileActivity.userReviewCard.setVisibility(View.VISIBLE);
                                                } catch (IndexOutOfBoundsException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Boolean> call, Throwable t) {
                                            Log.e("error", t.getMessage());
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("no eliminar", null)
                            .show();

                }
            });
            ImageView saveEdit = holder.itemView.findViewById(R.id.saveEdit);
            holder.itemView.findViewById(R.id.editComment).setVisibility(View.VISIBLE);
            holder.itemView.findViewById(R.id.editComment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                replyET.setHint("Editar comentario");
                replyWindow.setVisibility(View.VISIBLE);
                replyPost.setVisibility(View.GONE);
                saveEdit.setVisibility(View.VISIBLE);
                    if (data.getRank() != null) {
                        userRatingBar.setVisibility(View.VISIBLE);
                    }else{
                        userRatingBar.setVisibility(View.GONE);
                    }
                saveEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<Boolean> call = FixCarClient.getInstance().getApi().putCommentary(data.getIdcomentary(),replyET.getText().toString());
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (!response.isSuccessful()) {
                                    Log.e("Code: ", String.valueOf(response.code()));
                                    return;
                                }
                                if (data.getRank() != null) {

                                    Call call1 = FixCarClient.getInstance().getApi().putRank(data.getRank().getIdranking(), String.valueOf(userRatingBar.getRating()));
                                    call1.enqueue(new Callback() {
                                        @Override
                                        public void onResponse(Call call, Response response) {
                                            if (!response.isSuccessful()) {
                                                Log.e("Code: ", String.valueOf(response.code()));
                                                return;
                                            }
                                            replyET.setText("");
                                            userRatingBar.setRating(0);
                                            replyWindow.setVisibility(View.GONE);
                                            ShopProfileActivity.getComments();
                                        }

                                        @Override
                                        public void onFailure(Call call, Throwable t) {
                                            Log.e("error", t.getMessage());
                                        }
                                    });
                                }else{
                                    replyET.setText("");
                                    userRatingBar.setRating(0);
                                    replyWindow.setVisibility(View.GONE);
                                    ShopProfileActivity.getComments();
                                }

                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.e("error", t.getMessage());
                            }
                        });

                    }
                });
                }
            });
        }
        holder.itemView.findViewById(R.id.replyComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyWindow.setVisibility(View.VISIBLE);
                replyPost.setVisibility(View.VISIBLE);
            }
        });
        replyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemView.findViewById(R.id.replyPost).setEnabled(false);
                EditText replyEt = holder.itemView.findViewById(R.id.replyET);
                Call<Boolean> call = FixCarClient.getInstance().getApi().postReply(data.getIdcomentary(),replyEt.getText().toString(),String.valueOf(pref.getInt("userId",-1)),String.valueOf(data.getIdworkshops()));
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Code: ", String.valueOf(response.code()));
                            return;
                        }
                        replyEt.setText("");
                        replyWindow.setVisibility(View.GONE);
                        ShopProfileActivity.getComments();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });
            }
        });
        holder.setData(data);
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        private CircleImageView commentPfp;
        private TextView commentDate, commentName, commentText;
        RecyclerView replyRV;
        CommentAdapter adapter;
        private RatingBar ratingBar;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            commentPfp = itemView.findViewById(R.id.commentPfp);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentName = itemView.findViewById(R.id.commentName);
            commentText = itemView.findViewById(R.id.commentText);
            replyRV = itemView.findViewById(R.id.replyRV);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
            linearLayoutManager1.setReverseLayout(true);
            replyRV.setLayoutManager(linearLayoutManager1);

        }

        public void setData(Commentary data) {
            adapter = new CommentAdapter(context,data.getReplyList(), workShop);
            replyRV.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (data.getIduser() == 0){
                if (workShop.getImage()!=null) {
                    Picasso.get().load("https://fixcarcesur.herokuapp.com" + workShop.getImage().substring(2)).
                            placeholder(R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).into(commentPfp);
                }
                ReadableInstant start;
                ReadableInstant end;
                Date displayDate = new Date();
                String message = "";
                if (data.getUpdate_date().before(data.getCreate_date())){
                    displayDate = data.getCreate_date();
                }else{
                    displayDate = data.getUpdate_date();
                    message = "(editado) ";
                }
                int offset = TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();
                Interval interval = new Interval( displayDate.getTime()+offset,new Date().getTime());
                Log.e("time", (displayDate.getTime()+offset)+", "+new Date().getTime());
                Period period = interval.toPeriod();
                Log.e("period",""+period.getYears()+", "+period.getMonths()+", "+period.getDays()+", "+period.getHours()+", "+period.getMinutes()+", "+period.getSeconds());
                if (period.getYears()>0){
                    if (period.getYears() == 1){
                        commentDate.setText(message+"hace "+1+" año");
                    }else {
                        commentDate.setText(message+"hace "+period.getYears()+" años");
                    }
                }else if (period.getMonths()>0){
                    if (period.getMonths() == 1){
                        commentDate.setText(message+"hace "+1+" mes");
                    }else {
                        commentDate.setText(message+"hace "+period.getMonths()+" meses");
                    }
                }else if (period.getDays()>0) {
                    if (period.getDays() == 1) {
                        commentDate.setText(message+"hace " + 1 + " dia");
                    } else {
                        commentDate.setText(message+"hace " + period.getDays() + " dias");
                    }
                }else if (period.getHours()>0) {
                    if (period.getHours() == 1) {
                        commentDate.setText(message+"hace " + 1 + " hora");
                    } else {
                        commentDate.setText(message+"hace " + period.getHours() + " horas");
                    }
                }else if (period.getMinutes()>0) {
                    if (period.getMinutes() == 1) {
                        commentDate.setText(message+"hace " + 1 + " minuto");
                    } else {
                        commentDate.setText(message+"hace " + period.getMinutes() + " minutos");
                    }
                }else if (period.getSeconds()>0) {
                    if (period.getSeconds() == 1) {
                        commentDate.setText(message+"hace " + 1 + " segundo");
                    } else {
                        commentDate.setText(message+"hace " + period.getSeconds() + " segundos");
                    }
                }
                if (data.getRank()!=null){
                    ratingBar.setRating(data.getRank().getRanking());
                }else{
                    ratingBar.setVisibility(View.GONE);
                }
                commentName.setText(workShop.getName());
                commentText.setText(data.getComentary());
            }else {
                Call<Usuario> call = FixCarClient.getInstance().getApi().getUser(data.getIduser());
                call.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Code: ", String.valueOf(response.code()));
                            return;
                        }
                        Usuario user = response.body();
                        if (user.getImage() != null) {
                            Picasso.get().load("https://fixcarcesur.herokuapp.com" + user.getImage().substring(2)).
                                    placeholder(R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).into(commentPfp);
                        }
                        ReadableInstant start;
                        ReadableInstant end;
                        Date displayDate = new Date();
                        String message = "";
                        if (data.getUpdate_date().before(data.getCreate_date())) {
                            displayDate = data.getCreate_date();
                        } else {
                            displayDate = data.getUpdate_date();
                            message = "(editado) ";
                        }
                        int offset = TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();
                        Interval interval = new Interval(displayDate.getTime() + offset, new Date().getTime());
                        Log.e("time", (displayDate.getTime() + offset) + ", " + new Date().getTime());
                        Period period = interval.toPeriod();
                        Log.e("period", "" + period.getYears() + ", " + period.getMonths() + ", " + period.getDays() + ", " + period.getHours() + ", " + period.getMinutes() + ", " + period.getSeconds());
                        if (period.getYears() > 0) {
                            if (period.getYears() == 1) {
                                commentDate.setText(message + "hace " + 1 + " año");
                            } else {
                                commentDate.setText(message + "hace " + period.getYears() + " años");
                            }
                        } else if (period.getMonths() > 0) {
                            if (period.getMonths() == 1) {
                                commentDate.setText(message + "hace " + 1 + " mes");
                            } else {
                                commentDate.setText(message + "hace " + period.getMonths() + " meses");
                            }
                        } else if (period.getDays() > 0) {
                            if (period.getDays() == 1) {
                                commentDate.setText(message + "hace " + 1 + " dia");
                            } else {
                                commentDate.setText(message + "hace " + period.getDays() + " dias");
                            }
                        } else if (period.getHours() > 0) {
                            if (period.getHours() == 1) {
                                commentDate.setText(message + "hace " + 1 + " hora");
                            } else {
                                commentDate.setText(message + "hace " + period.getHours() + " horas");
                            }
                        } else if (period.getMinutes() > 0) {
                            if (period.getMinutes() == 1) {
                                commentDate.setText(message + "hace " + 1 + " minuto");
                            } else {
                                commentDate.setText(message + "hace " + period.getMinutes() + " minutos");
                            }
                        } else if (period.getSeconds() > 0) {
                            if (period.getSeconds() == 1) {
                                commentDate.setText(message + "hace " + 1 + " segundo");
                            } else {
                                commentDate.setText(message + "hace " + period.getSeconds() + " segundos");
                            }
                        }
                        if (data.getRank() != null) {
                            ratingBar.setRating(data.getRank().getRanking());
                        } else {
                            ratingBar.setVisibility(View.GONE);
                        }
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
}
