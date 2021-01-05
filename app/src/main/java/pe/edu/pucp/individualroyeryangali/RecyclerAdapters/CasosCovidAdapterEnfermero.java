package pe.edu.pucp.individualroyeryangali.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import pe.edu.pucp.individualroyeryangali.Enfermero.VacunarCasoEnfermero;
import pe.edu.pucp.individualroyeryangali.Entity.CasoCovid;
import pe.edu.pucp.individualroyeryangali.R;

public class CasosCovidAdapterEnfermero extends RecyclerView.Adapter<CasosCovidAdapterEnfermero.CasosCovidViewHolderEnfermero>{
    private ArrayList<CasoCovid> listaDeCasosCovidEnfermero;
    private Context context;

    public CasosCovidAdapterEnfermero(ArrayList<CasoCovid> listaDeCasosCovidEnfermero, Context context) {
        this.listaDeCasosCovidEnfermero = listaDeCasosCovidEnfermero;
        this.context = context;
    }

    @NonNull
    @Override
    public CasosCovidViewHolderEnfermero onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.casos_covid_rv_enfermero,parent,false);
        CasosCovidViewHolderEnfermero casosCovidViewHolderEnfermero = new CasosCovidViewHolderEnfermero(itemView);
        return casosCovidViewHolderEnfermero;
    }

    @Override
    public void onBindViewHolder(@NonNull CasosCovidViewHolderEnfermero holder, int position) {
        final CasoCovid casoCovidEnfermero = listaDeCasosCovidEnfermero.get(position);
        StorageReference reference =
                FirebaseStorage.getInstance().getReference().child(casoCovidEnfermero.getPkCaso()+"/"+casoCovidEnfermero.getNombreFoto());
        Glide.with(context).load(reference).into(holder.imagen);
        holder.zona.setText("Zona: "+casoCovidEnfermero.getZonaLimena()+" - Fecha Registro: "+casoCovidEnfermero.getFechaRegistro());
        holder.sintoma.setText("Síntomas: "+ casoCovidEnfermero.getSintomas());
        holder.direccionGPS.setText("Dirección: "+ casoCovidEnfermero.getDireccionGPS());
        holder.usuarioQueRegistra.setText("Registrado por: "+ casoCovidEnfermero.getUsuarioQueRegistra().getNombreUsuario());
        holder.estado.setText("Estado: " + casoCovidEnfermero.getEstado());
        holder.nombrePaciente.setText("Paciente: " + casoCovidEnfermero.getNombrePaciente());
        holder.dniPaciente.setText("DNI: "+casoCovidEnfermero.getDniPaciente());
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VacunarCasoEnfermero.class);
                intent.putExtra("casoCovidEnfermero", casoCovidEnfermero);
                context.startActivity(intent);
            }
        });
        holder.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("CasosCovid/"+casoCovidEnfermero.getPkCaso()).setValue(null)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("infoApp","Se ha borrado de la databaseee");
                                Toast.makeText(context, "Caso borrado exitosamente", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });

            }
        });

    }

    @Override
    public int getItemCount() {
        return listaDeCasosCovidEnfermero.size();
    }

    public static  class CasosCovidViewHolderEnfermero extends RecyclerView.ViewHolder{
        TextView zona;
        TextView sintoma;
        TextView direccionGPS;
        TextView usuarioQueRegistra;
        TextView estado;
        ImageView imagen;
        TextView nombrePaciente;
        TextView dniPaciente;
        Button borrar;
        Button editar;
        public CasosCovidViewHolderEnfermero(@NonNull View itemView) {
            super(itemView);
            zona = itemView.findViewById(R.id.textViewTipoModeEnfermero);
            sintoma = itemView.findViewById(R.id.textViewCaracteristicaEnfermero);
            direccionGPS = itemView.findViewById(R.id.textViewStockEnfermero);
            usuarioQueRegistra = itemView.findViewById(R.id.textViewIncluyeEnfermero);
            imagen = itemView.findViewById(R.id.imageViewDevice);
            borrar = itemView.findViewById(R.id.buttonborrarEnfermero);
            editar = itemView.findViewById(R.id.buttonEditarEnfermero);
            estado= itemView.findViewById(R.id.textViewEstadoEnfermero);
            nombrePaciente= itemView.findViewById(R.id.textViewnombrePaciente);
            dniPaciente = itemView.findViewById(R.id.textViewdniPaciente);
        }
    }
}
