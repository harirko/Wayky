package com.auk.wakey.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.auk.wakey.AlarmHandler;
import com.auk.wakey.R;
import com.auk.wakey.model.Alarm;
import com.auk.wakey.view.DaysToggle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmViewAdapter extends RecyclerView.Adapter<AlarmViewAdapter.AlarmViewHolder> {

    private final Context context;
    private final List<Alarm> alarms;
    private AlarmHandler alarmHandler = AlarmHandler.getInstance();

    public AlarmViewAdapter(Context context, List<Alarm> alarms) {
        this.context = context;
        this.alarms = alarms;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.alarm_item, viewGroup, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder alarmViewHolder, int i) {
        Alarm alarm = alarms.get(i);
        alarmViewHolder.alarmIsOn.setChecked(alarm.getIsOn());
        alarmViewHolder.alarmDaysToggle.setRepeatedDays(alarm.getRepeats());
        alarmViewHolder.alarmIsOn.setOnCheckedChangeListener((compoundButton, b) -> {
            alarm.setIsOn(b);
            alarmHandler.setAlarms(alarms, (alms, didUpdate) ->
                    new Handler(context.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Updated in db", Toast.LENGTH_SHORT).show();
                    }));
        });
        alarmViewHolder.itemView.setOnLongClickListener(view -> {
            alarmHandler.removeAlarm(alarm, (alms, didUpdate) -> new Handler(context.getMainLooper()).post(() -> {

                if (didUpdate) {
                    Toast.makeText(context, "Updated in db", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }));
            return true;
        });
        alarmViewHolder.alarmDaysToggle.setOnDaysChangedListener(days -> {
            alarm.setRepeats(days);
            alarmHandler.setAlarms(alarms, (alms, didUpdate) -> new Handler(context.getMainLooper()).post(() -> {
                if (didUpdate) {
                    Toast.makeText(context, "Days changed", Toast.LENGTH_LONG).show();
                }
            }));
        });
        //TODO get is24 from SharedPreferences
        String time = TimeFormatFactory.getFormat(alarm.getAlarmDate(), false);
        alarmViewHolder.alarmTimer.setText(time);
        alarmViewHolder.alarmDescription.setText("Morning Alarm");
        if (alarm.getRingtoneUrl() == null || alarm.getRingtoneUrl().isEmpty()) {
            alarmViewHolder.alarmRingtoneButton.setText("Default Ringtone");
        } else {
            alarmViewHolder.alarmRingtoneButton.setText(alarm.getRingtoneUrl());
        }
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    static class TimeFormatFactory {
        static String getFormat(Calendar calendar, boolean is24) {
            return SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime()) + " " + calendar.getTimeZone().getDisplayName();
        }
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.alarmTimer)
        TextView alarmTimer;
        @BindView(R.id.alarmDescription)
        TextView alarmDescription;
        @BindView(R.id.alarmRingtoneButton)
        Button alarmRingtoneButton;
        @BindView(R.id.alarmDaysToggle)
        DaysToggle alarmDaysToggle;
        @BindView(R.id.alarmIsOn)
        CheckBox alarmIsOn;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
