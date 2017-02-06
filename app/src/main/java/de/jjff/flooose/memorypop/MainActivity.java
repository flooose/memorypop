package de.jjff.flooose.memorypop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

import static de.jjff.flooose.memorypop.R.id.newWordLayout;
import static de.jjff.flooose.memorypop.R.id.playLayout;

public class MainActivity extends AppCompatActivity {

    private View playButton;
    private View newWordButton;

    @BindView(R.id.startLayout) View startLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        playButton = startLayout.findViewById(R.id.play_button);
        newWordButton = startLayout.findViewById(R.id.new_word_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                startActivity(intent);

//                playLayout.setVisibility(View.VISIBLE);
//                newWordLayout.setVisibility(View.GONE);
            }
        });

        newWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewEntryActivity.class);
                startActivity(intent);
//                newWordLayout.setVisibility(View.VISIBLE);
//                playLayout.setVisibility(View.GONE);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
