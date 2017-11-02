package com.example.aayush.dualcamera;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, SurfaceHolder.Callback {

    SurfaceView surfaceView, surfaceView2;
    SurfaceHolder surfaceHolder, surfaceHolder2;
    ToggleButton toggleButton;
    Camera camera=null,camera2=null;
    int c=0;
    byte arr[]=null;
    byte arr2[]=null;
    byte []a1=null;
    byte []a2=null;
    Bitmap b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        surfaceView2 = (SurfaceView) this.findViewById(R.id.surfaceView2);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder2 = surfaceView2.getHolder();
        toggleButton = (ToggleButton) this.findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(this);
        surfaceHolder.addCallback(this);
        surfaceHolder2.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == surfaceHolder) {
            //camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            camera=Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder == surfaceHolder) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //camera.takePicture(null, null, picture);
            camera.takePicture(null,null,picture);
        }
        else {
                    camera2.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "DUOS_PICS");
                            if (file.exists()) {
                                String string = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.getDefault()).format(new Date());
                                File file1 = new File(file.getAbsolutePath() + "/FRONT_IMG" + string + ".JPEG");
                                try {
                                    FileOutputStream fos = new FileOutputStream(file1);
                                    try {
                                        fos.write(data);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    fos.close();
                                    Toast.makeText(MainActivity.this, "FILE SAVED:" + file1.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                    camera2.stopPreview();
                                    camera2.release();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }

    }
           Camera.PictureCallback picture =new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "DUOS_PICS");
                    if (!file.exists())
                        file.mkdir();
                    String string = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.getDefault()).format(new Date());
                    File file1 = new File(file.getAbsolutePath() + "/BACK_IMG" + string + ".JPEG");
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file1);
                        try {
                            fileOutputStream.write(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                            setData(data);
                        fileOutputStream.close();
                        Toast.makeText(MainActivity.this, "FILE SAVED:" + file1.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    camera.stopPreview();
                    try {
                        camera.release();
                    } catch (RuntimeException re){
                     re.printStackTrace();
                    }

                        camera2 = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                        try {
                            camera2.setPreviewDisplay(surfaceHolder2);
                            camera2.startPreview();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


           };

    private void setData(byte[] data) {
    }

}


