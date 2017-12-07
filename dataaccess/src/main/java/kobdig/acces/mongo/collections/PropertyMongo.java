package kobdig.acces.mongo.collections;

import org.springframework.data.annotation.Id;
/**
 * Created by Matthieu on 07/12/2017.
 */
public class PropertyMongo  {

    @Id
    private String id;

    private int idSimularion;

    private int step;

    private double idProperty;

    private double price;

    private double rent;

    private double value;

    private String state;

    private int codigo_upz;

    private String geom;


    public PropertyMongo(){

    }

    public PropertyMongo(int idSimularion, int step, double idProperty, double price, double rent, double value, String state, int codigo_upz, String geom){
        this.idSimularion = idSimularion;
        this.step = step;
        this.idProperty = idProperty;
        this.price = price;
        this.rent = rent;
        this.value = value;
        this.state = state;
        this.codigo_upz = codigo_upz;
        this.geom = geom;
    }
}