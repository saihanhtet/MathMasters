package com.hanhtetsan.mathmasters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    MediaPlayer bgMusic;
    Switch song_player;
    boolean isPlaying = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        song_player = findViewById(R.id.song_player);
        bgMusic = MediaPlayer.create(MainActivity.this, R.raw.sherlock_holmes);
        float volume = 0.02f;
        bgMusic.setVolume(volume, volume);
        bgMusic.setLooping(true);
        bgMusic.start();
        song_player.setChecked(true);
        song_player.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!isPlaying) {
                        bgMusic.start();
                        isPlaying = true;
                    }
                } else {
                    bgMusic.pause();
                    isPlaying = false;
                }
            }
        });
    }
    public void Level0Btn(View view){
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        intent.putExtra("level", 0);
        intent.putExtra("time", 0);
        startActivity(intent);
    }
    public void Level1Btn(View view){
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        intent.putExtra("level", 1);
        intent.putExtra("time", 20);
        startActivity(intent);
    }
    public void Level2Btn(View view){
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        intent.putExtra("level", 2);
        intent.putExtra("time", 10);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bgMusic.release();
    }
}