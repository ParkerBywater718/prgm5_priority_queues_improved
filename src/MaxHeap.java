import java.util.NoSuchElementException;

/**
 * Skew heap implementation of a max heap
 * @param <T> yon type parameter
 * @author Parker Bywater
 */
public class MaxHeap<T extends Comparable<T>> {

    private HeapNode<T> root;

    private static class HeapNode<T> {
        T element;
        HeapNode<T> left;
        HeapNode<T> right;

        HeapNode(T element, HeapNode<T> left, HeapNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }

        HeapNode(T element) {
            this.element = element;
        }

        @Override
        public String toString() {
            return "HeapNode<" + this.element + ", " + (this.left != null ? this.left.element : null) + ", "
                    + (this.right != null ? this.right.element : null) + ">";
        }
    }

    MaxHeap() {
        this.root = null;
    }

    public void insert(T element) {
        this.root = merge(this.root, new HeapNode<>(element));
    }

    /**
     * private method to merge two heaps
     *
     * @param h1 a heap
     * @param h2 other heap
     * @return node rooting the merged tree
     */
    private HeapNode<T> merge(HeapNode<T> h1, HeapNode<T> h2) {
        if (h1 == null)
            return h2;
        else if (h2 == null)
            return h1;
        else if (h1.element.compareTo(h2.element) < 0) {
            h2.right = merge(h2.right, h1);
            // swap
            HeapNode<T> temp = h2.left;
            h2.left = h2.right;
            h2.right = temp;
            return h2;
        } else {
            h1.right = merge(h1.right, h2);
            // swap
            HeapNode<T> temp = h1.left;
            h1.left = h1.right;
            h1.right = temp;
            return h1;
        }
    }

    /**
     * the most glorious heap operation
     * @return the value of the maximum element contained in the heap
     */
    T deleteMax() {
        if (isEmpty())
            throw new NoSuchElementException("Tried to call deleteMax() on empty heap");
        HeapNode<T> theRoot = this.root;
        this.root = merge(this.root.left, this.root.right);
        return theRoot.element;
    }

    boolean isEmpty() {
        return this.root == null;
    }

    /**
     * Empties the heap
     */
    void clear() {
        this.root = null;
    }

    /**
     * Return a string displaying the tree contents as a tree with one node per line
     * Calls the private toString() method below to do this.
     */
    @Override
    public String toString() {
        if (root == null)
            return ("Empty heap\n");

        return toString(this.root, "");
    }

    /**
     * @param startNode The node to start from
     * @return a string representing the heap
     * @author Parker Bywater
     */
    private String toString(HeapNode<T> startNode, String spaces) {
        String out = "";

        // go down the right subtree until the max element is found. recursion will take care of this
        if (startNode.right != null)
            out += toString(startNode.right, spaces + " "); //+ "[" + rootNode.element + "]\n";

        // process the current node
        out += spaces + startNode.element.toString() + "\n"; //  + "[no parent]\n";

        // go down the left subtree
        if (startNode.left != null)
            out += toString(startNode.left, spaces + " "); //+ "[" + rootNode.element + "]\n";
        return out;
    }
}
