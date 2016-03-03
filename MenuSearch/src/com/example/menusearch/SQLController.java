package com.example.menusearch;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLController {
	private DBHelper dbHelper;
	private Context ourcontext;
	private SQLiteDatabase database;
	
	// Object of ConstraintsAndItems & SQLController Class
	private ConstraintsAndItems caiList = new ConstraintsAndItems();
	
	
	public SQLController(Context c) {
		ourcontext = c;
	}
	
	public SQLController open() throws SQLException {
		dbHelper = new DBHelper(ourcontext);
		database = dbHelper.getWritableDatabase();
		
		String countQuery = "SELECT  * FROM " + DBHelper.TABLE_ALL;
		Cursor cursor = database.rawQuery(countQuery, null);
	    int cnt = cursor.getCount();
		
	    // TO INSERT DEFAULT ITEMS IN THE TABLE	
	    if(cnt == 0)
	    	{
	    		defaultItems();	// FOR ALL TABLE
	    		
	    		// REST OF THE TABLES
	    		database.execSQL("INSERT INTO " + DBHelper.TABLE_NAME_WALK + " (_id,items,context,probability) SELECT _id,items,context,c11 FROM TALL");
	    		database.execSQL("INSERT INTO " + DBHelper.TABLE_NAME_RUN + " (_id,items,context,probability) SELECT _id,items,context,c11 FROM TALL");
	    		database.execSQL("INSERT INTO " + DBHelper.TABLE_NAME_BIKE + " (_id,items,context,probability) SELECT _id,items,context,c11 FROM TALL");
	    		database.execSQL("INSERT INTO " + DBHelper.TABLE_NAME_DRIVE + " (_id,items,context,probability) SELECT _id,items,context,c11 FROM TALL");
	    	}
		return this;

	}

	// To Insert items from FoodList
	private void defaultItems() {
		// TODO Auto-generated method stub
		
		for(int i=0; i<caiList.foodlist.size() ; i++)
		{
			insertDefault(caiList.foodlist.get(i), "Food");
		}
		
		// To Insert items from BuyList
		for(int i=0; i<caiList.buylist.size() ; i++)
		{
			insertDefault(caiList.buylist.get(i), "Buy");
		}
		
		// To Insert items from FunList
		for(int i=0; i<caiList.funlist.size() ; i++)
		{
			insertDefault(caiList.funlist.get(i), "Fun");
		}
	}

	public void close() {
		dbHelper.close();
	}
	
	// Normal Insertion for new items
	public void insert(String item, String context) {
		ContentValues contentValue = new ContentValues();
		contentValue.put(DBHelper.ITEMS, item);
		contentValue.put(DBHelper.CONTEXT, context);
		database.insert(DBHelper.TABLE_ALL, null, contentValue);
	}
	
	// Inserting Default items and values into the table
	private void insertDefault(String item, String context) {
		// TODO Auto-generated method stub
		
		ContentValues contentValue = new ContentValues();
		contentValue.put(DBHelper.ITEMS, item);
		contentValue.put(DBHelper.CONTEXT, context);
		database.insert(DBHelper.TABLE_ALL, null, contentValue);
	}

	// To get data from All Table
	public List<String> get7None(String tn, String col){

		String tableName = tn, column = col;
		
		List<String> list = new ArrayList<String>();	// List if selected items
		
		String selectQuery = "SELECT " + DBHelper.ITEMS + " FROM " + tableName + " ORDER BY " + column + " DESC LIMIT 7";
	    Cursor cursor = database.rawQuery(selectQuery, null);
	             
	    if (cursor.moveToFirst()) {
	        do {
	        	list.add(cursor.getString(cursor.getColumnIndex(DBHelper.ITEMS))); // get  the  data into list
	        } while (cursor.moveToNext());
	    }
	    //database.close();
	    return list;
	}
	
	// To get Average of the column
	public float getAvg(String tn, String daytime) {
		// TODO Auto-generated method stub
		String tableName = tn, column = daytime;
		// Querry
		String selectQuery = "SELECT AVG(" + column + ") " + "FROM " + tableName;
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    cursor.moveToFirst(); 
	    int i=cursor.getInt(0);
	    
	    return i;
	}
		
	// Get particular value in the TABLE_ALL
	public float getValue(String col, String itm) {
		
		String tableName = DBHelper.TABLE_ALL, column = col, item = itm;
		String countQuery = "SELECT " + column + " FROM " + tableName + " WHERE " + DBHelper.ITEMS + " = '" + item + "'" ;
	    Cursor cursor = database.rawQuery(countQuery, null);
	    float val = 0;
	    cursor.moveToFirst();
	    val = cursor.getFloat(0);
	    cursor.close();
	    return val;
	}
	
	public Cursor fetch() {
		String[] columns = new String[] { DBHelper.ITEMS };
		Cursor cursor = database.query(DBHelper.TABLE_ALL, columns, null,
				null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	// DBHelper.TABLE_ALL,daytime,val, buttons[1].getText().toString()
	public int update(String tn, String col, float val, String itm) {
		String tableName = tn, column = col, item = itm;
		float value = val;
		ContentValues contentValues = new ContentValues();
		contentValues.put(column, value);
		int i = database.update(DBHelper.TABLE_ALL, contentValues,
				DBHelper.ITEMS + " = " + "'" + item + "'", null );
		return i;
	}

	public void updateProbability(String tn, String col, float val, String itm) {
		// TODO Auto-generated method stub
		String tableName = tn, column = col, item = itm;
		float value = val;
		// Querry
		String selectQuery = "UPDATE " + tableName + " SET " + column + " = " + value +" WHERE " + DBHelper.ITEMS + " = " + "'" + item + "'";
	    database.rawQuery(selectQuery, null);
	    
	}

	
}
