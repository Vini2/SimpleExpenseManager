package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.MyDBFields;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.MyDBHandler;

/**
 * Created by User on 12/4/2015.
 */
public class PersistentAccountDAO implements AccountDAO {

    private Context context;

    //Constructor
    public PersistentAccountDAO(Context context) {
        this.context = context;
    }

    @Override
    public List<String> getAccountNumbersList() {

        //Open the database connection
        MyDBHandler handler = MyDBHandler.getInstance(context);
        SQLiteDatabase db = handler.getReadableDatabase();

        //Query to select all account numbers from the account table
        String query = "SELECT "+ MyDBFields.COLUMN_ACCOUNT_NO+" FROM " + MyDBFields.TABLE_ACCOUNT+" ORDER BY " + MyDBFields.COLUMN_ACCOUNT_NO + " ASC";

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<String> resultSet = new ArrayList<>();

        //Add account numbers to a list
        while (cursor.moveToNext())
        {
            resultSet.add(cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_ACCOUNT_NO)));
        }

        cursor.close();

        //Return the list of account numbers
        return resultSet;

    }

    @Override
    public List<Account> getAccountsList() {

        MyDBHandler handler = MyDBHandler.getInstance(context);
        SQLiteDatabase db = handler.getReadableDatabase();

        //Query to select all the details about all the accounts in the account table
        String query = "SELECT * FROM " + MyDBFields.TABLE_ACCOUNT+" ORDER BY "+MyDBFields.COLUMN_ACCOUNT_NO+" ASC";

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Account> resultSet = new ArrayList<>();

        //Add account details to a list
        while (cursor.moveToNext())
        {
            Account account = new Account(cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_ACCOUNT_NO)),
                    cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_BANK_NAME)),
                    cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_ACCOUNT_HOLDER_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(MyDBFields.COLUMN_BALANCE)));

            resultSet.add(account);
        }

        cursor.close();

        //Return list of account objects
        return resultSet;

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        MyDBHandler handler = MyDBHandler.getInstance(context);
        SQLiteDatabase db = handler.getReadableDatabase();

        //Query to get details of the account specifiec by the account number
        String query = "SELECT * FROM " + MyDBFields.TABLE_ACCOUNT + " WHERE " + MyDBFields.COLUMN_ACCOUNT_NO + " =  '" + accountNo + "'";

        Cursor cursor = db.rawQuery(query, null);

        Account account = null;

        //add the details to an account object
        if (cursor.moveToFirst()) {
            account = new Account(cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_ACCOUNT_NO)),
                    cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_BANK_NAME)),
                    cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_ACCOUNT_HOLDER_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(MyDBFields.COLUMN_BALANCE)));
        }
        //If account is not found throw an exception
        else {
            throw new InvalidAccountException("You have selected an invalid account number...!");
        }

        cursor.close();

        //Return the account object
        return account;
    }

    @Override
    public void addAccount(Account account) {

        MyDBHandler handler = MyDBHandler.getInstance(context);
        SQLiteDatabase db = handler.getWritableDatabase();

        //Save account details to the account table
        ContentValues values = new ContentValues();
        values.put(MyDBFields.COLUMN_ACCOUNT_NO, account.getAccountNo());
        values.put(MyDBFields.COLUMN_BANK_NAME, account.getBankName());
        values.put(MyDBFields.COLUMN_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        values.put(MyDBFields.COLUMN_BALANCE, account.getBalance());

        db.insert(MyDBFields.TABLE_ACCOUNT, null, values);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        MyDBHandler handler = MyDBHandler.getInstance(context);
        SQLiteDatabase db = handler.getWritableDatabase();

        //Query to delete a particular account from the account table
        String query = "SELECT * FROM " + MyDBFields.TABLE_ACCOUNT + " WHERE " + MyDBFields.COLUMN_ACCOUNT_NO + " =  '" + accountNo + "'";

        Cursor cursor = db.rawQuery(query, null);

        Account account = null;

        //Delete the account if found in the table
        if (cursor.moveToFirst()) {
            account = new Account(cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_ACCOUNT_NO)),
                    cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_BANK_NAME)),
                    cursor.getString(cursor.getColumnIndex(MyDBFields.COLUMN_ACCOUNT_HOLDER_NAME)),
                    cursor.getFloat(cursor.getColumnIndex(MyDBFields.COLUMN_BALANCE)));
            db.delete(MyDBFields.TABLE_ACCOUNT, MyDBFields.COLUMN_ACCOUNT_NO + " = ?", new String[] { accountNo });
            cursor.close();

        }
        //If account is not found throw an exception
        else {
            throw new InvalidAccountException("No such account found...!");
        }

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        MyDBHandler handler = MyDBHandler.getInstance(context);
        SQLiteDatabase db = handler.getWritableDatabase();

        ContentValues values = new ContentValues();

        //Retrieve the account details of the selected account
        Account account = getAccount(accountNo);

        //Update the balance if the account is found in the table
        if (account!=null) {

            double new_amount=0;

            //Deduct the amount is it is an expense
            if (expenseType.equals(ExpenseType.EXPENSE)) {
                new_amount = account.getBalance() - amount;
            }
            //Add the amount if it is an income
            else if (expenseType.equals(ExpenseType.INCOME)) {
                new_amount = account.getBalance() + amount;
            }

            //Query to update balance in the account table
            String strSQL = "UPDATE "+MyDBFields.TABLE_ACCOUNT+" SET "+MyDBFields.COLUMN_BALANCE+" = "+new_amount+" WHERE "+MyDBFields.COLUMN_ACCOUNT_NO+" = '"+ accountNo+"'";

            db.execSQL(strSQL);

        }
        //If account is not found throw an exception
        else {
            throw new InvalidAccountException("No such account found...!");
        }

    }
}
