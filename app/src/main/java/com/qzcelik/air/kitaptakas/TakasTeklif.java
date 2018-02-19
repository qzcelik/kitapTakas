package com.qzcelik.air.kitaptakas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TakasTeklif extends AppCompatActivity implements View.OnClickListener {


    TextView kitapAd,kitapSahip,digerKitapTxt;
    ImageView kitapResim;
    Spinner kitapListe;
    Button istekButon;
    ResimGetir resim;
    ArrayList <String> kitaplar;
    ArrayAdapter <String> spinnerAdaptor;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String url= "http://ibrahimozcelik.net/infoline/kitapBilgi.php";

    String veriGonderUrl = "http://ibrahimozcelik.net/infoline/takas.php";
    String asilKitap,digerKitap,kitapSahibi,kitapResimUrl,isteyenKullanici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takas_teklif);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        kitapAd = (TextView)findViewById(R.id.takasKitapAd);
        kitapSahip = (TextView)findViewById(R.id.TakasKitapSahip);
        digerKitapTxt = (TextView)findViewById(R.id.digerKitapTxt);

        kitapResim = (ImageView)findViewById(R.id.takasKitapImg);

        kitapListe = (Spinner)findViewById(R.id.takasKitapListe);
        istekButon = (Button) findViewById(R.id.takasButon);
        istekButon.setOnClickListener(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        Intent veriAl = getIntent();

        asilKitap = veriAl.getExtras().getString("takasKitap");
        kitapSahibi = veriAl.getExtras().getString("kitapSahip");
        kitapResimUrl =veriAl.getExtras().getString("takasKitapResim");
        isteyenKullanici = preferences.getString("kullaniciAdi","default");

        kitapAd.setText("Takas İstenen Kitap : " + asilKitap);
        kitapSahip.setText("Kitap Sahibi : "+kitapSahibi);


        bilgiDoldur();//kitap resim ve diğer kitap bilgileri için
        veriCek();//kullanıcının kendi kitaplarını listelemek için

  }

    void bilgiDoldur()
    {
        kitaplar = new ArrayList<String>();
        resim = new ResimGetir();
        RequestQueue queue = Volley.newRequestQueue(this);

        ImageRequest istek = new ImageRequest(kitapResimUrl, new Response.Listener<Bitmap>() {
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
                });
        queue.add(istek);

    }

    void veriCek()
    {

        RequestQueue queue = Volley.newRequestQueue(this);
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
        )
        {
            protected  Map<String,String> getParams() throws AuthFailureError
            {
                Map <String,String> parametre = new HashMap<>();
                parametre.put("kulAd",isteyenKullanici);

                return parametre;
            }

        };

        queue.add(istek);

 }

    void spinnerDoldur(String json)
    {

        JSONArray jsonArray = null;
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("kitaplar");

            for (int i =0 ;i<jsonArray.length();i++)
            {
                jsonObject = (JSONObject) jsonArray.get(i);
                kitaplar.add(jsonObject.getString("kitapAd"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        spinnerAdaptor = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,kitaplar);//adaptör hazırlandı
        spinnerAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//adaptör görünümü
        kitapListe.setAdapter(spinnerAdaptor);

        kitapListe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//hangi kitap takas edilecek
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                digerKitap = kitaplar.get(i);
                digerKitapTxt.setText(digerKitap);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.takasButon)
        {
            takasIstekGonder();
        }
    }

    void takasIstekGonder()//takas teklifi gönderildi
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest istek = new StringRequest(Request.Method.POST, veriGonderUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            Toast.makeText(TakasTeklif.this,"Takas Gönderildi",Toast.LENGTH_SHORT).show();
            finish();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(TakasTeklif.this,"bir problem oluştu",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            protected  Map<String,String> getParams() throws AuthFailureError
            {
                Map<String,String> parametre = new HashMap<>();

                parametre.put("kitapSahip",kitapSahibi);
                parametre.put("asilKitap",asilKitap);
                parametre.put("istekSahibi",isteyenKullanici);
                parametre.put("digerKitap",digerKitap);
                parametre.put("kitapResim",kitapResimUrl);
                return  parametre;
            }
        };

        queue.add(istek);
    }
}
