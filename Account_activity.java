package com.example.fypapplication;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account_activity extends AppCompatActivity {
Button edit;
    private CircleImageView profileImageView;
    TextView pass1, email2,phone1, logout;
    Button notifications;
    TextView verifyMsg,Changeprofileimage, Editprofile;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    String userId;
    String useremail,userphoneno,userpassword;
    DatabaseReference databaseReference;
    Button resendCode, resendPassLocal;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_activity);
        logout=findViewById(R.id.logout);
        profileImageView= findViewById(R.id.dp);
        notifications=findViewById(R.id.notify);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel("My Notifications","My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder builder= new NotificationCompat.Builder(Account_activity.this,"My Notifications")
                        .setContentTitle("Get 20 % off on 4 Guards")
                        .setContentText("DeppKins provide Special discount on hiring 4 guards for your area...")
                        .setSmallIcon(R.drawable.service)
                        .setAutoCancel(true);
                NotificationManagerCompat managerCompat =NotificationManagerCompat.from(Account_activity.this);
                managerCompat.notify(999,builder.build());
            }
        });
       email2=findViewById(R.id.name);
        Editprofile=findViewById(R.id.editprofile);
        phone1=findViewById(R.id.email1);
        Changeprofileimage= findViewById(R.id.editphoto);
        pass1=findViewById(R.id.phone);
        fAuth= FirebaseAuth.getInstance();
        storageReference=FirebaseStorage.getInstance().getReference();
        fstore=FirebaseFirestore.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();

        Changeprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });
      readData();


Editprofile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(v.getContext(),EditProfileGuard.class);
        i.putExtra("email2",email2.getText().toString());
        i.putExtra("phone1",phone1.getText().toString());
        i.putExtra("pass1",pass1.getText().toString());
        startActivity(i);
    }
});
logout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent p = new Intent(Account_activity.this,UsersActivity.class);
        startActivity(p);
    }
});
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation2);
        bottomNav.setSelectedItemId(R.id.nav_useraccount);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),UserDashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_guard:
                        startActivity(new Intent(getApplicationContext(),GuardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_useraccount:
                        return true;
                }
                return false;
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri=data.getData();
                profileImageView.setImageURI(imageUri);
                
                uploadImagetoFirebase(imageUri);
            }
        }
    }

    private void uploadImagetoFirebase(Uri imageUri) {
        // upload image to firebase storage
        final StorageReference fileRef= storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Account_activity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageView);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Account_activity.this, "Operation Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readData() {
        userId=user.getUid();
        DatabaseReference roorRef= FirebaseDatabase.getInstance().getReference("Users");
        roorRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                useremail=dataSnapshot.child("email1").getValue(String.class);
                email2.setText(useremail);
                userphoneno=dataSnapshot.child("num").getValue(String.class);
                phone1.setText(userphoneno);
                userpassword=dataSnapshot.child("passsord").getValue(String.class);
                pass1.setText(userpassword);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
