import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Championship chmp=new Championship(2019,"a","SSS","15",1,1);
        Championship chmp2=new Championship(2015,"a","FFF","5",3,5);
        ArrayList<Championship> temp = new ArrayList<Championship>();
        temp.add(chmp);
        temp.add(chmp2);
        Utils temp2 = new Utils("exemplo2.csv");
        temp2.printRacerCircuitRanking(temp,"a");
    }
}