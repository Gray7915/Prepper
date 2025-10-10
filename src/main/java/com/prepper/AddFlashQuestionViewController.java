package com.prepper;

import com.data.FlashQuestionDAO;
import com.data.JdbiDAOFactory;
import com.domain.FlashQuestion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddFlashQuestionViewController {
    @FXML
    AnchorPane contentPane;
    @FXML
    Button cancelSet, finishSet;
    @FXML
    TextField categoryNameInput;
    @FXML
    ScrollPane flashQuestionHolder;
    @FXML
    VBox flashHolder;
    int questionNumber = 0;
    List<FlashQuestion> flashQuestions;
    String paperCode;
    String questionSetCode;
    FlashQuestionDAO flashQuestionDAO = JdbiDAOFactory.getFlashQuestionDAO();

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    public void setQuestionSetCode(String questionSetCode) {
        this.questionSetCode = questionSetCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    @FXML
    public void initialize() {
        System.out.println("AddFlashQuestionViewController");
        flashHolder.setPadding(new Insets(15, 15, 15, 15));
        flashQuestions = new ArrayList<>();
        cancelSet.setOnAction(event -> {
            returnHome();
        });
        finishSet.setOnAction(event -> {
            addFlashQuestions();
        });
        createQuestion();
    }

    public void createQuestion() {
        FlashQuestion newQuestion = new FlashQuestion();
        flashQuestions.add(newQuestion);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(25);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(40);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(10);


        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(20);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(20);

        GridPane gridPane = new GridPane();
        gridPane.setId("question" + questionNumber);
        questionNumber++;
        gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
        gridPane.getRowConstraints().addAll(row1, row2);
        gridPane.setPrefSize(700, 150);
        gridPane.setMaxHeight(200);

        Label questionLabel = new Label("Question");
        gridPane.add(questionLabel, 0, 0);
        TextField questionText = new TextField();
        questionText.textProperty().addListener((obs, oldValue, newValue) -> {
            newQuestion.setQuestion(newValue); // set value in the Question object
        });
        gridPane.add(questionText, 0, 1);

        Label answerLabel = new Label("Answer");
        gridPane.add(answerLabel, 1, 0);
        TextField answerText = new TextField();
        answerText.textProperty().addListener((obs, oldValue, newValue) -> {
            newQuestion.setAnswer(newValue);
        });
        gridPane.add(answerText, 1, 1);

        ImageView questionImage = new ImageView();
        questionImage.setPickOnBounds(true);
        questionImage.setPreserveRatio(true);
        questionImage.setFitHeight(200);
        questionImage.setFitWidth(250);
        HBox questionImageWrapper = new HBox();
        questionImageWrapper.setAlignment(Pos.CENTER);
        questionImageWrapper.getChildren().add(questionImage);
        questionImage.setVisible(true);
        Button selectImageButton = new Button("Change Image"), saveChanges;
        selectImageButton.setOnAction(e -> {
            questionImage.setImage(new Image(chooseImagePath()));
        });

        Button createAnother = new Button("create Another");
        createAnother.setOnAction(event -> {
            createQuestion();
        });
        gridPane.add(createAnother, 3, 0);
        Button deleteQuestion = new Button("delete");
        deleteQuestion.setOnAction(event -> {
            flashHolder.getChildren().remove(gridPane);
        });
        gridPane.add(deleteQuestion, 3, 1);
        gridPane.getStyleClass().add("grid-pane-sidebar");
        flashHolder.getChildren().add(gridPane);
    }

    public void returnHome() {
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
            paperController.setPaperCode(paperCode);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addFlashQuestions() {
        if (categoryNameInput.getText().equals("")) {
            return;
        }
        for (FlashQuestion flashQuestion : flashQuestions) {
            flashQuestion.setCategory(categoryNameInput.getText());
            flashQuestion.setPaperCode(paperCode);
            flashQuestion.setQuestionSetCode(questionSetCode);
            flashQuestionDAO.insertFlashQuestion(flashQuestion);
        }
        returnHome();
    }

    public String chooseImagePath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(this.contentPane.getScene().getWindow());
        if (selectedFile != null) {
            return selectedFile.toURI().toString();
        }
        return null;
    }
}
