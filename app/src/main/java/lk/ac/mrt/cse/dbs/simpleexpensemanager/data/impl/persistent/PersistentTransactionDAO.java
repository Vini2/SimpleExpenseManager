package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.MyDBFields;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.MyDBHandler;

/**
 * Created by User on 12/4/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO {

    private Context context;

    //Constructor
    public PersistentTransactionDAO(Context context) {
        this.context = context;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        MyDBHandler handler = MyDBHandler.getInstance(context);
        SQLiteDatabase db = handler.getReadableDatabase();

        //Save transaction details to the transaction_log table
        ContentValues values = new ContentValues();
        values.put(MyDBFields.COLUMN_ACCOUNT_NO, accountNo);
        values.put(MyDBFields.COLUMN_TRANSACTION_DATE, convertDateToString(date));
        values.put(MyDBFields.COLUMN_TRANSACTION_AMOUNT, amount);
        values.put(MyDBFields.COLUMN_EXPENSE_TYPE, expenseType.toString());

        db.insert(MyDBFields.TABLE_TRANSACTION_LOG,null,values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return getPaginatedTransactionLogs(0);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        MyDBHandler handler = MyDBHandler.getInstance(context);
        SQLiteDatabase db = handler.getReadableDatabase();

        //Query to get details of all the transactions
        String query = "SELECT "+ MyDBFields.COLUMN_ACCOUNT_NO + ", " +
                MyDBFields.COLUMN_TRANSACTION_DATE + ", " +
                MyDBFields.COLUMN_EXPENSE_TYPE+", " +
                MyDBFields.COLUMN_TRANSACTION_AMOUNT +
                " FROM " + MyDBFields.TABLE_TRANSACTION_LOG + " ORDER BY " + MyDBFields.COLUMN_TRANSACTION_ID + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Transaction> transactionLogs = new ArrayList<>();

        //Add the transaction details to a list
        while (cursor.moveToNext())
        {
            try {

                ExpenseType expenseType = null;
                if (cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_EXPENSE_TYPE)).equals(ExpenseType.INCOME.toString())) {
                    expenseType = ExpenseType.INCOME;
                }
                else{
                    expenseType = ExpenseType.EXPENSE;
                }

                String dateString = cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_TRANSACTION_DATE));
                Date date = convertStringToDate(dateString);

                Transaction tans = new Transaction(
                        date,
                        cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_ACCOUNT_NO)),
                        expenseType,
                        cursor.getDouble(cursor.getColumnIndex(MyDBFields.COLUMN_TRANSACTION_AMOUNT)));

                transactionLogs.add(tans);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        //Return the list of transactions
        return transactionLogs;
    }

    //Method to convert a date object to a string
    public static String convertDateToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(date);
        return dateString;

    }

    //Method to convert a string to a date object
    public static Date convertStringToDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date strDate = dateFormat.parse(date);
        return strDate;
    }

}
