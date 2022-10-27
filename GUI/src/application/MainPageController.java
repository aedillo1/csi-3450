package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import utils.DBUtils;

public class MainPageController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField bookAuthor;
    @FXML
    private TextField bookISBN;
    @FXML
    private TextField bookTitle;
    @FXML
    private Button donateBtn;
    @FXML
    private Label donateLabel;
    @FXML
    private Button loginBtn;
    @FXML
    private Button searchLibraryBtn;
    @FXML
    private TextField userID;
    @FXML
    private PasswordField userPass;

    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = DBUtils.conDB();
    }

    public void addBook() {
        if (bookAuthor.getText().isEmpty() && bookISBN.getText().isEmpty()
            && bookTitle.getText().isEmpty()) {
                donateLabel.setText("Fill in one of the slots");
                donateLabel.setTextFill(Color.RED);
        }
        else {
            saveData(bookTitle.getText(), bookAuthor.getText(), bookISBN.getText());
        }
    }

    private void saveData(String title, String author, String isbn) {
        String st = "";
        String st2 = "INSERT INTO books (";
        System.out.println("title: " + title + "\nauthor: " + author + "\nisbn: " + isbn);

        Boolean checker = false;
        String[] tracker = new String[3];
        Integer count = -1;
        Integer trackerCount = 0;
        Integer max = 0;

        if (!title.isEmpty() && !checker)
        {
            try {
                //select all entries where the book title is the same as the given title name
                st = "SELECT * FROM books WHERE BOOK_TITLE = ?";
                pst = conn.prepareStatement(st);
                pst.setString(1, title);
                
                rs = pst.executeQuery();

                if (!rs.next()) {
                    //if the entry doesn't exist, start the counter
                    tracker[trackerCount] = title.toUpperCase();
                    trackerCount++;
                    count++;
                } else {
                    //if it does exist, update the stock by 1, and turn checker on
                    checker = true;
                    st = "UPDATE books SET BOOK_STOCK = BOOK_STOCK + 1 WHERE BOOK_TITLE = ?";
                    pst = conn.prepareStatement(st);
                    pst.setString(1, title.toUpperCase());
                    pst.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (!author.isEmpty() && !checker)
        {
            try {
                //select all entries where the author is the same as the given author name
                st = "SELECT * FROM books WHERE BOOK_AUTHOR = ?";
                pst = conn.prepareStatement(st);
                pst.setString(1, author);
                rs = pst.executeQuery();

                if (!rs.next()) {
                    //if the entry doesn't exist, start the counter
                    tracker[trackerCount] = author.toUpperCase();
                    trackerCount++;
                    count++;
                } else {
                    //if it does exist, update the stock by 1
                    st = "UPDATE books SET BOOK_STOCK = BOOK_STOCK + 1 WHERE BOOK_AUTHOR = ?";
                    pst = conn.prepareStatement(st);
                    pst.setString(1, author.toUpperCase());
                    pst.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        if (!isbn.isEmpty() && !checker)
        {
            try {
                //select all entries where the isbn is the same as the given isbn
                st = "SELECT * FROM books WHERE BOOK_ISBN = ?";
                pst = conn.prepareStatement(st);
                pst.setString(1, isbn);
                rs = pst.executeQuery();

                if (!rs.next()) {
                    //if the entry doesn't exist, start the counter(?)
                    tracker[trackerCount] = isbn.toUpperCase();
                    trackerCount++;
                    count++;
                } else {
                    //if it does exist, update the stock by 1
                    st = "UPDATE books SET BOOK_STOCK = BOOK_STOCK + 1 WHERE BOOK_ISBN = ?";
                    pst = conn.prepareStatement(st);
                    pst.setString(1, isbn.toUpperCase());
                    pst.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (checker) {
            resetText();
            donateLabel.setText("Book successfully donated!");
            donateLabel.setTextFill(Color.GREEN);
        } else {
            max = count;
            System.out.println(max);
            System.out.println(count);

            if (!title.isEmpty())
            {
                st2 += "BOOK_TITLE";
                if (count > 0)
                {
                    count--;
                    st2 += ",";
                }
            }
    
            if (!author.isEmpty())
            {
                st2 += "BOOK_AUTHOR";
                if (count > 0)
                {
                    count--;
                    st2 += ",";
                }
            }
    
            if (!isbn.isEmpty())
            {
                st2 += "BOOK_ISBN";
            }
    
            switch(max)
            {
                default:
                    System.out.println("what the hell am i");
                    break;
                case 0:
                    st2 += ") VALUES (?)";
                    break;
                case 1:
                    st2 += ") VALUES (?,?)";
                    break;
                case 2:
                    st2 += ") VALUES (?,?,?)";
                    break;
            }
    
            System.out.println(st2);
            try {
                pst = conn.prepareStatement(st2);
                Integer index = 1;
                for (String s: tracker) {
                    if (s == null) {
                        // do nothing
                    } else {
                        pst.setString(index, s);
                    }
                    index++;
                }

                pst.executeQuery();

                resetText();
                donateLabel.setText("Book successfully donated!");
                donateLabel.setTextFill(Color.GREEN);
                
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void checkLogin(ActionEvent event) throws IOException {
        String uname = userID.getText();
        String pass = userPass.getText();

        if(uname.equals("") && pass.equals(""))
        {
            JOptionPane.showMessageDialog(null, "Username and Password Blank");
        }
        else
        {
            try {
                conn = DBUtils.conDB();
                String st = "employees";

                pst = conn.prepareStatement("SELECT * FROM " + st + " WHERE EMP_ID=? AND EMP_PASS=?");

                pst.setString(1, uname);
                pst.setString(2, pass);

                rs = pst.executeQuery();

                if(rs.next())
                {
                    JOptionPane.showMessageDialog(null, "Login Success");
                    DBUtils.login = true;
                    switchTo_empMenu(event);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Login Failed");
                    userID.setText("");
                    userPass.setText("");
                    userID.requestFocus();
                }
            } catch (SQLException e) {
                System.out.println("wrong sql command");
                e.printStackTrace();
            }
        }
    }

    public void switchTo_empMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manageBooks.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void resetText()
    {
        bookTitle.setText("");
        bookAuthor.setText("");
        bookISBN.setText("");
    }
    

}
