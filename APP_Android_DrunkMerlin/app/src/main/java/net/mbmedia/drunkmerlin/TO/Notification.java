package net.mbmedia.drunkmerlin.TO;

import android.os.Build;

import androidx.annotation.RequiresApi;

import net.mbmedia.drunkmerlin.Challenges.ChallengeArt;

import java.util.Base64;

public class Notification {

    private int id;
    private int art;
    private String text;
    private String base64text;
    private String freundName;

    public Notification(){

    }

    public String getFreundName() {
        return freundName;
    }

    public void setFreundName(String freundName) {
        this.freundName = freundName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public String getText() {
        return text;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setText(String text) {
        this.text = text;
        this.base64text = Base64.getEncoder().encodeToString(text.getBytes());
    }

    public String getBase64text() {
        return base64text;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setBase64text(String base64text) {
        this.base64text = base64text;
        this.text = new String(Base64.getDecoder().decode(base64text.getBytes()));
    }
}
