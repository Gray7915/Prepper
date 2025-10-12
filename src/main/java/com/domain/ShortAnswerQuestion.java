package com.domain;

public class ShortAnswerQuestion {
    private String question;
    private String answer;
    private String paperCode;
    private String questionSetCode;

    public ShortAnswerQuestion() {

    }

    public ShortAnswerQuestion(String question, String answer, String paperCode, String questionSetCode) {
        this.question = question;
        this.answer = answer;
        this.paperCode = paperCode;
        this.questionSetCode = questionSetCode;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public String getQuestionSetCode() {
        return questionSetCode;
    }

    public void setQuestionSetCode(String questionSetCode) {
        this.questionSetCode = questionSetCode;
    }
}
