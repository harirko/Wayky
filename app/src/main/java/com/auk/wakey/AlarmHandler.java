package com.auk.wakey;

import android.content.Context;
import android.util.Log;

import com.auk.wakey.model.Alarm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

//TODO add something real like SQLite
public class AlarmHandler {
    private static final AlarmHandler ourInstance = new AlarmHandler();
    private static final int UPDATED = 0;
    private static final int FAILED = 1;
    private static String TAG = "AlarmHandler";
    List<OnStorageUpdate> lists = new LinkedList<>();
    private String dbFilename = "data.json";
    private Gson gson;
    private List<Alarm> alarms;
    private Context context;

    private AlarmHandler() {
        gson = new Gson();
    }

    public static AlarmHandler getInstance() {
        return ourInstance;
    }

    public static AlarmHandler getInstance(Context context) throws Exception {
        if (ourInstance.context != null) {
            Log.e(TAG, "Context cant be set more than once");
            throw new Exception();
        }
        ourInstance.context = context;
        return ourInstance;
    }

    /*
     * Returns empty string on error
     */
    private String readJSON() {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(dbFilename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    /*
     * Returns true if write was success, false otherwise
     */
    private boolean writeJSON(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(dbFilename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            return true;
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }
    }

    public void getAlarms(final List<Alarm> alarms, OnAlarmsUpdate onAlarmsUpdate) {
        this.alarms = alarms;
        if (alarms == null) {
            return;
        }
        Thread thread = new Thread(() -> {
            List<Alarm> alms = gson.fromJson(readJSON(), new TypeToken<List<Alarm>>() {
            }.getType());
            if (alms == null) {
                onAlarmsUpdate.onAlarmsUpdate(alarms, false);
                return;
            }
            alarms.addAll(alms);
            onAlarmsUpdate.onAlarmsUpdate(alarms, true);
        });
        thread.start();
    }

    public void setAlarms(final List<Alarm> alarms, OnAlarmsUpdate onAlarmsUpdate) {
        if (this.alarms == null) {
            Log.e(TAG, "Alarms is empty in the first place, reload app");
            return;
        }
        Thread thread = new Thread(() -> {
            String data = gson.toJson(alarms, new TypeToken<List<Alarm>>() {
            }.getType());
            if (writeJSON(data)) {
                onAlarmsUpdate.onAlarmsUpdate(alarms, true);
            } else {
                onAlarmsUpdate.onAlarmsUpdate(null, false);
            }
        });
        thread.start();
    }

    public void addAlarm(Alarm alarm, OnStorageUpdate onStorageUpdate) {
        if (onStorageUpdate == null || alarm == null) {
            return;
        }
        alarms.add(alarm);//TODO add to db and call based on result and do it with queue
        Thread thread = new Thread(() -> {
            String data = gson.toJson(alarms, List.class);
            if (writeJSON(data)) {
                onStorageUpdate.onStorageUpdate(alarm, true);
            } else {
                onStorageUpdate.onStorageUpdate(null, false);
            }
        });
        thread.start();
    }

    public void removeAlarm(Alarm alarm, OnStorageUpdate onStorageUpdate) {
        if (onStorageUpdate == null) {
            return;
        }
        //TODO remove from db and call based on result and do it with queue
        onStorageUpdate.onStorageUpdate(alarm, true);
    }

    public interface OnStorageUpdate {
        void onStorageUpdate(Alarm alarm, boolean didUpdate);
    }

    public interface OnAlarmsUpdate {
        void onAlarmsUpdate(List<Alarm> alarms, boolean didUpdate);
    }

}
