package it.pgp.adapterandthreadexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

// skeleton for FindActivity
public class MainActivity extends Activity {

    public static MainActivity mainActivity;
    Button startShowAndHideActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);

        startShowAndHideActivity = findViewById(R.id.startShowAndHideActivity);
        startShowAndHideActivity.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,DropdownActivity.class);
            startActivity(intent);
        });
    }
}
