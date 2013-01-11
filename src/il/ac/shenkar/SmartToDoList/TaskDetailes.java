package il.ac.shenkar.SmartToDoList;

import java.util.Date;

public class TaskDetailes
{
	public TaskDetailes(String taskTitle, String taskDescription,
			boolean haveReminder, int reminderDate)
	{
		super();
		this.taskTitle = taskTitle;
		this.taskDescription = taskDescription;
		this.haveReminder = haveReminder;
		this.reminderDate = reminderDate;
		this.createDate = System.currentTimeMillis();
	}

	private String taskTitle;
	private String taskDescription;
	private double createDate;
	private boolean haveReminder;
	private int reminderDate;				// in miliseconds
	
	public boolean HaveReminder()
	{
		if (this.haveReminder == true)
			return true;
		return false;
	}
	

	public String getTaskTitle()
	{
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle)
	{
		this.taskTitle = taskTitle;
	}

	public boolean isHaveReminder()
	{
		return haveReminder;
	}

	public void setHaveReminder(boolean haveReminder)
	{
		this.haveReminder = haveReminder;
	}

	public int getReminderDate()
	{
		return reminderDate;
	}

	public void setReminderDate(int reminderDate)
	{
		this.reminderDate = reminderDate;
	}

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

	public TaskDetailes(){}

	public void setCreateDate(double createDate)
	{
		this.createDate = createDate;
	}
	

}
