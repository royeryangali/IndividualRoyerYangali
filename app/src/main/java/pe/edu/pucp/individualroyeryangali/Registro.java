package pe.edu.pucp.individualroyeryangali;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pe.edu.pucp.individualroyeryangali.Entity.CitaUsuario;
import pe.edu.pucp.individualroyeryangali.Entity.Usuario;

public class Registro extends AppCompatActivity {

    Usuario user = new Usuario();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        TextView textViewSeñalador = findViewById(R.id.textViewSeñalador);
        String [] lista = {"Doctor","Enfermero", "Paciente"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lista);
        Spinner spinner = findViewById(R.id.spinnerRol);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("infoApp","SELECCIONASTE ESTO : " + parent.getItemAtPosition(position).toString());
                if(position == 0){
                    user.setRol("Doctor");
                    textViewSeñalador.setText("Por favor ingrese su código de Doctor.");
                    textViewSeñalador.setVisibility(View.VISIBLE);
                }else if(position == 1){
                    user.setRol("Enfermero");
                    textViewSeñalador.setText("Por favor ingrese su código de Enfermero.");
                    textViewSeñalador.setVisibility(View.VISIBLE);
                }else if(position == 2){
                    user.setRol("Paciente");
                    textViewSeñalador.setText("Por favor ingrese su código de Paciente.");
                    textViewSeñalador.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void guardarUsuario(View view){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        ArrayList<CitaUsuario> citasDelUsuario =new ArrayList<>();
        user.setListaDeCitasUsuario(citasDelUsuario);
        EditText codigo = findViewById(R.id.editTextNumber);

        String cadena = String.valueOf(codigo.getText());

        if(user.getRol().equalsIgnoreCase("Doctor")){
            int resultadoDoctor = cadena.indexOf("DOC-");
            if(resultadoDoctor!=-1){
                user.setCodigoUsuario(codigo.getText().toString());
                user.setNombreUsuario(firebaseUser.getDisplayName());

                databaseReference.child("users/"+firebaseUser.getUid()).setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("infoApp","GUARDADO EXITOSO EN TU DATABASE");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
            }else {
                Toast.makeText(Registro.this, "Por favor ingrese un código válido de Doctor.", Toast.LENGTH_SHORT).show();
            }
        }else if(user.getRol().equalsIgnoreCase("Enfermero")){
            int resultadoEnfermero=cadena.indexOf("ENF-");
            if(resultadoEnfermero!=-1){
                user.setCodigoUsuario(codigo.getText().toString());
                user.setNombreUsuario(firebaseUser.getDisplayName());

                databaseReference.child("users/"+firebaseUser.getUid()).setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("infoApp","GUARDADO EXITOSO EN TU DATABASE");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
            }else {
                Toast.makeText(Registro.this, "Por favor ingrese un código válido de Enfermero.", Toast.LENGTH_SHORT).show();
            }
        }else if (user.getRol().equalsIgnoreCase("Paciente")){
            user.setCodigoUsuario(codigo.getText().toString());
            user.setNombreUsuario(firebaseUser.getDisplayName());

            databaseReference.child("users/"+firebaseUser.getUid()).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("infoApp","GUARDADO EXITOSO EN TU DATABASE");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }else {
            Toast.makeText(Registro.this, "Por favor ingrese un código válido.", Toast.LENGTH_SHORT).show(); //FORMATO DE UN TOAST QUE ES COMO UN POP UP
        }
    }

}