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

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
}
