package com.example.testsub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class set_func extends AppCompatActivity {
    private Button con_btn;
    private EditText input1;
    private EditText input2;
    private String data_file;

    private final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() :
            "/mnt/sdcard";//保存到SD卡
    private final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/Density_Predict/usrData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_func);

        input1 = (EditText)findViewById(R.id.input1);
        input2 = (EditText)findViewById(R.id.input2);
        con_btn = (Button)findViewById(R.id.con_btn);

        con_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double k = Double.valueOf(input1.getText().toString());
                    Double b = Double.valueOf(input2.getText().toString());

                    Intent it = getIntent();
                    data_file = it.getStringExtra("usr") + ".txt";

                    saveFile(data_file,k,b,25,88);

                    Intent intent = new Intent();
                    intent.setClass(set_func.this, getPic.class);
                    intent.putExtra("k",k);
                    intent.putExtra("b",b);
                    startActivity(intent);
                } catch (NumberFormatException e) {
                    Toast.makeText(set_func.this,"两参数应为小数",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public String saveFile(String fileName,double k,double b,double px3,double px4) {
        String subForder = SAVE_REAL_PATH;
        File folder = new File(subForder);
        if (!folder.exists()) {
            Boolean bl = folder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (!myCaptureFile.exists()) {
            try {
                myCaptureFile.createNewFile();
                String data = "-0.28790104"+"="+"24.883634395270192"+"="+"25"+"="+"88" + '\n';
                FileWriter fileWritter = new FileWriter(myCaptureFile.getAbsolutePath(),true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write(data);
                bufferWritter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            String data = k+"="+b+"="+px3+"="+px4 + '\n';
            FileWriter fileWritter = new FileWriter(myCaptureFile.getAbsolutePath(),true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(data);
            bufferWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "模型存储成功，路径:" + myCaptureFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        return myCaptureFile.getAbsolutePath();
    }
}
