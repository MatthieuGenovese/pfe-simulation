package kobdig.tables;


import org.hibernate.annotations.Type;
import org.postgis.PGgeometry;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "upz")
public class DivisionE implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gid")
    private int id;

    @Column(name = "codigo_upz")
    private int codigo_upz;

    @Column(name = "geom")
    private String geom;

    public DivisionE(){

    }

    public DivisionE(int codigo_upz, String geom){
        this.codigo_upz = codigo_upz;
        this.geom = geom;
    }

    public int getCodigo_upz() {
        return codigo_upz;
    }

    public int getId() {
        return id;
    }

    public String getGeom() {
        return geom;
    }

    @Override
    public String toString(){
        return "Division "+getId()+";"+geom+";"+codigo_upz;
    }
}
