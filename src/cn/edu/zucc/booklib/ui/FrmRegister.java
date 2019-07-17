package cn.edu.zucc.booklib.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.edu.zucc.booklib.control.SystemUserManager;
import cn.edu.zucc.booklib.model.BeanSystemUser;
import cn.edu.zucc.booklib.util.BaseException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class FrmRegister extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField password1;
	private JPasswordField password2;
	private JTextField textField_name;
	private JTextField textField_email;
	//判断字符串是否包含数字
		public static boolean isContainNumber(String company) {

	        Pattern p = Pattern.compile("[0-9]");
	        Matcher m = p.matcher(company);
	        if (m.find()) {
	            return true;
	        }
	        return false;
	    }
		
	//判断字符串是否都为数字
			public static boolean isNumeric(String str)
				{
				   for (int i = str.length();--i>=0;)
				   {  
				        if (!Character.isDigit(str.charAt(i)))
				        {
				            return false;
				        }
				   }
				    return true;
				 }
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmRegister frame = new FrmRegister();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrmRegister() {
		setTitle("注册界面");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 305, 352);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btn_confirm = new JButton("\u786E\u5B9A");
		btn_confirm.setBounds(0, 240, 130, 30);
		btn_confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cusers = textField.getText();
				String pwd1 = new String(password1.getPassword());
				String pwd2 = new String(password2.getPassword());
				String cemail = textField_email.getText();
				String cname = textField_name.getText();
				if(isNumeric(cusers)==true)
				{
					JOptionPane.showMessageDialog(null, "账户不能全为数字或为空","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(isContainNumber(cname)==true)
				{
					JOptionPane.showMessageDialog(null, "用户名不能含有数字","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
//					BeanCus bc = PetsysUtil.cusManager.reg(cusers, pwd1, pwd2, cname, ctelnum, cemail,cother);
//					CustomerLogin cuslogin = new CustomerLogin();
//					cuslogin.setVisible(true);
					BeanSystemUser bsu =  new SystemUserManager().reg(cusers, pwd1, pwd2, cname, cemail, FrmFileChoose.text);
					if(bsu !=null)
						dispose();
				} catch (BaseException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
					return;
					// TODO: handle exception
				}
				
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btn_confirm);
		
		textField = new JTextField();
		textField.setBounds(0, 116, 130, 26);
		textField.setToolTipText("请输入你喜欢的用户名");
		contentPane.add(textField);
		textField.setColumns(10);
		
		password1 = new JPasswordField();
		password1.setBounds(0, 162, 130, 26);
		password1.setToolTipText("请输入你的密码");
		contentPane.add(password1);
		
		password2 = new JPasswordField();
		password2.setBounds(158, 162, 130, 26);
		password2.setToolTipText("为了安全，请再次输入你的密码");
		contentPane.add(password2);
		
		JLabel label_newuser = new JLabel("*\u65B0\u7684\u8D26\u6237\uFF1A                  *\u7528\u6237\u59D3\u540D\uFF1A");
		label_newuser.setBounds(34, 101, 278, 16);
		label_newuser.setForeground(Color.BLACK);
		label_newuser.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		contentPane.add(label_newuser);
		
		JLabel label_renewpwd = new JLabel("*\u786E\u8BA4\u5BC6\u7801\uFF1A");
		label_renewpwd.setBounds(178, 146, 71, 16);
		label_renewpwd.setForeground(Color.BLACK);
		label_renewpwd.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		contentPane.add(label_renewpwd);
		
		JLabel label_newpwd = new JLabel("*\u65B0\u7684\u5BC6\u7801\uFF1A");
		label_newpwd.setBounds(34, 146, 71, 16);
		label_newpwd.setForeground(Color.BLACK);
		label_newpwd.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		contentPane.add(label_newpwd);
		
		textField_name = new JTextField();
		textField_name.setBounds(158, 116, 130, 26);
		contentPane.add(textField_name);
		textField_name.setColumns(10);
		
		JButton btn_back = new JButton("");
		btn_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmRegister frmreg = new FrmRegister();
				frmreg.setVisible(true);
				dispose();
			}
		});
		
		textField_email = new JTextField();
		textField_email.setBounds(0, 202, 130, 26);
		contentPane.add(textField_email);
		textField_email.setColumns(10);
		
		JLabel label_1 = new JLabel("温馨提示：*代表必填项");
		label_1.setBounds(6, 25, 167, 16);
		label_1.setForeground(Color.RED);
		contentPane.add(label_1);
		
		JLabel lblNewLabel = new JLabel("电子邮箱：");
		lblNewLabel.setBounds(40, 189, 78, 16);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		contentPane.add(lblNewLabel);
		
		JButton button_bacl = new JButton("\u9000\u51FA");
		button_bacl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button_bacl.setBounds(158, 241, 130, 29);
		contentPane.add(button_bacl);
		
		JButton btnNewButton_upload = new JButton("\u4E0A\u4F20\u7167\u7247");
		btnNewButton_upload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmFileChoose ffc = new FrmFileChoose();
				ffc.setVisible(true);
			}
		});
		btnNewButton_upload.setBounds(158, 200, 130, 29);
		contentPane.add(btnNewButton_upload);
		
		JLabel label = new JLabel("");
		label.setBounds(0, 0, 305, 330);
		contentPane.add(label);
	}
}
