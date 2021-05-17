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


public class Mengepicker extends DialogFragment {

    private TextView txt_menge_auserhalb;
    private TextView txt_menge_innerhalb;

    private SeekBar seekBar;

    public Mengepicker(TextView txt_menge){
        this.txt_menge_auserhalb = txt_menge;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_menge, null);


        builder.setView(view)
                .setPositiveButton(getActivity().getApplicationContext().getString(R.string.btn_Fertig), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        txt_menge_auserhalb.setText(txt_menge_innerhalb.getText());
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



        txt_menge_innerhalb = view.findViewById(R.id.txt_menge_innerhalb);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                txt_menge_innerhalb.setText(progress + " ml");

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
