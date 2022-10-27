package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Book;
import utils.DBUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;


public class ManageBooksController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label bookText;

    @FXML
    private Button addBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button removeBtn;
    
    @FXML
    private Button updateBtn;

    @FXML
    private TableColumn<Book, String> col_ID;

    @FXML
    private TableColumn<Book, String> col_name;

    @FXML
    private TableColumn<Book, String> col_author;

    @FXML
    private TableColumn<Book, String> col_stock;
    
    @FXML
    private TableColumn<Book, String> col_isbn;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TextField text_name;

    @FXML
    private TextField text_author;

    @FXML
    private TextField text_stock;

    @FXML
    private TextField text_ID;

    @FXML
    private TextField text_isbn;

    String q;
    ResultSet rs;
    PreparedStatement preStmt;
    Connection conn;
    Book bk;

    ObservableList<Book> bkList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialization (basically starts up the table, database, and the key event)
        loadData();

        if (!DBUtils.login) {
            addBtn.setVisible(false);
            removeBtn.setVisible(false);
            updateBtn.setVisible(false);
            refreshBtn.setVisible(false);

            text_name.setVisible(false);
            text_author.setVisible(false);
            text_stock.setVisible(false);
            text_ID.setVisible(false);
            text_isbn.setVisible(false);

            bookText.setText("Search for Books");
        }

        searchField.setOnKeyPressed((EventHandler<? super KeyEvent>) new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    displaySearchResult();
                }
            }
        });
    }

    private void loadData() {
        //load the table
        conn = DBUtils.conDB();
        refreshTable();

        col_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        col_isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
    }

    @FXML
    public void addItem(ActionEvent event) {
        //add in a book
        if (text_ID.getText().isEmpty() || text_author.getText().isEmpty()
            || text_name.getText().isEmpty() || text_stock.getText().isEmpty() || text_isbn.getText().isEmpty()) 
        {
            //you have to fill in all the slots
            JOptionPane.showMessageDialog(null, "fill in all the slots");
        }
        else {
            saveData();
        }
    }

    public void saveData() {
        //save the data into the database
        if (!text_ID.getText().matches("[0-9]+") || !text_stock.getText().matches("[0-9]+"))
        {
            //only integers for these fields
            JOptionPane.showMessageDialog(null, "ID/Stock only accepts digits");
        }
        else {
            try {
                //add the book with the given parameters
                String st = "INSERT INTO books (BOOK_ID, BOOK_TITLE, BOOK_AUTHOR, BOOK_STOCK, BOOK_ISBN) VALUES (?,?,?,?,?)";
                preStmt = conn.prepareStatement(st);
                preStmt.setString(1, text_ID.getText());
                preStmt.setString(2, text_name.getText());
                preStmt.setString(3, text_author.getText());
                preStmt.setString(4, text_stock.getText());
                preStmt.setString(5, text_isbn.getText());

                preStmt.executeUpdate();
            } catch (SQLException e) {
                //failed sql command. something happened and idk what lol
                System.out.println(e.getMessage());
            }

            resetText();
            refreshTable();
        }
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        //go back to the main page
        DBUtils.login = false;
        root = FXMLLoader.load(getClass().getResource("mainPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void refreshTable() {
        try {
            //refresh the table and database
            bkList.clear();
            q = "SELECT * FROM books";
            preStmt = conn.prepareStatement(q);
            rs = preStmt.executeQuery();

            while (rs.next()) {
                //the query iterates through all valid entries from the database and adds it to the visible table
                bkList.add(new Book(
                    rs.getInt("BOOK_ID"), 
                    rs.getString("BOOK_TITLE").toUpperCase(), 
                    rs.getString("BOOK_AUTHOR").toUpperCase(), 
                    rs.getInt("BOOK_STOCK"), 
                    rs.getString("BOOK_ISBN").toUpperCase()));

                tableView.setItems(bkList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void removeItem(ActionEvent event) {
        //remove book from table, given the ID
        if (text_ID.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "fill in id slot to delete");
        }
        else {
            try {
                String st = "DELETE FROM books WHERE BOOK_ID = ?";
                preStmt = conn.prepareStatement(st);
                preStmt.setString(1, text_ID.getText());
    
                preStmt.executeUpdate();
    
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            resetText();
            refreshTable();
        }
    }

    @FXML
    void updateItem(ActionEvent event) {
        // update the item, given the ID and supposed text fields
        // to be completely honest, i just wanted to automate this process because
        // i didn't want to do too much work
        if (text_ID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "fill in id slot to start update");
        } 
        else if (text_name.getText().isEmpty() && text_author.getText().isEmpty() 
            && text_isbn.getText().isEmpty() && text_stock.getText().isEmpty()) 
        {
            JOptionPane.showMessageDialog(null, "fill in one of the four slots to update");
        } else {

            // are you ready for crusty coding?
            // in order to update a book, we need to keep track of a few parameters,
            // namely, the name, author, stock, and isbn. we keep track of them with
            // a string array called 'tracker'. we also need to keep count of how many
            // parameters are actually being fed to the statement string.
            Integer count = -1;
            Integer max = 0;
            String[] tracker = new String[4];
            Integer trackerCount = 0;

            if (!text_name.getText().isEmpty())
            {
                // if this field, specifically the name, isn't empty, it is added
                // to the tracker array
                tracker[trackerCount] = text_name.getText().toUpperCase();
                trackerCount++;
                count++;
            }

            if (!text_author.getText().isEmpty())
            {
                tracker[trackerCount] = text_author.getText().toUpperCase();
                trackerCount++;
                count++;
            }

            if (!text_stock.getText().isEmpty())
            {
                tracker[trackerCount] = text_stock.getText();
                trackerCount++;
                count++;
            }

            if (!text_isbn.getText().isEmpty())
            {
                tracker[trackerCount] = text_isbn.getText();
                trackerCount++;
                count++;
            }

            // we keep a max index for one particular parameter at the end of the string
            // which is the ID of the book that is going to be updated
            max = trackerCount;

            String st = "UPDATE books SET ";
            if (!text_name.getText().isEmpty()) 
            {
                // if the text field isn't empty, we add this string
                // to 'st' itself. it will so far look like "UPDATE book SET BOOK_TITLE = ?"
                st += "BOOK_TITLE = ?";
                if (count > 0){
                    count--;
                    st += ",";
                }
            }

            if (!text_author.getText().isEmpty())
            {
                // commas are added if there are more than 1 parameters to update
                // if we have both author and name, the string will look like:
                // "UPDATE books SET BOOK_TITLE = ?, BOOK_AUTHOR = ?"
                st += "BOOK_AUTHOR = ?";
                if (count > 0){
                    count--;
                    st += ",";
                }
            }

            if (!text_stock.getText().isEmpty())
            {
                st += "BOOK_STOCK = ?";
                if (count > 0){
                    count--;
                    st += ",";
                }
            }

            if (!text_isbn.getText().isEmpty())
            {
                st += "BOOK_ISBN = ?";
            }

            // we end this string with this paramater, which will then look like:
            // UPDATE books SET BOOK_TITLE = ?, BOOK_AUTHOR = ? WHERE BOOK_ID = ?
            st += " WHERE BOOK_ID = ?";
            System.out.println(st);
            System.out.println(Arrays.toString(tracker));
            
            try {
                // more automation
                // we prepare the statement with the string that was completed previously
                // we then begin using that array, which has kept track of the fields and parameters
                // neatly.
                preStmt = conn.prepareStatement(st);
                Integer index = 1;
                for (String s: tracker) {
                    if (s == null) {
                        // do nothing because not dealing with any null objects in an array will
                        // probably cause an error.
                    }
                    else {
                        // we put down the index, which is the position of the sql statement
                        // for example:
                        // UPDATE books SET BOOK_TITLE = ?
                        // preStmt.setString(1, "name") -- this will set the parameter to have "name"
                        // what's neat is that this is automated so that it will go iterate until
                        // it has reached the limit of the string array.
                        preStmt.setString(index, s);
                    }
                    index++;
                }
                // we add the last parameter to the statement, which is the ID of the book
                // that needs to be updated
                preStmt.setString(max+1, text_ID.getText());
                preStmt.executeQuery();

            } catch (SQLException e) {
                // if this is running an error, then uhhhhhh
                // oops?
                System.out.println(e.getMessage());
            }

            resetText();
            refreshTable();
        }
    }

    private void displaySearchResult() {
        try {
            // search function
            // we specifically look for a book, given the book's ID
            bkList.clear();
            String st = searchField.getText();
            if (st.isEmpty()) {
                q = "SELECT * FROM books";
            } else {
                q = "SELECT * FROM books WHERE BOOK_ID = " + st;
            }

            preStmt = conn.prepareStatement(q);
            rs = preStmt.executeQuery();

            while (rs.next()) {
                bkList.add(new Book(
                    rs.getInt("BOOK_ID"), 
                    rs.getString("BOOK_TITLE").toUpperCase(), 
                    rs.getString("BOOK_AUTHOR").toUpperCase(), 
                    rs.getInt("BOOK_STOCK"), 
                    rs.getString("BOOK_ISBN").toUpperCase()));

                tableView.setItems(bkList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetText()
    {
        // just resets the text fields so that they're not left over
        // once the table refreshes
        text_ID.setText("");
        text_name.setText("");
        text_author.setText("");
        text_stock.setText("");
        text_isbn.setText("");
    }

}
