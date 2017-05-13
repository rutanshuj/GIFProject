package com.example.admin.gifproject;

import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;


public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    android.hardware.Camera camera;
    android.hardware.Camera.Parameters parameters;
    SurfaceView surfaceView;
    ImageView gallery;
    Button capture, exit;
    android.hardware.Camera.PictureCallback pictureCallback;
    android.hardware.Camera.ShutterCallback shutterCallback;
    SurfaceHolder surfaceHolder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        exit = (Button) findViewById(R.id.btn_exit);
        capture = (Button) findViewById(R.id.btn_take_photo);
        gallery = (ImageView) findViewById(R.id.btn_lib);

        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        });
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        pictureCallback = new android.hardware.Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
                FileOutputStream outputStream = null;
                File file_img = getDirc();
                if(!file_img.exists() && !file_img.mkdirs()){
                    Toast.makeText(CameraActivity.this, "Can't create directory to save image", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                String date = simpleDateFormat.format(new Date());
                String photofile = "Cam_Demo" +date+".jpg";
                String file_name = file_img.getAbsolutePath() + "/" +photofile;
                File picfile = new File(file_name);
                try{
                    outputStream = new FileOutputStream(picfile);
                    outputStream.write(data);
                    outputStream.close();
                }catch (FileNotFoundException e){}
                catch(IOException ex){}
                finally {
                    Toast.makeText(CameraActivity.this, "Picture Saved", Toast.LENGTH_SHORT).show();
                    refreshCamera();
                    refreshGallery(picfile);
                }
            }
        };
    }

    public void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
    public void refreshCamera(){
        if(surfaceHolder.getSurface() == null){
            return;
        }
        try{
            camera.stopPreview();

        }catch(Exception e){

        }//Set preview and make any resize, rotate or
        //reformatting changes here
        //starts preview with new settings

        try{
            camera.setPreviewDisplay(surfaceHolder);
        }catch (Exception e){}
    }
    private File getDirc(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(dir, "Camera_Demo");
    }

    public void captureImage() {
        camera.takePicture(null, null, pictureCallback);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            camera = android.hardware.Camera.open();
            parameters = camera.getParameters();

            parameters.setPreviewFrameRate(20);
            parameters.setPreviewSize(352, 288);

            camera.setParameters(parameters);
            camera.setDisplayOrientation(90);

            try{
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch(RuntimeException e){}
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}
