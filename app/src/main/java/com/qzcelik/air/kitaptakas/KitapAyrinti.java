package com.qzcelik.air.kitaptakas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class KitapAyrinti extends AppCompatActivity {


    TextView kitapAd,kitapTur;
    ImageView kitapResim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_ayrinti);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        kitapAd = (TextView)findViewById(R.id.kitapAdText);
        kitapTur = (TextView) findViewById(R.id.kitapTurText);
        kitapResim = (ImageView)findViewById(R.id.kitapImg);

        listeDoldur();

    }
    void listeDoldur()
    {

        RequestQueue queue = Volley.newRequestQueue(KitapAyrinti.this);
        Intent intentVeriAl = getIntent();

        kitapAd.setText(intentVeriAl.getExtras().getString("kitapAd"));
        kitapTur.setText(intentVeriAl.getExtras().getString("kitapTur"));

        ImageRequest resimIstek = new ImageRequest(intentVeriAl.getExtras().getString("kitapResim"), new Response.Listener<Bitmap>() {
            @Override//sunucudan link ile resim istendi imageview'de gösterildi
            public void onResponse(Bitmap response) {
                kitapResim.setImageBitmap(response);
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

}
