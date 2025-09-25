package com.domain;


import javafx.scene.image.Image;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.io.ByteArrayInputStream;

public class Question {

    private String question;
    private Image image;
    @ColumnName("AnswerA")
    private String a;
    @ColumnName("AnswerB")
    private String b;
    @ColumnName("AnswerC")
    private String c;
    @ColumnName("AnswerD")
    private String d;
    private String answer;
    private String paperCode;
    private String questionSetCode;

    public Question() {

    }

    public Question(String Question, byte[] Image, String A, String B, String C, String D, String Answer, String PaperCode, String QuestionSetCode) {
        this.question = Question;
        ByteArrayInputStream bis = new ByteArrayInputStream(Image);
        this.image = new Image(bis);
        this.a = A;
        this.b = B;
        this.c = C;
        this.d = D;
        this.answer = Answer;
        this.paperCode = PaperCode;
        this.questionSetCode = QuestionSetCode;
    }

    public Question(String Question, String A, String B, String C, String D, String Answer, String PaperCode, String QuestionSetCode) {
        this.question = Question;
        this.a = A;
        this.b = B;
        this.c = C;
        this.d = D;
        this.answer = Answer;
        this.paperCode = PaperCode;
        this.questionSetCode = QuestionSetCode;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String Question) {
        this.question = Question;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image Image) {
        this.image = Image;
    }

    public void setImage(byte[] Image) {
        ByteArrayInputStream bis = new ByteArrayInputStream(Image);
        this.image = new Image(bis);
    }

    public String getA() {
        return a;
    }

    @ColumnName("AnswerA")
    public void setA(String A) {
        this.a = A;
    }

    public String getB() {
        return b;
    }

    @ColumnName("AnswerB")
    public void setB(String B) {
        this.b = B;
    }

    public String getC() {
        return c;
    }

    @ColumnName("AnswerC")
    public void setC(String C) {
        this.c = C;
    }

    public String getD() {
        return d;
    }

    @ColumnName("AnswerD")
    public void setD(String D) {
        this.d = D;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String Answer) {
        this.answer = Answer;
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

    @Override
    public String toString() {
        return "Question{" + "question=" + question + ", + A= " + a + ", B= " + b + ", C= " + c + ", D= " + d + ", Answer " + answer;
    }
}
