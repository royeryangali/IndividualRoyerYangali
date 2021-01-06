package pe.edu.pucp.individualroyeryangali;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import pe.edu.pucp.individualroyeryangali.Entity.CasoCovid;
import pe.edu.pucp.individualroyeryangali.Medico.PagPrincipalDoctor;
import pe.edu.pucp.individualroyeryangali.RecyclerAdapters.CasosCovidAdapterDoctor;

public class VerResumen extends AppCompatActivity {

    PieChartView pieChartView;

    int ejeX;
    int ejeY;
    int axisEste;
    int axisOeste;
    int axisNorte;
    int axisSur;
    int axisCentro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_resumen);
        setTitle("Resumen Casos");
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
                    Toast.makeText(VerResumen.this, "Casos en Pie Chart", Toast.LENGTH_SHORT).show();
                    Log.d("infoApp", "total" + casoCovidArrayList.size());
                    for (CasoCovid casitoCovid : casoCovidArrayList) {
                        if (casitoCovid.getEstado().equalsIgnoreCase("Pendiente")) {
                            ejeX = ejeX + 1;
                            Log.d("infoApp", "totalX" + ejeX);
                        } else {
                            ejeY = ejeY + 1;
                            Log.d("infoApp", "totalY" + ejeY);
                        }
                        if (casitoCovid.getZonaLimena().equalsIgnoreCase("Lima Norte")) {
                            axisNorte = axisNorte + 1;
                            Log.d("infoApp", "totalNorte" + axisNorte);
                        }
                        if (casitoCovid.getZonaLimena().equalsIgnoreCase("Lima Sur")) {
                            axisSur = axisSur + 1;
                            Log.d("infoApp", "totalSur" + axisSur);
                        }
                        if (casitoCovid.getZonaLimena().equalsIgnoreCase("Lima Este")) {
                            axisEste = axisEste + 1;
                            Log.d("infoApp", "totalEste" + axisEste);
                        }
                        if (casitoCovid.getZonaLimena().equalsIgnoreCase("Lima Oeste")) {
                            axisOeste = axisOeste + 1;
                            Log.d("infoApp", "totalOeste" + axisOeste);
                        }
                        if (casitoCovid.getZonaLimena().equalsIgnoreCase("Lima Centro")) {
                            axisCentro = axisCentro + 1;
                            Log.d("infoApp", "totalCentro" + axisCentro);
                        }
                    }

                    float extra1 = (float) ejeX;
                    float extra2 = (float) ejeY;
                    float variablenorte = (float) axisNorte;
                    float variablesur = (float) axisSur;
                    float variableeste = (float) axisEste;
                    float variableoeste = (float) axisOeste;
                    float variablecentro = (float) axisCentro;

                    PieChartView pieChartView = findViewById(R.id.chart);

                    pieChartView = findViewById(R.id.chart);

                    List pieData = new ArrayList<>();
                    if (extra1 != 0.0) {
                        pieData.add(new SliceValue(extra1, Color.RED).setLabel("Pendientes: " + ejeX));
                    }
                    if (extra2 != 0.0) {
                        pieData.add(new SliceValue(extra2, Color.GREEN).setLabel("Vacunados: " + ejeY));
                    }
                    int valorSuma = ejeX + ejeY;
                    //pieData.add(new SliceValue(60, Color.MAGENTA).setLabel("Q4: $28"));

                    PieChartData pieChartData = new PieChartData(pieData);
                    pieChartData.setHasLabels(true).setValueLabelTextSize(10);
                    pieChartData.setHasCenterCircle(true).setCenterText1("Total Casos: " + valorSuma).setCenterText1FontSize(14).setCenterText1Color(Color.parseColor("#0097A7"));
                    pieChartView.setPieChartData(pieChartData);

                    PieChartView pieChartView2 = findViewById(R.id.chart);

                    pieChartView2 = findViewById(R.id.chart2);

                    List pieData2 = new ArrayList<>();
                    if (variableeste != 0.0) {
                        pieData2.add(new SliceValue(variableeste, Color.MAGENTA).setLabel("L. Este: " + axisEste));
                    }
                    if (variableoeste != 0.0) {
                        pieData2.add(new SliceValue(variableoeste, Color.CYAN).setLabel("L. Oeste: " + axisOeste));
                    }
                    if (variablesur != 0.0) {
                        pieData2.add(new SliceValue(variablesur, Color.YELLOW).setLabel("L. Sur:: " + axisSur));
                    }
                    if (variablenorte != 0.0) {
                        pieData2.add(new SliceValue(variablenorte, Color.DKGRAY).setLabel("L. Norte: " + axisNorte));
                    }
                    if (variablecentro != 0.0) {
                        pieData2.add(new SliceValue(variablecentro, Color.BLUE).setLabel("L. Centro: " + axisCentro));
                    }
                    int a = (axisCentro + axisOeste + axisEste + axisSur + axisNorte);
                    PieChartData pieChartData2 = new PieChartData(pieData2);
                    pieChartData2.setHasLabels(true).setValueLabelTextSize(10);
                    pieChartData2.setHasCenterCircle(true).setCenterText1("Total Casos: " + a).setCenterText1FontSize(14).setCenterText1Color(Color.parseColor("#0097A7"));
                    pieChartView2.setPieChartData(pieChartData2);


                } else {
                    Toast.makeText(VerResumen.this, "No hay casos", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}