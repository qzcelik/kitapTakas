package com.qzcelik.air.kitaptakas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakasDurum extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String anaKullanici;
    String url="http://ibrahimozcelik.net/infoline/takasKitap.php";
    RecyclerView recyclerView;
    List<takasDurumGetSet> kitapListe;//kitap bilgierli cardview ler için
    int listelemeSarti;

    Button takasIstek,takasTeklif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takas_durum);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        anaKullanici = preferences.getString("kullaniciAdi","default");

        recyclerView = (RecyclerView)findViewById(R.id.rcListe2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        takasIstek = (Button)findViewById(R.id.takasIstBut);
        takasIstek.setOnClickListener(this);
        takasTeklif = (Button)findViewById(R.id.takasTekBut);
        takasTeklif.setOnClickListener(this);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        recyclerView.setLayoutManager(layoutManager);



        veriCek();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.takasIstBut)
        {
            listelemeSarti =0;
            veriCek();
        }

        if(view.getId() == R.id.takasTekBut)
        {
            listelemeSarti =1;
            veriCek();
        }
    }

    void veriCek()//tüm kitapları çekiyoruz
    {
        RequestQueue queue = Volley.newRequestQueue(TakasDurum.this);
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                recylerViewDoldur(response,listelemeSarti);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        queue.add(istek);
    }



    void recylerViewDoldur(String json,int kontrol)
    {

        kitapListe = new ArrayList<takasDurumGetSet>();

        JSONArray jsonArray;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("kitaplar");

            for (int i=0;i<jsonArray.length();i++)
            {
                jsonObject = (JSONObject)jsonArray.get(i);

                if(kontrol == 0) {

                    if (jsonObject.getString("istekSahip").equals(anaKullanici))
                    {
                        kitapListe.add(new takasDurumGetSet(jsonObject.getString("kitapSahip"), jsonObject.getString("asilKitap")
                                , jsonObject.getString("digerKitap"), jsonObject.getString("kitapResim"),jsonObject.getInt("kitapId"),
                                jsonObject.getInt("durum")));
                    }
                }

                if(kontrol == 1)
                {
                    if(jsonObject.getString("kitapSahip").equals(anaKullanici))
                    {
                        kitapListe.add(new takasDurumGetSet(jsonObject.getString("kitapSahip"),jsonObject.getString("asilKitap")
                                ,jsonObject.getString("digerKitap"),jsonObject.getString("kitapResim"),jsonObject.getInt("kitapId"),
                        jsonObject.getInt("durum")));
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        RecylerAdapterTD adapter = new RecylerAdapterTD(getApplicationContext(),kitapListe, new CustomItemClickListener() {//tıklama olayları için kendi kullandığımız
            @Override                                                                            //  CustomItemClickListener arayüzü
            public void onItemClick(View v, int position) {//recylerda elemana basılnca yapılan işlemler

                takasDurumGetSet takasIslem = kitapListe.get(position);
                Intent intent = new Intent(TakasDurum.this,TakasSonuc.class);

                intent.putExtra("kullanici",takasIslem.getKullanici());
                intent.putExtra("asilKitap",takasIslem.getAsilKitap());
                intent.putExtra("digerKitap",takasIslem.getDigerKitap());
                intent.putExtra("kitapResim",takasIslem.getKitapResim());
                intent.putExtra("id",takasIslem.getId());
                intent.putExtra("durum",takasIslem.getDurum());

                startActivity(intent);


          }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


}
