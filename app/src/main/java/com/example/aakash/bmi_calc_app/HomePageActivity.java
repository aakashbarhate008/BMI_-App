
package com.example.aakash.bmi_calc_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class HomePageActivity extends AppCompatActivity {
    TextView tvHeight,tvName,tvFeet,tvInch,tvvFeet,tvvInch,tvTemp;
    Switch switch2;
    EditText etCms,etWeight;
    Button btnFind;
    Spinner spinFeet,spinInch;
    TextToSpeech tts;

    SharedPreferences sp;
    SharedPreferences bp;

    static DbHandler db;



    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Context context;
        tts= new TextToSpeech( HomePageActivity.this,
                new TextToSpeech.OnInitListener() {

                    @Override
                    public void onInit(int i) {
                        if (i != TextToSpeech.ERROR)
                            tts.setLanguage(Locale.ENGLISH);

                    }

                });




        int o=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);



        tvHeight=findViewById(R.id.tvHeight);
        tvName=findViewById(R.id.tvName);
        tvFeet=findViewById(R.id.tvFeet);
        tvInch=findViewById(R.id.tvInch);
        tvvFeet=findViewById(R.id.tvvFeet);
        tvvInch=findViewById(R.id.tvvInch);
        tvTemp=findViewById(R.id.tvTemp);

        etCms=findViewById(R.id.etCms);
        etWeight=findViewById(R.id.etWeight);

        btnFind=findViewById(R.id.btnFind);

        spinFeet=findViewById(R.id.spinFeet);
        spinInch=findViewById(R.id.spinInch);

        switch2=findViewById(R.id.switch2);
        db=new DbHandler(this);

        sp=getSharedPreferences("f1",MODE_PRIVATE);
        String name=sp.getString("name","");
        String age=sp.getString("age","");
        String phno=sp.getString("phoneno","");
        String gen=sp.getString("gender","");

        bp=getSharedPreferences("f2",MODE_PRIVATE);
        String bmi=bp.getString("BMI","");

        final ArrayList<String> s1=new ArrayList<>();
        s1.add("1");
        s1.add("2");
        s1.add("3");
        s1.add("4");
        s1.add("5");
        s1.add("6");
        s1.add("7");
        s1.add("8");
        s1.add("9");
        s1.add("10");

        ArrayAdapter arrayAdapter1=new ArrayAdapter(this,android.R.layout.simple_spinner_item,s1);
        spinFeet.setAdapter(arrayAdapter1);

        final ArrayList<String> s2=new ArrayList<>();
        s2.add("1");
        s2.add("2");
        s2.add("3");
        s2.add("4");
        s2.add("5");
        s2.add("6");
        s2.add("7");
        s2.add("8");
        s2.add("9");
        s2.add("10");

        tvName.setText("WELCOME "+name);
        Toast.makeText(this, ""+name+" "+age+" "+phno+" "+gen, Toast.LENGTH_SHORT).show();
        tts.speak("result="+name,TextToSpeech.QUEUE_ADD,null);


        ArrayAdapter arrayAdapter2=new ArrayAdapter(this,android.R.layout.simple_spinner_item,s2);
        spinInch.setAdapter(arrayAdapter1);

        class MeraTask extends AsyncTask<String ,Void,Double>
        {
            double temp;
            @Override
            protected Double doInBackground(String... strings) {
                String json=" ",line="";
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.connect();

                    InputStreamReader isr = new InputStreamReader(con.getInputStream());
                    BufferedReader br = new BufferedReader(isr);

                    while ((line = br.readLine()) != null) {
                        json = json + line + "\n";

                    }

                    JSONObject o = new JSONObject(json);
                    JSONObject p = o.getJSONObject("main");
                    temp = p.getDouble("temp");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return temp;
            }

            @Override
            protected void onPostExecute(Double aDouble) {
                super.onPostExecute(aDouble);
                tvTemp.setText("TEMP IS :"+aDouble);
            }
        }

        MeraTask f1=new MeraTask();
        String w1="http://api.openweathermap.org/data/2.5/weather?units=metric";
        String w2="&q="+"thane";
        String w3="&appid=c6e315d09197cec231495138183954bd";
        String w=w1+w2+w3;
        f1.execute(w);


        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    etCms.setVisibility(View.VISIBLE);
                    tvvFeet.setVisibility(View.INVISIBLE);
                    tvvInch.setVisibility(View.INVISIBLE);
                    spinFeet.setVisibility(View.INVISIBLE);
                    spinInch.setVisibility(View.INVISIBLE);

                    btnFind.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String htms = etCms.getText().toString();
                            String wt = etWeight.getText().toString();

                            if (htms.length() == 0) {
                                etCms.setError("CANT BE EMPTY");
                                etCms.requestFocus();
                                return;
                            }

                            if (wt.length() == 0) {
                                etWeight.setError("CANT BE EMPTY");
                                etWeight.requestFocus();
                                return;
                            }
                            Double ht = Double.parseDouble(htms) / 100;
                            Float res = calBmi(ht, Integer.parseInt(wt));

                            SharedPreferences.Editor editor = bp.edit();
                            editor.putString("BMI", String.valueOf(res));
                            editor.commit();

                            Intent i = new Intent(HomePageActivity.this, ResultActivity.class);
                            startActivity(i);
                            finish();

                        }
                    });
                }

                else
                {
                    etCms.setVisibility(View.INVISIBLE);
                    tvvFeet.setVisibility(View.VISIBLE);
                    tvvInch.setVisibility(View.VISIBLE);
                    spinFeet.setVisibility(View.VISIBLE);
                    spinInch.setVisibility(View.VISIBLE);


                    btnFind.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int fid=spinFeet.getSelectedItemPosition();
                            String feet=s1.get(fid);

                            int inchid=spinInch.getSelectedItemPosition();
                            String inch=s2.get(inchid);

                            String wt=etWeight.getText().toString();

                            if(wt.matches(""))
                            {
                                Toast.makeText(HomePageActivity.this, "FILL WEIGHT", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int weight=Integer.parseInt(wt);


                            if(wt.length()==0 || weight<=0)
                            {
                                etWeight.setError("INVALID");
                                etWeight.requestFocus();
                                return;
                            }

                            double ht=((Double.parseDouble(feet)*0.3048)+(Double.parseDouble(inch)*0.0254));

                            Float res = calBmi(ht, Integer.parseInt(wt));

                            SharedPreferences.Editor editor = bp.edit();
                            editor.putString("BMI", String.valueOf(res));
                            editor.commit();

                            Intent i = new Intent(HomePageActivity.this, ResultActivity.class);
                            startActivity(i);
                            finish();

                            SharedPreferences.Editor editor2 = bp.edit();
                            editor.putString("name", String.valueOf(res));
                            editor.commit();

                        }
                    });



                }
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fid=spinFeet.getSelectedItemPosition();
                String feet=s1.get(fid);

                int inchid=spinInch.getSelectedItemPosition();
                String inch=s2.get(inchid);

                String wt=etWeight.getText().toString();

                if(wt.matches(""))
                {
                    Toast.makeText(HomePageActivity.this, "Fill Weight", Toast.LENGTH_SHORT).show();
                    return;
                }
                int weight=Integer.parseInt(wt);

                if(wt.length()==0 || weight<=0)
                {
                    etWeight.setError("INVALID");
                    etWeight.requestFocus();
                    return;
                }

                double fe= Double.parseDouble(feet);
                double in= Double.parseDouble(inch);
                double  feetm=fe*0.305;
                double inchm=(in*2.54)/100;
                double ht=feetm+inchm;


                Float res = calBmi(ht, Integer.parseInt(wt));

                SharedPreferences.Editor editor = bp.edit();
                editor.putString("BMI", String.valueOf(res));
                editor.commit();

                Intent i = new Intent(HomePageActivity.this, ResultActivity.class);
                startActivity(i);
                finish();

                SharedPreferences.Editor editor2 = bp.edit();
                editor.putString("name", String.valueOf(res));
                editor.commit();

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

    public float calBmi(Double height, int Weight)
    {
        double bmi;
        bmi=(Weight/(height *height));
        float res=(int)(bmi*100 +.5);
        return (float)res/100;

    }

}
