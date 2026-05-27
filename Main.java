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
        // Precondition : Key can't be null.
        assert (key != null && key.length() != 0) : "Key can't be null";



        //If the trie(DLB tree) is null,initialize the root node first.
        if (this.first == null){
            this.first = new Node(key.charAt(0));       //we use charAt(index : 0) to get the 0 position character of the key
        }
        
        

        //If the trie(DLB tree) is not null , we need to find the position to insert the new key-value pair.
        Node curr = this.first;
        Node prev = null;           // prev here is only do for sibling movement, once we move down to the child level, we will reset prev to null, and only update it when we move to the sibling level.

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
        /* 

            SOME COMMENTS ON THE TIME COMPLEXITY OF THE "add" METHOD:

                Worst-case time complexity: O(m),
                where m is the length of the given key.

                Reason:
                
                    1. In the "for" loop , we at most go through all the characters of the key , which is O(m).
                    2  In the "while" loop , since the number of English letters is at most 26 , so it is bounded by O(26) which is O(1).
                    3. The rest of the operations in the loop are all O(1).

                    Therefore , the Worst-case time complexity is O(m) * O(1) = O(m).

        */
    }





    boolean contains(String key) {
        
        /*
            Precondition : Key can't be null.
            
            Why we need this assertion here?

                In add method , we asserted that the key cannot be null,
                So here we need this assertion.
        */
        assert (key != null && key.length() != 0) : "Your aim key can't be null";



        Node curr = this.first;

        for (int i = 0;i < key.length();i++){
            char c = key.charAt(i);

            //Movement1:Find the letter c among the right siblings.
            while (curr != null && curr.symbol != c){
                curr = curr.sibling;
            }

            //Movement2:No c among right siblings,no such word in the dictionary. return false.
            if (curr == null){
                return false;
            }


            /*
              Movement3:Walk to the last letter of the word.
              Checking if the value exist. 
            */
            if (i == key.length() - 1){     // if we have reached the last letter of the word , if the keyPresent is true , it means that we have this key in the dictionary , return true , otherwise , return false.
                return curr.keyPresent;
            }

            //Movement4:Move down to the next level.
            curr = curr.child;
        }

        return false;
        /*

            SOME COMMENTS ON THE TIME COMPLEXITY OF THE "contains" METHOD:

                Worst-case time complexity: O(m)
                where m is the length of the given key.

                Reason:

                    1. In the "for" loop , we at most go through all the characters of the key , which is O(m).
                    2  In the "while" loop , since the number of English letters is at most 26 , so it is bounded by O(26) which is O(1).
                    3. The rest of the operations in the loop are all O(1).

                    Therefore , the Worst-case time complexity is O(m) * O(1) = O(m).

        */
    }





    T get(String key) {
    
        //Precondition : The key must be contained in the dictionary.
        assert ( contains(key) == true ) : "The key must be contained in the dictionary";

        Node curr = this.first;

        for (int i = 0;i < key.length();i++){
            char c = key.charAt(i);

            while (curr.symbol != c){
                curr = curr.sibling;
            }
            
            //Arrived at the last letter;
            if (i == key.length() - 1){
                return curr.value;
            }

            //Move down to the next level.
            curr = curr.child;
        }
        return null;

        /*

            SOME COMMENTS ON THE TIME COMPLEXITY OF THE "get" METHOD:

                Worst-case time complexity: O(m)
                where m is the length of the given key.

                Reason:

                    1. In the "for" loop , we at most go through all the characters of the key , which is O(m).
                    2  In the "while" loop , since the number of English letters is at most 26 , so it is bounded by O(26) which is O(1).
                    3. The rest of the operations in the loop are all O(1).

                    Therefore , the Worst-case time complexity is O(m) * O(1) = O(m).
        
        */
    }





    void remove(String key) {
        
        //Precondition : The aim key can't be null.
        assert (key != null && key.length() != 0) : "Your aim key can't be null";

        this.first = removeMachine (this.first, key, 0);
        /*
            SOME COMMENTS ON THE TIME COMPLEXITY OF THE "remove" METHOD:
        
                Worst-case time complexity: O(m),
                where m is the length of the given key.

                Reason:
                
                    1. In the "removeMachine" method , we at most go through all the characters of the key , which is O(m).
                    2  In the "while" loop inside the "removeMachine" method, since the number of English letters is at most 26 , so it is bounded by O(26) which is O(1).
                    3. The rest of the operations in the "removeMachine" method are all O(1).

                    Therefore , the Worst-case time complexity is O(m) * O(1) = O(m).
        */
    }




    /*
        An auxiliary method for the remove method.

        This private method will delete the aim key and its value, 
        and also remove the useless nodes that are only used for this key.

        This method will return the updated node after deletion, which will be used to update the parent node's child or sibling pointer.


        About the parameters :
            1. Node curr        : the current node we are looking at in the DLB tree.
            2. String key       : the key we want to remove.
            3. int i            : the index of the character in the key that we are currently looking at.
    */
    private Node removeMachine (Node curr, String key, int i){


        //Base case:Reach a dead end directly,the key is not in the dictionary.
        if(curr == null){
            return null;
        }
        char c = key.charAt(i);


        //Step1:From Top to Down,find the key and delete it.
        if (curr.symbol == c){
            if (i == key.length() - 1){     //If we reached the last letter of the key , delete the key and its value ,set keyPresent to false, and value to null.
                curr.keyPresent = false;
                curr.value = null;
            }
            else{                           //If we have not reached the last letter of the key , do this recursively on the child node.
                curr.child = removeMachine (curr.child, key, i + 1);
            }
        }
        else{
            curr.sibling = removeMachine(curr.sibling, key, i);
        }

        
        //Step2:From Bottom to Top,remove the useless nodes
        /*
            If this node does not contain a key and has no child,
            it is a useless node.
        */
        if (curr.child == null && !curr.keyPresent){
            return curr.sibling;
        }

        /*
            Otherwise , the node is still useful, 
            either holds a key or has child, keep it.
        */
        return curr;
    }


    



    ArrayList<String> allKeys() {
        ArrayList<String> result = new ArrayList<>();

        StringBuilder currWord = new StringBuilder();

        collectKeys(this.first, currWord, result);
        return result;
    }

    /*
        An auxiliary method for the allKeys method.
        This private method will go through the whole DLB tree and collect all the keys in the result list.

        About the parameters :
            1. Node curr                : the current node we are looking at in the DLB tree.
            2. StringBuilder prefix     : the prefix we have built when we walk down the tree, which will be used to construct the key when we reach a node that marks a valid key.
            3. ArrayList<String> result : the list that we will add all the keys into. 
    */
    private void collectKeys (Node curr, StringBuilder prefix, ArrayList<String> result){
        //Base case:No node,backing!
        if (curr == null){
            return;
        }

        //Movement1:Add the current node's character to our working prefix.
        prefix.append(curr.symbol);


        //Movement2:Record the key.
        /*
            If this node marks the end of a valid word,
            add it to the result list.
        */
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




    /*
    a)
    
    Insert w1.Check prefixes and extensions.
    */
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




    /*
    b)
    
    Inserting two disjoint keys.Check prefixes and extensions.
    */
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




    /*
    c)
    
    Insert w1 and then an extension w2 .
    */
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




    /*
    d)
    
    Repeat the previous test,but insert w2 before w1 .
    */
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
    static String[] keys = {"a", "b", "aa", "ab", "ba", "bb", "aaa", "aab", "aba", "abb", "baa", "bab", "bba", "bbb"};
    



    /*
    e)
    
    lexicographic insertion
    */
    static void test5(){
        //Arrange
        DLBMap<Integer> d = new DLBMap<Integer>();

        //Act
        for (int i = 0; i < keys.length; i++){
            d.add(keys[i],i);
        }
        ArrayList<String> all = d.allKeys();

        //Assert
        //Check that the set of keys contains all the keys.
        for (int i = 0; i < keys.length; i++){
            assert d.contains(keys[i]) && d.get(keys[i]).equals(i);
        }
        assert all.size() == keys.length;
        for (String k : keys){
            assert all.contains(k);
        }
    }




    /*
    f)
    
    Overwrite all the values with new values
    */
    static void test6(){
        //Arrange
        DLBMap<Integer> d = new DLBMap<Integer>();
        for (int i = 0; i < keys.length; i++){
            d.add(keys[i],i);
        }

        //Act
        for (int i = 0; i < keys.length; i++){
            d.add(keys[i],i + 100);
        }

        //Assert
        //Re-check that they have the correct values.
        for (int i = 0; i < keys.length; i++){
            assert d.get(keys[i]).equals(i + 100);
        }
    }




    /*
    g)
    
    in reverse lexicographic order
    */
    static void test7(){
        //Arrange
        DLBMap<Integer> d = new DLBMap<Integer>();

        //Act
        for (int i = keys.length - 1; i >= 0; i--){
            d.add(keys[i],i);
        }

        //Assert
        //check that they have the correct values.
        for (int i = 0; i < keys.length; i++){
            assert d.contains(keys[i]) && d.get(keys[i]).equals(i);
        }
    }




    /*
    h)
    
    Validate that key removal is working correctly.
    */
    static void test8(){
        //Arrange
        DLBMap<Integer> d = new DLBMap<Integer>();
        d.add("bad", 1);
        d.add("baby", 2);
        d.add("cat", 3);

        //Act1
        d.remove("bad");
        //Assert1
        assert !d.contains("bad");
        assert d.contains("baby");

        //Act2
        d.remove("baby");
        //Assert2
        assert !d.contains("baby");
        assert d.contains("cat");

        //Act3
        d.remove("cat");
        //Assert3
        assert !d.contains("cat");
        assert d.allKeys().isEmpty();
    }

}

