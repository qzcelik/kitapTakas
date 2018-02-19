package com.qzcelik.air.kitaptakas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.sax.SAXResult;

public class UyeOl extends AppCompatActivity  implements View.OnClickListener{

    EditText kulAd,kulSifre,kulSifreTekrar,kulSehir;
    Button gonder;
    int sayac;//aynı isimde başkası var mı kontrolü sayacı
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye_ol);

       preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       editor  = preferences.edit();//kullanıcı adını saklayıp diğer activitylerde kullanmak için

       uiELeman();
    }

    void uiELeman()//UI elemanlarını bulma
    {

        kulAd = (EditText)findViewById(R.id.kulAdEt);
        kulSifre = (EditText) findViewById(R.id.kulSifreEt);
        kulSifreTekrar = (EditText)findViewById(R.id.kulSifreTekEt);
        kulSehir = (EditText) findViewById(R.id.sehirEt);
        gonder = (Button) findViewById(R.id.uyeOlBut);
        gonder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {//ekranda ki butonları bu fonksiyondan kontrol edeeceğiz

        if(view.getId()==R.id.uyeOlBut)
        {
            if(kulAd.getText().toString().length() > 0 && kulSifre.getText().toString().length()>0 //boş mu değil mi
                    && kulSifreTekrar.getText().toString().length()>0
                    && kulSehir.getText().toString().length()>0)
            {
                if(kulSifre.getText().toString().equals(kulSifreTekrar.getText().toString()))
                {
                    veriGonder();
                }
                else
                {
                    Toast.makeText(UyeOl.this,"Şifreler Uyuşmuyor",Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(UyeOl.this,"ALanlar Boş Geçilemez",Toast.LENGTH_SHORT).show();
            }
        }
    }

    void veriGonder()//mysql veritabanına verileri gönderiyoruz
    {
        String url = "http://ibrahimozcelik.net/infoline/login.php";
        RequestQueue queue = Volley.newRequestQueue(UyeOl.this);//isteklerin bir kuyruk yapısında saklamak için

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>()   {//backend'e istek yapıyoruz

            @Override
            public void onResponse(String response) {

                Toast.makeText(UyeOl.this,"Kayıt Başarılı",Toast.LENGTH_SHORT).show();
                editor.putString("kullaniciAdi",kulAd.getText().toString());//kullanıcı adı saklandı
                editor.commit();
                Intent intent = new Intent(UyeOl.this,KullaniciProfil.class);
                startActivity(intent);
                finish();

          }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(UyeOl.this,"istek onaylanmadı",Toast.LENGTH_SHORT).show();
                    }
                }){

            protected Map<String,String> getParams()
            {
                Map <String,String> parametreler = new HashMap<String, String>();

                parametreler.put("ad",kulAd.getText().toString());
                parametreler.put("sifre",kulSifre.getText().toString());
                parametreler.put("sehir",kulSehir.getText().toString());

                editor.putString("kullaniciAdi",kulAd.getText().toString());//kullanıcı adı saklandı
                editor.commit();

                return parametreler;
            }
        };

        queue.add(request);
    }
}
