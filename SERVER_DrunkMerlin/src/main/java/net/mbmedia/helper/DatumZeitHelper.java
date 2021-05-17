package net.mbmedia.helper;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class DatumZeitHelper {

    public Date getAktuelleZeitDatum(){
        Date date = new Date();
        return date;
    }

    public static Date getZeitFromBase64(String base){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        String decoded = new String(Base64.getDecoder().decode(base));
        System.out.println("decoded: " + decoded);
        Date date = new Date();
        try {
            date = formatter.parse(decoded);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
