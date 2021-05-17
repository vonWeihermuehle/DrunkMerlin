package net.mbmedia.drunkmerlin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class Toaster {

    private Context context;

    public Toaster(Context context){
        this.context = context;
    }

    public void sage(String zusagen){

        final String s = zusagen;


        Thread thread = new Thread(){
            public void run(){
                Looper.prepare();//Call looper.prepare()

                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, s, duration);
                toast.show();

                Looper.loop();
            }
        };
        thread.start();



    }
}
