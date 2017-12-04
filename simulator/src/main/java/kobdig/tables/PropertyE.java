package kobdig.tables;

import org.postgis.PGgeometry;

import javax.persistence.*;
import java.io.Serializable;



@Entity
@Table(name = "properties_state")
public class PropertyE implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gid")
    private int id;

    @Column(name = "idSimularion")
    private int idSimularion;

    @Column(name = "step")
    private int step;

    @Column(name = "idProperty")
    private int idProperty;

    @Column(name = "price")
    private double price;

    @Column(name = "rent")
    private double rent;

    @Column(name = "value")
    private double value;

    @Column(name = "state")
    private String state;

    @Column(name = "codigo_upz")
    private int codigo_upz;

    @Column(name = "geom")
    private String geom;


    public PropertyE(){

    }

    public PropertyE(int idSimularion, int step, int idProperty, double price, double rent, double value, String state, int codigo_upz, String geom){
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
