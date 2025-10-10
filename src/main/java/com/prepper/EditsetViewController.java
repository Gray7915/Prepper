package com.prepper;

import com.data.FlashQuestionDAO;
import com.data.JdbiDAOFactory;
import com.data.QuestionDAO;
import com.domain.FlashQuestion;
import com.domain.Question;
import com.domain.QuestionSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class EditsetViewController {
    @FXML
    private TextField searchBox;
    @FXML
    VBox container;
    @FXML
    private Button saveChanges;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    AnchorPane contentPane;
    @FXML
    HBox saveContainer;
    private Collection<Question> questionSet;
    private HashSet<GridPane> questionViewSet = new HashSet<>();
    private QuestionDAO questionDAO;
    private FlashQuestionDAO flashQuestionDAO;
    public String paperCode = "";
    private Collection<FlashQuestion> flashQuestionSet;
    private Collection<FlashQuestion> newflashQuestionSet = new ArrayList<>();
    private Collection<String> categories;

    @FXML
    public void initialize() {
        questionDAO = JdbiDAOFactory.getQuestionDAO();
        flashQuestionDAO = JdbiDAOFactory.getFlashQuestionDAO();
        scrollPane.setFitToHeight(false);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        container.setPadding(new Insets(5, 15, 0, 15));
        searchBox.setOnKeyTyped(e -> {
            Search(searchBox.getText().trim());
        });
        saveChanges.setOnAction(e -> {
            try {
                saveSet();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void setQuestionSet(QuestionSet Set) {
        QuestionDAO questionDAO = JdbiDAOFactory.getQuestionDAO();

        this.questionSet = questionDAO.getQuestionsForSet(Set.getPaperCode(), Set.getQuestionSetCode());
        categories = flashQuestionDAO.getFlashQuestionCategories(Set.getPaperCode(), Set.getQuestionSetCode());
        flashQuestionSet = flashQuestionDAO.getFlashQuestionForSet(Set.getPaperCode(), Set.getQuestionSetCode());
        Button allButton = new Button("All");
        allButton.setOnAction(e -> {
            Search("");
        });
        saveContainer.getChildren().add(allButton);
        for (String c : categories) {
            Button button = new Button(c);
            button.setOnAction(actionEvent -> {
                showCategory(c);
            });
            saveContainer.getChildren().add(button);
        }
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(15);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(5);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(30);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(40);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(20);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(20);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(20);
        RowConstraints row4 = new RowConstraints();
        row4.setPercentHeight(20);
        RowConstraints row5 = new RowConstraints();
        row5.setPercentHeight(20);

        for (Question question : questionSet) {
            GridPane gridPane = new GridPane();
            gridPane.setId(question.getQuestion());
            gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
            gridPane.getRowConstraints().addAll(row1, row2, row3, row4, row5);
            //First Column
            Label title = new Label(question.getQuestion());
            gridPane.add(title, 0, 0);
            gridPane.add(new Label("Answer A:"), 0, 1);
            gridPane.add(new Label("Answer B:"), 0, 2);
            gridPane.add(new Label("Answer C:"), 0, 3);
            gridPane.add(new Label("Answer D:"), 0, 4);

            //Second Column
            CheckBox QuesACheckBox = new CheckBox();
            CheckBox QuesBCheckBox = new CheckBox();
            CheckBox QuesCCheckBox = new CheckBox();
            CheckBox QuesDCheckBox = new CheckBox();

            gridPane.add(new Label("Answer"), 1, 0);
            Map<String, Runnable> actions = new HashMap<>();
            QuesACheckBox.setSelected(question.getAnswer().equals(question.getA()));
            QuesBCheckBox.setSelected(question.getAnswer().equals(question.getB()));
            QuesCCheckBox.setSelected(question.getAnswer().equals(question.getC()));
            QuesDCheckBox.setSelected(question.getAnswer().equals(question.getD()));
            QuesACheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    question.setAnswer(question.getA());
                    QuesBCheckBox.setSelected(false);
                    QuesCCheckBox.setSelected(false);
                    QuesDCheckBox.setSelected(false);
                }
            });
            QuesBCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    question.setAnswer(question.getB());
                    QuesACheckBox.setSelected(false);
                    QuesCCheckBox.setSelected(false);
                    QuesDCheckBox.setSelected(false);
                }
            });
            QuesCCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    question.setAnswer(question.getC());
                    QuesACheckBox.setSelected(false);
                    QuesBCheckBox.setSelected(false);
                    QuesDCheckBox.setSelected(false);
                }
            });
            QuesDCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    question.setAnswer(question.getD());
                    QuesACheckBox.setSelected(false);
                    QuesBCheckBox.setSelected(false);
                    QuesCCheckBox.setSelected(false);
                }
            });

            gridPane.add(QuesACheckBox, 1, 1);
            gridPane.add(QuesBCheckBox, 1, 2);
            gridPane.add(QuesCCheckBox, 1, 3);
            gridPane.add(QuesDCheckBox, 1, 4);

            //Third Column
            TextField textA = new TextField(question.getA());
            textA.textProperty().addListener((obs, oldValue, newValue) -> {
                question.setA(newValue); // set value in the Question object
            });
            gridPane.add(textA, 2, 1);
            TextField textB = new TextField(question.getB());
            textB.textProperty().addListener((obs, oldValue, newValue) -> {
                question.setB(newValue);
            });
            gridPane.add(textB, 2, 2);
            TextField textC = new TextField(question.getC());
            textC.textProperty().addListener((obs, oldValue, newValue) -> {
                question.setC(newValue);
            });
            gridPane.add(textC, 2, 3);
            TextField textD = new TextField(question.getD());
            textD.textProperty().addListener((obs, oldValue, newValue) -> {
                question.setD(newValue);
            });
            gridPane.add(textD, 2, 4);

            //Fourth Column
            ImageView questionImage = new ImageView();
            questionImage.setPickOnBounds(true);
            questionImage.setPreserveRatio(true);
            questionImage.setFitHeight(200);
            questionImage.setFitWidth(250);
            HBox questionImageWrapper = new HBox();
            questionImageWrapper.setAlignment(Pos.CENTER);
            questionImageWrapper.getChildren().add(questionImage);
            if (question.getImage() != null) {
                questionImage.setImage(new Image(new ByteArrayInputStream(question.getImage())));
                questionImage.setVisible(true);
            }
            Button selectImageButton = new Button("Change Image"), saveChanges;
            selectImageButton.setOnAction(e -> {
                questionImage.setImage(new Image(chooseImagePath()));

            });
            gridPane.add(selectImageButton, 3, 0);
            GridPane.setHalignment(selectImageButton, HPos.CENTER);
            gridPane.add(questionImageWrapper, 3, 1);
            GridPane.setRowSpan(questionImageWrapper, 4);
            gridPane.getStyleClass().add("grid-pane-sidebar");
            questionViewSet.add(gridPane);
            container.getChildren().add(gridPane);
        }
    }

    public void Search(String search) {
        search.toLowerCase();
        List<GridPane> gridPaneList = new ArrayList<>();
        for (GridPane item : questionViewSet) {
            if (!item.getId().toLowerCase().contains(search)) {
                item.setVisible(false);
            } else if (item.getId().toLowerCase().contains(search)) {
                item.setVisible(true);
                gridPaneList.add(item);
            } else if (searchBox.getText().toLowerCase().trim().length() == 0) {
                item.setVisible(true);
                gridPaneList.add(item);
            }
        }
        container.getChildren().clear();
        container.getChildren().addAll(gridPaneList);

    }

    public String chooseImagePath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(saveChanges.getScene().getWindow());
        if (selectedFile != null) {
            return selectedFile.toURI().toString();
        }
        return null;
    }


    public void saveSet() throws IOException {
        try {
            for (Question question : questionSet) {
                questionDAO.updateQuestion(question);
            }
            for (FlashQuestion flashQuestion : flashQuestionSet) {
                flashQuestionDAO.updateFlashQuestion(flashQuestion);
            }
            for (FlashQuestion flashQuestion : newflashQuestionSet) {
                System.out.println(flashQuestion.getQuestion() + "  " + flashQuestion.getAnswer());
                flashQuestionDAO.insertFlashQuestion(flashQuestion);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
    }

    //This shows only flashQuestions belonging to a given category.
    public void showCategory(String category) {
        container.getChildren().clear();
        for (FlashQuestion flashQuestion : flashQuestionSet) {
            if (flashQuestion.getCategory().equals(category)) {

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
                row2.setPercentHeight(70);
                RowConstraints row3 = new RowConstraints();
                row3.setPercentHeight(10);

                GridPane gridPane = new GridPane();
                gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
                gridPane.getRowConstraints().addAll(row1, row2, row3);
                gridPane.setMinHeight(200);
                gridPane.setPrefHeight(200);
                gridPane.setMaxHeight(200);
                Label questionLabel = new Label("Question");
                gridPane.add(questionLabel, 0, 0);
                TextArea questionText = new TextArea(flashQuestion.getQuestion());
                questionText.setPrefRowCount(1);
                questionText.setWrapText(true);
                questionText.setMaxHeight(150);
                questionText.setScrollTop(Double.MIN_VALUE);
                questionText.setScrollLeft(Double.MIN_VALUE);;
                questionText.textProperty().addListener((obs, oldValue, newValue) -> {
                    flashQuestion.setQuestion(newValue); // set value in the Question object
                });
                gridPane.add(questionText, 0, 1);

                Label answerLabel = new Label("Answer");
                gridPane.add(answerLabel, 1, 0);
                TextArea answerText = new TextArea(flashQuestion.getAnswer());
                answerText.setPrefRowCount(1);
                answerText.setWrapText(true);
                answerText.setMaxHeight(150);
                answerText.setScrollTop(Double.MIN_VALUE);
                answerText.setScrollLeft(Double.MIN_VALUE);
                answerText.textProperty().addListener((obs, oldValue, newValue) -> {
                    flashQuestion.setAnswer(newValue);
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
                if (flashQuestion.getImage() != null) {
                    questionImage.setImage(new Image(new ByteArrayInputStream(flashQuestion.getImage())));
                    questionImage.setVisible(true);
                }
                Button selectImageButton = new Button("Change Image"), saveChanges;
                selectImageButton.setOnAction(e -> {
                    String imagePath = chooseImagePath();
                    questionImage.setImage(new Image(imagePath));

                    try {
                        URI uri = URI.create(imagePath);
                        Path path = Paths.get(uri);
                        flashQuestion.setImage(Files.readAllBytes(path));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                Button createAnother = new Button("create Another");
                createAnother.setOnAction(event -> {
                    createQuestion(flashQuestion.getCategory(), flashQuestion.getQuestionSetCode(), flashQuestion.getPaperCode());
                });
                gridPane.add(createAnother, 3, 1);
                Button deleteQuestion = new Button("delete");
                deleteQuestion.setOnAction(event -> {
                    flashQuestionSet.remove(flashQuestion);
                    container.getChildren().remove(gridPane);
                });
                gridPane.add(deleteQuestion, 3, 2);
                gridPane.add(questionImageWrapper, 2, 1);
                gridPane.add(selectImageButton, 3, 0);
                gridPane.getStyleClass().add("grid-pane-sidebar");
                container.getChildren().add(gridPane);
            }
        }
    }

    public void createQuestion(String category, String questionSet, String paperCode) {
        FlashQuestion newQuestion = new FlashQuestion();
        newflashQuestionSet.add(newQuestion);
        newQuestion.setCategory(category);
        newQuestion.setQuestionSetCode(questionSet);
        newQuestion.setPaperCode(paperCode);
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
        row2.setPercentHeight(70);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(10);


        GridPane gridPane = new GridPane();
        gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
        gridPane.getRowConstraints().addAll(row1, row2, row3);
        gridPane.setPrefHeight(200);
        gridPane.setMinHeight(200);
        gridPane.setMaxHeight(200);
        Label questionLabel = new Label("Question");
        gridPane.add(questionLabel, 0, 0);
        TextArea questionText = new TextArea();
        questionText.setPrefRowCount(1);
        questionText.setWrapText(true);
        questionText.setMaxHeight(150);
        questionText.setScrollTop(Double.MIN_VALUE);
        questionText.setScrollLeft(Double.MIN_VALUE);;
        questionText.textProperty().addListener((obs, oldValue, newValue) -> {
            newQuestion.setQuestion(newValue); // set value in the Question object
        });
        gridPane.add(questionText, 0, 1);

        Label answerLabel = new Label("Answer");
        gridPane.add(answerLabel, 1, 0);
        TextArea answerText = new TextArea ();
        answerText.setPrefRowCount(1);
        answerText.setWrapText(true);
        answerText.setMaxHeight(150);
        answerText.setScrollTop(Double.MIN_VALUE);
        answerText.setScrollLeft(Double.MIN_VALUE);
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

        Button selectImageButton = new Button("Select Image"), saveChanges;
        selectImageButton.setOnAction(e -> {
            String imagePath = chooseImagePath();
            questionImage.setImage(new Image(imagePath));
            try {
                URI uri = URI.create(imagePath);
                Path path = Paths.get(uri);
                newQuestion.setImage(Files.readAllBytes(path));
                selectImageButton.setText("Change Image");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        Button createAnother = new Button("create Another");
        createAnother.setOnAction(event -> {
            createQuestion(category, questionSet, paperCode);
        });
        gridPane.add(createAnother, 3, 1);
        Button deleteQuestion = new Button("delete");
        deleteQuestion.setOnAction(event -> {
            flashQuestionSet.remove(newQuestion);
            container.getChildren().remove(gridPane);
        });
        gridPane.add(deleteQuestion, 3, 2);
        gridPane.add(questionImageWrapper, 2, 1);
        gridPane.add(selectImageButton, 3, 0);
        gridPane.getStyleClass().add("grid-pane-sidebar");
        container.getChildren().add(gridPane);
    }
}
