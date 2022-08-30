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
        if (args.length < 1)
        {
            System.out.println("File path not provided as argument");
            System.exit(0);
        }
        else if (args.length > 1)
        {
            System.out.println("WARNING: Unused arguments");
        }
        File program = new File(args[0]);
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
                    while (output.indexOf("\"") == -1)
                    {
                        String line = scanner.nextLine();
                        output += line + "\n";
                    
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
     * 
     * Example: * + 5 3 2 (which is (5 + 3) * 2 = 16)
     * After parsing, expression stack: [* + 5 3 1] <-- top
     * 
     * Stack views after each loop of evaluation:
     * iteration 1)
     *      expression: [* + 5 3]
     *      evaluation: [2]
     * iteration 2)
     *      expression: [* + 5]
     *      evaluation: [2 3]
     * iteration 2)
     *      expression: [* +]
     *      evaluation: [2 3 5]
     * iteration 2)
     *      expression: [*]
     *      evaluation: [2 8]
     * iteration 2)
     *      expression: []
     *      evaluation: [16] <-- result
     */
    public static int evaluateExpression(Scanner scanner) {
        Stack<String> expressionStack = new Stack<String>();
        Stack<Integer> evaluationStack = new Stack<Integer>();

        //
        // Parse the expression and store it in a stack since we will be 
        // evaluating the expression from back to front
        //
        int numLeft = 1;
        while (numLeft != 0) {
            String token = scanner.next();
            expressionStack.push(token);
            if (token.matches("\\d+") || symbolTable.containsKey(token))
            {
                numLeft--;
            }
            else if (!token.matches("[!~]"))
            {
                numLeft++;
            }
        }

        // Iterate until we have gone over the entire expression
        while (!expressionStack.isEmpty()) {
            String token = expressionStack.pop();
            if (token.matches("\\d+"))
            {
                // If the token is an integer, push it on the evaluation stack
                evaluationStack.push(Integer.parseInt(token));
            }
            else if (symbolTable.containsKey(token)) {
                // If the token is a variable, push its associated value on the evaluation stack
                evaluationStack.push(symbolTable.get(token));
            }
            else {
                // Token is an operator, handle the case of unary vs. binary operator
                if (token.matches("[!~]"))
                {
                    // Unary operator, pop operand from evaluation stack
                    int operand = evaluationStack.pop();

                    // Perform operation and push result onto evaluation stack
                    if (token.equals("!")) {
                        evaluationStack.push((operand == 0) ? 1 : 0);
                    }
                    else if (token.equals("~")) {
                        evaluationStack.push(operand * -1);
                    }
                }
                else
                {
                    // Binary operator, pop two operands from evaluation stack
                    int operand1 = evaluationStack.pop();
                    int operand2 = evaluationStack.pop();

                    // Perform operation and push result onto evaluation stack
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
                        evaluationStack.push((operand1 != 0 && operand2 != 0) ? 1 : 0);
                    }
                    else if (token.equals("||")) {
                        evaluationStack.push((operand1 != 0 || operand2 != 0) ? 1 : 0);
                    }
                    else if (token.equals(">")) {
                        evaluationStack.push((operand1 > operand2) ? 1 : 0);
                    }
                    else if (token.equals("<")) {
                        evaluationStack.push((operand1 < operand2) ? 1 : 0);
                    }
                    else if (token.equals("==")) {
                        evaluationStack.push((operand1 == operand2) ? 1 : 0);
                    }
                    else if (token.equals("!=")) {
                        evaluationStack.push((operand1 != operand2) ? 1 : 0);
                    }
                    else if (token.equals("<=")) {
                        evaluationStack.push((operand1 <= operand2) ? 1 : 0);
                    }
                    else if (token.equals(">=")) {
                        evaluationStack.push((operand1 >= operand2) ? 1 : 0);
                    }
                }
            }
        }

        // Final result is a single value on the evaluation stack
        return(evaluationStack.pop());
    }
}