package com.example.meep.firebase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Button conv;
    EditText cat;
    ImageView img;
    //Uri object to store file path
    Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conv=(Button)findViewById(R.id.buttonconvert);
        cat=(EditText)findViewById(R.id.editText);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "some_channel_id";
        CharSequence channelName = "Some Channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        //attaching listener
        buttonChoose.setOnClickListener(this);
        conv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File sdcard = Environment.getExternalStorageDirectory();

                //Getting the text file
                String path=filePath.getPath();
                path=path.replaceFirst("document/","");
                path=path.replace("primary:","");
                File file = new File(sdcard,path);
                StringBuilder text = new StringBuilder();
                try {
                    String parsedText="";
                    String env=Environment.getExternalStorageDirectory().getAbsolutePath();
                    PdfReader reader;
                    reader = new PdfReader(sdcard+path);
                    int n = reader.getNumberOfPages();
                   if (n>=3)
                    {
                        n=3;
                        //only displaying 3 pages if document is big
                    }
                    for (int i = 0; i <n ; i++) {
                        parsedText   = parsedText+ PdfTextExtractor.getTextFromPage(reader, i+1).trim()+"\n";
                        Toast.makeText(getBaseContext(),"Successfully Converted PDF to Editable Text Format!",Toast.LENGTH_LONG).show();
                    }
                    reader.close();
                    System.out.println(parsedText);
                    cat.setText(parsedText);




                }
catch (Exception e)
{
    cat.setText("Location: "+path+"\n Error"+e);

}
            }
        });
    }
    private static final int PICK_IMAGE_REQUEST = 234;
    private Button buttonChoose;
    private Button buttonUpload;
    String name;
     private void showFileChooser() {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Docuement"), PICK_IMAGE_REQUEST);
        }

        //handling the image chooser activity result
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
            }
        }
        @Override
        public void onClick(View view) {
            if (view == buttonChoose) {
                showFileChooser();
            }
        }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

}



