public class Node {
    String data;
    Node parent;
    Node leftChild;
    Node rightChild;


    public Node(String data) {
        this.data = data;
    }
    public Node(String data, Node parent) {
        this.data = data;
        this.parent = parent;
    }

    public void setLeftChild(Node child) {
        this.leftChild = child;
    }

    public void setRightChild(Node child) {
        this.rightChild = child;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean hasLeftChild() {
        return leftChild != null;
    }
    public boolean hasRightChild() {
        return rightChild != null;
    }

    public boolean isBinary() {
        return this.data.equals(">") || this.data.equals("|") || this.data.equals("&");
    }
    public boolean isUnary() {
        return this.data.equals("!");
    }



}
