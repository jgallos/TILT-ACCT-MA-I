package com.programmer.jgallos.ma_i;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

import com.instacart.library.truetime.TrueTime;
import com.instacart.library.truetime.TrueTimeRx;

import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class TimeClass extends Application {

    private static final String TAG = TimeClass.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        initRxTrueTime();
//        initTrueTime();
    }

    /**
     * init the TrueTime using a AsyncTask.
     */
    private void initTrueTime() {
        new InitTrueTimeAsyncTask().execute();
    }

    // a little part of me died, having to use this
    private class InitTrueTimeAsyncTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            try {
                TrueTime.build()
                        //.withSharedPreferences(SampleActivity.this)
                        .withNtpHost("time.google.com")
                        .withLoggingEnabled(false)
                        .withSharedPreferencesCache(TimeClass.this)
                        .withConnectionTimeout(3_1428)
                        .initialize();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "something went wrong when trying to initialize TrueTime", e);
            }
            return null;
        }
    }

    /**
     * Initialize the TrueTime using RxJava.
     */
    private void initRxTrueTime() {
        DisposableSingleObserver<Date> disposable = TrueTimeRx.build()
                .withConnectionTimeout(31_428)
                .withRetryCount(100)
                .withSharedPreferencesCache(this)
                .withLoggingEnabled(true)
                .initializeRx("time.google.com")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Date>() {
                    @Override
                    public void onSuccess(Date date) {
                        Log.d(TAG, "Success initialized TrueTime :" + date.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "something went wrong when trying to initializeRx TrueTime", e);
                    }
                });
    }


}
