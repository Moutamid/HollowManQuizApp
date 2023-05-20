package com.moutamid.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.quizapp.models.Coins;
import com.moutamid.quizapp.utils.Constants;

public class MainActivity extends AppCompatActivity {

    com.moutamid.quizapp.databinding.ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = com.moutamid.quizapp.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = Constants.auth();
        Constants.checkApp(MainActivity.this);
        binding.french.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QuizScreen.class);
                intent.putExtra("type","french");
                startActivity(intent);
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        binding.firstWar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QuizScreen.class);
                intent.putExtra("type","first");
                startActivity(intent);
            }
        });

        binding.secondWar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QuizScreen.class);
                intent.putExtra("type","second");
                startActivity(intent);
            }
        });
        binding.coldWar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QuizScreen.class);
                intent.putExtra("type","cold");
                startActivity(intent);
            }
        });
        binding.gulfWar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QuizScreen.class);
                intent.putExtra("type","gulf");
                startActivity(intent);
            }
        });

        getCoins();
    }

    private void getCoins() {
        DatabaseReference db = Constants.databaseReference().child("Coins");
        FirebaseUser user = mAuth.getCurrentUser();
        db.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Coins model= snapshot.getValue(Coins.class);
                    int count = model.getTotal();
                    binding.coins.setText(String.valueOf(count)+" Coins");
                }else {
                    binding.coins.setText(String.valueOf(0)+" Coin");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}