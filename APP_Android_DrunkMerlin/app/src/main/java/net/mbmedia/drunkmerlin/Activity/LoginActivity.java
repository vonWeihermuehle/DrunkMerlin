package net.mbmedia.drunkmerlin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.mbmedia.drunkmerlin.DB_online.User.DBLogin;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.R;

public class LoginActivity extends ActivityFunktionen {

    TextView btn_login;
    TextView btn_register;

    EditText txt_username;
    EditText txt_pw;

    ProgressBar progress;

    Boolean istEingeloogt = false;

    LokalSpeichern lokalSpeichern;

    public static final String ERSTER_START = "ersterStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initGUI();

        lokalSpeichern = new LokalSpeichern(this);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggeEin();
            }
        });

        Thread thread = new Thread(){
            public void run(){

                //läuft auch in RegisterActivity weiter

                while(!istEingeloogt) {
                    checkObEingeloggt();
                    wennEingeloggt(istEingeloogt);


                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        thread.start();


    }

    public void checkObEingeloggt(){
        if(!lokalSpeichern.getUserID().equals("null")){
            istEingeloogt = true;
        }
    }

    public void wennEingeloggt(Boolean istEingeloggt){
        if(istEingeloggt){

            if(lokalSpeichern.getGeschlecht().equals("null") || lokalSpeichern.getGewicht().equals("null")){
                Intent intent = new Intent(this, EinstellungenActivity.class);

                /*
                Wenn man sich das erste Mal einloggt.
                 */
                intent.putExtra(ERSTER_START, ERSTER_START);
                startActivity(intent);

            }else{
                Intent intent = new Intent(this, HauptActivity.class);
                startActivity(intent);
            }

            finish();

        }else{
            //gebe Fehler aus
        }
    }

    private void LoggeEin(){
        String username = txt_username.getText().toString().trim();
        String pw = txt_pw.getText().toString().trim();

        String pw_hash = MD5(pw);

        System.out.println(username + " " + pw);

        DBLogin login = new DBLogin(this, getHash(), username, pw_hash, progress);
        login.execute();


    }


    public void initGUI(){
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        txt_pw = findViewById(R.id.txt_pw);
        txt_username = findViewById(R.id.txt_username);

        progress = findViewById(R.id.progress);

    }


}
