package com.example.android.factorapp;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity
{
    int score=0;
    private SharedPreferences mpreferences;
    private SharedPreferences.Editor meditor;

    private TextView mtxtview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.btn);
        TextView txt4  = (TextView) findViewById(R.id.textView4);
        txt4.setText("0");

        mtxtview=(TextView) findViewById(R.id.textView6);
        mpreferences= PreferenceManager.getDefaultSharedPreferences(this);
        meditor= mpreferences.edit();

        checksharedpref();



        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final ListView Factorlist = (ListView) findViewById(R.id.List_of_factors);
                Factorlist.setEnabled(true);
                EditText ed1 = (EditText) findViewById(R.id.edittext);
                String num = ed1.getEditableText().toString();
                final int val;
                try {
                    val = Integer.parseInt(num);
                }
                catch(NumberFormatException ex)
                {
                    Toast.makeText(MainActivity.this,"Enter a number", Toast.LENGTH_SHORT).show();
                    return;
                }
                long[] a = new long[val];
                long[] b = new long[val];
                long[] c = new long[3];
                int j = 0; int k=0;
                ed1.setText("");

                if(val<=4)
                {
                    Toast.makeText(MainActivity.this,"Enter a number greater than 4", Toast.LENGTH_SHORT).show();
                }
                else {

                    for (int i = 1; i < val; ++i) {
                        if (val % i == 0) {
                            a[j] = i;
                            j++;
                        } else {
                            b[k] = i;
                            k++;
                        }
                    }

                    int rnd,rnd1=0,rnd2=0;

                    rnd = new Random().nextInt(j);
                    while(rnd1==rnd2) {
                        rnd1 = new Random().nextInt(k);
                        rnd2 = new Random().nextInt(k);
                    }
                    c[0] = a[rnd];
                    c[1] = b[rnd1];
                    c[2] = b[rnd2];

                    final ArrayList<Integer> fact = new ArrayList<>(c.length);

                    for (long i : c) {
                        fact.add(Integer.valueOf((int) i));
                    }
                    Collections.shuffle(fact);
                    ArrayAdapter adapter = new ArrayAdapter<Integer>(MainActivity.this, android.R.layout.simple_list_item_1, fact);
                    Factorlist.setAdapter(adapter);


                    Factorlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            TextView txt4  = (TextView) findViewById(R.id.textView4);


                            Factorlist.setEnabled(false);
                            if (val%fact.get(position)==0) {


                                Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                                Factorlist.getChildAt(position).setBackgroundColor(Color.GREEN);
                                 score=score+10;
                                txt4.setText("" + score);

                            }

                            else
                                for(int i=0; i<=2;i++) {
                                    if (val % fact.get(i) == 0) {
                                        Toast.makeText(MainActivity.this, "Incorrect! Game Over", Toast.LENGTH_SHORT).show();
                                        Factorlist.getChildAt(i).setBackgroundColor(Color.GREEN);
                                        Factorlist.getChildAt(position).setBackgroundColor(Color.RED);
                                    }
                                    if(score> mpreferences.getInt("score",0)) {
                                        meditor.putInt("score",score);
                                        meditor.commit();
                                    }
                                    score=0;
                                    txt4.setText(""+score);



                                }

                        }
                    });

                }



            }
        });

    }
    private void checksharedpref()
    {

        int hiscore= mpreferences.getInt("score",0);
        mtxtview.setText(hiscore);
    }


}
