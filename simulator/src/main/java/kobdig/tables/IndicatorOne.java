package kobdig.tables;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "indicator1")
public class IndicatorOne implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gid")
    private int id;

    @Column(name = "step")
    private int step;

    @Column(name = "ROP")
    private double rop;

    public IndicatorOne(){

    }

    public IndicatorOne(int step, double rop){
        this.step = step;
        this.rop = rop;
    }
}
