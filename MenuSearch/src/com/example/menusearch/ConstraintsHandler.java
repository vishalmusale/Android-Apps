package com.example.menusearch;

import java.util.Calendar;

import android.widget.Toast;

public class ConstraintsHandler {
	
	public static int motionType;
	
	// To get one of the 5 parameters
		public static String getParameter() {
			// TODO Auto-generated method stub
			
			// today's day which will return number.
			// Sunday =1 and Saturday = 7
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_WEEK);
			
			// current time
			int hh = calendar.get(Calendar.HOUR_OF_DAY);
			int mm = calendar.get(Calendar.MINUTE);
			
			// To create hour part of the string
			String h = null;
			if(hh>=6 && hh<=11)
				h = "1";	// Morning
			else if(hh>=12 && hh<=13)
				h = "2";	// Noon
			else if(hh>=13 && hh<=17)
				h = "3";	// Afternoon
			else if(hh>=18 && hh<=20)
				h = "4";	// Evening
			else if(hh>=21 && hh<=23)
				h = "5";	// Night
			else if(hh>=00 && hh<=5)
				h = "6";	// Late Night
			
			String dayTime = "c" + day + h;
			
			//List<String> displayList = new ArrayList<String>();
			//displayList= get7None(dayTime);
			
			return dayTime;
		}

		public static void setMotion(int motionType2) {
			// TODO Auto-generated method stub
			motionType = motionType2;
		}

		public static String getMotion() {
			// TODO Auto-generated method stub
			if(motionType == 3)
				return DBHelper.TABLE_NAME_RUN;
			else if(motionType == 4)
				return DBHelper.TABLE_NAME_WALK;
			else if(motionType == 5)
				return DBHelper.TABLE_ALL;
			else
				return DBHelper.TABLE_NAME_WALK;
		}

		
}
