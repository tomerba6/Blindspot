package com.example.blindspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import static com.example.blindspot.FBref.refAuth;
import static com.example.blindspot.FBref.refUsers;

/**
 * @author Tomer Ben Ari
 * @version 0.6.0
 * @since 0.5.0 (20/12/2019)
 *
 * Main Activity
 */

public class MainActivity extends AppCompatActivity {

    TextView textView_username;
    User user = new User();

    TextToSpeech tts;


    /**
     * On activity create gets the user from Firebase and sets username on TextView
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_username = (TextView)findViewById(R.id.textView_username);

        FirebaseUser firebaseUser = refAuth.getCurrentUser();
        refUsers.child(firebaseUser.getEmail().replace("."," "))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       user.copyUser(dataSnapshot.getValue(User.class));
                        textView_username.setText(user.getName());
                        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if(status != TextToSpeech.ERROR){
                                    tts.setLanguage(Locale.US);
                                    tts.speak( "Welcome "+user.getName() ,TextToSpeech.QUEUE_FLUSH, null);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void goToScanner(View view) {
        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        startActivity(intent);
    }
}