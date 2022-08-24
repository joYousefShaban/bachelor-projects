public class Coliniarity {
    public static boolean IsColinear(Vector v1, Vector v2){
        if(v1.getX()/ v2.getX() == v1.getY()/ v2.getY()) return true;
        else return false;
    }
}