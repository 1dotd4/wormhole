import java.io.StringWriter;
import java.io.PrintWriter;

import java.io.File;

import java.util.ArrayList; 
import java.util.List;
import java.util.Arrays; 
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.FileChooser;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import javafx.scene.paint.Color; 
import javafx.scene.Group; 


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


public class insertController {
    @FXML private Button submitButton;
    @FXML private Label statusLabel;
    @FXML private ProgressBar progressBar;

    @FXML protected void chooseFileToUpload(ActionEvent event) throws Exception {
      FileChooser fileChooserPane = new FileChooser();
      fileChooserPane.setTitle("Choose file to upload.");
      File chosenFile = fileChooserPane.showOpenDialog(statusLabel.getScene().getWindow());
      if (chosenFile == null) {
        return;
      }

      submitButton.setDisable(true);
      FileUploadController controller = new FileUploadController(chosenFile, statusLabel);
      if (!controller.start()) {
        // signal error
        Alert a = new Alert(AlertType.INFORMATION);
        a.setContentText(String.format("Couldn't upload file \"%s\".", chosenFile.getName()));
        Stage alertStage = (Stage) a.getDialogPane().getScene().getWindow();
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.setAlwaysOnTop(true);
        a.showAndWait();
      }
      submitButton.setDisable(false);
    }
}
