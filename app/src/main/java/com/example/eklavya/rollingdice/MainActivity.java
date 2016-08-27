package com.example.eklavya.rollingdice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer = null;
    private String mPlayer1Name,mPlayer2Name;
    private  Button roll,hold,reset;
    private TextView player1,player2,score1,score2,turnScore,playerTurn;
    private ImageView dice;
    int fs1=0,fs2=0,ts=0;
    Boolean BOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check initialy for bot enable or not.
        BOT = isBot();
        score1 = (TextView) findViewById(R.id.score1);
        score2 = (TextView) findViewById(R.id.score2);
        turnScore = (TextView) findViewById(R.id.turn_score);
        dice = (ImageView) findViewById(R.id.dice);
        playerTurn = (TextView) findViewById(R.id.player_turn);
        player1 = (TextView) findViewById(R.id.player1);
        player2 = (TextView) findViewById(R.id.player2);

        roll = (Button) findViewById(R.id.roll_button);
        hold = (Button) findViewById(R.id.hold_button);
        reset = (Button) findViewById(R.id.reset_button);
        mediaPlayer = MediaPlayer.create(this,R.raw.cheering);
        final Random randomGenerator = new Random();

        //get initial names from sharedPreferences and update ui.
        mPlayer1Name = getSharedPlayer(this.getString(R.string.preference_player1_key),this.getString(R.string.player1_text));
        mPlayer2Name= getSharedPlayer(this.getString(R.string.preference_player2_key),this.getString(R.string.player2_text));
        playerTurn.setText(mPlayer1Name);

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int diceNumber = updateImage(randomGenerator, dice,playerTurn);
                turnScore.setText(ts+"");
                if(diceNumber==1)
                {
                    changePlayer(playerTurn);
                    //if diceNumber is 1 and bot is enabled then go for bot's chance.
                    if(BOT)
                        computerTurn(randomGenerator);
                }
            }
        });
        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dice.setImageResource(R.drawable.dice1);

                if(playerTurn.getText().equals(mPlayer1Name))
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
                isAnyWin();

                //if second player is BOT then bot's turn.
                if(BOT && playerTurn.getText().equals(mPlayer2Name))
                {
                    computerTurn(randomGenerator);
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

    //check if any of player gets total above 100.
    private void isAnyWin() {
        if(fs1>=100)
        {
            mediaPlayer.start();
            Toast toast=Toast.makeText(MainActivity.this,player1.getText()+" wins!!!\nscore:"+fs1, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            updateToReset(score1, score2, turnScore, playerTurn, dice);
        }
        if(fs2>=100)
        {
            mediaPlayer.start();
            Toast toast=Toast.makeText(MainActivity.this,player2.getText()+" wins!!!\nscore:"+fs2, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            updateToReset(score1, score2, turnScore, playerTurn, dice);
        }
    }

    //check is bot enable or not.
    private boolean isBot() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(this.getString(R.string.preference_bot),false);
    }

    //get sharedPlayer names.
    @NonNull
    private String  getSharedPlayer(String key,String defaultName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(key,defaultName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this,Prefs.class));
            return true;
        }
        if(id==R.id.action_about)
        {
            startActivity(new Intent(this,About.class));
            return true;
        }

        if(id==R.id.action_feedback)
        {
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "shrikantlnmiit@gmail.com" });
            Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            Email.putExtra(Intent.EXTRA_TEXT, "Dear ...," + "");
            startActivity(Intent.createChooser(Email, "Send Feedback:"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        String player1_name = getSharedPlayer(this.getString(R.string.preference_player1_key),this.getString(R.string.player1_text));
        String player2_name= getSharedPlayer(this.getString(R.string.preference_player2_key),this.getString(R.string.player2_text));
        if(!BOT.equals(isBot()))
        {
            BOT=!BOT;
        }
        if(player1_name!=null && !player1_name.equals(mPlayer1Name))
        {
            mPlayer1Name=player1_name;
        }
        if(player2_name!=null && !player2_name.equals(mPlayer2Name))
        {
            mPlayer2Name=player2_name;
        }
        player1.setText(mPlayer1Name);
        player2.setText(mPlayer2Name);
    }

    //updates all ui to Zero.
    private void updateToReset(TextView score1, TextView score2, TextView turnScore, TextView playerTurn, ImageView dice) {
        ts=0;
        fs1=0;
        fs2=0;
        score1.setText(String.valueOf(0));
        score2.setText(String.valueOf(0));
        turnScore.setText(String .valueOf(0));
        playerTurn.setText(mPlayer1Name);
        dice.setImageResource(R.drawable.dice1);
    }

    //change player turn on ui.
    private void changePlayer(TextView playerTurn) {
        if(playerTurn.getText().equals(mPlayer1Name))
            playerTurn.setText(mPlayer2Name);
        else
            playerTurn.setText(mPlayer1Name);
    }

    //method for gettiing random number and updating dice image.
    private int updateImage(Random randomGenerator, ImageView dice,TextView playerTurn) {
        int diceNumber = randomGenerator.nextInt(6)+1;
        switch (diceNumber)
        {
            case 1: dice.setImageResource(R.drawable.dice1);
                ts=0;
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
        return diceNumber;
    }

    //play for computer.
    private void computerTurn(Random rand) {
        //diable buttons in computer's turn.
        roll.setEnabled(false);
        hold.setEnabled(false);

        int diceNumber;
        while(ts<15)
        {
            diceNumber=updateImage(rand, dice,playerTurn);
            turnScore.setText(String .valueOf(ts));
            if(diceNumber==1)
                break;
        }
        fs2+=ts;ts=0;
        score2.setText(String .valueOf(fs2));
        isAnyWin();

        //again enable buttons to player 1;
        roll.setEnabled(true);
        hold.setEnabled(true);
        changePlayer(playerTurn);
    }
}
