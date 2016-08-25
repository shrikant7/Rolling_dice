package com.example.eklavya.rollingdice;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer = null;
    int fs1=0,fs2=0,ts=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView score1 = (TextView) findViewById(R.id.score1);
        final TextView score2 = (TextView) findViewById(R.id.score2);
        final TextView playerTurn = (TextView) findViewById(R.id.turn);
        final TextView turnScore = (TextView) findViewById(R.id.turn_score);
        final ImageView dice = (ImageView) findViewById(R.id.dice);
        final TextView player1 = (TextView) findViewById(R.id.player1);
        final TextView player2 = (TextView) findViewById(R.id.player2);

        Button roll = (Button) findViewById(R.id.roll_button);
        Button hold = (Button) findViewById(R.id.hold_button);
        Button reset = (Button) findViewById(R.id.reset_button);
        mediaPlayer = MediaPlayer.create(this,R.raw.cheering);
        final Random randomGenerator = new Random();

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImage(randomGenerator, dice,playerTurn);
                turnScore.setText(ts+"");
            }
        });
        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dice.setImageResource(R.drawable.dice1);
                if(Integer.valueOf((String) playerTurn.getText())==1)
                {
                    fs1+=ts;
                    score1.setText(fs1+"");
                }
                else
                {
                    fs2+=ts;
                    score2.setText(fs2+"");
                }
                changePlayer(playerTurn);
                ts=0;
                turnScore.setText(ts+"");

                if(fs1>=50)
                {
                    mediaPlayer.start();
                    Toast toast=Toast.makeText(MainActivity.this,player1.getText()+" wins!!!\nscore:"+fs1, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    updateToReset(score1, score2, turnScore, playerTurn, dice);
                }
                if(fs2>=50)
                {
                    mediaPlayer.start();
                    Toast toast=Toast.makeText(MainActivity.this,player2.getText()+" wins!!!\nscore:"+fs2, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    updateToReset(score1, score2, turnScore, playerTurn, dice);
                }

            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToReset(score1, score2, turnScore, playerTurn, dice);
            }
        });

    }

    private void updateToReset(TextView score1, TextView score2, TextView turnScore, TextView playerTurn, ImageView dice) {
        ts=0;
        fs1=0;
        fs2=0;
        score1.setText(0+"");
        score2.setText(0+"");
        turnScore.setText(0+"");
        playerTurn.setText(1+"");
        dice.setImageResource(R.drawable.dice1);
    }

    private void changePlayer(TextView playerTurn) {
        if(Integer.valueOf((String) playerTurn.getText())==1)
            playerTurn.setText(2+"");
        else
            playerTurn.setText(1+"");
    }

    private void updateImage(Random randomGenerator, ImageView dice,TextView playerTurn) {
        int diceNumber = randomGenerator.nextInt(6)+1;
        switch (diceNumber)
        {
            case 1: dice.setImageResource(R.drawable.dice1);
                ts=0;
                changePlayer(playerTurn);
                break;
            case 2: dice.setImageResource(R.drawable.dice2);
                ts+=diceNumber;
                break;
            case 3: dice.setImageResource(R.drawable.dice3);
                ts+=diceNumber;
                break;
            case 4: dice.setImageResource(R.drawable.dice4);
                ts+=diceNumber;
                break;
            case 5: dice.setImageResource(R.drawable.dice5);
                ts+=diceNumber;
                break;
            case 6: dice.setImageResource(R.drawable.dice6);
                ts+=diceNumber;
                break;
        }
    }
}
