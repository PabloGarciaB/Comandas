package com.example.comandas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.comandas.Adaptadores.ListViewProductosAdapter;
import com.example.comandas.Models.Productos;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class ActivityInventario extends AppCompatActivity {

    private ArrayList<Productos> listProductos = new ArrayList<Productos>();
    ArrayAdapter<Productos> arrayAdapterProductos;
    ListViewProductosAdapter listViewProductosAdapter;
    LinearLayout linearLayoutEditar;
    ListView listViewProductos;

    EditText inputNombreArt, inputUnidadesArt, inputPrecioArt;
    Button btnCancelar;

    Productos productosSeleccionado;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        inputNombreArt = findViewById(R.id.nom_articulo);
        inputUnidadesArt = findViewById(R.id.num_unidades);
        inputPrecioArt = findViewById(R.id.precio_articulo);

        listViewProductos = findViewById(R.id.listViewProductos);
        linearLayoutEditar =findViewById(R.id.LinearLayoutEditar);

        listViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                productosSeleccionado = (Productos) adapterView.getItemAtPosition(position);
                inputNombreArt.setText(productosSeleccionado.getNombreProducto());
                inputUnidadesArt.setText(productosSeleccionado.getNumUnidades());
                inputPrecioArt.setText(productosSeleccionado.getPrecioProducto());

                //Hacer visible el linearlayout
                linearLayoutEditar.setVisibility(View.VISIBLE);
            }
        });


        /*
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutEditar.setVisibility(View.GONE);
                    productosSeleccionado = null;
            }
        });*/

        inicializarFirebase();
        listarProductos();
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarProductos(){
        databaseReference.child("Productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProductos.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()){
                    Productos p = objSnapshot.getValue(Productos.class);
                    listProductos.add(p);
                }

                //Iniciar nuestro adaptador
                listViewProductosAdapter = new ListViewProductosAdapter(ActivityInventario.this, listProductos);
               /* arrayAdapterProductos = new ArrayAdapter<Productos>(
                        ActivityInventario.this,
                        android.R.layout.simple_list_item_1,
                        listProductos
                );*/

                listViewProductos.setAdapter(listViewProductosAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crud_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String nombreP = inputNombreArt.getText().toString();
        String unidadP = inputUnidadesArt.getText().toString();
        String precioP = inputPrecioArt.getText().toString();

        switch (item.getItemId()){
            case R.id.menu_agregar:
                insertar();
                break;
            case R.id.menu_guardar:
                if (productosSeleccionado!=null) {
                    if (validarInputs() == false) {
                        Productos p = new Productos();
                        p.setIdProductos(productosSeleccionado.getIdProductos());
                        p.setNombreProducto(nombreP);
                        p.setNumUnidades(unidadP);
                        p.setPrecioProducto(precioP);
                        p.setFechaRegistro(productosSeleccionado.getFechaRegistro());
                        p.setTimestamp(productosSeleccionado.getTimestamp());
                        databaseReference.child("Productos").child(p.getIdProductos()).setValue(p);
                        Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();

                        linearLayoutEditar.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(this, "Seleccione un producto", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_eliminar:
                if (productosSeleccionado!=null){
                    Productos p2 = new Productos();
                    p2.setIdProductos(productosSeleccionado.getIdProductos());
                    databaseReference.child("Productos").child(p2.getIdProductos()).removeValue();
                    linearLayoutEditar.setVisibility(View.GONE);
                    Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this, "Seleccione un producto a eliminar", Toast.LENGTH_SHORT).show();
                }
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void insertar(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                ActivityInventario.this
        );

        View mView = getLayoutInflater().inflate(R.layout.insertar_articulo, null);
        Button btnInsertar = (Button) mView.findViewById(R.id.btn_Add);
        final EditText mInputNombreP = (EditText) mView.findViewById(R.id.inputNombreProducto);
        final EditText mInputUnidadP = (EditText) mView.findViewById(R.id.inputNumUnidades);
        final EditText mInputPrecioP = (EditText) mView.findViewById(R.id.inputPrecioProducto);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreP = mInputNombreP.getText().toString();
                String unidadP = mInputUnidadP.getText().toString();
                String precioP = mInputPrecioP.getText().toString();
                if (nombreP.isEmpty() || nombreP.length()<3){
                    showError(mInputNombreP, "Nombre invalido (Min. 3 letras)");
                } else if (unidadP.isEmpty() || unidadP.length() < 0) {
                    showError(mInputUnidadP, "Número de unidad invalido, verifique la cantidad");
                } else if (precioP.isEmpty() || precioP.length() < 0) {
                    showError(mInputPrecioP, "Precio invalido, verifique la cantidad");
                }else {
                    Productos p = new Productos();
                    p.setIdProductos(UUID.randomUUID().toString());
                    p.setNombreProducto(nombreP);
                    p.setNumUnidades(unidadP);
                    p.setPrecioProducto(precioP);
                    p.setFechaRegistro(getFechaNormal(getFechaMilisegundos()));
                    p.setTimestamp(getFechaMilisegundos() * -1);

                    databaseReference.child("Productos").child(p.getIdProductos()).setValue(p);
                    Toast.makeText(ActivityInventario.this,
                            "Producto registrado correctamente",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    public void showError(EditText input, String s){
        input.requestFocus();
        input.setError(s);
    }

    public long  getFechaMilisegundos(){
        Calendar calendar =Calendar.getInstance();
        long tiempounix = calendar.getTimeInMillis();

        return tiempounix;
    }

    public String getFechaNormal(long fechamilisegundos){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha = simpleDateFormat.format(fechamilisegundos);
        return fecha;
    }

    public boolean validarInputs(){
        String nombre_A = inputNombreArt.getText().toString();
        String unidad_A = inputUnidadesArt.getText().toString();
        String precio_A = inputPrecioArt.getText().toString();

        if (nombre_A.isEmpty() || nombre_A.length() < 3){
            showError(inputNombreArt, "Nombre invalido (Min. 3 letras)");
            return true;
        }else if(unidad_A.isEmpty() || unidad_A.length() < 0){
            showError(inputUnidadesArt, "Número de unidad invalido, verifique la cantidad");
            return true;
        } else if (precio_A.isEmpty() || precio_A.length() < 0) {
            showError(inputPrecioArt, "Precio invalido, verifique la cantidad");
            return true;
        }else{
            return false;
        }
    }

}