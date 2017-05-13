package com.example.admin.gifproject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    Button b1;
    private Button device, drive;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.capture);

        imageView = (ImageView) findViewById(R.id.picview);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, CAMERA_REQUEST);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                AnimatedGifEncoder encoder = new AnimatedGifEncoder();
                encoder.setDelay(500);  // ディレイ 500/ms
                encoder.setRepeat(0);   // 0:ループする -1:ループしない
                encoder.start(bos);     // gitデータ生成先ををbosに設定

                try {
                    Bitmap bmp1, bmp2, bmp3;

                    // ファイルの読み込み
                    bmp1 = BitmapFactory.decodeStream(new FileInputStream("/sdcard/target1.png"));
                    encoder.addFrame(bmp1);  // gifに追加
                    bmp1.recycle();

                    bmp2 = BitmapFactory.decodeStream(new FileInputStream("/sdcard/target2.png"));
                    encoder.addFrame(bmp2);  // gifに追加
                    bmp2.recycle();

                    bmp3= BitmapFactory.decodeStream(new FileInputStream("/sdcard/target3.png"));
                    encoder.addFrame(bmp3);  // gifに追加
                    bmp3.recycle();

                } catch (FileNotFoundException e) {
                }

                encoder.finish();  // 終了

                File filePath = new File("/sdcard", "sample.gif");
                FileOutputStream outputStream;
                try {
                    outputStream = new FileOutputStream(filePath);
                    // bosに生成されたgifデータをファイルに吐き出す
                    outputStream.write(bos.toByteArray());
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            showDialog();
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.customdialog);
        dialog.setTitle("What's next");


        //Setting dialog components

        TextView textView = (TextView) dialog.findViewById(R.id.textView);
        Button device = (Button) dialog.findViewById(R.id.onDevice);
        Button drive = (Button) dialog.findViewById(R.id.onDrive);


        device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();
    }
}
