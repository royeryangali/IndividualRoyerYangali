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

import pe.edu.pucp.individualroyeryangali.Entity.CasoCovid;
import pe.edu.pucp.individualroyeryangali.Medico.EditarCasoDoctor;
import pe.edu.pucp.individualroyeryangali.R;

public class CasosCovidAdapterDoctor extends RecyclerView.Adapter<CasosCovidAdapterDoctor.CasosCovidViewHolder>{
    private ArrayList<CasoCovid> listaDeCasosCovid;
    private Context context;

    public CasosCovidAdapterDoctor(ArrayList<CasoCovid> listaDeCasosCovid, Context context) {
        this.listaDeCasosCovid = listaDeCasosCovid;
        this.context = context;
    }

    @NonNull
    @Override
    public CasosCovidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.casos_covid_rv_doctor,parent,false);
        CasosCovidViewHolder casosCovidViewHolder = new CasosCovidViewHolder(itemView);
        return casosCovidViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CasosCovidViewHolder holder, int position) {
        final CasoCovid casoCovid = listaDeCasosCovid.get(position);
        StorageReference reference =
                FirebaseStorage.getInstance().getReference().child(casoCovid.getPkCaso()+"/"+casoCovid.getNombreFoto());
        Glide.with(context).load(reference).into(holder.imagen);
        holder.zona.setText("Zona: "+casoCovid.getZonaLimena()+" - Fecha Registro: "+casoCovid.getFechaRegistro());
        holder.sintoma.setText("Síntomas: "+ casoCovid.getSintomas());
        holder.direccionGPS.setText("Dirección: "+ casoCovid.getDireccionGPS());
        holder.usuarioQueRegistra.setText("Registrado por: "+ casoCovid.getUsuarioQueRegistra().getNombreUsuario());
        holder.estado.setText("Estado: " + casoCovid.getEstado());
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditarCasoDoctor.class);
                intent.putExtra("casoCovid", casoCovid);
                context.startActivity(intent);
            }
        });
        holder.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("CasosCovid/"+casoCovid.getPkCaso()).setValue(null)
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
        return listaDeCasosCovid.size();
    }

    public static  class CasosCovidViewHolder extends RecyclerView.ViewHolder{
        TextView zona;
        TextView sintoma;
        TextView direccionGPS;
        TextView usuarioQueRegistra;
        TextView estado;
        ImageView imagen;
        Button borrar;
        Button editar;
        public CasosCovidViewHolder(@NonNull View itemView) {
            super(itemView);
            zona = itemView.findViewById(R.id.textViewTipoMode);
            sintoma = itemView.findViewById(R.id.textViewCaracteristica);
            direccionGPS = itemView.findViewById(R.id.textViewStock);
            usuarioQueRegistra = itemView.findViewById(R.id.textViewIncluye);
            imagen = itemView.findViewById(R.id.imageViewDevice);
            borrar = itemView.findViewById(R.id.buttonborrar);
            editar = itemView.findViewById(R.id.buttonEditar);
            estado= itemView.findViewById(R.id.textViewEstado);
        }
    }
}
