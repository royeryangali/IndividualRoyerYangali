package pe.edu.pucp.individualroyeryangali.Medico;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pe.edu.pucp.individualroyeryangali.Entity.CasoCovid;
import pe.edu.pucp.individualroyeryangali.Entity.Usuario;
import pe.edu.pucp.individualroyeryangali.MainActivity;
import pe.edu.pucp.individualroyeryangali.R;
import pe.edu.pucp.individualroyeryangali.VerResumen;

public class CrearCasoDoctor extends AppCompatActivity {

    CasoCovid casoCovid = new CasoCovid();
    Usuario usuario = new Usuario();
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_caso_doctor);
        setTitle("Doctor - ReportApp");
        String[] lista = {"Lima Norte", "Lima Sur", "Lima Este", "Lima Oeste", "Lima Centro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lista);
        Spinner spinner = findViewById(R.id.spinnerZona);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("infoApp", "SELECCIONASTE ESTO : " + parent.getItemAtPosition(position).toString());
                if (position == 0) {
                    casoCovid.setZonaLimena("Lima Norte");
                } else if (position == 1) {
                    casoCovid.setZonaLimena("Lima Sur");
                } else if (position == 2) {
                    casoCovid.setZonaLimena("Lima Este");
                } else if (position == 3) {
                    casoCovid.setZonaLimena("Lima Oeste");
                } else if (position == 4) {
                    casoCovid.setZonaLimena("Lima Centro");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        EditText editTextMarca = findViewById(R.id.editTextTextRegistrador);
        editTextMarca.setText(firebaseUser.getDisplayName());

    }

    public void pickFile(View view) {
        ImageView foto = findViewById(R.id.imageViewFoto);
        if (foto.getVisibility() == View.VISIBLE) {
            foto.setVisibility(View.GONE);
        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Seleccione Foto para subir"), 10);

    }

    public void tomarFoto(View view) {
        TextView textViewFoto = findViewById(R.id.textViewFoto);
        if (textViewFoto.getVisibility() == View.VISIBLE) {
            textViewFoto.setVisibility(View.GONE);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    String fileName = getFileName(uri);
                    TextView textView = findViewById(R.id.textViewFoto);
                    textView.setText(fileName);
                    casoCovid.setNombreFoto(fileName);
                    textView.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    ImageView foto = findViewById(R.id.imageViewFoto);
                    foto.setVisibility(View.VISIBLE);
                    foto.setImageBitmap(bitmap);
                    guardarFotoTomada(bitmap);
                }
                break;
        }
    }

    public void guardarFotoTomada(Bitmap bitmap) {
        casoCovid.setNombreFoto("prueba.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, casoCovid.getNombreFoto());
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void guardarCaso(View view) {
        EditText textviewGPSaValidar = findViewById(R.id.textViewGps);
        if (textviewGPSaValidar.getVisibility() == View.VISIBLE) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            usuario.setNombreUsuario(firebaseUser.getDisplayName());
            usuario.setPrimaryKey(firebaseUser.getUid());
            casoCovid.setUsuarioQueRegistra(usuario);
            casoCovid.setEstado("Pendiente");

            EditText editTextTextSintomas = findViewById(R.id.editTextTextSintomas);
            EditText editTextNombrePaciente = findViewById(R.id.editTextNombrePaciente);
            EditText editTextDniPaciente = findViewById(R.id.editTextDniPaciente);
            EditText editTextGps = findViewById(R.id.textViewGps);
            EditText editTextFecha = findViewById(R.id.editTextTextFecha);
            final TextView textViewFoto = findViewById(R.id.textViewFoto);
            if (editTextTextSintomas.getText().toString().trim().isEmpty()) {
                editTextTextSintomas.setError("Este campo no puede estar vacío.");
            } else {
                if (editTextFecha.getText().toString().trim().isEmpty()) {
                    editTextFecha.setError("Debe seleccionar una fecha.");
                } else {
                    if (editTextNombrePaciente.getText().toString().trim().isEmpty()) {
                        editTextNombrePaciente.setError("Este campo no puede estar vacío.");
                    } else {
                        if (editTextGps.getText().toString().trim().isEmpty()) {
                            editTextGps.setError("Debe prender su GPS.");
                        } else {
                            if (editTextDniPaciente.getText().toString().trim().isEmpty()) {
                                editTextDniPaciente.setError("Debe ingresar un número.");
                            } else {
                                double a = Double.valueOf(editTextDniPaciente.getText().toString().trim());
                                if (a > 99999999 || a < 9999999) {
                                    editTextDniPaciente.setError("Este campo debe contener 8 dígitos.");
                                } else {

                                    if (textViewFoto.getVisibility()==View.INVISIBLE) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                                        alertDialog.setTitle("IMPORTANTE");
                                        alertDialog.setMessage("Debe examinar una foto para registrar el caso.");
                                        alertDialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        alertDialog.show();
                                    } else {
                                        casoCovid.setDireccionGPS(editTextGps.getText().toString());
                                        casoCovid.setDniPaciente(Integer.valueOf(editTextDniPaciente.getText().toString()));
                                        casoCovid.setNombrePaciente(editTextNombrePaciente.getText().toString());
                                        casoCovid.setSintomas(editTextTextSintomas.getText().toString());

                                        String mypk = databaseReference.push().getKey();
                                        casoCovid.setPkCaso(mypk);

                                        databaseReference.child("CasosCovid/" + casoCovid.getPkCaso()).setValue(casoCovid)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("JULIO", "GUARDADO EXITOSO EN TU DATABASE");

                                                        if (textViewFoto.getVisibility() == View.VISIBLE) {
                                                            subirArchivoConPutFile(textViewFoto.getText().toString());
                                                        } else {
                                                            subirArchivoConPutFile(casoCovid.getNombreFoto());
                                                        }

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                });


                                    }
                                }
                            }
                        }
                    }
                }
            }


        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("IMPORTANTE");
            alertDialog.setMessage("Por favor sigua los siguientes pasos:\n \n 1° Active su GPS \n  2° Presione nuevamente en 'OBTENER UBICACION'\n 3°Haga click en 'REGISTRAR' nuevamente");
            alertDialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
            // Toast.makeText(SolicitudReserva.this, "Para reservar debe activar el GPS y presionar el 'BOTÓN DE OBTENER UBICACION'", Toast.LENGTH_SHORT).show();
        }
    }


    public void subirArchivoConPutFile(String fileName) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            //subir archivo a firebase storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            StorageMetadata storageMetadata = new StorageMetadata.Builder()
                    .setCustomMetadata("autor", firebaseUser.getDisplayName())
                    .setCustomMetadata("pk", casoCovid.getPkCaso())
                    .build();

            UploadTask task = storageReference.child(casoCovid.getPkCaso() + "/" + casoCovid.getNombreFoto()).putFile(uri, storageMetadata);


            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CrearCasoDoctor.this);
                    alertDialog.setTitle("¡Creación de caso exitoso!");
                    alertDialog.setMessage("Podrás visualizar la lista de casos registrados en la opción 'Ver lista de casos' en el menú.");
                    alertDialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(CrearCasoDoctor.this, PagPrincipalDoctor.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    alertDialog.show();
                    Log.d("infoApp", "subida exitosa");


                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("infoApp", "error en la subida");
                    e.printStackTrace();
                }
            });
            task.addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                }
            });
            task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    long bytesTransferred = snapshot.getBytesTransferred();
                    long totalByteCount = snapshot.getTotalByteCount();
                    double progreso = (100.0 * bytesTransferred) / totalByteCount;
                    Log.d("infoApp", String.valueOf(progreso));
                }
            });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            Log.d("infoApp", "SIN PERMISOOOOOOOOOOOOOOOOOO");
        }
    }

    public void obtenerFecha(View view) {
        final Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int ano = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                EditText editTextFecha = findViewById(R.id.editTextTextFecha);
                editTextFecha.setText(i2 + "/" + (i1 + 1) + "/" + i);
                String diaLetra = String.valueOf(i2);
                String mesLetra = String.valueOf(i1 + 1);
                String anoLetra = String.valueOf(i);
                String cadenaFecha = diaLetra + "/" + mesLetra + "/" + anoLetra;
                casoCovid.setFechaRegistro(cadenaFecha);
            }
        }, dia, mes, ano);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    private boolean gpsActivo() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return providerEnabled;
    }

    public void mostrarInfoDeUbicacion(View view) {
        if (gpsActivo()) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                FusedLocationProviderClient location = LocationServices.getFusedLocationProviderClient(this);
                location.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("infoApp", "ALt" + location.getLongitude());
                        Log.d("infoApp", "Lat" + location.getLatitude());
                        casoCovid.setLatitud(location.getLatitude());
                        casoCovid.setLongitud(location.getLongitude());
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> direccion = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            Log.d("infoApp", "la direccion es:" + direccion.get(0).getAddressLine(0));
                            TextView textViewGps = findViewById(R.id.textViewGps);
                            textViewGps.setText(direccion.get(0).getAddressLine(0));
                            textViewGps.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(CrearCasoDoctor.this, UbicacionMapActivity.class);
                            intent.putExtra("latitud", casoCovid.getLatitud());
                            intent.putExtra("longitud", casoCovid.getLongitud());
                            startActivity(intent);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                location.getLastLocation().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("infoApp", "Fallo aquí GA");
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            Toast.makeText(CrearCasoDoctor.this, "Por favor active su GPS", Toast.LENGTH_SHORT).show(); //FORMATO DE UN TOAST QUE ES COMO UN POP UP

        }


    }

    ////para relacionar el layout de menú con esta vista

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_doctor, menu);
        return true;
    }

    ///para linkear las opciones del menú con una acción en particular de forma centralizada ///también puede realizarse desde el primer método onCreate pero de otra manera, revisar min 01:18:43 del video zoom

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.abrirMenuDoctor:
                View view = findViewById(R.id.abrirMenuDoctor);
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_doctor, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.verListaCasosDoctor:
                                Intent intent = new Intent(CrearCasoDoctor.this, PagPrincipalDoctor.class);
                                startActivity(intent);
                                return true;
                            case R.id.verResumenDoctor:
                                Intent intent1 = new Intent(CrearCasoDoctor.this, VerResumen.class);
                                startActivity(intent1);
                                return true;
                            case R.id.crearCasoDoctor:
                                Intent intent2 = new Intent(CrearCasoDoctor.this, CrearCasoDoctor.class);
                                startActivity(intent2);
                                finish();
                                return true;
                            case R.id.cerrarSesionDoctor:
                                logOut();
                                return true;
                            default:
                                return false;

                        }
                    }
                });
                popupMenu.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        AuthUI instance = AuthUI.getInstance();
        instance.signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Lógica de cerrao de sesión lo pongo aquí porque luego lo ecesitaremos cuando acabemos el menú de cliente y TI
                Intent intent = new Intent(CrearCasoDoctor.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}