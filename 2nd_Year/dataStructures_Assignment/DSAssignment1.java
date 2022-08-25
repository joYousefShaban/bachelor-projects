package ds.assignment;
import java.util.*;

public class DSAssignment1 
{
    public static void calculator(String infix)
    {
        //The following statement insures a balanced brackets input.
        int bracket=0;
        for(int i=0;i<infix.length();i++)
        {
            if(infix.charAt(i)=='(')
                bracket++;
            else if(infix.charAt(i)==')')
                bracket--;
        }
        boolean match=true;
        if(bracket != 0)
        {
            System.out.println("the brackets aren't a match");
            match=false;
        }
        if(match)
        {
            //PART 1-Infix to Postfix.
            Stack stk=new Stack();
            stk.push("");
            String postfix="";
            for(int i=0;i<infix.length();i++)   
            {
               char temp=infix.charAt(i);
               if(temp == '(')
                   stk.push(temp);
               else if(temp <= '9' && temp >= '0')//The following process allows the user to enter multiple digits
                {
                   postfix=(postfix+temp);
                   while(i+1!= infix.length() && infix.charAt(i+1) >= '0'  && infix.charAt(i+1) <= '9')
                   {
                       postfix=(postfix+infix.charAt(i+1));
                       i++;
                   }
                   if(infix.length()!=1)
                   postfix=(postfix+" ");
                }
               else if(temp=='-' || temp=='+')
               {
                   //If the stack had higher or equal precedence it will get popped
                   while(stk.peek().equals('*') || stk.peek().equals('/') || stk.peek().equals('+') || stk.peek().equals('-'))
                       postfix=(postfix+stk.pop()+" ");
                   stk.push(temp);
               }
               else if(temp=='*' || temp=='/')
               {
                   //If the stack had equal precedence it will get popped
                   if(stk.peek().equals('*') || stk.peek().equals('/'))
                       postfix=(postfix+stk.pop()+" ");
                   stk.push(temp);
               }
               else if(temp== ')') //return all values inside the brackets
               {
                   while(!stk.peek().equals('('))
                       postfix=(postfix+stk.pop()+" ");
                   stk.pop();
               }
               if(i==infix.length()-1 && infix.length()!=1) //return all values that were left in the stack
               {
                   while(!stk.empty() && stk.peek()!="")
                       postfix=(postfix+stk.pop()+" ");
               }
            }
            //Part 2-Postfix To an Decimal answer
            for(int i=0;i<postfix.length();i++)
            {
                char character=postfix.charAt(i);
                if((character=='+' || character=='-' || character=='*' || character=='/') && postfix.charAt(i+1)==' ')
                {
                    int first,range1=0;
                    int second,range2=0;
                    for(int j=i-1;postfix.charAt(--j)!=' ';)//second number digits counter.
                        range2++;

                    if((i-2)-range2==1)
                        range1++;
                    for(int j=((i-2)-range2);postfix.charAt(--j)!=' ' && j!=0;)//first number digits counter.
                    {
                        range1++;
                        if(j==1)
                            range1++;
                    }

                    first=Integer.parseInt(postfix.substring(i-2-range1-range2,i-range2-2));//String to integer convertion.
                    second=Integer.parseInt(postfix.substring(i-range2-1,i-1));//String to integer convertion.

                    int answer;
                    if(character=='+')
                        answer=first+second;
                    else if (character=='-')
                        answer=first-second;
                    else if(character=='*')
                        answer=first*second;
                    else 
                        answer=first/second;

                    postfix=postfix.replace(postfix.substring((((i-2)-range1)-range2),i+1),Integer.toString(answer));//replacing the equation to the answer of it "in the same string".
                    i=(((i-2)-range1)-range2);//resetting the range of the loop in order to prevent any loss of characters.
                }
            }
            System.out.println("The answer of the previous equation is: "+postfix.trim()); //We used the function "trim" to insure that the program is working with a string without spaces.
        }
    }
    
    public static void main(String[] args)
    {
        //Q1
        System.out.println("Please enter the Infix format of the equation:");
        Scanner uInput=new Scanner (System.in); //uInput stands for User Input.
        String infix=uInput.next();
        calculator(infix);
    }
}