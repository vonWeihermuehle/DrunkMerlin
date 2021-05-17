package net.mbmedia.drunkmerlin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.mbmedia.drunkmerlin.DB_online.User.DBRegister;
import net.mbmedia.drunkmerlin.R;




public class RegisterActivity extends ActivityFunktionen {

    private EditText txt_username;
    private EditText txt_pw;

    private TextView btn_register;
    private TextView btn_login;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        initGUI();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registriere();
            }
        });

    }

    private void registriere(){
        String username = txt_username.getText().toString().trim();
        String pw_hash = MD5(txt_pw.getText().toString().trim());

        DBRegister register = new DBRegister(this, username, pw_hash, getHash(),progressBar);
        register.execute();
    }


    private void initGUI(){
        txt_username = findViewById(R.id.txt_username);
        txt_pw = findViewById(R.id.txt_pw);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progress);
    }


}
