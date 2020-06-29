package com.example.findyourstyle.Adampters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.Modelo.ProductoTienda;
import com.example.findyourstyle.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterProductosTienda extends RecyclerView.Adapter<AdapterProductosTienda.productosTiendaHolder> implements View.OnClickListener{
    //NDCJ
    List<ProductoTienda> lista;
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

    public  AdapterProductosTienda(List<ProductoTienda> lista, Context context){
        this.lista = lista;
        this.context = context;
        request = Volley.newRequestQueue(context);
    }
    @NonNull
    @Override
    public AdapterProductosTienda.productosTiendaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_productos,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);

        vista.setOnClickListener(this);
        return new AdapterProductosTienda.productosTiendaHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProductosTienda.productosTiendaHolder holder, int position) {
        holder.nombreProductoTienda.setText(lista.get(position).getNombreProducto());
        holder.nombreMiTienda.setText(lista.get(position).getTienda());
        holder.direccionTienda.setText(lista.get(position).getDireccion());
        holder.precioProducto.setText(lista.get(position).getPrecio());

        if(lista.get(position).getRutaImagen()!=null){
            //holder.imagen.setImageBitmap(listaProducto.get(position).getImagen());
            cargarImagenServidor(lista.get(position).getRutaImagen(), holder);
        }else{
            holder.imagenProductotienda.setImageResource(R.drawable.ic_launcher_background);
        }


    }

    private void cargarImagenServidor(String rutaImagen, final AdapterProductosTienda.productosTiendaHolder holder){
        String ip=context.getString(R.string.ip);
        String url = ip+ "/findyourstyleBDR/" + rutaImagen;
        url = url.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imagenProductotienda.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return lista.size();
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

    public class productosTiendaHolder extends RecyclerView.ViewHolder {
        TextView nombreMiTienda, nombreProductoTienda, direccionTienda, precioProducto;
        ImageView imagenProductotienda, imgNuevasHoras;
        public productosTiendaHolder(@NonNull View itemView) {
            super(itemView);
            nombreProductoTienda =  itemView.findViewById(R.id.txtNombrePrductosTienda);
            nombreMiTienda =  itemView.findViewById(R.id.txtTiendaProductosTienda);
            direccionTienda = itemView.findViewById(R.id.txtDireccionProductosTienda);
            precioProducto =  itemView.findViewById(R.id.txtPrecpProductosTienda);
            imagenProductotienda = itemView.findViewById(R.id.imgProductosTienda);

            imgNuevasHoras = itemView.findViewById(R.id.imgHorasProductosTienda);
        }
    }
}
