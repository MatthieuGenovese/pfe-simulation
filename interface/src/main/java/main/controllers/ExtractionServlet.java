package main.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import main.controllers.eventbus.input.ExtractDataMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

public class ExtractionServlet extends HttpServlet {

    private static final String HTTP_LOCALHOST_8080_TRAFFIC_LIGHT = "http://localhost:9092/extract";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String entity = req.getParameter( "entity" );
        String identifiant = req.getParameter( "id" );

        int id = Integer.parseInt(identifiant);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();

        ExtractDataMessage message = new ExtractDataMessage();
        message.setEntity(entity);
        message.setIdSimulation(id);

        HttpEntity<ExtractDataMessage> request = new HttpEntity<>(message, headers);
        restTemplate.postForEntity(HTTP_LOCALHOST_8080_TRAFFIC_LIGHT, request, String.class);

        this.getServletContext().getRequestDispatcher( "/WEB-INF/extraction.jsp" ).forward( req, resp );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher( "/WEB-INF/extraction.jsp" ).forward( req, resp );
    }
}
