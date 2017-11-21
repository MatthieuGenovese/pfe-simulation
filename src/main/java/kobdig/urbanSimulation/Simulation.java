package kobdig.urbanSimulation;

import kobdig.agent.Agent;
import kobdig.agent.Fact;
import kobdig.urbanSimulation.entities.agents.AbstractAgent;
import kobdig.urbanSimulation.entities.agents.Household;
import kobdig.urbanSimulation.entities.agents.Investor;
import kobdig.urbanSimulation.entities.agents.Promoter;
import kobdig.urbanSimulation.entities.environement.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Meili on 20/06/16.
 */
public class Simulation {

    public static final double INCOME_GAP = 0.3;
    public static final String NETWORK = "network";

    public static final String EQUIPMENT = "equipment";
    protected static Agent investorAgent;




    /*

    public static void updateEquipmentsOrNetworkToConsider(String type, String newConsiderations) {
        if(type.equals(EQUIPMENT)) {
            filteredEquipments += "," + newConsiderations;
        }

        if(type.equals(NETWORK)) {
            filteredNetwork += "," + newConsiderations;
        }

        for (Property property : freeProperties) {
            property.setUpdated(false);
        }
        for (Property property : forRentProperties) {
            property.setUpdated(false);
        }
        for (Land land : forSaleLand) {
            land.setUpdated(false);
        }
        System.out.println("::: Updated " + type);
    }*/


    /**
     * Writes the resultant data indicators in the database
     * @throws SQLException
     */
    public static void writeIndicators(EntitiesCreator entitiesCreator, int time) throws SQLException{
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
    public static void writeResults(EntitiesCreator entitiesCreator, int time) throws SQLException{
        for (AdministrativeDivision division : entitiesCreator.getDivisions()) {
            if (division != null) {
                for (Property property : division.getProperties()) {
                    Statement s = entitiesCreator.getConn().createStatement();
                    String query = "INSERT INTO properties_state (\"step\",\"idProperty\",\"price\",\"rent\",\"value\",\"state\"" +
                            ",\"geom\",\"codigo_upz\") VALUES ('" + time + "','" + property.getId() + "','" + property.getCurrentPrice() + "','" +
                            property.getCurrentCapitalizedRent() + "','" + property.getCurrentValue() + "','" + property.getState() +
                            "','" + property.getGeom() + "','" + division.getCode() + "')";
                    s.executeUpdate(query);
                    s.close();
                }
            }
        }
    }

    public void start(){
        System.out.println("Testing the kobdig.urbanSimulation Simulator...");
        EntitiesCreator builder = new EntitiesCreator();
        builder.createAll();

        try {
            writeIndicators(builder, 0);
            writeResults(builder, 0);

            Statement s = builder.getConn().createStatement();
            ResultSet r = s.executeQuery("SELECT MAX(gid) FROM properties;");
            if(r.next())  builder.getIdManager()[0] = r.getInt(1) + 1;
            s.close();
            r.close();

            for (int time = 1; time <= builder.getNumSim(); time++) {

                System.out.println("Step " + time);


                for (AdministrativeDivision division : builder.getDivisions()) {
                    if (division != null) {
                        ArrayList<Land> landDiv = division.getLands();
                        for (Land land : landDiv) land.step(time - 1);
                        for (Property property : division.getProperties()) property.step(time - 1);
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

                System.err.println(time-1 + ". - free " + builder.getFreeProperties().size() + " for rent " + builder.getForRentProperties().size() + " total " +
                        (builder.getFreeProperties().size() + builder.getForRentProperties().size()) );

                writeIndicators(builder, time-1);
                writeResults(builder, time-1);

            }

            builder.getConn().close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}
