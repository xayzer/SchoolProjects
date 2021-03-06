package com.example.testsub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class getPic extends AppCompatActivity {

    private Button startCameraButton = null;
    private Button choiceFromAlbumButton = null;
    private Button jpButton = null;
    private ImageView pictureImageView = null;
    private TextView tv1 = null;

    private double k;
    private double b;
    private double px3;
    private double px4;
    private double gray_sacle;
    private double density;

    private static final int TAKE_PHOTO_PERMISSION_REQUEST_CODE = 0; // 拍照的权限处理返回码
    private static final int WRITE_SDCARD_PERMISSION_REQUEST_CODE = 1; // 读储存卡内容的权限处理返回码

    private static final int TAKE_PHOTO_REQUEST_CODE = 3; // 拍照返回的 requestCode
    private static final int CHOICE_FROM_ALBUM_REQUEST_CODE = 4; // 相册选取返回的 requestCode
    private static final int CROP_PHOTO_REQUEST_CODE = 5; // 裁剪图片返回的 requestCode

    private Uri photoUri = null;
    private Uri photoOutputUri = null; // 图片最终的输出文件的 Uri


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pic);

        startCameraButton = (Button) findViewById(R.id.startCameraButton);
        startCameraButton.setOnClickListener(clickListener);
        choiceFromAlbumButton = (Button) findViewById(R.id.choiceFromAlbumButton);
        choiceFromAlbumButton.setOnClickListener(clickListener);
        jpButton = (Button)findViewById(R.id.jp_btn);
        jpButton.setOnClickListener(clickListener);

        Intent it = getIntent();

        k=it.getDoubleExtra("k",0);
        b=it.getDoubleExtra("b",0);
        px3= it.getDoubleExtra("px3",25);
        px4= it.getDoubleExtra("px4",88);

        pictureImageView = (ImageView) findViewById(R.id.pictureImage);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText("模型K值"+k+'\n'+"模型B值"+b);

        if (ContextCompat.checkSelfPermission(getPic.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请读写内存卡内容的权限
            ActivityCompat.requestPermissions(getPic.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_SDCARD_PERMISSION_REQUEST_CODE);
        } else if(ContextCompat.checkSelfPermission(getPic.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getPic.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 调用相机拍照
            if (v == startCameraButton) {
                // 同上面的权限申请逻辑
                if (ContextCompat.checkSelfPermission(getPic.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    /*
                     * 下面是对调用相机拍照权限进行申请
                     */
                    ActivityCompat.requestPermissions(getPic.this,
                            new String[]{Manifest.permission.CAMERA,}, TAKE_PHOTO_PERMISSION_REQUEST_CODE);
                } else{
                    startCamera();
                }
                // 从相册获取
            } else if (v == choiceFromAlbumButton) {
                choiceFromAlbum();
            }else if(v==jpButton){
                Intent intent = new Intent();
                intent.setClass(getPic.this, drawResult.class);
                intent.putExtra("density",density);
                intent.putExtra("gray_scale",gray_sacle);
                intent.putExtra("k",k);
                intent.putExtra("b",b);
                intent.putExtra("px3",px3);
                intent.putExtra("px4",px4);
                startActivity(intent);
            }
        }
    };

    /**
     * 拍照
     */
    private void startCamera() {
        /**
         * 设置拍照得到的照片的储存目录，因为我们访问应用的缓存路径并不需要读写内存卡的申请权限，
         * 因此，这里为了方便，将拍照得到的照片存在这个缓存目录中
         */
        File file = new File(getExternalCacheDir(), "image.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 因 Android 7.0 开始，不能使用 file:// 类型的 Uri 访问跨应用文件，否则报异常，
         * 因此我们这里需要使用内容提供器，FileProvider 是 ContentProvider 的一个子类，
         * 我们可以轻松的使用 FileProvider 来在不同程序之间分享数据(相对于 ContentProvider 来说)
         */
        if (Build.VERSION.SDK_INT >= 24) {
            photoUri = FileProvider.getUriForFile(this, "com.zhi_dian.provider", file);
        } else {
            photoUri = Uri.fromFile(file); // Android 7.0 以前使用原来的方法来获取文件的 Uri
        }
        // 打开系统相机的 Action，等同于："android.media.action.IMAGE_CAPTURE"
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 设置拍照所得照片的输出目录
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST_CODE);
    }

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
            // 调用相机拍照：
            case TAKE_PHOTO_PERMISSION_REQUEST_CODE:
                // 如果用户授予权限，那么打开相机拍照
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
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
        jpButton.setVisibility(View.VISIBLE);
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
        aver_r/=width*height;
        aver_g/=width*height;
        aver_b/=width*height;
        gray_sacle= 0.3*aver_r + 0.59*aver_g + 0.11*aver_b;
        density = k*gray_sacle+b;

        //tv1.setText(aver_r+","+aver_g+","+aver_b+","+gray_sacle+","+density);
    }
}
