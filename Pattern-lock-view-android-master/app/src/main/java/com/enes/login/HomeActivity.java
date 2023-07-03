package com.enes.login;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnLogout;
    Button devam, btncek;
    FirebaseAuth mFirebaseAuth;
    TextView txtKullanici,t2,t3;
    String gelenmail;
    String nesne;
    final int beklemeSuresi = 5;


    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public void bekle(int sure) {
        try {
            // süre milisaniye cinsinden olduğu için 1000 ile çarpıyoruz.
            Thread.sleep(sure * 1000);
        } catch (InterruptedException e) {
            // Kesme işlemi (interrupt) olursa hata mesajı gösteriyoruz.
            System.err.println("Bekleme işlemi kesildi: " + e.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


       // btnLogout = findViewById(R.id.logout);
        devam=findViewById(R.id.buttondeneme);//devam et butonu

        txtKullanici=(TextView)findViewById(R.id.mailad);
        Intent gelenIntent=getIntent();
        gelenmail=gelenIntent.getStringExtra("emailId");
        txtKullanici.setText(gelenmail);

        t2=(TextView)findViewById(R.id.mailad1);

        String maildocument=txtKullanici.getText().toString();
        String b=maildocument+"x";
        t2.setText(b);
        String g=t2.getText().toString();
        t3=findViewById(R.id.kayit1);
        btncek=findViewById(R.id.buttoncek);//veriyi çektiğimiz buton


    btncek.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DocumentReference docRef = db.collection("Beykoz").document(b);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //  Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("UserName"));
                            String isim=document.getString("UserFirstKey");
                            t3.setText(isim);
                            nesne=t3.getText().toString();


                        } else {
                            //Log.d(TAG, "No such document");
                            String isim2="null";
                            t3.setText(isim2);
                            nesne=t3.getText().toString();

                        }
                    } else {
                        String isim2="null";
                        t3.setText(isim2);
                        nesne=t3.getText().toString();


                    }
                }
            });
            Toast.makeText(HomeActivity.this, "Veriler Eşitleniyor", Toast.LENGTH_LONG).show();



        }
    });



        nesne=t3.getText().toString();
        devam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Veriler Aktarıldı", Toast.LENGTH_SHORT).show();

               // nesne=t3.getText().toString();
                //SLEEP METHODUNU KOYACAĞIM

                    // işlemlerinizin durması için kod buraya gelebilir.

                    // 5 saniye boyunca bekleme işlemi yapalım.


                    // beklemesi biten işlemlerinizi burada devam ettirebilirsiniz.
                //int x=5;



                //bekle(x);


               /* Toast.makeText(HomeActivity.this, "Veriler Eşitleniyor", Toast.LENGTH_LONG).show();
                Toast.makeText(HomeActivity.this, "Veriler Aktarıldı", Toast.LENGTH_SHORT).show();*/


                if(t3.getText().toString()=="null" || t3.getText().toString()=="")
                {
                    String gmail=t2.getText().toString();
                    Intent sayfa1=new Intent(HomeActivity.this,LockScreenActivity.class);
                    sayfa1.putExtra("t2",gmail);
                    //sayfa1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                   // sayfa1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(sayfa1);
                   // finish();
                }

                else
                {
                    String gmail=t2.getText().toString();
                    Intent sayfa2=new Intent(HomeActivity.this,LockDesenScreen.class);
                    sayfa2.putExtra("t2",gmail);
                    startActivity(sayfa2);
                    //finish();

                }

                //finish();

              /*  String gmail=t3.getText().toString();
                Intent sayfa1=new Intent(HomeActivity.this,LockScreenActivity.class);
                sayfa1.putExtra("t3",gmail);
                startActivity(sayfa1);*/




            }
        });


       /*  if(nesne=="null")
        {
            Toast.makeText(HomeActivity.this,"HEnüz şifre oluşturmadınız yönlendiriliyorsunuz",Toast.LENGTH_SHORT).show();
            devam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String gmail=t3.getText().toString();
                    Intent sayfa1=new Intent(HomeActivity.this,LockScreenActivity.class);
                    sayfa1.putExtra("t3",gmail);
                    startActivity(sayfa1);




                }
            });
        }
        else
        {
            Toast.makeText(HomeActivity.this,"Şifre deseninizi onaylayın",Toast.LENGTH_SHORT).show();
            devam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String gmail=txtKullanici.getText().toString();
                    Intent sayfa2=new Intent(HomeActivity.this,LockScreenActivity.class);
                    sayfa2.putExtra("t3",gmail);
                    startActivity(sayfa2);






        }
            });
        }*/







       /* btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String gmail=txtKullanici.getText().toString();
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(HomeActivity.this, MainActivity.class);
              //  intToMain.putExtra("txtKullanici",gmail);
                startActivity(intToMain);
            }
        });*/

    }



}