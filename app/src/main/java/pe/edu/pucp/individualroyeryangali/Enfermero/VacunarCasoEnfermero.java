package pe.edu.pucp.individualroyeryangali.Enfermero;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import pe.edu.pucp.individualroyeryangali.Entity.CasoCovid;
import pe.edu.pucp.individualroyeryangali.R;

public class VacunarCasoEnfermero extends AppCompatActivity {
    CasoCovid casoCovidEnfermero = new CasoCovid();
    Uri uri = null;
    StorageReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacunar_caso_enfermero);
        Intent intent =  getIntent();
        casoCovidEnfermero = (CasoCovid) intent.getSerializableExtra("casoCovidEnfermero");

        ImageView imagen = findViewById(R.id.imageViewFotoEditVacuna);
        reference = FirebaseStorage.getInstance().getReference().child(casoCovidEnfermero.getPkCaso()+"/"+casoCovidEnfermero.getNombreFoto());
        Glide.with(this).load(reference).into(imagen);

        TextView textViewZonaVacuna = findViewById(R.id.textViewZonaEditVacuna);
        textViewZonaVacuna.setText("Zona: "+casoCovidEnfermero.getZonaLimena());

        TextView textViewRegistradorVacuna = findViewById(R.id.textviewRegistradorEditVacuna);
        textViewRegistradorVacuna.setText(casoCovidEnfermero.getUsuarioQueRegistra().getNombreUsuario());

        TextView texViewSintomasVacuna = findViewById(R.id.textviewSintomasEditVacuna);
        texViewSintomasVacuna.setText(casoCovidEnfermero.getSintomas());

        TextView textViewFechacreacionVacuna = findViewById(R.id.textviewFechaCreacionEditVacuna);
        textViewFechacreacionVacuna.setText(casoCovidEnfermero.getFechaRegistro());

        TextView textViewPacienteVacuna = findViewById(R.id.textviewPacienteVacuna);
        textViewPacienteVacuna.setText(casoCovidEnfermero.getNombrePaciente());

        TextView textViewDniPacienteVacuna = findViewById(R.id.textviewDniPacienteVacuna);
        String valor = String.valueOf(casoCovidEnfermero.getDniPaciente());
        textViewDniPacienteVacuna.setText(valor);

        TextView textViewDireccionGpsVacuna = findViewById(R.id.textviewDireccionGpsEditVacuna);
        textViewDireccionGpsVacuna.setText(String.valueOf(casoCovidEnfermero.getDireccionGPS()));

        TextView textViewEstadoVacuna = findViewById(R.id.textviewEstadoVacuna);
        textViewEstadoVacuna.setText(casoCovidEnfermero.getEstado());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    ImageView foto = findViewById(R.id.imageViewFotoEdit);
                    foto.setVisibility(View.INVISIBLE);
                    uri = data.getData();
                    TextView textView = findViewById(R.id.textViewNombreFotoEdit);
                    textView.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    TextView textView = findViewById(R.id.textViewNombreFotoEdit);
                    textView.setVisibility(View.INVISIBLE);
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap =  (Bitmap) bundle.get("data");
                    ImageView foto = findViewById(R.id.imageViewFotoEdit);
                    foto.setVisibility(View.VISIBLE);
                    foto.setImageBitmap(bitmap);
                }
                break;
        }
    }

    public void guardarVacuna(View view){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        casoCovidEnfermero.setEstado("Vacunado");
        final TextView textViewFoto = findViewById(R.id.textViewFoto);

        databaseReference.child("CasosCovid/"+casoCovidEnfermero.getPkCaso()).setValue(casoCovidEnfermero)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("infoApp","GUARDADO EXITOSO EN TU DATABASE");

                            Intent intent = new Intent(VacunarCasoEnfermero.this, PagPrincipalEnfermero.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(VacunarCasoEnfermero.this, "Usuario vacunado exitosamente", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

    }
    public void obtenerFechaVacuna(View view) {
        final Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int ano = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                EditText editTextFecha = findViewById(R.id.editTextTextFechaVacuna);
                editTextFecha.setText(i2 + "/" + (i1 + 1) + "/" + i);
                String diaLetra = String.valueOf(i2);
                String mesLetra = String.valueOf(i1 + 1);
                String anoLetra = String.valueOf(i);
                String cadenaFecha = diaLetra + "/" + mesLetra + "/" + anoLetra;
                casoCovidEnfermero.setFechaFinalizado(cadenaFecha);
            }
        }, dia, mes, ano);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }


}