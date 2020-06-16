package com.example.galeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.galeria.Login.View.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//comentario
public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private TextView nombreDePerfil;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        myRef =  FirebaseDatabase.getInstance().getReference();

        nombreDePerfil = (TextView)findViewById(R.id.perfil_usuario);

        getUserInfo();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                return (true);

            case R.id.logout:
               mAuth.signOut();
                Intent inicio = getIntent();
                finish();
                startActivity(inicio);
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    public void getUserInfo(){


        if(usuarioLogeado()){
            String id = mAuth.getCurrentUser().getUid();
            myRef.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String nombre = dataSnapshot.child("nombre").getValue().toString();
                        String apellido = dataSnapshot.child("apellido").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

                        setNombreDePerfil(nombre + " " +apellido +"\n"+email);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public boolean usuarioLogeado(){
        return mAuth.getCurrentUser() != null;
    }

    public void setNombreDePerfil(String _nombreDePerfil){
        nombreDePerfil.setText(_nombreDePerfil);
    }

}