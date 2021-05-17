package net.mbmedia.db;

import java.util.UUID;

public class GrundFunktionen extends Manager {

    public String getAllgFehlerMeldung(){
        return "[{\"id\":\"Fehler\"}]";
    }

    public String getMisserfolg(){
        return "[{\"status\":\"misserfolg\"}]";
    }

    public String getErfolg(){
        return "[{\"status\":\"erfolg\"}]";
    }

    public String getBereitsVorhanden(){
        return "[{\"id\":\"Benutzername oder Email bereits vergeben\"}]";
    }

    public static String createUUID(){
        UUID uuid = UUID.randomUUID();
        return String.valueOf(uuid);
    }


}
