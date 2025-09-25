package com.prepper;

import com.data.JdbiDAOFactory;
import com.data.PaperDAO;
import com.domain.Paper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class HomeViewController {
    @FXML
    private Button AddPaperButton;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private VBox sidebar;
    private PaperDAO paperDAO;

    @FXML
    public void initialize() {
        paperDAO = JdbiDAOFactory.getPaperDAO();
        for (Paper paper : paperDAO.getPapers()) {
            sidebar.getChildren().addLast(PaperSelectButton(paper.getPaperCode()));
        }
        AddPaperButton.setOnAction(e -> {
            Stage popup = new Stage();
            popup.setTitle("Enter Paper Code");

            TextField input = new TextField();
            input.setPromptText("Enter Paper Code");
            Button okButton = new Button("Enter");
            Button cancelButton = new Button("Cancel");
            Runnable handleInput = () -> {
                String PaperCode = input.getText().trim();
                boolean ValidPaperCode = addPaper(PaperCode);

                if (!PaperCode.isEmpty() && ValidPaperCode) {

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("paper-view.fxml"));
                        Node paperView = loader.load();
                        PaperViewController controller = loader.getController();
                        controller.setPaperCode(PaperCode);
                        contentPane.getChildren().clear();
                        contentPane.getChildren().add(paperView);
                        controller.setContentPane(contentPane);
                        popup.close();
                        // Make it resize with contentPane
                        AnchorPane.setTopAnchor(paperView, 0.0);
                        AnchorPane.setBottomAnchor(paperView, 0.0);
                        AnchorPane.setLeftAnchor(paperView, 0.0);
                        AnchorPane.setRightAnchor(paperView, 0.0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
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

    public boolean addPaper(String paperCode) {
        Paper paper = new Paper(paperCode);
        Collection<Paper> papers = paperDAO.getPapers();
        HashSet<Paper> paperSet = new HashSet<>(papers);
        if (paperSet.contains(paper)) {
            return false;
        } else {
            paperDAO.createPaper(paper);
            return true;
        }
    }

    AnchorPane getContentPane() {
        return contentPane;
    }

    public Button PaperSelectButton(String paperCode) {
        Button button = new Button();
        button.setText(paperCode);
        button.getStyleClass().add("label-style");
        button.setPrefWidth(200);
        button.setPrefHeight(50);
        VBox.setMargin(button, new Insets(15, 25, 0, 25));
        button.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("paper-view.fxml"));
                Node paperView = loader.load();
                PaperViewController controller = loader.getController();
                controller.setPaperCode(paperCode);
                contentPane.getChildren().clear();
                contentPane.getChildren().add(paperView);
                controller.setContentPane(contentPane);
                // Make it resize with contentPane
                AnchorPane.setTopAnchor(paperView, 0.0);
                AnchorPane.setBottomAnchor(paperView, 0.0);
                AnchorPane.setLeftAnchor(paperView, 0.0);
                AnchorPane.setRightAnchor(paperView, 0.0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        return button;
    }
}