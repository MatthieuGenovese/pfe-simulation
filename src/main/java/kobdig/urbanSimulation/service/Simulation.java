package kobdig.urbanSimulation.service;

import kobdig.agent.Agent;
import kobdig.agent.Fact;
import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.entities.agents.AbstractAgent;
import kobdig.urbanSimulation.entities.agents.Household;
import kobdig.urbanSimulation.entities.agents.Investor;
import kobdig.urbanSimulation.entities.agents.Promoter;
import kobdig.urbanSimulation.entities.environement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Meili on 20/06/16.
 */
@Service
public class Simulation {

    private int id;

    public static final double INCOME_GAP = 0.3;
    public static final String NETWORK = "network";

    public static final String EQUIPMENT = "equipment";
    protected static Agent investorAgent;

    @Autowired
    EntitiesCreator builder;

    /** Execution delay in milliseconds */
    private volatile int executionDelay;

    /** Animation thread. */
    private Thread thread;

    /** A flag for controlling the animation thread */
    private volatile boolean running = false;


    @Autowired
    public Simulation(){
        this.executionDelay = 10;
        this. id = 0;
    }

    /**
     * Writes the resultant data indicators in the database
     * @throws SQLException
     */
    public void writeIndicators(EntitiesCreator entitiesCreator, int time) throws SQLException{
        double countRent = 0.0;
        double countSale = 0.0;
        for (AdministrativeDivision division : entitiesCreator.getDivisions()) {
            if (division != null) {
                double rentInDivision = 0.0;
                double saleInDivision = 0.0;
                for (Property property : division.getProperties()) {
                    if (property.getState().equals(Property.OCCUPIED)) {
                        countSale++;
                        saleInDivision++;
                    } else if (property.getState().equals(Property.RENTED)) {
                        countRent++;
                        saleInDivision++;
                    }
                }
                division.setOnSaleProperties(saleInDivision);
                division.setRentedProperties(rentInDivision);
            }
        }
        for (AdministrativeDivision division : entitiesCreator.getDivisions()) {
            if (division != null) {
                double sale = (countSale > 0.0) ? division.getOnSaleProperties() / countSale : 0;
                double rent = (countRent > 0.0) ? division.getRentedProperties() / countRent : 0;
                double si = Math.abs(sale - rent) / 2.0;
                Statement s = entitiesCreator.getConn().createStatement();
                String query = "INSERT INTO indicator2 (\"step\",\"si\",\"idUPZ\") VALUES ('" + time + "','" + si + "','" +
                        division.getId() + "')";
                s.executeUpdate(query);
                s.close();
            }
        }
        double rop = ((countRent + countSale) > 0.0)? countRent/(countRent + countSale): 0.0;
        Statement s = entitiesCreator.getConn().createStatement();
        String query = "INSERT INTO indicator1 (\"step\",\"ROP\") VALUES ('" + time + "','" + rop + "')";
        s.executeUpdate(query);
        s.close();

    }

    /**
     * Writes the resultant data in the database
     * @throws SQLException
     */
    public void writeResults(EntitiesCreator entitiesCreator, int time) throws SQLException{
        for (AdministrativeDivision division : entitiesCreator.getDivisions()) {
            if (division != null) {
                for (Property property : division.getProperties()) {
                    Statement s = entitiesCreator.getConn().createStatement();
                    String query = "INSERT INTO properties_state (\"idSimularion\", \"step\",\"idProperty\",\"price\",\"rent\",\"value\",\"state\"" +
                            ",\"geom\",\"codigo_upz\") VALUES ('" + this.id + "','" + time + "','" + property.getId() + "','" + property.getCurrentPrice() + "','" +
                            property.getCurrentCapitalizedRent() + "','" + property.getCurrentValue() + "','" + property.getState() +
                            "','" + property.getGeom() + "','" + division.getCode() + "')";
                    s.executeUpdate(query);
                    s.close();
                }
            }
        }
    }

    public void setId(int id){
        this.id = id;
    }

    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("Animation is already running.");
        }

        // the reason we do not inherit from Runnable is that we do not want to
        // expose the void run() method to the outside world. We want to well
        // encapsulate the whole idea of a thread.
        // thread cannot be restarted so we need to always create a new one
        thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    synchronized (this) {
                        while (!running) {
                            try {
                                wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    try {
                        synchronized (this) {
                            Thread.sleep(executionDelay);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    show();

                }
            }
        };

        // start the thread
        thread.start();
        // set the flag
        running = true;
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void stop() {
        if (!running) {
            throw new IllegalStateException("Animation is stopped.");
        }
        running = false;
    }

    public void show(){
        System.out.println("Testing the kobdig.urbanSimulation Simulator...");

        try {
            writeIndicators(builder, 0);
            writeResults(builder, 0);

            Statement s = builder.getConn().createStatement();
            ResultSet r = s.executeQuery("SELECT MAX(gid) FROM properties;");
            if(r.next())  builder.getIdManager()[0] = r.getInt(1) + 1;
            s.close();
            r.close();
            for (int time = 1; time <= builder.getNumSim(); time++) {

               // System.out.println("FOR RENT PROPERTIES :" + builder.getForRentProperties().size());

                int occuped = 0;
                int rented = 0;
                int forsale = 0;
                int landsize = 0;
                int forrent = 0;
                for (int i = 0; i < builder.getDivisions().length; i++) {
                    if (builder.getDivisions()[i] != null) {
                        ArrayList<Land> landDiv = builder.getDivisions()[i].getLands();
                        for (Land land : landDiv) land.step(time - 1);
                        for (Property property : builder.getDivisions()[i].getProperties()) property.step(time - 1);
                        occuped += builder.getDivisions()[i].getPropertiesOccuped();
                        rented += builder.getDivisions()[i].getPropertiesRented();
                        forsale += builder.getDivisions()[i].getPropertiesForSale();
                        forrent += builder.getDivisions()[i].getPropertiesForRent();
                        landsize+= builder.getDivisions()[i].getLands().size();
                    }
                }


                for(AbstractAgent agent : builder.getAgents()){
                    agent.agentUpdateBeliefs(builder, time);
                    agent.agentIntentionsStep(builder);
                }

                for (Investor investor : builder.getInvestors()) {
                    investor.agentUpdateBeliefs(builder, time-1);
                    investor.agentIntentionsStep(builder);
                }


                writeIndicators(builder, time-1);
                writeResults(builder, time-1);

                System.out.println("occuped : " + occuped +" for rent " + forrent + " rented : " + rented + " for sale : " + forsale);
                System.out.println("land size : " +landsize);

            }

            builder.getConn().close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}
