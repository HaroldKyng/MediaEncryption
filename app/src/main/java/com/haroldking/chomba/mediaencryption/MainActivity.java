package com.haroldking.chomba.mediaencryption;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {
    Button encrypt, decrypt;
    ImageView imageView;
    String FILE_NAME_ENCRYPTED = "my_pic_encrypted";
    String FILE_NAME_DECRYPTED = "my_pic_decrypted.jpg";
    File myDir;
//KEY - In this Example will be hardcoded in code
    //in production app you can save it on Firebase / API and getwhen runtime
    String my_key="ltVkg0knCiDc9K80";//16 char = 128 bit
    String my_spec_key="BentH1dIPoOEawVa";//tod
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        encrypt = findViewById(R.id.encrypt);
        decrypt = findViewById(R.id.dencrypt);
        //init path
        myDir = new File(Environment.getExternalStorageDirectory().toString() + "/saved_images");
        Dexter.withActivity(this).withPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                encrypt.setEnabled(true);
                decrypt.setEnabled(true);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Toast.makeText(getApplicationContext(), "You must enable Permissions", Toast.LENGTH_SHORT).show();
            }
        }).check();


        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Convert Drawable to Bitmap
                Drawable drawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.my_pic);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                InputStream is = new ByteArrayInputStream(stream.toByteArray());

//                Create file

                File outputFileEncrypted = new File(myDir, FILE_NAME_ENCRYPTED);
                try{
                    MyEncrypter.encryptToFile(my_key,my_spec_key,is, new FileOutputStream(outputFileEncrypted));
                    Toast.makeText(getApplicationContext(), "Encrypted!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

            }
        });
        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputFileDecrypted = new File(myDir, FILE_NAME_DECRYPTED);
                File encFile = new File(myDir, FILE_NAME_ENCRYPTED);
                try{
                    MyEncrypter.decryptToFile(my_key,my_spec_key, new FileInputStream(encFile),
                            new FileOutputStream(outputFileDecrypted));
                       //After that, set for image view
                    imageView.setImageURI(Uri.fromFile(outputFileDecrypted));

                    // if you want to delete file after decryption, just keep this line
                    outputFileDecrypted.delete();

                    Toast.makeText(getApplicationContext(), "Decrypted!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }

            }
        });


    }
}
