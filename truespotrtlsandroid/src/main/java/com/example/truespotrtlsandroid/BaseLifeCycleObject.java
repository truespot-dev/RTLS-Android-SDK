package com.example.truespotrtlsandroid;

import timber.log.Timber;

public abstract class BaseLifeCycleObject {
    private String TAG;
    private int createdCount;
    private int resumedCount;

    public void create() {
        TAG = getClass().getSimpleName();

        createdCount++;
        if (createdCount == 1) {
            onCreate();
        } else {
            Timber.d(TAG + ".create() already created. createdCount:" + createdCount);
        }
    }

    protected abstract void onCreate();

    public void resume() {
        if (createdCount == 0) {
            //throw new RuntimeException("Error. Create before resuming.");
            Timber.e(TAG + ".resume(). Create before resuming");
            return;
        }
        resumedCount++;
        if (resumedCount == 1) {
            onResume();
        } else {
            Timber.d(TAG + ".resume() already resumed. resumedCount:" + resumedCount);
        }
    }

    protected abstract void onResume();

    public void pause() {
        if (resumedCount <= 0) {
            //throw new RuntimeException(TAG + ".Error. Resume before pausing.");
            Timber.e(TAG + ".pause(). Resume before pausing");
            return;
        }
        resumedCount--;
        if (resumedCount == 0) {
            onPause();
        } else {
            Timber.d(TAG + ".pause() ignoring. resumedCount:" + resumedCount);
        }
    }

    protected abstract void onPause();

    public void destroy() {
        if (createdCount <= 0) {
            //throw new RuntimeException(TAG + ".Error. Create before destroying.");
            Timber.e(TAG + ".destroy(). Create before destroying.");
            return;
        }
        createdCount--;
        if (createdCount == 0) {
            onDestroy();
        } else {
            Timber.d(TAG + ".destroy() ignoring. createdCount:" + createdCount);
        }
    }

    protected abstract void onDestroy();

    protected boolean isResumed() {
        return resumedCount >= 1;
    }
}
