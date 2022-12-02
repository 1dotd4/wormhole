import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;

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
      // byte[] data = Files.readAllBytes(Paths.get(this.fileToUpload.getPath()));
      FileInputStream fileIn = new FileInputStream(this.fileToUpload);
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
            // System.out.println("Got Okay");
            this.lableToUpdate.setText("Got Okay");
            state++;
            break;
          case 5:
            byte[] tmpBuf = new byte[16];
            int byteWrote = 0;
            while (byteWrote < this.fileToUpload.length() + (this.fileToUpload.length() + 16) % 16) {
            // while (byteWrote < this.fileToUpload.length()) {
              byteWrote += fileIn.read(tmpBuf);
              if (byteWrote >= this.fileToUpload.length()) { // - (this.fileToUpload.length() % 16)) {
                // Last block
                // Here we pad so we can encrypt correctly the message.
                System.out.println(
                    String.format(
                      "Should write last %d bytes...",
                      (this.fileToUpload.length() % 16)));
                int off = (int)(this.fileToUpload.length()) % 16;
                int bytesToPad = 16 - off;
                for (int i = 0; i < bytesToPad; i++) {
                  tmpBuf[off + i] = (byte) bytesToPad;
                }

                byteWrote += bytesToPad;
              }
              // cifra
              outStream.write(tmpBuf);
              this.lableToUpdate.setText(
                  String.format("Uploading... %d%%",
                  (byteWrote * 100 / this.fileToUpload.length())));
            }
            outStream.close();
            state++;
            break;
          default:
            // System.out.println("File uploeded.");
            this.lableToUpdate.setText("File uploaded.");
            shouldContinue = false;
            break;
        }
      }

      outStream.close();
      inStream.close();
      sock.close();
      // do stuff
    } catch (IOException e) {
      System.out.println(e);
      return false;
    }
    return true; // false if error
  }
}
