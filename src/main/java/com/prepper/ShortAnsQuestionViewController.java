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
import com.data.ShortAnswerQuestionDAO;
import com.domain.QuestionSet;
import com.domain.ShortAnswerQuestion;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.awt.event.ActionEvent;
import java.io.IOException;
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
    TextArea answerInput;
    @FXML
    Button nextQuestionButton, checkAnswer;
    ShortAnswerQuestionDAO shortAnswerQuestionDAO;
    List<ShortAnswerQuestion> shortAnswerQuestions;
    int question = 0;
    int correct = 0;


    @FXML
    public void initialize(){
        shortAnswerQuestionDAO = JdbiDAOFactory.getShortAnswerQuestionDAO();
        checkAnswer.setOnAction(e -> {
            try {
                checkAnswer(answerInput.getText().trim());
            } catch (ModelException ex) {
                throw new RuntimeException(ex);
            } catch (TranslateException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        nextQuestionButton.setOnAction(e -> {
            nextQuestionButtonAction();
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
        setQuestion(0);
    }

    public void setQuestion(int questionNumber){
        ShortAnswerQuestion question = shortAnswerQuestions.get(questionNumber);
        questionLabel.setText(question.getQuestion());
        Question = question.getQuestion();
        example = question.getAnswer();
    }

    public void checkAnswer(String answer) throws ModelException, TranslateException, IOException{
        Criteria<String, float[]> criteria = Criteria.builder()
                .setTypes(String.class, float[].class)
                .optModelUrls("djl://ai.djl.huggingface.pytorch/sentence-transformers/all-MiniLM-L6-v2")
                .optTranslatorFactory(new TextEmbeddingTranslatorFactory())
                .optEngine("PyTorch")
                .build();
        try (ZooModel<String, float[]> model = criteria.loadModel(); Predictor<String, float[]> predictor = model.newPredictor(); NDManager manager = NDManager.newBaseManager()) {
            float[] exampleEmb = predictor.predict(Question+example);
            System.out.println(Question+example);
            NDArray vExample = manager.create(exampleEmb);
                float[] studentEmb = predictor.predict(Question+answer);
            System.out.println(Question+answer);
            NDArray vStudent = manager.create(studentEmb);
                float similarity = vExample.dot(vStudent).div(vExample.norm().mul(vStudent.norm())).getFloat();
            if (similarity > 0.8){
                correct++;
                scoreLabel.setText("Score: " + correct + " / " + shortAnswerQuestions.size());
                System.out.println(questionLabel.getText());
            }
            System.out.println("Similarity: " + similarity);

            } catch (ModelNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void nextQuestionButtonAction() {
        question++;
        setQuestion(question);
    }
}
