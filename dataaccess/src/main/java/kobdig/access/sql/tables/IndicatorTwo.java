package kobdig.access.sql.tables;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "indicator2")
public class IndicatorTwo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gid")
    private int id;

    @Column(name = "step")
    private int step;

    @Column(name = "idUPZ")
    private double idUPZ;

    @Column(name = "si")
    private double si;

    public IndicatorTwo(){

    }

    public IndicatorTwo(int step, double idUPZ, double si){
        this.step = step;
        this.idUPZ = idUPZ;
        this.si = si;
    }

}
