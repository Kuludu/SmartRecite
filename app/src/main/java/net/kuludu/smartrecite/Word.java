package net.kuludu.smartrecite;

public class Word {
    private int index;
    private String word;
    private String soundmark;
    private String chinese;
    private int last_review;

    public Word(int index, String word, String soundmark, String chinese, int last_review) {
        this.index = index;
        this.word = word;
        this.soundmark = soundmark;
        this.chinese = chinese;
        this.last_review = last_review;
    }

    public int getIndex() {
        return index;
    }

    public String getWord() {
        return word;
    }

    public String getSoundmark() {
        return soundmark;
    }

    public String getChinese() {
        return chinese;
    }

    public int getLast_review() {
        return last_review;
    }
}
