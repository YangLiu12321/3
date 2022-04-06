import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.File;

import java.util.Scanner;
import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class JavaSQL {
  public static void main(String[] args) {
    Connection con = null;
    String dbAdress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db9";
    String dbUsername = "Group9";
    String dbPassword = "CSCI3170";
    try {
      Class.forName("com.mysql.jdbc.Driver");
      con = DriverManager.getConnection(dbAdress, dbUsername, dbPassword);
      Statement dropDatabase = con.createStatement();
      Statement createDatabase = con.createStatement();
      Statement stmt = con.createStatement();
      stmt.executeUpdate("use db9;");
      main_menu(con); 
      con.close();
    } catch (ClassNotFoundException e) {
      // Handle any errors
      System.out.println("[Error]: Java MySQL DB Driver not found!!");
      System.exit(0);
    } catch (SQLException e) {
        System.out.println(e);
    }
  }

  // done
  public static void main_menu(Connection conn) {
    int input;
    System.out.println("\n-----Main Menu-----");
    System.out.println("What kinds of operations would you like to perform?");
    System.out.println("1. Operations for Administrator");
    System.out.println("2. Operations for User");
    System.out.println("3. Operations for Manager");
    System.out.println("4. Exit this program");
    Scanner scan = new Scanner(System.in);
    do {
      System.out.print("Enter Your Choice: ");
      input = scan.nextInt();
    } while (input < 1 || input > 4);
    if (input == 1)
      admin_operation(conn);
    else if (input == 2)
      user_operation(conn);
    else if (input == 3)
      manager_operation(conn);
    else if (input == 4) {
      System.out.println("Thanks,bye!");
      System.exit(1);
    }
  }

  // done
  public static void admin_operation(Connection conn) {
    int input;
    System.out.println("\n-----Operations for administrator menu-----");
    System.out.println("What kinds of operations would you like to perform?");
    System.out.println("1. Create all tables");
    System.out.println("2. Delete all tables");
    System.out.println("3. Load from datafile");
    System.out.println("4. Show number of records in each table");
    System.out.println("5. Return to the main menu");
    Scanner scan = new Scanner(System.in);
    do {
      System.out.print("Enter Your Choice: ");
      input = scan.nextInt();
    } while (input < 1 || input > 5);
    if (input == 1)
      createAllTables(conn);
    else if (input == 2)
      dropAllTable(conn);
    else if (input == 3)
      LoadData(conn);
    else if (input == 4)
      showAllTable(conn);
    else if (input == 5)
      main_menu(conn);
    admin_operation(conn);
  }

  /* Create All tables */
  // done
  public static void createAllTables(Connection conn) {
    System.out.println("Processing...\n");
    try {
      String sqlStatement_create_usercategory;
      PreparedStatement pstmt_create_usercategory;

      /* create table: user_category */
      sqlStatement_create_usercategory = "CREATE TABLE user_category(" + "ucid integer(1) primary key,"
          + "max_cars integer(1) not null," + "loan_period integer(2) not null)";

      pstmt_create_usercategory = conn.prepareStatement(sqlStatement_create_usercategory);
      /* execute SQL */

      /* create table: user */
      String sqlStatement_create_user;
      PreparedStatement pstmt_create_user;

      sqlStatement_create_user = "CREATE TABLE user(" + "uid varchar(12) primary key,"
          + "name varchar(25) not null," + "age integer(2) not null, " + " occupation varchar(20) not null," + "user_category_id integer not null)";

      pstmt_create_user = conn.prepareStatement(sqlStatement_create_user);
      /* execute SQL */

      /* create table: car_category */
      String sqlStatement_create_carcategory;
      PreparedStatement pstmt_create_carcategory;

      sqlStatement_create_carcategory = "CREATE TABLE car_category(" + "ccid integer(1) primary key,"
          + "car_category_name varchar(20) not null)";

      pstmt_create_carcategory = conn.prepareStatement(sqlStatement_create_carcategory);
      /* execute SQL */
      
      /* create table: car */
      String sqlStatement_create_car;
      PreparedStatement pstmt_create_car;

      sqlStatement_create_car = "CREATE TABLE car(" + "call_number varchar(8) primary key,"
          + "car_name varchar(10) not null," + "manufacture_date varchar(10) not null," + "time_rent varchar(2) not null, "
          + "car_category_id integer(1) not null)";

      pstmt_create_car = conn.prepareStatement(sqlStatement_create_car);
      /* execute SQL */

      /* create table: produce */
      String sqlStatement_create_produce;
      PreparedStatement pstmt_create_produce;

      sqlStatement_create_produce = "CREATE TABLE produce(" + "name varchar(25) not null,"
          + "call_number varchar(8) not null," + "PRIMARY KEY(name, call_number))";

      pstmt_create_produce = conn.prepareStatement(sqlStatement_create_produce);
      /* execute SQL */

      /* create table: copy */
      String sqlStatement_create_copy;
      PreparedStatement pstmt_create_copy;

      sqlStatement_create_copy = "CREATE TABLE copy(" + "call_number varchar(8) not null,"
          + "copy_number integer not null," + "PRIMARY KEY(call_number, copy_number))";

      pstmt_create_copy = conn.prepareStatement(sqlStatement_create_copy);
      /* execute SQL */

      /* create table: rent */
      String sqlStatement_create_rent;
      PreparedStatement pstmt_create_rent;

      sqlStatement_create_rent = "CREATE TABLE rent(" + "uid varchar(10) not null,"
          + "call_number varchar(8) not null," + "copy_number integer not null," + "checkout_date varchar(10) not null,"
          + "return_date varchar(10)," + "PRIMARY KEY(uid, call_number, copy_number, checkout_date))";

      pstmt_create_rent = conn.prepareStatement(sqlStatement_create_rent);
      /* execute SQL */

      pstmt_create_usercategory.executeUpdate();
      pstmt_create_user.executeUpdate();
      pstmt_create_carcategory.executeUpdate();
      pstmt_create_car.executeUpdate();
      pstmt_create_produce.executeUpdate();
      pstmt_create_copy.executeUpdate();
      pstmt_create_rent.executeUpdate();

      System.out.println("Done. Database is initialized");
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }

  }

  /* Load ALL test data for database */
  //    done.
  //    loadDataCarCategory(conn, path); 
  //    loadDataCar(conn, path); 
  //    loadRent(conn,path); 
  //        to be implemented.
  public static void LoadData(Connection conn) {
    Scanner scan = new Scanner(System.in);
    System.out.print("Type in the Source Data Folder Path: ");
    String path = scan.nextLine();
    try {
      System.out.println("Processing...");
      loadDataUserCategory(conn, path);
      loadDataUser(conn, path);
      loadDataCarCategory(conn, path);
      loadDataCar(conn, path);
      loadRent(conn,path);
      System.out.println("Done. Data is inputted to the database\n");
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
    return;
  }

  /* load testdata for table: User Category */
  //    done
  public static void loadDataUserCategory(Connection conn, String path) throws Exception {
    File file = new File(path + "/" + "user_category.txt");
    Scanner scan = new Scanner(file);

    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user_category (ucid, max_cars, loan_period) VALUES (?, ?, ?)");
    while (scan.hasNextLine()) {
      String data = scan.nextLine();
      String[] result = data.split("\t");
      for (int i = 0; i < result.length; i++)
      pstmt.setString(i + 1, result[i]);
      pstmt.execute();
    }
    System.out.println("Data of user category have been loaded successfully!\n");
    return;
  }

  /* load test data for table: User */
  //    done
  public static void loadDataUser(Connection conn, String path) throws Exception {
    File file = new File(path + "/" + "user.txt");
    Scanner scan = new Scanner(file);

    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user (uid, name, age, occupation, user_category_id) VALUES (?, ?, ?, ?, ?)");
    while (scan.hasNextLine()) {
      String data = scan.nextLine();
      String[] result = data.split("\t");
      for (int i = 0; i < result.length; i++)
      pstmt.setString(i + 1, result[i]);
      pstmt.execute();
    }
    System.out.println("Data of user have been loaded successfully!\n");
    return;
  }
  
  /* load testdata for table: Car Category */
  //    done
  public static void loadDataCarCategory(Connection conn, String path) throws Exception {
    File file = new File(path + "/" + "car_category.txt");
    Scanner scan = new Scanner(file);

    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO car_category (ccid, car_category_name) VALUES (?, ?)");
    while (scan.hasNextLine()) {
      String data = scan.nextLine();
      String[] result = data.split("\t");
      for (int i = 0; i < result.length; i++)
      pstmt.setString(i + 1, result[i]);
      pstmt.execute();
    }
    System.out.println("Data of car category have been loaded successfully!\n");
    return;
  }


  /* load test data for tables: Car, Copy and Produce*/
  // done. fixed.
  public static void loadDataCar(Connection conn, String path) throws Exception {
    String[] result;
    File file = new File(path + "/" + "car.txt");
    Scanner scan = new Scanner(file);
    PreparedStatement ps_car = conn.prepareStatement("INSERT INTO car (call_number, car_name, manufacture_date, time_rent, car_category_id) VALUES (?, ?, ?, ?, ?)");
    PreparedStatement ps_copy = conn.prepareStatement("INSERT INTO copy (call_number, copy_number) VALUES (?,?)");
    PreparedStatement ps_produce = conn.prepareStatement("INSERT INTO produce (name, call_number) VALUES (?,?)");

    while (scan.hasNextLine()) {
      String data = scan.nextLine();
      result = data.split("\t");
      String company = result[3];
      String[] nameList = company.split(",");
      try {
        for (int i = 0; i < nameList.length; i++) {
          ps_produce.setString(1, nameList[i]);
          ps_produce.setString(2, result[0]);
          ps_produce.execute();
        }
      } catch (Exception ex) {
      }

      // car.txt: {call_number, name, manufacture, time_rent, ccid}
      ps_copy.setString(1, result[0]);
      ps_copy.setString(2, result[1]);
      ps_car.setString(1, result[0]);
      ps_car.setString(2, result[2]);
      ps_car.setString(3, result[4]);
      ps_car.setString(4, result[5]);
      ps_car.setString(5, result[6]);
      ps_copy.execute();
      ps_car.execute();
    }
    System.out.println("Data of car has been loaded successfully!\n");
  }

  /* load test data for table: Rent */
  //    done
  public static void loadRent(Connection conn, String path) throws Exception {
    File file = new File(path + "/" + "rent.txt");
    Scanner scan = new Scanner(file);
    PreparedStatement pstmt = conn.prepareStatement(
        "INSERT INTO rent (uid, call_number, copy_number, checkout_date, return_date) VALUES (?, ?, ?, ?, ?)");
    while (scan.hasNextLine()) {
      // rent.txt: {call_number, copy_number, user_id ,checkout_date, return_date}
      String data = scan.nextLine();
      String[] result = data.split("\t");
      pstmt.setString(1, result[2]);
      pstmt.setString(2, result[0]);
      pstmt.setString(3, result[1]);
      pstmt.setString(4, result[3]);
      if (result[4].equals("null"))
        pstmt.setString(5, "");
      else
        pstmt.setString(5, result[4]);

      pstmt.execute();
    }
    System.out.println("Data of rent has been loaded successfully!\n");
  }

  //    done
  public static void dropAllTable(Connection conn) {
    System.out.println("Processing...");
    String[] tables = { "user_category", "user", "car", "copy", "rent", "car_category","produce" };
    String sqlStatement = "DROP TABLE IF EXISTS  ";
    String setStatement = "SET FOREIGN_KEY_CHECKS=0;";
    try {
      for (int i = 0; i < tables.length; i++) {
        PreparedStatement setS = conn.prepareStatement(setStatement);
        setS.executeUpdate();
        String temp = sqlStatement + tables[i];
        PreparedStatement pstmt = conn.prepareStatement(temp);
        pstmt.executeUpdate();
      }
      System.out.println("Done. Database is removed!");
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
  }

  //    done
  public static void showAllTable(Connection conn) {
    System.out.println("\nNumber of records in each table: ");
    String[] tables = { "user_category", "user", "car_category", "car", "copy", "produce", "rent" };
    String sqlStatement = "SELECT COUNT(*) FROM ";
    try {
      for (int i = 0; i < tables.length; i++) {
        String temp = sqlStatement + tables[i];
        PreparedStatement pstmt = conn.prepareStatement(temp);
        ResultSet rs = pstmt.executeQuery();
        // Move cursor to data
        rs.next();
        int count = rs.getInt("count(*)");
        if (tables[i] == "produce"){
            System.out.println("\ncompany: " + count);
        }else{
            System.out.println("\n" + tables[i] + ": " + count);
        }
      }
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
  }

  //    done
  public static void user_operation(Connection conn) {
    int input;
    System.out.println("\n-----Operations for user menu-----");
    System.out.println("What kinds of operations would you like to perform?");
    System.out.println("1. Search for cars");
    System.out.println("2. Show loan records of a user");
    System.out.println("3. Return to the main menu");
    Scanner scan = new Scanner(System.in);
    do {
      System.out.print("Enter Your Choice: ");
      input = scan.nextInt();
    } while (input < 1 || input > 3);
    if (input == 1)
      carSearch(conn);
    else if (input == 2)
      showUserRecord(conn);
    else if (input == 3)
      main_menu(conn);
    user_operation(conn);
  }

  // done
  public static void carSearch(Connection conn) {
    int input;
    String searchKey, callNumber;
    System.out.println("Choose the search criteria:");
    System.out.println("1. Call Number");
    System.out.println("2. name");
    System.out.println("3. company");
    Scanner scan = new Scanner(System.in);
    int columns = 10000;
    try{
      String sqlStatement = "SELECT COUNT(*) FROM car";
      PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
      ResultSet rs = pstmt.executeQuery();
      // Move cursor to data
      rs.next();
      columns = rs.getInt("count(*)");
    }
    catch (Exception ex){

    }
    // Get searching criteria
    do {
      System.out.print("Choose the search criteria: ");
      input = scan.nextInt();
    } while (input < 1 || input > 3);
    Boolean hasResult = false;
    try {
      System.out.print("Type in the search keyword: ");
      Scanner keyword = new Scanner(System.in);
      String sqlStatement;
      PreparedStatement pstmt;
      // Build SQL statement
      if (input == 1) {
        callNumber = keyword.nextLine();
        sqlStatement = "SELECT * FROM " + "car, copy, produce, car_category WHERE " + "car.call_number = copy.call_number AND "
            + "car.call_number = produce.call_number AND " + "produce.call_number = copy.call_number AND " + "car_category.ccid = car.car_category_id AND "
            + "car.call_number = ?";
        pstmt = conn.prepareStatement(sqlStatement);
        pstmt.setString(1, callNumber);
      } else if (input == 2) {
        searchKey = keyword.nextLine();
        sqlStatement = "SELECT * FROM " + "car, copy, produce, car_category WHERE " + "car.call_number = copy.call_number AND "
            + "car.call_number = produce.call_number AND " + "produce.call_number = copy.call_number AND " + "car_category.ccid = car.car_category_id AND "
            + "car.car_name LIKE BINARY ?";
        pstmt = conn.prepareStatement(sqlStatement);
        searchKey = "%" + searchKey + "%";
        pstmt.setString(1, searchKey);
      } else {
        searchKey = keyword.nextLine();
        sqlStatement = "SELECT * FROM " + "car, copy, produce, car_category WHERE " + "car.call_number = copy.call_number AND "
            + "car.call_number = produce.call_number AND " + "produce.call_number = copy.call_number AND " + "car_category.ccid = car.car_category_id AND "
            + "produce.name LIKE BINARY ?";
        pstmt = conn.prepareStatement(sqlStatement);
        searchKey = "%" + searchKey + "%";
        pstmt.setString(1, searchKey);
      }
      // Parse Output
      ResultSet rs = pstmt.executeQuery();
      System.out.println("| Call Num | Name | Car Category | Company | Available No. of Copy |");
      int copyResult = 0;
      String[][] resultSet = new String[columns][4];
      int[] copyNumberSet = new int[columns];
      int count = 0;
      while (rs.next()) {
        hasResult = true;
        Boolean found = false;
        String callTemp = rs.getString("call_number");
        for(int i=0; i<count; i++){
          if(resultSet[i][0].equals(callTemp)){
            found = true;
            resultSet[i][2] = resultSet[i][2] + ", " + rs.getString("name");
          }
        }
        if(found)
          continue;

        resultSet[count][0] = callTemp;
        resultSet[count][1] = rs.getString("car_name");
        resultSet[count][2] = rs.getString("car_category_name");
        resultSet[count][3] = rs.getString("name");
        copyNumberSet[count] = rs.getInt("copy_number");
        
        count += 1;

        String sqlStatement_unreturn = "SELECT count(return_date) FROM rent WHERE return_date = '' AND call_number = ? group by call_number";
        PreparedStatement unreturnPstmt = conn.prepareStatement(sqlStatement_unreturn);
        ResultSet unreturnSet = unreturnPstmt.executeQuery();
        if(unreturnSet.next()){
          int number = unreturnSet.getInt("count(return_date)");
          copyNumberSet[count] -= number;
        }
      }
      if (!hasResult)
        throw new Exception("no output");
      else{
        for(int i=0; i<count; i++)
          System.out.println("| " + resultSet[i][0] + " | " + resultSet[i][1] + " | " + resultSet[i][2] + " | " + resultSet[i][3] + " | " + copyNumberSet[i] + "  |");
      }
    } catch (Exception exp) {
      System.out.println("[Error]: An matching search record is not found. The input does not exist in database.");
    }
  }

  // done
  public static void showUserRecord(Connection conn) {
    String userID;
    try {
      System.out.print("Enter The User ID: ");
      Scanner scan = new Scanner(System.in);
      String sqlStatement;
      PreparedStatement pstmt;
      // Build SQL statement
      userID = scan.nextLine();
      sqlStatement = "SELECT * FROM " + "car, copy, produce, rent WHERE "
          + "car.call_number = copy.call_number AND " + "car.call_number = produce.call_number AND "
          + "rent.call_number = car.call_number AND "
          + "rent.user_id = ?";
      pstmt = conn.prepareStatement(sqlStatement);
      pstmt.setString(1, userID);

      // Parse Output
      ResultSet rs = pstmt.executeQuery();
      System.out.println("Loan Record:");
      System.out.println("|Call Num|CopyNum|Name|Company|Check-out|Returned?|");
      Boolean hasResult = false;
      int copyResult = 0;
      String carNameResult = "", companyResult = "", callResult = "", checkoutResult = "", returnResult = "";
      while (rs.next()) {
        hasResult = true;
        String callTemp = rs.getString("call_number");
        if (callTemp.equals(callResult)) {
         companyResult = companyResult + ", " + rs.getString("name");
        } else {
          if (!callResult.equals(""))
            System.out.println("|" + callResult + "|" + copyResult + "|" + carNameResult + "|" + companyResult
                + "|" + checkoutResult + "|" + returnResult + "|");
          callResult = callTemp;
          copyResult = rs.getInt("copy_number");
          carNameResult = rs.getString("car_name");
          companyResult = rs.getString("name");
          checkoutResult = rs.getString("checkout_date");
          returnResult = rs.getString("return_date");
          if (returnResult.equals(""))
            returnResult = "No";
          else
            returnResult = "Yes";
        }
      }
      if (!hasResult)
        throw new Exception("no output");
      else
        System.out.println("|" + callResult + "|" + copyResult + "|" + carNameResult + "|" + companyResult
                + "|" + checkoutResult + "|" + returnResult + "|");
        System.out.println("End of Query");
    } catch (Exception exp) {
      System.out.println("[Error]: An matching search record is not found. The input does not exist in database.");
    }
  }

  //    done
  public static void manager_operation(Connection conn) {
    int input;
    System.out.println("\n-----Operations for manager menu-----");
    System.out.println("What kind of operation would you like to perform?");
    System.out.println("1. Car Renting");
    System.out.println("2. Car Returning");
    System.out.println("3. List all un-returned car copies which are checked-out within a period");
    System.out.println("4. Return to the main menu");
    Scanner scan = new Scanner(System.in);
    do {
      System.out.print("Enter Your Choice: ");
      input = scan.nextInt();
    } while (input < 1 || input > 4);
    if (input == 1)
      carRenting(conn);
    else if (input == 2)
      carReturning(conn);
    else if (input == 3)
      listUnreturnedCars(conn);
    else if (input == 4)
      main_menu(conn);
    manager_operation(conn);
  }
  
  //done
  /* manager function 1 : car renting */
  public static void carRenting(Connection conn) {
    /* Input user info: user id, car info: call number and copy nubmer*/
    String uid;
    String call_number;
    int copy_number;
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter The User ID: ");
    uid = scan.nextLine();
    System.out.print("Enter The Call Number: ");
    call_number = scan.nextLine();
    System.out.print("Enter The Copy Number: ");
    copy_number = scan.nextInt();

    /* Check the availablity of the car with callNumber and copyNumber */
    String sqlStatement_check;
    PreparedStatement pstmt_check;
    try {
      sqlStatement_check = "SELECT * FROM " + "rent WHERE " + "call_number = ? AND " + "copy_number = ? AND "
          + "return_date = ''";
      pstmt_check = conn.prepareStatement(sqlStatement_check);
      pstmt_check.setString(1, call_number);
      pstmt_check.setInt(2, copy_number);
      ResultSet rs_check = pstmt_check.executeQuery();
      
      if (!rs_check.next()) {
        String sqlStatement_borrow;
        PreparedStatement pstmt_borrow;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        /* Insert record */
        sqlStatement_borrow = "INSERT INTO rent (call_number, copy_number, uid, checkout_date, return_date) VALUES (?, ?, ?, ?, '')";
        pstmt_borrow = conn.prepareStatement(sqlStatement_borrow);
        pstmt_borrow.setString(1, call_number);
        pstmt_borrow.setInt(2, copy_number);
        pstmt_borrow.setString(3, uid);
        pstmt_borrow.setString(4, dtf.format(localDate));

        /* execute SQL */
        pstmt_borrow.execute();
        System.out.println("car renting performed successfully!!");
      } else {
        System.out.println("[Error]: The Car (Call Number: " + call_number + " , Copy Number: " + copy_number
            + ") has been rent!");
      }
    } catch (Exception exp) {
      System.out.println("car renting failed to perform!!");
      System.out.println("Error: " + exp);
    }
  }

  /* Manager function 2 : car returning */
  //done
  public static void carReturning(Connection conn) {
    /* Input user info: user id, car info: call number and copy nubmer*/
    String userID;
    String call_number;
    int copy_number;
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter The User ID: ");
    userID = scan.nextLine();
    System.out.print("Enter The Call Number: ");
    call_number = scan.nextLine();
    System.out.print("Enter The Copy Number: ");
    copy_number = scan.nextInt();

    /* Check the existence of the renting record with the user info and car info */
    String sqlStatement_check;
    PreparedStatement pstmt_check;
    try {
      sqlStatement_check = "SELECT * FROM " + "rent WHERE " + "uid = ? AND " + "call_number = ? AND "
          + "copy_number = ? AND " + "return_date = ''";
      pstmt_check = conn.prepareStatement(sqlStatement_check);
      pstmt_check.setString(1, userID);
      pstmt_check.setString(2, call_number);
      pstmt_check.setInt(3, copy_number);

      ResultSet rs_check = pstmt_check.executeQuery();
      /* If the result is NOT empty, return the car, otherwise do nothing and show message */
      if (rs_check.next()) {
        /* return the car */
        String sqlStatement_return;
        PreparedStatement pstmt_return;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        /* Update table */
        sqlStatement_return = "UPDATE rent " + "SET return_date = ? " + "WHERE uid = ? AND "
            + "call_number = ? AND " + "copy_number = ?";
        pstmt_return = conn.prepareStatement(sqlStatement_return);
        pstmt_return.setString(1, dtf.format(localDate));
        pstmt_return.setString(2, userID);
        pstmt_return.setString(3, call_number);
        pstmt_return.setInt(4, copy_number);

        /* execute SQL */
        pstmt_return.execute();
        /* Informative message of successfully renting */
        System.out.println("car returning performed successfully.");
      } else {
        /* The car has been return or some reason we cannot find the record! */
        System.out.println("[Error]: Cannot found such record!");
      }
    } catch (Exception exp) {
      System.out.println("car returning failed to perform.");
      System.out.println("Error: " + exp);
    }
  }
 
  /* Manager function 3 : list all un-returned car copies */
  public static void listUnreturnedCars(Connection conn) {
    /* Input the start date and end date for */
    String startDate;
    String endDate;
    int columns = 10000;
    try{
      String sqlStatement = "SELECT COUNT(*) FROM rent";
      PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
      ResultSet rs = pstmt.executeQuery();
      // Move cursor to data
      rs.next();
      columns = rs.getInt("count(*)");
    }
    catch (Exception ex){

    }
    String[][] resultsInRange = new String[columns][4];
    Scanner scan = new Scanner(System.in);
    System.out.print("Type in the starting date [DD/MM/YYYY]: ");
    startDate = scan.nextLine();
    System.out.print("Type in the ending date [DD/MM/YYYY]: ");
    endDate = scan.nextLine();

    /* Get unreturn data 
    rent (uid,   call_number,   copy_number,    checkout_date,  return_date) */
    String sqlStatement_unreturn;
    PreparedStatement pstmt_unreturn;
    int returnCount = 0;
    try {
      sqlStatement_unreturn = "SELECT uid, call_number, copy_number, checkout_date FROM " + "rent WHERE "
          + "return_date = '' " + "ORDER BY checkout_date DESC;";
      pstmt_unreturn = conn.prepareStatement(sqlStatement_unreturn);

      /* Printing the result to console */
      ResultSet rs_unreturn = pstmt_unreturn.executeQuery();

      System.out.println("|UID|CallNum|CopyNum|Checkout|");
      Boolean hasResult = false;
      while (rs_unreturn.next()) {
        String dateToCheck = rs_unreturn.getString("checkout_date");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Boolean checkDate = sdf.parse(dateToCheck).before(sdf.parse(endDate));
        checkDate = checkDate && sdf.parse(dateToCheck).after(sdf.parse(startDate));
        if(checkDate){
          hasResult = true;
          resultsInRange[returnCount][0] = rs_unreturn.getString("uid");
          resultsInRange[returnCount][1] = rs_unreturn.getString("call_number");
          resultsInRange[returnCount][2] = rs_unreturn.getString("copy_number");
          resultsInRange[returnCount][3] = dateToCheck;
          returnCount += 1;
        }
      }
      SimpleDateFormat checker = new SimpleDateFormat("dd/MM/yyyy");
      for(int i=0; i<returnCount; i++){
        for(int j=i+1; j<returnCount; j++){
          if(checker.parse(resultsInRange[i][3]).before(checker.parse(resultsInRange[j][3]))){
            String[] temp = new String[4];
            temp[0] = resultsInRange[i][0];
            temp[1] = resultsInRange[i][1];
            temp[2] = resultsInRange[i][2];
            temp[3] = resultsInRange[i][3];
            resultsInRange[i][0] = resultsInRange[j][0];
            resultsInRange[i][1] = resultsInRange[j][1];
            resultsInRange[i][2] = resultsInRange[j][2];
            resultsInRange[i][3] = resultsInRange[j][3];
            resultsInRange[j][0] = temp[0];
            resultsInRange[j][1] = temp[1];
            resultsInRange[j][2] = temp[2];
            resultsInRange[j][3] = temp[3];
          }
        }
      }
      for(int i=0; i<returnCount; i++)
      System.out.println("| " + resultsInRange[i][0] + " | " + resultsInRange[i][1] + " | " + resultsInRange[i][2] + " | " + resultsInRange[i][3] + " | ");

      if(!hasResult){
        System.out.println("[Error]: No matching data from the above criteria!!");
      }
    } catch (Exception exp) {
      System.out.println("Error: " + exp);
    }
  }
}
