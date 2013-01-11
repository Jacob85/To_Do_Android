package il.ac.shenkar.SmartToDoList;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnNavigationListener
{


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // here i am calling the function that retrieve the data to me
        DataModel datamodel= DataModel.getInstance(this);
        
        final ListView mainList = (ListView) findViewById(R.id.listView1);
        mainList.setAdapter(new TaskListBaseAdapter(this,datamodel.getList()));
        mainList.setOnItemClickListener(new OnItemClickListener()
		{

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				Toast.makeText(getApplicationContext(),
					      "Click ListItem Number " + arg2, Toast.LENGTH_LONG)
					      .show();
				
			}
        	
		});
        datamodel.setAdapter((BaseAdapter) mainList.getAdapter());

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.planets_array, android.R.layout.simple_spinner_item);
        ActionBar myActionBar = getActionBar();
        myActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        myActionBar.setListNavigationCallbacks(adapter, this);
        
	     
	     //service intent
	     Intent serviceIntent = new Intent(this, DailyTaskService.class);
	     PendingIntent pending = PendingIntent.getService(this,0, serviceIntent, 0);
	     AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);  
	     Calendar cal = Calendar.getInstance();
	     cal.set(cal.HOUR_OF_DAY, 23);
	     cal.set(cal.MINUTE, 59);
	     cal.set(cal.SECOND, 59);
	     alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pending);
	     
	    
    }
	


	// this method is called when the Action items is preaed
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		AddNewTask(null);
		return true;
	}



	public void doneButtonPreased(View view)
	{
		 //building the progress dialog
        ProgressDialog progressDialod = new ProgressDialog(this);
        progressDialod.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialod.setMessage("Working");
        progressDialod.setMessage("Jacob Rulez!!");
        progressDialod.setIndeterminate(true);
        progressDialod.setCancelable(false);
		progressDialod.show();
		
		DataModel model = DataModel.getInstance(this);
		String msg = (String) view.getTag(); 
		model.removeTask(msg);
		progressDialod.cancel();
		
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
	}
	/*
	 * this method is being called while the "Add Task" button is being pressed
	 * the method starts new activity for the user to enter his new task
	 */ 
	 public void AddNewTask(View view)
     {
     	Intent intent = new Intent(this, AddNewTaskActivity.class);
     	startActivity(intent);
     }
	
	
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_avtivity_menu, menu);
		return true;
	}



	public boolean onNavigationItemSelected(int itemPosition, long itemId)
	{
		// sort the list
		String[] strings = getResources().getStringArray(R.array.planets_array);
		DataModel model = DataModel.getInstance(this);
		model.sortBy(strings[itemPosition]);
		return true;
	}

	public void DisplayTask (View v)
	{
		// i need to display massage to the user
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set title
		alertDialogBuilder.setTitle("Task Details");  	
		// set dialog message
		alertDialogBuilder.setItems(DataModel.getInstance(this).getTaskToDisplay(v.findViewById(R.id.button1).getTag().toString()), null);
		alertDialogBuilder.setCancelable(false)
						  .setPositiveButton("OK",new DialogInterface.OnClickListener() {	
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close the dilaog
								dialog.cancel();
								return;
								}
						  	});
		
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}



}
