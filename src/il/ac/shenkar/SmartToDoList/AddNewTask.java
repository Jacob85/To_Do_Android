package il.ac.shenkar.SmartToDoList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class AddNewTask extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
 
    }

    
    
    // this function is called while the "create new task" button is preased
    public void createNewTask (View view)
    {
    	EditText TextTitle= (EditText) findViewById(R.id.editText1);
    	String msgTitle = TextTitle.getText().toString();
    	if (msgTitle != null)
    	{
    		DataModel model = DataModel.getInstance();
    		model.addNewTask(msgTitle);
    		finish();
    	}
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

}
