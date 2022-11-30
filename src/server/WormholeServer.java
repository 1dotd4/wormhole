import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;
import java.net.ServerSocket;

public class WormholeServer {
  public void launch(){
    try {
      ServerSocket servSock = new ServerSocket(31337);
      while (true) {
        Socket s = servSock.accept();
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        DataInputStream in = new DataInputStream(s.getInputStream());

        // read write byte here
        out.close();
        in.close();
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
