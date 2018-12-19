package it.pgp.adapterandthreadexample;

import android.util.Log;

// skeleton for FindUpdatesThread
class UpdaterThread extends Thread {
    private UpdaterThread() {}

    private static UpdaterThread instance;

    static synchronized boolean isSearchActive() {
        return !(instance == null || instance.getState()==State.TERMINATED);
    }

    /**
     * allow start of a new search only if no search has ever been done or the current one has ended
     */
    static synchronized boolean createAndStart() {
        if (instance == null || instance.getState()==State.TERMINATED) {
            instance = new UpdaterThread();
            instance.start();
            return true;
        }
        else return false;
    }

    static synchronized void stopAndDestroy() {
        if (instance != null) {
            instance.interrupt();
            instance = null;
        }
    }

    @Override
    public void run() {
        DropdownActivity.instance.runOnUiThread(()->DropdownActivity.instance.toggleSearchButtons(true));
        for(int i=0;i<100;i++) {
            if (isInterrupted()) {
                Log.e("Thread",this.getClass().getName()+" interrupted");
                DropdownActivity.instance.runOnUiThread(()->DropdownActivity.instance.toggleSearchButtons(false));
                return;
            }
            String k = i+"";
            DropdownActivity.instance.runOnUiThread(() -> MainAdapter.instance.add(k));
            try {
                Log.e("Sleeping","Sleeping");
                sleep(1000);
            }
            catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
