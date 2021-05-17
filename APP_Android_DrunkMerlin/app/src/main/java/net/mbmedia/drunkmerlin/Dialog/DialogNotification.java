package net.mbmedia.drunkmerlin.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import net.mbmedia.drunkmerlin.DB_online.Drink.DBshowDrink;
import net.mbmedia.drunkmerlin.R;
import net.mbmedia.drunkmerlin.TO.Drink;

public class DialogNotification extends Dialog {

    private Activity activity;

    private String text;
    private String author;

    private TextView txt_name;
    private TextView txt_text;
    private TextView txt_ok;



    public DialogNotification(Activity activity,String text, String author){
        super(activity);
        this.activity = activity;
        this.text = text;
        this.author = author;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_drinkuebersicht);

        txt_name = findViewById(R.id.txt_name);
        txt_text = findViewById(R.id.txt_text);
        txt_ok = findViewById(R.id.txt_ok);

        txt_name.setText(author);
        txt_text.setText(Html.fromHtml(text));

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

}
