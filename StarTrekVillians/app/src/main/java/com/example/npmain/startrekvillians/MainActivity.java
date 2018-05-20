package com.example.npmain.startrekvillians;

import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    private final static int INITIAL_SCORE = 0;
    private final static int PHASER_POINTS = 10;
    private final static int TORPEDO_POINTS = 20;
    private final static int GENESIS_POINTS = 50;
    private final static long TIMER_START_VALUE = 10000;
    private final static long TIMER_INTERVAL = 1000;
    private final static String TIMER_START_STRING = "10 Seconds";
    private int scoreKlingon = 0;
    private int scoreRomulan = 0;
    private long klingonMillisUntilFinished;
    private KlingonCountDownTimer klingonCountDownTimer;
    private boolean isKlingonShieldUp = false;
    private long romulanMillisUntilFinished;
    private RomulanCountDownTimer romulanCountDownTimer;
    private boolean isRomulanShieldUp = false;
    private final static String KLINGON_SCORE = "KlingonScore";
    private final static String ROMULAN_SCORE = "RomulanScore";
    private final static String KLINGON_SHIELD_UP = "KlingonShieldUp";
    private final static String ROMULAN_SHIELD_UP = "RomulanShieldUp";
    private final static String KLINGON_TIMER_MILLIS = "KlingonTimerMillis";
    private final static String ROMULAN_TIMER_MILLIS = "RomulanTimerMillis";

    Button klingonFirePhasersButton;
    Button klingonFirePhotonsButton;
    Button klingonFireGenesisButton;
    Button klingonShieldUp;
    Button romulanFirePhasersButton;
    Button romulanFirePhotonsButton;
    Button romulanFireGenesisButton;
    Button romulanShieldUp;
    TextView klingonTimer;
    TextView romulanTimer;
    TextView klingonScoreView;
    TextView romulanScoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storeViewIds();
    }

    /* This method "walks" the activity_main.xml and initializes the
    view ids of the XML elements used in the MainActivity.java file
     */
    private void storeViewIds()
    {
        klingonFirePhasersButton = findViewById(R.id.klingon_fire_phasers);
        klingonFirePhotonsButton = findViewById(R.id.klingon_fire_photons);
        klingonFireGenesisButton = findViewById(R.id.klingon_fire_genesis);
        klingonShieldUp = findViewById(R.id.klingon_shield_up);
        klingonTimer = findViewById(R.id.klingon_countdown_timer);
        klingonScoreView = findViewById(R.id.klingon_score);
        romulanFirePhasersButton = findViewById(R.id.romulan_fire_phasers);
        romulanFirePhotonsButton = findViewById(R.id.romulan_fire_photons);
        romulanFireGenesisButton = findViewById(R.id.romulan_fire_genesis);
        romulanShieldUp = findViewById(R.id.romulan_shield_up);
        romulanTimer = findViewById(R.id.romulan_countdown_timer);
        romulanScoreView = findViewById(R.id.romulan_score);
    }

    /* This method saves the UI state changes to the savedInstanceState bundle.
       This bundle will be passed to onCreate if the process is killed and restarted.*/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {


        savedInstanceState.putInt(KLINGON_SCORE, scoreKlingon);
        savedInstanceState.putInt(ROMULAN_SCORE, scoreRomulan);
        savedInstanceState.putBoolean(KLINGON_SHIELD_UP, isKlingonShieldUp);
        savedInstanceState.putBoolean(ROMULAN_SHIELD_UP, isRomulanShieldUp);
        if (isKlingonShieldUp) // if the Klingon shields are up, save the Countdown timer state
        {
            savedInstanceState.putLong(KLINGON_TIMER_MILLIS, klingonMillisUntilFinished);
        }
        if (isRomulanShieldUp) // if the Romulan shields are up, save the Countdown timer state
        {
            savedInstanceState.putLong(ROMULAN_TIMER_MILLIS, romulanMillisUntilFinished);
        }
        super.onSaveInstanceState(savedInstanceState);
    }
    /* Restore UI state from the savedInstanceState bundle
    This bundle has also been passed to onCreate. */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);


        scoreKlingon = savedInstanceState.getInt(KLINGON_SCORE);
        displayKlingon(scoreKlingon);
        scoreRomulan = savedInstanceState.getInt(ROMULAN_SCORE);
        displayRomulan(scoreRomulan);
        isKlingonShieldUp = savedInstanceState.getBoolean(KLINGON_SHIELD_UP);
        isRomulanShieldUp = savedInstanceState.getBoolean(ROMULAN_SHIELD_UP);
        if (isKlingonShieldUp)
        {
            klingonMillisUntilFinished = savedInstanceState.getLong(KLINGON_TIMER_MILLIS);
            klingonCountDownTimer = new KlingonCountDownTimer(klingonMillisUntilFinished, TIMER_INTERVAL);
            shieldUp(klingonCountDownTimer, klingonShieldUp, klingonFireGenesisButton);
        }
        if (isRomulanShieldUp)
        {
            romulanMillisUntilFinished = savedInstanceState.getLong(ROMULAN_TIMER_MILLIS);
            romulanCountDownTimer = new RomulanCountDownTimer(romulanMillisUntilFinished, TIMER_INTERVAL);
            shieldUp(romulanCountDownTimer, romulanShieldUp, romulanFireGenesisButton);
        }
    }

    /*
        This method takes the button that was pressed, and determines whether the Klingon score or
        Romulan Score should be increased and by how many points.
        If the shields are up, the other opponent fire buttons to do not increase the score.
        Also the person with the shields up cannot use their genesis weapon.
        @param View - the button pressed to initiate the callback method scorePoints
     */
    public void scorePoints(View view)
    {
        boolean isKlingon = false;

        if ((view == klingonFirePhasersButton) && (!isRomulanShieldUp))
        {
            scoreKlingon += PHASER_POINTS;
            isKlingon = true;
        }
        else if ((view == klingonFirePhotonsButton) && (!isRomulanShieldUp))
        {
            scoreKlingon += TORPEDO_POINTS;
            isKlingon = true;
        }
        else if ((view == klingonFireGenesisButton) && (!isKlingonShieldUp) && (!isRomulanShieldUp))
        {
            scoreKlingon += GENESIS_POINTS;
            isKlingon = true;
        }
        else if ((view == romulanFirePhasersButton) && (!isKlingonShieldUp))
        {
            scoreRomulan += PHASER_POINTS;
        }
        else if ((view == romulanFirePhotonsButton) && (!isKlingonShieldUp))
        {
            scoreRomulan += TORPEDO_POINTS;
        }
        else if ((view == romulanFireGenesisButton) && (!isKlingonShieldUp) && (!isRomulanShieldUp))
        {
            scoreRomulan += GENESIS_POINTS;
        }
        if (isKlingon)
            displayKlingon(scoreKlingon);
        else
            displayRomulan(scoreRomulan);
    }

    /**
     * Displays the score for Klingons
     */
    private void displayKlingon(int score)
    {
        klingonScoreView.setText(String.valueOf(score));
    }

    /**
     * Displays the given score for Romulans.
     */
    private void displayRomulan(int score)
    {
        romulanScoreView.setText(String.valueOf(score));
    }

    /*
        This method takes care of all UI events when the CountDownTimer reaches zero
        @param shieldUp - which Shields Up button was pressed
        @param fireGenesisButton - the Genesis button weapon is disabled when the Shields are up
        @param timerText - the TextView that is updated with the CountDownTimer to show how long the shields
                            will remain up (10 seconds or less)
         @return false - boolean value to indicate the shields are down
     */
    private boolean shieldDown(Button shieldUp, Button fireGenesisButton, TextView timerText)
    {
        shieldUp.setBackgroundResource(R.drawable.button);
        fireGenesisButton.setTextColor(ContextCompat.getColor(this,R.color.text_color));
        timerText.setText(TIMER_START_STRING);
        return false;
    }

     /*
        This method takes care of all UI events when the Shields Up button is pressed
        @param timer - The CountDownTimer to initialize and start
        @param shieldUpButton - the shields up button pressed, must change UI to show button is red
        @param genesisFireButton - the genesis weapon cannot be used when the shields are up,
                                   therefore disabled.
         @return false - boolean value to indicate the shields are up
     */
    private boolean shieldUp(CountDownTimer timer,  Button shieldUpButton, Button genesisFireButton)
    {
        shieldUpButton.setBackgroundResource(R.drawable.button_selected);
        genesisFireButton.setTextColor(ContextCompat.getColor(this,R.color.button_center_color));
        timer.start();
        return true;
    }

    /*
        Set the Klingon ship shields to down
     */
    private void klingonShieldDown()
    {
        isKlingonShieldUp = shieldDown(klingonShieldUp, klingonFireGenesisButton, klingonTimer);
    }
    /*
        Set the Klingon ship shields to up
    */
    public void klingonShieldUp(View view)
    {
        if (!isKlingonShieldUp)
        {
            klingonCountDownTimer = new KlingonCountDownTimer(TIMER_START_VALUE, TIMER_INTERVAL);
            isKlingonShieldUp = shieldUp(klingonCountDownTimer, klingonShieldUp, klingonFireGenesisButton);
        }
    }

    /*
        Set the Romulan ship shields to up
     */
    public void romulanShieldUp(View view)
    {
        if (!isRomulanShieldUp)
        {
            romulanCountDownTimer = new RomulanCountDownTimer(TIMER_START_VALUE, TIMER_INTERVAL);
            isRomulanShieldUp = shieldUp(romulanCountDownTimer, romulanShieldUp, romulanFireGenesisButton);
        }
    }
    /*
        Set the Romulan ship shields to down
     */
    private void romulanShieldDown()
    {
        isRomulanShieldUp = shieldDown(romulanShieldUp,romulanFireGenesisButton, romulanTimer);
    }

    /**
     * This method creates a countdown timer for the Klingon ship and returns the timer to the calling method
     */
    public class KlingonCountDownTimer extends CountDownTimer
    {

        public KlingonCountDownTimer(long klingonTime, long klingonInterval)
        {
            super(klingonTime, klingonInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            klingonMillisUntilFinished = millisUntilFinished;
            long first = millisUntilFinished / TIMER_INTERVAL;
            klingonTimer.setText(String.format("%02d", first ));

        }

        @Override
        public void onFinish()
        {
            klingonShieldDown();
        }
    }

    /**
     * This method creates a countdown timer for the Romulan ship and returns the timer to the calling method
     */
    public class RomulanCountDownTimer extends CountDownTimer
    {

        public RomulanCountDownTimer(long romulanTime, long romulanInterval)
        {
            super(romulanTime, romulanInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            romulanMillisUntilFinished = millisUntilFinished;
            long first = millisUntilFinished / TIMER_INTERVAL;
            romulanTimer.setText(String.format("%02d", first ));

        }

        @Override
        public void onFinish()
        {
            romulanShieldDown();
        }
    }

    /**
     * Reset the score to zero and update UI
     */
    protected void resetScores(View view)
    {
        if (view != null)
        {
            scoreKlingon = INITIAL_SCORE;
            scoreRomulan = INITIAL_SCORE;
            if (klingonCountDownTimer != null) klingonCountDownTimer.cancel();
            klingonShieldDown();
            if (romulanCountDownTimer != null) romulanCountDownTimer.cancel();
            romulanShieldDown();
            displayKlingon(scoreKlingon);
            displayRomulan(scoreRomulan);
        }

    }
}