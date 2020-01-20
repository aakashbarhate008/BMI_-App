package com.example.aakash.bmi_calc_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResultActivity extends AppCompatActivity {
    TextView tvResult,tvUnder,tvNormal,tvOver,tvObese;
    Button btnBack,btnShare,btnSave,btnView;

    SharedPreferences bp;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int o=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);

        tvResult=findViewById(R.id.tvResult);
        tvUnder=findViewById(R.id.tvUnder);
        tvNormal=findViewById(R.id.tvNormal);
        tvOver=findViewById(R.id.tvOver);
        tvObese=findViewById(R.id.tvObese);

        btnBack=findViewById(R.id.btnBack);
        btnSave=findViewById(R.id.btnSave);
        btnShare=findViewById(R.id.btnShare);
        btnView=findViewById(R.id.btnView);

        bp=getSharedPreferences("f2",MODE_PRIVATE);
        sp=getSharedPreferences("f1",MODE_PRIVATE);

        final String res=bp.getString("BMI","");

        final String name=sp.getString("name","");
        final String phoneno=sp.getString("phoneno","");
        final String age=sp.getString("age","");
        final String gen=sp.getString("gender","");
        String status=" ";


        tvResult.setText(" YOUR BMI IS :"+res);

        Double ans=Double.parseDouble(res);
        if(ans>=0.0 && ans<=18.4)
        {
            tvUnder.setTextColor(Color.RED);
            status="YOU ARE UNDERWEIGHT";
        }
        if(ans>=18.5 && ans<=24.9)
        {
            tvNormal.setTextColor(Color.RED);
            status="YOU ARE NORMAL";
        }
        if(ans>=25.0 && ans<=29.9)
        {
            tvOver.setTextColor(Color.RED);
            status="YOU ARE OVER-WEIGHT";

        }
        if(ans>=30.0)
        {
            tvObese.setTextColor(Color.RED);
            status="YOU ARE OBESES";

        }



        {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(ResultActivity.this,HomePageActivity.class);
                    startActivity(i);
                }
            });
        }

        final String finalStatus1 = status;
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg="MY TODAYS UPDATE \n NAME :"+name+"\n AGE :"+age+" \n Gender :"+gen+" \n PHONE NO :"+phoneno+"\n BMI :"+res+"\n STATUS: "+ finalStatus1;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, null));


            }
        });

        final String finalStatus = status;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Result r=new Result(name,age, phoneno,gen, finalStatus,res);
                HomePageActivity.db.addResult(r);
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ResultActivity.this,ViewActivity.class);
                startActivity(i);
            }
        });
    }

    public void onBackPressed()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you want to close");
        builder.setCancelable(false);

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.setTitle("EXIT");
        alertDialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.website)
        {
            Intent i=new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://"+"www.google.com"));
            startActivity(i);
        }

        if(item.getItemId()==R.id.about)
            Toast.makeText(this, "By Aakash Barhate version 1.0", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}
