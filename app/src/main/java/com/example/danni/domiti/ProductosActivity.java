package com.example.danni.domiti;

import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProductosActivity extends AppCompatActivity {
    private ListView listaProductos;
    private ArrayList<NuevoProducto> productos;
    String tiendaId,tiendaNombre,clienteId, tiendaFoto;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef1,myRef2,myRef3;
    String numPedidos = "0";
    int[] ProductosArray = new int[20]; ///numero maximo de prod en pedido

    private StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        //desde el main
        Bundle extras = getIntent().getExtras();
        tiendaId = extras.getString("TiendaId");
        tiendaNombre = extras.getString("TiendaNombre");
        tiendaFoto= extras.getString("TiendaFoto");
        setTitle(tiendaNombre);
        ActionBar ab =getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        listaProductos = (ListView) findViewById(R.id.listaProductos);




        //preferencias compartidas
        SharedPreferences sharedPrefs = getSharedPreferences("DomitiPreferences", this.MODE_PRIVATE);
        numPedidos = sharedPrefs.getString("numPedidos", "1");
        clienteId = sharedPrefs.getString("Celular","no se cargo celular");

        //list aproductos
        productos = new ArrayList<NuevoProducto>();
        final Adapter adapter = new Adapter(getApplicationContext(), productos);
        listaProductos.setAdapter(adapter);


        myRef1 = database.getReference("Productos").child(tiendaId);
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    productos.add(datasnapshot.getValue(NuevoProducto.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    class Adapter extends ArrayAdapter<NuevoProducto> {
        public Adapter(Context context, ArrayList<NuevoProducto> productos) {
            super(context, R.layout.lista_productos, productos);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView,
                            @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.lista_productos, null);

            final NuevoProducto productos = getItem(position);

            TextView tNombre = (TextView) item.findViewById(R.id.tNombreProducto);
            tNombre.setText(productos.getNombre());
            TextView tNegocio = (TextView) item.findViewById(R.id.tCantidad);
            tNegocio.setText(productos.getTamaño());
            TextView tTiempoEnvio = (TextView) item.findViewById(R.id.tMarca);
            tTiempoEnvio.setText(productos.getMarca());
            TextView tPedidoMin = (TextView) item.findViewById(R.id.tPrecio);
            tPedidoMin.setText(productos.getPrecio());

            ImageView imProducto = (ImageView)item.findViewById(R.id.imProducto);
            ImageView add = (ImageView) item.findViewById(R.id.bAddProduct);

            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(productos.getFoto());

            Glide.with(item.getContext())
                    .using(new FirebaseImageLoader())
                    .load(storageRef)
                    .into(imProducto);


            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),"Producto Agregado", Toast.LENGTH_SHORT).show();
                      ProductosArray[position] += 1;
                }
            });
            return item;
        }

    }
    public void ok(View view) {

        //auemntar pedidos
        Integer aux_cont = Integer.valueOf(numPedidos);
        aux_cont++;
        numPedidos = String.valueOf(aux_cont);

        //Toast.makeText(getApplicationContext(), String.valueOf(ProductosArray[1]), Toast.LENGTH_SHORT).show();
        for (int factor = 0; factor < 20; factor++) {
            //lee la cantida en el arreglo
            if (ProductosArray[factor] > 0) {
                funcion(factor,ProductosArray[factor]);
                funcion2(factor,ProductosArray[factor]);
            }
        }



        SharedPreferences sharedPrefs = getSharedPreferences("DomitiPreferences", this.MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sharedPrefs.edit();
        editorSP.putString("numPedidos",numPedidos);
        editorSP.commit();
        finish();
    }
    private void funcion(final int pos,final int cantidad){
        String Nombre = productos.get(pos).getNombre();
        String precio = productos.get(pos).getPrecio();
        String fotoProducto = productos.get(pos).getFoto();
        int total = Integer.valueOf(precio);
        total *= cantidad;
        // tiendaNombre
        Pedidos_DataBase pedido_database = new Pedidos_DataBase(String.valueOf(cantidad),
                String.valueOf(total),Nombre,tiendaNombre,numPedidos,tiendaId,tiendaFoto,fotoProducto);
        myRef2 = database.getReference("Pedidos_Cliente").child(clienteId).child(tiendaId).
                child(numPedidos).child(Nombre);
        myRef2.setValue(pedido_database);


    }
    private void funcion2(final int pos,final int cantidad){
        String Nombre = productos.get(pos).getNombre();
        String Precio = productos.get(pos).getPrecio();
        int total = Integer.valueOf(Precio)*cantidad;

        myRef3 = database.getReference("Pedidos_Tienda").child(tiendaId).child(clienteId).child(numPedidos).child(Nombre);
        myRef3.child("Nombre").setValue(Nombre);
        myRef3.child("Cantidad").setValue(Integer.toString(cantidad));
        myRef3.child("Total").setValue(Integer.toString(total));
        myRef3.child("Pedido").setValue(numPedidos);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
