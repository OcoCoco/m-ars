package com.example.m_ars_v2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AchievementActivity extends AppCompatActivity {

    ListView achievementListView;
    TextView achievementTextView;
//    ARActivity arActivity = new ARActivity();
    ArrayAdapter<String> arrayAdapter;

//    private boolean isUnlocked = arActivity.isachievementUnlocked;
    private int counter;

    ArrayList<String> achievementArrList;
    ArrayAdapter<String> achievementArrAdapater;
    String achievementString;
    Boolean isUnlocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        findViewById(R.id.achievementTextView);

        if (isUnlocked) {
            achievementTextView.setText("Achievements have not been unlocked");
        } else {
            achievementString = getIntent().getStringExtra("achievementString");
            isAchievementUnlocked(achievementString);
        }

    }

    public void isAchievementUnlocked(String achievementString){
//        if (isUnlocked){
        isUnlocked = true;
        achievementListView = findViewById(R.id.achievementListView);

        achievementArrList = new ArrayList<String>();
//            achievementArrList.add("test");

        achievementArrAdapater = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, achievementArrList);
        achievementListView.setAdapter(achievementArrAdapater);
        achievementArrList.add(achievementString);
        achievementArrAdapater.notifyDataSetChanged();

//            isUnlocked = true;
//        } else {
//            achievementTextView = findViewById(R.id.achievementTextView);
//            achievementTextView.setText("Achievements have not been unlocked");
//            isUnlocked = false;
//        }
//        return isUnlocked;
    }

}
