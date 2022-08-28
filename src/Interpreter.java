import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Stack;

public class Interpreter
{

    static HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();

    public static void main(String[] args) throws FileNotFoundException
    {
        File program = new File("src/basic_functionality.txt");
        Scanner scanner = new Scanner(program);
        while (scanner.hasNext())
        {
            String statement = scanner.next();
            if (statement.equals("//") || statement.equals(""))
            {
                // Comment or empty line, so skip it
                scanner.nextLine();
            }
            else if (statement.equals("text"))
            {
                String first = scanner.next();
                if (first.charAt(0) == '\"')
                {
                    String output = first.substring(1, first.length());
                    String line = scanner.nextLine();
                    while (output.indexOf("\"") == -1)
                    {
                        output += line;
                        line = scanner.nextLine();
                    
                    }
                    output = output.substring(0, output.indexOf("\""));
                    System.out.println(output);
                }
                else
                {
                    System.out.println(first);
                    scanner.nextLine();
                }
                
            }
            else if (statement.equals("output"))
            {
                System.out.println(evaluateExpression(scanner));
            }
            else if (statement.equals("var"))
            {
                String varName = scanner.next();
                int value = evaluateExpression(scanner);
                if (symbolTable.containsKey(varName)) {
                    System.out.println("variable " + varName + " incorrectly re-initialized");
                }
                symbolTable.put(varName, value);
            }
            else if (statement.equals("set"))
            {
                String varName = scanner.next();
                int value = evaluateExpression(scanner);
                if (!symbolTable.containsKey(varName)) {
                    System.out.println("variable " + varName + " not declared");
                }
                symbolTable.put(varName, value);
            }
            else
            {
                System.out.println("INVALID STATEMENT");
                System.exit(0);
            }
        }
        scanner.close();
    }

    /*
     * Provided an input scanner that points to the beginning of an expression
     * in prefix notation, this function returns the result of the evaluation
     * of that expression.
     */
    public static int evaluateExpression(Scanner scanner) {
        Stack<String> expression = new Stack<String>();
        Stack<Integer> evaluationStack = new Stack<Integer>();

        int numLeft = 1;
        while (numLeft != 0) {
            String token = scanner.next();
            expression.push(token);
            if (token.matches("\\d+") || symbolTable.containsKey(token))
            {
                numLeft--;
            }
            else if (!token.matches("[!~]"))
            {
                numLeft++;
            }
        }

        while (!expression.isEmpty()) {
            String token = expression.pop();
            if (token.matches("\\d+"))
            {
                evaluationStack.push(Integer.parseInt(token));
            }
            else if (symbolTable.containsKey(token)) {
                evaluationStack.push(symbolTable.get(token));
            }
            else {
                if (token.matches("[!~]"))
                {
                    int operand = evaluationStack.pop();
                    if (token.equals("!")) {
                        evaluationStack.push(operand == 0 ? 1 : 0);
                    }
                    else if (token.equals("~")) {
                        evaluationStack.push(operand * -1);
                    }
                }
                else
                {
                    int operand1 = evaluationStack.pop();
                    int operand2 = evaluationStack.pop();
                    if (token.equals("+")) {
                        evaluationStack.push(operand1 + operand2);
                    }
                    else if (token.equals("-")) {
                        evaluationStack.push(operand1 - operand2);
                    }
                    else if (token.equals("*")) {
                        evaluationStack.push(operand1 * operand2);
                    }
                    else if (token.equals("/")) {
                        evaluationStack.push(operand1 / operand2);
                    }
                    else if (token.equals("%")) {
                        evaluationStack.push(operand1 % operand2);
                    }
                    else if (token.equals("&&")) {
                        if (operand1 != 0 && operand2 != 0) {
                            evaluationStack.push(1);
                        }
                        else {
                            evaluationStack.push(0);
                        }
                    }
                    else if (token.equals("||")) {
                        if (operand1 != 0 || operand2 != 0) {
                            evaluationStack.push(1);
                        }
                        else {
                            evaluationStack.push(0);
                        }
                    }
                    else if (token.equals(">")) {
                        if (operand1 > operand2) {
                            evaluationStack.push(1);
                        }
                        else {
                            evaluationStack.push(0);
                        }
                    }
                    else if (token.equals("<")) {
                        if (operand1 < operand2) {
                            evaluationStack.push(1);
                        }
                        else {
                            evaluationStack.push(0);
                        }
                    }
                    else if (token.equals("==")) {
                        if (operand1 == operand2) {
                            evaluationStack.push(1);
                        }
                        else {
                            evaluationStack.push(0);
                        }
                    }
                    else if (token.equals("!=")) {
                        if (operand1 != operand2) {
                            evaluationStack.push(1);
                        }
                        else {
                            evaluationStack.push(0);
                        }
                    }
                    else if (token.equals("<=")) {
                        if (operand1 <= operand2) {
                            evaluationStack.push(1);
                        }
                        else {
                            evaluationStack.push(0);
                        }
                    }
                    else if (token.equals(">=")) {
                        if (operand1 >= operand2) {
                            evaluationStack.push(1);
                        }
                        else {
                            evaluationStack.push(0);
                        }
                    }
                }
            }
        }
        return(evaluationStack.pop());
    }
}