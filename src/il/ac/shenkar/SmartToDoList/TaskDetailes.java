package il.ac.shenkar.SmartToDoList;

public class TaskDetailes
{
	private String taskDescription;
	private double createDate;

	public String getTaskDescription()
	{
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription)
	{
		this.taskDescription = taskDescription;
	}
	public double getCreateDate()
	{
		return createDate;
	}

	public TaskDetailes(String taskDescription)
	{
		super();
		this.taskDescription = taskDescription;
		this.createDate = System.currentTimeMillis();
	}
	

}
