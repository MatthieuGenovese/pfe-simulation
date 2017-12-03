package kobdig.urbanSimulation;

import kobdig.agent.Agent;
import kobdig.repository.*;
import kobdig.tables.*;
import kobdig.urbanSimulation.entities.agents.AbstractAgent;
import kobdig.urbanSimulation.entities.agents.Household;
import kobdig.urbanSimulation.entities.agents.Investor;
import kobdig.urbanSimulation.entities.agents.Promoter;
import kobdig.urbanSimulation.entities.environement.*;
import kobdig.urbanSimulation.utils.SimulationSettings;
import org.postgis.Geometry;
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
import java.util.List;

import static org.postgis.PGgeometry.geomFromString;

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
    private SimulationSettings config;
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

    @Autowired
    DivisionRepository divisionRepository;

    @Autowired
    EquipmentRepository  equipmentRepository;

    @Autowired
    HouseholdRepository householdRepository;

    @Autowired
    IntersectionRepository intersectionRepository;

    @Autowired
    InvestorRepository investorRepository;

    @Autowired
    LandRepository landRepository;

    @Autowired
    PromoterRepository promoterRepository;

    @Autowired
    public TransportNetworkRepository transportNetworkRepository;

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
        config = new SimulationSettings();
        freeProperties = new ArrayList<>();
        forRentProperties = new ArrayList<>();
        investors = new ArrayList<>();
        forSaleLand = new ArrayList<>();
        agents = new ArrayList<>();
        divisions = new AdministrativeDivision[200];
        idManager = new int[5];
        config.parseConfFile();

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

    public SimulationSettings getConfig(){
        return config;
    }

    public void createAgents() throws IOException {
        if(config.getMode() == 0){
            householdAgentFile = new File(config.getPath() + "/householdAgent.apl");
            investorAgentFile = new File(config.getPath()+ "/investorAgent.apl");
            promoterAgentFile = new File(config.getPath()+ "/promoterAgent.apl");
        }
        else if(config.getMode() == 1){
            householdAgentFile = new File(config.getPath() + "/householdAgent"+String.valueOf(config.getActualIteration()+1)+".apl");
            investorAgentFile = new File(config.getPath()+ "/investorAgent"+String.valueOf(config.getActualIteration()+1)+".apl");
            promoterAgentFile = new File(config.getPath()+ "/promoterAgent"+String.valueOf(config.getActualIteration()+1)+".apl");
        }
        householdAgent = new Agent(new FileInputStream(householdAgentFile));
        investorAgent = new Agent(new FileInputStream(investorAgentFile));
        promoterAgent = new Agent(new FileInputStream(promoterAgentFile));
    }

    public void reset(){
        freeProperties.clear();
        forRentProperties.clear();
        investors.clear();
        forSaleLand.clear();
        agents.clear();
        divisions = new AdministrativeDivision[200];
        idManager = new int[5];
    }

    public void createAll() {
        System.out.println("Testing the kobdig.urbanSimulation Simulator...");
        time = 0;
        filteredEquipments = "(SELECT * FROM equipamentos WHERE codigo_upz IN (85,81,80,46,112,116,31,30,29,28,27";
        filteredNetwork = "(SELECT * FROM red_primaria WHERE gid IN (176,784,794,793,798,796,822,819,856,852,849,885,894," +
                "891,937,932,938,984,990,986,1029,1028,1076,1077,1113,1114,1117,1165,1164,1218,1221,1220,1280,1281,1284," +
                "1332,1330,1373,1368,1374,1418,1416,1455,1453,1487,1533,1527,51,48,52,64,63,76,94,91,90,96,102,101,106," +
                "110,109,114,113,118,117,122";

        try {
            conn = createConnection();
            createAgents();
            createDivisions();
            createTransportNetwork();
            createEquipments();
            //createProperties(conn);
            createHouseholds();
            createInvestors();
            createPromoters();
            createLand();


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

        /*try {
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
        s6.close();*/

        return conn;
    }

    private void createHouseholds() {

        for(HouseholdE householdE : householdRepository.findByNbr(nbrHousehold)){
            Household household = null;
            try {
                household = new Household(Integer.toString(householdE.getId()), householdE.getPurchasingpower(),
                        householdE.getNetmonthlyincome(), householdAgentFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            household.updateBelief("not r:1");
            household.updateBelief("not o:1");
            agents.add(household);
        }
    }

    private void createInvestors() {

        for(InvestorE investorE : investorRepository.findByNbr(nbrInvestor)){
            Investor investor = null;
            try {
                investor = new Investor(Integer.toString(investorE.getId()), investorE.getPurchasingpower(), 0.0, investorAgentFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            investors.add(investor);
        }
    }

    public ArrayList<AbstractAgent> getAgents() {
        return agents;
    }

    private void createPromoters() {

        for(PromoterE promoterE : promoterRepository.findByNbr(nbrPromoter)){
            Promoter promoter = null;
            try {
                promoter = new Promoter(Integer.toString(promoterE.getId()), promoterE.getPurchasingpower(), promoterAgentFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            agents.add(promoter);
        }
    }


    private void createLand() throws SQLException {

        for(LandE landE : landRepository.findAll()){
            Geometry geo = PGgeometry.geomFromString(landE.getGeom());
            PGgeometry geom = new PGgeometry(geo);
            Land land = new Land(Integer.toString(landE.getId()), landE.getLatitude(), landE.getLongitude(), landE.getPrice(), geom);
            if (landE.getCodigo_upzcodigo_upz() != 0) {
                land.setDivision(divisions[landE.getCodigo_upzcodigo_upz()]);
                divisions[landE.getCodigo_upzcodigo_upz()].addLand(land);
                forSaleLand.add(land);
            }
        }
    }

    private void createDivisions() throws SQLException {

        for(DivisionE divisionE : divisionRepository.findAll()){
            Geometry geo = PGgeometry.geomFromString(divisionE.getGeom());
            PGgeometry geom = new PGgeometry(geo);
            AdministrativeDivision division = new AdministrativeDivision(Integer.toString(divisionE.getId()), divisionE.getCodigo_upz(), geom);
            divisions[divisionE.getCodigo_upz()] = division;
        }
    }

    private void createTransportNetwork() throws SQLException {

        for(TransportNetworkE transportNetworkE : transportNetworkRepository.findAll()){
            if(isInListT(transportNetworkE.getId())) {
                Geometry geo = PGgeometry.geomFromString(transportNetworkE.getGeom());
                PGgeometry geom = new PGgeometry(geo);
                TransportNetwork transportNetwork = new TransportNetwork(Integer.toString(transportNetworkE.getId()), "primary", geom);
                for (IntersectionE intersectionE : intersectionRepository.findById_Redprimaria(transportNetworkE.getId())) {
                    if (intersectionE.getCodigo_upz() != 0) {
                        transportNetwork.setDivision(divisions[intersectionE.getCodigo_upz()]);
                        divisions[intersectionE.getCodigo_upz()].addNetwork(transportNetwork);
                    }
                }
            }
        }
    }

    public Agent getInvestorAgent() {
        return investorAgent;
    }

    private void createEquipments() throws SQLException {

        for(EquipmentE equipmentE : equipmentRepository.findAll()){
            if(isInListE(equipmentE.getCodigo_upz())) {
                Geometry geo = PGgeometry.geomFromString(equipmentE.getGeom());
                PGgeometry geom = new PGgeometry(geo);
                Equipment equip = new Equipment(Integer.toString(equipmentE.getId()), equipmentE.getTipo(), geom);
                if (equipmentE.getCodigo_upz() != 0 && equipmentE.getCodigo_upz() <= 117) {
                    equip.setDivision(divisions[equipmentE.getCodigo_upz()]);
                    divisions[equipmentE.getCodigo_upz()].addEquipement(equip);
                    equipmentsLength++;
                }
            }
        }
    }

    public boolean isInListT(int el){
        List<Integer> id = new ArrayList<>();
        id.add(176);
        id.add(784);
        id.add(794);
        id.add(793);
        id.add(798);
        id.add(796);
        id.add(822);
        id.add(819);
        id.add(856);
        id.add(852);
        id.add(849);

        for(Integer i : id){
            if(el != i){
                return false;
            }
        }

        return true;
    }

    public boolean isInListE(int el){
        List<Integer> codigo = new ArrayList<>();
        codigo.add(85);
        codigo.add(81);
        codigo.add(80);
        codigo.add(46);
        codigo.add(112);
        codigo.add(116);
        codigo.add(31);
        codigo.add(30);
        codigo.add(29);
        codigo.add(28);
        codigo.add(27);

        for(Integer i : codigo){
            if(el != i){
                return false;
            }
        }

        return true;

    }

    public Connection getConn(){
      return conn;
    }
}
