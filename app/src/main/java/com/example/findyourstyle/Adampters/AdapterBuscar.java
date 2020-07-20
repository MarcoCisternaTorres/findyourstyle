package com.example.findyourstyle.Adampters;

import android.content.Context;
import android.graphics.Bitmap;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterBuscar extends RecyclerView.Adapter<AdapterBuscar.ViewHolder> implements View.OnClickListener{

    List<ModeloBuscar> listaProducto;
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

    public  AdapterBuscar(List<ModeloBuscar> listaProducto, Context context){
        this.listaProducto = listaProducto;
        this.context = context;
        request = Volley.newRequestQueue(context);
    }
    @NonNull
    @Override
    public AdapterBuscar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_buscar,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new AdapterBuscar.ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBuscar.ViewHolder holderBuscar, int position) {
        holderBuscar.nombreProducto.setText(listaProducto.get(position).getNombreProducto());
        holderBuscar.nombreTienda.setText(listaProducto.get(position).getTienda());
        holderBuscar.direccion.setText(listaProducto.get(position).getDireccion());
        holderBuscar.precio.setText(listaProducto.get(position).getPrecio());

        if(listaProducto.get(position).getRutaImagen()!=null){
            //holder.imagen.setImageBitmap(listaProducto.get(position).getImagen());
            cargarImagenServidor(listaProducto.get(position).getRutaImagen(), holderBuscar);
        }else{
            holderBuscar.imagen.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private void cargarImagenServidor(String rutaImagen, final AdapterBuscar.ViewHolder holder){
        String ip=context.getString(R.string.ip);
        String url = ip+ "/findyourstyleBDR/" + rutaImagen;
        url = url.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imagen.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listaProducto.size();
    }

    public void  setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTienda, nombreProducto, direccion, precio;
        ImageView imagen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto =  itemView.findViewById(R.id.txt_card_view_nombre_producto);
            nombreTienda =  itemView.findViewById(R.id.txt_card_view_tienda);
            direccion = itemView.findViewById(R.id.txt_card_view_direccion);
            precio =  itemView.findViewById(R.id.txt_card_view_precio);
            imagen = itemView.findViewById(R.id.img_card_view_buscar);
        }
    }
}
