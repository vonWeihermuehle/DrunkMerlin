package net.mbmedia.servlet;

import net.mbmedia.db.entities.FreundeMitKarte;
import net.mbmedia.db.entities.FreundeMitPromille;
import net.mbmedia.db.jpa.FreundJPA;
import net.mbmedia.helper.JsonHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FreundeServlet extends GrundFunktionen{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();


        if(!checkToken(req.getParameter(APIParameter.hash.getWeb()))){
            printWriter.println(getAllgFehlerMeldung());
            return;
        }

        switch (req.getRequestURI()){
            case "/Freund/add":
                fuegeFreundhinzu(req, resp, printWriter);
                return;
            case "/Freund/del":
                loescheFreund(req, resp, printWriter);
                return;
            case "/Freund/show":
                zeigeFreunde(req, resp, printWriter);
                return;
            case "/Freund/karte":
                zeigeFreundeKarte(req, resp, printWriter);
                return;
            default:
                printWriter.println(getAllgFehlerMeldung());
                return;

        }
    }

    private void fuegeFreundhinzu(HttpServletRequest req, HttpServletResponse resp, PrintWriter printWriter) {
        FreundJPA jpa = new FreundJPA();
        jpa.insertFreunde(
                req.getParameter(APIParameter.nutzerid.getWeb()),
                req.getParameter(APIParameter.freundid.getWeb())
        );
        printWriter.println(getErfolg());
    }

    private void loescheFreund(HttpServletRequest req, HttpServletResponse resp, PrintWriter printWriter) {
        FreundJPA jpa = new FreundJPA();
        jpa.deleteFreunde(
                req.getParameter(APIParameter.nutzerid.getWeb()),
                req.getParameter(APIParameter.freundid.getWeb())
        );
        printWriter.println(getErfolg());
    }

    private void zeigeFreunde(HttpServletRequest req, HttpServletResponse resp, PrintWriter printWriter) {

        FreundJPA jpa = new FreundJPA();
        ArrayList<FreundeMitPromille> freunde =  jpa.holeFreunde(
                req.getParameter(APIParameter.nutzerid.getWeb())
        );
        printWriter.println(JsonHelper.generateJsonFromFreunde(freunde));
    }

    private void zeigeFreundeKarte(HttpServletRequest req, HttpServletResponse resp, PrintWriter printWriter) {
        FreundJPA jpa = new FreundJPA();
        ArrayList<FreundeMitKarte> freunde = jpa.zeigeFreundeMitKarte(
                req.getParameter(APIParameter.nutzerid.getWeb())
        );
        printWriter.println(JsonHelper.generateJsonFromFreundeKarte(freunde));
    }


}
