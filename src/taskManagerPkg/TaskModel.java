package taskManagerPkg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class TaskModel {

	public static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	
	public static boolean isValidCategoryName(String categoryName) {
		
		if(categoryName==null) {
			return false;
		}
		if(categoryName.trim().equals("")) {
			return false;
		}
		if(!Character.isLetter(categoryName.charAt(0))) {
			return false;
		}
		if(categoryName.split(" ").length>1) {
			return false;
		}
		for(char ch:categoryName.toCharArray()) {
			if(!Character.isLetterOrDigit(ch)) {
				return false;
			}
		}
		return true;
	}

	public static boolean createCategoryFile(String categoryName) {
		
	    File categoryFile = new File(Constants.folderPath + "\\" + categoryName + ".todo");
	    if (!categoryFile.exists()) {
	        try {
	            boolean isFileCreated = categoryFile.createNewFile();
	            if (isFileCreated) {
	            	return true;
	            }
	        } 
	        catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }
	    } 

		return false;
	}

	public static boolean isValidTaskName(String taskName) {
		if(isValidCategoryName(taskName)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean isValidPriority(int num) {
		if(num>0 && num<11) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean isValidDesc(String desc) {
		if(desc==null || desc.trim().equals("")) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static boolean isValidToDate(String strDate) {
		try {
			
			Date dt=sdf.parse(strDate);
			String formattedDate=sdf.format(dt);
			
			if(!strDate.equals(formattedDate)) {	//to check correct format
				return false;
			}
			if(dt.before(new Date())) {				//to check given date is past
				return false;
			}
			return true;
		}
		catch(ParseException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static boolean isValidTags(String tags) {
		if(!isValidDesc(tags)) {
			return false;
		}
		return true;
	}
	
	public static void addTaskToCategory(TaskBean task,String categoryName) {
		File f=new File(Constants.folderPath+"\\"+categoryName+".todo");
		if(f.exists() && f.isFile()) {
			BufferedWriter bw=null;
			
			try {
				bw=new BufferedWriter(new FileWriter(f,true));
				bw.write(task.toString());
				bw.newLine();
				bw.flush();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			finally {
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
		
				
	}
	public static List<TaskBean> getTasks(String categoryName) {
		List<TaskBean> taskList = new ArrayList<>();
		BufferedReader br = null;
		String line;
		try {
			br= new BufferedReader(new FileReader(Constants.folderPath+"\\"+categoryName+".todo"));
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			while((line=br.readLine())!=null) {
				String[] sa = line.split("::");
				
				String name=sa[0];
				String desc=sa[1];
				int prior=Integer.parseInt(sa[2].trim());
				String tags=sa[3];
				Date dt=null;
				String strDate="";
				try {
					dt = sdf.parse(sa[4]);
					strDate=sdf.format(dt);
					
				} 
				catch (ParseException e) {
					e.printStackTrace();
				}
				TaskBean task=new TaskBean(name,desc,prior,tags,strDate);
				taskList.add(task);
			}
			return taskList;
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(br!=null) {
				try {
				
					br.close();
				}
			
				catch (Exception e) {
					// TODO: handle 
					e.printStackTrace();
					
				}
			}
		}
	}
	
	public static String setTaskNewName(String categoryName,String currTaskName,String newTaskName) {
		List<TaskBean>tasks=getTasks(categoryName);
		if(tasks.isEmpty()) {
			return "File is Empty";
		}
		for(TaskBean task:tasks) {
			if(task.getName().equals(currTaskName)) {
				task.setName(newTaskName);
				return saveUpdates(categoryName,tasks);
			}
		}
		return "Task not found!";
	}
	
	public static String setTaskNewDesc(String categoryName,String taskName,String desc) {
		List<TaskBean>tasks=getTasks(categoryName);
		for(TaskBean task:tasks) {
			if(task.getName().equals(taskName)) {
				task.setDesc(desc);
				return saveUpdates(categoryName,tasks);
			}
		}
		return "Task not found!";
	}
	public static String setTaskNewDueDate(String categoryName,String taskName,String date) throws PastDateException{
		List<TaskBean>tasks=getTasks(categoryName);
		for(TaskBean task:tasks) {
			if(task.getName().equals(taskName)) {
				
					task.setDate(date);
					return saveUpdates(categoryName,tasks);
			
				
			}
		}
		return "Task not found!";
	}
	public static String setTaskNewPriority(String categoryName,String taskName,int priority) {
		List<TaskBean>tasks=getTasks(categoryName);
		for(TaskBean task:tasks) {
			if(task.getName().equals(taskName)) {
				task.setPriority(priority);
				return saveUpdates(categoryName,tasks);
			}
		}
		return "Task not found!";
	}
	public static String setTaskNewTags(String categoryName,String taskName,String tags) {
		List<TaskBean>tasks=getTasks(categoryName);
		for(TaskBean task:tasks) {
			if(task.getName().equals(taskName)) {
				task.setTags(tags);
				return saveUpdates(categoryName,tasks);
			}
		}
		return "Task not found!";
	}
	
	//method to update changes after using setters
	private static String saveUpdates(String categoryName,List<TaskBean> tasks) {
		BufferedWriter bw=null;
		try {
			bw=new BufferedWriter(new FileWriter(Constants.folderPath+"\\"+categoryName+".todo"));
			
			for(TaskBean task:tasks) {
				bw.write(task.toString());
				bw.newLine();
			}
			return Constants.SUCCESS;
		}
		catch(IOException e) {
			e.printStackTrace();
			return Constants.FAILED;
		}
		finally {
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
	
	public static String deleteTask(String categoryName,String taskName) {
		List<TaskBean>tasks=getTasks(categoryName);
		for(TaskBean task:tasks) {
			if(task.getName().equals(taskName)) {
				tasks.remove(task);
				saveUpdates(categoryName, tasks);
				return Constants.SUCCESS;
			}
		}
		return "Task not found!";
	}
	
	public static void listTasks(String categoryName) {
		List<TaskBean>tasks=getTasks(categoryName);
		if(tasks.isEmpty()) {
			System.out.println("No tasks in category!");
			return ;
		}
		for(TaskBean task:tasks) {
			System.out.println(task);
		}
	}
	
	public static boolean taskExist(String categoryName,String taskName) {
		List<TaskBean>tasks=getTasks(categoryName);
		for(TaskBean task:tasks) {
			if(task.getName().equals(taskName)) {
				return true;
			}
		}
		return false;
	}
	public static void searchTask(String categoryName,String taskName) {

		if(taskExist(categoryName, taskName)) {
			System.out.println(taskName);
				return;
		}
		else {
			System.out.println("Task does not exist");
		}
	}
	
	//list all tasks of category
	public static void loadCategory(String categoryName) {
		File f=new File(Constants.folderPath+"\\"+categoryName+".todo");
		if(f.exists()) {
			System.out.println(categoryName);
			listTasks(categoryName);
			return;
		}
		else {
			System.out.println("Category does not exist!");
			return;
		}
		
	}
	
	//writing all files except the category that want to delete/not needed
	public static String deleteCategory(String categoryName) {
		File f=new File(Constants.folderPath+"\\"+categoryName+".todo");
		if(f.exists()) {
			f.delete();
			return Constants.SUCCESS;	
		}
		return "Category does'nt exist";
		
	}
	
	//add all cat todo.files to list
	private static List<File> listCatFiles(){
		List<File> catFiles=new ArrayList<>();
		File f=new File(Constants.folderPath);
		File[] files=f.listFiles();
		for(File file:files) {
			if(file.isFile() && file.getName().endsWith(".todo")) {
				//String name=file.getName().substring(0,file.getName().indexOf(".todo"));
				catFiles.add(file.getAbsoluteFile());
			}
			
		}
		return catFiles;
	}
	
	public static void listCategories() {
		List<File>catFiles=listCatFiles();
		if(!catFiles.isEmpty()) {
			for(File f:catFiles) {
				System.out.println(f.getName());
			}
		}
		else {
			System.out.println("*** Empty Categories! ***");
		}
		
	}
	
	public static void searchCategory(String categoryName) {
		List<File>catFile=listCatFiles();
		for(File f:catFile) {
			String fileName=f.getName().substring(0,f.getName().indexOf(".todo"));
			if(fileName.equals(categoryName)) {
				System.out.println("found -> "+fileName);
				return;
			}
			
		}
		System.out.println("Category not found");
	}
	
	//file to pdf
	public static String categoryFilesToPdf() {
		Document document = new Document();
	    try {
	    	File pdfFolder=new File(Constants.pdfPath);
	        PdfWriter.getInstance(document, new FileOutputStream(Constants.pdfPath+"\\Categories.pdf"));
	        document.open();
	        List<File>catFiles=listCatFiles();
	        if(catFiles.isEmpty()) {
	        	return "No Categories!";
	        }
	        for(File file:catFiles) {
	        	String catName=file.getName().substring(0,file.getName().indexOf(".todo"));
	        	document.add(new Paragraph(catName));
	        	document.add(new Paragraph());
	        	List<TaskBean>tasks=getTasks(catName);
	        	if(!tasks.isEmpty()) {
	        		for(TaskBean task:tasks) {
		        		document.add(new Paragraph("	"+task.toString()));
		        	}
	        	}
	        	else {
	        		document .add(new Paragraph("No tasks in category!"));
	        	}
	        	
	        	document.add(new Paragraph("----------    ***   ----------   ***      ----------"));
	        }
	        document.close();
	        
	        return Constants.SUCCESS;
	    }
	    catch (DocumentException | FileNotFoundException e) {
	        e.printStackTrace();
	        return Constants.FAILED;
	    }
	    finally {
	    	if(document.isOpen()) {
	    		document.close();
	    	}
	    }
	}

	
	//excel
	public static String categoryFilesToExcel() {
	    Workbook workbook = new HSSFWorkbook();
	    try {
	        
	        Sheet sheet = workbook.createSheet("Categories");

	        Row header = sheet.createRow(0);
	        header.createCell(0).setCellValue("Category Name");
	        header.createCell(1).setCellValue("Task Name");
	        header.createCell(2).setCellValue("Task Description");
	        header.createCell(3).setCellValue("Priority");
	        header.createCell(4).setCellValue("Due Date");
	        header.createCell(5).setCellValue("Tags");

	        List<File> catFiles = listCatFiles();
	        int rowNum = 1; 

	        for (File file : catFiles) {
	            String catName = file.getName().substring(0, file.getName().indexOf(".todo"));
	            Row categoryRow = sheet.createRow(rowNum++);
	            categoryRow.createCell(0).setCellValue(catName);

	            List<TaskBean> tasks = new ArrayList<>();y
	            tasks = TaskModel.getTasks(catName);

	            for (TaskBean task : tasks) {
	                Row taskRow = sheet.createRow(rowNum++);
	                taskRow.createCell(0).setCellValue(catName);  // Add the category name
	                taskRow.createCell(1).setCellValue(task.getName());  // Task name
	                taskRow.createCell(2).setCellValue(task.getDesc());  // Task description
	                taskRow.createCell(3).setCellValue(task.getPriority());  // Task priority
	                taskRow.createCell(4).setCellValue(task.getToDate());  // Task due date
	                taskRow.createCell(5).setCellValue(task.getTags());  // Task tags
	            }
	        }

	        FileOutputStream fileOut = new FileOutputStream(Constants.excelPath);
	        workbook.write(fileOut);
	        fileOut.close();

	        return Constants.SUCCESS;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return Constants.FAILED;
	    } finally {
	        try {
	            if (workbook != null) {
	                workbook.close();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
}
