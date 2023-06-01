package com.moutamid.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moutamid.quizapp.databinding.ActivityLoginBinding;
import com.moutamid.quizapp.databinding.ActivityQuizScreenBinding;
import com.moutamid.quizapp.models.Answer;
import com.moutamid.quizapp.models.Coins;
import com.moutamid.quizapp.models.Quiz;
import com.moutamid.quizapp.utils.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizScreen extends AppCompatActivity {
    private ActivityQuizScreenBinding binding;
    private int i = 0;
    ArrayList<Quiz> franceList = new ArrayList<>();
    ArrayList<Quiz> firstWarList = new ArrayList<>();
    ArrayList<Quiz> secondWarList = new ArrayList<>();
    ArrayList<Quiz> coldWarList = new ArrayList<>();
    ArrayList<Quiz> gulfWarList = new ArrayList<>();
    String type = "" ;
    int final_score = 0;
    int count = 500;
    private FirebaseUser user;
    private InterstitialAd interstitialAd;
    int mistakes = 0;
    private boolean isClicked = false;
    private boolean previous = false;
    private int question = 0;
    private int correct = 0;
    private int wrong = 0;
    ArrayList<Answer> ansList = new ArrayList<>();
    private SharedPreferences manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manager = getPreferences(MODE_PRIVATE);
        type = getIntent().getStringExtra("type");
        user = Constants.auth().getCurrentUser();
        interstitialAd = new InterstitialAd (QuizScreen.this) ;
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

                //Showing a simple Toast Message to the user when The Google AdMob Sdk Initialization is Completed

                //Toast.makeText (QuizScreen.this, "AdMob Sdk Initialize "+ initializationStatus.toString(), Toast.LENGTH_LONG).show();

            }
        });
        //setting Ad Unit Id to the interstitialAd
        interstitialAd.setAdUnitId ( "ca-app-pub-3940256099942544/1033173712" ) ;

        if (type.equals("french")){
            loadFrance();
        }
        else if (type.equals("first")){
            loadFirstWar();
        }
        else if (type.equals("second")){
            loadSecondWar();
        }else if (type.equals("cold")){
            loadColdWar();
        }else if (type.equals("gulf")){
            loadGulfWar();
        }

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(isClicked){
                   if (final_score > 8) {
                       showWinDialig();
                       getCoins();
                   }else{
                       Toast.makeText(QuizScreen.this, "Better Luck next time!", Toast.LENGTH_SHORT).show();
                   }
               }
            }
        });


        if (i == 0) {
            if (type.equals("french")){
                binding.image.setImageDrawable(getDrawable(franceList.get(i).getImage()));
                binding.question.setText(franceList.get(i).getQuestiom());
                binding.option1.setText(franceList.get(i).getOption1());
                binding.option2.setText(franceList.get(i).getOption2());
                binding.option3.setText(franceList.get(i).getOption3());
                binding.option1Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final_score++;
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        isClicked = true;
                        saveValues(i,1,0);
    
                    }
                });
                binding.option2Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
    
                        isClicked = true;
                        saveValues(i,1,2);
                    }
                });
                binding.option3Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                        isClicked = true;
                        saveValues(i,1,3);
    
                    }
                });

            }
            else if (type.equals("first")){
                binding.image.setImageDrawable(getDrawable(firstWarList.get(i).getImage()));
                binding.question.setText(firstWarList.get(i).getQuestiom());
                binding.option1.setText(firstWarList.get(i).getOption1());
                binding.option2.setText(firstWarList.get(i).getOption2());
                binding.option3.setText(firstWarList.get(i).getOption3());
                binding.option1Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final_score++;
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        isClicked = true;

                        
    
                    }
                });
                binding.option2Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                        mistakes++;
                        isClicked = true;


    
                    }
                });
                binding.option3Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                        mistakes++;
                        isClicked = true;

                        
    
                    }
                });
            }
            else if (type.equals("second")){
                binding.image.setImageDrawable(getDrawable(secondWarList.get(i).getImage()));
                binding.question.setText(secondWarList.get(i).getQuestiom());
                binding.option1.setText(secondWarList.get(i).getOption1());
                binding.option2.setText(secondWarList.get(i).getOption2());
                binding.option3.setText(secondWarList.get(i).getOption3());
                binding.option1Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final_score++;
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        isClicked = true;

                        
    
                    }
                });
                binding.option2Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                        mistakes++;
                        isClicked = true;


    
                    }
                });
                binding.option3Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                        mistakes++;
                        isClicked = true;

                        
    
                    }
                });
            }else if (type.equals("cold")){
                binding.image.setImageDrawable(getDrawable(coldWarList.get(i).getImage()));
                binding.question.setText(coldWarList.get(i).getQuestiom());
                binding.option1.setText(coldWarList.get(i).getOption1());
                binding.option2.setText(coldWarList.get(i).getOption2());
                binding.option3.setText(coldWarList.get(i).getOption3());
                binding.option1Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final_score++;
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        isClicked = true;

                        
    
                    }
                });
                binding.option2Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                        mistakes++;
                        isClicked = true;


    
                    }
                });
                binding.option3Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                        mistakes++;
                        isClicked = true;

                        
    
                    }
                });
            }else if (type.equals("gulf")){
                binding.image.setImageDrawable(getDrawable(gulfWarList.get(i).getImage()));
                binding.question.setText(gulfWarList.get(i).getQuestiom());
                binding.option1.setText(gulfWarList.get(i).getOption1());
                binding.option2.setText(gulfWarList.get(i).getOption2());
                binding.option3.setText(gulfWarList.get(i).getOption3());
                binding.option1Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final_score++;
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        isClicked = true;

                        
    
                    }
                });
                binding.option2Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                        mistakes++;
                        isClicked = true;


    
                    }
                });
                binding.option3Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                        binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                        mistakes++;
                        isClicked = true;

                        
    
                    }
                });
            }
        }
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked) {
                    i = i + 1;
                    binding.option1Layout.setEnabled(true);
                    binding.option2Layout.setEnabled(true);
                    binding.option3Layout.setEnabled(true);
                    binding.option1Layout.setBackgroundResource(R.drawable.options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.options_background);

                    if (i < 10) {
                        //   Toast.makeText(QuizScreen.this, ""+ i, Toast.LENGTH_SHORT).show();
                        if (type.equals("french")) {
                            binding.image.setImageDrawable(getDrawable(franceList.get(i).getImage()));
                            binding.question.setText(franceList.get(i).getQuestiom());
                            binding.option1.setText(franceList.get(i).getOption1());
                            binding.option2.setText(franceList.get(i).getOption2());
                            if (franceList.get(i).getOption3().equals("")) {
                                binding.option3Layout.setVisibility(View.GONE);
                            } else {
                                binding.option3.setText(franceList.get(i).getOption3());
                                binding.option3Layout.setVisibility(View.VISIBLE);
                            }
                            checkFranceAnswers();

                        } else if (type.equals("first")) {
                            binding.image.setImageDrawable(getDrawable(firstWarList.get(i).getImage()));
                            binding.question.setText(firstWarList.get(i).getQuestiom());
                            binding.option1.setText(firstWarList.get(i).getOption1());
                            binding.option2.setText(firstWarList.get(i).getOption2());
                            binding.option3.setText(firstWarList.get(i).getOption3());
                            checkFirstWarAnswers();
                        } else if (type.equals("second")) {
                            binding.image.setImageDrawable(getDrawable(secondWarList.get(i).getImage()));
                            binding.question.setText(secondWarList.get(i).getQuestiom());
                            binding.option1.setText(secondWarList.get(i).getOption1());
                            binding.option2.setText(secondWarList.get(i).getOption2());
                            binding.option3.setText(secondWarList.get(i).getOption3());
                            checkSecondWarAnswers();
                        } else if (type.equals("cold")) {
                            binding.image.setImageDrawable(getDrawable(coldWarList.get(i).getImage()));
                            binding.question.setText(coldWarList.get(i).getQuestiom());
                            binding.option1.setText(coldWarList.get(i).getOption1());
                            binding.option2.setText(coldWarList.get(i).getOption2());
                            binding.option3.setText(coldWarList.get(i).getOption3());
                            checkColdWarAnswers();
                        } else if (type.equals("gulf")) {
                            binding.image.setImageDrawable(getDrawable(gulfWarList.get(i).getImage()));
                            binding.question.setText(gulfWarList.get(i).getQuestiom());
                            binding.option1.setText(gulfWarList.get(i).getOption1());
                            binding.option2.setText(gulfWarList.get(i).getOption2());
                            binding.option3.setText(gulfWarList.get(i).getOption3());
                            checkGulfWarAnswers();
                        }
                    }
                    if (i == 9) {
                        binding.buttons.setVisibility(View.GONE);
                        binding.submit.setVisibility(View.VISIBLE);
                    }

                    if (mistakes >= 3) {
                        showLoseDialig();
                    }
                    isClicked = false;
                }else{
                    Toast.makeText(QuizScreen.this, "please select any option!", Toast.LENGTH_SHORT).show();
                }
            }
        });
     /*   binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    i = i-1;
                    isClicked = true;
                    binding.option1Layout.setEnabled(false);
                    binding.option2Layout.setEnabled(false);
                    binding.option3Layout.setEnabled(false);
                    if (type.equals("french")) {
                        binding.image.setImageDrawable(getDrawable(franceList.get(i).getImage()));
                        binding.question.setText(franceList.get(i).getQuestiom());
                        binding.option1.setText(franceList.get(i).getOption1());
                        binding.option2.setText(franceList.get(i).getOption2());
                        binding.option3.setText(franceList.get(i).getOption3());
                        if (franceList.get(i).getOption3().equals("")) {
                            binding.option3Layout.setVisibility(View.GONE);
                        } else {
                            binding.option3.setText(franceList.get(i).getOption3());
                            binding.option3Layout.setVisibility(View.VISIBLE);
                        }
                        showValue();

                    } else if (type.equals("first")) {
                        binding.image.setImageDrawable(getDrawable(firstWarList.get(i).getImage()));
                        binding.question.setText(firstWarList.get(i).getQuestiom());
                        binding.option1.setText(firstWarList.get(i).getOption1());
                        binding.option2.setText(firstWarList.get(i).getOption2());
                        binding.option3.setText(firstWarList.get(i).getOption3());

                        showValue();

                    } else if (type.equals("second")) {
                        binding.image.setImageDrawable(getDrawable(secondWarList.get(i).getImage()));
                        binding.question.setText(secondWarList.get(i).getQuestiom());
                        binding.option1.setText(secondWarList.get(i).getOption1());
                        binding.option2.setText(secondWarList.get(i).getOption2());
                        binding.option3.setText(secondWarList.get(i).getOption3());

                        showValue();

                    } else if (type.equals("cold")) {
                        binding.image.setImageDrawable(getDrawable(coldWarList.get(i).getImage()));
                        binding.question.setText(coldWarList.get(i).getQuestiom());
                        binding.option1.setText(coldWarList.get(i).getOption1());
                        binding.option2.setText(coldWarList.get(i).getOption2());
                        binding.option3.setText(coldWarList.get(i).getOption3());

                        showValue();

                    } else if (type.equals("gulf")) {
                        binding.image.setImageDrawable(getDrawable(gulfWarList.get(i).getImage()));
                        binding.question.setText(gulfWarList.get(i).getQuestiom());
                        binding.option1.setText(gulfWarList.get(i).getOption1());
                        binding.option2.setText(gulfWarList.get(i).getOption2());
                        binding.option3.setText(gulfWarList.get(i).getOption3());

                        showValue();

                    }
                }
            }
        });*/
        loadInterstitialAd();
    }

    private void saveValues(int q,int correct,int wrong){
        Answer model = new Answer(q,correct,wrong);
        ansList.add(model);
        SharedPreferences.Editor prefsEditor = manager.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ansList);
        prefsEditor.putString("anwsers", json);
        prefsEditor.commit();
    }


    private void showValue() {

        Gson gson = new Gson();
        String json = manager.getString("anwsers", "");
        if (json.isEmpty()){
            Toast.makeText(this, "List Empty", Toast.LENGTH_SHORT).show();
        }else {
            Type type = new TypeToken<ArrayList<Answer>>(){

            }.getType();
            ArrayList<Answer> answerArrayList = gson.fromJson(json,type);

            for (Answer model : answerArrayList){
                if (model.getQuestion() == i) {
                    if (model.getCorrect() == 1) {
                        binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    } else if (model.getWrong() == 1) {
                        binding.option1Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    }
                    if (model.getCorrect() == 2) {
                        binding.option2Layout.setBackgroundResource(R.drawable.correct_options_background);
                    } else if (model.getWrong() == 2) {
                        binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    }
                    if (model.getCorrect() == 3) {
                        binding.option3Layout.setBackgroundResource(R.drawable.correct_options_background);
                    } else if (model.getWrong() == 3) {
                        binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    }
                }
            }
        }

    }

    private void loadInterstitialAd()
    {
        // Creating  a Ad Request
        AdRequest adRequest = new AdRequest.Builder().build() ;

        // load Ad with the Request
        interstitialAd.loadAd(adRequest) ;

        // Showing a simple Toast message to user when an ad is Loading
        Toast.makeText ( QuizScreen.this, "Interstitial Ad is loading ", Toast.LENGTH_LONG).show() ;
    }
    private void getCoins() {
        DatabaseReference db = Constants.databaseReference().child("Coins");
        db.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Coins model= snapshot.getValue(Coins.class);
                    count += model.getTotal();
                    storeCoins(count);
                }else {
                    storeCoins(500);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkFranceAnswers() {

        binding.option1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
              //      Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;

                    saveValues(i,1,0);
                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    saveValues(i,1,0);
                    
                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                  //  Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    saveValues(i,1,0);
                    
                }
                else if(i == 2){
                    binding.option3Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option1Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;

                    saveValues(i,3,1);
                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //      Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;

                    saveValues(i,1,0);
                }
                else if(i == 6){
                    binding.option2Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option1Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,2,1);

                    
                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;

                    saveValues(i,1,0);
                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    saveValues(i,1,0);
                    
                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    saveValues(i,1,0);
                    
                }
            }
        });
        binding.option2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==6){
                    binding.option2Layout.setBackgroundResource(R.drawable.correct_options_background);
                   // Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    saveValues(i,2,0);
                    
                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,1,2);
                    
                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,1,2);
                    
                }
                else if(i == 2){
                    binding.option3Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,3,2);
                    
                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,1,2);
                }
                else if(i == 1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,1,2);
                    
                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,1,2);
                    
                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,1,2);
                    
                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,1,2);
                    
                }
            }
        });
        binding.option3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==2){
                    binding.option3Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //   Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;

                    saveValues(i,3,2);
                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,1,3);

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;

                    saveValues(i,1,3);
                }
                else if(i == 6){
                    binding.option2Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,2,3);
                    
                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;

                    saveValues(i,1,3);
                }
                else if(i == 1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,1,3);
                    
                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;

                    saveValues(i,1,3);
                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;

                    saveValues(i,1,3);
                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    saveValues(i,1,3);
                    
                }
            }
        });
    }

    private void checkFirstWarAnswers() {
        binding.option1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //      Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;

                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //  Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //      Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }

            }
        });
        binding.option2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
            }
        });
        binding.option3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
            }
        });
    }

    private void checkSecondWarAnswers() {
        binding.option1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //      Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //  Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //      Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }

            }
        });
        binding.option2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;

                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
            }
        });
        binding.option3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
            }
        });
    }
    private void checkColdWarAnswers() {
        binding.option1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //      Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //  Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //      Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }

            }
        });
        binding.option2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
            }
        });
        binding.option3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
            }
        });
    }
    private void checkGulfWarAnswers() {
        binding.option1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //      Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //  Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //      Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    //    Toast.makeText(QuizScreen.this, franceList.get(i).getQuestiom(), Toast.LENGTH_SHORT).show();
                    final_score++;
                    

                }

            }
        });
        binding.option2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option2Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
            }
        });
        binding.option3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (i==2){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==4){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if (i==8){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 6){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 3){

                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 1){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 5){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 7){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
                else if(i == 9){
                    binding.option1Layout.setBackgroundResource(R.drawable.correct_options_background);
                    binding.option3Layout.setBackgroundResource(R.drawable.wrong_options_background);
                    mistakes++;
                    

                }
            }
        });
    }

    private void loadFrance() {
        Resources res = getResources();
        Quiz model1 = new Quiz(res.getString(R.string.qno1),R.drawable.f1,res.getString(R.string.q1op1),
                res.getString(R.string.q1op2),res.getString(R.string.q1op3));
        Quiz model2 = new Quiz(res.getString(R.string.qno2),R.drawable.f2,res.getString(R.string.q2op1),
                res.getString(R.string.q2op2),res.getString(R.string.q2op3));
        Quiz model3 = new Quiz(res.getString(R.string.qno3),R.drawable.f3,res.getString(R.string.q3op1),
                res.getString(R.string.q3op2),res.getString(R.string.q3op3));
        Quiz model4 = new Quiz(res.getString(R.string.qno4),R.drawable.f4,res.getString(R.string.q4op1),
                res.getString(R.string.q4op2),res.getString(R.string.q4op3));
        Quiz model5 = new Quiz(res.getString(R.string.qno5),R.drawable.f5,res.getString(R.string.q5op1),
                res.getString(R.string.q5op2),res.getString(R.string.q5op3));
        Quiz model6 = new Quiz(res.getString(R.string.qno6),R.drawable.f6,res.getString(R.string.q6op1),
                res.getString(R.string.q6op2),res.getString(R.string.q6op3));
        Quiz model7 = new Quiz(res.getString(R.string.qno7),R.drawable.f7,res.getString(R.string.q7op1),
                res.getString(R.string.q7op2),"");
        Quiz model8 = new Quiz(res.getString(R.string.qno8),R.drawable.f8,res.getString(R.string.q8op1),
                res.getString(R.string.q8op2),res.getString(R.string.q8op3));
        Quiz model9 = new Quiz(res.getString(R.string.qno9),R.drawable.f9,res.getString(R.string.q9op1),
                res.getString(R.string.q9op2),res.getString(R.string.q9op3));
        Quiz model10 = new Quiz(res.getString(R.string.qno10),R.drawable.f4,res.getString(R.string.q10op1),
                res.getString(R.string.q10op2),res.getString(R.string.q10op3));

        franceList.add(model1);
        franceList.add(model2);
        franceList.add(model3);
        franceList.add(model4);
        franceList.add(model5);
        franceList.add(model6);
        franceList.add(model7);
        franceList.add(model8);
        franceList.add(model9);
        franceList.add(model10);
    }

    private void loadFirstWar() {
        Resources res = getResources();
        Quiz model1 = new Quiz(res.getString(R.string.qno11),R.drawable.fw1,res.getString(R.string.q11op1),
                res.getString(R.string.q11op2),res.getString(R.string.q11op3));
        Quiz model2 = new Quiz(res.getString(R.string.qno12),R.drawable.fw2,res.getString(R.string.q12op1),
                res.getString(R.string.q12op2),res.getString(R.string.q12op3));
        Quiz model3 = new Quiz(res.getString(R.string.qno13),R.drawable.fw3,res.getString(R.string.q13op1),
                res.getString(R.string.q13op2),res.getString(R.string.q13op3));
        Quiz model4 = new Quiz(res.getString(R.string.qno14),R.drawable.fw4,res.getString(R.string.q14op1),
                res.getString(R.string.q14op2),res.getString(R.string.q14op3));
        Quiz model5 = new Quiz(res.getString(R.string.qno15),R.drawable.fw5,res.getString(R.string.q15op1),
                res.getString(R.string.q15op2),res.getString(R.string.q15op3));
        Quiz model6 = new Quiz(res.getString(R.string.qno16),R.drawable.fw6,res.getString(R.string.q16op1),
                res.getString(R.string.q16op2),res.getString(R.string.q16op3));
        Quiz model7 = new Quiz(res.getString(R.string.qno17),R.drawable.fw7,res.getString(R.string.q17op1),
                res.getString(R.string.q17op2),res.getString(R.string.q17op3));
        Quiz model8 = new Quiz(res.getString(R.string.qno18),R.drawable.fw8,res.getString(R.string.q18op1),
                res.getString(R.string.q18op2),res.getString(R.string.q18op3));
        Quiz model9 = new Quiz(res.getString(R.string.qno19),R.drawable.fw9,res.getString(R.string.q19op1),
                res.getString(R.string.q19op2),res.getString(R.string.q19op3));
        Quiz model10 = new Quiz(res.getString(R.string.qno20),R.drawable.fw10,res.getString(R.string.q20op1),
                res.getString(R.string.q20op2),res.getString(R.string.q20op3));

        firstWarList.add(model1);
        firstWarList.add(model2);
        firstWarList.add(model3);
        firstWarList.add(model4);
        firstWarList.add(model5);
        firstWarList.add(model6);
        firstWarList.add(model7);
        firstWarList.add(model8);
        firstWarList.add(model9);
        firstWarList.add(model10);
    }

    private void loadSecondWar() {
        Resources res = getResources();
        Quiz model1 = new Quiz(res.getString(R.string.qno21),R.drawable.sw1,res.getString(R.string.q21op1),
                res.getString(R.string.q21op2),res.getString(R.string.q21op3));
        Quiz model2 = new Quiz(res.getString(R.string.qno22),R.drawable.sw2,res.getString(R.string.q22op1),
                res.getString(R.string.q22op2),res.getString(R.string.q22op3));
        Quiz model3 = new Quiz(res.getString(R.string.qno23),R.drawable.sw3,res.getString(R.string.q23op1),
                res.getString(R.string.q23op2),res.getString(R.string.q23op3));
        Quiz model4 = new Quiz(res.getString(R.string.qno24),R.drawable.sw4,res.getString(R.string.q24op1),
                res.getString(R.string.q24op2),res.getString(R.string.q24op3));
        Quiz model5 = new Quiz(res.getString(R.string.qno25),R.drawable.sw5,res.getString(R.string.q25op1),
                res.getString(R.string.q25op2),res.getString(R.string.q25op3));
        Quiz model6 = new Quiz(res.getString(R.string.qno26),R.drawable.sw6,res.getString(R.string.q26op1),
                res.getString(R.string.q26op2),res.getString(R.string.q26op3));
        Quiz model7 = new Quiz(res.getString(R.string.qno27),R.drawable.sw7,res.getString(R.string.q27op1),
                res.getString(R.string.q27op2),res.getString(R.string.q27op3));
        Quiz model8 = new Quiz(res.getString(R.string.qno28),R.drawable.sw8,res.getString(R.string.q28op1),
                res.getString(R.string.q28op2),res.getString(R.string.q28op3));
        Quiz model9 = new Quiz(res.getString(R.string.qno29),R.drawable.sw9,res.getString(R.string.q29op1),
                res.getString(R.string.q29op2),res.getString(R.string.q29op3));
        Quiz model10 = new Quiz(res.getString(R.string.qno30),R.drawable.sw10,res.getString(R.string.q30op1),
                res.getString(R.string.q30op2),res.getString(R.string.q30op3));

        secondWarList.add(model1);
        secondWarList.add(model2);
        secondWarList.add(model3);
        secondWarList.add(model4);
        secondWarList.add(model5);
        secondWarList.add(model6);
        secondWarList.add(model7);
        secondWarList.add(model8);
        secondWarList.add(model9);
        secondWarList.add(model10);
    }

    private void loadColdWar() {
        Resources res = getResources();
        Quiz model1 = new Quiz(res.getString(R.string.qno31),R.drawable.c1,res.getString(R.string.q31op1),
                res.getString(R.string.q31op2),res.getString(R.string.q31op3));
        Quiz model2 = new Quiz(res.getString(R.string.qno32),R.drawable.c2,res.getString(R.string.q32op1),
                res.getString(R.string.q32op2),res.getString(R.string.q32op3));
        Quiz model3 = new Quiz(res.getString(R.string.qno33),R.drawable.c3,res.getString(R.string.q33op1),
                res.getString(R.string.q33op2),res.getString(R.string.q33op3));
        Quiz model4 = new Quiz(res.getString(R.string.qno34),R.drawable.c4,res.getString(R.string.q34op1),
                res.getString(R.string.q34op2),res.getString(R.string.q34op3));
        Quiz model5 = new Quiz(res.getString(R.string.qno35),R.drawable.c5,res.getString(R.string.q35op1),
                res.getString(R.string.q35op2),res.getString(R.string.q35op3));
        Quiz model6 = new Quiz(res.getString(R.string.qno36),R.drawable.c6,res.getString(R.string.q36op1),
                res.getString(R.string.q36op2),res.getString(R.string.q36op3));
        Quiz model7 = new Quiz(res.getString(R.string.qno37),R.drawable.c7,res.getString(R.string.q37op1),
                res.getString(R.string.q37op2),res.getString(R.string.q37op3));
        Quiz model8 = new Quiz(res.getString(R.string.qno38),R.drawable.c8,res.getString(R.string.q38op1),
                res.getString(R.string.q38op2),res.getString(R.string.q38op3));
        Quiz model9 = new Quiz(res.getString(R.string.qno39),R.drawable.c9,res.getString(R.string.q39op1),
                res.getString(R.string.q39op2),res.getString(R.string.q39op3));
        Quiz model10 = new Quiz(res.getString(R.string.qno40),R.drawable.c10,res.getString(R.string.q40op1),
                res.getString(R.string.q40op2),res.getString(R.string.q40op3));

        coldWarList.add(model1);
        coldWarList.add(model2);
        coldWarList.add(model3);
        coldWarList.add(model4);
        coldWarList.add(model5);
        coldWarList.add(model6);
        coldWarList.add(model7);
        coldWarList.add(model8);
        coldWarList.add(model9);
        coldWarList.add(model10);
    }


    private void loadGulfWar() {
        Resources res = getResources();
        Quiz model1 = new Quiz(res.getString(R.string.qno41),R.drawable.gw1,res.getString(R.string.q41op1),
                res.getString(R.string.q41op2),res.getString(R.string.q41op3));
        Quiz model2 = new Quiz(res.getString(R.string.qno42),R.drawable.gw2,res.getString(R.string.q42op1),
                res.getString(R.string.q42op2),res.getString(R.string.q42op3));
        Quiz model3 = new Quiz(res.getString(R.string.qno43),R.drawable.gw3,res.getString(R.string.q43op1),
                res.getString(R.string.q43op2),res.getString(R.string.q43op3));
        Quiz model4 = new Quiz(res.getString(R.string.qno44),R.drawable.gw4,res.getString(R.string.q44op1),
                res.getString(R.string.q44op2),res.getString(R.string.q44op3));
        Quiz model5 = new Quiz(res.getString(R.string.qno45),R.drawable.gw5,res.getString(R.string.q45op1),
                res.getString(R.string.q45op2),res.getString(R.string.q45op3));
        Quiz model6 = new Quiz(res.getString(R.string.qno46),R.drawable.gw6,res.getString(R.string.q46op1),
                res.getString(R.string.q46op2),res.getString(R.string.q46op3));
        Quiz model7 = new Quiz(res.getString(R.string.qno47),R.drawable.gw7,res.getString(R.string.q47op1),
                res.getString(R.string.q47op2),res.getString(R.string.q47op3));
        Quiz model8 = new Quiz(res.getString(R.string.qno48),R.drawable.gw8,res.getString(R.string.q48op1),
                res.getString(R.string.q48op2),res.getString(R.string.q48op3));
        Quiz model9 = new Quiz(res.getString(R.string.qno49),R.drawable.gw9,res.getString(R.string.q49op1),
                res.getString(R.string.q49op2),res.getString(R.string.q49op3));
        Quiz model10 = new Quiz(res.getString(R.string.qno50),R.drawable.gw10,res.getString(R.string.q50op1),
                res.getString(R.string.q50op2),res.getString(R.string.q50op3));

        gulfWarList.add(model1);
        gulfWarList.add(model2);
        gulfWarList.add(model3);
        gulfWarList.add(model4);
        gulfWarList.add(model5);
        gulfWarList.add(model6);
        gulfWarList.add(model7);
        gulfWarList.add(model8);
        gulfWarList.add(model9);
        gulfWarList.add(model10);
    }

    private void showLoseDialig(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(QuizScreen.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.lose_dialog_box, null);
        dialogBuilder.setView(dialogView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView scores = (TextView) dialogView.findViewById(R.id.score);
        AppCompatButton useBtn = (AppCompatButton) dialogView.findViewById(R.id.coins);
        AppCompatButton adsBtn = (AppCompatButton) dialogView.findViewById(R.id.ads);
        AlertDialog alertDialog = dialogBuilder.create();
        scores.setText(final_score+"/10");
        useBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useCoins();
                mistakes = 0;
                alertDialog.dismiss();
            }
        });
        adsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitialAd();
                mistakes = 0;
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void useCoins() {
        DatabaseReference db = Constants.databaseReference().child("Coins");
        db.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Coins model= snapshot.getValue(Coins.class);
                    count -= model.getTotal();
                    storeCoins(count);
                }else {
                    Toast.makeText(QuizScreen.this, "You don't have enough coins!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void storeCoins(int coins) {
        DatabaseReference db = Constants.databaseReference().child("Coins");
        //HashMap<String,Object> hashMap = new HashMap<>();
        //hashMap.put("total",coins);
        Coins model = new Coins(coins);
        db.child(user.getUid()).setValue(model);
    }


    private void showWinDialig(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(QuizScreen.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.win_dialog_box, null);
        dialogBuilder.setView(dialogView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView scores = (TextView) dialogView.findViewById(R.id.score);
        AppCompatButton homepage = (AppCompatButton) dialogView.findViewById(R.id.hompage);
        AlertDialog alertDialog = dialogBuilder.create();
        scores.setText(final_score+"/10");
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizScreen.this,MainActivity.class));
                finish();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void showInterstitialAd()
    {
        if ( interstitialAd.isLoaded() )
        {
            //showing the ad Interstitial Ad if it is loaded
            interstitialAd.show() ;

            // Showing a simple Toast message to user when an Interstitial ad is shown to the user
            Toast.makeText ( QuizScreen.this, "Interstitial is loaded and showing ad  ", Toast.LENGTH_LONG) .show();
        }
        else
        {
            //Load the Interstitial ad if it is not loaded
            loadInterstitialAd() ;

            // Showing a simple Toast message to user when an ad is not loaded
            Toast.makeText ( QuizScreen.this, "Interstitial Ad is not Loaded ", Toast.LENGTH_LONG).show() ;
        }
    }



}