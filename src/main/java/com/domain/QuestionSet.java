package com.domain;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.util.ArrayList;

public class QuestionSet {

    private String paperCode;
    private String questionSetCode;
    @ColumnName("AttemptCount")
    private Integer attemptCount;
    private float previousScore;
    private float averageScore;
    private float currentScore;
    private ArrayList<Question> Questions;

    public QuestionSet(String PaperCode, String QuestionSetCode, float PreviousScore, float AverageScore, int timesTaken) {
        this.paperCode = PaperCode;
        this.questionSetCode = QuestionSetCode;
        this.attemptCount = timesTaken;
        this.previousScore = PreviousScore;
        this.averageScore = AverageScore;
    }

    public QuestionSet(String PaperCode, String QuestionSetCode, float PreviousScore, float AverageScore) {
        this.paperCode = PaperCode;
        this.questionSetCode = QuestionSetCode;
        this.previousScore = PreviousScore;
        this.averageScore = AverageScore;
    }

    public QuestionSet(String PaperCode, String QuestionSetCode) {
        this.paperCode = PaperCode;
        this.questionSetCode = QuestionSetCode;
    }

    public QuestionSet() {
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String PaperCode) {
        this.paperCode = PaperCode;
    }

    public String getQuestionSetCode() {
        return questionSetCode;
    }

    public void setQuestionSetCode(String QuestionSetCode) {
        this.questionSetCode = QuestionSetCode;
    }

    public float getPreviousScore() {
        return previousScore;
    }

    public void setPreviousScore(float PreviousScore) {
        this.previousScore = PreviousScore;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float AverageScore) {
        this.averageScore = AverageScore;
    }

    public float getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(float CurrentScore) {
        this.currentScore = CurrentScore;
    }

    public ArrayList<Question> getQuestions() {
        return Questions;
    }

    public void setQuestions(ArrayList<Question> Questions) {
        this.Questions = Questions;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(Integer  attemptCount) {
        this.attemptCount = attemptCount;
    }
}

