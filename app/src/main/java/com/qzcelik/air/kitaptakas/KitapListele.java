package com.qzcelik.air.kitaptakas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.Map;

public class KitapListele extends AppCompatActivity {


    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ListView kitapListe;
    ArrayList <String> kitapIsim = new ArrayList<>();
    ArrayList <String> kitapTur = new ArrayList<>();
    ArrayList <String> kitapResim = new ArrayList<>();
    String url= "http://ibrahimozcelik.net/infoline/kitapBilgi.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_listele);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        kitapListe = (ListView)findViewById(R.id.listee);

        listeGetir();


    }


 void listeGetir()
    {
       RequestQueue queue = Volley.newRequestQueue(KitapListele.this);

        final StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    listeDoldur(response);
           }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }

        ){

          protected  Map <String,String> getParams()
            {
                Map<String,String> parametre = new HashMap<String, String>();
                parametre.put("kulAd",preferences.getString("kullaniciAdi","default"));

                return  parametre;
            }
        };




        queue.add(istek);

    }

    void listeDoldur(String json)
    {
        JSONArray jsonArray=null;
        JSONObject jsonObject=null;

        try {
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("kitaplar");

            for(int i=0;i<jsonArray.length();i++)
            {
                jsonObject = (JSONObject) jsonArray.get(i);

                kitapIsim.add(jsonObject.getString("kitapAd"));
                kitapTur.add(jsonObject.getString("kitapTur"));
                kitapResim.add(jsonObject.getString("resimYol"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        ArrayAdapter <String> veriAdaptor = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,kitapIsim);
        kitapListe.setAdapter(veriAdaptor);

        kitapListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(KitapListele.this,KitapAyrinti.class);
                intent.putExtra("kitapAd",kitapIsim.get(i));
                intent.putExtra("kitapTur",kitapTur.get(i));
                intent.putExtra("kitapResim",kitapResim.get(i));
                startActivity(intent);
            }
        });

    }

}
