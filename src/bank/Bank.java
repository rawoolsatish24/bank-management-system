/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bank;

import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Lenovo
 */
public class Bank {    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int intChoice;
        Customer objCustomer = new Customer();
        objCustomer.initializeDatabase();
        Scanner scGetInput = new Scanner(System.in);
        itrBanking : while(true) {
            System.out.println("\n|-------------------------------------------------------------------------|");
            System.out.format("|%73s|", "Bank Management System");
            System.out.println("\n|-------------------------------------------------------------------------|");
            System.out.println("\n1. Add Customer");
            System.out.println("2. Update Customer");
            System.out.println("3. Delete Customer");
            System.out.println("4. Deposit Money");
            System.out.println("5. Withdraw Money");
            System.out.println("6. Display Customer List");
            System.out.println("7. Display Transactions List");
            System.out.println("8. Display Bank Services List");
            System.out.println("9. Search Customer by AccountNo");
            System.out.println("10. Search Customer by Name");
            System.out.println("11. Add Annual Interest Amount");
            System.out.println("12. Total Available Amount In Bank");
            System.out.println("13. Clear The Bank Recordset");
            System.out.println("14. Exit");
            System.out.print("\nEnter Your Choice: ");
            intChoice = scGetInput.nextInt();
            switch(intChoice) {
                case 1 -> objCustomer.CreateNewAccount();
                case 2 -> objCustomer.UpdateAccount();
                case 3 -> objCustomer.DeleteAccount();
                case 4 -> objCustomer.DepositMoney();
                case 5 -> objCustomer.WithdrawMoney();
                case 6 -> objCustomer.DisplayCustomers("");
                case 7 -> objCustomer.DisplayTransactions();
                case 8 -> objCustomer.DisplayBankServices();
                case 9 -> objCustomer.DisplayCustomers("account_no");
                case 10 -> objCustomer.DisplayCustomers("customer_name");
                case 11 -> objCustomer.AddAnnualInterest();
                case 12 -> objCustomer.GetBankHoldingAmount();
                case 13 -> objCustomer.ClearBankData();
                case 14 -> {
                    JOptionPane.showMessageDialog(null, "Thank you! Please visit us soon.");
                    break itrBanking;
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid choice! Please make a valid choice.");
            }
        }
        scGetInput.close();
    }
}