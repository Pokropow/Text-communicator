import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {
	
	private JFrame frmKomunikator;
	private JTextField textField;
	private DefaultComboBoxModel<String> cmodel;
	private SInterface serv=null;
	private JTextField textName;
	private Communicator clnt;
	private KInterface partner=null;
	private JTextArea textArea;
	
	boolean registered = false;
	String name;
	
	public static void main(String[] args) {
		 EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Client window = new Client(args[0]);
						window.frmKomunikator.setVisible(true);
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
			 System.out.printf("Error: błąd podczas komunikacji z serwerem: %s\n",e.getMessage());
		 }
	}
	
	void print(String s)
	{
		textArea.setText(textArea.getText() + s);
	}
	void println(String s)
	{
		textArea.setText(textArea.getText() + s + '\n');
	}
	void logout() throws RemoteException
	{
		registered = false;
		serv.wypisz(name);
		clnt=null;
		partner = null;
	}
	private void initialize() throws RemoteException
	{
		frmKomunikator = new JFrame();
		frmKomunikator.setTitle("Komunikator");
		frmKomunikator.setBounds(100, 100, 380, 360);
		frmKomunikator.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);		
		frmKomunikator.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {}

			@Override
			public void windowClosed(WindowEvent arg0) {}

			@Override
			public void windowClosing(WindowEvent arg0) {
				if(registered)
				{
					try {
						logout();
						System.out.println("Wylogowano");
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				frmKomunikator.dispose();
				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {}

			@Override
			public void windowDeiconified(WindowEvent arg0) {}

			@Override
			public void windowIconified(WindowEvent arg0) {}

			@Override
			public void windowOpened(WindowEvent arg0) {}			
		});
		
		JPanel panel = new JPanel();
		frmKomunikator.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 44, 344, 198);
		panel.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		JButton btnPolacz = new JButton("Po\u0142\u0105cz");		
		btnPolacz.setBounds(156, 287, 89, 23);
		panel.add(btnPolacz);
		
		JButton btnRozlacz = new JButton("Roz\u0142\u0105cz");
		btnRozlacz.setEnabled(false);		
		btnRozlacz.setBounds(255, 287, 99, 23);
		panel.add(btnRozlacz);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(10, 253, 235, 23);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnWyslij = new JButton("Wy\u015Blij");
		btnWyslij.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(partner!=null)
				{
					String text = textField.getText();
					try {
						partner.pisz(name+":"+text);
						println(name+":"+text);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnWyslij.setEnabled(false);
		btnWyslij.setBounds(255, 253, 99, 23);
		panel.add(btnWyslij);		
		JButton btnRejestruj = new JButton("Zarejestruj/Wyrejestruj");
		
		
		cmodel = new DefaultComboBoxModel<>();
		JComboBox<String> comboBox = new JComboBox<String>(cmodel);
		comboBox.setBounds(10, 288, 136, 20);
		panel.add(comboBox);		
		
		btnPolacz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					String s = comboBox.getItemAt(comboBox.getSelectedIndex());
					if(s!=null)
					{
						partner = serv.pobierz(s);
						if(partner!=null)
						{
							btnPolacz.setEnabled(false);
							btnRozlacz.setEnabled(true);
							btnWyslij.setEnabled(true);
						}
					}
				}
				catch(Exception e)
				{
					
				}				
			}
		});
		
		btnRozlacz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				partner=null;
				btnPolacz.setEnabled(true);
				btnRozlacz.setEnabled(false);
				btnWyslij.setEnabled(false);
			}
		});
		
		btnRejestruj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				try {
					if(!registered)
					{
						String s = textName.getText();
						int res = serv.zapisz(clnt, s);
						if(res!=0)
						{
							registered = true;
							textName.setEditable(false);
							name = s;
							textField.setEditable(true);
							String[] names = serv.listuj();
							cmodel.removeAllElements();
							for(String str : names)
							{
								if(!str.equals(s))
								{
									cmodel.addElement(str);
								}
							}
						}
					}
					else
					{
						registered=false;
						serv.wypisz(name);
						cmodel.removeAllElements();
						textName.setEditable(true);
						textField.setEditable(false);
					}					
				} catch (RemoteException e) {
					
					e.printStackTrace();
				}				
			}
		});
		btnRejestruj.setBounds(208, 10, 146, 23);
		panel.add(btnRejestruj);
		
		textName = new JTextField();
		textName.setBounds(10, 11, 188, 20);
		panel.add(textName);
		textName.setColumns(10);	
		
		
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
						cmodel.removeAllElements();
						for(String str : names)
						{							
							if(!str.equals(name))
							{							
								cmodel.addElement(str);
							}
						}
					}
					else
					{
						textArea.setText(textArea.getText()+text+"\n");
					}
				}	
				catch (RemoteException e1) {
					e1.printStackTrace();
				}	
			}			
		});
	}
}
