package main.controllers;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterfaceController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nbrOfHousehold = req.getParameter( "household" );
        String nbrOfInvestor = req.getParameter( "investor" );
        String nbrOfPromoter = req.getParameter( "promoter" );
        String nbrOfStep = req.getParameter( "etape" );;
        String listOfTransport = req.getParameter( "listT" );
        String listOfEquipment = req.getParameter( "listE" );

        String[] arrayT = listOfTransport.split(",");
        String[] arrayE = listOfEquipment.split(",");

        List<Integer> listT = new ArrayList<>();
        List<Integer> listE = new ArrayList<>();

        for(String s : arrayT){
            listT.add(Integer.parseInt(s));
        }

        for(String s : arrayE){
            listE.add(Integer.parseInt(s));
        }


        this.getServletContext().getRequestDispatcher( "/WEB-INF/users.jsp" ).forward( req, resp );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        this.getServletContext().getRequestDispatcher( "/WEB-INF/users.jsp" ).forward( req, resp );
    }
}
