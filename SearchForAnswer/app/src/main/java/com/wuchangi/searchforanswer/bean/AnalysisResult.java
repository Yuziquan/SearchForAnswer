package com.wuchangi.searchforanswer.bean;

/**
 * Created by WuchangI on 2019/3/22.
 */

public class AnalysisResult {

    /**
     * analysis : distinguish right from wrong 的意思是 分清是非 ，是固定搭配。所以 B 是正确答 案。 A 的意思是 感觉、觉察 ，C 的意思是 看到、发现 ，D 的意思是 观察 均不符合题意。
     * answer : B
     * optionA : perceive
     * optionB : distinguish
     * optionC : Sight
     * optionD : observe
     * qid : 1
     * question : You should be able toright from wrong.
     * realquestion : You should be able to _________ right from wrong.
     */

    private String analysis;
    private String answer;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int qid;
    private String question;
    private String realquestion;

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRealquestion() {
        return realquestion;
    }

    public void setRealquestion(String realquestion) {
        this.realquestion = realquestion;
    }
}
