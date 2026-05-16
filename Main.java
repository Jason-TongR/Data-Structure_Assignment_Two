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
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
        test8();
        System.out.println("All tests passed.");
    }

    static void test0() {
        DLBMap<Integer> d = new DLBMap<Integer>();
        assert !d.contains("hi");
        assert !d.contains("hello");
        d.add("hi", 1);
        d.add("hello", 2);
        assert d.contains("hi");
        assert d.contains("hello");
        assert d.get("hi").equals(1);
        assert d.get("hello").equals(2);
    }

    //a)Insert w1.Check prefixes and extensions.
    static void test1() {
        //Arrange
        DLBMap<Integer> d = new DLBMap<Integer>();
        String w1 = "hello";
        String w2 = "hellogt";

        //Act
        d.add(w1, 3);

        //Assert
        //1.Check w1 is in the map with the correct value.
        assert d.contains(w1);
        assert d.get(w1).equals(3);
        //2.Check that none of its prefixes are in the map.
        assert !d.contains("h");
        assert !d.contains("he");
        assert !d.contains("hel");
        assert !d.contains("hell");
        //3.Check that none of the extensions of w1 
        //which are prefixes of w2 are in the map
        assert !d.contains(w2);
    }

    //b)Inserting two disjoint keys.Check prefixes and extensions.
    static void test2(){
        //Arrange
        DLBMap<Integer> d = new DLBMap<Integer>();
        String w1 = "Nube";
        String w2 = "Nubies";
        String w3 = "Nubes";

        //Act
        d.add(w1, 17);
        d.add(w2, 21);

        //Assert
        //1.Check the keys are in the map with the correct value.
        assert d.contains(w1) && d.get(w1).equals(17);
        assert d.contains(w2) && d.get(w2).equals(21);
        //2.Check that none of their prefixes are in the map.
        assert !d.contains("N");
        assert !d.contains("Nu");
        assert !d.contains("Nub");
        assert !d.contains("Nubi");
        assert !d.contains("Nubie");
        //3.Check that none of the extensions of w1 
        //which are prefixes of w3 are in the map
        assert !d.contains(w3);
    }

    //c)Insert w1 and then an extension w2 .
    static void test3(){
        //Arrange
        DLBMap<Integer> d = new DLBMap<Integer>();
        String w1 = "he";
        String w2 = "hello";

        //Act
        d.add(w1, 1);
        d.add(w2, 2);

        //Assert
        //1.Check that keys are in the map with the correct value.
        assert d.contains(w1) && d.get(w1).equals(1);
        assert d.contains(w2) && d.get(w2).equals(2);
        
        //2.Check that none of the extensions of w1 
        //which are prefixes of w2 are in the map.
        assert !d.contains("hel");
        assert !d.contains("hell");
    }

    //d)Repeat the previous test,but insert w2 before w1 .
    static void test4(){
        //Arrange
        DLBMap<Integer> d = new DLBMap<Integer>();
        String w1 = "hello";
        String w2 = "he";

        //Act
        d.add(w1, 2);
        d.add(w2, 1);

        //Assert
        //1.Check that keys are in the map with the correct value.
        assert d.contains(w1) && d.get(w1).equals(2);
        assert d.contains(w2) && d.get(w2).equals(1);
        
        //2.Check that none of the extensions of w1 
        //which are prefixes of w2 are in the map.
        assert !d.contains("hel");
        assert !d.contains("hell");
    }

    //List of keys for tests 5/6/7
    static String[] Keys = {"a", "b", "aa", "ab", "ba", "bb", "aaa", "aab", "aba", "abb", "baa", "bab", "bba", "bbb"}
    
    //e)lexicographic insertion
    static void test5(){
        
    }

    //f)
    static void test6(){

    }

    //g)
    static void test7(){

    }

    //h)
    static void test8(){

    }

}

