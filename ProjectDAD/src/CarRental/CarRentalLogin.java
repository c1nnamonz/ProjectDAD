package CarRental;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class CarRentalLogin {

    private JFrame frame;
    private JTextField textField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CarRentalLogin window = new CarRentalLogin();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CarRentalLogin() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Username: ");
        lblNewLabel.setBounds(80, 104, 72, 14);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblPassword = new JLabel("Password: ");
        lblPassword.setBounds(80, 140, 72, 14);
        frame.getContentPane().add(lblPassword);

        textField = new JTextField();
        textField.setBounds(162, 101, 172, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("CAR RENTAL SERVICE");
        lblNewLabel_1.setBounds(94, 21, 242, 56);
        lblNewLabel_1.setFont(new Font("Dubai Medium", Font.BOLD, 20));
        frame.getContentPane().add(lblNewLabel_1);

        passwordField = new JPasswordField();
        passwordField.setBounds(162, 137, 172, 20);
        frame.getContentPane().add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
            public void actionPerformed(ActionEvent e) {
                if (validation(textField.getText())) {
                    msgbox("Please Insert Username");
                } else {
                    if (validation(passwordField.getText())) {
                        msgbox("Please Insert Password");
                    } else {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("login", "userlogin"));
                        params.add(new BasicNameValuePair("username", textField.getText()));
                        params.add(new BasicNameValuePair("password", passwordField.getText()));

                        String strUrl = "http://localhost/ProjectDAD/restful.php";
                        JSONObject jsnObj = makeHttpRequest(strUrl, "POST", params);

                        try {
                            if (jsnObj.has("error")) {
                                msgbox(jsnObj.getString("error"));
                            } else {
                                String username = jsnObj.getString("usrname");
                                String usertype = Integer.toString(jsnObj.getInt("usertype"));
                                String userid = Integer.toString(jsnObj.getInt("id"));

                                if (jsnObj.getInt("usertype") == 2) {
                                    new carRentalAdmin(username, usertype);
                                    frame.dispose();
                                } else {
                                    new CarRentalUser(username, usertype, userid);
                                    frame.dispose();
                                }

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                }
            }
        });
        loginButton.setBounds(245, 177, 89, 23);
        frame.getContentPane().add(loginButton);

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
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();

            // Handling JSON object or array
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                JSONArray jsonArray = new JSONArray(json);
                if (jsonArray.length() > 0) {
                    jObj = jsonArray.getJSONObject(0);
                } else {
                    jObj = new JSONObject();
                    jObj.put("error", "Incorrect combination");
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return jObj;
    }

    boolean validation(String string) {
        return string == null || string.length() == 0;
    }

    private void msgbox(String s) {
        JOptionPane.showMessageDialog(null, s);
    }

    public JFrame getFrame() {
        return frame;
    }
}