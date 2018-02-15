package com.example.diegopacheco.myapplication;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;


public class Service extends android.app.Service{


    public Integer contador = 86385;
    TimerTask timerTask;
    Timer timer ;

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Service Running...")
                .setTicker("Service Running...")
                .setContentText("Service Running...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true).build();

        startForeground(101,notification);


        if(timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {

                    contador += 1;
                   // Log.d("contador", contador + "");
                    Intent intent = new Intent("FILTRO-CONTADOR");
                    intent.putExtra("cont", contador);
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);

                }
            };
            timer.schedule(timerTask, 0, 1000);
        }
        //Crea de nuevo el servicio despues de haber sido destruido por el sistema.
        return START_NOT_STICKY;
    }
    private void pararcontador(){
        if(contador != null){
            timerTask.cancel();
        }
    }

    @Override
    public void onDestroy() {
       pararcontador();
       Log.d(TAG,"Servicio destruido ...");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
