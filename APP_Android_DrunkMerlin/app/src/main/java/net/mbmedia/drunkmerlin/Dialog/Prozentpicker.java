package net.mbmedia.drunkmerlin.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.fragment.app.DialogFragment;

import net.mbmedia.drunkmerlin.R;


public class Prozentpicker extends DialogFragment {

    private TextView txt_prozent_auserhalb;
    private TextView txt_prozent_innerhalb;

    private SeekBar seekBar;

    public Prozentpicker(TextView txt_prozent){
        this.txt_prozent_auserhalb = txt_prozent;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_prozent, null);


        builder.setView(view)
                .setPositiveButton(getActivity().getApplicationContext().getString(R.string.btn_Fertig), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        txt_prozent_auserhalb.setText(txt_prozent_innerhalb.getText());
                    }
                })
                .setNegativeButton(getActivity().getApplicationContext().getString(R.string.btn_abbrechen), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // User cancelled the dialog
                    }
                });

        seekBar = view.findViewById(R.id.seekBar);

        txt_prozent_innerhalb = view.findViewById(R.id.txt_prozent_innerhalb);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_prozent_innerhalb.setText(progress + " %");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return builder.create();
    }
}
