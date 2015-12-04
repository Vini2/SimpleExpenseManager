package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by User on 12/4/2015.
 */
public class MyDBHandler extends SQLiteOpenHelper{

    private static MyDBHandler handler = null;

    //private static final
    private static final String DATABASE_NAME = "130366E.db";
    private static final int DATABASE_VERSION = 1;


    public MyDBHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static MyDBHandler getInstance(Context context)
    {
        if(handler == null)
            handler = new MyDBHandler(context);
        return handler;
    }

    //private static final

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_ACCOUNT_TABLE = "CREATE TABLE " +
                MyDBFields.TABLE_ACCOUNT + "("
                + MyDBFields.COLUMN_ACCOUNT_NO + " VARCHAR(255) NOT NULL PRIMARY KEY,"
                + MyDBFields.COLUMN_BANK_NAME + " VARCHAR(255) NULL,"
                + MyDBFields.COLUMN_ACCOUNT_HOLDER_NAME + " VARCHAR(255) NULL,"
                + MyDBFields.COLUMN_BALANCE + " DECIMAL(10,2) NULL )";

        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " +
                MyDBFields.TABLE_TRANSACTION_LOG + "("
                + MyDBFields.COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + MyDBFields.COLUMN_ACCOUNT_NO + " VARCHAR(255) NOT NULL,"
                + MyDBFields.COLUMN_TRANSACTION_DATE + " DATE NULL,"
                + MyDBFields.COLUMN_TRANSACTION_AMOUNT + " DECIMAL(10,2) NULL,"
                + MyDBFields.COLUMN_EXPENSE_TYPE + " VARCHAR(255) NULL, " +
                "FOREIGN KEY("+MyDBFields.COLUMN_ACCOUNT_NO+") REFERENCES "+MyDBFields.TABLE_ACCOUNT+"("+MyDBFields.COLUMN_ACCOUNT_NO+"))";

        sqLiteDatabase.execSQL(CREATE_ACCOUNT_TABLE);
        sqLiteDatabase.execSQL(CREATE_TRANSACTION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyDBFields.TABLE_ACCOUNT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyDBFields.TABLE_TRANSACTION_LOG);
        onCreate(sqLiteDatabase);

    }




}
