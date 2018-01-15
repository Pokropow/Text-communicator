import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server {
	public static void main(String[] args) {			 
		try
		{
			Service service = new Service();
			SInterface stub = (SInterface)service;
			Registry reg = LocateRegistry.getRegistry();
			reg.bind("Service", stub);
		}
		catch (RemoteException e) 
		{			
			System.out.println("Błąd podczas zakładania serwera");
			return;
		}
		catch(Exception e)
		{
			System.out.println("Błędny url");
			e.printStackTrace();
			return;
		}
		System.out.println("Serwer działa");
	}

}
