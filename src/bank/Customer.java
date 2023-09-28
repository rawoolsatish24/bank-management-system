/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bank;

import java.sql.ResultSet;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Lenovo
 */
public class Customer {
    
    float account_balance;
    String customer_name;
    String account_no;
    enum TYPE { Checking, Savings }
    TYPE account_type;
    Customer objTempCustomer;
    String tmpStr;
    Scanner scGetInput = new Scanner(System.in);
    MySQLDB objMySQLDB;
    ResultSet mysqlResult;
    float tmpFloat;
    int tmpInt;
    
    public Customer() {
        this.customer_name = "";
        this.account_no = "";
        this.account_type = TYPE.Savings;
        this.account_balance = 0;
    }
    
    public void initializeDatabase() { 
        objMySQLDB = new MySQLDB(); 
        objMySQLDB.ExecuteNonQuery("create table if not exists tblCustomerMaster (account_no varchar(50) not null primary key, customer_name varchar(100), account_type ENUM('Checking', 'Savings'), account_balance decimal(10, 2));");
        objMySQLDB.ExecuteNonQuery("create table if not exists tblTransactionMaster ( trans_no int auto_increment primary key, account_no varchar(50) references tblcustomermaster(account_no), trans_type ENUM('Deposit', 'Withdraw'), amount decimal(10, 2));");
        objMySQLDB.ExecuteNonQuery("create table if not exists tblBankServicesMaster ( service_no int auto_increment primary key, account_no varchar(50) references tblcustomermaster(account_no), cur_balance decimal(10, 2), percentage decimal(3, 2));");
    }
    
    public boolean checkIfAccountNoExist(String searchAccountNo) {
        return !objMySQLDB.ExecuteScalar("select Count(*) FROM tblcustomermaster where account_no = '" + searchAccountNo.trim() + "';").equals("0");
    }
    
    public Customer validateCustomer(String accNumber, String custName, String accType, float accBalance) {
        Customer objTempCustomer = new Customer();
        if(accNumber.trim().length() > 10) {
            JOptionPane.showMessageDialog(null, "Account number cannot be greater than 10 characters !!");
            return null;
        }
        if(custName.equals("")) {
            JOptionPane.showMessageDialog(null, "Customer name cannot be blank !!");
            return null;
        }
        if(custName.trim().length() > 30) {
            JOptionPane.showMessageDialog(null, "Customer name cannot be greater than 30 characters !!");
            return null;
        }
        if(!accType.toUpperCase().equals("C") && !accType.toUpperCase().equals("S")) {
            JOptionPane.showMessageDialog(null, "Invalid account type. Can be c/s only !!");
            return null;
        }
        if(accBalance < 0) {
            JOptionPane.showMessageDialog(null, "Opening balance cannot be negative !!");
            return null;
        }
        objTempCustomer.account_no = accNumber.toUpperCase().trim();
        objTempCustomer.customer_name = custName.toUpperCase().trim();
        if(accType.toUpperCase().equals("C")) { objTempCustomer.account_type = TYPE.Checking; }
        else { objTempCustomer.account_type = TYPE.Savings; }
        objTempCustomer.account_balance = accBalance;
        return objTempCustomer;
    }
    
    public void CreateNewAccount() {
        System.out.println("\nNew Account Details: ");
        objTempCustomer = new Customer();
        System.out.print("Enter New Account No. - ");
        objTempCustomer.account_no = scGetInput.nextLine();
        if(objTempCustomer.account_no.equals("")) {
            JOptionPane.showMessageDialog(null, "Sorry, account number cannot be blank !!"); 
            System.out.println("\nNew Account Creation Failed !!");
        }
        else if(checkIfAccountNoExist(objTempCustomer.account_no)) { 
            JOptionPane.showMessageDialog(null, "Sorry, entered account no. already exists !!"); 
            System.out.println("\nNew Account Creation Failed !!");
        }
        else {
            System.out.print("Enter Customer Name - ");
            objTempCustomer.customer_name = scGetInput.nextLine();
            System.out.print("Enter Account Type i.e. C(Checking)/S(Savings) - ");
            tmpStr = scGetInput.nextLine();
            System.out.print("Enter Opening Balance - ");
            objTempCustomer.account_balance = scGetInput.nextFloat();
            objTempCustomer = validateCustomer(objTempCustomer.account_no, objTempCustomer.customer_name, tmpStr, objTempCustomer.account_balance);
            if(objTempCustomer == null) { System.out.println("\nNew Account Creation Failed !!"); }
            else {
                objMySQLDB.ExecuteNonQuery("insert into tblcustomermaster values('" + objTempCustomer.account_no + "', '" + objTempCustomer.customer_name + "', '" + objTempCustomer.account_type + "', '" + objTempCustomer.account_balance + "');");
                JOptionPane.showMessageDialog(null, "Congratulation. New customer added successfully !!");
                System.out.println("\nNew Account Creation Succeed !!");
            }
        }
    }
    
    public void UpdateAccount() {
        System.out.println("\nUpdate Account Details: ");
        objTempCustomer = new Customer();
        objTempCustomer.account_no = JOptionPane.showInputDialog("Enter Account No. - ");
        if(objTempCustomer.account_no.equals("")) {
            JOptionPane.showMessageDialog(null, "Sorry, account number cannot be blank !!"); 
            System.out.println("\nAccount Updation Failed !!");
        }
        else if(!checkIfAccountNoExist(objTempCustomer.account_no)) { 
            JOptionPane.showMessageDialog(null, "Sorry, entered account no. don't exists !!"); 
            System.out.println("\nAccount Updation Failed !!");
        }
        else {
            System.out.print("Enter Customer Name - ");
            objTempCustomer.customer_name = scGetInput.nextLine();
            System.out.print("Enter Account Type i.e. C(Checking)/S(Savings) - ");
            tmpStr = scGetInput.nextLine();
            System.out.print("Enter Opening Balance - ");
            objTempCustomer.account_balance = scGetInput.nextFloat();
            objTempCustomer = validateCustomer(objTempCustomer.account_no, objTempCustomer.customer_name, tmpStr, objTempCustomer.account_balance);
            if(objTempCustomer == null) { System.out.println("\nAccount Updation Failed !!"); }
            else {
                objMySQLDB.ExecuteNonQuery("update tblcustomermaster set customer_name = '" + objTempCustomer.customer_name + "', account_type = '" + objTempCustomer.account_type + "', account_balance = '" + objTempCustomer.account_balance + "' where account_no = '" + objTempCustomer.account_no + "';");
                JOptionPane.showMessageDialog(null, "Congratulation. Customer updated successfully !!");
                System.out.println("\nAccount Updation Succeed !!");
            }
        }
    }
    
    public void DeleteAccount() {
        String strAccountNo;
        System.out.println("\nDelete Account Details: ");
        strAccountNo = JOptionPane.showInputDialog("Enter Account No. - ").trim();
        if(strAccountNo.equals("")) {
            JOptionPane.showMessageDialog(null, "Sorry, account number cannot be blank !!"); 
            System.out.println("\nAccount Deletion Failed !!");
        }
        else if(!checkIfAccountNoExist(strAccountNo)) { 
            JOptionPane.showMessageDialog(null, "Sorry, entered account no. don't exists !!"); 
            System.out.println("\nAccount Deletion Failed !!");
        }
        else {
            objMySQLDB.ExecuteNonQuery("delete from tblbankservicesmaster where account_no = '" + strAccountNo + "';");
            objMySQLDB.ExecuteNonQuery("delete from tbltransactionmaster where account_no = '" + strAccountNo + "';");
            objMySQLDB.ExecuteNonQuery("delete from tblcustomermaster where account_no = '" + strAccountNo + "';");
            JOptionPane.showMessageDialog(null, "Congratulation. Customer deleted successfully !!");
            System.out.println("\nAccount Deletion Succeed !!");
        }
    }
    
    public void DisplayCustomers(String displayMode) {
        String strSearchValue = "";
        String strSearchType = "";
        
        switch (displayMode) {
            case "" -> System.out.println("\nDisplay Customer list: ");
            case "account_no" -> strSearchType = "AccountNo";
            case "customer_name" -> strSearchType = "Name";
        }
        
        if(!displayMode.equals("")) {
            System.out.println("\nSearch Customer by " + strSearchType + ": ");
            strSearchValue = JOptionPane.showInputDialog("Enter Search " + strSearchType + " - ").toUpperCase().trim();
            if(strSearchValue.equals("")) {
                JOptionPane.showMessageDialog(null, "Sorry, " + strSearchType + " cannot be blank !!"); 
                System.out.println("\nCustomer Search Failed !!");
                return;
            }
            strSearchValue = " where " + displayMode + " like '%" + strSearchValue + "%'";
        }
        
        try {
            System.out.println("\n|-------------------------------------------------------------------------|");
            System.out.format("|%10s|%30s|%15s|%15s|", "Acc. No.", "Customer Name", "Acc. Type", "Balance");
            System.out.println("\n|-------------------------------------------------------------------------|");
            if(objMySQLDB.ExecuteScalar("select Count(*) FROM tblcustomermaster" + strSearchValue + ";").equals("0")) { System.out.format("|%73s|\n", "No Records Found!!"); }
            else {
                mysqlResult = objMySQLDB.ExecuteDataAdapter("select *from tblcustomermaster" + strSearchValue + " order by customer_name;");
                while(mysqlResult.next()) {
                    System.out.format("|%10s|%30s|%15s|%15s|\n", mysqlResult.getString("account_no"), mysqlResult.getString("customer_name"), mysqlResult.getString("account_type"), mysqlResult.getString("account_balance"));
                }
            }
            System.out.println("|-------------------------------------------------------------------------|");
        } catch(Exception ex){ System.out.println("Some issues while fetching data !!"); }	
    }
    
    public void DisplayTransactions() {
        String strAccountNo;
        System.out.println("\nDisplay Transactions list: ");
        strAccountNo = JOptionPane.showInputDialog("Enter Account No. - ").toUpperCase().trim();
        if(strAccountNo.equals("")) {
            JOptionPane.showMessageDialog(null, "Sorry, account number cannot be blank !!"); 
            System.out.println("\nDisplay Transactions Failed !!");
        }
        else if(!checkIfAccountNoExist(strAccountNo)) { 
            JOptionPane.showMessageDialog(null, "Sorry, entered account no. don't exists !!"); 
            System.out.println("\nDisplay Transactions Failed !!");
        }
        else {
            try {
                System.out.println("\n|------------------------------------------------------------------------------------|");
                System.out.format("|%10s|%10s|%30s|%15s|%15s|", "Trans. No.", "Acc. No.", "Customer Name", "Trans. Type", "Amount");
                System.out.println("\n|------------------------------------------------------------------------------------|");
                if(objMySQLDB.ExecuteScalar("select Count(*) from tbltransactionmaster where account_no = '" + strAccountNo + "';").equals("0")) { System.out.format("|%84s|\n", "No Records Found!!"); }
                else {
                    mysqlResult = objMySQLDB.ExecuteDataAdapter("select tab1.*, tab2.customer_name from tbltransactionmaster as tab1 inner join tblcustomermaster as tab2 on tab1.account_no = tab2.account_no where tab1.account_no = '" + strAccountNo + "' order by trans_no desc;");
                    while(mysqlResult.next()) {
                        System.out.format("|%10s|%10s|%30s|%15s|%15s|\n", mysqlResult.getString("trans_no"), mysqlResult.getString("account_no"), mysqlResult.getString("customer_name"), mysqlResult.getString("trans_type"), mysqlResult.getString("amount"));
                    }
                }
                System.out.println("|------------------------------------------------------------------------------------|");
            } catch(Exception ex){ System.out.println("Some issues while fetching data !!"); }	
        }
    }
    
    public void DisplayBankServices() {
        System.out.println("\nDisplay Bank Services list: ");
        try {
            System.out.println("\n|------------------------------------------------------------------------------------|");
            System.out.format("|%10s|%10s|%30s|%15s|%15s|", "ServiceNo.", "Acc. No.", "Customer Name", "CurBalance", "Percentage");
            System.out.println("\n|------------------------------------------------------------------------------------|");
            if(objMySQLDB.ExecuteScalar("select Count(*) from tblbankservicesmaster;").equals("0")) { System.out.format("|%84s|\n", "No Records Found!!"); }
            else {
                mysqlResult = objMySQLDB.ExecuteDataAdapter("select tab1.*, tab2.customer_name from tblbankservicesmaster as tab1 inner join tblcustomermaster as tab2 on tab1.account_no = tab2.account_no order by service_no desc;");
                while(mysqlResult.next()) {
                    System.out.format("|%10s|%10s|%30s|%15s|%15s|\n", mysqlResult.getString("service_no"), mysqlResult.getString("account_no"), mysqlResult.getString("customer_name"), mysqlResult.getString("cur_balance"), mysqlResult.getString("percentage"));
                }
            }
            System.out.println("|------------------------------------------------------------------------------------|");
        } catch(Exception ex){ System.out.println("Some issues while fetching data !!"); }	
    }
    
    public void DepositMoney() {
        String strAccountNo;
        System.out.println("\nDeposit Money: ");
        strAccountNo = JOptionPane.showInputDialog("Enter Account No. - ").toUpperCase().trim();
        if(strAccountNo.equals("")) {
            JOptionPane.showMessageDialog(null, "Sorry, account number cannot be blank !!"); 
            System.out.println("\nDeposit Money Failed !!");
        }
        else if(!checkIfAccountNoExist(strAccountNo)) { 
            JOptionPane.showMessageDialog(null, "Sorry, entered account no. don't exists !!"); 
            System.out.println("\nDeposit Money Failed !!");
        }
        else {
            System.out.print("Enter Amount - ");
            tmpFloat = scGetInput.nextFloat();
            if(tmpFloat <= 0) {
                JOptionPane.showMessageDialog(null, "Sorry, deposit amount must be greater than '0' always !!");
                System.out.println("\nDeposit Money Failed !!");
            }
            else {
                objMySQLDB.ExecuteNonQuery("update tblcustomermaster set account_balance = account_balance + '" + tmpFloat + "' where account_no = '" + strAccountNo + "';");
                objMySQLDB.ExecuteNonQuery("insert into tbltransactionmaster (account_no, trans_type, amount) values('" + strAccountNo + "', 'Deposit', '" + tmpFloat + "');");
                JOptionPane.showMessageDialog(null, "Congratulation. Amount deposited successfully !! New Balance: " + objMySQLDB.ExecuteScalar("select account_balance FROM tblcustomermaster where account_no = '" + strAccountNo + "' limit 1;"));
                System.out.println("\nDeposit Money Succeed !!");
            }
        }        
    }
    
    public void AddAnnualInterest() {
        String strAccountNo;
        System.out.println("\nAdd Annual Interest Percentage(%): ");
        strAccountNo = JOptionPane.showInputDialog("Enter Account No. - ").toUpperCase().trim();
        if(strAccountNo.equals("")) {
            JOptionPane.showMessageDialog(null, "Sorry, account number cannot be blank !!"); 
            System.out.println("\nAdd Annual Interest Failed !!");
        }
        else if(!checkIfAccountNoExist(strAccountNo)) { 
            JOptionPane.showMessageDialog(null, "Sorry, entered account no. don't exists !!"); 
            System.out.println("\nAdd Annual Interest Failed !!");
        }
        else {
            System.out.print("Enter Interest Percentage(%) - ");
            tmpFloat = scGetInput.nextFloat();
            if(tmpFloat <= 0 || tmpFloat > 100) {
                JOptionPane.showMessageDialog(null, "Invalid percentage value, must be between 0-100% always !!");
                System.out.println("\nAdd Annual Interest Failed !!");
            }
            else {
                tmpStr = objMySQLDB.ExecuteScalar("select account_balance FROM tblcustomermaster where account_no = '" + strAccountNo + "' limit 1;");
                objMySQLDB.ExecuteNonQuery("update tblcustomermaster set account_balance = account_balance * '" + (1 + (tmpFloat / 100)) + "' where account_no = '" + strAccountNo + "';");
                objMySQLDB.ExecuteNonQuery("insert into tblbankservicesmaster (account_no, cur_balance, percentage) values('" + strAccountNo + "', '" + tmpStr + "', '" + tmpFloat + "');");
                tmpStr = objMySQLDB.ExecuteScalar("select account_balance FROM tblcustomermaster where account_no = '" + strAccountNo + "' limit 1;");
                JOptionPane.showMessageDialog(null, "Congratulation. Annual interest added successfully !! New Balance: " + tmpStr);
                System.out.println("\nAdd Annual Interest Succeed !!");
            }
        }
    }
    
    public void WithdrawMoney() {
        String strAccountNo;
        System.out.println("\nWithdraw Money: ");
        String curBalance;
        strAccountNo = JOptionPane.showInputDialog("Enter Account No. - ").toUpperCase().trim();
        if(strAccountNo.equals("")) {
            JOptionPane.showMessageDialog(null, "Sorry, account number cannot be blank !!"); 
            System.out.println("\nWithdrawl Money Failed !!");
        }
        else if(!checkIfAccountNoExist(strAccountNo)) { 
            JOptionPane.showMessageDialog(null, "Sorry, entered account no. don't exists !!"); 
            System.out.println("\nWithdrawl Money Failed !!");
        }
        else if(objMySQLDB.ExecuteScalar("select Count(*) FROM tblcustomermaster where account_no = '" + strAccountNo + "' and account_type = 'Savings';").equals("1")) { 
            JOptionPane.showMessageDialog(null, "Money cannot be withdrawn from Savings Account !!"); 
            System.out.println("\nWithdrawl Money Failed !!");
        }
        else {
            System.out.print("Enter Amount - ");
            tmpFloat = scGetInput.nextFloat();
            curBalance = objMySQLDB.ExecuteScalar("select account_balance FROM tblcustomermaster where account_no = '" + strAccountNo + "' limit 1;");
            if(tmpFloat <= 0) {
                JOptionPane.showMessageDialog(null, "Sorry, withdrawl amount must be greater than '0' always !!");
                System.out.println("\nWithdrawl Money Failed !!");
            }
            else if(Float.parseFloat(curBalance) < tmpFloat) {
                JOptionPane.showMessageDialog(null, "Amount cannot be withdrawn as your current balance is $" + curBalance);
                System.out.println("\nWithdrawl Money Failed !!");
            }
            else {
                objMySQLDB.ExecuteNonQuery("update tblcustomermaster set account_balance = account_balance - '" + tmpFloat + "' where account_no = '" + strAccountNo + "';");
                objMySQLDB.ExecuteNonQuery("insert into tbltransactionmaster (account_no, trans_type, amount) values('" + strAccountNo + "', 'Withdraw', '" + tmpFloat + "');");
                curBalance = objMySQLDB.ExecuteScalar("select account_balance FROM tblcustomermaster where account_no = '" + strAccountNo + "' limit 1;");
                JOptionPane.showMessageDialog(null, "Congratulation. Amount deposited successfully !! New Balance: " + curBalance);
                System.out.println("\nWithdrawl Money Succeed !!");
            }
        }    
    }
    
    public void ClearBankData() {
        tmpInt = JOptionPane.showConfirmDialog(null, "Are you sure to clear the bank recordset? This can't be done reverse.");
        if(tmpInt != JOptionPane.YES_OPTION) { System.out.println("\nThe Bank Recordset Clearing Terminated !!"); }
        else {
            objMySQLDB.ExecuteNonQuery("delete from tblbankservicesmaster;");
            objMySQLDB.ExecuteNonQuery("delete from tbltransactionmaster;");
            objMySQLDB.ExecuteNonQuery("delete from tblcustomermaster;");
            objMySQLDB.ExecuteNonQuery("alter table tblbankservicesmaster auto_increment = 1;");
            objMySQLDB.ExecuteNonQuery("alter table tbltransactionmaster auto_increment = 1;");
            System.out.println("\nThe Bank Recordset Cleared Successfully !!");
        }
    }
    
    public void GetBankHoldingAmount() {
        tmpStr = objMySQLDB.ExecuteScalar("Select Sum(account_balance) from tblcustomermaster;");
        if(tmpStr.equals("")) { tmpStr = "0"; }
        System.out.println("\nThe Bank is currently holding total amount: " + tmpStr);
    }
        
}
