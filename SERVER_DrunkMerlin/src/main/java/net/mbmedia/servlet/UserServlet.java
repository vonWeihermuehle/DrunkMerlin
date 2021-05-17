package net.mbmedia.servlet;

import net.mbmedia.db.jpa.nutzerJPA;
import net.mbmedia.helper.JsonHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class UserServlet extends GrundFunktionen {

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
            case "/User/register":
                registriere(req, resp, printWriter);
                return;
            case "/User/login":
                login(req, resp, printWriter);
                return;
            case "/User/promille":
                updatePromille(req, resp, printWriter);
                return;
            default:
                printWriter.println(getAllgFehlerMeldung());
                return;

        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Test +++++++++++++++++++++++++++++++++++++++++++++++++++++");
        PrintWriter printWriter = resp.getWriter();
        printWriter.println("test");

    }

    private void registriere(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer){
        nutzerJPA nutzerJPA = new nutzerJPA();
        String newNutzerID = nutzerJPA.registriere(req.getParameter(APIParameter.username.getWeb()),
                req.getParameter(APIParameter.password.getWeb()));

        writer.println(JsonHelper.generateJsonFromFreunde(newNutzerID));
        return;
    }

    private void login(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer){
        nutzerJPA nutzerJPA = new nutzerJPA();
        String nutzerId = nutzerJPA.login(
                req.getParameter(APIParameter.username.getWeb()),
                req.getParameter(APIParameter.password.getWeb()));
        writer.println(JsonHelper.generateJsonFromFreunde(nutzerId));
        return;
    }

    private void updatePromille(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer){
        nutzerJPA nutzerJPA = new nutzerJPA();
        nutzerJPA.updateOrInsertPromille(
                req.getParameter(APIParameter.promille.getWeb()),
                req.getParameter(APIParameter.nutzerid.getWeb())
        );

        writer.println(getErfolg());
        return;
    }
}
