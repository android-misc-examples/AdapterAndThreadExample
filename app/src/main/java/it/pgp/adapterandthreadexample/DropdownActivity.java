package it.pgp.adapterandthreadexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class DropdownActivity extends Activity {

    ImageButton dropdownButton;
    View dropdownLayout;
    STATUS currentStatus = STATUS.DOWN;

    static DropdownActivity instance;

    // Dropdown layout views
    Button startSearch;
    Button stopSearch;
    Button clearResults;

    ListView resultsView;

    void startSearchTask(View unused) {
        MainAdapter.reset();
        resultsView.setAdapter(MainAdapter.instance);

        if (!UpdaterThread.createAndStart()){
            Toast.makeText(this, "There is an already active search task", Toast.LENGTH_SHORT).show();
        }
    }

    void stopSearchTask(View unused) {
        UpdaterThread.stopAndDestroy();
    }

    // called by UpdaterThread
    public void toggleSearchButtons(boolean searchIsActive) {
        startSearch.setEnabled(!searchIsActive);
        stopSearch.setEnabled(searchIsActive);
        clearResults.setEnabled(!searchIsActive);
    }

    private enum STATUS {
        UP(android.R.drawable.arrow_down_float),
        DOWN(android.R.drawable.arrow_up_float);

        int drawable;
        STATUS(int drawable) {
            this.drawable = drawable;
        }
        public int getDrawable() {
            return drawable;
        }
        STATUS next() {
            return this==UP?DOWN:UP;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_dropdown);

        dropdownLayout = findViewById(R.id.dropdown_layout);
        dropdownButton = findViewById(R.id.dropdown_button);
        dropdownButton.setImageResource(currentStatus.getDrawable());

        // animated layout views
        startSearch = findViewById(R.id.startSearch);
        stopSearch = findViewById(R.id.stopSearch);
        clearResults = findViewById(R.id.clearResults);

        startSearch.setOnClickListener(this::startSearchTask);
        stopSearch.setOnClickListener(this::stopSearchTask);
        clearResults.setOnClickListener(v->MainAdapter.reset());

        toggleSearchButtons(UpdaterThread.isSearchActive());

        resultsView = findViewById(R.id.results_view);
        MainAdapter.createIfNotExisting();
        resultsView.setAdapter(MainAdapter.instance);

        dropdownLayout.bringToFront();
        dropdownButton.bringToFront();
    }

    public void onSlideViewButtonClick(View unused) {
        dropdownLayout.animate().y(
                (currentStatus==STATUS.DOWN)?
                -dropdownLayout.getHeight():0).setDuration(1000).start();
        currentStatus = currentStatus.next();
        dropdownButton.bringToFront();
        dropdownButton.setImageResource(currentStatus.getDrawable());
    }
}
