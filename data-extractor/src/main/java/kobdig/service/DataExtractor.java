package kobdig.service;

import kobdig.mongo.collections.*;
import kobdig.mongo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthieu on 11/12/2017.
 */

@Component
public class DataExtractor {

    @Autowired
    private PropertyMongoRepository propertyMongoRepository;

    @Autowired
    private HouseholdMongoRepository householdMongoRepository;

    @Autowired
    private PromoterMongoRepository promoterMongoRepository;

    @Autowired
    private LandMongoRepository landMongoRepository;

    @Autowired
    private InvestorMongoRepository investorMongoRepository;

    @Autowired
    private ConfigurationMongoRepository configurationMongoRepository;

    public String findPropertiesBySimulationId(int idSimulation){
        List<PropertyMongo> res = propertyMongoRepository.findByidSimulation(idSimulation);
        BufferedWriter writer;
        String filename = foundLastFile("property", idSimulation);
        try {
            writer = new BufferedWriter(new FileWriter(new File(filename), false));
            writer.write("etape\t");
            writer.write("id\t");
            writer.write("currentPrice\t");
            writer.write("previousPrice\t");
            writer.write("currentCapitalizedRent\t");
            writer.write("previousCapitalizedRent\t");
            writer.write("currentPotentialRent\t");
            writer.write("previousPotentialRent\t");
            writer.write("currentValue\t");
            writer.write("previousValue\t");
            writer.write("state\t\n");
            for(PropertyMongo l : res){
                writer.write(l.getStep()+"\t");
                writer.write(l.getIdProperty() + "\t");
                writer.write(l.getCurrentPrice() + "\t");
                writer.write(l.getPreviousPrice() + "\t");
                writer.write(l.getCurrentCapitalizedRent()+"\t");
                writer.write(l.getPreviousCapitalizedRent() + "\t");
                writer.write(l.getCurrentPotentialRent() + "\t");
                writer.write(l.getPreviousPotentialRent() + "\t");
                writer.write(l.getCurrentValue()+"\t");
                writer.write(l.getPreviousValue() + "\t");
                writer.write(l.getState() + "\t\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String findConfigurationBySimulationId(int idSimulation){
        ConfigurationMongo res = configurationMongoRepository.findByidSimulation(idSimulation);
        BufferedWriter writer;
        String filename = foundLastFile("configuration", idSimulation);
        try {
            writer = new BufferedWriter(new FileWriter(new File(filename), false));
            writer.write("id\t");
            writer.write("nombreHouseholds\t");
            writer.write("nombreInvestors\t");
            writer.write("nombrePromoters\t");
            writer.write("fileHouseholds\t");
            writer.write("fileInvestors\t");
            writer.write("filePromoters\t");
            writer.write("listOfEquipement\t");
            writer.write("listOfNetwork\t");
            writer.write("time\t\n");
            writer.write(res.getIdSimulation() + "\t");
            writer.write(res.getNbrHousehold() + "\t");
            writer.write(res.getNbrInvestor() + "\t");
            writer.write(res.getNbrPromoter() + "\t");
            writer.write(res.getFileHouseholds() + "\t");
            writer.write(res.getFileInvestors() + "\t");
            writer.write(res.getFilePromoters() + "\t");
            writer.write(listToString(res.getListOfEquipement()) + "\t");
            writer.write(listToString(res.getListOfNetwork()) + "\t");
            writer.write(res.getTime() + "\t\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    private String listToString(List<Integer> list){
        String res = "[ ";
        for(Integer i : list){
            res+= i + " ";
        }
        res+= "]";
        return res;
    }

    public String findLandsBySimulationId(int idSimulation){
        List<LandMongo> res = landMongoRepository.findByidSimulation(idSimulation);
        BufferedWriter writer;
        String filename = foundLastFile("land", idSimulation);
        try {
            writer = new BufferedWriter(new FileWriter(new File(filename), false));
            writer.write("etape\t");
            writer.write("id\t");
            writer.write("price\t");
            writer.write("utility\t\n");
            for(LandMongo l : res){
                writer.write(l.getStep()+"\t");
                writer.write(l.getId() + "\t");
                writer.write(l.getPrice() + "\t");
                writer.write(l.getUtility() + "\t\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String findInvestorsBySimulationId(int idSimulation){
        List<InvestorMongo> res = investorMongoRepository.findByidSimulation(idSimulation);
        BufferedWriter writer;
        String filename = foundLastFile("investor", idSimulation);
        try {
            writer = new BufferedWriter(new FileWriter(new File(filename), false));
            writer.write("etape\t");
            writer.write("id\t");
            writer.write("householdID\t");
            writer.write("investDegree\t");
            writer.write("speculate\t");
            writer.write("currentRent\t");
            writer.write("pruchasingpower\t\n");
            for(InvestorMongo h : res){
                writer.write(h.getStep()+"\t");
                writer.write(h.getId() + "\t");
                writer.write(h.getHouseholdId() + "\t");
                writer.write(h.getInvestDegree() + "\t");
                writer.write(h.getSpeculate()+"\t");
                writer.write(h.getCurrentrent() + "\t");
                writer.write(h.getPurchasingpower() + "\t\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String findHouseholdsBySimulationId(int idSimulation){
        List<HouseholdMongo> res = householdMongoRepository.findByidSimulation(idSimulation);
        BufferedWriter writer;
        String filename = foundLastFile("household", idSimulation);
        try {
            writer = new BufferedWriter(new FileWriter(new File(filename), false));
            writer.write("etape\t");
            writer.write("id\t");
            writer.write("propertyId\t");
            writer.write("rentablesproperties\t");
            writer.write("pruchasingpower\t");
            writer.write("netmonthlyincome\t\n");
            for(HouseholdMongo h : res){
                writer.write(h.getStep()+"\t");
                writer.write(h.getId() + "\t");
                writer.write(h.getPropertyId() + "\t");
                writer.write(h.getRentableProperties()+"\t");
                writer.write(h.getPurchasingpower() + "\t");
                writer.write(h.getNetmonthlyincome() + "\t\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String findPromotersBySimulationId(int idSimulation){
        List<PromoterMongo> res = promoterMongoRepository.findByidSimulation(idSimulation);
        BufferedWriter writer;
        String filename = foundLastFile("promoter", idSimulation);
        try {
            writer = new BufferedWriter(new FileWriter(new File(filename), false));
            writer.write("etape\t");
            writer.write("id\t");
            writer.write("landssize\t");
            writer.write("pruchasingpower\t\n");
            for(PromoterMongo p : res){
                writer.write(p.getStep()+"\t");
                writer.write(p.getId() + "\t");
                writer.write(p.getLandsSize() + "\t");
                writer.write(p.getPurchasingpower() + "\t\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    private String foundLastFile(String type, int idSimulation){
        String res = "results/Simulation"+idSimulation + "/" + type + ".csv";
        File dir = new File("results/Simulation"+idSimulation);
        if (!dir.isDirectory()){
            dir.mkdirs();
        }
        return res;
    }
}
