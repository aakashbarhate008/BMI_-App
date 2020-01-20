package com.example.aakash.bmi_calc_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView tvMessage;
    EditText etName,etAge,etPhoneNo;
    RadioGroup rgGender;
    Button btnSubmit;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int o=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);

        tvMessage=findViewById(R.id.tvMessage);

        etName=findViewById(R.id.etName);
        etAge=findViewById(R.id.etAge);
        etPhoneNo=findViewById(R.id.etPhoneNo);

        rgGender=findViewById(R.id.rgGender);

        btnSubmit=findViewById(R.id.btnSubmit);

        sp=getSharedPreferences("f1",MODE_PRIVATE);

        final String name=sp.getString("name","");
        final String age=sp.getString("age","");
        final String phone=sp.getString("phone_no","");
        String gen=sp.getString("gender","");


        if(name.length()==0) {
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int id=rgGender.getCheckedRadioButtonId();
                    RadioButton rb=findViewById(id);
                    final String gender=rb.getText().toString();

                    String name = etName.getText().toString();
                    String age = etAge.getText().toString();
                    String phoneno = etPhoneNo.getText().toString();

                    if(name.matches("") || age.matches("") || phoneno.matches(""))
                        Toast.makeText(MainActivity.this, "FILL THE FORM", Toast.LENGTH_SHORT).show();


                    if (name.length() <2 || !(name.matches("^[a-zA-Z]*$")))
                    {
                        etName.setError("INVALID");
                        etName.requestFocus();
                        return;
                    }

                    if (age.length() == 0 || Integer.parseInt(age) <= 0) {
                        etAge.setError("INVALID AGE");
                        etAge.requestFocus();
                        return;
                    }

                    if (phoneno.length() < 10 || phoneno.length()>10) {
                        etPhoneNo.setError("Invalid Phone No");
                        etPhoneNo.requestFocus();
                        return;

                    }

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("name", name);
                    editor.putString("age", age);
                    editor.putString("phoneno", phoneno);
                    editor.putString("gender",gender);

                    editor.commit();
                    Intent i = new Intent(MainActivity.this, HomePageActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }

        else
        {
            Intent i=new Intent(MainActivity.this,HomePageActivity.class);
            startActivity(i);
            finish();
        }
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

}
