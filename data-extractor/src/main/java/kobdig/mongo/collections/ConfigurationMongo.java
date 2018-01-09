package kobdig.mongo.collections;

import org.springframework.data.annotation.Id;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Matthieu on 09/12/2017.
 */
public class ConfigurationMongo {
    @Id
    private String  mongo_id;

    private int num;

    private int nbrHousehold;

    private int nbrPromoter;

    private int nbrInvestor;

    private String fileHouseholds, filePromoters, fileInvestors;

    private int idSimulation;

    private List<Integer> listOfEquipement;

    private List<Integer> listOfNetwork;

    private String time;

    public int getNum() {
        return num;
    }

    public int getNbrHousehold() {
        return nbrHousehold;
    }

    public int getNbrPromoter() {
        return nbrPromoter;
    }

    public int getNbrInvestor() {
        return nbrInvestor;
    }

    public String getFileHouseholds() {
        return fileHouseholds;
    }

    public String getFilePromoters() {
        return filePromoters;
    }

    public String getFileInvestors() {
        return fileInvestors;
    }

    public int getIdSimulation() {
        return idSimulation;
    }

    public List<Integer> getListOfEquipement() {
        return listOfEquipement;
    }

    public List<Integer> getListOfNetwork() {
        return listOfNetwork;
    }

    public String getTime() {
        return time;
    }

    public ConfigurationMongo(String time, int num, int nbrHousehold, int nbrPromoter, int nbrInvestor, int idSimulation, String fileHouseholds, String filePromoters, String fileInvestors, List<Integer> listOfEquipement, List<Integer> listOfNetwork){
        this.num = num;
        this.nbrHousehold = nbrHousehold;
        this.nbrPromoter = nbrPromoter;
        this.time = time;
        this.listOfEquipement = listOfEquipement;
        this.listOfNetwork = listOfNetwork;
        this.fileHouseholds = fileHouseholds;
        this.fileInvestors = fileInvestors;
        this.filePromoters = filePromoters;
        this.nbrInvestor = nbrInvestor;
        this.idSimulation = idSimulation;
    }

    public int getId() {
        return idSimulation;
    }

    public void setId(int id) {
        this.idSimulation = id;
    }

}
