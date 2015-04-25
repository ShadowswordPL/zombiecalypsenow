package es.ucm.look.data.local.contentprovider.sql;

import es.ucm.look.data.remote.LookProperties;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class LookSQLContentProvider extends ContentProvider {

	// TABLES
	public static final String URI_FROM_MAINTABLE = "lookMain";
	public static final String URI_FROM_PROPERTIESTABLE = "lookProperties";

	// Set the URI
	public static final String AUTHORITY_PART = "es.ucm.look.data.local.contentprovider.sql";
	public static final String CONTENT_PREFIX = "content://";
	
	public static final Uri MAIN_CONTENT_URI = Uri.parse(CONTENT_PREFIX
			+ AUTHORITY_PART + "/"+URI_FROM_MAINTABLE);

	public static final Uri PROPERTIES_CONTENT_URI = Uri.parse(CONTENT_PREFIX
			+ AUTHORITY_PART + "/"+URI_FROM_PROPERTIESTABLE);

	public static final String INVALID_URI_MESSAGE = "Invalid Uri: ";
	public static final String EQUALS = "=";
	public static final int CODE_ALL_ITEMS = 1;
	public static final int CODE_SINGLE_ITEM = 2;
	public static final String MIME_TYPE_ALL_ITEMS = "vnd.android.cursor.dir/vnd.es.ucm.look.data.contentprovider.sql";
	public static final String MIME_TYPE_SINGLE_ITEM = "vnd.android.cursor.item/vnd.es.ucm.look.data.contentprovider.sql";

	/**
	 * @uml.property  name="database"
	 * @uml.associationEnd  
	 */
	private SQLiteDatabase database;
	/**
	 * @uml.property  name="dbHelper"
	 * @uml.associationEnd  
	 */
	private LookSQLHelper dbHelper;

	public static final UriMatcher MAIN_URI_MATCHER;
	// Initialized URI_MATCHER
	static {
		MAIN_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		MAIN_URI_MATCHER.addURI(AUTHORITY_PART, "lookMain", CODE_ALL_ITEMS);
		MAIN_URI_MATCHER.addURI(AUTHORITY_PART, "lookMain/#",
				CODE_SINGLE_ITEM);
	}

	public static final UriMatcher PROPERTIES_URI_MATCHER;
	// Initialized URI_MATCHER
	static {
		PROPERTIES_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		PROPERTIES_URI_MATCHER.addURI(AUTHORITY_PART, "lookProperties",
				CODE_ALL_ITEMS);
		PROPERTIES_URI_MATCHER.addURI(AUTHORITY_PART, "lookProperties/#",
				CODE_SINGLE_ITEM);
	}

	// METHODS

	@Override
	public boolean onCreate() {
		dbHelper = new LookSQLHelper(getContext());
		database = dbHelper.getWritableDatabase();
		return database != null && database.isOpen();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory(); /* The database is closed if more memory is needed */
		this.dbHelper.close();
	}

	/**
	 * Get the SQL associated
	 * 
	 * @return
	 */
	private SQLiteDatabase getOrOpenDatabase() {
		SQLiteDatabase db = null;
		if (this.database != null && database.isOpen()) {
			db = this.database;
		} else {
			db = dbHelper.getWritableDatabase();
		}
		return db;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int rowsAffected = 0;

		String getTable = uri.getPathSegments().get(0);

		if (getTable.contentEquals(URI_FROM_MAINTABLE)) {

			switch (MAIN_URI_MATCHER.match(uri)) {
			case CODE_ALL_ITEMS:
				rowsAffected = this.getOrOpenDatabase().delete(
						LookSQLHelper.MAIN_TABLENAME, where, whereArgs);
				break;
			case CODE_SINGLE_ITEM:
				String singleRecordId = uri.getPathSegments().get(1);
				rowsAffected = this.getOrOpenDatabase().delete(
						LookSQLHelper.MAIN_TABLENAME,
						LookProperties.FIELD_ID + EQUALS + singleRecordId,
						whereArgs);
				break;
			default:
				throw new IllegalArgumentException(INVALID_URI_MESSAGE + uri);
			}

		} else if (getTable.contentEquals(URI_FROM_PROPERTIESTABLE)) {

			switch (PROPERTIES_URI_MATCHER.match(uri)) {
			case CODE_ALL_ITEMS:
				rowsAffected = this.getOrOpenDatabase().delete(
						LookSQLHelper.PROPERTIES_TABLENAME, where, whereArgs);
				break;
			case CODE_SINGLE_ITEM:
				// TODO comprobar
				String singleRecordId = uri.getPathSegments().get(1);
				rowsAffected = this.getOrOpenDatabase().delete(
						LookSQLHelper.PROPERTIES_TABLENAME,
						LookProperties.FIELD_ID + EQUALS + singleRecordId,
						whereArgs);
				break;
			default:
				throw new IllegalArgumentException(INVALID_URI_MESSAGE + uri);
			}
		}

		return rowsAffected;
	}

	// TODO
	@Override
	public String getType(Uri uri) {
		switch (MAIN_URI_MATCHER.match(uri)) {
		case CODE_ALL_ITEMS:
			return MIME_TYPE_ALL_ITEMS;
		case CODE_SINGLE_ITEM:
			return MIME_TYPE_SINGLE_ITEM;
		default:
			throw new IllegalArgumentException(INVALID_URI_MESSAGE + uri);
		}
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {

		String getTable = arg0.getPathSegments().get(0);

		if (getTable.contentEquals(URI_FROM_MAINTABLE)) {

			long rowID = getOrOpenDatabase().insert(
					LookSQLHelper.MAIN_TABLENAME, null, arg1);
			Uri newRecordUri = null;
			switch (MAIN_URI_MATCHER.match(arg0)) {
			case CODE_ALL_ITEMS:
				if (rowID > 0) {
					newRecordUri = ContentUris.withAppendedId(
							MAIN_CONTENT_URI, rowID);
				}
				break;
			default:
				throw new IllegalArgumentException(INVALID_URI_MESSAGE + arg0);
			}
			
			return newRecordUri;

		} else if (getTable.contentEquals(URI_FROM_PROPERTIESTABLE)) {

			long rowID = getOrOpenDatabase().insert(
					LookSQLHelper.PROPERTIES_TABLENAME, null, arg1);
			Uri newRecordUri = null;
			switch (PROPERTIES_URI_MATCHER.match(arg0)) {
			case CODE_ALL_ITEMS:
				if (rowID > 0) {
					newRecordUri = ContentUris.withAppendedId(
							PROPERTIES_CONTENT_URI, rowID);
				}
				break;
			default:
				throw new IllegalArgumentException(INVALID_URI_MESSAGE + arg0);
			}
			return newRecordUri;

		}

		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		Cursor cursor = null;
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

		String getTable = uri.getPathSegments().get(0);

		if (getTable.contentEquals(URI_FROM_MAINTABLE)) {

			switch (MAIN_URI_MATCHER.match(uri)) {
			case CODE_SINGLE_ITEM:
				String id = uri.getPathSegments().get(1);
				qBuilder.appendWhere(LookProperties.FIELD_ID + EQUALS + id);
				break;
			case UriMatcher.NO_MATCH:
				throw new IllegalArgumentException(INVALID_URI_MESSAGE + uri);
			}

			qBuilder.setTables(LookSQLHelper.MAIN_TABLENAME);

		} else if (getTable.contentEquals(URI_FROM_PROPERTIESTABLE)) {
			
			
			
			qBuilder.setTables(LookSQLHelper.PROPERTIES_TABLENAME);
		}
		cursor = qBuilder.query(getOrOpenDatabase(), projection, selection,
				selectionArgs, null, null, null);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		int rowsAffected = 0;

		String getTable = uri.getPathSegments().get(0);

		if (getTable.contentEquals(URI_FROM_MAINTABLE)) {

			String recordId = null;
			switch (MAIN_URI_MATCHER.match(uri)) {
			case CODE_ALL_ITEMS:

				rowsAffected = getOrOpenDatabase()
						.update(LookSQLHelper.MAIN_TABLENAME, values, where,
								whereArgs);
				break;
			case CODE_SINGLE_ITEM:
				recordId = uri.getPathSegments().get(1);
				rowsAffected = getOrOpenDatabase().update(
						LookSQLHelper.MAIN_TABLENAME, values,
						LookProperties.FIELD_ID + EQUALS + recordId, whereArgs);
				break;
			default:
				throw new IllegalArgumentException(INVALID_URI_MESSAGE + uri);
			}

		} else if (getTable.contentEquals(URI_FROM_PROPERTIESTABLE)) {
			//String recordId = null;
			switch (PROPERTIES_URI_MATCHER.match(uri)) {
			case CODE_ALL_ITEMS:

				rowsAffected = getOrOpenDatabase().update(
						LookSQLHelper.PROPERTIES_TABLENAME, values, where,
						whereArgs);
				break;
			case CODE_SINGLE_ITEM:
				//recordId = uri.getPathSegments().get(1);
				rowsAffected = getOrOpenDatabase().update(
						LookSQLHelper.PROPERTIES_TABLENAME, values, null,
						whereArgs);// TODO
				break;
			default:
				throw new IllegalArgumentException(INVALID_URI_MESSAGE + uri);
			}
		}
		return rowsAffected;
	}

}
