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
        //Key can't be null.
        if (key == null || key.length() == 0){
            return;
        }

        //If the trie is null,initialize the root node first.
        if (this.first == null){
            this.first = new Node(key.charAt(0));
        }
        Node curr = this.first;
        Node prev = null;

        for (int i = 0;i < key.length();i++){
            char c = key.charAt(i);

            //Movement1:Find the letter c among the right siblings.
            while (curr != null && curr.symbol != c){
                prev = curr;
                curr = curr.sibling;
            }

            //Movement2:No c among right siblings,construct a new one in the end.
            if (curr == null){
                curr = new Node(c);
                prev.sibling = curr;
            }

            //Movement3:Check if we have reached the last letter of the words.
            if (i == key.length() - 1){
                curr.keyPresent = true;
                curr.value = value;
                return;
            }

            //Movement4:Not at the end of the word yet,prepare to move down to the next level.
            if (curr.child == null){
                curr.child = new Node(key.charAt(i+1));
            }
            prev = null;
            curr = curr.child;
        }
        //Worst-case time complexity: O(m),
        //where m is the length of the given key.
    }

    boolean contains(String key) {
        if (key == null || key.length() == 0){
            return false;
        }

        Node curr = this.first;

        for (int i = 0;i < key.length();i++){
            char c = key.charAt(i);

            //Movement1:Find the letter c among the right siblings.
            while (curr != null && curr.symbol != c){
                curr = curr.sibling;
            }

            //Movement2:No c among right siblings,no such word in the dictionary.
            if (curr == null){
                return false;
            }

            //Movement3:Walk to the last letter of the word.
            //Checking if the value exist.
            if (i == key.length() - 1){
                return curr.keyPresent;
            }

            //Movement4:Move down to the next level.
            curr = curr.child;
        }

        return false;
        //Worst-case time complexity: O(m),
        //where m is the length of the given key.
    }

    T get(String key) {
        if (key == null || key.length() == 0){
            return null;
        }

        Node curr = this.first;

        for (int i = 0;i < key.length();i++){
            char c = key.charAt(i);

            while (curr != null && curr.symbol != c){
                curr = curr.sibling;
            }
            
            //Although key must be contained,just to make sure.
            if (curr == null){
                return null;
            }

            //Arrived at the last letter;
            if (i == key.length() - 1){
                return curr.value;
            }

            curr = curr.child;
        }
        return null;
    }

    void remove(String key) {
        if (key == null || key.length() == 0){
            return;
        }

        this.first = removeMachine (this.first, key, 0);
        //Worst-case time complexity: O(m),
        //where m is the length of the given key.
    }

    private Node removeMachine (Node curr, String key, int i){
        //Base case:Reach a dead end directly,the key is not in the dictionary.
        if(curr == null){
            return null;
        }
        char c = key.charAt(i);

        //Step1:Top-Down
        if (curr.symbol == c){
            if (i == key.length() - 1){
                curr.keyPresent = false;
                curr.value = null;
            }else{
                curr.child = removeMachine (curr.child, key, i + 1);
            }
        }else{
            curr.sibling = removeMachine(curr.sibling, key, i);
        }

        //Step2:Bottom-Up
        //If this node does not contain a key and has no childr,
        //it is a useless node.
        if (curr.child == null && !curr.keyPresent){
            return curr.sibling;
        }

        //If the node is still useful, 
        //either holds a key or has child, keep it.
        return curr;
    }

    ArrayList<String> allKeys() {
        ArrayList<String> result = new ArrayList<>();
        StringBuilder currWord = new StringBuilder();
        collectKeys(this.first, currWord, result);
        return result;
    }

    private void collectKeys (Node curr, StringBuilder prefix, ArrayList<String> result){
        //Base case:No node,backing!
        if (curr == null){
            return;
        }

        //Movement1:Add the current node's character to our working prefix.
        prefix.append(curr.symbol);

        //Movement2:Record the key.
        //If this node marks the end of a valid word,
        //add it to the result list.
        if (curr.keyPresent){
            result.add(prefix.toString());
        }
        collectKeys(curr.child, prefix, result);

        //Movement3:Remove the current character,moving to the right sibling.
        prefix.deleteCharAt(prefix.length() - 1);
        collectKeys(curr.sibling, prefix, result);
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

