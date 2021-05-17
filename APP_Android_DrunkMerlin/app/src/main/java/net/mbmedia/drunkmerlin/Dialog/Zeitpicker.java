package net.mbmedia.drunkmerlin.Dialog;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Zeitpicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TextView zeit;

    public Zeitpicker(TextView txt_zeit){
        this.zeit = txt_zeit;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String h = "";
        if(hourOfDay < 10 ){
            h = "0" + hourOfDay;
        }else{
            h = ""+ hourOfDay;
        }

        String min ="";
        if(minute < 10){
            min = "0" + minute;
        }else{
            min = "" + minute;
        }
        zeit.setText(h + ":" + min + " " + getDatum());
    }

    public String getDatum(){
        SimpleDateFormat formatter= new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}
