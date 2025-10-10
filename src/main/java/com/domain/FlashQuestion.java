package com.domain;

import java.util.*;

public class FlashQuestion {
    private int ID;
    private String question;
    private byte[] image;
    private String answer;
    private String paperCode;
    private String questionSetCode;
    private String category;

    public FlashQuestion() {
        /*
        Hashtable<String, Integer> numbers = new Hashtable<>();
        numbers.put("one", 1);
        numbers.put("two", 2);
        numbers.put("three", 3);
        List<String> keyset = new ArrayList<>(numbers.keySet());
        keyset.remove("one");
        Collections.shuffle(keyset);
         */
    }

    public FlashQuestion(String question, byte[] image, String answer, String paperCode, String questionSetCode) {
        this.question = question;
        this.image = image;
        this.answer = answer;
        this.paperCode = paperCode;
        this.questionSetCode = questionSetCode;
    }

    public FlashQuestion(String question, String answer, String paperCode, String questionSetCode) {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
