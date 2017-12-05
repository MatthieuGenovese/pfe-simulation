package kobdig.access.tables;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "equipamentos")
public class EquipmentE implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "geom")
    private String geom;

    @Column(name = "id_equip")
    private int id_equi;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigo_upz")
    private int codigo_upz;

    public EquipmentE(){

    }

    public EquipmentE(String geom, int id_equi, String tipo, String nombre, int codigo_upz){
        this.geom = geom;
        this.id_equi = id_equi;
        this.tipo = tipo;
        this.nombre = nombre;
        this.codigo_upz = codigo_upz;
    }

    public int getId() {
        return id;
    }

    public int getCodigo_upz() {
        return codigo_upz;
    }

    public int getId_equi() {
        return id_equi;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public int getId_Equi(){
        return id_equi;
    }

    public String getGeom() {
        return geom;
    }
}
