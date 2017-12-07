package com.example.danni.domiti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView NDname, NDcelular;
    private CircleImageView NDfoto;
    private NuevoUsuario infoUsuario;
    private StorageReference storageReferenceMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        SharedPreferences sharedPrefs = getSharedPreferences("DomitiPreferences", this.MODE_PRIVATE);
        SharedPreferences.Editor editorSP= sharedPrefs.edit();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String Celular = user.getDisplayName();
            editorSP.putString("Celular",Celular);
            editorSP.commit();
            Toast.makeText(getApplicationContext(),Celular,Toast.LENGTH_SHORT).show();

            //INFORMACION DE PERFIL EN EL NAVDRAWER
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Usuarios").child(Celular);

            infoUsuario=new NuevoUsuario();
            NDfoto=(CircleImageView)headerView.findViewById(R.id.NDfoto);
            NDname=(TextView)headerView.findViewById(R.id.NDname);
            NDcelular=(TextView)headerView.findViewById(R.id.NDcelular);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    infoUsuario=dataSnapshot.getValue(NuevoUsuario.class);

                    NDname.setText(infoUsuario.getNombre());
                    NDcelular.setText(infoUsuario.getCelular());

                    storageReferenceMain = FirebaseStorage.getInstance().getReferenceFromUrl(infoUsuario.getFoto());

                    Glide.with(getApplicationContext())
                            .using(new FirebaseImageLoader())
                            .load(storageReferenceMain)
                            .into(NDfoto);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{

        }

        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager =getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor,new TabsFragment()).commit();






    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager =getSupportFragmentManager();

        if (id == R.id.inicio) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new TabsFragment()).commit();
        } else if (id == R.id.perfil) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new PerfilFragment()).commit();
        } else if (id == R.id.ubicacion) {
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);

        }  else if (id == R.id.info_domiti) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new AcercaFragment()).commit();
        } else if (id == R.id.cerrar_sesion) {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sharedPrefs = getSharedPreferences("DomitiPreferences", this.MODE_PRIVATE);
            SharedPreferences.Editor editorSP= sharedPrefs.edit();
            editorSP.putInt("optLog",0);
            editorSP.commit();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
