package hashtagcounter;

public class Node {

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isChildCut() {
        return childCut;
    }

    public void setChildCut(boolean childCut) {
        this.childCut = childCut;
    }

    // Class Variables
    private int degree = 0;
    private int data;
    private String key;
    private boolean childCut = false;
    Node right, left;
    Node parent = null;
    Node child = null;

    public Node(int d, String k){
        data = d;
        key = k;
        right = left = this;
    }
}
