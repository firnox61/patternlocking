package com.enes.login;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class LockScreenActivity extends AppCompatActivity {
    // EditText emailId, password;
    String save_pattern_key = "pattern_code";
    PatternLockView mPatternLockView;
    String final_pattern = "";
    String final_pattern2= "";
    Button butondene;
    Button butondene61;
    FirebaseAuth mFirebaseAuth;
    TextView t2,t5;
    String gelsin;
    String a;
    int sayac=0;
    PatternLockView patternLockView;
    List<PatternLockView.Dot> pattern = new ArrayList<>();
    List<PatternLockView.Dot> confirmPattern = new ArrayList<>();
    public Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            patternLockView.clearPattern();
        }
    };

    // String mail;

    // Bundle gelenVeri=getIntent().getExtras();
    // mail.setText(gelenVeri.getCharSequence("anahtar").toString());
    //  TextView sifre;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

       /* Intent intToHome=getIntent();
        String ad=intToHome.getStringExtra("ad");*/
        t2=(TextView)findViewById(R.id.mailad2);
        Intent gel=getIntent();
        gelsin=gel.getStringExtra("t2");
        t2.setText(gelsin);
        String maildocument=t2.getText().toString();
        String b=maildocument+"x";

        t5=(TextView)findViewById(R.id.textsifre);



        butondene61=findViewById(R.id.btnSetPattern);




        String nevarsa=t5.getText().toString();


        //1. şifreleme kaydı

        setContentView(R.layout.activity_lock_screen);
        Toast.makeText(LockScreenActivity.this, "Deseni belirleyiniz", Toast.LENGTH_SHORT).show();
        patternLockView=(PatternLockView) findViewById(R.id.pattern_lock_view);
       /* int dotNormalSize = patternLockView.getDotNormalSize();
        int dotSelectedSize = patternLockView.getDotSelectedSize();
        int dotNormalColor = patternLockView.getNormalStateColor();
        int dotCorrectColor = patternLockView.getCorrectStateColor();
        int dotErrorColor = patternLockView.getWrongStateColor();*/
        //patternLockView.setNormalStateColor(Color.GREEN);
        // patternLockView.setCorrectStateColor(Color.BLUE);
        // patternLockView.setWrongStateColor(Color.RED);


        Button btnSetup = (Button) findViewById(R.id.btnSetPattern);
        btnSetup.setEnabled(false);
        btnSetup.setOnClickListener((v) -> {

            Toast.makeText(LockScreenActivity.this, "SavePAttern okay", Toast.LENGTH_SHORT).show();
// FİRESTORE VERİ AKTARIMI

            Map<String, Object> user = new HashMap<>();

            user.put("UserFirstKey", final_pattern);
            //user.put("UserSecondKey", final_pattern2);
            //user.put("born", 1815);

// Add a new document with a generated ID
            db.collection("Beykoz").document(t2.getText().toString())
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

            //Intent sayfa5=new Intent(LockScreenActivity.this,HomeActivity.class);

           // startActivity(sayfa5);

            //finish();
            onBackPressed();

        });




        // patternLockView.setCorrectStateColor(ContextCompat.getColor(this, R.color.purple_500));
        //patternLockView.setErrorStateColor(ContextCompat.getColor(this, R.color.your_color));


        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if (pattern.size() < 4) {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    Toast.makeText(LockScreenActivity.this, "En az dört nokta birleştirmelisiniz", Toast.LENGTH_LONG).show();
                    patternLockView.postDelayed(mClearPatternRunnable, 2000);
                    return;
                }

                //patternLockView.setNormalStateColor(ContextCompat.getColor(LockScreenActivity.this, R.color.black));
                if (confirmPattern.isEmpty()) {
                    // patternLockView.setViewMode(PatternLockView.PatternViewMode.class);


                    // Kullanıcı desen kilidini ilk kez belirliyor.

                    confirmPattern.addAll(pattern);

                    patternLockView.clearPattern();
                    Toast.makeText(LockScreenActivity.this,"Deseni tekrar giriniz", Toast.LENGTH_SHORT).show();
                } else if (pattern.equals(confirmPattern)) {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                    //patternLockView.setNormalStateColor(Color.BLUE);
                    //patternLockView.setCorrectStateColor(ContextCompat.getColor(LockScreenActivity.this, R.color.purple_500));
                    // Kullanıcı desen kilidini doğru bir şekilde tekrar belirledi.
                    final_pattern=PatternLockUtils.patternToString(patternLockView, pattern);
                    Toast.makeText(LockScreenActivity.this, "Desen başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
                    confirmPattern.clear();
                    btnSetup.setEnabled(true);
                    //patternLockView.clearPattern();
                } else {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    // patternLockView.setNormalStateColor(Color.RED);
                    //patternLockView.setWrongStateColor(ContextCompat.getColor(LockScreenActivity.this, R.color.white));;
                    // Kullanıcı desen kilidini yanlış belirledi.
                    Toast.makeText(LockScreenActivity.this, "Desenler eşleşmiyor. Lütfen deseni tekrar çizin.", Toast.LENGTH_SHORT).show();
                    //patternLockView.clearPattern();
                }
            }





            @Override
            public void onCleared() {


            }
        });
        // mPatternLockView.setDotCount(4);
        patternLockView.setDotNormalSize((int) getResources().getDimension(R.dimen.pattern_lock_dot_size));
        patternLockView.setDotSelectedSize((int) getResources().getDimension(R.dimen.pattern_lock_dot_size));
        patternLockView.setPathWidth((int) getResources().getDimension(R.dimen.pattern_lock_path_width));
        patternLockView.setNormalStateColor(Color.parseColor("#c2c2c2"));
        patternLockView.setCorrectStateColor(Color.parseColor("#3ebd5d"));
        patternLockView.setWrongStateColor(Color.parseColor("#ff0000"));
     /*   if(final_pattern.equals(final_pattern2))
        {
            Toast.makeText(LockScreenActivity.this, "İki desen eşit", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(LockScreenActivity.this, "Farklı çizim tekrar deneyiniz", Toast.LENGTH_SHORT).show();
        }
*/



    }
}


