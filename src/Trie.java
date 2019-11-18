public class Trie {
    private TrieNode root;

    private static class TrieNode {
        char storedChar;
        HashMapASCII map;
        Term term;

        TrieNode(char c, Term t) {
            storedChar = c;
            term = t;
            map = new HashMapASCII();
        }

        @Override
        public String toString() {
            return "" + storedChar + " with " + map.mapSize + " children";
        }
    }

    private static class HashMapASCII {
        int ASCII_MIN = 32;
        int mapCapacity = 95;
        int mapSize;
        TrieNode[] theArray;

        HashMapASCII() {
            theArray = new TrieNode[mapCapacity];
            for (int i = 0; i < 26; i++)
                theArray[i] = null;
            mapSize = 0;
        }

        /**
         * adds a new TrieNode to the map
         *
         * @param c  the character, which behaves like a key
         * @param tn the node to add
         */
        void insert(char c, TrieNode tn) {
            theArray[c - ASCII_MIN] = tn;
            mapSize++;
        }

        boolean contains(char c) {
            return theArray[c - ASCII_MIN] != null;
        }

        TrieNode get(char c) {
            return theArray[c - ASCII_MIN];
        }

        TrieNode[] getAllChildren() {
            TrieNode[] out = new TrieNode[mapSize];
            int outIdx = 0;
            for (int i = 0; i < theArray.length; i++) {
                if (theArray[i] != null)
                    out[outIdx++] = theArray[i];
            }
            return out;
        }
    }

    Trie() {
        root = new TrieNode((char) 0, null);
    }

    public void insert(Term t) {
        insert(root, t, 0);
    }

    private void insert(TrieNode tn, Term t, int charIdx) {
        char theChar = t.word.charAt(charIdx);
        if (tn.map.contains(theChar) && charIdx < t.word.length() - 1)
            insert(tn.map.get(theChar), t, ++charIdx);
        else {
            TrieNode newTrieNode = new TrieNode(theChar, charIdx == t.word.length() - 1 ? t : null);
            tn.map.insert(theChar, newTrieNode);
            if (charIdx < t.word.length() - 1)
                insert(newTrieNode, t, ++charIdx);
        }
    }

    public void addMatchingTermsToHeapR(String prefix, MaxHeap<Term> h) {
        addMatchingTermsToHeapR(prefix, h, root, 0);
    }

    private void addMatchingTermsToHeapR(String prefix, MaxHeap<Term> h, TrieNode tn, int charIdx) {
        if (charIdx < prefix.length()) {
            char prefixChar = prefix.charAt(charIdx);
            if (tn.map.contains(prefixChar)) {
                // if the char stored in tn ends a word, add it to the heap. tn.term != null ==> char ends word
                addMatchingTermsToHeapR(prefix, h, tn.map.get(prefixChar), charIdx + 1);
                // go on to the next node in the word chain
            }
        } else {
            if (tn.term != null) // && charIdx == prefix.length() - 1)
                h.insert(tn.term);
            addTermsFromNodeDown(h, tn);
//            TrieNode[] nextChildren = tn.map.getAllChildren();
//            System.out.println(Arrays.toString(nextChildren));
//            for (TrieNode node :
//                    nextChildren) {
//                addTermsFromNodeDown(h, tn);
//            }
        }
    }

    public void addMatchingTermsToHeap(String prefix, MaxHeap<Term> h) {
        addMatchingTermsToHeap(prefix, h, root, 0);
    }

    public void addMatchingTermsToHeap(String prefix, MaxHeap<Term> h, TrieNode tn, int charIdx) {
        if (prefix.length() == 0)
            return;
        char searchChar = prefix.charAt(charIdx);
        TrieNode currNode = root;
        TrieNode nextNode = root.map.get(searchChar);
        while (++charIdx < prefix.length() && nextNode != null) {
            currNode = nextNode;
            searchChar = prefix.charAt(charIdx);
            nextNode = nextNode.map.get(searchChar);
        }
        // if nextNode == null there are none of this prefix
        if (nextNode != null) {
            if (nextNode.term != null)
                h.insert(nextNode.term);
            addTermsFromNodeDown(h, nextNode);
        }
    }

    private void addTermsFromNodeDown(MaxHeap<Term> h, TrieNode tn) {
        TrieNode[] nextChildren = tn.map.getAllChildren();
        for (TrieNode child : nextChildren) {
            if (child.term != null)
                h.insert(child.term);
            addTermsFromNodeDown(h, child);
        }
    }
}
