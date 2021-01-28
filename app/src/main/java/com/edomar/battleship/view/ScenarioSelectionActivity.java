package com.edomar.battleship.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.edomar.battleship.R;
import com.edomar.battleship.utils.Utils;

public class ScenarioSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ImageView ImageViewBadge;

    Button mRussianButton, mClassicButton, mStandardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_selection);

        sp = getSharedPreferences(getString(R.string.configuration_preference_key), MODE_PRIVATE);
        editor = sp.edit();

        /**Badge Configuration**/
        ImageViewBadge = (ImageView) findViewById(R.id.badge_image_view);
        //setFlagOfImageButtonFlag(sp.getString(getString(R.string.flag_key), "USA"));
        Utils.setFlagOfBadge(sp.getString(getString(R.string.flag_key), "USA"), ImageViewBadge);

        /**Buttons**/
        mRussianButton = (Button) findViewById(R.id.russian_button);
        mClassicButton = (Button) findViewById(R.id.classic_button);
        mStandardButton = (Button) findViewById(R.id.standard_button);

        mRussianButton.setOnClickListener(this);
        mRussianButton.setOnClickListener(this);
        mRussianButton.setOnClickListener(this);
    }

    /** Una volta cliccato un pulsante si inizia la partita**/
    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        Intent intent = new Intent(ScenarioSelectionActivity.this, GameActivity.class);
        intent.putExtra("scenario", button.getText());
        startActivity(intent);
        finish();
    }
}