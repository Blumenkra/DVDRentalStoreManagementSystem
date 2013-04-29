package kr.ac.kaist.iu1nguoi.java;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Scanner;

public class DVDRentalStoreManagementSystem {
    static Scanner stdin = new Scanner(System.in);

    static Connection con = null;

    static CallableStatement cs = null;

    public static void main(String[] args) {
        int func;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                    "server", "username", "password");

            do {
                System.out.println();
                System.out.println("DVD rental store management system");
                System.out.println("----------------------------------");
                System.out.println("[0] Quit");
                System.out.println("[1] Add User  [2] Add DVD  [3] Judge Delinquent");
                System.out.println("[4] Estimate Late Fee  [5] Rent DVD  [6] Return DVD");
                System.out.println("---------------------------------------------------");
                System.out.print("Select the function > ");
                func = stdin.nextInt();
                stdin.nextLine();
                switch (func) {
                case 0:
                    break;
                case 1:
                	function1_add_user();
                    break;
                case 2:
                	function2_add_dvd();
                    break;
                case 3:
                	function3_check_user();
                    break;
                case 4:
                    function4_estimate_late_fee();
                    break;
                case 5:
                    function5_rent();
                    break;
                case 6:
                    function6_return();
                    break;
                default:
                    System.out.println("Wrong input. Try again!");
                }
            } while (func != 0);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
            }
        }

    }

    private static void function6_return() {
        // TODO Auto-generated method stub
        int mid, did, result;
        String rdate, isreturned;
        Statement stmt = null;
        ResultSet rs = null;

        System.out.print("DVD ID : ");
        did = stdin.nextInt();
        stdin.nextLine();

        try {
            cs = con.prepareCall("{? = call returning(?)}");
            cs.registerOutParameter(1, Types.NUMERIC);
            cs.setInt(2, did);
            cs.execute();
            result = cs.getInt(1);
            System.out.println("Late fee is " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println("RENTLIST table");
        System.out.printf("%-5s  %-5s   %-20s   %-6s%n", "MID", "DID", "RDATE", "RETURN");
        System.out.println("------------------------------------------------------------");

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT mid,did,rdate,isreturned FROM rentlist ORDER BY rdate");

            while (rs.next()) {
                mid = rs.getInt(1);
                did = rs.getInt(2);
                rdate = rs.getString(3);
                isreturned = rs.getString(4);
                System.out.printf("%-5s  %-5s   %-20s   %-6s%n", mid, did, rdate, isreturned);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }

    private static void function5_rent() {
        // TODO Auto-generated method stub
        int mid, did;
        String rdate, isreturned;
        Statement stmt = null;
        ResultSet rs = null;

        System.out.print("User ID : ");
        mid = stdin.nextInt();
        stdin.nextLine();
        System.out.print("DVD ID : ");
        did = stdin.nextInt();
        stdin.nextLine();

        try {
            cs = con.prepareCall("{call renting(?,?)}");
            cs.setInt(1, mid);
            cs.setInt(2, did);
            cs.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println("RENTLIST table");
        System.out.printf("%-5s  %-5s   %-20s   %-6s%n", "MID", "DID", "RDATE", "RETURN");
        System.out.println("------------------------------------------------------------");

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT mid,did,rdate,isreturned FROM rentlist ORDER BY rdate");

            while (rs.next()) {
                mid = rs.getInt(1);
                did = rs.getInt(2);
                rdate = rs.getString(3);
                isreturned = rs.getString(4);
                System.out.printf("%-5s  %-5s   %-20s   %-6s%n", mid, did, rdate, isreturned);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }

    private static void function4_estimate_late_fee() {
        // TODO Auto-generated method stub
        int id, result;

        System.out.print("DVD ID : ");
        id = stdin.nextInt();
        stdin.nextLine();

        try {
            cs = con.prepareCall("{? = call estimate_late_fee(?)}");
            cs.registerOutParameter(1, Types.NUMERIC);
            cs.setInt(2, id);
            cs.execute();
            result = cs.getInt(1);
            System.out.println("The late fee is " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void function3_check_user() {
        // TODO Auto-generated method stub
        int id, result;

        System.out.print("User ID : ");
        id = stdin.nextInt();
        stdin.nextLine();

        try {
            cs = con.prepareCall("{? = call is_user_delinquent(?)}");
            cs.registerOutParameter(1, Types.NUMERIC);
            cs.setInt(2, id);
            cs.execute();
            result = cs.getInt(1);
            if (result == 1) {
                System.out.println("This user is a delinquent.");
            } else {
                System.out.println("This user is not a delinquent.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void function2_add_dvd() {
        // TODO Auto-generated method stub
        int mid;
        String mname;
        Statement stmt = null;
        ResultSet rs = null;

        System.out.print("DVD ID : ");
        mid = stdin.nextInt();
        stdin.nextLine();
        System.out.print("DVD Title : ");
        mname = stdin.nextLine();

        try {
            cs = con.prepareCall("{call add_dvd_information(?,?)}");
            cs.setInt(1, mid);
            cs.setString(2, mname);
            cs.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println("DVDS table");
        System.out.printf("%-5s  %-20s%n", "DID", "DTITLE");
        System.out.println("------------------------------------------------------------");

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT did,dtitle FROM dvds ORDER BY did");

            while (rs.next()) {
                mid = rs.getInt(1);
                mname = rs.getString(2);
                System.out.printf("%-5d  %-20s%n", mid, mname);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }

    private static void function1_add_user() {
        // TODO Auto-generated method stub
        int mid, mrentcount;
        String mname, mtel;
        Statement stmt = null;
        ResultSet rs = null;

        System.out.print("Member ID : ");
        mid = stdin.nextInt();
        stdin.nextLine();
        System.out.print("Name : ");
        mname = stdin.nextLine();
        System.out.print("Tel. : ");
        mtel = stdin.nextLine();
        try {
            cs = con.prepareCall("{call add_user_information(?,?,?)}");
            cs.setInt(1, mid);
            cs.setString(2, mname);
            cs.setString(3, mtel);
            cs.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println("MEMBERS table");
        System.out.printf("%-5s  %-10s   %-15s   %-6s%n", "MID", "MNAME", "MTEL", "MRENTCOUNT");
        System.out.println("------------------------------------------------------------");

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT mid,mname,mtel,mrentcount FROM members ORDER BY mid");

            while (rs.next()) {
                mid = rs.getInt(1);
                mname = rs.getString(2);
                mtel = rs.getString(3);
                mrentcount = rs.getInt(4);
                System.out.printf("%-5d  %-10s   %-15s   %-6d%n", mid, mname, mtel, mrentcount);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }
}
