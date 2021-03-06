package il.ac.shenkar.SmartToDoList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.R.bool;
import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class DataModel extends SQLiteOpenHelper
{
	private static DataModel instance = null;
	private ArrayList<TaskDetailes> list;
	private BaseAdapter adapter;
	private String Sorted;
	
	// DataBase Members
	private static final int DATABASE_VERSION =1;
	private final static String DATABASE_NAME = "taskManagers";
	private final static String TABLE_NAME = "tasks";
	private final String CR_DA = "createDate";
	private final String TA_DES = "taskDescription";
	private final String TA_TITEL = "taskTitle";
	private final String REMINDER_FLAG = "reminderFlag";
	private final String REMINDER_DATE = "reminderDate";
	
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_CONTACTS_TABLE  = "CREATE TABLE " + TABLE_NAME +"(createDate REAL PRIMARY KEY, taskDescription TEXT,taskTitle TEXT, reminderFlag INTEGER,reminderDate INTEGER )";
		db.execSQL(CREATE_CONTACTS_TABLE);
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // Create tables again
        onCreate(db);
		
	}
	/* constractor
	 * in the constractor i create the data structure and the initial list
	 */
	private DataModel(Context context)
	{
		super(context, DATABASE_NAME, null,DATABASE_VERSION);
		
		// here i need to widrow all the tasks from the database;
		list = new ArrayList<TaskDetailes>();
		getAllTasksFromDB();
		
		//the default sort is by date
		//When inistialising the Datamodel it's sorted by date
		this.Sorted = new String("Date");
		
	}
	/*Static Function to get the instance for the data structure
	 * 
	 * */			
	public static DataModel getInstance(Context context)
	{	
		if (instance == null)
			instance = new DataModel(context);
		return instance;
	}
	
	public void setAdapter (BaseAdapter a)
	{
		this.adapter = a;
	}
	
	
	public void addNewTask(String title,String description, boolean reminderFlag, int rminderDate )
	{
		// adding the new task to the list
		TaskDetailes toAdd = new TaskDetailes(title, description, reminderFlag, rminderDate);
		this.list.add(toAdd);
		
		// adding the new task to the database
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();	
		values.put(this.CR_DA, toAdd.getCreateDate()); 
		values.put(this.TA_DES, description);
		values.put(this.TA_TITEL, title);
		values.put(this.REMINDER_FLAG, reminderFlag);
		if (reminderFlag)
			values.put(this.REMINDER_DATE, rminderDate );
		
		
		db.insert(TABLE_NAME, null, values);
		db.close(); // Closing database connection

		// here i need to notify
		this.adapter.notifyDataSetChanged();
		
	}
	
	// i use this function only to add to the DB from the service
	public void addNewTaskWithoutNotify(String title,String description, boolean reminderFlag, int rminderDate )
	{
		// adding the new task to the list
		TaskDetailes toAdd = new TaskDetailes(title, description, reminderFlag, rminderDate);
		this.list.add(toAdd);
		
		// adding the new task to the database
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();	
		values.put(this.CR_DA, toAdd.getCreateDate()); 
		values.put(this.TA_DES, description);
		values.put(this.TA_TITEL, title);
		values.put(this.REMINDER_FLAG, reminderFlag);
		if (reminderFlag)
			values.put(this.REMINDER_DATE, rminderDate );
		
		db.insert(TABLE_NAME, null, values);
		db.close(); // Closing database connection
		
	}
	
	public void removeTask(String toDeleteString)
	{	
		// first I'll get the task i want to delete
		int id = getPositionFromTitle(toDeleteString);
		TaskDetailes toDeleteTask = this.list.get(id);
				
		// i'll remove the task from the database
		SQLiteDatabase db = this.getWritableDatabase();
				
		db.delete(TABLE_NAME,CR_DA+ "= ?", new String[] {String.valueOf(toDeleteTask.getCreateDate())});
		db.close();
		
		// here i will remove the task from the arraylist
		this.list.remove(getPositionFromTitle(toDeleteString));
		
		// here i will notify the GUI if my changes! 
		this.adapter.notifyDataSetChanged();
	}
	
	public String [] getTaskToDisplay(String Title)
	{
		// first i'll get the task from the list
		int id = getPositionFromTitle(Title);
		TaskDetailes toReturnTask = this.list.get(id);
		
		// check if the task have a reminder and decide the String[] size;
		String[] items;
		if (toReturnTask.HaveReminder())
		{
			Calendar c = Calendar.getInstance();
			items=new String [4];
			items[0] = toReturnTask.getTaskTitle();
			items[1] = toReturnTask.getTaskDescription();
			// set the calender to tha time
			c.setTimeInMillis(toReturnTask.getReminderDate());
			items[2] = c.getTime().toString();
		}
		else
		{
			
			items = new String[2];
			items[0] = toReturnTask.getTaskTitle();
			items[1] = toReturnTask.getTaskDescription();

			
			
		}
		return items;
		
		
	}
	
	
	public ArrayList<TaskDetailes> getList()
	{
		return list;
	}
	public void setList(ArrayList<TaskDetailes> list)
	{
		this.list = list;
	}

	// this method is for returning the index of the element by receiving a string to the elment title
	private int getPositionFromTitle(String toGet)
	{	
		for (int i =0; i < this.list.size();i++ )
		{
			if (this.list.get(i).getTaskTitle().equals(toGet))
				return i;
		}
		return -1;
		
	}
	
	public void getAllTasksFromDB()
	{
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
		
		if (cursor.moveToFirst())
		{
			do 
			{
				TaskDetailes newTask = new TaskDetailes();
				newTask.setCreateDate(cursor.getDouble(0));
				newTask.setTaskDescription(cursor.getString(1));
				newTask.setTaskTitle(cursor.getString(2));
				if (cursor.getInt(3) == 0)				//reminderflag = false;
				{
					newTask.setHaveReminder(false);
					
				}
				else
				{
					newTask.setHaveReminder(true);
					newTask.setReminderDate(cursor.getInt(4));
				}
				// adding the new task to the list
				list.add(newTask);
			}while (cursor.moveToNext()); // move to the next row in the DB
		
		}
		cursor.close();
		db.close();
	}
	
	public void sortBy (String order)
	{
		// we check if the list is already sorted according to the user request
		if (order.equals(this.Sorted))
		{
			return;
		}
		
		if (order.equals("Date"))
		{

			Collections.sort(this.list,new Comparator<TaskDetailes>()
			{
				//sort by date
				public int compare(TaskDetailes lhs, TaskDetailes rhs)
				{
					if (lhs.getCreateDate() > rhs.getCreateDate())
						return 1;
					else if (lhs.getCreateDate() == rhs.getCreateDate())
						return 0;
					return -1;
				}
				
			});
		}
		
		if (order.equals("Name"))
		{
			//sort by name
			Collections.sort(this.list,new Comparator<TaskDetailes>()
				{
					//sort by name
					public int compare(TaskDetailes lhs, TaskDetailes rhs)
					{
						return lhs.getTaskDescription().compareTo(rhs.getTaskDescription());
					}
				});
		}
		
		
		//we need to notifay the data has changed
		this.adapter.notifyDataSetChanged();
		this.Sorted = new String(order);
		
	}
	
}
