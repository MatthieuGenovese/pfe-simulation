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

    private String beliefInvestor;

    private String desiresInvestor;

    private String beliefHousehold;

    private String desiresHousehold;

    private String beliefPromoter;

    private String desiresPromoter;

    private int idSimulation;

    private String time;

    public ConfigurationMongo(Date time, int num, int nbrHousehold, int nbrPromoter, int nbrInvestor, int id, Household h, Investor i, Promoter p){
        this.num = num;
        this.nbrHousehold = nbrHousehold;
        this.nbrPromoter = nbrPromoter;
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        this.time = shortDateFormat.format(time);
        this.nbrInvestor = nbrInvestor;
        this.idSimulation = id;
        this.beliefHousehold = h.beliefs().toString();
        this.beliefInvestor = i.beliefs().toString();
        this.beliefPromoter = p.beliefs().toString();
        this.desiresHousehold = h.desires().toString();
        this.desiresInvestor = i.desires().toString();
        this.desiresPromoter = p.desires().toString();
    }

    public int getId() {
        return idSimulation;
    }

    public void setId(int id) {
        this.idSimulation = id;
    }

}
