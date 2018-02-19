package com.qzcelik.air.kitaptakas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class kitapEkle extends AppCompatActivity implements View.OnClickListener {


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    int pickImage = 99;
    String url = "http://ibrahimozcelik.net/infoline/multipart.php";
    Bitmap bitmap;
    ImageView resimSec;
    Button resimGonder;
    EditText kitapAdiEt,kitapTuruEt,kitapYazarEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_ekle);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        UIEleman();
    }

    void UIEleman()
    {
        resimSec = (ImageView)findViewById(R.id.resimSecImage);
        resimGonder = (Button)findViewById(R.id.resimGonderBut);
        kitapAdiEt = (EditText)findViewById(R.id.kitapAdiEt);
        kitapTuruEt = (EditText)findViewById(R.id.kitapTuruEt);
        kitapYazarEt = (EditText)findViewById(R.id.kitapYazarEt);
        resimSec.setOnClickListener(this);
        resimGonder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.resimSecImage)
        {
            galeriAc();
        }

        if(view.getId() == R.id.resimGonderBut)
        {
            resimYolla();
        }
    }

    void galeriAc()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Resim Seçin"),pickImage);// elde edilen resim onActivityResult yardımı ile
                                                                                   //ekranda gösteriliyor

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pickImage && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //seçilen resim
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Toast.makeText(this, ""+bitmap, Toast.LENGTH_SHORT).show();

                resimSec.setMaxHeight(100);
                resimSec.setMaxWidth(100);

                resimSec.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void resimYolla()//seçilen resmin volley kütüphanesi ile hostinge yollanma kısmı
    {
        RequestQueue queue = Volley.newRequestQueue(kitapEkle.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(kitapEkle.this, "Kitap Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                finish();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(kitapEkle.this, ""+error, Toast.LENGTH_SHORT).show();
                    }
                }
        ){
           protected Map <String,String> getParams() throws AuthFailureError
            {

                Map<String,String> parametre = new HashMap<>();

                String resim = resimString(bitmap);
                parametre.put("image",resim);
                parametre.put("kulAd",preferences.getString("kullaniciAdi","default"));
                parametre.put("kitapAd",kitapAdiEt.getText().toString());
                parametre.put("kitapTuru",kitapTuruEt.getText().toString());
                parametre.put("kitapYazar",kitapYazarEt.getText().toString());
                return  parametre;
            }
        };

        queue.add(request);
    }

    public String resimString(Bitmap bitmap) //resmi hostinge yollamak için gerekli ayarlamalar yaplıyor
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte [] bayt = byteArrayOutputStream.toByteArray();
        String temp = Base64.encodeToString(bayt,Base64.DEFAULT);
        return temp;
    }

}
