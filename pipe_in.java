import java.io.*;
import java.util.*;

public class pipe_in {
	static private String FIFOIn;
    	static private String FIFOOut;
    	static FileOutputStream out;
    	static FileInputStream in;

	static public void pipe_set(int id){
		FIFOOut = "Java2C"+String.format("%03d",id);
		FIFOIn  = "C2Java"+String.format("%03d",id);
		try {
	    		out = new FileOutputStream(FIFOOut);
		} catch (Exception ex){
	    		System.out.println(ex);
		}
	
 		try {
	    		in = new FileInputStream(FIFOIn);
		} catch (Exception ex){
	    		System.out.println(ex);
		}
	};

	static public void write_read(byte[] buf){
		try {
	   		out.write(buf);
			for(int i=0;i<17;i++) 
				buf[i]=(byte)in.read();
		} catch (Exception ex){
	    		System.out.println(ex);
		}

	};
		


	public static void main(String[] args){
		pipe_set(0);
		byte[] buf = {2,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
		write_read(buf);
			
		System.out.println(Arrays.toString(buf));
		//write0();

	};
};
