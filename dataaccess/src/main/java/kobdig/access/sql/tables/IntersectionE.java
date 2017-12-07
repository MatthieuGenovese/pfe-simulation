package kobdig.access.sql.tables;


import javax.persistence.*;

@Entity
@Table(name = "interseccion_upz_redprimaria")
public class IntersectionE {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gid")
    private int id;

    @Column(name = "id_redprimaria")
    private int id_redprimaria;

    @Column(name = "codigo_upz")
    private int codigo_upz;

    public IntersectionE(){

    }

    public IntersectionE(int id_redprimaria, int codigo_upz){
        this.id_redprimaria = id_redprimaria;
        this.codigo_upz = codigo_upz;
    }

    public int getId() {
        return id;
    }

    public int getCodigo_upz() {
        return codigo_upz;
    }

    public int getId_redprimaria() {
        return id_redprimaria;
    }
}
