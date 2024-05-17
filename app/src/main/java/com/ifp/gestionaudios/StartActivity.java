package com.ifp.gestionaudios;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    private TextView text1;
    private ListView listView1;
    private ArrayList<Audio> listaAudios = new ArrayList<Audio>();
    private ArrayAdapter<Audio> adaptador;
    private DataBaseSQL bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startactivity);

        bd = new DataBaseSQL(this);

        text1 = findViewById(R.id.text1_start);
        listView1 = findViewById(R.id.listView);

        // Obtener los datos de la base de datos
        listaAudios = bd.getAllAudios();

        // Inicializar el adaptador con los datos obtenidos de la base de datos
        adaptador = new ArrayAdapter<Audio>(StartActivity.this, android.R.layout.simple_list_item_1, listaAudios) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);

                // Obtener el objeto Audio en la posición actual
                Audio audio = getItem(position);

                // Mostrar el título del audio en el TextView
                textView.setText(audio.getTitulo());

                return view;
            }
        };


        // Establecer el adaptador en el ListView
        listView1.setAdapter(adaptador);

        // Recuperar la información enviada desde CrearActivity
        Intent intent = getIntent();
        if (intent != null) {
            String contenidoCaja1 = intent.getStringExtra("contenidoCaja1");
            String contenidoCaja2 = intent.getStringExtra("contenidoCaja2");

            // Añadir la información al array "audios"
            if (contenidoCaja1 != null && contenidoCaja2 != null) {
                bd.insertarNota(contenidoCaja1, contenidoCaja2);

                // Actualizar la lista de audios
                listaAudios.clear();
                listaAudios.addAll(bd.getAllAudios());
                adaptador.notifyDataSetChanged();
            }
        }

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Obtener el elemento seleccionado de la ListView
                Audio audioSeleccionado = listaAudios.get(i);

                // Crear un Intent para abrir la actividad ReproducirActivity
                Intent intent = new Intent(StartActivity.this, ReproducirActivity.class);

                // Crear un Bundle para almacenar los datos del audio
                Bundle bundle = new Bundle();
                bundle.putString("titulo", audioSeleccionado.getTitulo());
                bundle.putString("url", audioSeleccionado.getUrl());

                // Pasar el Bundle como extra al Intent
                intent.putExtra("audioData", bundle);

                // Iniciar la actividad ReproducirActivity con el Intent
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refrescar la lista de audios cuando la actividad se reanuda
        listaAudios.clear();
        listaAudios.addAll(bd.getAllAudios());
        adaptador.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        int id = item.getItemId();
        if (id == R.id.agregar_boton_menu) {
            Intent intent = new Intent(StartActivity.this, CrearActivity.class);
            startActivity(intent);
        } else if (id == R.id.boton_salir_menu) {
            System.exit(0);
        }
        return true;
    }
}
