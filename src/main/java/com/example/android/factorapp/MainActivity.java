package com.example.android.factorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    int score = 0;
    private SharedPreferences mpreferences;
    private SharedPreferences.Editor meditor;

    private TextView mtxtview;
    private CountDownTimer countDownTimer;
    Vibrator vibrator;
    private boolean timerrunning = false;
    private long timeleft = 11000;
    ArrayList<Integer> fact = new ArrayList<>(3);
    int pos;
    private boolean ans;
    int val;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView Factorlist = (ListView) findViewById(R.id.List_of_factors);
        final Button button1 = (Button) findViewById(R.id.btn);
        Button button2 = (Button) findViewById(R.id.btn_reset);
        final TextView txt4 = (TextView) findViewById(R.id.textView4);
        txt4.setText("0");

        mtxtview = (TextView) findViewById(R.id.textView6);
        mpreferences = this.getPreferences(Context.MODE_PRIVATE);
        meditor = mpreferences.edit();

        final TextView txt7 = (TextView) findViewById(R.id.textView7);
        checksharedpref();

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Factorlist.setVisibility(View.VISIBLE);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(300);
                txt4.setText("0");
                if (timerrunning) {
                    countDownTimer.cancel();
                }
                txt7.setText("");
                meditor.putInt("hiscore", 0);
                meditor.apply();
                int hiscore = mpreferences.getInt("hiscore", 0);
                mtxtview.setText("" + hiscore);
                score = 0;

                Factorlist.setVisibility(View.INVISIBLE);
                button1.setEnabled(true);

            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerrunning = true;

                Factorlist.setVisibility(View.VISIBLE);
                Factorlist.setEnabled(true);
                button1.setEnabled(false);
                EditText ed1 = (EditText) findViewById(R.id.edittext);
                String num = ed1.getEditableText().toString();

                try {
                    val = Integer.parseInt(num);
                } catch (NumberFormatException ex) {
                    Toast.makeText(MainActivity.this, "Enter a number", Toast.LENGTH_SHORT).show();
                    button1.setEnabled(true);
                    Factorlist.setEnabled(false);
                    Factorlist.setVisibility(View.INVISIBLE);
                    return;
                }

                long[] a = new long[val];
                long[] b = new long[val];
                long[] c = new long[3];
                int j = 0;
                int k = 0;
                ed1.setText("");

                if (val <= 4) {
                    Toast.makeText(MainActivity.this, "Enter a number greater than 4", Toast.LENGTH_SHORT).show();
                    button1.setEnabled(true);
                    timerrunning = false;
                    Factorlist.setVisibility(View.INVISIBLE);
                } else {
                    timeleft = 11000;
                    startTimer();
                    for (int i = 1; i < val; ++i) {
                        if (val % i == 0) {
                            a[j] = i;
                            j++;
                        } else {
                            b[k] = i;
                            k++;
                        }
                    }

                    int rnd, rnd1 = 0, rnd2 = 0;

                    rnd = new Random().nextInt(j);
                    while (rnd1 == rnd2) {
                        rnd1 = new Random().nextInt(k);
                        rnd2 = new Random().nextInt(k);
                    }
                    c[0] = a[rnd];
                    c[1] = b[rnd1];
                    c[2] = b[rnd2];


                    fact.clear();
                    for (long i : c) {
                        fact.add(Integer.valueOf((int) i));
                    }
                    Collections.shuffle(fact);
                    ArrayAdapter adapter = new ArrayAdapter<Integer>(MainActivity.this, android.R.layout.simple_list_item_1, fact);
                    Factorlist.setAdapter(adapter);


                    Factorlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            TextView txt4 = (TextView) findViewById(R.id.textView4);
                            button1.setEnabled(true);
                            pos = position;

                            Factorlist.setEnabled(false);
                            if (val % fact.get(position) == 0) {
                                ans = true;
                                Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                                Factorlist.getChildAt(position).setBackgroundColor(Color.GREEN);
                                score = score + 10;
                                txt4.setText("" + score);
                                countDownTimer.cancel();
                                timerrunning = false;
                                txt7.setText("");
                                if (score > mpreferences.getInt("hiscore", 0)) {
                                    meditor.putInt("hiscore", score);
                                    meditor.apply();
                                    int hiscore = mpreferences.getInt("hiscore", 0);
                                    mtxtview.setText("" + hiscore);
                                }


                            } else {
                                ans = false;
                                vibrator.vibrate(300);
                                for (int i = 0; i <= 2; i++) {
                                    if (val % fact.get(i) == 0) {
                                        Toast.makeText(MainActivity.this, "Incorrect! Game Over", Toast.LENGTH_SHORT).show();
                                        Factorlist.getChildAt(i).setBackgroundColor(Color.GREEN);
                                        Factorlist.getChildAt(position).setBackgroundColor(Color.RED);
                                    }

                                    score = 0;
                                    txt4.setText("" + score);
                                    countDownTimer.cancel();
                                    timerrunning = false;
                                    txt7.setText("Game Over!!!");


                                }
                            }
                        }
                    });

                }


            }
        });

    }


    private void startTimer() {


        countDownTimer = new CountDownTimer(timeleft, 1000) {
            TextView txt4 = (TextView) findViewById(R.id.textView4);
            final ListView Factorlist = (ListView) findViewById(R.id.List_of_factors);
            final TextView txt7 = (TextView) findViewById(R.id.textView7);
            final Button button1 = (Button) findViewById(R.id.btn);

            public void onTick(long millisUntilFinished) {
                timeleft = millisUntilFinished;
                txt7.setText("" + millisUntilFinished / 1000);
            }


            public void onFinish() {
                vibrator.vibrate(300);
                txt7.setText("Time Up, Game Over!!");
                if (score > mpreferences.getInt("hiscore", 0)) {
                    meditor.putInt("hiscore", score);
                    meditor.apply();
                    int hiscore = mpreferences.getInt("hiscore", 0);
                    mtxtview.setText("" + hiscore);

                }
                score = 0;
                txt4.setText("" + score);
                Factorlist.setVisibility(View.INVISIBLE);
                button1.setEnabled(true);
                timerrunning = false;
                timeleft = 11000;


            }


        }.start();
    }

    private void setcolour(ListView Factorlist)
    {

        if (ans) {
            Factorlist.getChildAt(pos).setBackgroundColor(Color.GREEN);
        }
        else {
            Factorlist.getChildAt(pos).setBackgroundColor(Color.RED);
            for (int i = 0; i <= 2; i++) {
                if (val % fact.get(i) == 0) {
                    Factorlist.getChildAt(i).setBackgroundColor(Color.GREEN);

                }

            }
        }
    }


    private void checksharedpref() {

        int hiscore = mpreferences.getInt("hiscore", 0);
        mtxtview.setText("" + hiscore);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score", score);
        outState.putBoolean("timerrunning", timerrunning);
        outState.putLong("timeleft", timeleft);
        outState.putIntegerArrayList("fact", fact);
        outState.putBoolean("ans", ans);
        outState.putInt("pos", pos);
        outState.putInt("val", val);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ans = savedInstanceState.getBoolean("ans");
        pos = savedInstanceState.getInt("pos");
        TextView txt4 = (TextView) findViewById(R.id.textView4);
        fact.clear();
        fact = savedInstanceState.getIntegerArrayList("fact");
        final ListView Factorlist = (ListView) findViewById(R.id.List_of_factors);

        timeleft = savedInstanceState.getLong("timeleft");
        score = savedInstanceState.getInt("score");
        timerrunning = savedInstanceState.getBoolean("timerrunning");
        txt4.setText("" + score);
        final Button button1 = (Button) findViewById(R.id.btn);
        if (timerrunning) {
            button1.setEnabled(false);
            startTimer();

        } else
            button1.setEnabled(true);
        ArrayAdapter adapter = new ArrayAdapter<Integer>(MainActivity.this, android.R.layout.simple_list_item_1, fact);
            Factorlist.setAdapter(adapter);
          setcolour(Factorlist);
        }


    }

