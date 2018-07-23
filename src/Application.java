import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class Application {
	
	private static Connection con;

	private JFrame frmGenerateDomain;
	private static JTextField textField;
	private static JTextPane textPane;
	private static Config config;
	private JList list;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frmGenerateDomain.setVisible(true);
					config = new Config();
					con = new SQLConnect(config.getUrl(), config.getUsername(), config.getPassword()).getConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGenerateDomain = new JFrame();
		frmGenerateDomain.setTitle("Generate Domain");
		frmGenerateDomain.setBounds(100, 100, 600, 321);
		frmGenerateDomain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGenerateDomain.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textField.setToolTipText("Table Name");
		textField.setBounds(135, 11, 298, 35);
		frmGenerateDomain.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("SUBMIT");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String sql = " SELECT table_name, column_name, data_type, data_length FROM USER_TAB_COLUMNS WHERE DATA_TYPE = 'VARCHAR2' ";
//				PreparedStatement pre = null;
//				try {
//					con = new SQLConnect(config.getUrl(), config.getUsername(), config.getPassword()).getConnection();
//					pre = con.prepareStatement(sql);
//					ResultSet rs = pre.executeQuery();
//					String t = "";
//					while(rs.next()) {
//						t += " ALTER TABLE ";
//						t += rs.getString("TABLE_NAME");
//						t += " MODIFY (";
//						t += rs.getString("COLUMN_NAME") + " ";
//						t += rs.getString("DATA_TYPE") + "(";
//						t += rs.getString("DATA_LENGTH") + " CHAR));";
//					}
//					textPane.setText(t);
//				} catch (SQLException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}
				String sql = "";
				PreparedStatement pre = null;
				sql += " SELECT table_name, column_name, data_type, data_length ";
				sql += " FROM USER_TAB_COLUMNS ";
				sql += " WHERE table_name = ? ";
				try {
					con = new SQLConnect(config.getUrl(), config.getUsername(), config.getPassword()).getConnection();
					pre = con.prepareStatement(sql);
					pre.setString(1, textField.getText());
					ResultSet rs = pre.executeQuery();
					String txt = "";
					String[] rsArr;
					String t = "";
					txt += "public class ";
					String[] txtInp = textField.getText().split("_");
					txt += arrayToString(0, txtInp);
					t = "";
					txt += " {\n";
					while(rs.next()) {
						rsArr = rs.getString("COLUMN_NAME").split("_");
						txt += "\tprivate ";
						txt += new TypeMapping(config.getTypeSQL(), config.getTypeJava()).TypeMap(rs.getString("DATA_TYPE")) + " ";
						txt += rsArr[0].toLowerCase();
						txt += arrayToString(1, rsArr);
						txt += ";\n";
					}
					rs = pre.executeQuery();
					while(rs.next()) {
						rsArr = rs.getString("COLUMN_NAME").split("_");
						txt += "\tpublic ";
						txt += new TypeMapping(config.getTypeSQL(), config.getTypeJava()).TypeMap(rs.getString("DATA_TYPE")) + " get";
						txt += arrayToString(0, rsArr);
						txt += "() {\n";
						txt += "\t\treturn ";
						txt += rsArr[0].toLowerCase();
						txt += arrayToString(1, rsArr);
						txt += ";\n";
						txt += "\t}\n";
						txt += "\tpublic ";
						txt += "void set";
						txt += arrayToString(0, rsArr);
						txt += "(";
						txt += new TypeMapping(config.getTypeSQL(), config.getTypeJava()).TypeMap(rs.getString("DATA_TYPE")) + " ";
						txt += rsArr[0].toLowerCase();
						txt += arrayToString(1, rsArr);
						txt += ") {\n";
						txt += "\t\tthis." + rsArr[0].toLowerCase();
						txt += arrayToString(1, rsArr);
						txt += " = ";
						txt += rsArr[0].toLowerCase();
						txt += arrayToString(1, rsArr);
						txt += ";\n";
						txt += "\t}\n";
					}
					txt += "}";
					textPane.setText(txt);
					FileOutputStream out;
					File f = new File("./domain/"); // initial file (folder)
			        if (!f.exists()) { // check folder exists
			            if (f.mkdirs()) {
			                System.out.println("Directory is created!");
			            } else {
			            	System.out.println("Failed to create directory!");
			            }
			        }
					try {
						out = new FileOutputStream("./domain/" + arrayToString(0, txtInp) + ".java");
						try {
							out.write(txt.getBytes());
							out.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					con.close();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(443, 11, 131, 35);
		frmGenerateDomain.getContentPane().add(btnNewButton);
		
		JLabel lblTableName = new JLabel("TABLE NAME");
		lblTableName.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTableName.setBounds(10, 11, 117, 35);
		frmGenerateDomain.getContentPane().add(lblTableName);
				
				scrollPane_1 = new JScrollPane();
				scrollPane_1.setBounds(10, 137, 564, 134);
				frmGenerateDomain.getContentPane().add(scrollPane_1);
		
				textPane = new JTextPane();
				scrollPane_1.setViewportView(textPane);
				textPane.setEditable(false);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 57, 564, 69);
		frmGenerateDomain.getContentPane().add(scrollPane);
		
		list = new JList();
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
		            final List<String> selectedValuesList = list.getSelectedValuesList();
		            textField.setText(selectedValuesList.get(0));
		        }
			}
		});
		scrollPane.setViewportView(list);
		list.setFont(new Font("Tahoma", Font.PLAIN, 20));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new AbstractListModel() {
			String[] values = listTable();
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
	}
	
	private String[] listTable() {
		ArrayList<String> str = new ArrayList<String>();
		config = new Config();
		String sql = "";
		PreparedStatement pre = null;
		sql += " SELECT DISTINCT OBJECT_NAME ";
		sql += " FROM USER_OBJECTS ";
		sql += " WHERE OBJECT_TYPE = 'TABLE' ";
		try {
			con = new SQLConnect(config.getUrl(), config.getUsername(), config.getPassword()).getConnection();
			pre = con.prepareStatement(sql);
			ResultSet rs = pre.executeQuery();
			while(rs.next()) {
				str.add(rs.getString("OBJECT_NAME"));
			}
			con.close();
			return str.toArray(new String[str.size()]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return str.toArray(new String[str.size()]);
		}
	}
	
	private String arrayToString(int from, String[] rsArr) {
		String t = "";
		String txt = "";
		for(int i=from; i<rsArr.length; i++) {
			t = "";
			t += rsArr[i].charAt(0);
			txt += t.toUpperCase() + rsArr[i].substring(1, rsArr[i].length()).toLowerCase();
		}
		return txt;
	}
}
