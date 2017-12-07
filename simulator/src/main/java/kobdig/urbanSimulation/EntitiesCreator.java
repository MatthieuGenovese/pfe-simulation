package kobdig.urbanSimulation;

import kobdig.access.sql.repository.*;
import kobdig.access.sql.tables.*;
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
    private SimulationSettings config;
    private ArrayList<AbstractAgent> agents;
    private int numSim, networkLength, equipmentsLength;
    private int[] idManager;
    File householdAgentFile;
    File investorAgentFile;
    File promoterAgentFile;

    private int nbrHousehold;
    private int nbrInvestor;
    private int nbrPromoter;

    private List<Integer> listOfEquipment;
    private List<Integer> listOfTransport;

    private int id;

    private int time;

    protected static EntitiesCreator _singleton;

    @Autowired
    DivisionRepository divisionRepository;

    @Autowired
    public EquipmentRepository  equipmentRepository;

    @Autowired
    public HouseholdRepository householdRepository;

    @Autowired
    public IntersectionRepository intersectionRepository;

    @Autowired
    public InvestorRepository investorRepository;

    @Autowired
    public LandRepository landRepository;

    @Autowired
    public PromoterRepository promoterRepository;

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
        listOfEquipment = new ArrayList<>();
        listOfTransport = new ArrayList<>();

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

    public List<Integer> getListOfEquipment(){
        return listOfEquipment;
    }

    public void setListOfEquipment(List<Integer> listOfEquipment) {
        this.listOfEquipment = listOfEquipment;
    }

    public List<Integer> getListOfTransport() {
        return listOfTransport;
    }

    public void setListOfTransport(java.util.List<Integer> listOfTransport) {
        this.listOfTransport = listOfTransport;
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
    }

    public void reset(){
        time = 0;
        freeProperties.clear();
        forRentProperties.clear();
        investors.clear();
        forSaleLand.clear();
        agents.clear();
        divisions = new AdministrativeDivision[200];
        idManager = new int[5];
    }

    public void createAll() {
        System.out.println("Initialisation...");
        reset();
        try {
            createAgents();
            createDivisions();
            createTransportNetwork();
            createEquipments();
            createHouseholds();
            createInvestors();
            createPromoters();
            createLand();


        } catch (Exception e) {
            e.printStackTrace();
        }
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

        for (TransportNetworkE transportNetworkE : transportNetworkRepository.findById(listOfTransport)) {
            networkLength++;
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

    private void createEquipments() throws SQLException {

        for(EquipmentE equipmentE : equipmentRepository.findByCodigo_Upz(listOfEquipment)){
            if(equipmentE.getGeom() != null) {
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
}
