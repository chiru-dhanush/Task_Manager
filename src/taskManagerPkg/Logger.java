package taskManagerPkg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	
	public static final int LOW=1;
	public static final int MEDIUM=2;
	public static final int HIGH=3;
	public static final int CRITICAL=4;
	
	private static BufferedWriter bw;
	static {
		try {
			bw=new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"\\TaskManagerLog.log"));
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Logger log;
	private Logger() {
		
	}
	
	public static Logger getInstance() {
		if(log==null) {
			log=new Logger();
		}
		return log;
	}
	
	public void log(String data,int priority) {
		new Thread(new Runnable() {
			public void run() {
				String formattedMessage=formatMessage(data,priority);
				try {
					bw.write(formattedMessage);
					bw.newLine();
					bw.flush();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public String formatMessage(String data,int priority) {
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		String dateStr=sdf.format(new Date());
		return String.format("%s , %s , %s ",data,priority,dateStr);
	}
	
	public static void close() {
		if(bw!=null) {
			try {
				bw.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
