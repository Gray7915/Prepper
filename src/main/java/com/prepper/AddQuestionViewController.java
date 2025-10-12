package com.prepper;

import com.data.JdbiDAOFactory;
import com.data.QuestionDAO;
import com.data.QuestionSetDAO;
import com.domain.Question;
import com.domain.QuestionSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AddQuestionViewController {
    @FXML
    Button finishSet, saveQuestion, createAnother, selectImageButton;
    @FXML
    TextField QuestionInput, AnswerAInput, AnswerBInput, AnswerCInput, AnswerDInput;
    @FXML
    CheckBox AnswerACheck, AnswerBCheck, AnswerCCheck, AnswerDCheck;
    @FXML
    VBox rootVBox;
    @FXML
    FlowPane topFlowPane;
    @FXML
    ImageView imageHolder;
    @FXML
    private AnchorPane contentPane;
    String imageFilePath = null;
    String QuestionSetCode;
    String PaperCode;
    String setType;
    QuestionDAO questionDAO;
    QuestionSetDAO questionSetDAO;

    @FXML
    public void initialize() {
        questionDAO = JdbiDAOFactory.getQuestionDAO();
        questionSetDAO = JdbiDAOFactory.getQuestionSetDAO();
        selectImageButton.setOnAction(e -> {
            imageFilePath = getFilePath();
        });

        saveQuestion.setOnAction(e -> {
            saveQuestion();
        });

        createAnother.setOnAction(e -> {
            createAnother();
        });

        finishSet.setOnAction(e -> {
            finishSet();
        });

    }

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    public void saveQuestion() {
        String Answer = "";
        if (AnswerACheck.isSelected()) {
            Answer = AnswerAInput.getText();
        } else if (AnswerBCheck.isSelected()) {
            Answer = AnswerBInput.getText();
        } else if (AnswerCCheck.isSelected()) {
            Answer = AnswerCInput.getText();
        } else if (AnswerDCheck.isSelected()) {
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
            questionDAO.insertQuestion(new Question(QuestionInput.getText().trim(), imageBytes, AnswerAInput.getText().trim(), AnswerBInput.getText().trim(), AnswerCInput.getText().trim(), AnswerDInput.getText().trim(), Answer.trim(), PaperCode, QuestionSetCode));
        } else {
            questionDAO.insertQuestion(new Question(QuestionInput.getText().trim(), AnswerAInput.getText().trim(), AnswerBInput.getText().trim(), AnswerCInput.getText().trim(), AnswerDInput.getText().trim(), Answer.trim(), PaperCode, QuestionSetCode));
        }
        //resets the average score and such when adding more settings
        QuestionSet qSet = new QuestionSet();
        qSet.setQuestionSetCode(QuestionSetCode);
        qSet.setPaperCode(PaperCode);
        qSet.setAverageScore(0);
        qSet.setPreviousScore(0);
        qSet.setAttemptCount(0);
        questionSetDAO.addScoreandOverall(qSet);

    }

    public void createAnother() {
        QuestionInput.setText("");
        AnswerAInput.setText("");
        AnswerBInput.setText("");
        AnswerCInput.setText("");
        AnswerDInput.setText("");
        AnswerACheck.setSelected(false);
        AnswerBCheck.setSelected(false);
        AnswerCCheck.setSelected(false);
        AnswerDCheck.setSelected(false);
        imageFilePath = null;
    }

    public void finishSet(){
        try {
            FXMLLoader paperViewLoader = new FXMLLoader(getClass().getResource("paper-view.fxml"));
            Node paperView = paperViewLoader.load();
            PaperViewController paperController = paperViewLoader.getController();
            paperController.setPaperCode(PaperCode);
            contentPane.getChildren().clear();
            contentPane.getChildren().add(paperView);
            AnchorPane.setTopAnchor(paperView, 0.0);
            AnchorPane.setBottomAnchor(paperView, 0.0);
            AnchorPane.setLeftAnchor(paperView, 0.0);
            AnchorPane.setRightAnchor(paperView, 0.0);

            paperController.setContentPane(contentPane);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getFilePath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (selectedFile != null) {
            imageHolder.setImage(new Image(selectedFile.toURI().toString()));
            return selectedFile.getAbsolutePath();
        }
        return null;
    }
}