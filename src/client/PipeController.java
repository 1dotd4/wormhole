import java.io.*;
import java.util.*;

public class PipeController {
  static private FileOutputStream out;
  static private FileInputStream in;

  public PipeController(String prefix) {
    String FIFOOut = prefix + "_in";
    String FIFOIn  = prefix + "_out";
    try {
      in = new FileInputStream(FIFOIn);
    } catch (Exception ex){
      System.out.println(ex);
    }
    try {
      out = new FileOutputStream(FIFOOut);
    } catch (Exception ex){
      System.out.println(ex);
    }
  }

  private byte[] write_read(int op, byte[] data){
    byte[] buf = new byte[17];
    buf[0] = (byte) op;
    for (int i = 0; i < 16; i++) {
      buf[i + 1] = data[i];
    }
    try {
      out.write(buf);
      for(int i = 0; i < 17; i++) {
        buf[i] = (byte)in.read();
      }
    } catch (Exception ex){
      System.out.println(ex);
    }
    for (int i = 0; i < 16; i++) {
      data[i] = buf[i + 1];
    }
    return data;
  }

  public byte[] decrypt(byte[] data) { return write_read(0, data); }
  public byte[] encrypt(byte[] data) { return write_read(1, data); }
  public void set_vec(byte[] data) { write_read(2, data); }
  public void set_key(byte[] data) { write_read(3, data); }

  public void close() {
    write_read(4, new byte[16]);
    try {
      in.close(); 
    } catch (Exception ex) {
      System.out.println(ex);
    }
    try {
      out.close();
    } catch (Exception ex) {
      System.out.println(ex);
    }
  }

  public static void main(String[] args){
    PipeController p = new PipeController("test");
    byte[] buf = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    byte[] iv = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    byte[] key = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    p.set_key(key);
    p.set_vec(iv);
    buf = p.encrypt(buf);
    System.out.println(Arrays.toString(buf));
    buf = p.decrypt(buf);
    System.out.println(Arrays.toString(buf));
    // p.write0();
  }
};
