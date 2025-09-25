package com.prepper;

import com.data.JdbiDAOFactory;
import com.data.QuestionDAO;
import com.domain.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AddQuestionViewController {
    @FXML
    Button finishSet, saveQuestion, createAnother, selectImageButton;
    @FXML
    TextArea QuestionInput, AnswerAInput, AnswerBInput, AnswerCInput, AnswerDInput;
    @FXML
    CheckBox AnswerACheck, AnswerBCheck, AnswerCCheck, AnswerDCheck;
    @FXML
    VBox rootVBox;
    @FXML
    FlowPane topFlowPane;
    String imageFilePath = null;
    String QuestionSetCode;
    String PaperCode;
    QuestionDAO questionDAO;

    @FXML
    public void initialize() {
        questionDAO = JdbiDAOFactory.getQuestionDAO();
        selectImageButton.setOnAction(e -> {
            imageFilePath = getFilePath();
        });

        saveQuestion.setOnAction(e -> {
            String Answer = "";
            if(AnswerACheck.isSelected()){
                Answer = AnswerAInput.getText();
            }else if(AnswerBCheck.isSelected()){
                Answer = AnswerBInput.getText();
            }else if(AnswerCCheck.isSelected()){
                Answer = AnswerCInput.getText();
            }else if(AnswerDCheck.isSelected()){
                Answer = AnswerDInput.getText();
            }

            if (imageFilePath != null) {
                File imageFile = new File(imageFilePath);
                byte[] imageBytes = null;
                try {
                    imageBytes = Files.readAllBytes(imageFile.toPath());
                } catch (IOException error) {
                    error.printStackTrace();
                }
                questionDAO.insertQuestion(new Question(QuestionInput.getText(), imageBytes, AnswerAInput.getText(), AnswerBInput.getText(), AnswerCInput.getText(), AnswerDInput.getText(), Answer, PaperCode, QuestionSetCode));
            }else{
                questionDAO.insertQuestion(new Question(QuestionInput.getText(), AnswerAInput.getText(), AnswerBInput.getText(), AnswerCInput.getText(), AnswerDInput.getText(), Answer, PaperCode, QuestionSetCode));
                System.out.println("PaperCode: " + QuestionSetCode);
                System.out.println("QuestionSetCode: " + PaperCode);
            }
        });

    }

    public String getFilePath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        }
        return null;
    }
}
