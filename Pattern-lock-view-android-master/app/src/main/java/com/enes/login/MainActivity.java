package com.enes.login;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText emailId, password, Ad, Yas,Cinsiyet;

    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    String[] genders = {"Erkek", "Kadın"};
    AlertDialog genderDialog;

    //TextView sifre;
    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            StringBuilder sb = new StringBuilder();
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (Character.isLetter(c) || c == ' ') {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
    };


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnSignUp = findViewById(R.id.button2);
        tvSignIn = findViewById(R.id.textView);
        Ad=findViewById(R.id.Ad1);
        Ad.setFilters(new InputFilter[]{filter});
        Cinsiyet=findViewById(R.id.Cinsiyet1);
        Yas=findViewById(R.id.Yas1);
        /*
        String[] genders = {"Erkek", "Kadın"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.activity_main, genders);
        Cinsiyet.setAdapter(adapter);*/
        tvSignIn.setEnabled(true);

       /* Ad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("[a-zA-Z]+")) {
                    Ad.setError("Sadece harf girilebilir!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });*/





        Yas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Current date to set as the default date in the picker
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                String tarih=day+"/"+month+"/"+year;

                // Create a new DatePickerDialog instance
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Calculate the age from the selected date
                                Calendar dob = Calendar.getInstance();
                                dob.set(year, month, dayOfMonth);
                                Calendar today = Calendar.getInstance();
                                int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
                                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                                    age--;
                                }
                                StringBuilder sb = new StringBuilder();
                                sb.append(day).append("/").append(month).append("/").append(year);

                                // Set the age as the text of the EditText
                                //Yas.setText(Integer.toString(age));
                                Yas.setText(sb.toString());

                            }
                        }, year, month, day);

                // Set the maximum date to today's date
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });
        //     sifre=findViewById(R.id.passo);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String ad=Ad.getText().toString();
                String cinsiyet=Cinsiyet.getText().toString();
                String yas=Yas.getText().toString();


                //sifre.setText(email);
                 // FİRESTORE VERİ AKTARIMI

                Map<String, Object> user = new HashMap<>();
                user.put("UserMAil", email);
                user.put("UserPassword", pwd);
                user.put("UserName",ad);
                user.put("UserSex",cinsiyet);
                user.put("UserAge",yas);
                //user.put("born", 1815);
                //user.put("born", 1815);

// Add a new document with a generated ID
                db.collection("Beykoz").document(email)
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
                CharSequence error = Ad.getError();
                if(email.isEmpty()){
                    emailId.setError("Lütfen EMail giriniz !!!");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Lütfen şifrenizi giriniz !!!");
                    password.requestFocus();
                }
               else if(ad.isEmpty()){
                    Ad.setError("Lütfen adınızı giriniz !!!");
                    Ad.requestFocus();
                }
               else if(error!=null)
                {
                    Ad.setError("Lütfen string türünde değer giriniz !!!");
                    Ad.requestFocus();

                }
                else  if(cinsiyet.isEmpty()){
                    Cinsiyet.setError("Lütfen cinsiyetinizi giriniz !!!");
                    Cinsiyet.requestFocus();
                }
                else  if(yas.isEmpty()){
                    Yas.setError("Lütfen yaşınızı giriniz !!!");
                    Yas.requestFocus();
                }

                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Kaydolma Başarısız, Lütfen Tekrar Deneyin",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intToHome = new Intent(MainActivity.this,HomeActivity.class);
                                intToHome.putExtra("emailId",email);
                                startActivity(intToHome);

                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this,"Hata oluştu!",Toast.LENGTH_SHORT).show();

                }


            }
        });


        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        Cinsiyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });
    }
    public void showGenderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cinsiyet Seçimi");
        builder.setItems(genders, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cinsiyet.setText(genders[which]);
            }
        });


        genderDialog = builder.create();
        genderDialog.show();



    }
}