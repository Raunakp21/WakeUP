package com.raunak.alarmdemo4.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;
import com.arbelkilani.clock.Clock;
import com.arbelkilani.clock.enumeration.ClockType;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.raunak.alarmdemo4.Fragments.HomeFragment;
import com.raunak.alarmdemo4.HelperClasses.AlarmsDBhelperClass;
import com.raunak.alarmdemo4.R;
import com.raunak.alarmdemo4.Recievers.AlarmReceiver;

import java.io.IOException;
import java.util.Calendar;

public class SnoozeActivity extends AppCompatActivity implements OnStateChangeListener {

    AlarmsDBhelperClass mBhelperClass;
    SQLiteDatabase db;
    SwipeButton btnSwipe;
    Clock mClock;
    int hour,min;
    MediaPlayer mp = new MediaPlayer();
    String songPath;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        min = c.get(Calendar.MINUTE);
        Intent intent = getIntent();
        btnSwipe = findViewById(R.id.btnSwipe);
        mClock = findViewById(R.id.clock);
        mBhelperClass = new AlarmsDBhelperClass(this);
        db= mBhelperClass.getWritableDatabase();
        Log.d("gaurnagSnooze",""+hour+" "+min);
        Cursor cursor = db.rawQuery("SELECT musicPath FROM alarms WHERE hours=? AND minutes=?",new String[]{""+hour,""+min});
        if (cursor.moveToFirst()) {
            songPath = cursor.getString(cursor.getColumnIndex(AlarmsDBhelperClass.MUSIC_PATH));
            Log.d("notOkay ","songpath initialized!");
        }
        cursor.close();

        Log.d("gaurangaSnooze","songPath : "+songPath);
        try {
            mp.setDataSource(songPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();

    }
    @Override
    public void onStateChange(boolean active) {
        mp.stop();
        Toast.makeText(getApplicationContext(), "Alarm Stopped", Toast.LENGTH_SHORT).show();
        ContentValues values = new ContentValues();
        values.put("status","OFF");
        db.update("alarms",values,"alarm_mode=?",new String[]{"⚡"});
        finish();
    }
}
