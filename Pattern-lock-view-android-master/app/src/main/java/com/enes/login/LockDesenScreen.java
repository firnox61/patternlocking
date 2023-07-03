package com.enes.login;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class LockDesenScreen extends AppCompatActivity {
    String save_pattern_key = "pattern_code",desen3;
    PatternLockView mPatternLockView;
    // PatternLockView patternLockView;
    int sayac=0;
    final int beklemeSuresi = 30;
    String final_pattern = "",gelenmail,gelensifre,a,b;
    TextView d1,d2,d3;
    Button b1,b2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_desen_screen);
        d1=findViewById(R.id.text61);


        Intent gele=getIntent();
        gelenmail=gele.getStringExtra("t2");
        d1.setText(gelenmail);
        b1=(Button)findViewById(R.id.button61);
        d2=findViewById(R.id.text60);
        d3=findViewById(R.id.text50);
        b2=findViewById(R.id.buttonesitle);
        DocumentReference docRef = db.collection("Beykoz").document(d1.getText().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //  Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("UserName"));
                        String gelensif=document.getString("UserFirstKey");
                        d2.setText(gelensif);

                    } else {
                        //Log.d(TAG, "No such document");

                    }
                } else {
                    // Log.d(TAG, "get failed with ", task.getException());
                    // String isim2="null";
                    //t3.setText(isim2);
                }
            }
        });

        mPatternLockView=(PatternLockView) findViewById(R.id.pattern_lock_view);
        /*int dotNormalSize = mPatternLockView.getDotNormalSize();
        int dotSelectedSize = mPatternLockView.getDotSelectedSize();
        int dotNormalColor = mPatternLockView.getNormalStateColor();
        int dotCorrectColor = mPatternLockView.getCorrectStateColor();
        int dotErrorColor = mPatternLockView.getWrongStateColor();*/
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
               // sayac=0;

                final_pattern=PatternLockUtils.patternToString(mPatternLockView,pattern);
                d3.setText(final_pattern);
                // Paper.book().write(save_pattern_key, final_pattern);

                a=d2.getText().toString();
                b=d3.getText().toString();
                //mPatternLockView.clearPattern();
                if (a.equals(b)) {
                    mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                    // mPatternLockView.setNormalStateColor(Color.BLUE);
                    Toast.makeText(LockDesenScreen.this, "Şifre Doğru", Toast.LENGTH_SHORT).show();
                    String f = d1.getText().toString();
                    Intent sayfa6 = new Intent(LockDesenScreen.this, UserActivity.class);
                    sayfa6.putExtra("d1", f);
                    startActivity(sayfa6);
                } else {
                    //mPatternLockView.setNormalStateColor(Color.RED);
                    mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    Toast.makeText(LockDesenScreen.this, "Şifre Yanlış", Toast.LENGTH_SHORT).show();
                    sayac+=1;
                    if(sayac==5)
                    {
                        mPatternLockView.setEnabled(false);
                        Toast.makeText(LockDesenScreen.this, "5 Kez hatalı deneme yaptınız", Toast.LENGTH_SHORT).show();

                        new Thread(new Runnable() {
                            public void run() {
                                for(int i = beklemeSuresi; i >= 0; i--) {
                                    final int sayac = i;
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(LockDesenScreen.this, "Bekleme süresi: " + sayac + " saniye", Toast.LENGTH_SHORT).show();
                                            if(sayac==0)
                                            {
                                                mPatternLockView.setEnabled(true);
                                            }
                                        }
                                    });
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(LockDesenScreen.this, "Bekleme tamamlandı!", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }).start();
                        sayac=0;
                    }
                  /*  sayac=0;
                    if(sayac==0)
                    {
                        mPatternLockView.setEnabled(true);
                    }*/

                }
            }

            @Override
            public void onCleared() {

            }
        });
        mPatternLockView.setDotNormalSize((int) getResources().getDimension(R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) getResources().getDimension(R.dimen.pattern_lock_dot_size));
        mPatternLockView.setPathWidth((int) getResources().getDimension(R.dimen.pattern_lock_path_width));
        mPatternLockView.setNormalStateColor(Color.parseColor("#c2c2c2"));
        mPatternLockView.setCorrectStateColor(Color.parseColor("#3ebd5d"));
        mPatternLockView.setWrongStateColor(Color.parseColor("#ff0000"));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //Toast.makeText(LockDesenScreen.this, "SavePAttern okay", Toast.LENGTH_SHORT).show();




            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
/*b2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if(a==b) {
            Toast.makeText(LockDesenScreen.this, "Desen doğru", Toast.LENGTH_SHORT).show();
            String f = d1.getText().toString();
            Intent sayfa6 = new Intent(LockDesenScreen.this, UserActivity.class);
            sayfa6.putExtra("d1", f);
            startActivity(sayfa6);
        }
        else{
            Toast.makeText(LockDesenScreen.this, "Hatalı giriş tekrar deneyiniz", Toast.LENGTH_SHORT).show();
        }

    }
});*/

    }
}
               /* Map<String, Object> user = new HashMap<>();
                // user.put("UserMail", a);
                //  user.put("UserPassword", b);
                // user.put("UserFirstKey", save_pattern);
                user.put("UserFirstKey", final_pattern);
                //user.put("born", 1815);

// Add a new document with a generated ID
                db.collection("sondeneme").document(d1.getText().toString())
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

               // finish();
              /*  Paper.book().write(save_pattern_key, final_pattern);
                //Paper.init(this);
                String cv=Paper.book().read(save_pattern_key);
               // String as=cv.getBytes().toString();
                d1.setText(cv);*/