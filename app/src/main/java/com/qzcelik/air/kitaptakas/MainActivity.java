package com.qzcelik.air.kitaptakas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    Button girisYap, uyeOl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        girisYap = (Button)findViewById(R.id.girisBut);
        uyeOl = (Button) findViewById(R.id.uyeBut);

        girisYap.setOnClickListener(this);
        uyeOl.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {


        if(view.getId() == R.id.girisBut)
        {
            Intent giriYap = new Intent(MainActivity.this,GirisYap.class);
            startActivity(giriYap);

        }
        if(view.getId() == R.id.uyeBut)
        {
            Intent uyeOl = new Intent(MainActivity.this,UyeOl.class);
            startActivity(uyeOl);

        }

    }
}
