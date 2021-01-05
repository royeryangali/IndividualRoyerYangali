package pe.edu.pucp.individualroyeryangali.Enfermero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.edu.pucp.individualroyeryangali.Entity.CasoCovid;
import pe.edu.pucp.individualroyeryangali.R;
import pe.edu.pucp.individualroyeryangali.RecyclerAdapters.CasosCovidAdapterEnfermero;

public class PagPrincipalEnfermero extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_principal_enfermero);
        setTitle("Página principal Enfermero");
verCasosPendientes();
    }


    public void verCasosPendientes(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("CasosCovid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CasoCovid> casoCovidArrayList = new ArrayList<>();
                for(DataSnapshot children : snapshot.getChildren()){
                    CasoCovid casoCovid = children.getValue(CasoCovid.class);
                    if(casoCovid.getEstado().equalsIgnoreCase("Pendiente")){
                        casoCovidArrayList.add(casoCovid);
                    }
                }
                if(!casoCovidArrayList.isEmpty()){
                    TextView message = findViewById(R.id.textViewMessageDispo);
                    if(message.getVisibility()== View.VISIBLE){
                        message.setVisibility(View.INVISIBLE);
                    }
                    CasosCovidAdapterEnfermero adapter = new CasosCovidAdapterEnfermero(casoCovidArrayList, PagPrincipalEnfermero.this);
                    RecyclerView recyclerView = findViewById(R.id.casosEnfermeroRv);
                    if(recyclerView.getVisibility()==View.INVISIBLE){
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PagPrincipalEnfermero.this));
                }else{
                    TextView message = findViewById(R.id.textViewMessageDispo);
                    message.setVisibility(View.VISIBLE);
                    RecyclerView recyclerView = findViewById(R.id.casosEnfermeroRv);
                    if(recyclerView.getVisibility()==View.VISIBLE){
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void vacunar(){

    }

}