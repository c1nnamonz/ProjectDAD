package CarRental;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class testing {

    public testing(String username, String usertype) {
        initialize(username, usertype);
    }
    

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testing window = new testing(null, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    private void initialize(String username,String usertype) {
    	JFrame frame = new JFrame("Alert Box Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        // Create a button to trigger the alert
        JButton showAlertButton = new JButton("Show Alert");
        frame.add(showAlertButton);

        // Add action listener to the button
        showAlertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the alert dialog with time input
                showAlertWithTimeInput(frame);
            }
        });

        // Display the main frame
        frame.setVisible(true);
	}
	
    private static void showAlertWithTimeInput(JFrame parentFrame) {
        // Create a dialog for the alert
        JDialog alertDialog = new JDialog(parentFrame, "Enter Time", true);
        alertDialog.setSize(300, 150);
        alertDialog.setLayout(new FlowLayout());

        // Create time input components
        JLabel hourLabel = new JLabel("Hour:");
        JTextField hourField = new JTextField(2);

        JLabel minuteLabel = new JLabel("Minute:");
        JTextField minuteField = new JTextField(2);

        // Add components to the dialog
        alertDialog.add(hourLabel);
        alertDialog.add(hourField);
        alertDialog.add(minuteLabel);
        alertDialog.add(minuteField);

        // Create OK button
        JButton okButton = new JButton("OK");
        alertDialog.add(okButton);

        // Add action listener to the OK button
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int hour = Integer.parseInt(hourField.getText());
                    int minute = Integer.parseInt(minuteField.getText());
                    if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                        throw new NumberFormatException();
                    }
                    // Print the input time to the console (or perform any action you need)
                    System.out.println("Time entered: " + hour + ":" + String.format("%02d", minute));
                    alertDialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(alertDialog, "Please enter valid time values.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Display the alert dialog
        alertDialog.setVisible(true);
    }
    
}
