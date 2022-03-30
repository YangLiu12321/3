import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;


import java.io.FileNotFoundException;

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
    // TODO code application logic here
    Connection con = null;
    String dbAdress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db9";
    String dbUsername = "Group9";
    String dbPassword = "CSCI3170";
    try {
      Class.forName("com.mysql.jdbc.Driver");
      con = DriverManager.getConnection(dbAdress, dbUsername, dbPassword);
    } catch (ClassNotFoundException e) {
      // handle any errors
      System.out.println("[Error]: Java MySQL DB Driver not found!!");
      System.exit(0);
    } catch (SQLException e) {
        System.out.println(e);
    }
  }

  public static void main_menu(Connection conn) {
    int input;
    System.out.println("\n-----Main Menu-----");
    System.out.println("What kinds of operations would you like to perform?");
    System.out.println("1. Operations for administrator");
    System.out.println("2. Operations for library user");
    System.out.println("3. Operations for librarian");
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
      librarian_operation(conn);
    else if (input == 4) {
      System.out.println("GoodBye!!!");
      System.exit(1);
    }
  }

  public static void admin_operation(Connection conn) {
    int input;
    System.out.println("\n-----Operations for administrator menu-----");
    System.out.println("What kinds of operations would you like to perform?");
    System.out.println("1. Create all tables");
    System.out.println("2. Delete all tables");
    System.out.println("3. Load Data");
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
  public static void createAllTables(Connection conn) {
    System.out.println("Create ALL Table...\n");
    System.out.println("Processing...\n");
    try {
      String sqlStatement_create_category;
      PreparedStatement pstmt_create_category;

      /* create table: category */
      sqlStatement_create_category = "CREATE TABLE category(" + "id integer primary key,"
          + "max_books integer not null," + "loan_period integer not null)";

      pstmt_create_category = conn.prepareStatement(sqlStatement_create_category);
      /* execute SQL */

      /* create table: user */
      String sqlStatement_create_user;
      PreparedStatement pstmt_create_user;

      sqlStatement_create_user = "CREATE TABLE user(" + "user_id varchar(10) primary key,"
          + "name varchar(25) not null," + "address varchar(100) not null," + "category_id integer not null,"
          + "FOREIGN KEY(category_id) REFERENCES category(id))";

      pstmt_create_user = conn.prepareStatement(sqlStatement_create_user);
      /* execute SQL */

      /* create table: book */
      String sqlStatement_create_book;
      PreparedStatement pstmt_create_book;

      sqlStatement_create_book = "CREATE TABLE book(" + "call_number varchar(8) primary key,"
          + "title varchar(30) not null," + "publish_date varchar(10) not null)";

      pstmt_create_book = conn.prepareStatement(sqlStatement_create_book);
      /* execute SQL */

      /* create table: author */
      String sqlStatement_create_author;
      PreparedStatement pstmt_create_author;

      sqlStatement_create_author = "CREATE TABLE author(" + "name varchar(25) not null,"
          + "call_number varchar(8) not null," + "PRIMARY KEY(name, call_number),"
          + "FOREIGN KEY(call_number) REFERENCES book(call_number))";

      pstmt_create_author = conn.prepareStatement(sqlStatement_create_author);
      /* execute SQL */

      /* create table: copy */
      String sqlStatement_create_copy;
      PreparedStatement pstmt_create_copy;

      sqlStatement_create_copy = "CREATE TABLE copy(" + "call_number varchar(8) not null,"
          + "copy_number integer not null," + "PRIMARY KEY(call_number, copy_number),"
          + "FOREIGN KEY(call_number) REFERENCES book(call_number));";

      pstmt_create_copy = conn.prepareStatement(sqlStatement_create_copy);
      /* execute SQL */

      /* create table: checkout_record */
      String sqlStatement_create_checkout_record;
      PreparedStatement pstmt_create_checkout_record;

      sqlStatement_create_checkout_record = "CREATE TABLE checkout_record(" + "user_id varchar(10) not null,"
          + "call_number varchar(8) not null," + "copy_number integer not null," + "checkout_date varchar(10) not null,"
          + "return_date varchar(10)," + "PRIMARY KEY(user_id, call_number, copy_number, checkout_date),"
          + "FOREIGN KEY(user_id) REFERENCES user(user_id),"
          + "FOREIGN KEY(call_number, copy_number) REFERENCES copy(call_number, copy_number))";

      pstmt_create_checkout_record = conn.prepareStatement(sqlStatement_create_checkout_record);
      /* execute SQL */
      pstmt_create_category.executeUpdate();
      pstmt_create_user.executeUpdate();
      pstmt_create_book.executeUpdate();
      pstmt_create_author.executeUpdate();
      pstmt_create_copy.executeUpdate();
      pstmt_create_checkout_record.executeUpdate();

      System.out.println("All talbes are created successfully!!!");
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }

  }

  /* Load ALL test data for database */
  public static void LoadData(Connection conn) {
    Scanner scan = new Scanner(System.in);
    System.out.print("Type in the Source Data Folder Path: ");
    String path = scan.nextLine();
    try {
      System.out.println("Processing...\n");
      loadDataCategory(conn, path);
      loadDataUser(conn, path);
      loadDataBookAndAuthor(conn, path);
      loadDataCheckoutRecord(conn, path);
      System.out.println("Data are loaded successfully!\n");
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
    return;
  }

  /* load testdata for table: Category */
  public static void loadDataCategory(Connection conn, String path) throws Exception {
    File file = new File(path + "/" + "category.txt");
    Scanner scan = new Scanner(file);

    PreparedStatement pstmt = conn
        .prepareStatement("INSERT INTO category (id, max_books, loan_period) VALUES (?, ?, ?)");
    while (scan.hasNextLine()) {
      String data = scan.nextLine();
      String[] result = data.split("\t");
      for (int i = 0; i < result.length; i++)
        pstmt.setString(i + 1, result[i]);
      pstmt.execute();
    }
    System.out.println("Data of Category have been loaded successfully!\n");
    return;
  }

  /* load test data for table: User */
  public static void loadDataUser(Connection conn, String path) throws Exception {
    File file = new File(path + "/" + "user.txt");
    Scanner scan = new Scanner(file);

    PreparedStatement pstmt = conn
        .prepareStatement("INSERT INTO user (user_id, name, address, category_id) VALUES (?, ?, ?, ?)");
    while (scan.hasNextLine()) {
      String data = scan.nextLine();
      String[] result = data.split("\t");
      for (int i = 0; i < result.length; i++)
        pstmt.setString(i + 1, result[i]);
      pstmt.execute();
    }
    System.out.println("Data of User have been loaded successfully!\n");
    return;
  }

  /* load test data for tables: Book, Copy and Author*/
  public static void loadDataBookAndAuthor(Connection conn, String path) throws Exception {
    String[] result;
    File file = new File(path + "/" + "book.txt");
    Scanner scan = new Scanner(file);
    PreparedStatement ps_book = conn
        .prepareStatement("INSERT INTO book (call_number, title, publish_date) VALUES (?, ?, ?)");
    PreparedStatement ps_copy = conn.prepareStatement("INSERT INTO copy (call_number, copy_number) VALUES (?,?)");
    PreparedStatement ps_author = conn.prepareStatement("INSERT INTO author (name, call_number) VALUES (?,?)");

    while (scan.hasNextLine()) {
      String data = scan.nextLine();
      result = data.split("\t");
      String author = result[3];
      String[] nameList = author.split(",");
      try {
        for (int i = 0; i < nameList.length; i++) {
          ps_author.setString(1, nameList[i]);
          ps_author.setString(2, result[0]);
          ps_author.execute();
        }
      } catch (Exception ex) {
      }

      // book.txt: {call_number, copy_number, title, arthur_name, publish_date}
      ps_copy.setString(1, result[0]);
      ps_copy.setString(2, result[1]);
      ps_book.setString(1, result[0]);
      ps_book.setString(2, result[2]);
      ps_book.setString(3, result[4]);

      ps_copy.execute();
      ps_book.execute();
    }
    System.out.println("Data of Book and Author have been loaded successfully!\n");
  }

  /* load test data for table: Checkout_record */
  public static void loadDataCheckoutRecord(Connection conn, String path) throws Exception {
    File file = new File(path + "/" + "checkout.txt");
    Scanner scan = new Scanner(file);
    PreparedStatement pstmt = conn.prepareStatement(
        "INSERT INTO checkout_record (user_id, call_number, copy_number, checkout_date, return_date) VALUES (?, ?, ?, ?, ?)");
    while (scan.hasNextLine()) {
      // checkout.txt: {call_number, copy_number, user_id ,checkout_date, return_date}
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
    System.out.println("Data of Checkout Record have been loaded successfully!\n");
  }

  public static void dropAllTable(Connection conn) {
    System.out.println("Processing...");
    String[] tables = { "category", "user", "book", "copy", "checkout_record", "author" };
    String sqlStatement = "DROP TABLE IF EXISTS  ";
    try {
      for (int i = 0; i < tables.length; i++) {
        String temp = sqlStatement + tables[i];
        PreparedStatement pstmt = conn.prepareStatement(temp);
        pstmt.executeUpdate();
      }
      System.out.println("Done! Tables are removed!");
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
  }

  public static void showAllTable(Connection conn) {
    System.out.println("\nNumber of records in each table: ");
    String[] tables = { "category", "user", "book", "copy", "checkout_record", "author" };
    String sqlStatement = "SELECT COUNT(*) FROM ";
    try {
      for (int i = 0; i < tables.length; i++) {
        String temp = sqlStatement + tables[i];
        PreparedStatement pstmt = conn.prepareStatement(temp);
        ResultSet rs = pstmt.executeQuery();
        // Move cursor to data
        rs.next();
        int count = rs.getInt("count(*)");
        System.out.println("\033[3m" + tables[i] + "\033[0m" + ": " + count);
      }
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
  }

  public static void user_operation(Connection conn) {
    int input;
    System.out.println("\n-----Operations for library user menu-----");
    System.out.println("What kinds of operations would you like to perform?");
    System.out.println("1. Search for books");
    System.out.println("2. Show checkout records of a user");
    System.out.println("3. Return to the main menu");
    Scanner scan = new Scanner(System.in);
    do {
      System.out.print("Enter Your Choice: ");
      input = scan.nextInt();
    } while (input < 1 || input > 3);
    if (input == 1)
      bookSearch(conn);
    else if (input == 2)
      showUserRecord(conn);
    else if (input == 3)
      main_menu(conn);
    user_operation(conn);
  }

  public static void bookSearch(Connection conn) {
    int input;
    String searchKey, callNumber;
    System.out.println("Choose the search criteria:");
    System.out.println("1. Call Number");
    System.out.println("2. Title");
    System.out.println("3. Author");
    Scanner scan = new Scanner(System.in);
    int columns = 10000;
    try{
      String sqlStatement = "SELECT COUNT(*) FROM book";
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
        sqlStatement = "SELECT * FROM " + "book, copy, author WHERE " + "book.call_number = copy.call_number AND "
            + "book.call_number = author.call_number AND " + "author.call_number = copy.call_number AND "
            + "book.call_number = ?";
        pstmt = conn.prepareStatement(sqlStatement);
        pstmt.setString(1, callNumber);
      } else if (input == 2) {
        searchKey = keyword.nextLine();
        sqlStatement = "SELECT * FROM " + "book, copy, author WHERE " + "book.call_number = copy.call_number AND "
            + "book.call_number = author.call_number AND " + "author.call_number = copy.call_number AND "
            + "book.title LIKE BINARY ?";
        pstmt = conn.prepareStatement(sqlStatement);
        searchKey = "%" + searchKey + "%";
        pstmt.setString(1, searchKey);
      } else {
        searchKey = keyword.nextLine();
        sqlStatement = "SELECT * FROM " + "book, copy, author WHERE " + "book.call_number = copy.call_number AND "
            + "book.call_number = author.call_number AND " + "author.call_number = copy.call_number AND "
            + "author.name LIKE BINARY ?";
        pstmt = conn.prepareStatement(sqlStatement);
        searchKey = "%" + searchKey + "%";
        pstmt.setString(1, searchKey);
      }
      // Parse Output
      ResultSet rs = pstmt.executeQuery();
      System.out.println("| Call Number | Title | Author |  Available Copies |");
      int copyResult = 0;
      String titleResult = "", authorResult = "", callResult = "";
      String[][] resultSet = new String[columns][3];
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
        resultSet[count][1] = rs.getString("title");
        resultSet[count][2] = rs.getString("name");
        copyNumberSet[count] = rs.getInt("copy_number");
        
        count += 1;

        String sqlStatement_unreturn = "SELECT count(return_date) FROM checkout_record WHERE return_date = '' AND call_number = ? group by call_number";
        PreparedStatement unreturnPstmt = conn.prepareStatement(sqlStatement_unreturn);
        unreturnPstmt.setString(1, callResult);
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
          System.out.println("| " + resultSet[i][0] + " | " + resultSet[i][1] + " | " + resultSet[i][2] + " | " + copyNumberSet[i] + "  |");
      }
    } catch (Exception exp) {
      System.out.println("[Error]: An matching search record is not found. The input does not exist in database.");
    }
  }

  public static void showUserRecord(Connection conn) {
    String userID;
    try {
      System.out.print("Enter the User ID: ");
      Scanner scan = new Scanner(System.in);
      String sqlStatement;
      PreparedStatement pstmt;
      // Build SQL statement
      userID = scan.nextLine();
      sqlStatement = "SELECT * FROM " + "book, copy, author, checkout_record WHERE "
          + "book.call_number = copy.call_number AND " + "book.call_number = author.call_number AND "
          + "author.call_number = copy.call_number AND " + "checkout_record.call_number = book.call_number AND "
          + "checkout_record.user_id = ?";
      pstmt = conn.prepareStatement(sqlStatement);
      pstmt.setString(1, userID);

      // Parse Output
      ResultSet rs = pstmt.executeQuery();
      System.out.println("| Call Number | Copy Number | Title | Author | Check-out | Returned? |");
      Boolean hasResult = false;
      int copyResult = 0;
      String titleResult = "", authorResult = "", callResult = "", checkoutResult = "", returnResult = "";
      while (rs.next()) {
        hasResult = true;
        String callTemp = rs.getString("call_number");
        if (callTemp.equals(callResult)) {
          authorResult = authorResult + ", " + rs.getString("name");
        } else {
          if (!callResult.equals(""))
            System.out.println("| " + callResult + " | " + copyResult + " | " + titleResult + " | " + authorResult
                + " | " + checkoutResult + " | " + returnResult + "  |");
          callResult = callTemp;
          copyResult = rs.getInt("copy_number");
          titleResult = rs.getString("title");
          authorResult = rs.getString("name");
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
        System.out.println("| " + callResult + " | " + copyResult + " | " + titleResult + " | " + authorResult + " | "
            + checkoutResult + " | " + returnResult + "  |");
    } catch (Exception exp) {
      System.out.println("[Error]: An matching search record is not found. The input does not exist in database.");
    }
  }

  public static void librarian_operation(Connection conn) {
    int input;
    System.out.println("\n-----Operations for librarian menu-----");
    System.out.println("What kinds of operations would you like to perform?");
    System.out.println("1. Book Borrowing");
    System.out.println("2. Book Returning");
    System.out.println("3. List all un-returned book copies which are checked out within a period");
    System.out.println("4. Return to the main menu");
    Scanner scan = new Scanner(System.in);
    do {
      System.out.print("Enter Your Choice: ");
      input = scan.nextInt();
    } while (input < 1 || input > 4);
    if (input == 1)
      bookBorrowing(conn);
    else if (input == 2)
      bookReturning(conn);
    else if (input == 3)
      listUnreturnedBooks(conn);
    else if (input == 4)
      main_menu(conn);
    librarian_operation(conn);
  }

  /* librarian function 1 : book borrowing */
  public static void bookBorrowing(Connection conn) {
    /* Input user info: user id, book info: call number and copy nubmer*/
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

    /* Check the availablity of the book with callNumber and copyNumber */
    String sqlStatement_check;
    PreparedStatement pstmt_check;
    try {
      sqlStatement_check = "SELECT * FROM " + "checkout_record WHERE " + "call_number = ? AND " + "copy_number = ? AND "
          + "return_date = ''";
      pstmt_check = conn.prepareStatement(sqlStatement_check);
      pstmt_check.setString(1, call_number);
      pstmt_check.setInt(2, copy_number);
      ResultSet rs_check = pstmt_check.executeQuery();
      
      /* If the result is empty, borrow the book, otherwise do nothing and show message */
      if (!rs_check.next()) {
        /* Borrow the book */
        String sqlStatement_borrow;
        PreparedStatement pstmt_borrow;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        /* Insert record */
        sqlStatement_borrow = "INSERT INTO checkout_record (call_number, copy_number, user_id, checkout_date, return_date) VALUES (?, ?, ?, ?, '')";
        pstmt_borrow = conn.prepareStatement(sqlStatement_borrow);
        pstmt_borrow.setString(1, call_number);
        pstmt_borrow.setInt(2, copy_number);
        pstmt_borrow.setString(3, userID);
        pstmt_borrow.setString(4, dtf.format(localDate));

        /* execute SQL */
        pstmt_borrow.execute();
        /* Informative message of successfully checkout */
        System.out.println("Book checkout performed successfully!!");
      } else {
        /* The book has been borrowed! */
        System.out.println("[Error]: The Book (Call Number: " + call_number + " , Copy Number: " + copy_number
            + ") has been borrowed!");
      }
    } catch (Exception exp) {
      System.out.println("Book checkout failed to perform!!");
      System.out.println("Error: " + exp);
    }
  }

  /* librarian function 2 : book returning */
  public static void bookReturning(Connection conn) {
    /* Input user info: user id, book info: call number and copy nubmer*/
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

    /* Check the existence of the checkout record with the user info and book info */
    String sqlStatement_check;
    PreparedStatement pstmt_check;
    try {
      sqlStatement_check = "SELECT * FROM " + "checkout_record WHERE " + "user_id = ? AND " + "call_number = ? AND "
          + "copy_number = ? AND " + "return_date = ''";
      pstmt_check = conn.prepareStatement(sqlStatement_check);
      pstmt_check.setString(1, userID);
      pstmt_check.setString(2, call_number);
      pstmt_check.setInt(3, copy_number);

      ResultSet rs_check = pstmt_check.executeQuery();
      /* If the result is NOT empty, return the book, otherwise do nothing and show message */
      if (rs_check.next()) {
        /* return the book */
        String sqlStatement_return;
        PreparedStatement pstmt_return;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        /* Update table */
        sqlStatement_return = "UPDATE checkout_record " + "SET return_date = ? " + "WHERE user_id = ? AND "
            + "call_number = ? AND " + "copy_number = ?";
        pstmt_return = conn.prepareStatement(sqlStatement_return);
        pstmt_return.setString(1, dtf.format(localDate));
        pstmt_return.setString(2, userID);
        pstmt_return.setString(3, call_number);
        pstmt_return.setInt(4, copy_number);

        /* execute SQL */
        pstmt_return.execute();
        /* Informative message of successfully checkout */
        System.out.println("Book returning performed successfully!!!");
      } else {
        /* The book has been return or some reason we cannot find the record! */
        System.out.println("[Error]: Cannot found such record!");
      }
    } catch (Exception exp) {
      System.out.println("Book returning failed to perform!!");
      System.out.println("Error: " + exp);
    }
  }

  /* librarian function 3 : list all un-returned book copies */
  public static void listUnreturnedBooks(Connection conn) {
    /* Input the start date and end date for */
    String startDate;
    String endDate;
    int columns = 10000;
    try{
      String sqlStatement = "SELECT COUNT(*) FROM checkout_record";
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
    checkout_record (user_id,   call_number,   copy_number,    checkout_date,  return_date) */
    String sqlStatement_unreturn;
    PreparedStatement pstmt_unreturn;
    int returnCount = 0;
    try {
      sqlStatement_unreturn = "SELECT user_id, call_number, copy_number, checkout_date FROM " + "checkout_record WHERE "
          + "return_date = '' " + "ORDER BY checkout_date DESC;";
      pstmt_unreturn = conn.prepareStatement(sqlStatement_unreturn);

      /* Printing the result to console */
      ResultSet rs_unreturn = pstmt_unreturn.executeQuery();

      System.out.println("| User ID | Call Number | Copy Number | Checkout Date |");
      Boolean hasResult = false;
      while (rs_unreturn.next()) {
        String dateToCheck = rs_unreturn.getString("checkout_date");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Boolean checkDate = sdf.parse(dateToCheck).before(sdf.parse(endDate));
        checkDate = checkDate && sdf.parse(dateToCheck).after(sdf.parse(startDate));
        if(checkDate){
          hasResult = true;
          resultsInRange[returnCount][0] = rs_unreturn.getString("user_id");
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
