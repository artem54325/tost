package test;

public class StringIntObj implements Comparable<StringIntObj> {
    private String word;
    private int number;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public StringIntObj() {
    }

    public StringIntObj(String text) {
        this.word = text.split(" ")[0];
        this.number=Integer.parseInt(text.split(" ")[1]);
    }

    public StringIntObj(String word, int number) {
        this.word = word;
        this.number = number;
    }

    public String get(){
        return getWord() + " " + getNumber();
    }

    @Override
    public int compareTo(StringIntObj o) {
        int result = o.getNumber()-getNumber();
        if (result!=0) return result;
        result = getWord().compareTo(o.getWord());
        return result;
    }
}
