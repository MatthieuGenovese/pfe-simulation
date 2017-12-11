package kobdig.mongo.collections;


import kobdig.urbanSimulation.entities.agents.Promoter;
import kobdig.urbanSimulation.entities.environement.Land;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

/**
 * Created by Matthieu on 08/12/2017.
 */
public class PromoterMongo {
    @Id
    private String mongo_id;

    private String id;

    private int step;

    private int idSimulation;

    private ArrayList<String> landsId;

    private int landsSize;

    private double purchasingpower;



    public PromoterMongo(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getIdSimulation() {
        return idSimulation;
    }

    public void setIdSimulation(int idSimulation) {
        this.idSimulation = idSimulation;
    }

    public ArrayList<String> getLandsId() {
        return landsId;
    }

    public void setLandsId(ArrayList<String> landsId) {
        this.landsId = landsId;
    }

    public int getLandsSize() {
        return landsSize;
    }

    public void setLandsSize(int landsSize) {
        this.landsSize = landsSize;
    }

    public double getPurchasingpower() {
        return purchasingpower;
    }

    public void setPurchasingpower(double purchasingpower) {
        this.purchasingpower = purchasingpower;
    }

    public PromoterMongo(int sumlationId, int step, Promoter p){
        landsId = new ArrayList<>();
        this.step = step;
        this.idSimulation = sumlationId;
        this.id = p.getId();
        this.purchasingpower = p.getPurchasingPower();
        this.landsSize = p.getPurchasableLand().size();

        for(Land l : p.getPurchasableLand()){
            landsId.add(l.getId());
        }
    }
}