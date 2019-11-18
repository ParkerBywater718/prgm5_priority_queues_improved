public class SmallTests {
    public static void main(String[] args) {
        Trie trie = new Trie();
        for (int i = 32; i < 127; i++)
            System.out.println(i % 29);

//        trie.insert(new Term("rat", 45));
        trie.insert(new Term("ratism", 9001));
        trie.insert(new Term("ratty", 199));
        trie.insert(new Term("ratlike", 145));
        trie.insert(new Term("rats", 10000000));
        MaxHeap<Term> testHeap = new MaxHeap<>();
        trie.addMatchingTermsToHeap("rat", testHeap);
        System.out.println(testHeap);
    }
}
