package com.prepper;

import com.data.JdbiDAOFactory;
import com.data.PaperDAO;
import com.data.QuestionSetDAO;
import com.domain.Paper;
import com.domain.QuestionSet;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class PaperViewController {
    @FXML
    private Label PaperIdLabel; // example UI element
    @FXML
    private Button CreateQuestionSet;
    @FXML
    private VBox paperViewRoot;

    private String paperCode;

    @FXML
    AnchorPane contentPane;
    private QuestionSetDAO questionSetDAO;

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    // Setter to receive the value
    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
        if (PaperIdLabel != null) {
            PaperIdLabel.setText(paperCode);
            questionSetDisplay(); // <--- call it here
        }
    }

    @FXML
    public void initialize() {
        questionSetDAO = JdbiDAOFactory.getQuestionSetDAO();
        // If paperCode was set before initialization
        if (paperCode != null) {
            PaperIdLabel.setText(paperCode);
        }

        CreateQuestionSet.setOnAction(e -> {
            Stage popup = new Stage();
            popup.setTitle("Enter set name");

            TextField input = new TextField();
            input.setPromptText("Enter set name");
            Button okButton = new Button("Enter");
            Button cancelButton = new Button("Cancel");
            Runnable handleInput = () -> {
                String SetName = input.getText().trim();
                boolean ValidSetName = addSet(SetName);
                if (ValidSetName && SetName != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddQuestion-view.fxml"));
                    VBox addQuestionView = null;
                    try {
                        addQuestionView = loader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    AddQuestionViewController controller = loader.getController();
                    controller.QuestionSetCode = SetName;
                    controller.PaperCode = paperCode;
                    contentPane.getChildren().clear();
                    contentPane.getChildren().add(addQuestionView);
                    // Make it resize with contentPane
                    AnchorPane.setTopAnchor(addQuestionView, 0.0);
                    AnchorPane.setBottomAnchor(addQuestionView, 0.0);
                    AnchorPane.setLeftAnchor(addQuestionView, 0.0);
                    AnchorPane.setRightAnchor(addQuestionView, 0.0);
                    popup.close();
                }
            };
            okButton.setOnAction(ev -> handleInput.run());
            input.setOnAction(ev -> handleInput.run());
            cancelButton.setOnAction(ev -> popup.close());
            VBox layout = new VBox(10, input, okButton, cancelButton);
            layout.setPadding(new Insets(10));

            Scene scene = new Scene(layout);
            popup.setScene(scene);
            popup.show();
        });
    }

    public void questionSetDisplay(){
        System.out.println(paperCode);
        Collection<QuestionSet> questionSets = questionSetDAO.getQuestionSetsForPaper(paperCode);
        System.out.println("Loaded " + questionSets.size() + " question sets for paper " + paperCode);
        for (QuestionSet questionSet : questionSets) {
            HBox hBox = new HBox();
            hBox.setPrefWidth(paperViewRoot.getPrefWidth()*0.8);
            hBox.setPrefHeight(paperViewRoot.getHeight()/4);
            hBox.setMaxHeight(192);
            hBox.setMaxWidth(819.2);
            hBox.setMinHeight(192);
            hBox.setMinWidth(819.2);
            Label questionSetLabel = new Label(questionSet.getQuestionSetCode());
            Button takeQuestionSetButton = new Button("Take Question Set");
            takeQuestionSetButton.setOnAction(ev -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("question-view.fxml"));
                AnchorPane questionView = null;
                try {
                    questionView = loader.load();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                QuestionViewController controller = loader.getController();
                controller.setQuestionSet(questionSet);
                contentPane.getChildren().clear();
                contentPane.getChildren().add(questionView);
                controller.setContentPane(contentPane);
                // Make it resize with contentPane
                AnchorPane.setTopAnchor(questionView, 0.0);
                AnchorPane.setBottomAnchor(questionView, 0.0);
                AnchorPane.setLeftAnchor(questionView, 0.0);
                AnchorPane.setRightAnchor(questionView, 0.0);
            });
            Button editQuestionSetButton = new Button("Edit Question Set");
            editQuestionSetButton.setOnAction(ev -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AddQuestion-view.fxml"));
                VBox addQuestionView = null;
                try {
                    addQuestionView = loader.load();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                AddQuestionViewController controller = loader.getController();
                controller.QuestionSetCode = questionSet.getQuestionSetCode();
                controller.PaperCode = paperCode;
                contentPane.getChildren().clear();
                contentPane.getChildren().add(addQuestionView);
                // Make it resize with contentPane
                AnchorPane.setTopAnchor(addQuestionView, 0.0);
                AnchorPane.setBottomAnchor(addQuestionView, 0.0);
                AnchorPane.setLeftAnchor(addQuestionView, 0.0);
                AnchorPane.setRightAnchor(addQuestionView, 0.0);
            });
            hBox.getChildren().add(questionSetLabel);
            hBox.getChildren().add(takeQuestionSetButton);
            hBox.getChildren().add(editQuestionSetButton);
            paperViewRoot.getChildren().addLast(hBox);
        }
    }
    public boolean addSet(String setName) {
        QuestionSet questionSet = new QuestionSet(paperCode, setName);
        Collection<QuestionSet> sets = questionSetDAO.getQuestionSets();
        HashSet<QuestionSet> QuestSets = new HashSet<>(sets);
        if (QuestSets.contains(questionSet)) {
            return false;
        } else {
            questionSetDAO.SaveQuestionSet(questionSet);
            return true;
        }
    }
}
