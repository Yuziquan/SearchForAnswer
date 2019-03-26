package com.wuchangi.searchforanswer.bean;

/**
 * Created by WuchangI on 2019/3/26.
 */

public class AnswerSheetResult {

    /**
     * count : 3
     * realAnswer : BDCAADACDCABDBBC
     * recognitionAnswer : CCCCCCCCCCCDCCCA
     */

    private int count;
    private String realAnswer;
    private String recognitionAnswer;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRealAnswer() {
        return realAnswer;
    }

    public void setRealAnswer(String realAnswer) {
        this.realAnswer = realAnswer;
    }

    public String getRecognitionAnswer() {
        return recognitionAnswer;
    }

    public void setRecognitionAnswer(String recognitionAnswer) {
        this.recognitionAnswer = recognitionAnswer;
    }
}
