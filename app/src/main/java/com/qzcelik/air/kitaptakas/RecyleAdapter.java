package com.qzcelik.air.kitaptakas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import  android.support.v7.widget.CardView;
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
 * Created by air on 17.02.2018.
 */
//verileri recylerview de göstermek için adaptör
public class RecyleAdapter extends  RecyclerView.Adapter<RecyleAdapter.ViewHolder> {


    public static  class ViewHolder extends RecyclerView.ViewHolder{

        TextView kitapAd;
        TextView kitapTur;
        ImageView kitapResim;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            kitapAd = (TextView)itemView.findViewById(R.id.cardViewIsim);
            kitapTur = (TextView)itemView.findViewById(R.id.cardViewTur);
            kitapResim = (ImageView)itemView.findViewById(R.id.cardViewResim);
        }
    }

    List<kitapBilgileri> listKitap;
    CustomItemClickListener listener;

    public  RecyleAdapter(List<kitapBilgileri> listKitap,CustomItemClickListener listener)
    {
        this.listKitap = listKitap;
        this.listener = listener;

    }


    @Override
    public RecyleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);//yapılan cardview layautu çağrıldı
        final  ViewHolder viewHolder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {//tıklama olaylarının gerçekleşeceği kısım
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,viewHolder.getPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyleAdapter.ViewHolder holder, int position) {

        holder.kitapAd.setText(listKitap.get(position).getKitapAd());
        holder.kitapTur.setText(listKitap.get(position).getKitapTur());

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
