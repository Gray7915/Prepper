package com.prepper;

import com.data.FlashQuestionDAO;
import com.data.JdbiDAOFactory;
import com.data.QuestionDAO;
import com.data.QuestionSetDAO;
import com.domain.FlashQuestion;
import com.domain.Question;
import com.domain.QuestionSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    Collection<FlashQuestion> flashQuestions;
    QuestionSet QuestionSet;
    int questionNumber = 0;
    int Score = 0;
    QuestionDAO questionDAO;
    QuestionSetDAO questionSetDAO;
    FlashQuestionDAO flashQuestionDAO;
    private List<Button> buttons;

    @FXML
    public void initialize() {
        buttons = new ArrayList<>();
        buttons.add(ButtonA);
        buttons.add(ButtonB);
        buttons.add(ButtonC);
        buttons.add(ButtonD);
    }

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    public void setQuestions(Collection<Question> questions) {
        this.questions = new ArrayList<>(questions);
        Collections.shuffle(this.questions);
        System.out.println(questions.size());
        for (Button button : buttons) {
            button.setOnAction(event -> {
                buttonAction(button);
            });
        }

        nextQuestion.setOnAction(e -> {
            questionNumber += 1;
            nextQuestion.setVisible(false);
            for (Button button : buttons) {
                button.setStyle("");
            }
            imageHolder.setImage(null);
            setQuestion(questionNumber);
        });
    }

    public void setQuestionSet(QuestionSet questionSet) {
        this.QuestionSet = questionSet;
        questionDAO = JdbiDAOFactory.getQuestionDAO();
        questionSetDAO = JdbiDAOFactory.getQuestionSetDAO();
        flashQuestionDAO = JdbiDAOFactory.getFlashQuestionDAO();
        setQuestions(questionDAO.getQuestionsForSet(QuestionSet.getPaperCode(), QuestionSet.getQuestionSetCode()));
        this.flashQuestions = flashQuestionDAO.getFlashQuestionForSet(QuestionSet.getPaperCode(), QuestionSet.getQuestionSetCode());
        if (!flashQuestions.isEmpty()) {
            for (FlashQuestion flashQuestion : flashQuestions) {
                Question question = new Question();
                question.setPaperCode(flashQuestion.getPaperCode());
                question.setQuestionSetCode(flashQuestion.getQuestionSetCode());
                question.setQuestion(flashQuestion.getQuestion());
                question.setAnswer(flashQuestion.getAnswer());
                question.setA(flashQuestion.getAnswer());
                List<FlashQuestion> tempList = new ArrayList<>(flashQuestions);
                tempList.remove(flashQuestion);
                Collections.shuffle(tempList);
                if (tempList.size() > 0) {
                    question.setB(tempList.get(0).getAnswer());
                }
                if (tempList.size() > 1) {
                    question.setC(tempList.get(1).getAnswer());
                }
                if (tempList.size() > 2) {
                    question.setD(tempList.get(2).getAnswer());
                }
                questions.add(question);
            }
        }
        Collections.shuffle(questions);
        setQuestion(questionNumber);
        ScoreLabel.setText("Score: " + Score + " / " + questions.size());
    }

    public void setQuestion(int questionNumber) {
        Question question = questions.get(questionNumber);
        QuestionLabel.setText(question.getQuestion());
        List<String> questionList = new ArrayList<>(List.of(
                question.getA(),
                question.getB(),
                question.getC(),
                question.getD()
        ));
        Collections.shuffle(questionList);
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setText(questionList.get(i));
        }
        disableEnableButtons(false);
        if (question.getImage() != null) {
            imageHolder.setImage(new Image(new ByteArrayInputStream(question.getImage())));
            imageHolder.setVisible(true);
        }
        if (questionNumber == questions.size() - 1) {
            nextQuestion.setText("Finish");
            nextQuestion.setOnAction(e -> {
                finishQuestionSet();
            });
        }
    }

    public boolean ButtonClick(String Answer) {
        boolean correct = false;
        if (Answer.equals(questions.get(questionNumber).getAnswer())) {
            ScoreLabel.setText("Score: " + Score + " / " + questions.size());
            correct = true;
        }
        nextQuestion.setVisible(true);
        return correct;
    }

    public void finishQuestionSet() {
        if (QuestionSet.getAttemptCount() == null) {
            QuestionSet.setAttemptCount(1);
        }
        float newAverage = (QuestionSet.getAverageScore() * QuestionSet.getAttemptCount()
                + ((float) Score / questions.size() * 100))
                / (QuestionSet.getAttemptCount() + 1);
        QuestionSet.setAverageScore(newAverage);
        QuestionSet.setAttemptCount(QuestionSet.getAttemptCount() + 1);
        QuestionSet.setPreviousScore((float) Score / questions.size() * 100);
        questionSetDAO.addScoreandOverall(QuestionSet);
        try {
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
    }

    public void buttonAction(Button button) {
        boolean correct = ButtonClick(button.getText());
        if (correct) {
            button.setStyle("-fx-background-color: green");
            Score++;
            ScoreLabel.setText("Score: " + Score + " / " + questions.size());
        } else {
            button.setStyle("-fx-background-color: red");
            for (Button button1 : buttons) {
                disableEnableButtons(true);
                if (ButtonClick(button1.getText())) {
                    button1.setStyle("-fx-background-color: #9db419");
                }
            }
        }
    }

    public void disableEnableButtons(boolean status) {
        for (Button button : buttons) {
            button.setMouseTransparent(status);
            button.setFocusTraversable(!status);
        }
    }
}
