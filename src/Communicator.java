import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Communicator extends UnicastRemoteObject implements KInterface {
	
	private List<ActionListener> listeners;
	String text = null;
	public Communicator() throws RemoteException {
		super();
		listeners = new ArrayList<ActionListener>();
	}
	

	@Override
	public void pisz(String s) throws RemoteException {	
		text=s;
		for(ActionListener al:listeners)
		{
			al.actionPerformed(null);
		}
	}
	String getText()
	{
		return text;
	}
	void addActionListener(ActionListener al)
	{
		listeners.add(al);
	}
}
