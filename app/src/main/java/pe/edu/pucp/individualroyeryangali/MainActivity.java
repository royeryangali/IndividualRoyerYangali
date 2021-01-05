package pe.edu.pucp.individualroyeryangali;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import pe.edu.pucp.individualroyeryangali.Enfermero.PagPrincipalEnfermero;
import pe.edu.pucp.individualroyeryangali.Entity.Usuario;
import pe.edu.pucp.individualroyeryangali.Medico.PagPrincipalDoctor;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void login(View view) {
        List<AuthUI.IdpConfig> proveedores = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.loginpersonalizado).setEmailButtonId(R.id.buttonEmail).setGoogleButtonId(R.id.buttonGmail).build();

        Intent intent = AuthUI.getInstance().createSignInIntentBuilder().setAuthMethodPickerLayout(authMethodPickerLayout).setAvailableProviders(proveedores).build(); ///con .setTheme() se puede cambiar el tema .setLogo(R.drawable.logoindividual)
        startActivityForResult(intent, 1);
    }


    public void logOut(View view) {
        AuthUI instance = AuthUI.getInstance();
        instance.signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Lógica de cerrao de sesión lo pongo aquí porque luego lo ecesitaremos cuando acabemos el menú de cliente y TI
                //Intent intent = new Intent(MainActivity.this, MainActivity.class);
                // startActivity(intent);
                //finish();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            validarUsuario();
        }

    }

    public void validarUsuario() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.setLanguageCode("es-419");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (firebaseUser.isEmailVerified()) {
                        validarRegistro();
                    } else {
                        Toast.makeText(MainActivity.this, "Se le ha enviado un correo para verificar su cuenta", Toast.LENGTH_SHORT).show();
                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("emailVer", "Correo enviado");
                            }
                        });
                    }
                }
            });
        }
    }

    public void validarRegistro() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("infoApp", firebaseUser.getUid());
        databaseReference.child("users/" + firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Usuario user = snapshot.getValue(Usuario.class);
                    Log.d("infoApp", "ENCONTRADO");
                    if (user != null) {
                        if (user.getRol().equalsIgnoreCase("Doctor")) {
                            Toast.makeText(MainActivity.this, "Bienvenido doctor " + user.getNombreUsuario(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, PagPrincipalDoctor.class);
                            startActivity(intent);
                            finish();
                        }else if(user.getRol().equalsIgnoreCase("Enfermero")){
                            Toast.makeText(MainActivity.this, "Bienvenido enfermero " + user.getNombreUsuario(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, PagPrincipalEnfermero.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {
                    Log.d("infoApp", "BÚSQUEDA DE USUARIO FALLIDA.");
                    Intent intent = new Intent(MainActivity.this, Registro.class);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}