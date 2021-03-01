package com.example.testsub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class drawResult extends AppCompatActivity {

    ImageView iv;
    private final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() :
            "/mnt/sdcard";//保存到SD卡
    private final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/Density_Predict/savePic";
    private Paint mTextPaint;
    private Paint mRedPaint;
    private Paint mGreenPaint;
    private Paint mBluePaint;
    private Paint mGridPaint;
    private Point mWinSize;//屏幕尺寸
    private Point mCoo;//坐标系原点
    private Button sh_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_result);

        iv = findViewById(R.id.iv);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }

        Bitmap bitmap = Bitmap.createBitmap(outMetrics.widthPixels, outMetrics.heightPixels, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);
        Canvas canvas = new Canvas(bitmap);
        iv.setImageBitmap(bitmap);

        Intent intent = getIntent();
        double k = intent.getDoubleExtra("k", 0);
        double b = intent.getDoubleExtra("b", 0);
        double px3 = intent.getDoubleExtra("px3", 25);
        double px4 = intent.getDoubleExtra("px4", 88);

        double density = intent.getDoubleExtra("density", 0);
        double gray_scale = intent.getDoubleExtra("gray_scale", 0);

        init();

        drawGrid(canvas, mWinSize, mGridPaint);
        drawCoo(canvas, mCoo, mWinSize, mGridPaint);
        drawPoint(canvas, k, b, gray_scale);
        drawLine(canvas, k, b, px3, px4);
        canvas.save();

        Random random = new Random();
        final String loc = saveFile(bitmap, "MyDen_" + random.nextInt() + ".png");

        sh_btn = (Button) findViewById(R.id.sharebtn);
        sh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMsg("Main2Activity", loc);
            }
        });


    }

    private void init() {
        mWinSize = new Point();
        mCoo = new Point(250, 1500);
        mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedPaint.setColor(Color.RED);
        mRedPaint.setStrokeWidth(10);
        mBluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBluePaint.setColor(Color.BLUE);
        mBluePaint.setStrokeWidth(10);
        mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGreenPaint.setColor(Color.DKGRAY);
        mGreenPaint.setStrokeWidth(20);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.DKGRAY);
        mTextPaint.setStrokeWidth(10);
        loadWinSize(drawResult.this, mWinSize);
    }

    public static void loadWinSize(Context ctx, Point winSize) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        winSize.x = outMetrics.widthPixels;
        winSize.y = outMetrics.heightPixels;
    }

    public static void drawGrid(Canvas canvas, Point winSize, Paint paint) {
        //初始化网格画笔
        paint.setStrokeWidth(2);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
        canvas.drawPath(gridPath(50, winSize), paint);
    }

    public static Path gridPath(int step, Point winSize) {
        Path path = new Path();
        for (int i = 0; i < winSize.y / step + 1; i++) {
            path.moveTo(0, step * i);
            path.lineTo(winSize.x, step * i);
        }
        for (int i = 0; i < winSize.x / step + 1; i++) {
            path.moveTo(step * i, 0);
            path.lineTo(step * i, winSize.y);
        }
        return path;
    }

    public static Path cooPath(Point coo, Point winSize) {
        Path path = new Path();
        //x正半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(winSize.x, coo.y);
        //x负半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x - winSize.x, coo.y);
        //y负半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x, coo.y - winSize.y);
        //y负半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x, winSize.y);
        return path;
    }

    public static void drawCoo(Canvas canvas, Point coo, Point winSize, Paint paint) {
        //初始化网格画笔
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        paint.setPathEffect(null);
        //绘制直线
        canvas.drawPath(cooPath(coo, winSize), paint);
        //左箭头
        canvas.drawLine(winSize.x, coo.y, winSize.x - 40, coo.y - 20, paint);
        canvas.drawLine(winSize.x, coo.y, winSize.x - 40, coo.y + 20, paint);
        //下箭头
        canvas.drawLine(coo.x, winSize.y, coo.x - 20, winSize.y - 40, paint);
        canvas.drawLine(coo.x, winSize.y, coo.x + 20, winSize.y - 40, paint);
        //为坐标系绘制文字
        drawText4Coo(canvas, coo, winSize, paint);
    }

    private static void drawText4Coo(Canvas canvas, Point coo, Point winSize, Paint paint) {
        //绘制文字
        paint.setTextSize(50);
        canvas.drawText("x", winSize.x - 60, coo.y - 40, paint);
        canvas.drawText("y", coo.x - 40, winSize.y - 60, paint);
        paint.setTextSize(25);
        //X正轴文字
        for (int i = 1; i < (winSize.x - coo.x) / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(12.5 * i + "", coo.x - 20 + 100 * i, coo.y + 40, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x + 100 * i, coo.y, coo.x + 100 * i, coo.y - 10, paint);
        }
        //X负轴文字
        for (int i = 1; i < coo.x / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(-12.5 * i + "", coo.x - 20 - 100 * i, coo.y + 40, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x - 100 * i, coo.y, coo.x - 100 * i, coo.y - 10, paint);
        }
        //y正轴文字
        for (int i = 1; i < (winSize.y - coo.y) / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(-2 * i + "", coo.x + 20, coo.y + 10 + 100 * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x, coo.y + 100 * i, coo.x + 10, coo.y + 100 * i, paint);
        }
        //y负轴文字
        for (int i = 1; i < coo.y / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(2 * i + "", coo.x + 20, coo.y + 10 - 100 * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x, coo.y - 100 * i, coo.x + 10, coo.y - 100 * i, paint);
        }
    }

    private void drawPoint(Canvas canvas, double k, double b, double x) {
        //绘制点
        double px = x;
        double py = k * px + b;
        canvas.drawPoint((float) (mCoo.x + px * 8), (float) (mCoo.y - py * 50), mGreenPaint);
        drawText(canvas, px, py);
    }

    private void drawLine(Canvas canvas, double k, double b, double px3, double px4) {
        double px1 = -25;
        double px2 = 100;

        double py1 = k * px1 + b;
        double py2 = k * px2 + b;
        double py3 = k * px3 + b;
        double py4 = k * px4 + b;
        canvas.drawLine((float) (mCoo.x + px1 * 8), (float) (mCoo.y - py1 * 50), (float) (mCoo.x + px2 * 8), (float) (mCoo.y - py2 * 50), mRedPaint);
        canvas.drawLine((float) (mCoo.x + px3 * 8), (float) (mCoo.y - py3 * 50), (float) (mCoo.x + px4 * 8), (float) (mCoo.y - py4 * 50), mBluePaint);
        //绘制一组点，坐标位置由float数组指定(必须是4的倍数个)

    }

    private void drawText(Canvas canvas, double px, double py) {
        mTextPaint.setTextSize(30);
        canvas.drawText("温馨提示，蓝色范围为相对有效范围", 350, 100, mTextPaint);
        canvas.drawText("超出范围测试结果误差可能较大", 350, 150, mTextPaint);
        mTextPaint.setTextSize(50);
        canvas.drawText("灰度值:" + px, 400, 400, mTextPaint);
        canvas.drawText(" 农药预测浓度约:" + (float) py, 400, 500, mTextPaint);
    }

    public String saveFile(Bitmap bm, String fileName) {
        String subForder = SAVE_REAL_PATH;
        File folder = new File(subForder);
        if (!folder.exists()) {
            Boolean bl = folder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (!myCaptureFile.exists()) {
            try {
                myCaptureFile.createNewFile();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = Uri.fromFile(new File(myCaptureFile.getAbsolutePath()));
        intent.setData(uri);
        drawResult.this.sendBroadcast(intent);

        Toast.makeText(this, "保存成功,路径:" + myCaptureFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        return myCaptureFile.getAbsolutePath();
    }

    public void shareMsg(String activityTitle, String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/png");
                Uri u = FileProvider.getUriForFile(
                        this,
                        "com.zhi_dian.provider",
                        f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));

    }
}
