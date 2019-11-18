//public class Term<AnyType implements Comparable<? super AnyType>>{
public class Term implements Comparable<Term> {

    public long freq;
    public String word;

    public Term(String word, long freq){
        this.word = word;
        this.freq = freq;
    }

    public String toString(){
        return word + "\t" + freq;
    }

    public int compareTo(Term t2){
        if (this.freq==t2.freq) return 0;
        else if (this.freq < t2.freq) return -1;
        return 1;
     }

}

