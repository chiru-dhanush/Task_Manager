package taskManagerPkg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskBean {
	private String name;
	private String desc;
	private String to_Date;
	private int priority;
	private String tags;
	
	public TaskBean(String name,String desc,int priority,String tags,String to_Date) {
		if(name==null || name.trim().equals("")) {
			throw new IllegalArgumentException("Enter proper name");
		}
		if(desc==null || desc.trim().equals("")) {
			throw new IllegalArgumentException("Enter description");
		}
		if(to_Date==null) {
			throw new IllegalArgumentException("Enter Date properly");
		}
		if(tags==null || tags.trim().equals("")) {
			throw new IllegalArgumentException("Enter tags");
		}
		if(priority<1 || priority>10) {
			throw new IllegalArgumentException("Enter priority between range 1-10");
		}
		this.name=name;
		this.desc=desc;
		this.to_Date=to_Date;
		this.priority=priority;
		this.tags=tags;	
	}
	//setters
	public void setName(String name) {
		if(name==null || name.trim().equals("")) {
			throw new IllegalArgumentException("Enter proper name");
		}
		this.name=name;
	}
	public void setDesc(String desc) {
		if(desc==null || desc.trim().equals("")) {
			throw new IllegalArgumentException("Enter description");
		}
		this.desc=desc;
	}
	public void setDate(String to_Date) throws PastDateException {
		if(to_Date==null || to_Date.trim().equals("")) {
			throw new IllegalArgumentException("Enter Date properly");
		}
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date dt=sdf.parse(to_Date);
			if(dt.before(new Date())) {
				throw new PastDateException("Kindly enter date of oncoming dates");
			}
			else {
				String formattedDate=sdf.format(dt);
				this.to_Date=formattedDate;
			}
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		
	}
	public void setPriority(int priority) {
		if(priority<1 || priority>10) {
			throw new IllegalArgumentException("Enter priority between range 1-10");
		}
		this.priority=priority;
	}
	public void setTags(String tags) {
		if(tags==null || tags.trim().equals("")) {
			throw new IllegalArgumentException("Enter tags");
		}
		this.tags=tags;
	}
	
	//getters
	public String getName() {
		return name;
	}
	public String getDesc() {
		return desc;
	}
	public String getToDate() {
		return to_Date;
	}
	public int getPriority() {
		return priority;
	}
	public String getTags() {
		return tags;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof TaskBean) {
			TaskBean tb=(TaskBean)obj;
			if(this.name.equals(tb.getName()) && this.desc.equals(tb.getDesc())
				&& this.to_Date.equals(tb.getToDate()) && this.priority==tb.getPriority()
				&& this.tags.equals(tb.getTags())) {
					return true;
			}			
		}
		return false;
	}
	
	public int hashCode() {
		return (name+desc+priority+tags+to_Date).hashCode();
	}
	
	public String toString() {
		return name+"::"+desc+"::"+priority+"::"+tags+"::"+to_Date;
	}
}
