package com.example.menusearch;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	// Object of ConstraintsAndItems & SQLController Class
	private ConstraintsAndItems caiList = new ConstraintsAndItems();
	
	// Size of all types of lists in ConstraintsAndItems Class
	public int totalSize = caiList.foodlist.size() + caiList.buylist.size() + caiList.funlist.size();
	
	// Database Information
	private static final String DB_NAME = "MENU_BASED_SEARCH.db";
	private static final int DB_VERSION = 1;
	
// Table Name
	public static final String TABLE_ALL = "TALL";
	public static final String TABLE_NAME_WALK = "TWALK";
	public static final String TABLE_NAME_DRIVE = "TDRIVE";
	public static final String TABLE_NAME_BIKE = "TBIKE";
	public static final String TABLE_NAME_RUN = "TRUN";

	// Table columns for ALL Table
	public static final String _ID = "_id";
	public static final String ITEMS = "items";
	public static final String CONTEXT = "context";
	public static final String PROB = "probability";
	public static final String C11 = "c11",C12 = "c12",C13 = "c13",C14 = "c14",C15 = "c15",C16 = "c16";
	public static final String C21 = "c21",C22 = "c22",C23 = "c23",C24 = "c24",C25 = "c25",C26 = "c26";
	public static final String C31 = "c31",C32 = "c32",C33 = "c33",C34 = "c34",C35 = "c35",C36 = "c36";
	public static final String C41 = "c41",C42 = "c42",C43 = "c43",C44 = "c44",C45 = "c45",C46 = "c46";
	public static final String C51 = "c51",C52 = "c52",C53 = "c53",C54 = "c54",C55 = "c55",C56 = "c56";
	public static final String C61 = "c61",C62 = "c62",C63 = "c63",C64 = "c64",C65 = "c65",C66 = "c66";
	public static final String C71 = "c71",C72 = "c72",C73 = "c73",C74 = "c74",C75 = "c75",C76 = "c76";
	
	// Creating table query
	// ALL TABLE
	private static final String CREATE_TABLE_ALL = "create table " + TABLE_ALL + "(" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEMS + " TEXT NOT NULL, " + CONTEXT + " TEXT NOT NULL, " + 
			C11 + " NUMERIC DEFAULT 0.0, " + C12 + " NUMERIC DEFAULT 0.0, " + C13 + " NUMERIC DEFAULT 0.0, " + 
			C14 + " NUMERIC DEFAULT 0.0, " + C15 + " NUMERIC DEFAULT 0.0, " + C16 + " NUMERIC DEFAULT 0.0, " + 
			
			C21 + " NUMERIC DEFAULT 0.0, " + C22 + " NUMERIC DEFAULT 0.0, " + C23 + " NUMERIC DEFAULT 0.0, " + 
			C24 + " NUMERIC DEFAULT 0.0, " + C25 + " NUMERIC DEFAULT 0.0, " + C26 + " NUMERIC DEFAULT 0.0, " +
			
			C31 + " NUMERIC DEFAULT 0.0, " + C32 + " NUMERIC DEFAULT 0.0, " + C33 + " NUMERIC DEFAULT 0.0, " + 
			C34 + " NUMERIC DEFAULT 0.0, " + C35 + " NUMERIC DEFAULT 0.0, " + C36 + " NUMERIC DEFAULT 0.0, " + 
			
			C41 + " NUMERIC DEFAULT 0.0, " + C42 + " NUMERIC DEFAULT 0.0, " + C43 + " NUMERIC DEFAULT 0.0, " + 
			C44 + " NUMERIC DEFAULT 0.0, " + C45 + " NUMERIC DEFAULT 0.0, " + C46 + " NUMERIC DEFAULT 0.0, " + 
			
			C51 + " NUMERIC DEFAULT 0.0, " + C52 + " NUMERIC DEFAULT 0.0, " + C53 + " NUMERIC DEFAULT 0.0, " + 
			C54 + " NUMERIC DEFAULT 0.0, " + C55 + " NUMERIC DEFAULT 0.0, " + C56 + " NUMERIC DEFAULT 0.0, " + 
			
			C61 + " NUMERIC DEFAULT 0.0, " + C62 + " NUMERIC DEFAULT 0.0, " + C63 + " NUMERIC DEFAULT 0.0, " + 
			C64 + " NUMERIC DEFAULT 0.0, " + C65 + " NUMERIC DEFAULT 0.0, " + C66 + " NUMERIC DEFAULT 0.0, " + 
			
			C71 + " NUMERIC DEFAULT 0.0, " + C72 + " NUMERIC DEFAULT 0.0, " + C73 + " NUMERIC DEFAULT 0.0, " + 
			C74 + " NUMERIC DEFAULT 0.0, " + C75 + " NUMERIC DEFAULT 0.0, " + C76 + " NUMERIC DEFAULT 0.0);";
	
	// WALK TABLE
	public final String CREATE_TABLE_WALK = "create table " + TABLE_NAME_WALK + " as select " + _ID
			+ ", " + ITEMS + ", " + CONTEXT + ", " + C11 + " as " + PROB +  " from " + TABLE_ALL;
	//private static final String CREATE_TABLE_WALK_2 = "ALTER TABLE " + TABLE_NAME_WALK + " ADD COLUMN " + PROB + " NUMERIC DEFAULT 0.0;";
	
	// RUN TABLE
	public final String CREATE_TABLE_RUN = "create table " + TABLE_NAME_RUN + " as select " + _ID
			+ ", " + ITEMS + ", " + CONTEXT + ", " + PROB + " from " + TABLE_NAME_WALK;
	
	// BIKE TABLE
	public final String CREATE_TABLE_BIKE = "create table " + TABLE_NAME_BIKE + " as select " + _ID
			+ ", " + ITEMS + ", " + CONTEXT + ", " + PROB + " from " + TABLE_NAME_RUN;
	
	// DRIVE TABLE
	public final String CREATE_TABLE_DRIVE = "create table " + TABLE_NAME_DRIVE + " as select " + _ID
			+ ", " + ITEMS + ", " + CONTEXT + ", " + PROB + " from " + TABLE_NAME_RUN;
	
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL(CREATE_TABLE_ALL);	// First create ALL table only....
		// After adding data into this table, other tables are created
		
		// Other remaining tables are created....
		db.execSQL(CREATE_TABLE_WALK);	// Walk
		db.execSQL(CREATE_TABLE_RUN);	// RUN
		db.execSQL(CREATE_TABLE_BIKE);	// BIKE
		db.execSQL(CREATE_TABLE_DRIVE);	// DRIVE
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WALK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RUN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BIKE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DRIVE);
		onCreate(db);
	}
		
}
