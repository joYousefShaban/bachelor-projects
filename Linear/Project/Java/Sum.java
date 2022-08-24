public class Sum {
    Vector answer;
    int summationValue;
    public static Vector Sum(Vector v1,Vector v2){
        Vector answer=new Vector((v1.getX()+ v2.getX()),(v1.getY()+v2.getY()));
        return answer;
    }
}
