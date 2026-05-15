import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        DLBMapTest test = new DLBMapTest();
        test.runAllTests();
    }

}

class DLBMap<T> {

    /* Implementation */

    DLBMap() {
        this.first = null;
    }

    void add(String key, T value) {
        //If the trie is null,initialize the root node first.
        if (first == null){
            first = new Node(key.charAt(0));
        }
        Node 
        /* COMPLETE */
    }

    boolean contains(String key) {
        /* COMPLETE */
        return false;
    }

    T get(String key) {
        /* COMPLETE */
        return null;
    }

    void remove(String key) {
        /* COMPLETE */
    }

    ArrayList<String> allKeys() {
        /* COMPLETE */
        return null;
    }

    /* Representation */

    private class Node {
        boolean keyPresent;
        T value;
        char symbol;
        Node sibling;  // Right sibling.
        Node child;    // First child.

        Node(char symbol) {
            this.keyPresent = false;
            this.value = null;
            this.symbol = symbol;
            this.sibling = null;
            this.child = null;
        }
    }
    private Node first;
}

class DLBMapTest {

    static void runAllTests() {
        test0();
        test1();
        // TODO: add more tests
    }

    static void test0() {
        DLBMap d = new DLBMap<Integer>();
        assert !d.contains("hi");
        assert !d.contains("hello");
        d.add("hi", 1);
        d.add("hello", 2);
        assert d.contains("hi");
        assert d.contains("hello");
        assert d.get("hi").equals(1);
        assert d.get("hello").equals(2);
    }

    static void test1() {
        /* COMPLETE */
    }

    /* add more tests */
}

