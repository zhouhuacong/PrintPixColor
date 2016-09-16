package com.zhc.printpixcolor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhouhuacong
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0x100;

    private ImageView mImgPic;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImgPic = (ImageView) findViewById(R.id.img_pic);
        findViewById(R.id.btn_take_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                // 为兼容android6.0; 动态获取Camera权限
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 3);
                } else {
                    startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });
    }

    /**
     * 获取存储地址
     *
     * @return uri
     */
    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * 获取file
     *
     * @return file
     */
    private static File getOutputMediaFile() {
        File picPath = null;
        try {
            picPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "PrintPixColorDemo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (picPath != null && !picPath.exists()) {
            if (!picPath.mkdirs()) {
                return null;
            }
        }
        // 文件名加日期
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return new File(picPath.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE == requestCode) {
            Log.d(TAG, "CAPTURE_IMAGE");

            if (RESULT_OK == resultCode) {
                Log.d(TAG, "RESULT_OK");

                if (data != null) {
                    if (data.hasExtra("data")) {
                        Bitmap thumbnail = data.getParcelableExtra("data");
                        mImgPic.setImageBitmap(thumbnail);
                    }
                } else {
                    // 适应imageView
                    int width = mImgPic.getWidth();
                    int height = mImgPic.getHeight();

                    BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

                    factoryOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);

                    int imageWidth = factoryOptions.outWidth;
                    int imageHeight = factoryOptions.outHeight;

                    int scaleFactor = Math.min(imageWidth / width, imageHeight / height);

                    factoryOptions.inJustDecodeBounds = false;
                    factoryOptions.inSampleSize = scaleFactor;
                    factoryOptions.inPurgeable = true;

                    final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                            factoryOptions);
                    if (bitmap != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PixUtil.getPixColorByBitmap(bitmap);
                            }
                        }).start();
                        mImgPic.setImageBitmap(bitmap);
                    }
                }
            }
        }

    }
}
