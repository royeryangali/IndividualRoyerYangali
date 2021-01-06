package pe.edu.pucp.individualroyeryangali.Medico;

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
import pe.edu.pucp.individualroyeryangali.R;
import pe.edu.pucp.individualroyeryangali.RecyclerAdapters.CasosCovidAdapterDoctor;
import pe.edu.pucp.individualroyeryangali.VerResumen;

public class PagPrincipalDoctor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_principal_doctor);
        setTitle("Página principal Doctor");
        listarCasos();

    }


    public void crearCita() {

    }

    public void crearCaso(View view) {
        Intent intent = new Intent(PagPrincipalDoctor.this, CrearCasoDoctor.class);
        startActivity(intent);
    }

    public void listarCitas() {

    }

    public void listarCasos() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("CasosCovid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CasoCovid> casoCovidArrayList = new ArrayList<>();
                for (DataSnapshot children : snapshot.getChildren()) {
                    CasoCovid casoCovid = children.getValue(CasoCovid.class);
                    casoCovidArrayList.add(casoCovid);
                }
                if (!casoCovidArrayList.isEmpty()) {
                    TextView message = findViewById(R.id.textViewMessageDispo);
                    if (message.getVisibility() == View.VISIBLE) {
                        message.setVisibility(View.INVISIBLE);
                    }
                    CasosCovidAdapterDoctor adapter = new CasosCovidAdapterDoctor(casoCovidArrayList, PagPrincipalDoctor.this);
                    RecyclerView recyclerView = findViewById(R.id.casosDoctorRv);
                    if (recyclerView.getVisibility() == View.INVISIBLE) {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PagPrincipalDoctor.this));
                } else {
                    TextView message = findViewById(R.id.textViewMessageDispo);
                    message.setVisibility(View.VISIBLE);
                    RecyclerView recyclerView = findViewById(R.id.casosDoctorRv);
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
                                return true;
                            case R.id.verResumenDoctor:
                                Intent intent1 = new Intent(PagPrincipalDoctor.this, VerResumen.class);
                                startActivity(intent1);
                                return true;
                            case R.id.crearCasoDoctor:
                                Intent intent2 = new Intent(PagPrincipalDoctor.this, CrearCasoDoctor.class);
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
                Intent intent = new Intent(PagPrincipalDoctor.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}