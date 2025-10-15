package com.manankakkar.smartpaint;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AiMode {
    private final OpenAiPaint openAi;
    private PaintModel model;

    public AiMode(OpenAiPaint openAi) {
        this.openAi = openAi;
        this.model = new PaintModel();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Draw with AI!");
        TextArea inputArea = new TextArea();
        inputArea.setEditable(true);
        inputArea.setWrapText(true);
        inputArea.setPromptText("Describe what you want AI to draw: ");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setPromptText("AI response: ");

        HBox buttonBar = gethBox(inputArea, outputArea, stage);
        buttonBar.setPadding(new Insets(10));
        buttonBar.setStyle("-fx-alignment: center-right;");

        VBox layout = new VBox(10, new Label("AI Request:"), inputArea,
                new Label("AI Response:"), outputArea, buttonBar);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private HBox gethBox(TextArea inputArea, TextArea outputArea, Stage stage) {
        Button submitButton = new Button("Draw");
        submitButton.setDefaultButton(true);
        submitButton.setOnAction(e -> {
           String userPrompt = inputArea.getText().trim();
           if (userPrompt.isEmpty()) {
               outputArea.appendText("Please enter your request first!\n");
           }
           else {
               outputArea.setText("Generating...");
               new Thread(() -> {
                   try {
                       String result = openAi.callWithValidation(userPrompt, "GPTFILE.txt", this.model);
                       javafx.application.Platform.runLater(() -> outputArea.setText("Generated GPTFILE.txt! \nPlease check your home directory to view the outcome!"));
                   } catch (Exception ex) {
                       javafx.application.Platform.runLater(() ->
                               outputArea.setText("Error: " + ex.getMessage()));
                   }
               }).start();
           }
        });

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> stage.close());

        HBox buttonBar = new HBox(10, submitButton, closeButton);
        return buttonBar;
    }
}
