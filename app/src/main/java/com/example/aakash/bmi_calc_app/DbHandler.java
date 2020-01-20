package com.example.aakash.bmi_calc_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class DbHandler extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;
    Context context;

    DbHandler(Context context)
    {
        super(context,"result.db",null,1);
        this.context=context;
        sqLiteDatabase=this.getWritableDatabase();


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="create table result(name String ,age String,phoneno String,gender String,bmi String,status String)";
        sqLiteDatabase.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addResult(Result r)
    {
        ContentValues cv=new ContentValues();
        cv.put("name",r.getName());
        cv.put("age",r.getAge());
        cv.put("phoneno",r.getPhoneno());
        cv.put("gender",r.getGender());
        cv.put("bmi",r.getBmi());
        cv.put("status",r.getStatus());

        long id=sqLiteDatabase.insert("result",null,cv);

        if(id<0)
        {
            Toast.makeText(context, "Issue in Save", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "SucessFul save", Toast.LENGTH_SHORT).show();

    }

    public String viewResult() {
        StringBuffer sb = new StringBuffer();
        Cursor cursor = sqLiteDatabase.query("result", null, null, null, null, null, null);
        cursor.moveToFirst();

        if(cursor.getCount()>0) {

            do {
                String name = cursor.getString(0);
                String age = cursor.getString(1);
                String phoneno = cursor.getString(2);
                String gender = cursor.getString(3);
                String bmi = cursor.getString(4);
                String status = cursor.getString(5);
                sb.append("Name :" + name + "\nAge :" + age + "\nPhoneno :" + phoneno + "\nGender :" + gender + "\nBmi :" + bmi + "\nStatus :" + status + "\n\n");


            }
            while (cursor.moveToNext());
        }
        return sb.toString();
    }
}
