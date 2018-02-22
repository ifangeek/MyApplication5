package com.example.diegopacheco.myapplication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR;

public class MainActivity extends AppCompatActivity {

    TextView contador;
    Button iniciar, pausar, detener;
    Service mService;
    boolean mBound = false;
    String result;


    BroadcastReceiver br = new MyBroadcast();
    public long segundo, minuto, hora, dia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contador = (TextView) findViewById(R.id.TV_contador);
        iniciar = (Button) findViewById(R.id.btnIniciar);
        pausar = (Button) findViewById(R.id.btnPausar);
        detener = (Button) findViewById(R.id.btnDetener);

        iniciarServicio();
        Pausar();
        Reanudar();
        Detener();

    }

    public class MyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int numero = Integer.parseInt(intent.getExtras().getInt("cont") + "");

            dia = TimeUnit.SECONDS.toDays(numero);
            hora = TimeUnit.SECONDS.toHours(numero) - dia * 24;
            minuto = TimeUnit.SECONDS.toMinutes(numero) - TimeUnit.SECONDS.toHours(numero) * 60;
            segundo = TimeUnit.SECONDS.toSeconds(numero) - TimeUnit.SECONDS.toMinutes(numero) * 60;

            result = String.format("%1$02d:%2$02d:%3$02d:%4$02d", dia, hora, minuto, segundo);
            contador.setText(result.toString());

            Log.d("RESULTADO: ", result);
        }
    }

    private void iniciarServicio() {
        Intent intent = new Intent(this, Service.class);
        startService(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("FILTRO-CONTADOR");
        LocalBroadcastManager.getInstance(this).registerReceiver(br, filter);

        Intent intent = new Intent(this, Service.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

    }


    public void Pausar() {
        pausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.pararcontador();
                iniciar.setText("Reanudar Servicio");
                iniciar.setEnabled(true);

            }
        });

    }

    public void Reanudar() {

            iniciar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contador.setText(result);
                    mService.reanudarcontador();
                    iniciar.setEnabled(false);
                }
            });
        }



    //Uso de clases del Servicio.
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Service.LocalBinder binder = (Service.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
                mBound = false;
        }
    };

    public void Detener(){
        detener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.detener();
                contador.setText("00:00:00:00");
                iniciar.setText("Iniciar Servicio");
                iniciar.setEnabled(true);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mService.pararcontador();
    }
}
