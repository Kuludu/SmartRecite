package net.kuludu.smartrecite;

public class Word {
    private Integer index;
    private String word;
    private String soundmark;
    private String chinese;
    private Integer last_review;

    public Word(Integer index, String word, String soundmark, String chinese, Integer last_review) {
        this.index = index;
        this.word = word;
        this.soundmark = soundmark;
        this.chinese = chinese;
        this.last_review = last_review;
    }

    public Integer getIndex() {
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

    public Integer getLast_review() {
        return last_review;
    }

    public void setLast_review(Integer last_review) {
        this.last_review = last_review;
    }
}
