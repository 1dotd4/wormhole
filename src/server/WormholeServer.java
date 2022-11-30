import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;

import java.net.Socket;
import java.net.ServerSocket;

public class WormholeServer {
  public void launch(){
    try {
      ServerSocket servSock = new ServerSocket(31337);
      while (true) {
        Socket sock = servSock.accept();
        DataOutputStream outStream = new DataOutputStream(sock.getOutputStream());
        DataInputStream inStream = new DataInputStream(sock.getInputStream());
        // BufferedReader inReader = new DataOutputStream(inStream);
        // read write byte here
        boolean shouldContinue = true;
        int state = 1;
        String s;
        String fileName = "";
        int fileSize = 0;
        System.out.println("Connected");
        try {
          while (shouldContinue) {
            switch (state) {
              case 1:
                // 1. S <- C
                s = inStream.readUTF();
                if (!s.equals("Hello there")) {
                  shouldContinue = false;
                  break;
                }
                System.out.println("Got Hello there");
                state++;
                break;
              case 2:
                // 2. S -> C
                outStream.writeUTF("General Kenobi");
                state++;
                break;
              case 3:
                // 3. S <- C
                s = inStream.readUTF();
                if (s.charAt(0) != 'U') {
                  shouldContinue = false;
                  break;
                }
                String[] splitted = s.split(":");
                if (splitted.length != 3) {
                  shouldContinue = false;
                  break;
                }
                fileName = splitted[1];
                fileSize = Integer.parseInt(splitted[2]);
                if (fileSize > 2 * 1024 * 1024) {
                  // maxFile 2MB
                  outStream.writeUTF(String.format("NO"));
                  System.out.println("File is not okay");
                  shouldContinue = false;
                  break;
                }
                System.out.println("File is ok");
                outStream.writeUTF(String.format("OK"));
                state++;
                break;
              case 4:
                System.out.println(String.format("%s: %d", fileName, fileSize));
                state++;
                break;
              default:
                shouldContinue = false;
                break;
            }

          }
        } catch (IOException e) {

        }

        outStream.close();
        inStream.close();
        sock.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    WormholeServer s = new WormholeServer();
    s.launch();
  }
}
