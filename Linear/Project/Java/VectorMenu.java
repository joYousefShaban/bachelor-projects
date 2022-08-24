import java.util.*;

public class VectorMenu {
    public static void ColiniarityAction(){
        Scanner cin = new Scanner(System.in);
        System.out.println("Introduce the 2 vectors: ");
        System.out.print("x1: ");
        int x1 = cin.nextInt();
        System.out.print("y1: ");
        int y1 = cin.nextInt();
        System.out.print("x2: ");
        int x2 = cin.nextInt();
        System.out.print("y2: ");
        int y2 = cin.nextInt();
        Vector v1 = new Vector(x1,y1);
        Vector v2 = new Vector(x2,y2);
        if(Coliniarity.IsColinear(v1,v2)) System.out.println("The vectors are colinear");
        else System.out.println("The vectors aren't colinear");
    }
    public static void MultiplicationAction(){
        Scanner cin = new Scanner(System.in);
        System.out.println("Introduce the vector: ");
        System.out.print("x: ");
        int x = cin.nextInt();
        System.out.print("y: ");
        int y = cin.nextInt();
        System.out.print("Introduce multiplication value: ");
        int m = cin.nextInt();
        Vector v = new Vector(x,y);
        Vector result = Multiplication.Multiplication(v,m);
        System.out.println("The result is "+ result.toString());

    }
    public static void SumAction(){
        Scanner cin = new Scanner(System.in);
        System.out.println("Please introduce the 2 vectors:");
        System.out.print("x1: ");
        int x1 = cin.nextInt();
        System.out.print("y1: ");
        int y1 = cin.nextInt();
        System.out.print("x2: ");
        int x2 = cin.nextInt();
        System.out.print("y2: ");
        int y2 = cin.nextInt();
        Vector v1 = new Vector(x1,y1);
        Vector v2 = new Vector(x2,y2);
        Vector answer = Sum.Sum(v1,v2);
        System.out.println("The result is "+ answer.toString());
    }


    public static void main(String... args){
        Scanner cin = new Scanner(System.in);
        System.out.println("What operation would you want to do?");
        System.out.println("1- Check the coliniarity of 2 vectors");
        System.out.println("2- Multiply a vector by a scalar value");
        System.out.println("3- Sum 2 vectors");
        int x = cin.nextInt();

        switch (x) {
            case 1: ColiniarityAction();
            break;
            case 2: MultiplicationAction();
            break;
            case 3: SumAction();
            break;
        }
    }
}