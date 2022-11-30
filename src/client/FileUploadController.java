import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.net.Socket;

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
      // TODO: pigliare sto indirizzo da qualche parte
      Socket sock = new Socket("127.0.0.1", 31337);
      DataOutputStream outStream = new DataOutputStream(sock.getOutputStream());
      DataInputStream inStream = new DataInputStream(sock.getInputStream());
      // read write byte here
      boolean shouldContinue = true;
      int state = 1;
      String s;
      System.out.println("Connected");
      while (shouldContinue) {
        switch (state) {
          case 1:
            // C -> S
            outStream.writeUTF("Hello there");
            state++;
            break;
          case 2:
            // C <- S
            s = inStream.readUTF();
            if (!s.equals("General Kenobi")) {
              shouldContinue = false;
              break;
            }
            System.out.println("Got General Kenobi");
            state++;
            break;
          case 3:
            // C -> S
            outStream.writeUTF(
                String.format("U:%s:%d:"
                  , this.fileToUpload.getName()
                  , this.fileToUpload.length()));
            state++;
            break;
          case 4:
            // C <- S
            s = inStream.readUTF();
            if (!s.equals("OK")) {
              shouldContinue = false;
              System.out.println("Got No");
              break;
            }
            System.out.println("Got Okay");
            state++;
            break;
          // case 5:
          //   break;
          default:
            shouldContinue = false;
            break;
        }
      }

      outStream.close();
      inStream.close();
      sock.close();
      // do stuff
    } catch (IOException e) {
      return false;
    }
    return true; // false if error
  }
}
