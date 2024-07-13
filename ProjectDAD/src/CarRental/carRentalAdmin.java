package CarRental;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class carRentalAdmin {

    private JFrame frame;
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;

    public carRentalAdmin(String username, String usertype) {
        initialize(username, usertype);
    }

    private void initialize(String username, String usertype) {
        frame = new JFrame();
        frame.setBounds(100, 100, 1141, 597);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // welcome
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JLabel label = new JLabel("Welcome, " + username);
        panel.add(label);

        // navigation
        JPanel panel_1 = new JPanel();
        frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton homeButton = new JButton("Home");
        panel_1.add(homeButton);

        JButton profileButton = new JButton("Add New Car");
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddCarDialog();
            }
        });
        panel_1.add(profileButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            CarRentalLogin window = new CarRentalLogin();
                            window.getFrame().setVisible(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
        panel_1.add(logoutButton);

        // picture
        JPanel panel_2 = new JPanel();
        ImageIcon imageIcon1 = new ImageIcon(new ImageIcon("img/towa6.jpg").getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
        panel_2.add(new JLabel(imageIcon1));
        frame.getContentPane().add(panel_2, BorderLayout.EAST);

        // table
        String[] columnNames = {"Car ID", "Car Brand", "Plat No", "Price Per Hour", "Total Hour", "Cust Name", "I/C", "Accept", "Reject"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        TableColumn actionColumn = table.getColumnModel().getColumn(7);
        TableColumn actionColumn2 = table.getColumnModel().getColumn(8);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
        actionColumn2.setCellRenderer(new ButtonRenderer());
        actionColumn2.setCellEditor(new ButtonEditor(new JCheckBox()));

        table.setRowHeight(30);
        scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        getData();

        frame.setVisible(true);
    }

    private void showAddCarDialog() {
        JTextField carBrandField = new JTextField();
        JTextField sitTypeField = new JTextField();
        JTextField platNoField = new JTextField();
        JTextField pricePerHourField = new JTextField();

        Object[] message = {
            "Car Brand:", carBrandField,
            "Sit Type:", sitTypeField,
            "Plat No:", platNoField,
            "Price Per Hour:", pricePerHourField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add New Car", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String carBrand = carBrandField.getText();
            String sitType = sitTypeField.getText();
            String platNo = platNoField.getText();
            String pricePerHour = pricePerHourField.getText();

            if (!carBrand.isEmpty() && !sitType.isEmpty() && !platNo.isEmpty() && !pricePerHour.isEmpty()) {
                addNewCar(carBrand, sitType, platNo, pricePerHour);
            } else {
                JOptionPane.showMessageDialog(null, "All fields must be filled out.");
            }
        }
    }

    private void addNewCar(String carBrand, String sitType, String platNo, String pricePerHour) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", "addcar"));
        params.add(new BasicNameValuePair("carbrand", carBrand));
        params.add(new BasicNameValuePair("sitType", sitType));
        params.add(new BasicNameValuePair("platNo", platNo));
        params.add(new BasicNameValuePair("pricePerHour", pricePerHour));

        String strUrl = "http://localhost/ProjectDAD/restful.php";
        JSONObject response = makeHttpRequest(strUrl, "POST", params);

        try {
            if (response != null) {
                System.out.println(response.toString()); // Debugging line
                if ("success".equalsIgnoreCase(response.optString("status"))) {
                    msgbox("Car added successfully.");
                    tableModel.setRowCount(0);
                    getData();
                } else {
                    msgbox("Failed to add car.");
                }
            } else {
                System.err.println("No response from server");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error during car addition: " + e.getMessage());
        }
    }
	
	public void getData() {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", "pending"));
        params.add(new BasicNameValuePair("status", "Pending"));
    	String strUrl = "http://localhost/ProjectDAD/restful.php";
        JSONObject jsnObj = makeHttpRequest(strUrl, "POST", params);
        
        System.out.println(jsnObj.toString()); // Debugging line
        
        try {
            if (jsnObj.has("error")) {
                msgbox(jsnObj.getString("error"));
            } else {
                if (jsnObj.has("data")) {
                    JSONArray jsonArray = jsnObj.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String carId = Integer.toString(jsonObj.getInt("carid"));
                        String carBrand = jsonObj.getString("carbrand");
                        String platNo = jsonObj.getString("platNo");
                        String pricePerhour = String.valueOf(jsonObj.getDouble("pricePerHour"));
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        Date d1 = sdf.parse(jsonObj.getString("bookStart"));
                        Date d2 = sdf.parse(jsonObj.getString("bookEnd"));
                        
                        long difference_In_Time = d2.getTime() - d1.getTime();
                        int difference_In_Hours = (int) ((difference_In_Time / (1000 * 60 * 60)) % 24);
                        
                        String TotalHour = Integer.toString(difference_In_Hours);
                        String custName = jsonObj.getString("name");
                        String noIc = jsonObj.getString("noIC");

                        Object[] rowData = {carId, carBrand, platNo, pricePerhour, TotalHour,custName, noIc, "Accept", "Reject"};
                        tableModel.addRow(rowData);
                    }
                } else {
                    msgbox("No data found");
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	//jsonData
	public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {
		InputStream is = null;
	    String json = "";
	   	JSONObject jObj = null;

	   	try {
	      	if (method.equals("POST")) {
	          	DefaultHttpClient httpClient = new DefaultHttpClient();
	           	HttpPost httpPost = new HttpPost(url);
	          	httpPost.setEntity(new UrlEncodedFormEntity(params));

	           	HttpResponse httpResponse = httpClient.execute(httpPost);
	           	HttpEntity httpEntity = httpResponse.getEntity();
	           	is = httpEntity.getContent();
	      	}

	       	BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	      	StringBuilder sb = new StringBuilder();
	       	String line;
	      	while ((line = reader.readLine()) != null) {
	          	sb.append(line).append("\n");
	       	}
	      	is.close();
	       	json = sb.toString();

	     	try {
	          	jObj = new JSONObject(json);
	       	} catch (JSONException e) {
	         	JSONArray jsonArray = new JSONArray(json);
			      	if (jsonArray.length() > 0) {
			           	jObj = new JSONObject();
			          	jObj.put("data", jsonArray);
			      	} else {
			      		jObj = new JSONObject();
	                    jObj.put("error", "No Pending Approval");
	                }
	            }
	        } catch (Exception ee) {
	            ee.printStackTrace();
	        }

	        return jObj;
	    }
	
	private void msgbox(String s) {
        JOptionPane.showMessageDialog(null, s);
    }
	
	@SuppressWarnings("serial")
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    @SuppressWarnings("serial")
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                int selectedRow = table.getSelectedRow();
                int action = table.getSelectedColumn();
                String name = (String) table.getValueAt(selectedRow, 0);
                String todo = (String) table.getValueAt(selectedRow, action);
                // approval operation
                if (approval(name, todo)) {
                    msgbox("Database updated");
                    tableModel.setRowCount(0);
                    getData();
                } else {
                    msgbox("Fail to update Database");
                }
                // approval operation
            }
            isPushed = false;
            return label;
        }
        
        public boolean approval(String carId, String todo) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("login", "approval"));
            params.add(new BasicNameValuePair("todo", todo));
            params.add(new BasicNameValuePair("carid", carId));
            String strUrl = "http://localhost/ProjectDAD/restful.php";
            JSONObject response = makeHttpRequest(strUrl, "POST", params);

            try {
                if (response != null) {
                    System.out.println(response.toString()); // Debugging line
                    return "success".equalsIgnoreCase(response.optString("status"));
                } else {
                    System.err.println("No response from server");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error during approval: " + e.getMessage());
            }
            return false;
        }


        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
