package com.example.testsub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class LeastSquareMethod extends AppCompatActivity {
    private final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() :
            "/mnt/sdcard";//保存到SD卡
    private final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/Density_Predict/usrData";

    private String data_file;
    private TextView tv = null;
    private Button btn1=null;
    private Button btn2=null;
    private EditText input1 = null;
    private EditText input2 = null;
    public ArrayList<Double> listx = new ArrayList<>();
    // 这里的listx与listy是两个可以不断累积的参数列表，在你后面使用图像识别获取到相应的x，y之后可以使用add加到
    //这两个list里面然后拟合成新的a，b。
    public ArrayList<Double> listy = new ArrayList<>();
    @Override
    /*
    * 使用最小二乘法进行拟合：y = a x + b
    */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_least_square_method);

        tv = findViewById(R.id.tv);
        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(listener);
        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(listener);
        input1= findViewById(R.id.input1);
        input2= findViewById(R.id.input2);

        Intent intent = getIntent();
        data_file = intent.getStringExtra("usr") + ".txt";
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btn1){
                try {
                    Double x = Double.parseDouble(input1.getText().toString());
                    Double y = Double.parseDouble(input2.getText().toString());
                    tv.setText(x+","+y);
                    listx.add(x);
                    listy.add(y);
                    input1.setText("");
                    input2.setText("");
                    Toast.makeText(LeastSquareMethod.this, "已有数据:"+listx.size()+"条", Toast.LENGTH_LONG).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(LeastSquareMethod.this,"灰度值和浓度应为小数",Toast.LENGTH_LONG).show();
                }

                //输入完成之后，清空输入框以便于下一次使用
            }
            if (view == btn2){
                if (listx.size()>1){
                    // 当按下立即拟合时，使用最小二乘法对已经获取到的参数集合进行拟合
                    Double[] x=listx.toArray(new Double[listx.size()]);
                    Double[] y=listy.toArray(new Double[listy.size()]);
                    double a= getFuncUtil.getA(x,y);
                    double b = getFuncUtil.getB(x,y);

                    saveFile(data_file,a,b, getFuncUtil.ListDoubleArrayMin(listx), getFuncUtil.ListDoubleArrayMax(listx));

                    Intent intent = new Intent();
                    intent.setClass(LeastSquareMethod.this, getPic.class);
                    intent.putExtra("k",a);
                    intent.putExtra("b",b);
                    intent.putExtra("px3", getFuncUtil.ListDoubleArrayMin(listx));
                    intent.putExtra("px4", getFuncUtil.ListDoubleArrayMax(listx));

                    startActivity(intent);

                }else {
                    Toast.makeText(LeastSquareMethod.this, "输入数据应不少于两条", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

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
