package pe.edu.pucp.individualroyeryangali.Medico;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
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

import pe.edu.pucp.individualroyeryangali.Entity.CasoCovid;
import pe.edu.pucp.individualroyeryangali.MainActivity;
import pe.edu.pucp.individualroyeryangali.R;

public class EditarCasoDoctor extends AppCompatActivity {
    CasoCovid casoCovid = new CasoCovid();
    Uri uri = null;
    StorageReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_caso_doctor);
        Intent intent =  getIntent();
        casoCovid = (CasoCovid) intent.getSerializableExtra("casoCovid");
        ImageView imagen = findViewById(R.id.imageViewFotoEdit);
        reference = FirebaseStorage.getInstance().getReference().child(casoCovid.getPkCaso()+"/"+casoCovid.getNombreFoto());
        Glide.with(this).load(reference).into(imagen);
        TextView textView = findViewById(R.id.textViewZonaEdit);
        textView.setText("Zona: "+casoCovid.getZonaLimena());
        EditText editCaracteristica = findViewById(R.id.editTextRegistradorEdit);
        editCaracteristica.setText(casoCovid.getUsuarioQueRegistra().getNombreUsuario());
        EditText editTextMarcaEdit = findViewById(R.id.editTextSintomasEdit);
        editTextMarcaEdit.setText(casoCovid.getSintomas());
        EditText editTextInluyeEdit = findViewById(R.id.editTexFechaCreacionEdit);
        editTextInluyeEdit.setText(casoCovid.getFechaRegistro());
        EditText editTextStockEdit = findViewById(R.id.editTextDireccionGpsEdit);
        editTextStockEdit.setText(String.valueOf(casoCovid.getDireccionGPS()));
    }
    public void limpiarImagen(View view){

        TextView textViewFoto = findViewById(R.id.textViewNombreFotoEdit);
        if(textViewFoto.getVisibility()==View.VISIBLE){
            textViewFoto.setVisibility(View.INVISIBLE);
        }
        ImageView imagen = findViewById(R.id.imageViewFotoEdit);
        if(imagen.getVisibility()!=View.VISIBLE){
            imagen.setVisibility(View.VISIBLE);
        }
        if(uri  != null){
            Log.d("JULIO","PK: " + casoCovid.getPkCaso());
            Glide.with(this).load(reference).into(imagen);
        }
        uri = null;
    }

    public void pickFileEdit(View view) {
        ImageView foto = findViewById(R.id.imageViewFotoEdit);
        if(foto.getVisibility()==View.VISIBLE){
            foto.setVisibility(View.INVISIBLE);
        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Seleccione Foto para subir"), 10);

    }

    public  void  tomarFotoEdit(View view){
        TextView textViewFoto = findViewById(R.id.textViewNombreFotoEdit);
        if(textViewFoto.getVisibility()==View.VISIBLE){
            textViewFoto.setVisibility(View.INVISIBLE);
        }
        Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,1);
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
                    String fileName = getFileNameEdit(uri);
                    TextView textView = findViewById(R.id.textViewNombreFotoEdit);
                    textView.setText(fileName);
                    casoCovid.setNombreFoto(fileName);
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
                    guardarFotoTomadaEdit(bitmap);
                }
                break;
        }
    }

    public void guardarFotoTomadaEdit(Bitmap bitmap){
        casoCovid.setNombreFoto("prueba.jpg");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, casoCovid.getNombreFoto());
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            uri  = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            try(OutputStream outputStream = getContentResolver().openOutputStream(uri)){
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    public String getFileNameEdit(Uri uri) {
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
    public void guardarDispositivoEdit(View view){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        EditText editTextSintomas = findViewById(R.id.editTextSintomasEdit);
        casoCovid.setSintomas(editTextSintomas.getText().toString());
        EditText editTextTextRegistrador = findViewById(R.id.editTextRegistradorEdit);
       EditText editTextEstado = findViewById(R.id.editTextEstado);
        editTextEstado.setText(casoCovid.getEstado());
        editTextTextRegistrador.setText(casoCovid.getUsuarioQueRegistra().getNombreUsuario());
        EditText editTextFechaCreacion = findViewById(R.id.editTexFechaCreacionEdit);
        casoCovid.setFechaRegistro(editTextFechaCreacion.getText().toString());
        EditText editTextDireccionGps = findViewById(R.id.editTextDireccionGpsEdit);
        casoCovid.setDireccionGPS(editTextDireccionGps.getText().toString());
        final TextView textViewFoto = findViewById(R.id.textViewFoto);

        databaseReference.child("CasosCovid/"+casoCovid.getPkCaso()).setValue(casoCovid)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("infoApp","GUARDADO EXITOSO EN TU DATABASE");
                        if(uri != null){
                            if(textViewFoto.getVisibility()==View.VISIBLE){
                                subirArchivoConPutFileEdit(textViewFoto.getText().toString());
                            }else{
                                subirArchivoConPutFileEdit(casoCovid.getNombreFoto());
                            }
                        }else{
                            Intent intent = new Intent(EditarCasoDoctor.this, PagPrincipalDoctor.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(EditarCasoDoctor.this, "Dispositivo editado exitosamente", Toast.LENGTH_SHORT).show();
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
    public void subirArchivoConPutFileEdit( String fileName) {
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

            UploadTask task = storageReference.child(casoCovid.getPkCaso()+"/"+casoCovid.getNombreFoto()).putFile(uri, storageMetadata);


            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("infoApp", "subida exitosa");
                    Intent intent = new Intent(EditarCasoDoctor.this, PagPrincipalDoctor.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(EditarCasoDoctor.this, "Dispositivo editado exitosamente", Toast.LENGTH_SHORT).show();
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("infoApp", "error en la subida");
                    Toast.makeText(EditarCasoDoctor.this, "Error Al subir, vuelva a intentar", Toast.LENGTH_SHORT).show();
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
                                Intent intent = new Intent(EditarCasoDoctor.this, PagPrincipalDoctor.class);
                                startActivity(intent);
                                return true;
                            case R.id.verResumenDoctor:

                                return true;
                            case R.id.crearCasoDoctor:
                                Intent intent2 = new Intent(EditarCasoDoctor.this, CrearCasoDoctor.class);
                                startActivity(intent2);
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
                Intent intent = new Intent(EditarCasoDoctor.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}