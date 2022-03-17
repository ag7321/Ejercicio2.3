package com.example.ejercicio23;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ejercicio23.configuraciones.SQLiteConexion;
import com.example.ejercicio23.configuraciones.Transacciones;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText txtDescripcion;
    Button btnSalvar,btnVer;
    ImageView imageView;
    static final int PETICION_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;
    String currentPhotoPath;
    Bitmap imgUser;
    SQLiteConexion conexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);

        txtDescripcion = (EditText) findViewById(R.id.textDescripcion);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnVer = (Button) findViewById(R.id.btnVerPhotograh);
        imageView = (ImageView) findViewById(R.id.imageViewPhotograh);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                permisos();
                return false;
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePhotograh();
            }
        });

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), viewPhotograh.class);
                startActivity(intent);
            }
        });



    }

    private void savePhotograh() {

        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Transacciones.descripcion, txtDescripcion.getText().toString());

        ByteArrayOutputStream bay = new ByteArrayOutputStream(10480);

        imgUser.compress(Bitmap.CompressFormat.JPEG, 0 , bay);

        byte[] bl = bay.toByteArray();

        String img= Base64.encodeToString(bl,Base64.DEFAULT);

        values.put(Transacciones.imagen, img);

        Long result = db.insert(Transacciones.tablaPhotograh, Transacciones.id, values);

        Toast.makeText(getApplicationContext(), "Photograh guardada correctamente"
                ,Toast.LENGTH_LONG).show();

        db.close();
    }

    private void permisos(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_CAM);
        }else{
            dispatchTakePictureIntent();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PETICION_CAM){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Se necesitan permisos a la camara", Toast.LENGTH_LONG).show();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.toString();
            }
            // Continue only if the File was successfully created
            try {
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.ejercicio23.fileprovider",
                            photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takePictureIntent, TAKE_PIC_REQUEST);
                }
            }catch (Exception e){
                Log.i("Error", "dispatchTakePictureIntent: " + e.toString());
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK){

            Bitmap image = BitmapFactory.decodeFile(currentPhotoPath);

            imgUser = image;
            imageView.setImageBitmap(image);
        }
    }
}