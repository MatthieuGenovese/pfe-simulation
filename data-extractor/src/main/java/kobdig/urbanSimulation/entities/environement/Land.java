package kobdig.urbanSimulation.entities.environement;

import kobdig.urbanSimulation.entities.IActionnable;
import org.postgis.PGgeometry;

/**
 * Created by Matthieu on 20/11/2017.
 */
public class Land extends AbstractEnvironment implements IActionnable{
    private double price;
    private double transportUtility = 0.0;
    private double equipementUtility = 0.0;

    public double getTransportUtility() {
        return transportUtility;
    }

    public void setTransportUtility(double transportUtility) {
        this.transportUtility = transportUtility;
    }

    public double getEquipementUtility() {
        return equipementUtility;
    }

    public void setEquipementUtility(double equipementUtility) {
        this.equipementUtility = equipementUtility;
    }

    public Land(String id, double latitude, double longitude, double price, PGgeometry geom) {
        super(id,latitude,longitude,geom);
        this.price = price;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void step(int time) {

        //price = (price - Math.exp(time) < 0) ? 0.0 : price - Math.exp(time);
        price = price * 0.97;
        }

}
