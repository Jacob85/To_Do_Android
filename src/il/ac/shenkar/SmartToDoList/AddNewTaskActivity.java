package il.ac.shenkar.SmartToDoList;

import java.util.Calendar;

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


    
    
    

}
