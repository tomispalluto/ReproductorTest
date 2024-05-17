package com.ifp.gestionaudios;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ReproducirActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private ImageButton botonStop;
    private ImageButton botonPlay;
    private ImageButton botonReset;
    private Button botonVolver;
    private DataBaseSQL bd;
    private MediaPlayer mp;
    private String tituloAudio;
    private int resId;
    private int longitud = 0;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproducir);

        bd = new DataBaseSQL(this);

        textView1 = findViewById(R.id.textView2_reproducir);
        textView2 = findViewById(R.id.textView3_reproducir);
        botonStop = findViewById(R.id.button_pause);
        botonPlay = findViewById(R.id.button_start);
        botonReset = findViewById(R.id.button_reset);
        botonVolver = findViewById(R.id.button_volver_reproducir);

        // Recuperar los datos pasados desde la actividad anterior
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("audioData");
            if (bundle != null) {
                tituloAudio = bundle.getString("titulo");
                String url = bundle.getString("url");

                textView1.setText("Titulo: " + tituloAudio);
                textView2.setText("URL: " + url);

                resId = getResources().getIdentifier(tituloAudio, "raw", getPackageName());

                botonPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPaused) {
                            // Si la canción está pausada, reanudar la reproducción desde donde se detuvo
                            mp.start();
                            isPaused = false;
                        } else {
                            reproducirCancion();
                        }
                    }
                });

            }
        }

        botonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null && mp.isPlaying()) {
                    // Si está reproduciendo, pausar y guardar la posición de reproducción
                    mp.pause();
                    longitud = mp.getCurrentPosition();
                    isPaused = true;
                }
            }
        });

        botonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    // Detener la reproducción y preparar para la reproducción desde el principio
                    mp.stop();
                    try {
                        mp.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    longitud = 0;
                }
            }
        });

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    mp.release(); // Liberar recursos del MediaPlayer al volver a la actividad anterior
                }
                Intent intent = new Intent(ReproducirActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

    }

    private void reproducirCancion() {
        if (resId != 0) {
            if (mp != null) {
                mp.release(); // Liberar recursos del MediaPlayer si ya estaba reproduciendo otra canción
            }
            mp = MediaPlayer.create(this, resId);
            if (mp != null) {
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                    }
                });
                mp.seekTo(longitud); // Establecer la posición de reproducción desde donde se detuvo
                mp.start();
            }
        } else {
            // Mostrar Toast si el archivo de audio no existe
            Toast.makeText(this, getString(R.string.toast_reproducir_mensaje), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar recursos del MediaPlayer cuando la actividad se destruye
        if (mp != null) {
            mp.release();
        }
    }
}
