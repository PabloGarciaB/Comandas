package com.example.comandas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.comandas.Models.Productos;
import com.example.comandas.R;

import java.util.ArrayList;

public class ListViewProductosAdapter extends BaseAdapter {
    Context context;
    ArrayList<Productos> productosData;
    LayoutInflater layoutInflater;
    Productos productosModel;

    public ListViewProductosAdapter(Context context, ArrayList<Productos> productosData) {
        this.context = context;
        this.productosData = productosData;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return productosData.size();
    }

    @Override
    public Object getItem(int position) {
        return productosData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView==null){
            rowView = layoutInflater.inflate(R.layout.lista_productos,
                    null,
                    true );
        }
        //Enlazar vistas
        TextView nombres_P = rowView.findViewById(R.id.art_nombre);
        TextView unidades_P = rowView.findViewById(R.id.art_unidades);
        TextView precio_P = rowView.findViewById(R.id.art_precio);
        TextView fechaRegistro_P = rowView.findViewById(R.id.fecha_registro_art);

        productosModel = productosData.get(position);

        nombres_P.setText(productosModel.getNombreProducto());
        unidades_P.setText(productosModel.getNumUnidades());
        precio_P.setText(productosModel.getPrecioProducto());
        fechaRegistro_P.setText(productosModel.getFechaRegistro());

        return rowView;
    }
}
