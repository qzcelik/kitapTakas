package com.qzcelik.air.kitaptakas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.List;

public class TakasKitap extends AppCompatActivity implements  View.OnClickListener{


    Spinner kitapTurleri;
    ArrayAdapter <String> spinnerAdaptor;//spiner için adaptör
    String url="http://ibrahimozcelik.net/infoline/veriCek.php";
    RecyclerView recyclerView;
    Button yazarAra,kitapAra;
    EditText kitapAraEt,yazarAraEt;
    List <kitapBilgileri> kitapBilgiList;//kitap bilgierli cardview ler için
    ArrayList<String> kitapTurler = new ArrayList<String>();//spinner doldurmak için
    String tumVeriler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takas_kitap);

        veriCek();

        kitapTurleri = (Spinner)findViewById(R.id.kitapTurSpin);

        recyclerView = (RecyclerView)findViewById(R.id.rcListe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        kitapAraEt = (EditText) findViewById(R.id.kitapAraEt);
        yazarAraEt = (EditText)findViewById(R.id.yazarAraEt);

        yazarAra = (Button)findViewById(R.id.yazarAraBut);
        kitapAra = (Button)findViewById(R.id.kitapAraBut);

        yazarAra.setOnClickListener(this);
        kitapAra.setOnClickListener(this);


        recyclerView.setLayoutManager(layoutManager);
        //recylerViewDoldur();

    }


    void veriCek()//tüm kitapları çekiyoruz
    {
        RequestQueue queue = Volley.newRequestQueue(TakasKitap.this);
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                spinnerDoldur(response);//kitap türleri hemen dolduruldu
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

    void recylerViewDoldur()
    {

        RecyleAdapter adapter = new RecyleAdapter(getApplicationContext(),kitapBilgiList, new CustomItemClickListener() {//tıklama olayları için kendi kullandığımız
            @Override                                                                            //  CustomItemClickListener arayüzü
            public void onItemClick(View v, int position) {//recylerda elemana basılnca yapılan işlemler

                kitapBilgileri kitapBilgi = kitapBilgiList.get(position);//tıklanan elemana ait bilgiler alınıyor

                Intent takasIntent = new Intent(TakasKitap.this,TakasTeklif.class);

                takasIntent.putExtra("kitapSahip",kitapBilgi.getKitapSahip());
                takasIntent.putExtra("takasKitap",kitapBilgi.getKitapAd());
                takasIntent.putExtra("takasKitapResim",kitapBilgi.getKitapResim());

                startActivity(takasIntent);

            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    JSONArray jsonArray = null;
    JSONObject jsonObject=null;

    void spinnerDoldur(String jsonDizi)//kitap türlerini backend den çekip göstermek için
    {
        tumVeriler = jsonDizi;
        int sayac = 0;
        kitapTurler.add("Kitap Tür Seçiniz");

        try {
            jsonObject = new JSONObject(jsonDizi);
            jsonArray = jsonObject.getJSONArray("kitaplar");

            for (int i =0;i<jsonArray.length();i++)//veritabanından aynı isimdeki kategoriler birden çok kere olacağı için
            {                                      //bu kategorileri teke indirmek için

               for(int j = sayac ;j<jsonArray.length();j++)
                {

                    jsonObject = (JSONObject) jsonArray.get(i);
                    if(kitapTurler.get(j).equals(jsonObject.getString("kitapTur")))
                    {
                         break;
                    }
                    else
                    {
                        sayac++;
                        kitapTurler.add(jsonObject.getString("kitapTur"));
                        break;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        spinnerAdaptor = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,kitapTurler);//adaptör hazırlandı
        spinnerAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//adaptör görünümü
        kitapTurleri.setAdapter(spinnerAdaptor);

        kitapTurleri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//spinnerdan seçilen tür ile recylerview dolduruluyor
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {//item seçme

                kitapBilgiList =new ArrayList<kitapBilgileri>();//her tıklamada yeni bir arraylist

                for (int j =0;j<jsonArray.length();j++)
                {
                    try {

                  jsonObject = (JSONObject) jsonArray.get(j);
                  if(jsonObject.getString("kitapTur").equals(adapterView.getSelectedItem().toString()))//o türdeki kitaplar
                        {
                            kitapBilgiList.add(new kitapBilgileri(jsonObject.getString("kitapAd"),jsonObject.getString("kitapTur")
                                    ,jsonObject.getString("resimYol"),jsonObject.getString("kulAd")));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                 }
                recylerViewDoldur();
             }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.kitapAraBut)//kitap adına göre arama yapılıp listeleniyor
        {
            kitapBilgiList = new ArrayList<kitapBilgileri>();

            try {
                jsonObject = new JSONObject(tumVeriler);
                jsonArray = jsonObject.getJSONArray("kitaplar");

                for(int i =0;i<jsonArray.length();i++)
                {

                    jsonObject = (JSONObject) jsonArray.get(i);

                    if(jsonObject.getString("kitapAd").equals(kitapAraEt.getText().toString()))
                    {
                        kitapBilgiList.add(new kitapBilgileri(jsonObject.getString("kitapAd"),
                                jsonObject.getString("kitapTur"),jsonObject.getString("resimYol"),jsonObject.getString("kulAd")));
                    }
                }

                recylerViewDoldur();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        if(view.getId() == R.id.yazarAraBut) //yazar adına göre arama yapılıp listeleniyor
        {

            kitapBilgiList = new ArrayList<kitapBilgileri>();

            try {
                jsonObject = new JSONObject(tumVeriler);
                jsonArray = jsonObject.getJSONArray("kitaplar");


                for(int i =0;i<jsonArray.length();i++)
                {

                    jsonObject = (JSONObject) jsonArray.get(i);
                    if(jsonObject.getString("kitapYazar").equals(yazarAraEt.getText().toString() ))
                    {
                        kitapBilgiList.add(new kitapBilgileri(jsonObject.getString("kitapAd"),
                                jsonObject.getString("kitapTur"),jsonObject.getString("resimYol"),jsonObject.getString("kulAd")));
                    }
                }

                recylerViewDoldur();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
