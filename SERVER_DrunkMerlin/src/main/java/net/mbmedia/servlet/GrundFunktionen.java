package net.mbmedia.servlet;

import javax.servlet.http.HttpServlet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GrundFunktionen extends HttpServlet {

    public String getAllgFehlerMeldung(){
        return "[{\"id\":\"fehler\"}]";
    }

    public String getErfolg(){
        return "[{\"status\":\"erfolg\"}]";
    }

    public boolean checkToken(String token){
        return token.equals(getHash());
    }

    public static String getHash(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String datum =  formatter.format(date);

        String zu_hashen = datum + "DrunkMerlinGeheim";
        //System.out.println(zu_hashen);
        return MD5(zu_hashen);
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
