package com.auk.wakey;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.auk.wakey.adapter.AlarmViewAdapter;
import com.auk.wakey.model.Alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmActivity
        extends AppCompatActivity
        implements AlarmHomeFragment.OnFragmentInteractionListener, VoiceRecorderFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {

    private static final String TAG = "AlarmActivity";
    final List<Alarm> alarms = new ArrayList<>();
    @BindView(R.id.recButton)
    Button recButton;
    @BindView(R.id.alarmRecyclerView)
    RecyclerView alarmRecyclerView;
    @BindView(R.id.alarmTimer)
    TimePicker alarmTimer;
    @BindView(R.id.gmtButton)
    ToggleButton gmtButton;
    AlarmHandler alarmHandler;


    public Calendar getCalendarFromTimerPicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarmTimer.getCurrentHour());
        calendar.set(Calendar.MINUTE, alarmTimer.getCurrentMinute());
        if (gmtButton.isChecked()) {
            calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return calendar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        try {
            alarmHandler = AlarmHandler.getInstance(this);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, "Fatal error, restart app", Toast.LENGTH_LONG).show();
            return;
        }
        recButton.setEnabled(false);
        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        AlarmViewAdapter alarmViewAdapter = new AlarmViewAdapter(this, alarms);
        alarmRecyclerView.setAdapter(alarmViewAdapter);

        alarmHandler.getAlarms(alarms, (alarms, updated) -> new Handler(getMainLooper()).post(() -> recButton.setEnabled(true)));


        recButton.setOnLongClickListener(view -> {
            Toast.makeText(this, "Audio recording started", Toast.LENGTH_LONG).show();
            return true;
        });
        recButton.setOnClickListener(view -> {
            Alarm alarm = new Alarm(UUID.randomUUID().toString(), true, getCalendarFromTimerPicker(), new boolean[7], "", false);
            alarmHandler.addAlarm(alarm, (alrm, didUpdate) -> new Handler(getMainLooper()).post(() -> {
                if (didUpdate) {
                    alarmViewAdapter.notifyDataSetChanged();
                    Toast.makeText(this, alrm.getUid(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Adding alarm failed", Toast.LENGTH_LONG).show();
                }
            }));
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
