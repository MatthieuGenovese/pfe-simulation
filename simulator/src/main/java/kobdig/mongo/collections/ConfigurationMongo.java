package kobdig.mongo.collections;

import kobdig.urbanSimulation.entities.agents.Household;
import kobdig.urbanSimulation.entities.agents.Investor;
import kobdig.urbanSimulation.entities.agents.Promoter;
import org.springframework.data.annotation.Id;
import java.text.DateFormat;
import java.util.Date;

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

    private int idSimulation;

    private String time;

    public ConfigurationMongo(String time, int num, int nbrHousehold, int nbrPromoter, int nbrInvestor, int idSimulation){
        this.num = num;
        this.nbrHousehold = nbrHousehold;
        this.nbrPromoter = nbrPromoter;
        this.time = time;
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
