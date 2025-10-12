package com.prepper;

import com.data.JdbiDAOFactory;
import com.data.QuestionSetDAO;
import com.domain.QuestionSet;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;


public class PaperViewController {
    @FXML
    private Label PaperIdLabel; // example UI element
    @FXML
    private Button CreateQuestionSet, shortAnsSet;
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
        shortAnsSet.setOnAction(e -> {
            Stage popup = new Stage();
            popup.setTitle("Enter Short Ans Set name");

            TextField input = new TextField();
            input.setPromptText("Enter set name");
            Button okButton = new Button("Enter");
            Button cancelButton = new Button("Cancel");
            Runnable handleInput = () -> {
                String SetName = input.getText().trim();
                boolean ValidSetName = addSet(SetName, "shortAns");
                if (ValidSetName && SetName != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddQuestion-view.fxml"));
                    VBox addQuestionView = null;
                    try {
                        addQuestionView = loader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    addshortAnswerQuestion controller = loader.getController();
                    controller.QuestionSetCode = SetName;
                    controller.PaperCode = paperCode;
                    controller.setType = "shortAns";
                    controller.setContentPane(contentPane);
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

        CreateQuestionSet.setOnAction(e -> {
            Stage popup = new Stage();
            popup.setTitle("Enter set name");

            TextField input = new TextField();
            input.setPromptText("Enter set name");
            Button okButton = new Button("Enter");
            Button cancelButton = new Button("Cancel");
            Runnable handleInput = () -> {
                String SetName = input.getText().trim();
                boolean ValidSetName = addSet(SetName, "regular");
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
                    controller.setType = "regular";
                    controller.setContentPane(contentPane);
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

    public void questionSetDisplay() {
        System.out.println(paperCode);
        Collection<QuestionSet> questionSets = questionSetDAO.getQuestionSetsForPaper(paperCode);
        System.out.println("Loaded " + questionSets.size() + " question sets for paper " + paperCode);
        for (QuestionSet questionSet : questionSets) {
            if(!questionSet.getSetType().equals("regular")){
                break;
            }
            HBox hBox = new HBox();
            hBox.setPrefWidth(paperViewRoot.getPrefWidth() * 0.8);
            hBox.setPrefHeight(paperViewRoot.getHeight() / 4);
            hBox.setMinHeight(192);
            hBox.setMinWidth(619.2);
            hBox.setStyle("-fx-background-color: #2a2a2a");
            VBox vBox = new VBox();
            vBox.setPrefWidth(219.2);
            vBox.setSpacing(150 / 10);
            vBox.setPadding(new Insets(0, 0, 0, 15));

            Label questionSetLabel = new Label(questionSet.getQuestionSetCode());
            questionSetLabel.setStyle(
                    "-fx-font-size: 18; -fx-text-fill: white; "
            );
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
            Button addQuestionButton = new Button("Add Question");
            addQuestionButton.setOnAction(ev -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("addQuestion-view.fxml"));
                VBox addQuestionView = null;
                try {
                    addQuestionView = loader.load();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                AddQuestionViewController controller = loader.getController();
                controller.PaperCode = questionSet.getPaperCode();
                controller.QuestionSetCode = questionSet.getQuestionSetCode();
                contentPane.getChildren().clear();
                contentPane.getChildren().add(addQuestionView);
                // Make it resize with contentPane
                AnchorPane.setTopAnchor(addQuestionView, 0.0);
                AnchorPane.setBottomAnchor(addQuestionView, 0.0);
                AnchorPane.setLeftAnchor(addQuestionView, 0.0);
                AnchorPane.setRightAnchor(addQuestionView, 0.0);
            });
            Button editSetButton = new Button("Edit Set");
            editSetButton.setOnAction(ev -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("editset-view.fxml"));
                AnchorPane editSetView = null;
                try {
                    editSetView = loader.load();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                EditsetViewController controller = loader.getController();
                controller.contentPane = contentPane;
                controller.paperCode = questionSet.getPaperCode();
                controller.setQuestionSet(questionSet);
                contentPane.getChildren().clear();
                contentPane.getChildren().add(editSetView);

                // Make it resize with contentPane
                AnchorPane.setTopAnchor(editSetView, 0.0);
                AnchorPane.setBottomAnchor(editSetView, 0.0);
                AnchorPane.setLeftAnchor(editSetView, 0.0);
                AnchorPane.setRightAnchor(editSetView, 0.0);
            });
            Button addFlashQuestionButton = new Button("Add Flash Question");
            addFlashQuestionButton.setOnAction(ev -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("addFlashQuestion-view.fxml"));
                AnchorPane flashQuestionView = null;
                try {
                    flashQuestionView = loader.load();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                AddFlashQuestionViewController controller = loader.getController();
                contentPane.getChildren().clear();
                contentPane.getChildren().add(flashQuestionView);
                controller.setContentPane(contentPane);
                controller.setPaperCode(paperCode);
                controller.setQuestionSetCode(questionSet.getQuestionSetCode());
                // Make it resize with contentPane
                AnchorPane.setTopAnchor(flashQuestionView, 0.0);
                AnchorPane.setBottomAnchor(flashQuestionView, 0.0);
                AnchorPane.setLeftAnchor(flashQuestionView, 0.0);
                AnchorPane.setRightAnchor(flashQuestionView, 0.0);
            });
            vBox.getChildren().addAll(questionSetLabel, takeQuestionSetButton, addQuestionButton, editSetButton, addFlashQuestionButton);

            double prevScore = questionSet.getPreviousScore();
            double prevScoreFraction = prevScore / 100.0;
            Color prevScoreBarColor = Color.RED.interpolate(Color.LIMEGREEN, prevScoreFraction);
            Tile prevScoreChart = TileBuilder.create()
                    .skinType(Tile.SkinType.CIRCULAR_PROGRESS)
                    .title("Previous Score")
                    .value(prevScore)
                    .maxValue(100)
                    .barColor(prevScoreBarColor)
                    .prefSize(180, 180)
                    .build();
            prevScoreChart.setTitleAlignment(TextAlignment.CENTER);
            prevScoreChart.setTextSize(Tile.TextSize.BIGGER);
            double averageScore = questionSet.getAverageScore();
            double aveScoreFraction = averageScore / 100.0;
            Color aveScoreBarColor = Color.RED.interpolate(Color.LIMEGREEN, aveScoreFraction);
            Tile aveScoreChart = TileBuilder.create()
                    .skinType(Tile.SkinType.CIRCULAR_PROGRESS)
                    .title("Average Score")
                    .value(averageScore)
                    .maxValue(100)
                    .barColor(aveScoreBarColor)
                    .prefSize(180, 180)
                    .build();
            aveScoreChart.setTitleAlignment(TextAlignment.CENTER);
            aveScoreChart.setTextSize(Tile.TextSize.BIGGER);
            hBox.getChildren().addAll(vBox, prevScoreChart, aveScoreChart);
            paperViewRoot.setPadding(new Insets(0, 20, 0, 20));
            paperViewRoot.getChildren().add(hBox);
            paperViewRoot.setSpacing(10);
        }

        for(QuestionSet questionSet : questionSets){
            if(questionSet.getSetType().equals("shortAns")){
                HBox hBox = new HBox();
                hBox.setPrefWidth(paperViewRoot.getPrefWidth() * 0.8);
                hBox.setPrefHeight(paperViewRoot.getHeight() / 4);
                hBox.setMinHeight(192);
                hBox.setMinWidth(619.2);
                hBox.setStyle("-fx-background-color: #2a2a2a");
                VBox vBox = new VBox();
                vBox.setPrefWidth(219.2);
                vBox.setSpacing(150 / 10);
                vBox.setPadding(new Insets(0, 0, 0, 15));

                Label questionSetLabel = new Label(questionSet.getQuestionSetCode());
                questionSetLabel.setStyle(
                        "-fx-font-size: 18; -fx-text-fill: white; "
                );
                Button takeQuestionSetButton = new Button("Take Question Set");
                takeQuestionSetButton.setOnAction(ev -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("shortAnsQuestion-view.fxml"));
                    AnchorPane questionView = null;
                    try {
                        questionView = loader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    ShortAnsQuestionViewController controller = loader.getController();
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
                Button addQuestionButton = new Button("Add Question");
                addQuestionButton.setOnAction(ev -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("addshortAnswerQuestion.fxml"));
                    AnchorPane addQuestionView = null;
                    try {
                        addQuestionView = loader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    addshortAnswerQuestion controller = loader.getController();
                    controller.PaperCode = questionSet.getPaperCode();
                    controller.QuestionSetCode = questionSet.getQuestionSetCode();
                    contentPane.getChildren().clear();
                    contentPane.getChildren().add(addQuestionView);
                    // Make it resize with contentPane
                    AnchorPane.setTopAnchor(addQuestionView, 0.0);
                    AnchorPane.setBottomAnchor(addQuestionView, 0.0);
                    AnchorPane.setLeftAnchor(addQuestionView, 0.0);
                    AnchorPane.setRightAnchor(addQuestionView, 0.0);
                });
                Button editSetButton = new Button("Edit Set");
                editSetButton.setOnAction(ev -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("editset-view.fxml"));
                    AnchorPane editSetView = null;
                    try {
                        editSetView = loader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    EditsetViewController controller = loader.getController();
                    controller.contentPane = contentPane;
                    controller.paperCode = questionSet.getPaperCode();
                    controller.setQuestionSet(questionSet);
                    contentPane.getChildren().clear();
                    contentPane.getChildren().add(editSetView);

                    // Make it resize with contentPane
                    AnchorPane.setTopAnchor(editSetView, 0.0);
                    AnchorPane.setBottomAnchor(editSetView, 0.0);
                    AnchorPane.setLeftAnchor(editSetView, 0.0);
                    AnchorPane.setRightAnchor(editSetView, 0.0);
                });

                vBox.getChildren().addAll(questionSetLabel, takeQuestionSetButton, addQuestionButton, editSetButton);

                double prevScore = questionSet.getPreviousScore();
                double prevScoreFraction = prevScore / 100.0;
                Color prevScoreBarColor = Color.RED.interpolate(Color.LIMEGREEN, prevScoreFraction);
                Tile prevScoreChart = TileBuilder.create()
                        .skinType(Tile.SkinType.CIRCULAR_PROGRESS)
                        .title("Previous Score")
                        .value(prevScore)
                        .maxValue(100)
                        .barColor(prevScoreBarColor)
                        .prefSize(180, 180)
                        .build();
                prevScoreChart.setTitleAlignment(TextAlignment.CENTER);
                prevScoreChart.setTextSize(Tile.TextSize.BIGGER);
                double averageScore = questionSet.getAverageScore();
                double aveScoreFraction = averageScore / 100.0;
                Color aveScoreBarColor = Color.RED.interpolate(Color.LIMEGREEN, aveScoreFraction);
                Tile aveScoreChart = TileBuilder.create()
                        .skinType(Tile.SkinType.CIRCULAR_PROGRESS)
                        .title("Average Score")
                        .value(averageScore)
                        .maxValue(100)
                        .barColor(aveScoreBarColor)
                        .prefSize(180, 180)
                        .build();
                aveScoreChart.setTitleAlignment(TextAlignment.CENTER);
                aveScoreChart.setTextSize(Tile.TextSize.BIGGER);
                hBox.getChildren().addAll(vBox, prevScoreChart, aveScoreChart);
                paperViewRoot.setPadding(new Insets(0, 20, 0, 20));
                paperViewRoot.getChildren().add(hBox);
                paperViewRoot.setSpacing(10);
            }
        }
    }

    public boolean addSet(String setName, String setType) {

        QuestionSet questionSet = new QuestionSet(setType, paperCode, setName);
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
