import java.util.*;
import java.util.regex.Pattern;

// ! - 4; & - 3; | - 2; > - 1.

public class Main {
    public static void main(String[] args) {
        //Pattern regEx = Pattern.compile("[a-zA-Z’0-9]");
        Deque<Character> stack = new ArrayDeque<>();
        Deque<String>  newLine = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        line = line.replaceAll("\\s+","");
        int strLen = line.length();
        int pointer = 0;
        while (pointer < strLen) {
            char tmpChar = line.charAt(pointer);
            if (tmpChar == '!' || tmpChar == '(') {
                stack.push(tmpChar);
            }
            // TODO здесь скорее всего такая же багулина к и с |
            else if  (tmpChar == '&') {
                if (!stack.isEmpty()) {
                    char stackChar = stack.pop();
                    if (stackChar == '!' || stackChar == '&' || stackChar != '(') {
                        while (stackChar != '>' && stackChar != '(' && !stack.isEmpty() && stackChar != '|') {
                            newLine.add(String.valueOf(stackChar));
                            stackChar = stack.pop();
                        }
                        if (stackChar != '>' && stackChar != '(' && stack.isEmpty() && stackChar != '|')
                            newLine.add(String.valueOf(stackChar));
                        if (stackChar == '>' || stackChar == '(' || stackChar == '|')
                            stack.push(stackChar);
                    } else {
                        stack.push(stackChar);
                    }
                }
                stack.push(tmpChar);
            }
            else if (tmpChar == ')') {
                char stackChar = ' ';
                while (true) {
                    stackChar = stack.pop();
                    if (stackChar == '(') {
                        break;
                    }
                    newLine.add(String.valueOf(stackChar));
                }
            }
            else if (tmpChar == '-') {
                pointer++;
                continue;
            }
            else if (tmpChar == '>') {
                if (!stack.isEmpty()) {
                    char stackChar = stack.pop();
                    if (stackChar == '>' || stackChar == '(') {
                        stack.push(stackChar);
                    }
                    else {
                        while (stackChar != '>' && stackChar != '(' && !stack.isEmpty()){
                            newLine.add(String.valueOf(stackChar));
                            stackChar = stack.pop();
                        }
                        if (stack.isEmpty() && stackChar != '>' && stackChar != '(')
                            newLine.add(String.valueOf(stackChar));
                        if (stackChar == '>' || stackChar == '(')
                            stack.push(stackChar);
                    }
                }
                stack.push(tmpChar);
            }

            else if (tmpChar == '|') {
                if (!stack.isEmpty()) {
                    char stackChar = stack.pop();
                    if (stackChar != '>' && stackChar != '(') {
                        while (stackChar != '>' && stackChar != '(' && !stack.isEmpty()) {
                            newLine.add(String.valueOf(stackChar));
                            stackChar = stack.pop();
                        }
                        if (stack.isEmpty() && stackChar != '>' && stackChar != '(')
                            newLine.add(String.valueOf(stackChar));
                        if (stackChar == '>' || stackChar == '(')
                            stack.push(stackChar);
                    }
                    else {
                        stack.push(stackChar);
                    }
                }
                stack.push(tmpChar);
            }
            else {
                StringBuilder tempString = new StringBuilder();
                tempString.append(line.charAt(pointer));
                while (pointer + 1 != line.length() && isVariable(line.charAt(pointer + 1))) {
                    if (line.charAt(pointer + 1) != ' ' && line.charAt(pointer + 1) != '\t') {
                        tempString.append(line.charAt(pointer + 1));
                    }
                    pointer++;
                }
                newLine.add(tempString.toString());
            }
            pointer++;
        }
        // Вытаскиваю оставшиеся элементы в стеке
        while (!stack.isEmpty()) {
            char tmp = stack.pop();
            newLine.add(String.valueOf(tmp));
        }


        int listLen = newLine.size();
        Node currentNode = null;
        Node rootNode = null;
        for (int i = listLen; i > 0; i--) {
            if (i == listLen) {
                rootNode = new Node(newLine.pollLast());
                currentNode = rootNode;
                continue;
            }
            while (true) {
                if (currentNode.isBinary()) {
                    if (!currentNode.hasRightChild()) {
                        Node childNode = new Node(newLine.pollLast(), currentNode);
                        currentNode.setRightChild(childNode);
                        currentNode = childNode;
                        break;
                    } else if (!currentNode.hasLeftChild()) {
                        Node childNode = new Node(newLine.pollLast(), currentNode);
                        currentNode.setLeftChild(childNode);
                        currentNode = childNode;
                        break;
                    } else {
                        currentNode = currentNode.parent;
                    }
                }
                else if (currentNode.isUnary()) {
                    if (!currentNode.hasRightChild()) {
                        Node childNode = new Node(newLine.pollLast(), currentNode);
                        currentNode.setRightChild(childNode);
                        currentNode = childNode;
                        break;
                    } else {
                        currentNode = currentNode.parent;
                        continue;
                    }
                }
                else {
                    currentNode = currentNode.parent;
                    continue;
                }
            }
        }

        System.out.println(printTree(rootNode));
    }

    static public String printTree(Node rootNode) {
        if (rootNode.isBinary()) {
            if (rootNode.data.equals(">")) {
                return "(-" + rootNode.data + "," + printTree(rootNode.leftChild) + "," + printTree(rootNode.rightChild) + ")";
            } else {
                return "(" + rootNode.data + "," + printTree(rootNode.leftChild) + "," + printTree(rootNode.rightChild) + ")";
            }
        }
        else if (rootNode.isUnary()) {
            return "(" + rootNode.data + printTree(rootNode.rightChild) + ")";
        }
        else {
            return rootNode.data;
        }
    }
    public static long mem() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    private static Boolean isVariable(char c) {
        return c != '!' && c != '&' && c != '|' && c != '-' && c != '(' && c != ')';
    }
}