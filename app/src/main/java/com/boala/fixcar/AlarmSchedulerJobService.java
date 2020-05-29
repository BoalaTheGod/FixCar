package com.boala.fixcar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmSchedulerJobService extends JobService {
    private static final String TAG = "NotificationService";
    private static final int TIME = 432000000;
    private boolean jobCancelled = false;
    NotificationManager notificationManager;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        setNotifications(jobParameters);
        return true;
    }

    private void setNotifications(JobParameters jobParameters) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
                Call<List<VehiculoExpandable>> call = FixCarClient.getInstance().getApi().getVehiclesUID(pref.getInt("userId",-1));
                call.enqueue(new Callback<List<VehiculoExpandable>>() {
                    @Override
                    public void onResponse(Call<List<VehiculoExpandable>> call, Response<List<VehiculoExpandable>> response) {
                        if (!response.isSuccessful()) {
                            Log.e("error", String.valueOf(response.code()));
                            return;
                        }
                        ArrayList<VehiculoExpandable> vehiculos = (ArrayList<VehiculoExpandable>) response.body();
                        int i = 0;
                        for (VehiculoExpandable vehiculo : vehiculos){
                            if (vehiculo.getItvDate().getTime()-System.currentTimeMillis() <= TIME && vehiculo.getItvDate().getTime()-System.currentTimeMillis() > 0){
                                notificationManager = getSystemService(NotificationManager.class);
                                createNotificationChannel();
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "notifyFixCar")
                                        .setSmallIcon(R.drawable.ic_logo_itv_white)
                                        .setContentTitle("ITV")
                                        .setContentText("quedan "+((vehiculo.getItvDate().getTime()-System.currentTimeMillis())/(1000*3600*24))+" dias, "+vehiculo.getBrand()+" "+vehiculo.getModel())
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                notificationManager.notify(11+i,builder.build());
                            }

                            if (vehiculo.getRevisionDate().getTime()-System.currentTimeMillis() <= TIME && vehiculo.getRevisionDate().getTime()-System.currentTimeMillis() > 0){
                                notificationManager = getSystemService(NotificationManager.class);
                                createNotificationChannel();
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "notifyFixCar")
                                        .setSmallIcon(R.drawable.car_cog_white)
                                        .setContentTitle("Revisión")
                                        .setContentText("quedan "+((vehiculo.getRevisionDate().getTime()-System.currentTimeMillis())/(1000*3600*24))+" dias, "+vehiculo.getBrand()+" "+vehiculo.getModel())
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                notificationManager.notify(12+i,builder.build());
                            }

                            if (vehiculo.getOilDate().getTime()-System.currentTimeMillis() <= TIME && vehiculo.getOilDate().getTime()-System.currentTimeMillis() > 0){
                                notificationManager = getSystemService(NotificationManager.class);
                                createNotificationChannel();
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "notifyFixCar")
                                        .setSmallIcon(R.drawable.oil_white)
                                        .setContentTitle("Cambio de aceite")
                                        .setContentText("quedan "+((vehiculo.getOilDate().getTime()-System.currentTimeMillis())/(1000*3600*24))+" dias, "+vehiculo.getBrand()+" "+vehiculo.getModel())
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                notificationManager.notify(13+i,builder.build());
                            }

                            if (vehiculo.getTiresDate().getTime()-System.currentTimeMillis() <= TIME && vehiculo.getTiresDate().getTime()-System.currentTimeMillis() > 0){
                                notificationManager = getSystemService(NotificationManager.class);
                                createNotificationChannel();
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "notifyFixCar")
                                        .setSmallIcon(R.drawable.car_tire_alert_white)
                                        .setContentTitle("Cambio de neumaticos")
                                        .setContentText("quedan "+((vehiculo.getTiresDate().getTime()-System.currentTimeMillis())/(1000*3600*24))+" dias, "+vehiculo.getBrand()+" "+vehiculo.getModel())
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                notificationManager.notify(14+i,builder.build());
                            }
                            i++;
                        }
                        jobFinished(jobParameters,false);


                    }

                    @Override
                    public void onFailure(Call<List<VehiculoExpandable>> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });

                /**Intent intent1 = new Intent(getApplicationContext(),ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonPressed = System.currentTimeMillis();
                long  tenSeconds = 10 * 1000;

                alarmManager.set(AlarmManager.RTC_WAKEUP,timeAtButtonPressed+tenSeconds, pendingIntent);**/
            }
        }).start();
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "FixCarReminderChannel";
            String description = "channel for FixCar reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyFixCar", name, importance);
            channel.setDescription(description);

            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobCancelled = true;
        return true;
    }
}
