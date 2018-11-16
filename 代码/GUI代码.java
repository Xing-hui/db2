import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.sql.*;
import java.io.*;
import java.util.*;
import java.math.*;


public class user{
	private String acc_number;
	private String password;
	private double balance;
}
public class a extends AbstractTableModel{
	 static {
		  try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public static  Connection getconnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/account? characterEncoding=UTF-8", "root",
              "123456");
	}
	
	public class user {
		private String name;
		private String password;
		private double balance; 
		public user() {
        super();
			}
		public String getName() {
			return name;
			}
		public void setName(String name) {
			this.name = name;
			}
		public String getPassword() {
			return password;
			}
		public void setPassword(String password) {
			this.password = password;
			}
		public double getbalance() {
			return balance;
			}
		public void setbalance(double balance) {
			this.balance = balance;
			}
		}
	
	public class ExecuteSQL {
    protected static String dbClassName = "com.mysql.jdbc.Driver";
    protected static String dbUrl = "jdbc:mysql://localhost:3306/atm";
    protected static String dbUser = "root";
    protected static String dbPwd = "root";
    private static Connection conn = null;

    private ExecuteSQL() {
        try {
            if (conn == null) {
                Class.forName(dbClassName).newInstance();
                conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
            }
            else
                return;
        } catch (Exception ee) {
            ee.printStackTrace();
        }

    }

    //重写executeQuer方法
    //返回ResultSet结果集
    private static ResultSet executeQuery(String sql) {
        try {
            if(conn==null)
            new ExecuteSQL();
            return conn.createStatement().executeQuery(sql);//ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
        }
    }

    //重写executeUpdate方法
    private static int executeUpdate(String sql) {  
        try {
            if(conn==null)
                new ExecuteSQL();
            return conn.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());         
            return -1;
        } finally {
        }
    }

    public static void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            conn = null;
        }
    }

    //登录测试账号 密码
    public static user check(String name,String password){
        int i = 0;
        user u = new user();
        String sql = "select name, password from bank where name = '"+name+"'";
        ResultSet rs = ExecuteSQL.executeQuery(sql);
		password
        try {
            while(rs.next()){               
                u.setName(rs.getString("name"));
                u.setPassword(rs.getString("password"));    
            //  u.setbalance(rs.getFloat("balance"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ExecuteSQL.close();
        return u;
    }
    //注册，添加用户信息
    public static int addUser(String name,String password){
        int i = 0;
        String sql = "insert into bank(name,password,balance)"
                + "values('"+name+"','"+password+"','"+ 0 +"')";
        i = ExecuteSQL.executeUpdate(sql);
        ExecuteSQL.close();
        return i;

    }

    //查询用户信息
    public static user query(String name){
        user u = new user();
        String sql = "select name,balance from bank where name = '"+name+"'";
        ResultSet rs =  ExecuteSQL.executeQuery(sql);
        try {
            while(rs.next()){
                u.setName(rs.getString("name"));
                u.setbalance(rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ExecuteSQL.close();
        return u;
    }

    // 修改账户的余额
    public static int  modifyMoney(String name, double balance) {
        user u = new user();
        String sql = "update bank set balance = '" + balance + "' where name ='" + name + "'";
        int i  =  ExecuteSQL.executeUpdate(sql);
        ExecuteSQL.close();
        return i;
    }   
}
	
	 //登录事件监听器
    class loginAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
			int count = 0;
            user = ExecuteSQL.check(u_name_Field.getText(), u_password_field.getText());
			for (int count = 0 ;count <= 2; count++; ){}
            if (user.getName() != null) {
                if (user.getPassword().equals(u_password_field.getText())) {
                    try {
                        atmFrame frame = new atmFrame(user.getName());
                        frame.setTitle(user.getName());
                        frame.setVisible(true);
                        setVisible(false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "密码不正确！请重新输入");
                    u_name_Field.setText("");
                    u_password_field.setText("");
                }

            } else {
                JOptionPane.showMessageDialog(null, "找不到该用户，请先注册！");
                u_name_Field.setText("");
                u_password_field.setText("");
            }
        }
    }

    //注册事件监听器
    class signAction implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            SignFrame frame = new SignFrame();
            frame.setVisible(true);
            setVisible(false);
        }

    }
	
	
	//注册按钮监听器
    class OKButtonAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            if (!u_name.getText().equals("")) {
                if (!u_password.getText().equals("")) {
                    if (!u_password_1.getText().equals("")) {
                        if (u_password.getText().equals(u_password_1.getText())) {
                            user = ExecuteSQL.check(u_name.getText(), u_password.getText());
                            if (!u_name.getText().equals(user.getName())) {
                                 ExecuteSQL.addUser(u_name.getText(), u_password.getText());
                                setVisible(false);
                                LoginFrame frame = new LoginFrame();
                                frame.setVisible(true);
                                JOptionPane.showMessageDialog(null, "注册成功,可以继续登录！");
                            } else {
                                JOptionPane.showMessageDialog(null, "用户名已经存在！");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "密码确认不符！");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "未输入确认密码！");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "未输入密码！");
                }
            } else {
                JOptionPane.showMessageDialog(null, "未输入用户名！");
            }
        }

    }
	
	
	OKButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(Float.parseFloat(inputField.getText())<100000){
                    user_query = ExecuteSQL.query(name);
                    double temp = user_query.getbalance()+Double.parseDouble(inputField.getText());
                    DecimalFormat df = new DecimalFormat( "0.00 "); 
                    int i = ExecuteSQL.modifyMoney(name,temp);  
                    if(i>0){
                        setVisible(false);
                        atmFrame frame = new atmFrame(name);
                        frame.setVisible(true);
                        JOptionPane.showMessageDialog(null, "交易成功！"+"\n" + "当前余额为：" + df.format(temp)); 
                    }else{
                        JOptionPane.showMessageDialog(null, "交易失败!"+"\n" + "当前余额为：" + df.format(temp));
                    }

                }else{
                    JOptionPane.showMessageDialog(null, "输入金额大于1000000，请重新输入！");
                    inputField.setText("");
                }               
            }

        });
	
	OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int res = JOptionPane.showConfirmDialog(null, "确认此次转账？", "转账确认", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    user_query_deposit = ExecuteSQL.query(out_nameField.getText());//转出钱
                    if (out_nameField.getText().equals(user_query_deposit.getName())) {
                        if (Float.parseFloat(out_moneyField.getText()) < 100000) {
                            user_query_withdraw = ExecuteSQL.query(name);
                            // user_1 = ExecuteSQL.query(out_nameField);
                            if (user_query_withdraw.getbalance() > Double.parseDouble(out_moneyField.getText())) {
                                double temp = user_query_withdraw.getbalance() - Double.parseDouble(out_moneyField.getText());
                                double temp_1 = user_query_deposit.getbalance() + Double.parseDouble(out_moneyField.getText());
                                DecimalFormat df = new DecimalFormat("0.00 ");
                                int i_withdraw = ExecuteSQL.modifyMoney(name, temp);
                                int i_deposit = ExecuteSQL.modifyMoney(out_nameField.getText(), temp_1);
                                if(i_withdraw>0 && i_deposit>0){
                                    setVisible(false);
                                    atmFrame frame = new atmFrame(name);
                                    frame.setVisible(true);
                                    JOptionPane.showMessageDialog(null, "转账交易成功！" + "\n" + "剩余余额为：" + df.format(temp));
                                }else{
                                    JOptionPane.showMessageDialog(null, "转账交易失败！" + "\n" + "剩余余额为：" + df.format(temp));                     
                                }                                                       
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "余额不足，请重新输入！" + "\n" + "当前余额为：" + user_query_withdraw.getbalance());
                                out_moneyField.setText("");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "转账金额必须10000，请重新输入！");
                            out_moneyField.setText("");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "找不到该转账用户！");
                        out_nameField.setText("");
                    }
                } else {
                    return;
                }
            }
        });
	
}