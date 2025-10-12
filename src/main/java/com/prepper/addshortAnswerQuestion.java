package com.prepper;
import com.data.JdbiDAOFactory;
import com.data.ShortAnswerQuestionDAO;
import com.domain.ShortAnswerQuestion;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.awt.*;

public class addshortAnswerQuestion {
    String QuestionSetCode;
    String PaperCode;
    String setType;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private TextField questionBox;
    @FXML
    private TextArea answerBox;
    @FXML
    private Button finishSetButton, saveQuestionButton;
    private ShortAnswerQuestionDAO shortAnswerQuestionDAO;

    @FXML
    void initialize() {
        shortAnswerQuestionDAO = JdbiDAOFactory.getShortAnswerQuestionDAO();
        setSaveQuestionButton();
    }
    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    public void setSaveQuestionButton(){
        saveQuestionButton.setOnAction(e -> {
            System.out.println("did i run?");
            ShortAnswerQuestion question = new ShortAnswerQuestion(questionBox.getText().trim(), answerBox.getText().trim(), PaperCode, QuestionSetCode);
            shortAnswerQuestionDAO.SaveShortAnswerQuestion(question);
        });
    }
}
