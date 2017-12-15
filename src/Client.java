import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Client {
	
	private JFrame frame;
	private JTextField textField;
	private DefaultListModel<String> model;
	private SInterface serv=null;
	private JTextField textField_1;
	private Communicator clnt;
	
	boolean registered = false;
	String name;
	
	public static void main(String[] args) {				 
		 
		 
		 EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Client window = new Client(args[0]);
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});		 
	} 
	public Client(String host) throws RemoteException
	{		
		try 
		 {
			Registry registry = LocateRegistry.getRegistry(host);		
			serv = (SInterface)registry.lookup("Service");	
			initialize();
		 } 
		 catch (Exception e) 
		 {
			 System.out.printf("Error: b³¹d podczas komunikacji z serwerem: %s\n",e.getMessage());
		 }
	}
	private void initialize() throws RemoteException
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 380, 360);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 44, 235, 198);
		panel.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(255, 44, 99, 198);
		panel.add(scrollPane_1);
		
		model = new DefaultListModel<String>();
		JList<String> list = new JList<String>(model);		
		scrollPane_1.setViewportView(list);
		
		JButton btnNewButton = new JButton("Po\u0142\u0105cz");
		btnNewButton.setBounds(10, 287, 89, 23);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Roz\u0142\u0105cz");
		btnNewButton_1.setBounds(255, 287, 99, 23);
		panel.add(btnNewButton_1);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(10, 253, 235, 23);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnWylij = new JButton("Wy\u015Blij");
		btnWylij.setBounds(255, 253, 99, 23);
		panel.add(btnWylij);		
		JButton btnNewButton_2 = new JButton("Zarejestruj/Wyrejestruj");
		
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				try {
					if(!registered)
					{
						String s = textField_1.getText();						
						int res = serv.zapisz(clnt, s);
						if(res!=0)
						{
							registered = true;
							textField_1.setEditable(false);
							name = s;
							textField.setEditable(true);
							String[] names = serv.listuj();
							model.clear();
							for(String str : names)
							{
								if(!str.equals(s))
								{
									model.addElement(str);
								}
							}
						}
					}
					else
					{
						registered=false;
						serv.wypisz(name);
						model.clear();
						textField_1.setEditable(true);
						textField.setEditable(false);
					}					
				} catch (RemoteException e) {
					e.printStackTrace();
				}				
			}
		});
		btnNewButton_2.setBounds(208, 10, 146, 23);
		panel.add(btnNewButton_2);
		
		textField_1 = new JTextField();
		textField_1.setBounds(10, 11, 188, 20);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		
		clnt = new Communicator();
		clnt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String text = clnt.getText();
					if(text.length()==0)
					{
						String[] names;						
						names = serv.listuj();
						model.clear();
						for(String str : names)
						{							
							if(!str.equals(name))
							{							
								model.addElement(str);
							}
						}
					}
					else
					{
						textArea.setText(textArea.getText()+text+"\n");
					}
				}	
				catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
			}			
		});
	}
}
