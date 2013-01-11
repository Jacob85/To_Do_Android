package il.ac.shenkar.SmartToDoList;

import java.util.ArrayList;




import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class TaskListBaseAdapter extends BaseAdapter
{
	private static ArrayList<TaskDetailes> taskDetailsrrayList;
	private LayoutInflater l_Inflater;

	
	public TaskListBaseAdapter(Context context, ArrayList<TaskDetailes> results)
	{
		this.taskDetailsrrayList = results;
		this.l_Inflater = LayoutInflater.from(context);
	}

	public int getCount()
	{
		return this.taskDetailsrrayList.size();
	}

	public Object getItem(int position)
	{
		return this.taskDetailsrrayList.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		Button currButton;
		ViewHolder holder;
		
		if (convertView == null)
		{
			convertView = l_Inflater.inflate(R.layout.task_layout, null);
			holder = new ViewHolder();
			holder.txt_itemTitle = (TextView) convertView.findViewById(R.id.description);
			
			convertView.setTag(holder);
		} else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		/*
		 * Here I get id for the button of this specific view and give him "tag" so i could delete it later on
		 * */
		currButton = (Button) convertView.findViewById(R.id.button1);
		holder.txt_itemTitle.setText(taskDetailsrrayList.get(position).getTaskTitle());
		currButton.setTag(holder.txt_itemTitle.getText().toString());
		// this part is only for displaying and not displaying the reminder Icon
		if (taskDetailsrrayList.get(position).HaveReminder())
			convertView.findViewById(R.id.imageView1).setVisibility(View.VISIBLE);
		else
			convertView.findViewById(R.id.imageView1).setVisibility(View.INVISIBLE);
		return convertView;
		
	
	}

	
	// while i'm removing or adding new task i notify the adapter
	// the adapter find out what has changed and refresh the list
	@Override
	public void notifyDataSetChanged()
	{
		super.notifyDataSetChanged();
	}

	static class ViewHolder
	{
		TextView txt_itemTitle;	
	}
}
	