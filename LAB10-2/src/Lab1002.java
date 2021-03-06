import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Lab1002 extends Application {
    private TextField tfAnnualInterestRate = new TextField();
    private TextField tfNumberOfYears = new TextField();
    private TextField tfLoanAmount = new TextField();
    private TextField tfMonthlyPayment = new TextField();
    private TextField tfTotalPayment = new TextField();
    private Button btCalculate = new Button("Calculate");
    private Button btSave = new Button("Save");
    private Button btLoad = new Button("Load");
    private Button btClear = new Button("Clear");

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.add(new Label("Annual Interest Rate:"), 0, 0);
        gridPane.add(tfAnnualInterestRate, 1, 0);
        gridPane.add(new Label("Number of Years:"), 0, 1);
        gridPane.add(tfNumberOfYears, 1, 1);
        gridPane.add(new Label("Loan Amount:"), 0, 2);
        gridPane.add(tfLoanAmount, 1, 2);
        gridPane.add(new Label("Monthly Payment:"), 0, 3);
        gridPane.add(tfMonthlyPayment, 1, 3);
        gridPane.add(new Label("Total Payment:"), 0, 4);
        gridPane.add(tfTotalPayment, 1, 4);

        HBox saveLoad = new HBox(btSave, btLoad);
        HBox clearCal = new HBox(btClear, btCalculate);
        saveLoad.setAlignment(Pos.CENTER);
        clearCal.setAlignment(Pos.CENTER);
        saveLoad.setSpacing(19);
        clearCal.setSpacing(20);
        gridPane.add(saveLoad, 0, 6);
        gridPane.add(clearCal, 1, 6);

        gridPane.setAlignment(Pos.CENTER);
        tfAnnualInterestRate.setAlignment(Pos.BOTTOM_RIGHT);
        tfNumberOfYears.setAlignment(Pos.BOTTOM_RIGHT);
        tfLoanAmount.setAlignment(Pos.BOTTOM_RIGHT);
        tfMonthlyPayment.setAlignment(Pos.BOTTOM_RIGHT);
        tfTotalPayment.setAlignment(Pos.BOTTOM_RIGHT);
        tfMonthlyPayment.setEditable(false);
        tfTotalPayment.setEditable(false);

        btCalculate.setOnAction(this::calculated);
        btClear.setOnAction(e -> {
            tfAnnualInterestRate.clear();
            tfNumberOfYears.clear();
            tfLoanAmount.clear();
            tfMonthlyPayment.clear();
            tfTotalPayment.clear();
        });
        btSave.setOnAction(this::save);
        btLoad.setOnAction(this::load);

        Scene scene = new Scene(gridPane, 300, 200);
        primaryStage.setTitle("LoanCalculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void save(ActionEvent ae) {
        Loan loan;
        try {
            double interest = Double.parseDouble(tfAnnualInterestRate.getText());
            int year = Integer.parseInt(tfNumberOfYears.getText());
            double loanAmount = Double.parseDouble(tfLoanAmount.getText());
            loan = new Loan(interest, year, loanAmount);
        } catch (Exception e) {
            return;
        }
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("loan.dat"))) {
            o.writeObject(loan);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load(ActionEvent ae) {
        try (ObjectInputStream o = new ObjectInputStream(new FileInputStream("loan.dat"))) {
            Loan loan = (Loan)o.readObject();
            tfAnnualInterestRate.setText(loan.getAnnualInterestRate() + "");
            tfNumberOfYears.setText(loan.getNumberOfYears() + "");
            tfLoanAmount.setText(loan.getLoanAmount() + "");
            tfMonthlyPayment.setText(String.format("$%.2f", loan.getMonthlyPayment()));
            tfTotalPayment.setText(String.format("$%.2f", loan.getTotalPayment()));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    private void calculated(ActionEvent ae) {
        try {
            double interest = Double.parseDouble(tfAnnualInterestRate.getText());
            int year = Integer.parseInt(tfNumberOfYears.getText());
            double loanAmount = Double.parseDouble(tfLoanAmount.getText());
            Loan loan = new Loan(interest, year, loanAmount);
            tfMonthlyPayment.setText(String.format("$%.2f", loan.getMonthlyPayment()));
            tfTotalPayment.setText(String.format("$%.2f", loan.getTotalPayment()));
        } catch (Exception e) {
            return;
        }
    }

}