package main.controllers;


import main.controllers.eventbus.input.SimulationMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OneSimulationServlet extends HttpServlet {

    private static final String HTTP_LOCALHOST_8080_TRAFFIC_LIGHT = "http://localhost:9090/state";


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

        int nbrH = Integer.parseInt(nbrOfHousehold);
        int nbrI = Integer.parseInt(nbrOfInvestor);
        int nbrP = Integer.parseInt(nbrOfPromoter);
        int nbrS = Integer.parseInt(nbrOfStep);


        List<Integer> listT = new ArrayList<>();
        List<Integer> listE = new ArrayList<>();

        String fileH = req.getParameter( "fileHousehold" );
        String fileI = req.getParameter( "fileInvestor" );
        String fileP = req.getParameter( "filePromoter" );

        for(String s : arrayT){
            listT.add(Integer.parseInt(s));
        }

        for(String s : arrayE){
            listE.add(Integer.parseInt(s));
        }

        SimulationMessage simulationMessage = new SimulationMessage();

        simulationMessage.setFileHousehold(fileH);
        simulationMessage.setFileInvestor(fileI);
        simulationMessage.setFilePromoter(fileP);

        simulationMessage.setListOfEquipment(listE);
        simulationMessage.setListOfTransport(listT);

        simulationMessage.setNbrHousehold(nbrH);
        simulationMessage.setNbrInvestor(nbrI);
        simulationMessage.setNbrPromoter(nbrP);

        simulationMessage.setNum(nbrS);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<SimulationMessage> request = new HttpEntity<>(simulationMessage, headers);
        restTemplate.postForEntity(HTTP_LOCALHOST_8080_TRAFFIC_LIGHT, request, String.class);


        this.getServletContext().getRequestDispatcher( "/WEB-INF/oneSimu.jsp" ).forward( req, resp );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        this.getServletContext().getRequestDispatcher( "/WEB-INF/oneSimu.jsp" ).forward( req, resp );
    }
}
