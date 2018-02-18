package com.qzcelik.air.kitaptakas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by air on 18.02.2018.
 */

public class ResimGetir {


    public Bitmap getImage(Context context, final String resimYol)
    {
        final Bitmap[] resim = {null};

        RequestQueue queue = Volley.newRequestQueue(context);

        ImageRequest resimIstek = new ImageRequest(resimYol, new Response.Listener<Bitmap>() {
            @Override//sunucudan link ile resim istendi imageview'de gösterildi
            public void onResponse(Bitmap response) {

                resim[0] = response;
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
        return resim[0];
    }

}
