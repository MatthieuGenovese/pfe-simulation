package kobdig.access.sql.tables;



import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "land")
public class LandE implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gid")
    private int id;

    @Column(name = "latitude")
    private int latitude;

    @Column(name = "longitude")
    private int longitude;

    @Column(name = "price")
    private int price;

    @Column(name = "geom")
    private String geom;

    @Column(name = "codigo_upz")
    private int codigo_upzcodigo_upz;

    public LandE(){

    }

    public LandE(int latitude, int longitude, int price, String geom, int codigo_upzcodigo_upz){
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.geom = geom;
        this.codigo_upzcodigo_upz = codigo_upzcodigo_upz;
    }

    public String getGeom() {
        return geom;
    }

    public int getId() {
        return id;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getCodigo_upzcodigo_upz() {
        return codigo_upzcodigo_upz;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getPrice() {
        return price;
    }


}
