package com.example.productdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;
//import android.app.Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //String EXTRA_MESSAGE = "AAAAAAAAAAAAAAAAAAA";
        Intent switch_to_secondary = new Intent(MainActivity.this, SecondActivity.class);

        //aaaaaaaaaaa.putExtra("key", value); //Optional parameters

        //Log.e(R.class.getName(),Date.getDate());
        //Log.d("lmao", "onCreate: in MAIN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button1);
        TextView txt=findViewById(R.id.textView);
        Main.Product prod=new Main.Product(2L,"bruh",4f,5f,7f,new Main.DD("1ධ3ධ5ධ6ධ7ධ8ධ9ධ1ධ2"),"literal");
        try {
//            txt.setText(DBDate.toReadableString(DBDate.getDate(), ";XXXjkhkjhiu;"));
            txt.setText(prod.format("%name%,%description%,%%id%*69% real=%%2kcal*price%/12% %1/2% %DD% ,%kcalpprice*%"));
        }catch(Exception e){
            e.printStackTrace();
            Log.e(R.class.getName(),"lmao ur dumb");
            txt.setText("ur dumb");
            Toast.makeText(this,"ur dumb",Toast.LENGTH_SHORT).show();
        }

        button.setOnClickListener( new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                //button.setText("i hate this");
                //setContentView(R.layout.second_activity);
                ((Main) getApplication()).global_i+=1;
                Log.e(R.class.getName(),"global_i="+((Main) getApplication()).global_i);
                MainActivity.this.startActivity(switch_to_secondary);
            }

        });

    }

}
