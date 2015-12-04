package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

/**
 * Created by User on 12/4/2015.
 */
public class MyDBFields {

    //Fields of account table
    public static final String TABLE_ACCOUNT = "account";
    public static final String COLUMN_ACCOUNT_NO = "accountNo";
    public static final String COLUMN_BANK_NAME = "bankName";
    public static final String COLUMN_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String COLUMN_BALANCE = "balance";

    //Fields of transaction_log table
    public static final String TABLE_TRANSACTION_LOG = "transaction_log";
    public static final String COLUMN_TRANSACTION_ID = "transaction_id";
    public static final String COLUMN_TRANSACTION_AMOUNT = "amount";
    public static final String COLUMN_TRANSACTION_DATE = "date";
    public static final String COLUMN_EXPENSE_TYPE = "expense_type";
}
