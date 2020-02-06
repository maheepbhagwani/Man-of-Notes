package com.example.meep.firebase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class upload extends Activity {
    final static int PICK_PDF_CODE = 2342;
    TextView textViewStatus;
    EditText editTextFilename;
    ProgressBar progressBar;
    Button b;
    TextView t;
    //the firebase objects for storage and database
    StorageReference mStorageReference;

    DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        //getting firebase objects
        try{
            mStorageReference = FirebaseStorage.getInstance().getReference();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference(Const.DATABASE_PATH_UPLOADS);
        }

        catch (Exception e)
        {
            editTextFilename.setText(""+e);
        }
        //getting the views
        try {
            textViewStatus = (TextView) findViewById(R.id.textViewStatus);
            editTextFilename = (EditText) findViewById(R.id.editTextFileName);
            progressBar = (ProgressBar) findViewById(R.id.progressbar);
        }
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(),"Error: "+e,Toast.LENGTH_LONG).show();
        }
        //attaching listeners to views
        t=(TextView)findViewById(R.id.textViewUploads);
        b=(Button)findViewById(R.id.buttonUploadFile);

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ViewUploads.class));
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPDF();
            }
        });
    }


    private void getPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadFile(Uri data) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference sRef = mStorageReference.child(Const.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf");
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            textViewStatus.setText("File Uploaded Successfully");

                            Uploads upload = new Uploads(editTextFilename.getText().toString(), sRef.getDownloadUrl().toString());
                            mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            textViewStatus.setText((int) progress + "% Uploading...");
                        }
                    });
        }
        catch (Exception e){ Toast.makeText(getBaseContext(),"error: "+e,Toast.LENGTH_LONG).show();}
    }
}
