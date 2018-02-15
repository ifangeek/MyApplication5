package com.example.diegopacheco.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    TextView contador;
    BroadcastReceiver br = new MyBroadcast();
    public long segundo,minuto,hora,dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contador = (TextView)findViewById(R.id.TV_contador);
        iniciarServicio();

    }
    public class MyBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            int numero = Integer.parseInt(intent.getExtras().getInt("cont")+"");

            dia = TimeUnit.SECONDS.toDays(numero);
            hora = TimeUnit.SECONDS.toHours(numero) - dia * 24;
            minuto = TimeUnit.SECONDS.toMinutes(numero) - TimeUnit.SECONDS.toHours(numero)* 60;
            segundo = TimeUnit.SECONDS.toSeconds(numero) - TimeUnit.SECONDS.toMinutes(numero) * 60;
           /* segundo = TimeUnit.SECONDS.toSeconds(numero) - minuto *60;
            Log.d("SEGUNDO",segundo+"");
            minuto = TimeUnit.SECONDS.toMinutes(numero) - hora * 60 ;
            Log.d("MINUTOS",minuto+"");
            hora = TimeUnit.SECONDS.toHours(numero) - TimeUnit.SECONDS.toDays(numero) * 24;
            Log.d("HORA",hora+"");
            dia = TimeUnit.SECONDS.toDays(numero) - hora * 24;*/

            contador.setText(dia+" : "+hora + " : " + minuto + " : "+ segundo);
        }
    }

    private void iniciarServicio(){
        Intent intent = new Intent(this,Service.class);
        startService(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("FILTRO-CONTADOR");
        LocalBroadcastManager.getInstance(this).registerReceiver(br,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
    }
}
