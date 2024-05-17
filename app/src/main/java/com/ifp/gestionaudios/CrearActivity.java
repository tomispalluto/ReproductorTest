package com.ifp.gestionaudios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CrearActivity extends AppCompatActivity {

    private EditText editText;
    private EditText editText2;
    private Button botonCrear;
    private String contenidoCaja1 = "";
    private String contenidoCaja2 = "";
    private DataBaseSQL bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        bd = new DataBaseSQL(this);

        editText = findViewById(R.id.editText1_crear);
        editText2 = findViewById(R.id.editText2_crear);
        botonCrear = findViewById(R.id.button_crear);

        botonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contenidoCaja1= editText.getText().toString();
                contenidoCaja2= editText2.getText().toString();

                if (contenidoCaja1.equals("") || contenidoCaja2.equals(""))
                {
                    Toast.makeText(CrearActivity.this, getString(R.string.toast_crear_mensaje1), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(CrearActivity.this, getString(R.string.toast_crear_mensaje2), Toast.LENGTH_SHORT).show();
                    bd.insertarNota(contenidoCaja1,contenidoCaja2);
                    //caja1.setText("");
                    Intent intent = new Intent(CrearActivity.this, StartActivity.class);
                    startActivity(intent);

                }

            }
        });
    }
}