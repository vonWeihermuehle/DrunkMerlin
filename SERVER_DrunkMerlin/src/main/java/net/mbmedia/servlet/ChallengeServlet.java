package net.mbmedia.servlet;

import net.mbmedia.db.entities.Challenge;
import net.mbmedia.db.entities.ChallengeMitUsername;
import net.mbmedia.db.jpa.ChallengeJPA;
import net.mbmedia.helper.JsonHelper;
import sun.security.krb5.internal.APOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ChallengeServlet extends GrundFunktionen {

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
            case "/Challenge/add":
                fuegeHinzu(req, resp, printWriter);
                return;
            case "/Challenge/del":
                loesche(req, resp, printWriter);
                return;
            case "/Challenge/show":
                zeige(req, resp, printWriter);
                return;
            default:
                printWriter.println(getAllgFehlerMeldung());
                return;

        }
    }

    private void fuegeHinzu(HttpServletRequest req, HttpServletResponse resp, PrintWriter printWriter) {
        ChallengeJPA jpa = new ChallengeJPA();
        jpa.fuegeHinzu(
                req.getParameter(APIParameter.nutzerid.getWeb()),
                req.getParameter(APIParameter.freundid.getWeb()),
                req.getParameter(APIParameter.text.getWeb())
        );
        printWriter.println(getErfolg());
    }

    private void loesche(HttpServletRequest req, HttpServletResponse resp, PrintWriter printWriter) {
        ChallengeJPA jpa = new ChallengeJPA();
        jpa.loesche(
                Integer.parseInt(req.getParameter(APIParameter.id.getWeb()))
        );
        printWriter.println(getErfolg());
    }

    private void zeige(HttpServletRequest req, HttpServletResponse resp, PrintWriter printWriter) {
        ChallengeJPA jpa = new ChallengeJPA();
        ArrayList<ChallengeMitUsername> challenges = jpa.zeige(
                req.getParameter(APIParameter.nutzerid.getWeb())
        );

        printWriter.println(JsonHelper.generateJsonFromChallenges(challenges));
    }
}
