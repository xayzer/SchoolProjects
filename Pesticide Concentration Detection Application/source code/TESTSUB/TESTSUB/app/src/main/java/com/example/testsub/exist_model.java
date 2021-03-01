package com.example.testsub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class exist_model extends AppCompatActivity {

    private TextView tv;
    private Spinner sp;
    private Button con_btn;
    private ArrayAdapter<String> adapter;
    private String data_file;
    private Button remove_btn;

    private String strdata;
    private double k;
    private double b;
    private double px3;
    private double px4;

    private final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() :
            "/mnt/sdcard";//保存到SD卡
    private final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/Density_Predict/usrData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist_model);

        tv = (TextView) findViewById(R.id.tv);
        sp = (Spinner) findViewById(R.id.sp);
        con_btn = (Button) findViewById(R.id.con_btn);
        remove_btn = (Button)findViewById(R.id.remove_btn);

        Intent intent = getIntent();
        data_file = intent.getStringExtra("usr") + ".txt";
        final ArrayList<String> arrayList = readFile(data_file);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> ap = (ArrayAdapter<String>) parent.getAdapter();
                strdata = ap.getItem(position);
                String[] str_arr = strdata.split("=");
                k = Double.parseDouble(str_arr[0]);
                b = Double.parseDouble(str_arr[1]);
                px3 = Double.parseDouble(str_arr[2]);
                px4 = Double.parseDouble(str_arr[3]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        con_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(exist_model.this, getPic.class);
                intent.putExtra("k", k);
                intent.putExtra("b", b);
                intent.putExtra("px3", px3);
                intent.putExtra("px4", px4);
                startActivity(intent);
            }
        });

        remove_btn = (Button)findViewById(R.id.remove_btn);
        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.remove(strdata);
                changeFile(data_file,arrayList);
            }
        });
    }


    public ArrayList<String> readFile(String fileName) {
        String subForder = SAVE_REAL_PATH;
        File folder = new File(subForder);
        if (!folder.exists()) {
            Boolean bl = folder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (!myCaptureFile.exists()) {
            try {
                myCaptureFile.createNewFile();
                String data = "-0.28790104" + "=" + "24.883634395270192" + "=" + "25" + "=" + "88" + '\n';
                FileWriter fileWritter = new FileWriter(myCaptureFile.getAbsolutePath(), true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write(data);
                bufferWritter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(myCaptureFile.getAbsolutePath());
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public void changeFile(String fileName,ArrayList<String> al){
        String subForder = SAVE_REAL_PATH;
        File folder = new File(subForder);
        if (!folder.exists()) {
            Boolean bl = folder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (myCaptureFile.exists()) {
            myCaptureFile.delete();
            try {
                myCaptureFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fileWritter = new FileWriter(myCaptureFile.getAbsolutePath(),true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            for (String data:al){
                bufferWritter.write(data+'\n');
            }
            bufferWritter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "删除成功", Toast.LENGTH_LONG).show();
    }

}
