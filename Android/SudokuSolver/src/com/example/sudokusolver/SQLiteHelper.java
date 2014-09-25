package com.example.sudokusolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	//Database object
	private SQLiteDatabase db;
	
	//Context
	private Context context;
	
	//Constants for database
	//Name of database
	private static final String DATABASE_NAME = "NeuralNet.db";
	//Current version of database
	private static final int DATABASE_VERSION = 8;
	
	//Constants for tables
	public static final String SHAPES = "shapes";
	public static final String THETA1 = "theta1";
	public static final String THETA2 = "theta2";

	public static final String ID_COLUMN = "_id";
	public static final String WEIGHTS_COLUMN = "weights";
	public static final String DIMENSIONS = "dimensions";
	private static final String[] SHAPES_QUERY_COLUMNS = {DIMENSIONS};
	private static final String[] WEIGHT_QUERY_COLUMNS = {WEIGHTS_COLUMN};
	private static final String[] DATA_TABLES = {THETA1, THETA2};
	private static final String[] ALL_TABLES = {THETA1, THETA2, SHAPES};
	
	//Constructor to access database
	public SQLiteHelper(Context c) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
		context = c;
		db = this.getWritableDatabase();
		if (DatabaseUtils.queryNumEntries(db, SHAPES) == 0) {
			long startTime = System.currentTimeMillis();
			insertWeights();
		    long endTime = System.currentTimeMillis();
		    System.out.println("insert into db: "
				       + (endTime-startTime) + "ms");
		}
	}

	//Creates the database
	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i < DATA_TABLES.length; i++) {
			db.execSQL(getCreateCommand(DATA_TABLES[i]));
		}
		db.execSQL("CREATE TABLE " + SHAPES + "("
				  + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				  + DIMENSIONS + " TEXT)");
	}
	
	private void insertWeights() {
		BufferedReader reader = null;
		NeuralNet tempNet = null;
		String[] temp;
		try {
			InputStream file = context.getResources().openRawResource(R.raw.nnregdataset);
		    reader = new BufferedReader(new InputStreamReader(file));
		    
		    String line = reader.readLine();
		    insertShape(line);

		    int count = 0;
		    int mNum = 0;
		    while ((line = reader.readLine()) != null) {
				if (line.equals("")) {
				    mNum++;
				    count = 0;
				} else {
				    temp = line.split(" ");
				    for (int c = 0; c < temp.length; c++) {
				    	insertWeight(Double.parseDouble(temp[c]), DATA_TABLES[mNum]);
				    }
				    count++;
				}
		    }
		    reader.close();

		} catch(IOException e) {
		    e.printStackTrace();
		}
	}
	
	private String getCreateCommand(String name) {
		return "CREATE TABLE " + name + "("
				  + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				  + WEIGHTS_COLUMN + " REAL)";
	}

	//Upgrades the databse, drop older versions of tables
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (int i = 0; i < ALL_TABLES.length; i++) {
			db.execSQL("DROP TABLE IF EXISTS " + ALL_TABLES[i]);
		}
		onCreate(db);
	}
	
	//Return weights for theta1
	public Cursor getTheta1() {
		return db.query(THETA1, WEIGHT_QUERY_COLUMNS, null,
						null, null, null, null, null);
	}
	
	//Return weights for theta2
	public Cursor getTheta2() {
		return db.query(THETA2, WEIGHT_QUERY_COLUMNS, null,
						null, null, null, null, null);
	}
	
	//Return weights for hidden layer bias
	public Cursor getShapes() {
		return db.query(SHAPES, SHAPES_QUERY_COLUMNS, null,
						null, null, null, null, null);
	}
	
	//Add an entry
	private void insertWeight(Double weight, String table) {
		ContentValues val = new ContentValues();
		val.put(WEIGHTS_COLUMN, weight);
		db.insert(table, null, val);
	}
	
	private void insertShape(String d) {
		ContentValues val = new ContentValues();
		val.put(DIMENSIONS, d);
		db.insert(SHAPES, null, val);
	}
	
	//Close connection to database
	public void end() {
		db.close();
	}
}