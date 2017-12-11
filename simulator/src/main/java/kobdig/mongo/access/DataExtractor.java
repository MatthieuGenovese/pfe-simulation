package kobdig.mongo.access;

import kobdig.mongo.collections.*;
import kobdig.mongo.repository.*;

import java.io.*;
import java.util.List;

/**
 * Created by Matthieu on 11/12/2017.
 */
public class DataExtractor {

    public DataExtractor(){

    }

    public String findPropertiesBySimulationId(PropertyMongoRepository repo, int idSimulation){
        List<PropertyMongo> res = repo.findByidSimulation(idSimulation);
        BufferedWriter writer;
        String filename = foundLastFile("property", idSimulation);
        try {
            writer = new BufferedWriter(new FileWriter(new File(filename), false));
            writer.write("etape\t");
            writer.write("id\t");
            writer.write("price\t");
            writer.write("capitalizedRent\t");
            writer.write("value\t");
            writer.write("state\t\n");
            for(PropertyMongo l : res){
                writer.write(l.getStep()+"\t");
                writer.write(l.getIdProperty() + "\t");
                writer.write(l.getPrice() + "\t");
                writer.write(l.getCapitalizedRent()+"\t");
                writer.write(l.getValue()+"\t");
                writer.write(l.getState() + "\t\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String findLandsBySimulationId(LandMongoRepository repo, int idSimulation){
        List<LandMongo> res = repo.findByidSimulation(idSimulation);
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

    public String findInvestorsBySimulationId(InvestorMongoRepository repo, int idSimulation){
        List<InvestorMongo> res = repo.findByidSimulation(idSimulation);
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

    public String findHouseholdsBySimulationId(HouseholdMongoRepository repo, int idSimulation){
        List<HouseholdMongo> res = repo.findByidSimulation(idSimulation);
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

    public String findPromotersBySimulationId(PromoterMongoRepository repo, int idSimulation){
        List<PromoterMongo> res = repo.findByidSimulation(idSimulation);
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
        String res = "results/Simulation "+idSimulation + "/" + type + ".csv";
        File dir = new File("results/Simulation "+idSimulation);
        if (!dir.isDirectory()){
            dir.mkdirs();
        }
        return res;
    }
}
