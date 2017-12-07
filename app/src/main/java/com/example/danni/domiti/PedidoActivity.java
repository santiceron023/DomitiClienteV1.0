package com.example.danni.domiti;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class PedidoActivity extends AppCompatActivity {
    private ListView listaProductos;
    private ArrayList<Pedidos_DataBase> productos;
    private String tiendaId,tiendaNombre,clienteId,numeroPedido;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Intent intent;
    DatabaseReference myRef1;
    private StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);


        listaProductos = (ListView) findViewById(R.id.listaPedido);

        //desde el main
        intent = new Intent(getApplicationContext(),MainActivity.class);
        Bundle extras = getIntent().getExtras();
        tiendaId = extras.getString("TiendaId");
        tiendaNombre = extras.getString("TiendaNombre");
        numeroPedido = extras.getString("NumeroPedido");

        setTitle(tiendaNombre);
        ActionBar ab =getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        SharedPreferences sharedPrefs = getSharedPreferences("DomitiPreferences",MODE_PRIVATE);
        clienteId = sharedPrefs.getString("Celular","no cargo celular");

        //list aproductos
        productos = new ArrayList<Pedidos_DataBase>();
        final Adapter adapter = new Adapter(getApplicationContext(), productos);
        listaProductos.setAdapter(adapter);

        //database
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Pedidos_Cliente").child(clienteId).
                child(tiendaId).child(numeroPedido);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //padre:productos
                for(DataSnapshot padre : dataSnapshot.getChildren()){
                    productos.add(padre.getValue(Pedidos_DataBase.class));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    class Adapter extends ArrayAdapter<Pedidos_DataBase> {
        public Adapter(Context context, ArrayList<Pedidos_DataBase> pedidos) {
            super(context, R.layout.lista_pedidos_larga,pedidos);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater= LayoutInflater.from(getContext());
            View item =inflater.inflate(R.layout.lista_pedidos_larga,null);

            Pedidos_DataBase pedido = getItem(position);

            TextView tNombre=(TextView)item.findViewById(R.id.tCostoTotal);
            tNombre.setText(pedido.getTotal());
            TextView tNegocio=(TextView)item.findViewById(R.id.tNombre);
            tNegocio.setText(pedido.getNombreProd());
            TextView tTiempoEnvio=(TextView)item.findViewById(R.id.tCantidad);
            tTiempoEnvio.setText(pedido.getCantidad());

            ImageView imagenNegocio = (ImageView)item.findViewById(R.id.imagenNegocio);

            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(pedido.getFotoProducto());
            Glide.with(item.getContext())
                   .using(new FirebaseImageLoader())
                    .load(storageRef)
                    .into(imagenNegocio);
            return item;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void bBorrar(View view){
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Pedidos_Cliente").child(clienteId).
                child(tiendaId).child(numeroPedido);
        myRef.removeValue();
        myRef = database.getReference("Pedidos_Tienda").child(tiendaId).
                child(clienteId).child(numeroPedido);
        myRef.removeValue();
        startActivity(intent);
        finish();

    }
    public void bOk(View view){
        startActivity(intent);
        finish();
    }

}
