package com.qzcelik.air.kitaptakas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class TakasSonuc extends AppCompatActivity implements View.OnClickListener {


    Button kabul,ret;
    TextView textTakass,textDurum;
    ImageView kitapResim;
    String url;
    String url2="http://ibrahimozcelik.net/infoline/durumGuncelle.php";
    String sira;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takas_sonuc);


        kitapResim = (ImageView)findViewById(R.id.kitapImg);

        textTakass = (TextView)findViewById(R.id.textTakas);
        textDurum = (TextView)findViewById(R.id.durum);

        Intent getIntent = getIntent();
        url = getIntent.getExtras().getString("kitapResim");

        sira = ""+getIntent.getExtras().getInt("id");

        if(getIntent.getExtras().getInt("durum") == 0)
        {
            textDurum.setText("Reddedildi");
        }

        if(getIntent.getExtras().getInt("durum") == 1)
        {
            textDurum.setText("Onaylandı");
        }

        if(getIntent.getExtras().getInt("durum") == 2)
        {
            textDurum.setText("Beklemede");
        }

        textTakass.setText("Kullanici Adi : "+getIntent.getExtras().getString("asilKitap")+"\n"+
        "Senin Kitabın : "+getIntent.getExtras().getString("kullanici")+"\n"+
            "Diğer Kitap : "+getIntent.getExtras().getString("digerKitap"));
        resimYukle();

        kabul = (Button)findViewById(R.id.btnKabul);
        ret = (Button)findViewById(R.id.btnRet);
        kabul.setOnClickListener(this);
        ret.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btnKabul)
        {
            durumDegistir("1");
        }

        if(view.getId() == R.id.btnRet)
        {
            durumDegistir("0");
        }
    }


    void durumDegistir(final String durum)
    {

        RequestQueue queue = Volley.newRequestQueue(TakasSonuc.this);

       final StringRequest request = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(TakasSonuc.this,response,Toast.LENGTH_SHORT).show();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        )
        {
            protected Map <String,String> getParams()
            {
                Map<String,String> parametre = new HashMap<String, String>();
                parametre.put("durumGuncelle",durum);
                parametre.put("sira",sira);

                return  parametre;
            }
        };
            queue.add(request);
    }


    void resimYukle()
    {

        RequestQueue queue = Volley.newRequestQueue(TakasSonuc.this);

        ImageRequest resimIstek = new ImageRequest(url, new Response.Listener<Bitmap>() {
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
