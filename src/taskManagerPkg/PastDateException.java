package taskManagerPkg;

public class PastDateException extends Exception{
	public PastDateException() {	
	}
	public PastDateException(String msg) {
		super(msg);
	}
}
