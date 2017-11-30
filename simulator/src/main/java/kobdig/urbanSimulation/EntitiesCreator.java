package kobdig.urbanSimulation;

import kobdig.agent.Agent;
import kobdig.urbanSimulation.entities.agents.AbstractAgent;
import kobdig.urbanSimulation.entities.agents.Household;
import kobdig.urbanSimulation.entities.agents.Investor;
import kobdig.urbanSimulation.entities.agents.Promoter;
import kobdig.urbanSimulation.entities.environement.*;
import org.postgis.PGgeometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Matthieu on 20/11/2017.
 */
@Service
public class EntitiesCreator {
    private ArrayList<Investor> investors;
    private ArrayList<Land> forSaleLand;
    private AdministrativeDivision[] divisions;
    private ArrayList<Property> freeProperties;
    private ArrayList<Property> forRentProperties;
    private String filteredEquipments, filteredNetwork;
    private Connection conn;
    private Agent householdAgent, investorAgent, promoterAgent;
    private ArrayList<AbstractAgent> agents;
    private int numSim, networkLength, equipmentsLength;
    private int[] idManager;
    File householdAgentFile;
    File investorAgentFile;
    File promoterAgentFile;

    private int nbrHousehold;
    private int nbrInvestor;
    private int nbrPromoter;

    private int id;

    private int time;

    protected static EntitiesCreator _singleton;

    @PostConstruct
    public static void init() {

        if (_singleton != null) {
            throw new RuntimeException("EntitiesCreator already created by "
                    + _singleton.getClass().getName());
        } else {
            _singleton = new EntitiesCreator();
        }
    }

    @Autowired
    public static EntitiesCreator getInstance() {
        return _singleton;
    }

    private EntitiesCreator(){

        freeProperties = new ArrayList<>();
        forRentProperties = new ArrayList<>();
        investors = new ArrayList<>();
        forSaleLand = new ArrayList<>();
        agents = new ArrayList<>();
        divisions = new AdministrativeDivision[200];
        idManager = new int[5];

    }

    public void begin(){
        freeProperties.clear();
        forRentProperties.clear();
        investors.clear();
        forSaleLand.clear();
        agents.clear();
        divisions = new AdministrativeDivision[200];
        idManager = new int[5];

        createAll();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNbrHousehold(int nbrHousehold){
        this.nbrHousehold = nbrHousehold;
    }

    public void setNbrInvestor(int investor){
        this.nbrInvestor = investor;
    }

    public void setNbrPromoter(int nbrPromoter){
        this.nbrPromoter = nbrPromoter;
    }

    public File getHouseholdAgentFile() {
        return householdAgentFile;
    }

    public File getInvestorAgentFile() {
        return investorAgentFile;
    }

    public File getPromoterAgentFile() {
        return promoterAgentFile;
    }

    public void setHouseholdAgentFile(File file){
        this.householdAgentFile = file;
    }

    public void setInvestorAgentFile(File file){
        this.investorAgentFile = file;
    }

    public void setPromoterAgentFile(File file){
        this.promoterAgentFile = file;
    }

    public ArrayList<Property> getFreeProperties() {
        return freeProperties;
    }

    public ArrayList<Property> getForRentProperties() {
        return forRentProperties;
    }

    public ArrayList<Investor> getInvestors() {
        return investors;
    }

    public ArrayList<Land> getForSaleLand() {
        return forSaleLand;
    }

    public AdministrativeDivision[] getDivisions() {
        return divisions;
    }

    public String getFilteredEquipments() {
        return filteredEquipments;
    }

    public String getFilteredNetwork() {
        return filteredNetwork;
    }

    public int getNumSim() {
        return numSim;
    }

    public void setNumSim(int numSim){ this.numSim = numSim; }

    public int getNetworkLength() {
        return networkLength;
    }

    public int getEquipmentsLength() {
        return equipmentsLength;
    }

    public int[] getIdManager() {
        return idManager;
    }

    public int getTime(){
        return time;
    }

    public void setTime(int time){
        this.time = time;
    }

    public void createAll() {
        System.out.println("Testing the kobdig.urbanSimulation Simulator...");
        time = 0;
        String pwd = new File("").getAbsolutePath();
        filteredEquipments = "(SELECT * FROM equipamentos WHERE codigo_upz IN (85,81,80,46,112,116,31,30,29,28,27";
        filteredNetwork = "(SELECT * FROM red_primaria WHERE gid IN (176,784,794,793,798,796,822,819,856,852,849,885,894," +
                "891,937,932,938,984,990,986,1029,1028,1076,1077,1113,1114,1117,1165,1164,1218,1221,1220,1280,1281,1284," +
                "1332,1330,1373,1368,1374,1418,1416,1455,1453,1487,1533,1527,51,48,52,64,63,76,94,91,90,96,102,101,106," +
                "110,109,114,113,118,117,122";

        try {
            conn = createConnection();

            householdAgentFile = new File(pwd + "/docs/householdAgent.apl");
            investorAgentFile = new File(pwd + "/docs/investorAgent.apl");
            promoterAgentFile = new File(pwd + "/docs/promoterAgent.apl");
            try {
                householdAgent = new Agent(new FileInputStream(householdAgentFile));
                investorAgent = new Agent(new FileInputStream(investorAgentFile));
                promoterAgent = new Agent(new FileInputStream(promoterAgentFile));

                createDivisions(conn);
                createTransportNetwork(conn);
                createEquipments(conn);
                //createProperties(conn);
                createHouseholds(conn);
                createInvestors(conn);
                createPromoters(conn);
                createLand(conn);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  Connection createConnection() throws ClassNotFoundException, SQLException {
        //  Load the JDBC driver and establish a connection.
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/tomsa";
        conn = DriverManager.getConnection(url, "tomsa", "tomsa");
        //  Add the geometry types to the connection.
        ((org.postgresql.PGConnection) conn).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
        ((org.postgresql.PGConnection) conn).addDataType("box3d", Class.forName("org.postgis.PGbox3d"));

        try {
            Statement s1 = conn.createStatement();
            s1.executeUpdate("DROP TABLE properties_state");
            s1.close();
        } catch (SQLException e) {
            System.out.println("Creating results table...");
        }

        try {
            Statement s1 = conn.createStatement();
            s1.executeUpdate("DROP TABLE indicator1");
            s1.close();
        } catch (SQLException e) {
            System.out.println("Creating indicator1 table...");
        }

        try {
            Statement s1 = conn.createStatement();
            s1.executeUpdate("DROP TABLE indicator2");
            s1.close();
        } catch (SQLException e) {
            System.out.println("Creating indicator1 table...");
        }

        Statement s2 = conn.createStatement();
        String query = "CREATE TABLE \"properties_state\" (gid serial,\"idSimularion\" numeric, \"step\" numeric,\"idProperty\" numeric,\"price\"" +
                "numeric,\"rent\" numeric,\"value\" numeric,\"state\" varchar(200),\"codigo_upz\" numeric)";
        s2.executeUpdate(query);
        s2.close();

        Statement s3 = conn.createStatement();
        query = "ALTER TABLE \"properties_state\" ADD PRIMARY KEY (gid)";
        s3.executeUpdate(query);
        s3.close();

        Statement s4 = conn.createStatement();
        query = "SELECT AddGeometryColumn('','properties_state','geom','0','POINT',2)";
        s4.executeQuery(query);
        s4.close();

        Statement s5 = conn.createStatement();
        query = "CREATE TABLE \"indicator1\" (gid serial,\"step\" numeric,\"ROP\" numeric)";
        s5.executeUpdate(query);
        s5.close();

        Statement s6 = conn.createStatement();
        query = "CREATE TABLE \"indicator2\" (gid serial,\"step\" numeric,\"idUPZ\" numeric,\"si\" numeric)";
        s6.executeUpdate(query);
        s6.close();

        return conn;
    }

    private void createHouseholds(Connection conn) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery("select * from households limit "+nbrHousehold);
        while (r.next()) {
            int id = r.getInt(1);
            String lastname = r.getString(2);
            double purchasingPower = r.getDouble(3);
            double netMonthlyIncome = r.getDouble(4);
            Household household = null;
            try {
                household = new Household(Integer.toString(id), purchasingPower,
                        netMonthlyIncome, householdAgentFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            household.updateBelief("not r:1");
            household.updateBelief("not o:1");
            agents.add(household);
        }
        r.close();
        s.close();
    }

    private void createInvestors(Connection conn) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery("select * from investors limit "+nbrInvestor);
        while (r.next()) {
            int id = r.getInt(1);
            double purchasingPower = r.getDouble(2);
            Investor investor = null;
            try {
                investor = new Investor(Integer.toString(id), purchasingPower, 0.0, investorAgentFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            investors.add(investor);
        }
        r.close();
        s.close();
    }

    public ArrayList<AbstractAgent> getAgents() {
        return agents;
    }

    private void createPromoters(Connection conn) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery("select * from promoters limit "+nbrPromoter);
        while (r.next()) {
            int id = r.getInt(1);
            double purchasingPower = r.getDouble(2);
            Promoter promoter = null;
            try {
                promoter = new Promoter(Integer.toString(id), purchasingPower, promoterAgentFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            agents.add(promoter);

        }
        r.close();
        s.close();
    }


    private void createLand(Connection conn) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery("select * from land");
        while (r.next()) {
            int id = r.getInt(1);
            double lat = r.getDouble(2);
            double lon = r.getDouble(3);
            double price = r.getDouble(4);
            PGgeometry geom = (PGgeometry) r.getObject(5);
            int codigo_upz = r.getInt(6);
            Land land = new Land(Integer.toString(id), lat, lon, price, geom);
            if (codigo_upz != 0) {
                land.setDivision(divisions[codigo_upz]);
                divisions[codigo_upz].addLand(land);
                forSaleLand.add(land);
            }
        }
        r.close();
        s.close();
    }

    private void createDivisions(Connection conn) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery("select gid,codigo_upz,geom from upz");
        while (r.next()) {
            int id = r.getInt(1);
            int code = r.getInt(2);
            PGgeometry geom = (PGgeometry) r.getObject(3);
            AdministrativeDivision division = new AdministrativeDivision(Integer.toString(id), code, geom);
            divisions[code] = division;
        }
        r.close();
        s.close();
    }

    private void createTransportNetwork(Connection conn) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery("select a.gid,a.geom from " + filteredNetwork + ")) a");
        while (r.next()) {
            networkLength++;
            int id = r.getInt(1);
            PGgeometry geom = (PGgeometry) r.getObject(2);
            TransportNetwork network = new TransportNetwork(Integer.toString(id), "primary", geom);
            Statement s1 = conn.createStatement();
            String query = "SELECT codigo_upz FROM interseccion_upz_redprimaria where id_redprimaria = '" + id + "';";
            ResultSet r1 = s1.executeQuery(query);
            while (r1.next()) {
                int codigo_upz = r1.getInt(1);
                if (codigo_upz != 0) {
                    network.setDivision(divisions[codigo_upz]);
                    divisions[codigo_upz].addNetwork(network);
                }
            }
            s1.close();
            r1.close();
        }
        r.close();
        s.close();
    }

    public Agent getInvestorAgent() {
        return investorAgent;
    }

    private void createEquipments(Connection conn) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery("select a.id,a.codigo_upz,a.geom,a.nombre,a.tipo from " + filteredEquipments + ")) a");
        while (r.next()) {
            int id = r.getInt(1);
            int codigo_upz = r.getInt(2);
            PGgeometry geom = (PGgeometry) r.getObject(3);
            String nombre = r.getString(4);
            String tipo = r.getString(5);
            Equipment equip = new Equipment(Integer.toString(id), tipo, geom);
            int codigoUPZ = r.getInt(2);
            if (codigoUPZ != 0 && codigoUPZ <= 117) {
                equip.setDivision(divisions[codigoUPZ]);
                divisions[codigoUPZ].addEquipement(equip);
                equipmentsLength++;
            }
        }
        r.close();
        s.close();
    }

    public Connection getConn(){
        return conn;
    }
}
