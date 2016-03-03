package com.example.menusearch;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Home extends Activity  implements SensorEventListener {

	// FOR ACCELEROMETER
	private SensorManager mSensorManager;
	  private float mAccel; // acceleration apart from gravity
	  private float mAccelCurrent; // current acceleration including gravity
	  private float mAccelLast; // last acceleration including gravity
	
	  // Array of buttons - instead of writing 8 stmts.
	public Button[] buttons;
	public int pass=0;
	
	public DBHelper dbh = new DBHelper(null);
			
	// Object of ProbabilityDB class
	private SQLController dbcon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
		
		// FOR ACCELEROMETER 
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;
	    
		// Connects to the database
		dbcon = new SQLController(this);
		dbcon.open();
		
		// Initialization of buttons
		buttons = new Button[8];
		for(int i=0; i<buttons.length; i++) {
		
			String buttonID = "btn" + (i+1);
	
			int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
			buttons[i] = ((Button) findViewById(resID));
		}
		
		passHandler(pass);
		//showToast(pass,l);		
	}
	
	// FOR ACCELEROMETER 
	private final SensorEventListener mSensorListener = new SensorEventListener() {

	    public void onSensorChanged(SensorEvent se) {
	      float x = se.values[0];
	      float y = se.values[1];
	      float z = se.values[2];
	      
	      mAccelLast = mAccelCurrent;
	      mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
	      float delta = mAccelCurrent - mAccelLast;
	      mAccel = mAccel * 0.9f + delta; // perform low-cut filter
	      
	      
	      // To check the Motion
	      if (mAccel > 10) {
	    	  
			    ConstraintsHandler.setMotion(3);	// Running
			}
	      else if (mAccel > 1 && mAccel < 10) {
	    	  
	    	  ConstraintsHandler.setMotion(4);		// Walking
			}
	      else if (mAccel < 1) {
	    	 
	    	  ConstraintsHandler.setMotion(5);		// None
	    	  
			}
	    }

	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    }
	  };

	// FOR ACCELEROMETER 
	  @Override
	  protected void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	  }
	// FOR ACCELEROMETER 
	  @Override
	  protected void onPause() {
	    mSensorManager.unregisterListener(mSensorListener);
	    super.onPause();
	  }

	
	/*
	 * Pass 0 - Motion
	 * Pass 1 - Based on Probability
	 * Pass 2 - Context List
	 * Pass 3 - Food
	 * Pass 4 - Fun
	 * Pass 5 - Buy
	 */
	private void passHandler(int p) {
		// TODO Auto-generated method stub
		int pass = p;
		List<String> l = null;
		switch (pass) {
        case 0:  
        	String tbl = ConstraintsHandler.getMotion();
        	
        	if(tbl == DBHelper.TABLE_ALL)
        		passHandler(1);
        	else{
        		l = getButtonName(tbl, DBHelper.PROB);
            	displayItems(0, p, l);
        	}
        	break;
        case 1:  
        	String daytime = ConstraintsHandler.getParameter();
        	
        	l = getButtonName(DBHelper.TABLE_ALL, daytime);
        	displayItems(0, p, l);
        	break;
        case 2:  
        	//l.clear();
        	l = ConstraintsAndItems.contextlist;
        	//displayItems(0, p, l);
        	pass2(l);
        	break;
        case 3:  
        	//l.clear();
        	l = ConstraintsAndItems.foodlist;
        	displayItems(0, p, l);
        	break;
        case 4:  
        	//l.clear();
        	l = ConstraintsAndItems.funlist;
        	displayItems(0, p, l);
        	break;
        case 5:  
        	//l.clear();
        	l = ConstraintsAndItems.buylist;
        	displayItems(0, p, l);
        	break;
        	
        default: break;
    }
	}
	
	
	// to get list of items from database...
	private List<String> getButtonName(String table, String column) {
		// TODO Auto-generated method stub
		
		String tn = table, col = column;
		
		List<String> l;
		l = dbcon.get7None(tn, col);
		
		return l;
	}

	// To display the button names
	private void displayItems(int nextB, int pass, List<String> l) {
		// TODO Auto-generated method stub
		int temp, NxtButton = nextB;
		int Pass = pass;
		final List<String> list = l;
		
		// To display 1st 7 items
		for (int i = 1; i < buttons.length ; i++)
		{
			if(list.size() - ((7 * NxtButton)+i) >=0)
			{
				temp = (NxtButton*7) + (i-1);
				
				buttons[i-1].setText(list.get(temp));
				buttons[i-1].setVisibility(View.VISIBLE);
				//pass ++;
				buttons[7].setText("Other");
				buttons[7].setVisibility(View.VISIBLE);
			}
			else 
				break;
			
		}
		
		NxtButton++;
		final int lp1 = NxtButton;	// to send the value of pass to the recursive function call
		final int samePass = Pass;
		
		final String daytime = ConstraintsHandler.getParameter();
		   
		buttons[0].setOnClickListener(new OnClickListener() {

			   public void onClick(View v) {
				   // To Update Probability value in the table
				   float ogVal= dbcon.getValue(daytime, buttons[0].getText().toString());
				   float newVal = ogVal+1;
				   dbcon.update(DBHelper.TABLE_ALL,daytime,newVal, buttons[0].getText().toString());
				   
			   // Search for restaurants nearby
				Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + buttons[0].getText().toString());
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				startActivity(mapIntent);
			   }
			});;
		buttons[1].setOnClickListener(new OnClickListener() {

			   public void onClick(View v) {
				// To Update Probability value in the table
				   float ogVal= dbcon.getValue(daytime, buttons[1].getText().toString());
				   float newVal = ogVal+1;
				   dbcon.update(DBHelper.TABLE_ALL,daytime,newVal, buttons[1].getText().toString());

			   // Search for restaurants nearby
				Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + buttons[1].getText().toString());
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				startActivity(mapIntent);
				   
			   }
			});;
		buttons[2].setOnClickListener(new OnClickListener() {

			   public void onClick(View v) {
				// To Update Probability value in the table
				   float ogVal= dbcon.getValue(daytime, buttons[2].getText().toString());
				   float newVal = ogVal+1;
				   dbcon.update(DBHelper.TABLE_ALL,daytime,newVal, buttons[2].getText().toString());

			   // Search for restaurants nearby
				Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + buttons[2].getText().toString());
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				startActivity(mapIntent);
				   
			   }
			});;
		buttons[3].setOnClickListener(new OnClickListener() {

			   public void onClick(View v) {
				// To Update Probability value in the table
				   float ogVal= dbcon.getValue(daytime, buttons[3].getText().toString());
				   float newVal = ogVal+1;
				   dbcon.update(DBHelper.TABLE_ALL,daytime,newVal, buttons[3].getText().toString());

			   // Search for restaurants nearby
				Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + buttons[3].getText().toString());
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				startActivity(mapIntent);
				   
			   }
			});;
		buttons[4].setOnClickListener(new OnClickListener() {

			   public void onClick(View v) {
				// To Update Probability value in the table
				   float ogVal= dbcon.getValue(daytime, buttons[4].getText().toString());
				   float newVal = ogVal+1;
				   dbcon.update(DBHelper.TABLE_ALL,daytime,newVal, buttons[4].getText().toString());

			   // Search for restaurants nearby
				Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + buttons[4].getText().toString());
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				startActivity(mapIntent);
				   
			   }
			});;
		buttons[5].setOnClickListener(new OnClickListener() {

			   public void onClick(View v) {
				// To Update Probability value in the table
				   float ogVal= dbcon.getValue(daytime, buttons[5].getText().toString());
				   float newVal = ogVal+1;
				   dbcon.update(DBHelper.TABLE_ALL,daytime,newVal, buttons[5].getText().toString());

			   // Search for restaurants nearby
				Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + buttons[5].getText().toString());
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				startActivity(mapIntent);
				   
			   }
			});;
		buttons[6].setOnClickListener(new OnClickListener() {

			   public void onClick(View v) {
				// To Update Probability value in the table
				   float ogVal= dbcon.getValue(daytime, buttons[6].getText().toString());
				   float newVal = ogVal+1;
				   dbcon.update(DBHelper.TABLE_ALL,daytime,newVal, buttons[6].getText().toString());

			   // Search for restaurants nearby
				Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + buttons[6].getText().toString());
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				startActivity(mapIntent);
				   
			   }
			});;
		// "Other" button to show remaining items
		if(7 * NxtButton < list.size())
		{
			buttons[7].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            	
            	disableButtons();
            	displayItems(lp1,samePass,list);
            }
          });
		}
		else if(Pass < 2)		// For Motion and All Table
		{
			final int DifferentPass = Pass+1;
			buttons[7].setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View view) {
	            	disableButtons();
	    			passHandler(DifferentPass);
	            }
	          });
		}
		else
		{
			buttons[7].setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View view) {
	            	disableButtons();
	    			passHandler(2);
	            }
	          });
		}
		
		
	}
	
	 @Override
	 public void onBackPressed()
	 {      
	//Code for update
		 
	 } 
	
	//To disable the buttons after every pass, so that name of the button in previous pass.
	protected void disableButtons() {
		// TODO Auto-generated method stub
		for (int i = 1; i <= buttons.length ; i++)
		{
			buttons[i-1].setVisibility(View.INVISIBLE);
		}
	}

	
	private void pass2(List<String> l) {
		// TODO Auto-generated method stub
		
		final List<String> list = l;
		
		// To display 3 contexts
		for (int i = 1; i < 4 ; i++)
		{
			buttons[i-1].setText(list.get(i-1));
			buttons[i-1].setVisibility(View.VISIBLE);
		}
		//buttons[7].setVisibility(View.INVISIBLE);
		
		// assign listeners to buttons
		buttons[0].setOnClickListener(new OnClickListener() {

			   public void onClick(View v) {
				   disableButtons();
				   passHandler(3);
			   }
			});;
		buttons[1].setOnClickListener(new OnClickListener() {

			   public void onClick(View v) {
				   disableButtons();
				   passHandler(4);
			   }
			});;
		buttons[2].setOnClickListener(new OnClickListener() {

			   public void onClick(View v) {
				   disableButtons();
				   passHandler(5);
			   }
			});;
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	/* 	USELESS METHODS....
	 * 
	 * (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}
}
