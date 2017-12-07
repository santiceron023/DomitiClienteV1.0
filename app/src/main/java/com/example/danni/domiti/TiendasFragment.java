package com.example.danni.domiti;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TiendasFragment extends Fragment {
    private ListView listaTiendas;
    private ArrayList<Tienda> tiendas;
    private String Celular;
    private StorageReference storageRef;


    public TiendasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tiendas, container, false);

        listaTiendas=(ListView)view.findViewById(R.id.listaTiendas);
        tiendas=new ArrayList<Tienda>();
        final Adapter adapter = new Adapter(getActivity(),tiendas);
        listaTiendas.setAdapter(adapter);
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("DomitiPreferences", getActivity().MODE_PRIVATE);
        Celular = sharedPrefs.getString("Celular", "no_cargo_celular");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tiendas");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot tiendasSnapshot:dataSnapshot.getChildren()){
                    tiendas.add(tiendasSnapshot.getValue(Tienda.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listaTiendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String nombre = tiendas.get(position).getNombre();
                String tiendId = tiendas.get(position).getCelular();
                String fotoTienda = tiendas.get(position).getFoto();

                Intent intent = new Intent(getActivity(),ProductosActivity.class);
                //pasar el celular
                intent.putExtra("TiendaId",tiendId);
                intent.putExtra("TiendaNombre",nombre);
                intent.putExtra("TiendaFoto",fotoTienda);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }
    class Adapter extends ArrayAdapter<Tienda> {

        public Adapter(Context context, ArrayList<Tienda> tiendas) {
            super(context, R.layout.lista_tiendas, tiendas);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.lista_tiendas,null);
            Tienda tienda = getItem(position);

            TextView tNombre = (TextView)item.findViewById(R.id.tNombre);
            tNombre.setText(tienda.getNombre());

            TextView tNegocio = (TextView)item.findViewById(R.id.tNegocio);
            tNegocio.setText(tienda.getTipo());

            TextView tTiempoEnvio =(TextView)item.findViewById(R.id.tTiempoEnvio);
            tTiempoEnvio.setText(tienda.getTiempoEnvio());

            TextView tPedidoMin =(TextView)item.findViewById(R.id.tPedidoMin);
            tPedidoMin.setText(tienda.getPedidoMin());

            TextView tCostoEnvio = (TextView)item.findViewById(R.id.tCostoEnvio);
            tCostoEnvio.setText(tienda.getCostoEnvio());

            ImageView imagenNegocio=(ImageView)item.findViewById(R.id.imagenNegocio);


            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(tienda.getFoto());

            Glide.with(item.getContext())
                    .using(new FirebaseImageLoader())
                    .load(storageRef)
                    .into(imagenNegocio);
            return item;
        }


    }
}
