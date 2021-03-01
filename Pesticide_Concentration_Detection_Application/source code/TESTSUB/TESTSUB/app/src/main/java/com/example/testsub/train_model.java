package com.example.testsub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class train_model extends AppCompatActivity {

    private final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() :
            "/mnt/sdcard";//保存到SD卡
    private final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/Density_Predict/usrData";

    private Button choiceFromAlbumButton = null;
    private Button addButton = null;
    private Button jpButton = null;
    private ImageView pictureImageView = null;
    private TextView tv1 = null;
    private EditText input = null;
    private String data_file;

    public ArrayList<Double> listx = new ArrayList<>();
    public ArrayList<Double> listy = new ArrayList<>();

    private double gray_sacle;

    private static final int WRITE_SDCARD_PERMISSION_REQUEST_CODE = 1; // 读储存卡内容的权限处理返回码

    private static final int TAKE_PHOTO_REQUEST_CODE = 3; // 拍照返回的 requestCode
    private static final int CHOICE_FROM_ALBUM_REQUEST_CODE = 4; // 相册选取返回的 requestCode
    private static final int CROP_PHOTO_REQUEST_CODE = 5; // 裁剪图片返回的 requestCode

    private Uri photoUri = null;
    private Uri photoOutputUri = null; // 图片最终的输出文件的 Uri


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_model);


        choiceFromAlbumButton = (Button) findViewById(R.id.choiceFromAlbumButton);
        choiceFromAlbumButton.setOnClickListener(clickListener);
        addButton = (Button) findViewById(R.id.add_btn);
        addButton.setOnClickListener(clickListener);
        jpButton = (Button) findViewById(R.id.jump_btn);
        jpButton.setOnClickListener(clickListener);

        input = (EditText) findViewById(R.id.input);

        pictureImageView = (ImageView) findViewById(R.id.pictureImage);
        tv1 = (TextView) findViewById(R.id.tv1);

        Intent intent = getIntent();
        data_file = intent.getStringExtra("usr") + ".txt";

        if (ContextCompat.checkSelfPermission(train_model.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请读写内存卡内容的权限
            ActivityCompat.requestPermissions(train_model.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_SDCARD_PERMISSION_REQUEST_CODE);
        } else if (ContextCompat.checkSelfPermission(train_model.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(train_model.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 调用相机拍照
            if (v == choiceFromAlbumButton) {
                choiceFromAlbum();
            } else if (v == addButton) {
                try {
                    Double density = Double.parseDouble(input.getText().toString());
                    tv1.setText("灰度值为:" + gray_sacle + '\n' + "浓度值:" + density);
                    input.setText("");
                    listx.add(gray_sacle);
                    listy.add(density);
                    choiceFromAlbumButton.setText("继续选择图片");
                    Toast.makeText(train_model.this, "已有数据：" + listx.size() + "条", Toast.LENGTH_LONG).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(train_model.this, "浓度应为小数", Toast.LENGTH_LONG).show();
                }
            } else if (v == jpButton) {
                if (listx.size() > 1) {
                    Double[] x = listx.toArray(new Double[listx.size()]);
                    Double[] y = listy.toArray(new Double[listy.size()]);
                    double a = getFuncUtil.getA(x, y);
                    double b = getFuncUtil.getB(x, y);

                    saveFile(data_file,a,b, getFuncUtil.ListDoubleArrayMin(listx), getFuncUtil.ListDoubleArrayMax(listx));

                    Intent intent = new Intent();
                    intent.setClass(train_model.this, getPic.class);
                    intent.putExtra("k", a);
                    intent.putExtra("b", b);
                    intent.putExtra("px3", getFuncUtil.ListDoubleArrayMin(listx));
                    intent.putExtra("px4", getFuncUtil.ListDoubleArrayMax(listx));
                    startActivity(intent);
                } else {
                    Toast.makeText(train_model.this, "数据应不少于两条", Toast.LENGTH_LONG).show();
                }

            }
        }
    };

    /**
     * 从相册选取
     */
    private void choiceFromAlbum() {
        // 打开系统图库的 Action，等同于: "android.intent.action.GET_CONTENT"
        Intent choiceFromAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        // 设置数据类型为图片类型
        choiceFromAlbumIntent.setType("image/*");
        startActivityForResult(choiceFromAlbumIntent, CHOICE_FROM_ALBUM_REQUEST_CODE);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri inputUri) {
        // 调用系统裁剪图片的 Action
        Intent cropPhotoIntent = new Intent("com.android.camera.action.CROP");
        // 设置数据Uri 和类型
        cropPhotoIntent.setDataAndType(inputUri, "image/*");
        // 授权应用读取 Uri，这一步要有，不然裁剪程序会崩溃
        cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 设置图片的最终输出目录
        cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                photoOutputUri = Uri.parse("file:////sdcard/image_output.jpg"));
        startActivityForResult(cropPhotoIntent, CROP_PHOTO_REQUEST_CODE);
    }

    /**
     * 在这里进行用户权限授予结果处理
     *
     * @param requestCode  权限要求码，即我们申请权限时传入的常量
     * @param permissions  保存权限名称的 String 数组，可以同时申请一个以上的权限
     * @param grantResults 每一个申请的权限的用户处理结果数组(是否授权)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            // 打开相册选取：
            case WRITE_SDCARD_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "读写内存卡内容权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 通过这个 activity 启动的其他 Activity 返回的结果在这个方法进行处理
     * 我们在这里对拍照、相册选择图片、裁剪图片的返回结果进行处理
     *
     * @param requestCode 返回码，用于确定是哪个 Activity 返回的数据
     * @param resultCode  返回结果，一般如果操作成功返回的是 RESULT_OK
     * @param data        返回对应 activity 返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 通过返回码判断是哪个应用返回的数据
            switch (requestCode) {
                // 拍照
                case TAKE_PHOTO_REQUEST_CODE:
                    cropPhoto(photoUri);
                    break;
                // 相册选择
                case CHOICE_FROM_ALBUM_REQUEST_CODE:
                    cropPhoto(data.getData());
                    break;
                // 裁剪图片
                case CROP_PHOTO_REQUEST_CODE:
                    File file = new File(photoOutputUri.getPath());
                    if (file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(photoOutputUri.getPath());
                        pictureImageView.setImageBitmap(bitmap);
                        getPicturePixel(bitmap);
//                        file.delete(); // 选取完后删除照片
                    } else {
                        Toast.makeText(this, "找不到照片", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void getPicturePixel(Bitmap bitmap) {
        addButton.setVisibility(View.VISIBLE);
        jpButton.setVisibility(View.VISIBLE);
        input.setVisibility(View.VISIBLE);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // 保存所有的像素的数组，图片宽×高
        int[] pixels = new int[width * height];

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int aver_r = 0;
        int aver_g = 0;
        int aver_b = 0;
        for (int i = 0; i < pixels.length; i++) {
            int clr = pixels[i];
            int red = (clr & 0x00ff0000) >> 16; // 取高两位
            int green = (clr & 0x0000ff00) >> 8; // 取中两位
            int blue = clr & 0x000000ff; // 取低两位
            aver_r += red;
            aver_g += green;
            aver_b += blue;
            //Log.d("tag", "r=" + red + ",g=" + green + ",b=" + blue);
        }
        aver_r /= width * height;
        aver_g /= width * height;
        aver_b /= width * height;
        gray_sacle = 0.3 * aver_r + 0.59 * aver_g + 0.11 * aver_b;

        //tv1.setText(aver_r+","+aver_g+","+aver_b+","+gray_sacle+","+density);
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