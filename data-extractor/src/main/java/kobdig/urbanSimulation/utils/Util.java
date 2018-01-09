package kobdig.urbanSimulation.utils;

import java.util.ArrayList;
import java.util.List;

public class Util {


    public static boolean isInListT(int el){
        List<Integer> id = new ArrayList<>();
        id.add(176);
        id.add(784);
        id.add(794);
        id.add(793);
        id.add(798);
        id.add(796);
        id.add(822);
        id.add(819);
        id.add(856);
        id.add(852);
        id.add(849);

        for(Integer i : id){
            if(el != i){
                return false;
            }
        }

        return true;
    }

    public boolean isInListE(int el){
        List<Integer> codigo = new ArrayList<>();
        codigo.add(85);
        codigo.add(81);
        codigo.add(80);
        codigo.add(46);
        codigo.add(112);
        codigo.add(116);
        codigo.add(31);
        codigo.add(30);
        codigo.add(29);
        codigo.add(28);
        codigo.add(27);

        for(Integer i : codigo){
            if(el != i){
                return false;
            }
        }

        return true;

    }

}
