package il.ac.shenkar.SmartToDoList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;

@SuppressLint("NewApi")
@TargetApi(11)
public class AddNewTaskActivity extends Activity {

	private ProgressDialog progressDialod;
	private int reminderHour;
	private int reminderMinute;
	private int reminderDay;
	private int reminderMonth;
	private int reminderYear;
	
	
	
    public int getReminderDay()
	{
		return reminderDay;
	}
	public void setReminderDay(int reminderDay)
	{
		this.reminderDay = reminderDay;
	}
	public int getReminderMonth()
	{
		return reminderMonth;
	}
	public void setReminderMonth(int reminderMonth)
	{
		this.reminderMonth = reminderMonth;
	}
	public int getReminderYear()
	{
		return reminderYear;
	}
	public void setReminderYear(int reminderYear)
	{
		this.reminderYear = reminderYear;
	}
	public int getReminderMinute()
	{
		return reminderMinute;
	}
	public void setReminderMinute(int reminderMinute)
	{
		this.reminderMinute = reminderMinute;
	}
	public int getReminderHour()
	{
		return reminderHour;
	}
	public void setReminderHour(int reminderHour)
	{
		this.reminderHour = reminderHour;
	}



	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
 
    }

	private boolean isReminderValid()
	{
		Calendar c = Calendar.getInstance();
		if (this.getReminderYear() > c.get(Calendar.YEAR))
			return true;
		else if (this.getReminderYear() < c.get(Calendar.YEAR))
			return false;
		if (this.getReminderMonth() > c.get(Calendar.MONTH))
			return true;
		else if (this.getReminderMonth() < c.get(Calendar.MONTH))
			return false;
		if (this.getReminderDay() > c.get(Calendar.DAY_OF_MONTH))
			return true;
		else if (this.getReminderDay() < c.get(Calendar.DAY_OF_MONTH))
			return false;
		
		//if we ate here the date is valid and we need to check the time 
		if (this.getReminderHour() > c.get(Calendar.HOUR_OF_DAY))
			return true;
		else if (this.getReminderHour() < c.get(Calendar.HOUR_OF_DAY))
			return false;
		if (this.getReminderMinute() < c.get(Calendar.MINUTE))
			return false;
		return true;
	}

    // this function is called while the "create new task" button is preased
    public void createNewTask (View view)
    {
    	
    	EditText TextTitle= (EditText) findViewById(R.id.editText1);
    	String msgTitle = TextTitle.getText().toString();
    	// we will check the text field is not empty
    	if (msgTitle.equals(""))
    	{
    		Toast.makeText(getBaseContext(), "You Must Enter Task Detailes", Toast.LENGTH_LONG).show();
    		TextTitle.setHintTextColor(Color.RED);	
    		return;
    	}
    	
    	// we ill check if the Enable Reminder is checked
    	CheckBox enable = (CheckBox) findViewById(R.id.checkBox1);
    	if (enable.isChecked())
    	{
    		// we need to check if the time & date selected are valid
    		if (this.isReminderValid() == false)
    		{
    			Toast.makeText(getBaseContext(), "Invalid Date Or Time Chosen! Please Try Again", Toast.LENGTH_LONG).show();
    			return;
    		}
    		// if we are here means that the text field is not empty and the date & time are valid! we need to add the reminder
    		Intent intent = new Intent("com.rtt.reminder_broadcast");
    		intent.putExtra("msg", msgTitle);
    		PendingIntent pendingInent = PendingIntent.getBroadcast(this, 0, intent, 0);
    		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    		
    		Calendar c = Calendar.getInstance();
    		c.set(this.getReminderYear(),getReminderMonth(), getReminderDay(), getReminderHour(), getReminderMinute());
    		
    		alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingInent);
    	
    	}
   
    	// here anyway we add the task to the DB
    		DataModel model = DataModel.getInstance(this);
    		model.addNewTask(msgTitle);
    		finish();
   
  
    }
    
    /**
     * this function is been invoked when the user press the random button
     * @param view
     * @throws MalformedURLException 
     */
    public void Random(View view) throws MalformedURLException
    {
    	//starting the spinner
        myProgressDialogStart("Download Task From Web Server...");
		URL url = new URL("http://mobile1-tasks-dispatcher.herokuapp.com/task/random");
    	new GetFromWebTask().execute(url);
    	
    }
    
    // creating the progress dialog
   public void myProgressDialogStart(String msg)
    {
    	//building the progress dialog
        this.progressDialod = new ProgressDialog(this);
        progressDialod.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialod.setMessage(msg);
        progressDialod.setIndeterminate(true);
        progressDialod.setCancelable(false);
		progressDialod.show();
    }
   
   //canceling the progress dialog
   public void myProgressDialogStop() {this.progressDialod.cancel();}
    
    public String getFromWeb(URL url)
    {
    	try
		{
    		//open connection
    		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			// Fetching the string 
    		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(inReader);
			StringBuilder responseBuilder = new StringBuilder();
			for (String line = bufferedReader.readLine();line!=null; line = bufferedReader.readLine())
			{
				responseBuilder.append(line);
			}
			
			urlConnection.disconnect();
			return responseBuilder.toString();
		} catch (IOException e)
		{
	
			e.printStackTrace();
		}
    	return null;
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_new_task, menu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    

    // the enable reminder reviled the date & time Pickers
    public void EnableReminder (View v)
    {
    	CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
    	Button time = (Button) findViewById(R.id.timePickerButton);
		Button date = (Button) findViewById(R.id.datePickerButton);
    	if (checkBox.isChecked())
    	{
    		time.setVisibility(View.VISIBLE);
    		date.setVisibility(View.VISIBLE);
    	}
    	else 
    	{
    		time.setVisibility(View.INVISIBLE);
    		date.setVisibility(View.INVISIBLE);
    	}
    }
    
   @TargetApi(11)
	public void ShowTimePicker(View v) 
    {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
        
        
        	
    }
   
   public void showDatePicker(View v)
   {
	   DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");

   }
    
    
    //*********************************************************************************************
    //									TIME PICKER
    //*********************************************************************************************
  
	@TargetApi(11)
	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	// Use the current time as the default values for the picker
	final Calendar c = Calendar.getInstance();
	int hour = c.get(Calendar.HOUR_OF_DAY);
	int minute = c.get(Calendar.MINUTE);
	
	// Create a new instance of TimePickerDialog and return it
	return new TimePickerDialog(getActivity(), this, hour, minute,
	DateFormat.is24HourFormat(getActivity()));
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
	{
	// Do something with the time chosen by the user
	
		// changing the UI with the chosen time
		Button time = (Button) this.getActivity().findViewById(R.id.timePickerButton);
		time.setText("Reminder's Time - "+hourOfDay+":"+minute);
		
		// changing the object of the reminder with the new time & date chosen by the user
		((AddNewTaskActivity) this.getActivity()).setReminderHour(hourOfDay);
		((AddNewTaskActivity) this.getActivity()).setReminderMinute(minute);
	
	
	}
	
	}

	
	//*********************************************************************************************
    //									DATE PICKER
    //*********************************************************************************************
	
	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
	
	// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			// changing the UI with the chosen time
			Button time = (Button) this.getActivity().findViewById(R.id.datePickerButton);
			time.setText("Reminder's Date - : "+dayOfMonth+"/"+monthOfYear+"/"+year);
			
				
			// changing the object of the reminder with the new time & date chosen by the user
			((AddNewTaskActivity) this.getActivity()).setReminderDay(dayOfMonth);
			((AddNewTaskActivity) this.getActivity()).setReminderMonth(monthOfYear);
			((AddNewTaskActivity) this.getActivity()).setReminderYear(year);
		}
	}


    
	
	private class GetFromWebTask extends AsyncTask<URL, Integer, String>
	{

		@Override
		protected String doInBackground(URL... params)
		{
			String result = getFromWeb(params[0]);

		return result;
		}

		@Override
		protected void onPostExecute(String result)
		{
			try
			{
				JSONObject Json = new JSONObject(result);
				EditText textview = (EditText) findViewById(R.id.editText1);
				textview.setText(Json.getString("topic")+", "+ Json.getString("description"));
				myProgressDialogStop();
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
    
    

}
