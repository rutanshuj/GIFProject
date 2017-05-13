package com.example.admin.gifproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

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

                Intent intent = new Intent(MainActivity.this,  VideoServer.class);
                startActivity(intent);
//                Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
//
////                  File picDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
////                String picName = getPictureName();
////                File imageFile = new File(picDirectory, picName);
////                Uri uri = Uri.fromFile(imageFile);
////                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                AnimatedGifEncoder encoder = new AnimatedGifEncoder();
                encoder.setDelay(500);
                encoder.setRepeat(0);
                encoder.start(bos);

                try {
                    Bitmap bmp1, bmp2, bmp3;


                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.ic_launcher);
                    encoder.addFrame(bitmap);
                    bitmap.recycle();


                    Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.images);
                    encoder.addFrame(bitmap1);
                    bitmap.recycle();

                    Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.mm);
                    encoder.addFrame(bitmap2);



                } catch (Exception e) {
                }

                encoder.finish();

                String fileName = "note.gif";

                File extStore = Environment.getExternalStorageDirectory();
                // ==> /storage/emulated/0/note.txt
                String path = extStore.getAbsolutePath() + "/" + fileName;
                Log.i("ExternalStorageDemo", "Save to: " + path);


                    File myFile = new File(path, "hello.gif");
                try{
                    //myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);

                    fOut.write(bos.toByteArray());
                    fOut.close();
                    Toast.makeText(getApplicationContext(), fileName + " saved", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "gifImage" + timestamp + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            showDialog(photo);
        }
    }

    private void showDialog(Bitmap photo) {
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
