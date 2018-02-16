package com.qzcelik.air.kitaptakas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class GirisYap extends AppCompatActivity implements View.OnClickListener {


    EditText kulAdEt,sifreEt;
    Button girisButon;
    int kontrolSayac;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);
        UIEleman();
    }

    void UIEleman()
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();


        kulAdEt = (EditText)findViewById(R.id.kulAdEtGiris);
        sifreEt = (EditText)findViewById(R.id.kulSifreEtGiris);
        girisButon = (Button)findViewById(R.id.girisButon);

        girisButon.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.girisButon)
        {
            veriGonder();//giriş yapmak için veritabanına verileri gönderiyoruz
        }
    }


    void veriGonder()
    {

        String url="http://ibrahimozcelik.net/infoline/giris.php";

        RequestQueue queue = Volley.newRequestQueue(GirisYap.this);//isteklerin bir kuyruk yapısında saklamak için

        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try//veritabanından veriler çekilerek kullanıcı var mı yok mu kontrolü yapılıyor
                {
                    JSONArray jsonArray = null;
                    JSONObject jsonObject = null;

                    jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("kullanicilar");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        kontrolSayac++;
                        jsonObject = (JSONObject) jsonArray.get(i);
                        if(jsonObject.getString("kulAd").equals(kulAdEt.getText().toString())
                                &&jsonObject.getString("kulSifre").equals(sifreEt.getText().toString()))
                        {
                            break;
                        }
                    }

                    if(kontrolSayac==jsonArray.length())
                    {
                        Toast.makeText(GirisYap.this,"Kullanıcı Adı veya Şifre Yanlış",Toast.LENGTH_SHORT).show();
                        kontrolSayac =0;
                    }
                    else
                    {

                        Toast.makeText(GirisYap.this,"Sisteme Giriş Başarılı",Toast.LENGTH_SHORT).show();

                        editor.putString("kullaniciAdi",kulAdEt.getText().toString());
                        editor.commit();

                        kontrolSayac =0;
                        Intent giris = new Intent(GirisYap.this,KullaniciProfil.class);
                        startActivity(giris);
                        finish();

                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GirisYap.this,"İnternet Bağlantınızı Kontrol Edin",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(istek);
    }
}
