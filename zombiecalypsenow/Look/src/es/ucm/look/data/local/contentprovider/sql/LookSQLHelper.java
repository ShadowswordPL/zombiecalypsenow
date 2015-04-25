package es.ucm.look.data.local.contentprovider.sql;

import es.ucm.look.data.remote.LookProperties;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LookSQLHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "lookDataBase.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String MAIN_TABLENAME = "mainTable";
	public static final String PROPERTIES_TABLENAME = "propertiesTable";

	public static final String MAIN_PROJECTION_ALL_FIELDS[] = new String[] {
		LookProperties.FIELD_ID, LookProperties.FIELD_POS_X, LookProperties.FIELD_POS_Y, LookProperties.FIELD_POS_Z, LookProperties.FIELD_LAST_UPDATE};
	
	public static final String PROPERTIES_PROJECTION_ALL_FIELDS[] = new String[] {
		LookProperties.FIELD_ID, LookProperties.FIELD_PROPERTY , LookProperties.FIELD_VALUE};

	
	public LookSQLHelper(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * Esto se ejecutara solo si se va a crear la base de datos. Si solo se
		 *  va a acceder a ella no se ejecutara
		 */
		
		db.execSQL("CREATE TABLE " + MAIN_TABLENAME + " (" + 
				LookProperties.FIELD_ID	+ " INTEGER PRIMARY KEY," 
				+ LookProperties.FIELD_POS_X	+ " REAL," 
				+ LookProperties.FIELD_POS_Y	+ " REAL," 
				+ LookProperties.FIELD_POS_Z + " REAL," 
				+ LookProperties.FIELD_LAST_UPDATE + " DATE)");
		
		db.execSQL("CREATE TABLE " + PROPERTIES_TABLENAME + " (" + 
				LookProperties.FIELD_ID	+ " INTEGER," 
				+ LookProperties.FIELD_PROPERTY	+ " TEXT," 
				+ LookProperties.FIELD_VALUE + " TEXT," +
				"PRIMARY KEY(" + LookProperties.FIELD_ID+","+ LookProperties.FIELD_PROPERTY + "))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

	}
	
	@Override
	public void onOpen (SQLiteDatabase db){

	}

}
