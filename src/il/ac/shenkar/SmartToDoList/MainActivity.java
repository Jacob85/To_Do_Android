package il.ac.shenkar.SmartToDoList;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.Spinner;

public class MainActivity extends Activity implements OnItemSelectedListener 
{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // here i am calling the function that retrieve the data to me
        DataModel datamodel= DataModel.getInstance();
        
        final ListView mainList = (ListView) findViewById(R.id.listView1);
        mainList.setAdapter(new TaskListBaseAdapter(this,datamodel.getList()));
        datamodel.setAdapter((BaseAdapter) mainList.getAdapter());
          
        
        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
	     // Create an ArrayAdapter using the string array and a default spinner layout
	     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	             R.array.planets_array, android.R.layout.simple_spinner_item);
	     // Specify the layout to use when the list of choices appears
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     // Apply the adapter to the spinner
	     spinner.setAdapter(adapter);
	     spinner.setOnItemSelectedListener(this);
          
    }
	
	public void doneButtonPreased(View view)
	{
		  //building the progress dialog
        ProgressDialog progressDialod = new ProgressDialog(this);
        progressDialod.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialod.setMessage("Working");
        progressDialod.setIndeterminate(true);
        progressDialod.setCancelable(false);
		progressDialod.show();
		
		DataModel model = DataModel.getInstance();
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
     	Intent intent = new Intent(this, AddNewTask.class);
     	startActivity(intent);
     }
	
	
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id)
	{
		  //building the progress dialog
        ProgressDialog progressDialod = new ProgressDialog(this);
        progressDialod.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialod.setMessage("Working");
        progressDialod.setIndeterminate(true);
        progressDialod.setCancelable(false);
		progressDialod.show();
		
		//Retrieve the user selection from the drop down 
		String sortBy =	(String) parent.getItemAtPosition(pos);	
		//Sort the list by the user demand;
		DataModel model = DataModel.getInstance();
		model.sortBy(sortBy);
		
		progressDialod.cancel();
	}

	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub
		
	}

}
