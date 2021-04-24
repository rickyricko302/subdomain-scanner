package com.hikki.subdoasukadev;

import android.app.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import org.json.*;

public class MainActivity extends Activity 
{
	TextView result,author;
	ImageView save;
	LinearLayout load;
	JSONObject o;
	String data,urla;
	ProgressDialog pd;
	EditText ed;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		result = findViewById(R.id.result);
		
		ed = findViewById(R.id.ed);
		load = findViewById(R.id.loading);
		save = findViewById(R.id.save);
		author = findViewById(R.id.author);
		author.setText("Develop by { Ricky V | _Hikki }");
		ed.setOnEditorActionListener(new TextView.OnEditorActionListener(){

				@Override
				public boolean onEditorAction(TextView p1, int p2, KeyEvent p3)
				{
					// TODO: Implement this method
					save.setVisibility(View.GONE);
					author.setVisibility(View.GONE);
					urla = p1.getText().toString();
					new exe().execute();
					return false;
				}

			
		});
		ed.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					result.setText("Result Search : ");
					data = "";
					author.setVisibility(View.VISIBLE);
					save.setVisibility(View.GONE);
				}
				
			
		});
		save.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					save.setAlpha(0.5f);
					new Handler().postDelayed(new Runnable(){

							@Override
							public void run()
							{
								// TODO: Implement this method
								save.setAlpha(1.0f);
							}				
						
					},500);
					if (data != null && !data.equals("kosong")){
						try
						{
							File f = new File("/sdcard/SubdoAsukadev");
							if (!f.exists()){
								f.mkdir();
							}
							FileWriter fw = new FileWriter(f+"/"+urla+".txt");
							fw.write(data);
							fw.flush();
							fw.close();
							Toast.makeText(getApplicationContext(),"Succes save into /sdcard/SubdoAsukadev/"+urla+".txt",Toast.LENGTH_LONG).show();

						}
						catch (IOException e)
						{}}
				}
				
			
		});
    }
	public class exe extends AsyncTask<Void,Void,Boolean>
	{
		@Override
		protected void onPreExecute()
		{
			// TODO: Implement this method
			load.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void[] p1)
		{
			// TODO: Implement this method
			try{
				data = "";
				URL u = new URL("http://jamet1337.ml/api/subdo.php?url="+urla);
				URLConnection uc = u.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				String g;
				StringBuilder sb = new StringBuilder();
				while ((g=br.readLine())!=null){
					sb.append(g);
				}
				o = new JSONObject(sb.toString());
				if (o.get("status").toString().equals("succes")){
					data+=o.get("hasil").toString();
					return true;
				}
				else{
					Log.e("JSON ERROR","JSONKSON");
					return false;
				}

			}catch(Exception e){
				Log.e("EROR",e.toString());
				return false;
			}
		
		}
		

		
		
		
		@Override
		protected void onPostExecute(Boolean resulst)
		{
			// TODO: Implement this method
			hideSoftKeyboard(MainActivity.this);
			if (resulst){
			save.setVisibility(View.VISIBLE);
			author.setVisibility(View.GONE);
			result.setText("Subdomain from "+urla+"\n"+data);
			}
			else{	
				result.setText("Failed search subdomain from "+urla+" :(");
				save.setVisibility(View.GONE);
				author.setVisibility(View.VISIBLE);
			}
			load.setVisibility(View.GONE);
			super.onPostExecute(resulst);
		}
		
	}
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = 
			(InputMethodManager) activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
			activity.getCurrentFocus().getWindowToken(), 0);
	}
}
