package kobdig.sql.tables;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "red_primaria")
public class TransportNetworkE implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gid")
    private int id;

    @Column(name = "geom")
    private String geom;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "nombre")
    private String nombre;

    public TransportNetworkE(){

    }

    public TransportNetworkE(String geom, String tipo, String nombre){
        this.geom = geom;
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public int getId(){
        return id;
    }

    public String getGeom(){
        return geom;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }
}
