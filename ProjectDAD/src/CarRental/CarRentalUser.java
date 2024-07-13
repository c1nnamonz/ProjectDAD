package CarRental;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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

public class CarRentalUser {

    private JFrame frame;
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    
    private String id;
    private JTextField textField;

    public CarRentalUser(String username, String usertype, String userid) {
        setUserId(userid);
        initialize(username, usertype);
    }
    
    public void setUserId(String userId) {
        this.id = userId;
    }
    
    public String getUserId() {
        return id;
    }

    private void initialize(String username, String usertype) {
        frame = new JFrame();
        frame.setBounds(100, 100, 1141, 597);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

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

        JButton btnNewButton_1 = new JButton("Profile");
        panel_1.add(btnNewButton_1);

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
        JLabel label2 = new JLabel("Total Hours:");
        
        panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
        panel_2.add(new JLabel(imageIcon1));
        panel_2.add(label2);
        frame.getContentPane().add(panel_2, BorderLayout.EAST);
        
        textField = new JTextField();
        panel_2.add(textField);
        textField.setColumns(1);
        Dimension size = new Dimension(100, 25);
        textField.setMaximumSize(size);

        // table content
        String[] columnNames = {"Car ID", "Car Brand", "Sit Type", "Plat No", "Price Per Hour", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        TableColumn actionColumn = table.getColumnModel().getColumn(5);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
        
        table.setRowHeight(30);
        scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        getData();

        frame.setVisible(true);
    }
    
    public void getData() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", "cartable"));
        params.add(new BasicNameValuePair("status", "Available"));
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
                        String sitType = jsonObj.getString("sitType");
                        String platNo = jsonObj.getString("platNo");
                        String pricePerHour = String.valueOf(jsonObj.getDouble("pricePerHour"));

                        Object[] rowData = {carId, carBrand, sitType, platNo, pricePerHour, "Book Now"};
                        tableModel.addRow(rowData);
                    }
                } else {
                    msgbox("No data found");
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
    
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
                    jObj.put("error", "No Available Cars");
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
                String name = (String) table.getValueAt(selectedRow, 0);
                int hour;
                try {
                    hour = Integer.parseInt(textField.getText());
                    if (hour < 0 || hour > 23) {
                        throw new NumberFormatException();
                    }
                    // booking operation
                    if (booking(name, hour)) {
                        msgbox("Database updated");
                        tableModel.setRowCount(0);
                        getData();
                    } else {
                        msgbox("Fail to update Database");
                    }
                    // booking operation
                } catch (NumberFormatException ex) {
                    msgbox("Please enter a valid hour (0-23).");
                }
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    
    public boolean booking(String carId, int hour) {
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", "booking"));
        params.add(new BasicNameValuePair("id", carId));
        params.add(new BasicNameValuePair("userid", getUserId()));
        params.add(new BasicNameValuePair("hour", Integer.toString(hour)));
        
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
            System.err.println("Error during booking: " + e.getMessage());
        }
        return false;
    }
}
