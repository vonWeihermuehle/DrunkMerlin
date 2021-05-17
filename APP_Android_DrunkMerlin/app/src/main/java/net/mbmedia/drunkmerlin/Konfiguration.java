package net.mbmedia.drunkmerlin;

public class Konfiguration {

    public static final String protocol = "https://";
    public static final String Hostname = "192.168.10.61";
    //public static final String Hostname = "192.168.56.1";
    public static final String Port = "8443";

    public static final String URL = protocol + Hostname + ":" + Port;

    public static final String FEHLER = "fehler";
    public static final String BREITS_VERGEBEN = "Benutzername oder Email bereits vergeben";

    public static final int SPLASH_DAUER = 900; //2000;

    public static final int AKTUALISIERUNG = 30000;//30000;

    public static final int TEILEN_AUFFORDERUNG_MIN = 20;

    public static final int NOTIFICATION_TIME = 10; //3600000 = 1 stunde

}
