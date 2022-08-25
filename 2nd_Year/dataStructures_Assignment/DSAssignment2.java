package ds.assignment;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Stack;

public class DSAssignment2 
{
    public static String balance(String str)
    {
        Stack stk=new Stack();
        for(int i=0;i<str.length();i++)
        {
            char character=str.charAt(i);
            if(character=='(' || character=='{' || character=='[')
                stk.push(str.charAt(i));
            else if(character==')' && stk.peek().equals('('))
                stk.pop();
            else if(character=='}' && stk.peek().equals('{'))
                stk.pop();
            else if(character==']' && stk.peek().equals('['))
                stk.pop();
        }
        if(stk.empty())
            return "balanced";
        return "unbalanced";
    }

    public static void main(String[] args) throws FileNotFoundException 
    {
        //Q2
        Scanner txtfile=new Scanner (new FileReader("src/balance.txt"));
        StringBuilder str=new  StringBuilder();
        while(txtfile.hasNext())    //If the new line isn't null he'll add it to the stringbuilder.
            str.append(txtfile.next());
        System.out.println("The file linked to the program is: "+balance(str.toString()));//passes an string to the function.
    }
}
