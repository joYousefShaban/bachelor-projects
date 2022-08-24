public class Multiplication {
        Vector answer;
        int multiplicationValue;
        public static Vector Multiplication(Vector v,int multiplicationValue){
            Vector answer=new Vector((v.getX()*multiplicationValue),(v.getY()*multiplicationValue));
            return answer;
        }
}
