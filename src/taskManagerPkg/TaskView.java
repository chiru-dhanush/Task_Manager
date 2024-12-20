	package taskManagerPkg;
	import java.text.SimpleDateFormat;
import java.util.Scanner;
	public class TaskView {
		public static void main(String[] args) {
			
			Logger logger=Logger.getInstance();
			logger.log("main() starts",4);
			
			Scanner sc1=new Scanner(System.in);
			Scanner sc2=new Scanner(System.in);
			
			String categoryName,taskName,taskDesc,taskTags;
			int taskPriority;
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			String task_toDate;
			
			String status;
			
			try {
				logger.log("Starting TaskManager", 1);
				int categoryChoice=0;
				while(categoryChoice!=7) {
					System.out.println("Press 1 to Create Category");
					System.out.println("Press 2 to Load Category");
					System.out.println("Press 3 to Delete Category");
					System.out.println("Press 4 to List Category");
					System.out.println("Press 5 to Search Category");
					System.out.println("Press 6 to Export Category");
					System.out.println("Press 7 to Exit");
					System.out.println("Enter your choice:");
					categoryChoice=sc1.nextInt();
			
					switch(categoryChoice) {
						case 1:
							logger.log("Starting Create Category", 3);
							System.out.println("Enter Category name");
							categoryName=sc2.nextLine();
							
							if(TaskModel.isValidCategoryName(categoryName)) {
								
								if(TaskModel.createCategoryFile(categoryName)){
									status=Constants.SUCCESS;
									System.out.println(status);
									int taskChoice=0;
									while(taskChoice!=6) {
										System.out.println("Press 1 to Create Task");
										System.out.println("Press 2 to Update Task");
										System.out.println("Press 3 to Delete Task");
										System.out.println("Press 4 to List Task");
										System.out.println("Press 5 to Search Task");
										System.out.println("Press 6 to Go Back to Home Menu");
										System.out.println("Enter your choice:");
										taskChoice=sc1.nextInt();
										
										switch(taskChoice) {
											case 1:
												logger.log("1.Creating task",2);
												System.out.println("Enter Task name:");
												taskName=sc2.nextLine();									
												
												if(TaskModel.isValidTaskName(taskName)) {
													System.out.println("Enter Task description:");
													taskDesc=sc2.nextLine();
													if(TaskModel.isValidDesc(taskDesc)) {
														System.out.println("Enter Task Priority between 1-10");
														taskPriority=sc1.nextInt();										
														
														if(TaskModel.isValidPriority(taskPriority)) {
															System.out.println("Enter the date you want to finish doing the task (DateForamt: dd/MM/yyyy)");
															task_toDate=sc2.nextLine();
															
															if(TaskModel.isValidToDate(task_toDate)) {
																//task_toDate=sdf.parse(strDate);
																
																System.out.println("Enter Task Tags seperated by ','(commas)");
																taskTags=sc2.nextLine();
																if(TaskModel.isValidTags(taskTags)) {
																	
																	TaskBean task=new TaskBean(taskName,taskDesc,taskPriority,taskTags,task_toDate);
																	TaskModel.addTaskToCategory(task, categoryName);
																	
																	System.out.println(Constants.SUCCESS);
																}
																
															}
															else {
																System.out.println("Incorrect Date or format , (correct format : dd/MM/yyyy)");
															}
														
														}
														else {
															System.out.println("Incorrect Priority ! ,Kindly Enter Priority between 1-10");
														}
													}
	
													
												}
												else {
													System.out.println("Task Name format should start with letter , digits not allowed in name , only Single word allowed , no spaces allowed");
												}
												
												logger.log("Ending Create Taksk", 2);
												break;
												
											case 2:
												logger.log("Updating Task starts", 2);
												System.out.println("Enter task name , which you want to update");
												taskName=sc2.nextLine();
//												TaskBean updTask = TaskModel.searchTask(taskName,categoryName);
												if(TaskModel.taskExist(categoryName, taskName)) {
													int updChoice=0;
													while(updChoice!=6) {
														System.out.println("1.Update Task Name");
														System.out.println("2.Update Description");
														System.out.println("3.Update Priority");
														System.out.println("4.Update Tags");
														System.out.println("5.Update Due Date");
														System.out.println("6.Go Back");
														System.out.println("Enter choice:");
														updChoice=sc1.nextInt();
													
														switch(updChoice) {
															
															case 1:
																logger.log("case 1 Updating new task name", 2);
																
																System.out.println("Enter new Task name");
																String newTaskName=sc2.nextLine();
																if(TaskModel.isValidTaskName(newTaskName)) {
																	status=TaskModel.setTaskNewName(categoryName,taskName,newTaskName);
																	taskName=newTaskName;
																	System.out.println(status);
																}
																
																logger.log("case 1 Ending task name update", 2);
																break;
														
															case 2:
																logger.log("case 2 Updating new Description",2);
																
																System.out.println("Enter new Description");
																String newDesc=sc2.nextLine();
																status=TaskModel.setTaskNewDesc(categoryName,taskName,newDesc);
																System.out.println(status);
																
																logger.log("case 2 Ending task desc update", 2);
																break;
															case 3:
																logger.log("case 3 Updating new Priority",2);
																
																System.out.println("Enter new Priority");
																int newPriority=sc1.nextInt();
																status=TaskModel.setTaskNewPriority(categoryName,taskName,newPriority);
																System.out.println(status);
																
																logger.log("case 2 Ending task priority update", 2);
																break;
															case 4:
																logger.log("case 4 Updating new Priority",2);
																
																System.out.println("Enter new Tags");
																String newTags=sc2.nextLine();
																status=TaskModel.setTaskNewTags(categoryName,taskName,newTags);
																System.out.println(status);
																
																logger.log("case 4 Ending task tags update", 2);
																break;
															case 5:
																logger.log("case 5 Updating new Due Date",2);
																
																System.out.println("Enter new Due Date (format: dd/MM/yyyy)");
																String newDueDate=sc2.nextLine();
																status=TaskModel.setTaskNewDueDate(categoryName,taskName,newDueDate);
																System.out.println(status);
																
																logger.log("case 5 Ending task date update", 2);
																break;
																
															case 6:
																break;
																
															default:
																System.out.println("Enter valid choice");
																break;
														}
													}
												}
												else {
													System.out.println("Task does not Exist!");
												}
												
											    
												
												logger.log("case 2 Updating task ends", 2);
												break;
												
											case 3:
												logger.log("case 3 Deleting task starts", 2);
												
												System.out.println("Enter the task name to delete");
												taskName=sc2.nextLine();
												status=TaskModel.deleteTask(categoryName,taskName);
												System.out.println(status);
												
												logger.log("case 3 Deleting task ends", 2);
												break;
												
											case 4:
												logger.log("case 4 Listing tasks starts", 2);
												
												System.out.println("Listing Tasks");
												TaskModel.listTasks(categoryName);
												
												logger.log("case 4 Listing tasks ends", 2);
												break;
												
											case 5:
												logger.log("case 5 Search task starts", 2);
												
												System.out.println("Enter Task name you want to search");
												taskName=sc2.nextLine();
												TaskModel.searchTask(categoryName, taskName);
												
												logger.log("case 5 Search task ends",2);
												break;
												
											case 6:
												break;
												
											default:
												System.out.println("Enter valid choice:");
												break;
										}
									}
								}
								
								else {
									System.out.println("Category already exists! ");
								}
								
							}
							else {
								System.out.println("Category Name format should start with letter , digits not allowed in name , only Single word allowed , no spaces allowed");
								break;
							}
							
							
							break;
							
						case 2:
							logger.log("Loading category starts", 3);
							
							System.out.println("Enter the category name , you want to load");
							categoryName=sc2.nextLine();
							if(TaskModel.isValidCategoryName(categoryName)) {
								TaskModel.loadCategory(categoryName);
							}
							
							logger.log("Loading category ends", 3);
							break;
							
						case 3:
							logger.log("Deleting category starts", 3);
							
							System.out.println("Enter the category name , you want to delete");
							categoryName=sc2.nextLine();
							status=TaskModel.deleteCategory(categoryName);
							System.out.println(status);
							
							logger.log("Deleting category ends", 3);
							break;
							
						case 4:
							logger.log("Listing category starts", 3);
							
							System.out.println("Listing categories:");
							TaskModel.listCategories();
							
							logger.log("Listing category ends", 3);
							break;
							
						case 5:
							logger.log("Searching category starts", 3);
							
							System.out.println("Enter category name , you want searched");
							categoryName=sc2.nextLine();
							TaskModel.searchCategory(categoryName);
							
							logger.log("Searching category ends", 3);
							break;
							
						case 6:
							System.out.println("Exporting Categories");
							int exportChoice=0;
							while(exportChoice!=3) {
								System.out.println("1.Export as PDF");
								System.out.println("2.Export as Excel");
								System.out.println("3.Go Back");
								System.out.println("Enter choice:");
								exportChoice=sc1.nextInt();
								
								switch(exportChoice) {
									case 1:
										status=TaskModel.categoryFilesToPdf();
										System.out.println(status);
										break;
										
									case 2:
										status=TaskModel.categoryFilesToExcel();
										System.out.println(status);
										break;

									case 3:
										break;
										
									default:
										System.out.println("Enter valid choice , either 1 or 2");
										break;
								}
							}
							break;
							
						case 7:
							System.out.println("Exiting App :(");
							break;
							
						default:
							System.out.println("Enter valid choice");
							break;
					}
				}
			}
			catch(Throwable t) {
				t.printStackTrace();
			}
			
			logger.log("main() ends", 4);
		}
	}
