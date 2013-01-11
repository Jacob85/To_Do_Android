package il.ac.shenkar.SmartToDoList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;

public class DailyTaskService extends IntentService
{

	public DailyTaskService()
	{
		super("Daily Task Service");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		try
		{
			//Fetching the new Task
			URL url = new URL("http://mobile1-tasks-dispatcher.herokuapp.com/task/random");
			String response =  getFromWeb(url);
			JSONObject Json = new JSONObject(response);
			// creating the new task
			String description = Json.getString("description");
			String title =  Json.getString("topic");
			DataModel model = DataModel.getInstance(this);
			model.addNewTaskWithoutNotify(title, description, false, 0);
			return;
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	
	
	
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
}
