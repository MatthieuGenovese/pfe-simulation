package kobdig.urbanSimulation;

import kobdig.agent.Agent;
import kobdig.agent.Fact;
import kobdig.urbanSimulation.entities.agents.Household;
import kobdig.urbanSimulation.entities.agents.Investor;
import kobdig.urbanSimulation.entities.agents.Promoter;
import kobdig.urbanSimulation.entities.environement.*;
import org.postgis.PGgeometry;
import java.io.*;
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
    protected static ArrayList<Household> households;
    protected static ArrayList<Investor> investors;
    protected static ArrayList<Promoter> promoters;
    protected static ArrayList<Property> freeProperties;
    protected static ArrayList<Property> forRentProperties;
    protected static ArrayList<Land> forSaleLand;
    protected static AdministrativeDivision[] divisions;
    protected static int equipmentsLength;
    protected static int networkLength;
    protected static Agent investorAgent;
    protected static int time;
    protected static int numSim;
    protected static int[] idManager;
    protected static Connection conn;
    protected static String filteredEquipments;
    protected static String filteredNetwork;



    /**
     * Generates a household intention step in the simulation
     * @param household The household
     */
    public static void householdIntentionStep(Connection conn, Household household) {

        System.out.println("____________________Interntion Step____________________");
        Iterator<Fact> iter = household.getAgent().goals().factIterator();
        System.out.println("Simulation step: " + time + " Household no." + household.getId());
        while (iter.hasNext()){
            System.out.println(iter.next().formula().toString());
        }
        System.out.println("________________________________________");

         iter = household.getAgent().goals().factIterator();
        while(iter.hasNext()) {
            String goal = iter.next().formula().toString();
            // If the goal is to buy
            if (goal.contains(Household.BUY) && goal.contains(Household.OWNER)){
                Property taken = buyProperty(conn, household);
                if (taken != null) {
                    freeProperties.remove(taken);
                    taken.setState(Property.OCCUPIED);
                    household.setOwnerOccupied(true);
                }
            }

            // If the goal is to invest
            else if (goal.contains(Household.BUY) && !goal.contains(Household.NOT_LANDLORD) && goal.contains(Household.LANDLORD)){
                Property taken = invest(conn, household);
                if (taken != null) {
                    taken.setState(Property.SEEKING_TENANT);
                    freeProperties.remove(taken);
                    forRentProperties.add(taken);
                    taken.setUpdated(false);
                    Investor newInvestor = new Investor(investorAgent, household,taken);
                    investors.add(newInvestor);
                    household.setProperty(null);
                }
            }

            // If the goal is to rent
            if (goal.contains(Household.RENT)){
                Property taken = rentProperty(conn, household);
                if (taken != null) {
                    forRentProperties.remove(taken);
                    taken.setState(Property.RENTED);
                    household.setProperty(null);
                    household.setRenting(true);
                }
            }

            // If the goal is to change and either sell or invest
            if (goal.contains(Household.CHANGE)){

                Property taken = buyProperty(conn, household);
                if (taken != null) {
                    freeProperties.remove(taken);
                    taken.setState(Property.OCCUPIED);
                }

                if (goal.contains(Household.LANDLORD)){
                    if(household.getProperty() != null) {
                        household.invest(household.getProperty());
                        freeProperties.remove(household.getProperty());
                        forRentProperties.add(household.getProperty());
                        Investor newInvestor = new Investor(investorAgent, household, household.getProperty());
                        newInvestor.getProperty().setState(Property.SEEKING_TENANT);
                        investors.add(newInvestor);
                        household.setProperty(null);
                    }
                }
                else if (goal.contains(Household.SELL)){
                    //TODO: Implement the seller part of the property
                    if (household.getProperty() != null) {
                        freeProperties.add(household.getProperty());
                        household.getProperty().setState(Property.FOR_SALE);
                        household.setProperty(null);
                    }
                }

            }

        }
    }

    /**
     * Generates a investor step in the simulation for an specific investor
     * @param investor The investor
     * @param time The time in the simulation
     */
    public static void investorUpdateBeliefs(Investor investor, int time) {

        investor.step(time);
        Property cheapestProperty = null;
        double cheapestPrice = Double.POSITIVE_INFINITY;

        // Updates de affordBuying and affordRenting beliefs and gets the cheapest property
        int purchFound = 0;

        for (Property property : freeProperties) {
            if (property.getCurrentPrice() < cheapestPrice) {
                cheapestPrice = property.getCurrentPrice();
                cheapestProperty = property;
            }

            if (investor.getCurrentPurchasingPower() >= property.getCurrentPrice()) {
                investor.addPurchasableProperty(property);
                purchFound++;
            }
        }

        if(purchFound > 0) investor.updateBelief("ab:" + Double.toString(purchFound/(0.0 + freeProperties.size())));

        else investor.updateBelief("not ab:1");

        // Updates the buyingRentable belief
        // TODO: Improve this approach
        if(cheapestPrice < Double.POSITIVE_INFINITY){
            if(investor.getCurrentPurchasingPower() > cheapestPrice){
                investor.updateBelief("br:" + cheapestProperty.getCurrentCapitalizedRent()/(0.0 +
                        cheapestProperty.getCurrentPotentialRent()));
            }
        }


        // Updates the sellingRentable belief
        // TODO: Improve this approach
        if (investor.getProperty() != null) {
            if (cheapestPrice < Double.POSITIVE_INFINITY) {
                if (investor.getCurrentPurchasingPower() > cheapestPrice) {
                    double sellingRentability = investor.getProperty().getCurrentCapitalizedRent() /
                            (0.0 + investor.getProperty().getCurrentCapitalizedRent() +
                                    cheapestProperty.getCurrentCapitalizedRent());

                    investor.updateBelief("sr:" + sellingRentability);
                }
            }
        }
    }

    /**
     * Generates a investor intention step in the simulation
     * @param investor The investor
     */
    public static void investorIntentionStep(Connection conn, Investor investor) {

        Iterator<Fact> iter = investor.getAgent().goals().factIterator();
        while(iter.hasNext()) {
            String goal = iter.next().formula().toString();

            // If the goal is to invest
            if (goal.contains(Investor.BUY) && goal.contains(Investor.LANDLORD)){
                if (investor.getProperty() != null) {
                    Property taken = invest(conn, investor);
                    if (taken != null) {
                        freeProperties.remove(taken);
                        forRentProperties.add(taken);
                        taken.setState(Property.SEEKING_TENANT);
                        //Investor newInvestor = new Investor(investorAgent, investor, taken);
                        investor.setAgent(investorAgent);
                        investor.setProperty(taken);
                        investor.setOwner(true);
                        investors.add(investor);
                    }
                }
                else{
                    Property taken = invest(conn, investor);
                    if (taken != null) {
                        freeProperties.remove(taken);
                        forRentProperties.add(taken);
                        taken.setState(Property.SEEKING_TENANT);
                    }
                }
            }

            // If the goal is to sell
            // TODO: Improve this approach
//            if (!goal.contains(Investor.BUY) && !goal.contains(Investor.LANDLORD) && !goal.contains(Investor.NOT_SELL)
//                    && goal.contains(Investor.SELL)){
//                if (investor.getProperty() != null) {
//                    freeProperties.add(investor.getProperty());
//                    investor.getProperty().setState(Property.FOR_SALE);
//                }
//            }

        }
    }

    /**
     * Generates a promoter step in the simulation for an specific promoter
     * @param promoter The promoter
     * @param time The time in the simulation
     */
    public static void promoterUpdateBeliefs(Promoter promoter, int time) {

        promoter.step(time);
        int purchFound = 0;

        // Updates affordBuyingLand
        for (Land land : forSaleLand) {
            if(promoter.getPurchasingPower() >= land.getPrice()){
                promoter.addPurchasableLand(land);
                purchFound++;
            }
        }
        
        if(purchFound > 0) promoter.updateBelief("abl:" + Double.toString(purchFound/(0.0 + forSaleLand.size())));

        else promoter.updateBelief("not abl:1");

        // Updates ac
        // TODO: Improve this approach

        double rnd = Math.random();
        if (rnd < 0.5) promoter.updateBelief("ac:" + 1);
        else promoter.updateBelief("not ac:" + 1);
    }

    /**
     * Generates a promoter intention step in the simulation
     * @param promoter The promoter
     */
    public static void promoterIntentionStep(Connection conn, Promoter promoter) {
        Iterator<Fact> iter = promoter.getAgent().goals().factIterator();
        while(iter.hasNext()) {
            String goal = iter.next().formula().toString();
            //if (goal.contains(Promoter.BUY_LAND) && goal.contains(Promoter.SELL_OFF_PLANS)){
            if (goal.contains(Promoter.BUY_LAND)){    
            	Land taken = buyLand(conn, promoter);
                if (taken != null){
                    forSaleLand.remove(taken);
                    Property construction = null;
                    try {
                        int id = idManager[0]++;
                        construction = new Property(Integer.toString(id),taken.getLatitude(),
                                taken.getLongitude(),(taken.getPrice() + 150), taken.getPrice()/10, taken.getPrice(),
                                taken.getGeom(), taken);
                        construction.setDivision(taken.getDivision());
                        taken.getDivision().addProperty(construction);
                        construction.setState(Property.FOR_SALE);
                        freeProperties.add(construction);
                    }
                    catch (Exception e) {}
                }
            }
        }
    }


    /**
     * Purchases a land
     * @return The land purchased
     */
    public static Property rentProperty(Connection conn, Household investor){
        double maxUtility = 0.0;
        Property selection = null;
        for (Property purchasable : investor.getRentableProperties()) {
            try {
                if (purchasable.getDivision() != null && !purchasable.isUpdated()) {
                    double equipUtility = 0.0;
                    double transportUtility = 0.0;
                    Statement s1 = conn.createStatement();
                    String query_equipments = "SELECT COUNT(a.*) FROM " + filteredEquipments + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " + purchasable.getLand().getId();
                    ResultSet r1 = s1.executeQuery(query_equipments);
                    if(r1.next()) {
                        equipUtility = r1.getInt(1);
                    }
                    s1.close();
                    r1.close();

                    Statement s2 = conn.createStatement();
                    String query_transport = "SELECT COUNT(a.*) FROM " + filteredNetwork + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " + purchasable.getLand().getId();
                    ResultSet r2 = s2.executeQuery(query_transport);
                    if(r2.next()) {
                        transportUtility = r2.getInt(1);
                    }
                    s2.close();
                    r2.close();

                    purchasable.setUtility(0.4*(equipUtility/(double)equipmentsLength) + 0.6*(transportUtility/(double)networkLength));
//                    purchasable.setUtility(0.0*(equipUtility/(double)equipmentsLength) + 1.0*(transportUtility/(double)networkLength));
//                    purchasable.setUtility(Math.random());
                    purchasable.setUpdated(true);
                }
                if(purchasable.getUtility() > maxUtility){
                    maxUtility = purchasable.getUtility();
                    selection = purchasable;
                }
            }
            catch (SQLException e){                 e.printStackTrace();             }

        }
        investor.setRenting(selection != null);
        if(investor.isRenting()){
            investor.setPreviousNetMonthlyIncome(investor.getCurrentNetMonthlyIncome());
            investor.setCurrentNetMonthlyIncome(investor.getPreviousNetMonthlyIncome() - selection.getCurrentCapitalizedRent());
        }
        return selection;
    }

    /**
     * Purchases a land
     * @return The land purchased
     */
    public static Property buyProperty(Connection conn, Household investor){
        double maxUtility = 0.0;
        Property selection = null;
        for (Property purchasable : investor.getPurchasableProperties()) {
            try {
                double equipUtility = 0.0;
                double transportUtility = 0.0;
                if (purchasable.getDivision() != null && !purchasable.isUpdated()) {
                    Statement s1 = conn.createStatement();
                    String query_equipments = "SELECT COUNT(a.*) FROM " 
                    + filteredEquipments + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " 
                    		+ purchasable.getLand().getId();
                    ResultSet r1 = s1.executeQuery(query_equipments);
                    if(r1.next()) {
                        equipUtility = r1.getInt(1);
                    }
                    s1.close();
                    r1.close();

                    Statement s2 = conn.createStatement();
                    String query_transport = "SELECT COUNT(a.*) FROM " + filteredNetwork + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " + purchasable.getLand().getId();
                    ResultSet r2 = s2.executeQuery(query_transport);
                    if(r2.next()) {
                        transportUtility = r2.getInt(1);
                    }
                    s2.close();
                    r2.close();

                    purchasable.setUtility(0.4*(equipUtility/(double)equipmentsLength) + 0.6*(transportUtility/(double)networkLength));
//                    purchasable.setUtility(0.0*(equipUtility/(double)equipmentsLength) + 1.0*(transportUtility/(double)networkLength));
//                        purchasable.setUtility(Math.random());
                    purchasable.setUpdated(true);
                }
                if(purchasable.getUtility() > maxUtility){
                    maxUtility = purchasable.getUtility();
                    selection = purchasable;
                }
            }
            catch (SQLException e){ e.printStackTrace();             }
        }
        investor.setOwnerOccupied(selection != null);
        if(investor.isOwnerOccupied()){
            investor.setProperty(selection);
            investor.setPreviousPurchasingPower(investor.getCurrentPurchasingPower());
            investor.setCurrentPurchasingPower(investor.getPreviousPurchasingPower() - investor.getProperty().getCurrentPrice());
            investor.getProperty().setState(Property.OCCUPIED);
        }
        return selection;
    }

    /**
     * Purchases a land
     * @return The land purchased
     */
    public static Property invest(Connection conn, Household investor){
        double maxUtility = 0.0;
        Property selection = null;
        for (Property purchasable : investor.getPurchasableProperties()) {
            try {
                if (purchasable.getDivision() != null && !purchasable.isUpdated()) {
                    double equipUtility = 0.0;
                    double transportUtility = 0.0;
                    Statement s1 = conn.createStatement();
                    String query_equipments = "SELECT COUNT(a.*) FROM " + filteredEquipments + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " + purchasable.getLand().getId();
                    ResultSet r1 = s1.executeQuery(query_equipments);
                    if(r1.next()) {
                        equipUtility = r1.getInt(1);
                    }
                    s1.close();
                    r1.close();

                    Statement s2 = conn.createStatement();
                    String query_transport = "SELECT COUNT(a.*) FROM " + filteredNetwork + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " + purchasable.getLand().getId();
                    ResultSet r2 = s2.executeQuery(query_transport);
                    if(r2.next()) {
                        transportUtility = r2.getInt(1);
                    }
                    s2.close();
                    r2.close();

                    purchasable.setUtility(0.4*(equipUtility/(double)equipmentsLength) + 0.6*(transportUtility/(double)networkLength));
//                    purchasable.setUtility(0.0*(equipUtility/(double)equipmentsLength) + 1.0*(transportUtility/(double)networkLength));
//                    purchasable.setUtility(Math.random());
                    purchasable.setUpdated(true);
                }
                if(purchasable.getUtility() > maxUtility){
                    maxUtility = purchasable.getUtility();
                    selection = purchasable;
                }
            }
            catch (SQLException e){                 e.printStackTrace();             }
        }
        if (selection != null){
            investor.setPreviousPurchasingPower(investor.getCurrentPurchasingPower());
            investor.setCurrentPurchasingPower(investor.getPreviousPurchasingPower() - selection.getCurrentPrice());
        }
        return selection;
    }

    /**
     * Purchases a land
     * @return The land purchased
     */
    public static Property invest(Connection conn, Investor investor){
        double maxUtility = 0.0;
        Property selection = null;
        for (Property purchasable : investor.getPurchasableProperties()) {
            try {
                if (purchasable.getDivision() != null && !purchasable.isUpdated()) {
                    double equipUtility = 0.0;
                    double transportUtility = 0.0;
                    Statement s1 = conn.createStatement();
                    String query_equipments = "SELECT COUNT(a.*) FROM " + filteredEquipments + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " + purchasable.getLand().getId();
                    ResultSet r1 = s1.executeQuery(query_equipments);
                    if(r1.next()) {
                        equipUtility = r1.getInt(1);
                    }
                    s1.close();
                    r1.close();

                    Statement s2 = conn.createStatement();
                    String query_transport = "SELECT COUNT(a.*) FROM " + filteredNetwork + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " + purchasable.getLand().getId();
                    ResultSet r2 = s2.executeQuery(query_transport);
                    if(r2.next()) {
                        transportUtility = r2.getInt(1);
                    }
                    s2.close();
                    r2.close();
                    purchasable.setUtility(0.4*(equipUtility/(double)equipmentsLength) + 0.6*(transportUtility/(double)networkLength));
//                    purchasable.setUtility(0.0*(equipUtility/(double)equipmentsLength) + 1.0*(transportUtility/(double)networkLength));
//                    purchasable.setUtility(Math.random());
                    purchasable.setUpdated(true);
                }
                if(purchasable.getUtility() > maxUtility){
                    maxUtility = purchasable.getUtility();
                    selection = purchasable;
                }
            }
            catch (SQLException e){                 e.printStackTrace();             }
        }
        if (selection != null){
            investor.setPreviousPurchasingPower(investor.getCurrentPurchasingPower());
            investor.setCurrentPurchasingPower(investor.getPreviousPurchasingPower() - selection.getCurrentPrice());
        }
        return selection;
    }

    /**
     * Purchases a land
     * @return The land purchased
     */
    public static Land buyLand(Connection conn, Promoter promoter){
        double maxUtility = 0.0;
        Land selection = null;
        System.out.println(promoter.getPurchasableLand().size());
        for (Land purchasable : promoter.getPurchasableLand()) {
        	System.out.println(purchasable.getId());
            try {
                if (purchasable.getDivision() != null && !purchasable.isUpdated()) {
                    double equipUtility = 0.0;
                    double transportUtility = 0.0;

                    Statement s1 = conn.createStatement();
                    String query_equipments = "SELECT COUNT(a.*) FROM " + filteredEquipments + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " + purchasable.getId();
                    ResultSet r1 = s1.executeQuery(query_equipments);
                    if(r1.next()) {
                        equipUtility = r1.getInt(1);
                    }
                    s1.close();
                    r1.close();


                    Statement s2 = conn.createStatement();
                    String query_transport = "SELECT COUNT(a.*) FROM " + filteredNetwork + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " + purchasable.getId();
                    ResultSet r2 = s2.executeQuery(query_transport);
                    if(r2.next()) {
                        transportUtility = r2.getInt(1);
                    }
                    s2.close();
                    r2.close();
                    purchasable.setUtility(0.4*(equipUtility/(double)equipmentsLength) + 0.6*(transportUtility/(double)networkLength));
//                    purchasable.setUtility(0.0*(equipUtility/(double)equipmentsLength) + 1.0*(transportUtility/(double)networkLength));
//                    purchasable.setUtility(Math.random());
                    purchasable.setUpdated(true);
                }
                if(purchasable.getUtility() > maxUtility){
                    maxUtility = purchasable.getUtility();
                    selection = purchasable;
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        if (selection != null){
            promoter.setPurchasingPower(promoter.getPurchasingPower() - selection.getPrice());
        }
        return selection;
    }

    /**
     * Updates utility
     */
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
    }


    /**
     * Writes the resultant data indicators in the database
     * @param conn The connection to the database
     * @throws SQLException
     */
    public static void writeIndicators(Connection conn, int time) throws SQLException{
        double countRent = 0.0;
        double countSale = 0.0;
        for (AdministrativeDivision division : divisions) {
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
        for (AdministrativeDivision division : divisions) {
            if (division != null) {
                double sale = (countSale > 0.0) ? division.getOnSaleProperties() / countSale : 0;
                double rent = (countRent > 0.0) ? division.getRentedProperties() / countRent : 0;
                double si = Math.abs(sale - rent) / 2.0;
                Statement s = conn.createStatement();
                String query = "INSERT INTO indicator2 (\"step\",\"si\",\"idUPZ\") VALUES ('" + time + "','" + si + "','" +
                        division.getId() + "')";
                s.executeUpdate(query);
                s.close();
            }
        }
        double rop = ((countRent + countSale) > 0.0)? countRent/(countRent + countSale): 0.0;
        Statement s = conn.createStatement();
        String query = "INSERT INTO indicator1 (\"step\",\"ROP\") VALUES ('" + time + "','" + rop + "')";
        s.executeUpdate(query);
        s.close();

    }

    /**
     * Writes the resultant data in the database
     * @param conn The connection to the database
     * @throws SQLException
     */
    public static void writeResults(Connection conn, int time) throws SQLException{
        for (AdministrativeDivision division : divisions) {
            if (division != null) {
                for (Property property : division.getProperties()) {
                    Statement s = conn.createStatement();
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

    public static ArrayList<Household> getHouseholds() {
        return households;
    }

    public static void setHouseholds(ArrayList<Household> households) {
        Simulation.households = households;
    }

    public static ArrayList<Investor> getInvestors() {
        return investors;
    }

    public static void setInvestors(ArrayList<Investor> investors) {
        Simulation.investors = investors;
    }

    public static ArrayList<Promoter> getPromoters() {
        return promoters;
    }

    public static void setPromoters(ArrayList<Promoter> promoters) {
        Simulation.promoters = promoters;
    }

    public static AdministrativeDivision[] getDivisions() {
        return divisions;
    }

    public static void setDivisions(AdministrativeDivision[] divisions) {
        Simulation.divisions = divisions;
    }

    public static int getNumSim() {
        return numSim;
    }

    public static void setNumSim(int numSim) {
        Simulation.numSim = numSim;
    }

    public static int[] getIdManager() {
        return idManager;
    }

    public static void setIdManager(int[] idManager) {
        Simulation.idManager = idManager;
    }

    public static String getFilteredEquipments() {
        return filteredEquipments;
    }

    public static void setFilteredEquipments(String filteredEquipments) {
        Simulation.filteredEquipments = filteredEquipments;
    }

    public static String getFilteredNetwork() {
        return filteredNetwork;
    }

    public static void setFilteredNetwork(String filteredNetwork) {
        Simulation.filteredNetwork = filteredNetwork;
    }

    public static void main(String[] args){
        System.out.println("Testing the kobdig.urbanSimulation Simulator...");
        EntitiesCreator builder = new EntitiesCreator();
        builder.createAll();
        freeProperties = new ArrayList<>();
        forRentProperties = new ArrayList<>();
        households = builder.getHouseholds();
        investors = builder.getInvestors();
        divisions = builder.getDivisions();
        promoters = builder.getPromoters();
        networkLength = builder.getNetworkLength();
        equipmentsLength = builder.getEquipmentsLength();
        forSaleLand = builder.getForSaleLand();
        idManager = builder.getIdManager();
        numSim = builder.getNumSim();
        filteredEquipments = builder.getFilteredEquipments();
        filteredNetwork = builder.getFilteredNetwork();
        investorAgent = builder.getInvestorAgent();

        try {
            conn = builder.createConnection();

                writeIndicators(conn, 0);
                writeResults(conn, 0);

                Statement s = conn.createStatement();
                ResultSet r = s.executeQuery("SELECT MAX(gid) FROM properties;");
                if(r.next())  idManager[0] = r.getInt(1) + 1;
                s.close();
                r.close();

                for (time = 1; time <= numSim; time++) {
                	
                	System.out.println("Step " + time);


                    for (AdministrativeDivision division : divisions) {
                        if (division != null) {
                            ArrayList<Land> landDiv = division.getLands();
                            for (Land land : landDiv) land.step(time - 1);
                            for (Property property : division.getProperties()) property.step(time - 1);
                        }
                    }

                    for (Promoter promoter : promoters) {
                        promoterUpdateBeliefs(promoter, time-1);
                        promoterIntentionStep(conn, promoter);
                    }

                    for (Investor investor : investors) {
                        investorUpdateBeliefs(investor, time-1);
                        investorIntentionStep(conn, investor);
                    }

                    for (Household household : households) {
                        // Updates household's beliefs
                        household.householdUpdateBeliefs(time-1, freeProperties, forRentProperties);
                        // Generates household's new intentions
                        householdIntentionStep(conn, household);
                    }

                    System.err.println(time-1 + ". - free " + freeProperties.size() + " for rent " + forRentProperties.size() + " total " +
                            (freeProperties.size() + forRentProperties.size()) );

                    writeIndicators(conn, time-1);
                    writeResults(conn, time-1);

                }

            conn.close();


            } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
