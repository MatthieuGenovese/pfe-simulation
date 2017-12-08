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

    private double purchasingpower;



    public PromoterMongo(){

    }

    public PromoterMongo(int sumlationId, int step, Promoter p){
        landsId = new ArrayList<>();
        this.step = step;
        this.idSimulation = sumlationId;
        this.id = p.getId();
        this.purchasingpower = p.getPurchasingPower();
        for(Land l : p.getPurchasableLand()){
            landsId.add(l.getId());
        }
    }
}