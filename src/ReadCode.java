import java.io.File;
import java.util.Scanner;


public class ReadCode {
    public static void main(String[] args) {
        try {
            // read in the terms
            String filename = "SortedWords.txt";
            Scanner reader = new Scanner(new File(filename));
            Trie terms = new Trie();
//            Term[] termA = new Term[4351] ;
            int i = 0;
            while ((reader.hasNext())) {
                String word = reader.next();
//                System.out.println(word);
                long freq = reader.nextInt();
                terms.insert(new Term(word, freq));
//                termA[i++] = new Term(word, freq);
            }

//            testMethod(termA, terms);
            // user input stuff
            Scanner input = new Scanner(System.in);
            MaxHeap<Term> h = new MaxHeap<>();
            while (true) {
                // more user input stuff
                System.out.print("Enter a prefix followed by the number of predictions you'd like to see " +
                        "(type zoop-zop 0 to quit): ");
                String prefix = input.next();
                int numberToDisplay = input.nextInt();
                if (prefix.trim().equals("zoop-zop"))
                    break;

                // get the terms matching the prefix and start a timer
                long startTime = System.currentTimeMillis();
                terms.addMatchingTermsToHeap(prefix, h);
                if (h.isEmpty())
                    System.out.println("No predicitions available for that prefix");
                else {
                    // display the appropriate number of terms
                    int displayed = 0;
                    while (displayed++ < numberToDisplay && !h.isEmpty())
                        System.out.println("\t" + h.deleteMax());
                    System.out.printf("time to get predictions: %4d ms\n", System.currentTimeMillis() - startTime);
                    // clear the queue before the next inquiry
                    h.clear();
                }

            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * finds the terms which match a given prefix by performing a modified binary search
     *
     * @param terms  the array of terms to search (pre-sorted by compareTo())
     * @param prefix the prefix to match
     * @return a two-element array where a[0] = index of first matching term and a[1] = index of last matching term or
     * an empty array if no terms match the prefix
     */
    static int[] getMatchingTermsBounds(Term[] terms, String prefix) {
        int lo = 0;
        int hi = terms.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            String termAtMid = terms[mid].word;

            if (prefix.compareTo(termAtMid) == 0) {
                lo = mid;
                for (hi = mid; hi < terms.length - 1; ) {
                    if (terms[hi + 1].word.length() >= prefix.length() && terms[hi + 1].word.substring(0, prefix.length()).equals(prefix))
                        hi++;
                    else {
//                        hi--;
                        break;
                    }
                }
                return new int[]{lo, hi};
            } else if (prefix.compareTo(termAtMid) < 0) {
                if (termAtMid.length() >= prefix.length() && prefix.equals(termAtMid.substring(0, prefix.length()))) {
                    // search for the word having key as a prefix with the lowest index
                    lo = mid;
                    while (lo > 0 && terms[lo - 1].word.length() >= prefix.length()
                            && prefix.equals(terms[lo - 1].word.substring(0, prefix.length()))
                            && prefix.compareTo(terms[lo - 1].word) <= 0) {
                        lo--;
                    }

                    // search for the word having key as a prefix with the highest index
                    hi = mid;
                    while (hi < terms.length - 1 && terms[hi + 1].word.length() >= prefix.length()
                            && prefix.equals(terms[hi + 1].word.substring(0, prefix.length()))) {
                        hi++;
                    }
                    return new int[]{lo, hi};
                } else
                    hi = mid - 1;
            } else
                lo = mid + 1;
        }
        return new int[]{};

    }

    /**
     * A method which reads in all the terms contained in SortedWords.txt and runs the text prediction algorithm on
     * each.
     *
     * @param terms an array of Terms
     */
    static void testMethod(Term[] terms, Trie trie) {
        long startTime = System.currentTimeMillis();
        MaxHeap<Term> heap = new MaxHeap<>();
        for (Term t : terms) {
            for (int i = 1; i <= t.word.length(); i++) {
                String theQuery = t.word.substring(0, i);
                System.out.println("the query: " + theQuery);

                trie.addMatchingTermsToHeapR(theQuery, heap);
                if (!heap.isEmpty()) {
                    while (!heap.isEmpty()) {
                        System.out.println("\t" + heap.deleteMax());
                    }
                } else {
                    System.out.println("No predicitions available for that prefix");
                }
            }
        }
        System.out.println("elapsed time for all possible substrings of all possible words: " +
                (System.currentTimeMillis() - startTime) + " ms");
    }
}