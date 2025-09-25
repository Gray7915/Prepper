package com.prepper;

import com.data.JdbiDAOFactory;
import com.data.QuestionDAO;
import com.domain.Question;
import com.domain.QuestionSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuestionViewController {
    @FXML
    private Label QuestionLabel, ScoreLabel;
    @FXML
    private Button ButtonA, ButtonB, ButtonC, ButtonD, nextQuestion;
    @FXML
    private ImageView imageHolder;
    @FXML
    private AnchorPane contentPane;
    List<Question> questions;
    QuestionSet QuestionSet;
    int questionNumber = 0;
    int Score = 0;
    QuestionDAO questionDAO;

    @FXML
    public void initialize() {

    }

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    public void setQuestions(Collection<Question> questions) {
        this.questions = new ArrayList<>(questions);
        setQuestion(0);
        ButtonA.setOnAction(e -> {
            boolean correct = ButtonClick(ButtonA.getText());
            if (correct) {
                ButtonA.setStyle("-fx-background-color: green;");
            }
        });
        ButtonB.setOnAction(e -> {
            boolean correct = ButtonClick(ButtonB.getText());
            if (correct) {
                ButtonB.setStyle("-fx-background-color: green;");
            }
        });
        ButtonC.setOnAction(e -> {
            boolean correct = ButtonClick(ButtonC.getText());
            if (correct) {
                ButtonC.setStyle("-fx-background-color: green;");
            }
        });
        ButtonD.setOnAction(e -> {
            boolean correct = ButtonClick(ButtonD.getText());
            if (correct) {
                ButtonD.setStyle("-fx-background-color: green;");
            }
        });

        nextQuestion.setOnAction(e -> {
            questionNumber += 1;
            setQuestion(questionNumber);
            nextQuestion.setVisible(false);
            ButtonA.setStyle("");
            ButtonB.setStyle("");
            ButtonC.setStyle("");
            ButtonD.setStyle("");
        });
    }

    public void setQuestionSet(QuestionSet questionSet) {
        this.QuestionSet = questionSet;
        questionDAO = JdbiDAOFactory.getQuestionDAO();
        setQuestions(questionDAO.getQuestionsForSet(QuestionSet.getPaperCode(), QuestionSet.getQuestionSetCode()));
        setQuestion(questionNumber);
        ScoreLabel.setText("Score: " + Score);
    }

    public void setQuestion(int questionNumber) {
        Question question = questions.get(questionNumber);
        System.out.println(question.toString());
        QuestionLabel.setText(question.getQuestion());
        ButtonA.setText(question.getA());
        ButtonB.setText(question.getB());
        ButtonC.setText(question.getC());
        ButtonD.setText(question.getD());
        if (question.getImage() != null) {
            imageHolder.setImage(question.getImage());
            imageHolder.setVisible(true);
        }
        if (questionNumber == questions.size() - 1) {
            System.out.println("Finish Test");
            nextQuestion.setText("Finish");
            nextQuestion.setOnAction(e -> {
                try {
                    System.out.println("Finish Test Set ");

                    FXMLLoader paperViewLoader = new FXMLLoader(getClass().getResource("paper-view.fxml"));
                    Node paperView = paperViewLoader.load();
                    PaperViewController paperController = paperViewLoader.getController();

                    contentPane.getChildren().clear();
                    contentPane.getChildren().add(paperView);
                    AnchorPane.setTopAnchor(paperView, 0.0);
                    AnchorPane.setBottomAnchor(paperView, 0.0);
                    AnchorPane.setLeftAnchor(paperView, 0.0);
                    AnchorPane.setRightAnchor(paperView, 0.0);

                    paperController.setContentPane(contentPane);
                    paperController.setPaperCode(QuestionSet.getPaperCode());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    public boolean ButtonClick(String Answer) {
        boolean correct = false;
        if (Answer.equals(questions.get(questionNumber).getAnswer())) {
            Score++;
            ScoreLabel.setText("Score: " + Score);
            correct = true;
        }
        nextQuestion.setVisible(true);
        return correct;
    }
}
