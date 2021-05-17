package net.mbmedia.servlet;

import net.mbmedia.db.entities.Drink;
import net.mbmedia.db.jpa.DrinkJPA;
import net.mbmedia.helper.DatumZeitHelper;
import net.mbmedia.helper.JsonHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class DrinkServlet extends GrundFunktionen{

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
            case "/Drink/add":
                fuegeHinzu(req, resp, printWriter);
                return;
            case "/Drink/del":
                loesche(req, resp, printWriter);
                return;
            case "/Drink/show":
                zeige(req, resp, printWriter);
                return;
            default:
                printWriter.println(getAllgFehlerMeldung());
                return;

        }
    }

    private void zeige(HttpServletRequest req, HttpServletResponse resp, PrintWriter printWriter) {
        System.out.println("Zeige");
        DrinkJPA jpa = new DrinkJPA();
        ArrayList<Drink> drinks = jpa.getAlleDrinks(req.getParameter(APIParameter.nutzerid.getWeb()));
        String json = JsonHelper.generateJsonFromDrinks(drinks);
        System.out.println(json);
        printWriter.println(JsonHelper.generateJsonFromDrinks(drinks));
    }

    private void loesche(HttpServletRequest req, HttpServletResponse resp, PrintWriter printWriter) {
        System.out.println("loesche");
        DrinkJPA jpa = new DrinkJPA();

        if(req.getParameter(APIParameter.drinkid.getWeb()).equals("all")){
            jpa.loescheAlle(req.getParameter(APIParameter.nutzerid.getWeb()));
        }else{
            jpa.loesche(
                    req.getParameter(APIParameter.nutzerid.getWeb()),
                    Integer.parseInt(req.getParameter(APIParameter.drinkid.getWeb()))
            );
        }

        printWriter.println(getErfolg());


    }

    private void fuegeHinzu(HttpServletRequest req, HttpServletResponse resp, PrintWriter printWriter) {
        System.out.println("Menge: " + req.getParameter(APIParameter.menge.getWeb()));
        DrinkJPA jpa = new DrinkJPA();

        Date zeitpunkt = DatumZeitHelper.getZeitFromBase64(req.getParameter(APIParameter.zeitpunkt.getWeb()));

        jpa.fuegeHinzu(
                req.getParameter(APIParameter.nutzerid.getWeb()),
                req.getParameter(APIParameter.name.getWeb()),
                req.getParameter(APIParameter.prozent.getWeb()),
                req.getParameter(APIParameter.menge.getWeb()),
                zeitpunkt,
                req.getParameter(APIParameter.latlng.getWeb())
        );
        printWriter.println(getErfolg());
    }
}
