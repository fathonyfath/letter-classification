package letterclassification.model;

/**
 * Created by bradhawk on 6/1/2016.
 */
public class Classify {
    private Alphabet alphabet;
    private String classifyResult;

    public Classify(Alphabet alphabet, String classifyResult) {
        this.alphabet = alphabet;
        this.classifyResult = classifyResult;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    public String getClassifyResult() {
        return classifyResult;
    }

    public void setClassifyResult(String classifyResult) {
        this.classifyResult = classifyResult;
    }
}
