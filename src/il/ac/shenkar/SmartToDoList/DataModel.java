package il.ac.shenkar.SmartToDoList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class DataModel
{
	private static DataModel instance = null;
	private ArrayList<TaskDetailes> list;
	private BaseAdapter adapter;
	private String Sorted;
	
	/* constractor
	 * in the constractor i create the data structure and the initial list
	 */
	private DataModel()
	{
		list = new ArrayList<TaskDetailes>();
		for(int i =1; i<=30 ; i++)
		{
			list.add(new TaskDetailes("find a job "+i));
		}
		//the default sort is by date
		//When inistialising the Datamodel it's sorted by date
		this.Sorted = new String("Date");
		
	}
	/*Static Function to get the instance for the data structure
	 * 
	 * */			
	public static DataModel getInstance()
	{	
		if (instance == null)
			instance = new DataModel();
		return instance;
	}
	
	public void setAdapter (BaseAdapter a)
	{
		this.adapter = a;
	}
	
	
	public void addNewTask(String msg)
	{
		this.list.add(new TaskDetailes(msg));
		// here i need to notify
		this.adapter.notifyDataSetChanged();
		
	}
	
	public void removeTask(String toDelete)
	{	
		this.list.remove(getPositionFromDescription(toDelete));
		this.adapter.notifyDataSetChanged();
	}
	public ArrayList<TaskDetailes> getList()
	{
		return list;
	}
	public void setList(ArrayList<TaskDetailes> list)
	{
		this.list = list;
	}

	// this method is for returning the index of the element by receiving a string to the elment description
	private int getPositionFromDescription(String toGet)
	{	
		for (int i =0; i < this.list.size();i++ )
		{
			if (this.list.get(i).getTaskDescription() == toGet)
				return i;
		}
		return -1;
		
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
