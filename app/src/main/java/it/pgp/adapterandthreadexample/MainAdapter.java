package it.pgp.adapterandthreadexample;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Construction rationale behind asynchronous adapter (keeping state and surviving across activities-listviews instance, while preserving real-time notify requirements):
 * - Adapter is nullable singleton
 * - It is created using an activity reference, in order to perform add method via runOnUIThread
 * - Even if activity is recreated, the old reference is not GCed, and some way runOnUIThread seems
 *   to continue working even against the new listview instance in the new activity instance
 */

// skeleton for FindAdapter
class MainAdapter extends ArrayAdapter<String> {

    static MainAdapter instance;

    private MainAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    static synchronized void createIfNotExisting() {
        if (instance == null) // with this check, the old adapter is recycled with the new activity instance
            instance = new MainAdapter(MainActivity.mainActivity.getApplicationContext());
    }

    static synchronized void reset() {
        createIfNotExisting();
        instance.clear();
    }
}
