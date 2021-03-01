package com.example.testsub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class choose_model extends AppCompatActivity {

    private Button exist_btn;
    private Button manual_btn;
    private Button train_btn;
    private Button func_btn;
    private String usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_model);

        exist_btn = (Button) findViewById(R.id.exist_btn);
        manual_btn = (Button) findViewById(R.id.manual_btn);
        train_btn = (Button) findViewById(R.id.train_btn);
        func_btn= (Button)findViewById(R.id.func_btn);

        exist_btn.setOnClickListener(clickListener);
        manual_btn.setOnClickListener(clickListener);
        train_btn.setOnClickListener(clickListener);
        func_btn.setOnClickListener(clickListener);

        Intent intent = getIntent();
        usr = intent.getStringExtra("usr");

        if (ContextCompat.checkSelfPermission(choose_model.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请读写内存卡内容的权限
            ActivityCompat.requestPermissions(choose_model.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else if (ContextCompat.checkSelfPermission(choose_model.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(choose_model.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == exist_btn) {
                Intent intent = new Intent();
                intent.setClass(choose_model.this, exist_model.class);
                intent.putExtra("usr",usr);
                startActivity(intent);

            } else if (v == manual_btn) {
                Intent intent = new Intent();
                intent.setClass(choose_model.this, LeastSquareMethod.class);
                intent.putExtra("usr",usr);
                startActivity(intent);
            } else if (v == train_btn) {
                Intent intent = new Intent();
                intent.setClass(choose_model.this, train_model.class);
                intent.putExtra("usr",usr);
                startActivity(intent);
            }else if(v==func_btn){
                Intent intent = new Intent();
                intent.setClass(choose_model.this, set_func.class);
                intent.putExtra("usr",usr);
                startActivity(intent);
            }
        }
    };
}
