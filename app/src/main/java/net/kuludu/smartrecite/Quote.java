package net.kuludu.smartrecite;

public class Quote {
    private Integer index;
    private String english;
    private String chinese;

    public Quote(Integer index, String english, String chinese) {
        this.index = index;
        this.english = english;
        this.chinese = chinese;
    }

    public Integer getIndex() {
        return index;
    }

    public String getEnglish() {
        return english;
    }

    public String getChinese() {
        return chinese;
    }
}
