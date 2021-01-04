package pe.edu.pucp.individualroyeryangali.Medico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import pe.edu.pucp.individualroyeryangali.R;

public class PagPrincipalDoctor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_principal_medico);
        setTitle("Página principal Médico");
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

    }

}