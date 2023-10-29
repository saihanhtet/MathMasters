package com.hanhtetsan.mathmasters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {
    private int LEVEL, TIME;
    private final int TotalQuestions = 10;
    private int CurrentQuestionIndex = 0;
    private QuestionModel CurrentQuestion;
    private Boolean answered;
    private int score;
    private TextView time_remain, question_count, question;
    private CountDownTimer countDownTimer;
    private List<QuestionModel> questionModelList;
    private RadioButton[] optionButtons;
    private RadioButton selectedRadioButton = null;
    private Button submit_btn;
    private final String correctColor= "#6cff69";
    private final String wrongColor= "#ff6969";
    private final String SelectColor= "#69dbff";
    private final String defaultButtonCOlor = "#45B0D1";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initializeLevelAndTime();

        addQuestion();
        showNextQuestion();
    }

    private void correctSong(){
        int[] musicResourceIds = {R.raw.correct, R.raw.correct_choice, R.raw.good_job, R.raw.excellent};
        int randomIndex = new Random().nextInt(musicResourceIds.length);
        int randomResourceId = musicResourceIds[randomIndex];
        MediaPlayer mediaPlayer = MediaPlayer.create(this, randomResourceId);
        mediaPlayer.start();
    }
    private void wrongSong(){
        int[] musicResourceIds = {R.raw.wrong, R.raw.wrong_choice};
        int randomIndex = new Random().nextInt(musicResourceIds.length);
        int randomResourceId = musicResourceIds[randomIndex];
        MediaPlayer mediaPlayer = MediaPlayer.create(this, randomResourceId);
        mediaPlayer.start();
    }

    public void onRadioButtonClick(View view) {
        RadioButton clickedRadioButton = (RadioButton) view;
        if (selectedRadioButton != null && selectedRadioButton != clickedRadioButton) {
            selectedRadioButton.setChecked(false); // Uncheck the previously selected radio button
            setButtonBackground(selectedRadioButton, defaultButtonCOlor);
        }
        selectedRadioButton = clickedRadioButton; // Set the newly selected radio button
        setButtonBackground(selectedRadioButton, SelectColor);
    }

    private void setButtonBackground(RadioButton button, String color) {
        Drawable drawable = button.getBackground();
        if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setCornerRadii(new float[]{14, 14, 14, 14, 14, 14, 14, 14});
            gradientDrawable.setColor(Color.parseColor(color));
        }
    }

    public void OnclickSubmitBtn(View view) {
        if (!answered) {
            if (selectedRadioButton == null) {
                Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show();
            } else {
                checkAnswer();
                if (LEVEL!=0){
                    countDownTimer.cancel();
                }
            }
        } else {
            showNextQuestion();
        }

    }


    @SuppressLint("SetTextI18n")
    private void checkAnswer() {
        if (selectedRadioButton != null) {
            answered = true;
            String selectedAnswer = selectedRadioButton.getText().toString();
            if (selectedAnswer.equals(CurrentQuestion.getCorrectAnswer())) {
                score++;
                setButtonBackground(selectedRadioButton, correctColor); //correct
                correctSong();
            } else {
                setButtonBackground(selectedRadioButton, wrongColor); //wrong
                wrongSong();
            }

            for (RadioButton optionButton : optionButtons) {
                optionButton.setEnabled(false);
                if (optionButton == selectedRadioButton) {
                    if (selectedAnswer.equals(CurrentQuestion.getCorrectAnswer())) {
                        setButtonBackground(optionButton, correctColor); //correct
                        //time_remain.setTextSize(Float.parseFloat("18sp"));
                        time_remain.setText("Correct!!!");
                    } else {
                        setButtonBackground(optionButton, wrongColor); //wrong
                        //time_remain.setTextSize(Float.parseFloat("18sp"));
                        time_remain.setText("Wrong!!!");
                    }
                } else if (optionButton.getText().toString().equals(CurrentQuestion.getCorrectAnswer())) {
                    setButtonBackground(optionButton, correctColor); //correct
                }
            }
            submit_btn.setText("Next");
        }
        if (CurrentQuestionIndex == TotalQuestions) {
            showScore(); // Show the score layout
        }
    }
    @SuppressLint("SetTextI18n")
    private void showScore() {
        // start celebration song
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.celebration);
        float volume = 0.08f;
        mediaPlayer.setVolume(volume, volume);
        mediaPlayer.start();
        // Calculate and display the score as a percentage
        setContentView(R.layout.score_layout);
        TextView score_text = findViewById(R.id.score_text);
        TextView lvl_has = findViewById(R.id.lvl_has);
        // Calculate the percentage
        int percentage = (score * 100) / TotalQuestions;
        // Display the score as a percentage
        score_text.setText("You got: " + percentage + "%");
        lvl_has.setText("Level " + LEVEL + " has been cleared!");
        // Handle the return to the main menu button
        Button returnToMenuBtn = findViewById(R.id.return_to_menu_btn);
        returnToMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void initializeLevelAndTime() {
        Intent intent = getIntent();
        if (intent != null) {
            LEVEL = intent.getIntExtra("level", 0); // Default to 0 if not provided
            TIME = intent.getIntExtra("time", 0); // Default to 0 if not provided
        }
        TextView level_flag = findViewById(R.id.level_flag);
        level_flag.setText("Level " + LEVEL);
        time_remain = findViewById(R.id.time_remain);
        question_count = findViewById(R.id.question_list);
        question = findViewById(R.id.question);
        optionButtons = new RadioButton[4];
        optionButtons[0] = findViewById(R.id.optionA);
        optionButtons[1] = findViewById(R.id.optionB);
        optionButtons[2] = findViewById(R.id.optionC);
        optionButtons[3] = findViewById(R.id.optionD);
        submit_btn = findViewById(R.id.submit_btn);
    }

    @SuppressLint("SetTextI18n")
    private void Timer() {
        if (LEVEL == 0) {
            time_remain.setText("Time Remaining: Unlimited");
        } else {
            countDownTimer = new CountDownTimer(TIME * 1000L, 1000) { // Convert TIME to milliseconds
                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    time_remain.setText(String.format("Time Remaining: %d", millisUntilFinished / 1000));
                }
                @Override
                public void onFinish() {
                    showNextQuestion();
                }
            }.start();
        }
    }
    private void showNextQuestion() {
        answered = false;
        selectedRadioButton = null;
        for (RadioButton optionButton : optionButtons) {
            optionButton.setChecked(false);
            optionButton.setEnabled(true);
            setButtonBackground(optionButton, defaultButtonCOlor); // Set the background to default color
        }

        if (CurrentQuestionIndex < TotalQuestions) {
            CurrentQuestion = questionModelList.get(CurrentQuestionIndex);
            question_count.setText(CurrentQuestionIndex + 1 + "/" + TotalQuestions);
            question.setText(CurrentQuestion.getQuestion());

            for (int i = 0; i < optionButtons.length; i++) {
                optionButtons[i].setText(getOptionText(i));
            }

            CurrentQuestionIndex++;
            submit_btn.setText("Check");
            Timer();
        } else {
            finish();
        }
    }

    private String getOptionText(int index) {
        switch (index) {
            case 0:
                return CurrentQuestion.getOptionA();
            case 1:
                return CurrentQuestion.getOptionB();
            case 2:
                return CurrentQuestion.getOptionC();
            case 3:
                return CurrentQuestion.getOptionD();
            default:
                return "";
        }
    }

    private void addQuestion() {
        questionModelList = QuestionGenerator.generateQuestions(10);
    }
}
