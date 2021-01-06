package pe.edu.pucp.individualroyeryangali.Enfermero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.edu.pucp.individualroyeryangali.Entity.CasoCovid;
import pe.edu.pucp.individualroyeryangali.MainActivity;
import pe.edu.pucp.individualroyeryangali.Medico.PagPrincipalDoctor;
import pe.edu.pucp.individualroyeryangali.R;
import pe.edu.pucp.individualroyeryangali.RecyclerAdapters.CasosCovidAdapterEnfermero;
import pe.edu.pucp.individualroyeryangali.VerResumen;

public class PagPrincipalEnfermero extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_principal_enfermero);
        setTitle("Enfermero - ReportApp");
        verCasosPendientes();
    }


    public void verCasosPendientes() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("CasosCovid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CasoCovid> casoCovidArrayList = new ArrayList<>();
                for (DataSnapshot children : snapshot.getChildren()) {
                    CasoCovid casoCovid = children.getValue(CasoCovid.class);
                    if (casoCovid.getEstado().equalsIgnoreCase("Pendiente")) {
                        casoCovidArrayList.add(casoCovid);
                    }
                }
                if (!casoCovidArrayList.isEmpty()) {
                    TextView message = findViewById(R.id.textViewMessageDispo);
                    if (message.getVisibility() == View.VISIBLE) {
                        message.setVisibility(View.INVISIBLE);
                    }
                    CasosCovidAdapterEnfermero adapter = new CasosCovidAdapterEnfermero(casoCovidArrayList, PagPrincipalEnfermero.this);
                    RecyclerView recyclerView = findViewById(R.id.casosEnfermeroRv);
                    if (recyclerView.getVisibility() == View.INVISIBLE) {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PagPrincipalEnfermero.this));
                } else {
                    TextView message = findViewById(R.id.textViewMessageDispo);
                    message.setVisibility(View.VISIBLE);
                    RecyclerView recyclerView = findViewById(R.id.casosEnfermeroRv);
                    if (recyclerView.getVisibility() == View.VISIBLE) {
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void vacunar() {

    }
////para relacionar el layout de menú con esta vista

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_enfermero, menu);
        return true;
    }

    ///para linkear las opciones del menú con una acción en particular de forma centralizada ///también puede realizarse desde el primer método onCreate pero de otra manera, revisar min 01:18:43 del video zoom

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.abrirMenuEnfermero:
                View view = findViewById(R.id.abrirMenuEnfermero);
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_enfermero, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.verListaCasosEnfermero:
                                return true;
                            case R.id.verResumenEnfermero:
                                Intent intent = new Intent(PagPrincipalEnfermero.this, VerResumen.class);
                                startActivity(intent);
                                return true;
                            case R.id.cerrarSesionEnfermero:
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
                Intent intent = new Intent(PagPrincipalEnfermero.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}