package stackdriver;
//Geneiva Ocampo
// Stack Examples discussed in class.
import java.util.Scanner;

public class StackDriver {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
         boolean exit = false;
         //stores our inputs
         String s = null;
         String currentPostfixExpression = null;


        while (!exit) {
            // Display the menu
            System.out.println("1. Enter a fully parenthesized expression: ");
            System.out.println("2. Evaluate the fully parenthesized expression.");
            System.out.println("3. Convert the fully parenthesized expression to postfix.");
            System.out.println("4. Evaluate the postfix expression.");
            System.out.println("5. Quit.");
            System.out.print("Enter your choice: ");
            String choice = in.nextLine();  // Read user input
            
           switch (choice) {
             case "1":
                    System.out.print("Enter a fully parenthesized expression: ");
                    s = in.nextLine();
                    if (isLegal(s)) {
                        System.out.println(s + " is a legal parenthesization.");
                    } else {
                        System.out.println(s + " is not a legal parenthesization.");
                        s = null;  // reset if illegal
                    }
                    break;

              case "2":
                    // Evaluate the fully parenthesized expression
                    if (s != null && isLegal(s)) {
                        evaluateExpr(s);
                    } else {
                        System.out.println("No valid fully parenthesized expression entered.");
                    }
                    break;

             case "3":
                    // Convert the expression to postfix notation
                    if (s != null) {
                        currentPostfixExpression = convertToPostfix(s);
                        System.out.println("Postfix notation: " + currentPostfixExpression);
                    } else {
                        System.out.println("No expression has been entered.");
                    }
                    break;

             case "4":
                    // Evaluate the postfix expression
                    if (currentPostfixExpression != null) {
                        System.out.print("Do you want to use the previously converted postfix expression? (yes/no): ");
                        String response = in.nextLine();
                        if (response.equalsIgnoreCase("yes")) {
                            evaluatePostfix(currentPostfixExpression);
                        } else {
                            System.out.print("Enter a new postfix expression: ");
                            String newPostfixExpression = in.nextLine();
                            evaluatePostfix(newPostfixExpression);
                        }
                    } else {
                        System.out.println("No postfix expression to evaluate.");
                    }
                    break;
 
            case "5":
                // Exit the program
                exit = true;
                System.out.println("Exiting...");

                break;
 
            default:
                // Handle invalid input
                System.out.println("Invalid choice, please try again.");
                break;
            }
            
        }
        in.close();

    }

    // Go through the string and:
    // Every time it sees a '(', it pushes it on the stack, and it will only pop a '('
    // off the stack when it encounters a ')'.
    public static boolean isLegal(String str) {
        ListStack<Character> s = new ListStack<Character>();

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                s.push('(');
            } else if (str.charAt(i) == ')') {
                s.pop();
            }
        }
        return s.isEmpty();
    }

    // Evaluate the expression, but the expression must be fully parenthesized.
    // In other words, it will not evaluate 9 + 5, but it will evaluate (9 + 5).
    // Also, it will not evaluate (8 * 4) + 7, but will evaluate ((8 * 4) + 7).
    //
    // Go through the string and:
    // - If you see a number, push it on stack s1.
    // - If you see an operator, push it on stack s2.
    // - If you see a ')', pop operand2 and operand1 from stack s1 (in this order),
    //   pop the operator from stack s2, and apply the operator to the operands.
    //   Push the result back on stack s1.
    // Finally, at the end, print the result and pop it off of stack s1.
    public static void evaluateExpr(String str) {
        ListStack<Integer> s1 = new ListStack<Integer>();
        ListStack<Character> s2 = new ListStack<Character>();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (Character.isDigit(ch)) {
                s1.push(ch - '0'); // Convert char digit to integer
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%') {
                s2.push(ch);
            } else if (ch == ')') {
                int opnd1, opnd2;
                char oper = s2.topAndPop();
                opnd2 = s1.topAndPop();
                opnd1 = s1.topAndPop();

                switch (oper) {
                    case '+':
                        s1.push(opnd1 + opnd2);
                        break;
                    case '-':
                        s1.push(opnd1 - opnd2);
                        break;
                    case '*':
                        s1.push(opnd1 * opnd2);
                        break;
                    case '/':
                        s1.push(opnd1 / opnd2);
                        break;
                    case '%':
                        s1.push(opnd1 % opnd2);
                        break;
                }
            }
        }
        System.out.println("The result of the expression is: " + s1.topAndPop());
    }
        // Method to convert an infix expression to postfix notation
    public static String convertToPostfix(String str) {
        // Assume that the expression is legal and fully parenthesized
        StringBuilder postfix = new StringBuilder();
        ListStack<Character> s = new ListStack<>();
        
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            
            if (Character.isDigit(ch)) {
                postfix.append(ch).append(' ');
            } 
            else if (ch == '(') {
                s.push(ch);
            } 
            else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%') {
                s.push(ch);
            } 
            else if (ch == ')') {
                while (!s.isEmpty() && s.top() != '(') {
                    postfix.append(s.topAndPop()).append(' ');
                }
                s.pop(); // Remove the '(' from the stack
            }
        }
        
        return postfix.toString().trim();
    }
    // Method to evaluate a postfix expression
public static void evaluatePostfix(String str) {
    ListStack<Integer> s1 = new ListStack<Integer>();  // Only one stack is needed for operands

    for (int i = 0; i < str.length(); i++) {
        char ch = str.charAt(i);

        if (Character.isDigit(ch)) {
            // Push the operand (digit) onto the stack
            s1.push(ch - '0'); // Convert char digit to integer
        } 
        else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%') {
            // Pop two operands from the stack
            int opnd2 = s1.topAndPop();  // Second operand
            int opnd1 = s1.topAndPop();  // First operand

            // Perform the operation based on the operator
            switch (ch) {
                case '+':
                    s1.push(opnd1 + opnd2);  // Push the result back onto the stack
                    break;
                case '-':
                    s1.push(opnd1 - opnd2);
                    break;
                case '*':
                    s1.push(opnd1 * opnd2);
                    break;
                case '/':
                    s1.push(opnd1 / opnd2);
                    break;
                case '%':
                    s1.push(opnd1 % opnd2);
                    break;
                default:
                    System.out.println("Unknown operator: " + ch);
            }
        }
    }

    // The result should be the last remaining value on the stack
    if (!s1.isEmpty()) {
        System.out.println("The result of the postfix expression is: " + s1.topAndPop());
    } else {
        System.out.println("Invalid postfix expression.");
    }
}

    }



