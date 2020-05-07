package hashtagcounter;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class FiboHeap {

    /* max object of the fibonacci heap */
    private Node max = null;
    /* size of the max fibonacci heap */
    private int size = 0;

    Queue removedMaxQueue = new LinkedList();
    HashMap<String, Node> hashMap = null;

    public int getSize() {
        return size;
    }

    public  void setSize(int s) {
        this.size = s;
    }

    public void setMax(Node max) {
        this.max = max;
    }

    public Node getMax(){
        /* if the heap is not empty, returns the max element of the heap.
           else, throws a NoSuchElementException.
         */
        if (isEmpty()){
            throw new NoSuchElementException("Heap has no elements!");
        }
        return max;
    }

    public boolean isEmpty(){
        /* returns true if the Fibonacci Heap is Empty, else returns false */
        return (max == null);
    }

    private static Node combine(Node p, Node q){
        if (p == null && q == null){
            return null;
        }
        else if (p != null && q == null){
            return p;
        }
        else if (p == null && q != null){
            return q;
        }
        else{
            Node pRight = p.right;
            p.right = q.right;
            p.right.left = p;
            q.right = pRight;
            q.right.left = q;

            /* pointer to the node having larger value */
            return p.getData() > q.getData() ? p : q;
        }
    }

    public Node insert(int value, String key){
        /* create a new node */
        Node fibNode = new Node(value, key);

        /* reset Fibonacci Heap Max after insertion of new node */
        max = FiboHeap.combine(max, fibNode);

        /* increase Heap Size */
        setSize(getSize() + 1);

        return fibNode;
    }

    public FiboHeap meld(FiboHeap f, FiboHeap s){
        /* Holds the heap that is the combination of the two input heaps */
        FiboHeap result = new FiboHeap();
        result.max = combine(f.getMax(), s.getMax());
        result.size = f.getSize() + s.getSize();

        /* clean up all the old heaps */
        f.setSize(0);
        f.setMax(null);
        s.setSize(0);
        s.setMax(null);

        return result;
    }

    public Node removeMax(){
        /*
            if the heap is not empty, remove the largest value heap and return it
            else, throws NoSuchElementException.
         */
        if (isEmpty()){
            throw new NoSuchElementException("Heap has no elements!");
        }
        else{
            // reduce the heap size by 1
            setSize(getSize() - 1);

            // element to be removed from the heap
            Node maxElem = this.max;


            // reset the max value to an arbitrary node (the right sibling of the root)
            if (max.right == max){
                max =  null;
            }
            else{
                max.left.right = max.right;
                max.right.left = max.left;
                max = max.right;
            }

            // un-attach all the child nodes of the max element and add them to the top-level.
            if (maxElem.child != null){
                Node curr = maxElem.child;
                do{
                    curr.parent = null;
                    curr = curr.right;
                }while(curr != maxElem.child);
            }

            // set the new max value
            max = combine(maxElem.child, max);

            // if max is null => the list is empty
            if (max == null){
                return maxElem;
            }
            else{

                /* treeTab keeps track of the degree of the subtrees during pairwise combine*/
                List<Node> treeTab = new ArrayList<Node>();

                /*List of nodes to visit during the traversal*/
                List<Node> nodeToVisit = new ArrayList<Node>();

                // list of nodes to be visited
                for (Node curr = max; nodeToVisit.isEmpty() || nodeToVisit.get(0) != curr; curr = curr.right)
                    nodeToVisit.add(curr);

                for (Node curr : nodeToVisit) {
                    while (true) {
                        while (curr.getDegree() >= treeTab.size()){
                            treeTab.add(null);
                        }

                        /* traverse until two trees of the same degree are found*/
                        if (treeTab.get(curr.getDegree()) == null) {
                            treeTab.set(curr.getDegree(), curr);
                            break;
                        }

                        Node other = treeTab.get(curr.getDegree());
                        treeTab.set(curr.getDegree(), null); // Clear the old slot

                        Node min = (other.getData() < curr.getData()) ? other : curr;
                        Node max = (other.getData() < curr.getData()) ? curr : other;

                        /*Remove the minimum element from the list*/
                        min.right.left = min.left;
                        min.left.right = min.right;

                        /*Make the min child of max by pairwise combine*/
                        min.right = min.left = min;
                        max.child = combine(max.child, min);
                        min.parent = max;

                        min.setChildCut(false);
                        max.setDegree(max.getDegree() + 1);
                        curr = max;
                    }
                    // Update the max element
                    if (curr.getData() > max.getData()){
                        max = curr;
                    }
                }
                return maxElem;

            }

        }
    }

    private void cascadingCut(Node pNode){
        // reset the node's childCut field
        pNode.setChildCut(false);

        if(pNode.parent != null){
            // if the node has a parent node

            // remove pNode from its sibling list
            if (pNode.right != pNode){
                pNode.left.right = pNode.right;
                pNode.right.left = pNode.left;
            }

            // change the child pointer of the parent of the node being cut
            if (pNode.parent.child == pNode){
                if (pNode.right != pNode){
                    pNode.parent.child = pNode.right;
                }
                else{
                    pNode.parent.child = null;
                }

            }
            // decrease the degree of the parent of the node being cut
            pNode.parent.setDegree(pNode.parent.getDegree() - 1);

            pNode.left = pNode.right = pNode;
            pNode.setChildCut(false);
            // add the cut node to the top-level list
            max = combine(max, pNode);

            if (pNode.parent.isChildCut()){
                cascadingCut(pNode.parent);
            }
            else{
                pNode.parent.setChildCut(true);
            }

            // reset cut node parent to null
            pNode.parent = null;
        }
        else {
            return;
        }

    }

    public void increaseKey(Node target, int incBy){
        // increase the node data value by incBy
        target.setData(target.getData() + incBy);

        // if the target node has parent node, and value at target is greater than parent - perform child cut
        if (target.parent != null && target.parent.getData() < target.getData()){
            cascadingCut(target);
        }

        // if the new node added to the top-level list has larger value than previous max
        if (target.getData() > max.getData()){
            max = target;
        }

    }

    public void removeNMaxes(int n, String filename) throws Exception{
        // the function takes the number of remove max's ,and the name of the output file to be written into

        FileWriter fileWriter = new FileWriter(new File(filename), true);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (int i = 0; i <n ; i++) {
           Node removedMax = removeMax();
           printWriter.write(removedMax.getKey());
           if (i != n-1){
               printWriter.write(",");
           }
           removedMaxQueue.add(removedMax);
        };
        printWriter.println();
        printWriter.flush();
        printWriter.close();

        while(!removedMaxQueue.isEmpty()){
            Node i = (Node) removedMaxQueue.remove();
            Node res = insert(i.getData(), i.getKey());
            hashMap.get(i.getKey()).right = res;
        }
    }

    public void removeNMaxes(int n){
        // the function takes the number of items to be removed and prints them on the screen
        // remove n items from the fibonacci heap
        for (int i=0; i <n; i++){
            Node removedMax = removeMax();
            System.out.print(removedMax.getKey());
            if(i != n-1){
                System.out.print(",");
            }
            removedMaxQueue.add(removedMax);
        }
		System.out.print("\n");
        // re-insert the removed items into the fibonacci heap
        while(!removedMaxQueue.isEmpty()){
            Node i = (Node) removedMaxQueue.remove();
            Node res = insert(i.getData(), i.getKey());
            hashMap.get(i.getKey()).right = res;
        }
    }


}
