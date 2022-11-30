import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUploadController {
  private File fileToUpload;
  private Label lableToUpdate;

  public FileUploadController(File fileToUpload, Label lableToUpdate) {
    this.fileToUpload = fileToUpload;
    this.lableToUpdate = lableToUpdate;
  }

  public boolean start() {
    try {
      byte[] data = Files.readAllBytes(Paths.get(this.fileToUpload.getPath()));
      // do stuff
    } catch (IOException e) {
      return false;
    }
    return true; // false if error
  }
}
