package com.prepper;

import ai.djl.ModelException;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.MalformedModelException;
import ai.djl.huggingface.translator.TextEmbeddingTranslatorFactory;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import com.data.JdbiDAOFactory;
import com.data.QuestionSetDAO;
import com.data.ShortAnswerQuestionDAO;
import com.domain.QuestionSet;
import com.domain.ShortAnswerQuestion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ShortAnsQuestionViewController {
    private AnchorPane contentPane;
    private QuestionSet questionSet;
    private String Question;
    private String example;
    @FXML
    Label scoreLabel;
    @FXML
    Label questionLabel;
    @FXML
    Label answerLabel;
    @FXML
    Label expleAns;
    @FXML
    Label questionNo;
    @FXML
    TextArea answerInput;
    @FXML
    Button nextQuestionButton, checkAnswer, setAsCorrect, notSure, retakeBtn;
    QuestionSetDAO questionSetDAO;
    ShortAnswerQuestionDAO shortAnswerQuestionDAO;
    List<ShortAnswerQuestion> shortAnswerQuestions;
    List<ShortAnswerQuestion> incorrectShortAnswerQuestions = new ArrayList<>();
    int question = 0;
    int questionDisplay = question + 1;
    int correct = 0;


    @FXML
    public void initialize() {
        nextQuestionButton.setMouseTransparent(true);
        nextQuestionButton.setFocusTraversable(false);
        answerLabel.setVisible(false);
        expleAns.setVisible(false);

        shortAnswerQuestionDAO = JdbiDAOFactory.getShortAnswerQuestionDAO();
        checkAnswer.setOnAction(e -> {
            try {
                checkAnswer(answerInput.getText().trim());
                checkAnswer.setMouseTransparent(true);
                checkAnswer.setFocusTraversable(false);
                checkAnswer.setDefaultButton(false);
            } catch (ModelException ex) {
                throw new RuntimeException(ex);
            } catch (TranslateException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        nextQuestionButton.setOnAction(e -> {
            if (question != shortAnswerQuestions.size() - 1) {
                if (question == shortAnswerQuestions.size() - 2){
                    nextQuestionButton.setText("Finish Set");
                    retakeBtn.setVisible(true);
                }
                nextQuestionButtonAction();
            } else {
                finishSet();
            }
            checkAnswer.setMouseTransparent(false);
            checkAnswer.setFocusTraversable(true);
            nextQuestionButton.setMouseTransparent(true);
            nextQuestionButton.setFocusTraversable(false);
            retakeBtn.setMouseTransparent(true);
            retakeBtn.setFocusTraversable(false);
        });
        setAsCorrect.setOnAction(e -> {
            setAsCorrect();
        });
        notSure.setOnAction(e -> {
            notSureButtonAction();
        });
        retakeBtn.setOnAction(e -> {
            retakeBtn();
        });
    }

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    public void setQuestionSet(QuestionSet questionSet) {
        this.questionSet = questionSet;
        shortAnswerQuestions = (List<ShortAnswerQuestion>) shortAnswerQuestionDAO.getShortAnswerQuestionSet(questionSet.getPaperCode(), questionSet.getQuestionSetCode());
        Collections.shuffle(shortAnswerQuestions);
        scoreLabel.setText("Score: " + correct + " / " + shortAnswerQuestions.size());
        setQuestion(question);
        questionNo.setText("Question No. " + (questionDisplay));
    }

    public void setQuestion(int questionNumber) {
        ShortAnswerQuestion question = shortAnswerQuestions.get(questionNumber);
        questionLabel.setText(question.getQuestion());
        Question = question.getQuestion();
        example = question.getAnswer();
    }

    public void setAsCorrect() {
        incorrectShortAnswerQuestions.removeLast();
        correct++;
        scoreLabel.setText("Score: " + correct + " / " + shortAnswerQuestions.size());
    }

    public void checkAnswer(String answer) throws ModelException, TranslateException, IOException {
        answerInput.setEditable(false);
        answerInput.setMouseTransparent(true);
        answerInput.setFocusTraversable(false);
        Criteria<String, float[]> criteria = Criteria.builder()
                .setTypes(String.class, float[].class)
                .optModelUrls("djl://ai.djl.huggingface.pytorch/sentence-transformers/all-MiniLM-L6-v2")
                .optTranslatorFactory(new TextEmbeddingTranslatorFactory())
                .optEngine("PyTorch")
                .build();
        try (ZooModel<String, float[]> model = criteria.loadModel(); Predictor<String, float[]> predictor = model.newPredictor(); NDManager manager = NDManager.newBaseManager()) {
            float[] exampleEmb = predictor.predict(Question + example);
            System.out.println(Question + example);
            NDArray vExample = manager.create(exampleEmb);
            float[] studentEmb = predictor.predict(Question + answer);
            System.out.println(Question + answer);
            NDArray vStudent = manager.create(studentEmb);
            float QuestionAnswerSim = vExample.dot(vStudent).div(vExample.norm().mul(vStudent.norm())).getFloat();

            float[] answerEmb = predictor.predict(answer);
            NDArray vAnswer = manager.create(answerEmb);
            float[] exampleAnsEmb = predictor.predict(example);
            NDArray vExampleAns = manager.create(exampleAnsEmb);
            float AnswerSim = vExampleAns.dot(vAnswer).div(vExampleAns.norm().mul(vAnswer.norm())).getFloat();
            if (QuestionAnswerSim > 0.70 && AnswerSim > 0.85) {
                correct++;
                answerInput.getStyleClass().add("fading-border");
                scoreLabel.setText("Score: " + correct + " / " + shortAnswerQuestions.size());
                System.out.println(questionLabel.getText());
            } else {
                incorrectShortAnswerQuestions.add(shortAnswerQuestions.get(question));
                answerInput.setStyle("-fx-border-color: red; -fx-border-width: 5px; -fx-border-radius: 5;");
                answerLabel.setText(example);
                setAsCorrect.setVisible(true);
                expleAns.setVisible(true);
                answerLabel.setVisible(true);
                System.out.println("Example Answer: " + example + " Answer: " + answer);
            }
            System.out.println("Question Plus Answer Similarity: " + QuestionAnswerSim);
            System.out.println("Answer Similarity: " + AnswerSim);

        } catch (ModelNotFoundException e) {
            throw new RuntimeException(e);
        }
        nextQuestionButton.setMouseTransparent(false);
        nextQuestionButton.setFocusTraversable(true);
        retakeBtn.setMouseTransparent(false);
        retakeBtn.setFocusTraversable(true);
    }

    public void notSureButtonAction(){
        checkAnswer.setMouseTransparent(true);
        checkAnswer.setFocusTraversable(false);
        nextQuestionButton.setMouseTransparent(false);
        nextQuestionButton.setFocusTraversable(true);
        answerLabel.setText(example);
        setAsCorrect.setVisible(true);
        expleAns.setVisible(true);
        answerLabel.setVisible(true);
        incorrectShortAnswerQuestions.add(shortAnswerQuestions.get(question));
    }

    public void nextQuestionButtonAction() {
        answerInput.setStyle("");
        answerInput.getStyleClass().clear();
        answerInput.clear();
        answerInput.setEditable(true);
        answerInput.setMouseTransparent(false);
        answerInput.setFocusTraversable(true);
        expleAns.setVisible(false);
        answerLabel.setVisible(false);
        setAsCorrect.setVisible(false);
        questionNo.setText("Question No. " + (questionDisplay));
        question++;
        questionDisplay++;
        setQuestion(question);
    }

    public void finishSet() {
        questionSetDAO = JdbiDAOFactory.getQuestionSetDAO();
        if (questionSet.getAttemptCount() == null) {
            questionSet.setAttemptCount(0);
        }
        float newAverage = (questionSet.getAverageScore() * questionSet.getAttemptCount()
                + ((float) correct / shortAnswerQuestions.size() * 100))
                / (questionSet.getAttemptCount() + 1);
        questionSet.setAverageScore(newAverage);
        questionSet.setAttemptCount(questionSet.getAttemptCount() + 1);
        questionSet.setPreviousScore((float) correct / shortAnswerQuestions.size() * 100);
        questionSetDAO.addScoreandOverall(questionSet);
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
            paperController.setPaperCode(questionSet.getPaperCode());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void retakeBtn(){
        retakeBtn.setVisible(false);
        shortAnswerQuestions.clear();
        shortAnswerQuestions = new ArrayList<>(incorrectShortAnswerQuestions);
        question = 0;
        correct = 0;
        questionDisplay = 1;
        scoreLabel.setText("Score: " + correct + " / " + shortAnswerQuestions.size());
        setQuestion(question);
        questionNo.setText("Question No. " + (questionDisplay));
        nextQuestionButton.setText("Next Question");
        checkAnswer.setMouseTransparent(false);
        checkAnswer.setFocusTraversable(true);
        answerInput.clear();
        answerInput.setEditable(true);
        answerInput.setMouseTransparent(false);
        answerInput.setFocusTraversable(true);
        expleAns.setVisible(false);
        answerLabel.setVisible(false);
        setAsCorrect.setVisible(false);
        retakeBtn.setVisible(false);
        incorrectShortAnswerQuestions.clear();
        answerInput.setStyle("");
    }
}
