package com.qzcelik.air.kitaptakas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class KullaniciProfil extends AppCompatActivity implements View.OnClickListener{


    TextView text;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Button kitapEkleButon,kitapListeleButon,kitapTakas,takasDurum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_profil);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        kitapEkleButon = (Button)findViewById(R.id.kitapEkleBut);
        kitapEkleButon.setOnClickListener(this);

        kitapListeleButon = (Button)findViewById(R.id.kitapListeBut);
        kitapListeleButon.setOnClickListener(this);

        kitapTakas = (Button)findViewById(R.id.kitapBul);
        kitapTakas.setOnClickListener(this);

        takasDurum = (Button)findViewById(R.id.takasDurumlari);
        takasDurum.setOnClickListener(this);

        text = (TextView)findViewById(R.id.textView);
        text.setText(preferences.getString("kullaniciAdi","default"));

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.kitapEkleBut:
            {
                Intent intent = new Intent(KullaniciProfil.this,kitapEkle.class );
                startActivity(intent);
            }break;

            case R.id.kitapListeBut:
            {
                Intent intent = new Intent(KullaniciProfil.this,KitapListele.class );
                startActivity(intent);

            }break;

            case R.id.kitapBul:
            {
                Intent intent = new Intent(KullaniciProfil.this,TakasKitap.class);
                startActivity(intent);
            }break;

            case R.id.takasDurumlari:
            {
                Intent intent = new Intent(KullaniciProfil.this,TakasDurum.class);
                startActivity(intent);
            }break;

          }

    }
}
