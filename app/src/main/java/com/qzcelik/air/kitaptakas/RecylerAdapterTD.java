package com.qzcelik.air.kitaptakas;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

/**
 * Created by air on 18.02.2018.
 */

public class RecylerAdapterTD extends RecyclerView.Adapter<RecylerAdapterTD.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView asilKitap;
        TextView digerKitap;
        TextView kullanici;
        ImageView kitapResim;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view2);
            asilKitap = (TextView)itemView.findViewById(R.id.tdAsilKitap);
            digerKitap = (TextView)itemView.findViewById(R.id.tdDigerKitap);
            kullanici = (TextView)itemView.findViewById(R.id.tdKullanici);
            kitapResim = (ImageView)itemView.findViewById(R.id.takasDurumImg);


        }
    }

    List<takasDurumGetSet> listKitap;
    CustomItemClickListener listener;
    Context context;
    public  RecylerAdapterTD(Context context,List<takasDurumGetSet> listKitap,CustomItemClickListener listener)
    {
        this.listKitap = listKitap;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public RecylerAdapterTD.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view2,parent,false);//yapılan cardview layautu çağrıldı
        final RecylerAdapterTD.ViewHolder viewHolder = new RecylerAdapterTD.ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {//tıklama olaylarının gerçekleşeceği kısım
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,viewHolder.getPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecylerAdapterTD.ViewHolder holder, int position) {

        holder.asilKitap.setText(listKitap.get(position).getAsilKitap());
        holder.digerKitap.setText(listKitap.get(position).getDigerKitap());
        holder.kullanici.setText(listKitap.get(position).getKullanici());

        RequestQueue queue = Volley.newRequestQueue(context);//cardview  resimleri için

        ImageRequest resimIstek = new ImageRequest(listKitap.get(position).getKitapResim(), new Response.Listener<Bitmap>() {
            @Override//sunucudan link ile resim istendi imageview'de gösterildi
            public void onResponse(Bitmap response) {
                holder.kitapResim.setImageBitmap(response);
            }
        },0,0,null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        queue.add(resimIstek);

    }


    @Override
    public int getItemCount() {
        return listKitap.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
