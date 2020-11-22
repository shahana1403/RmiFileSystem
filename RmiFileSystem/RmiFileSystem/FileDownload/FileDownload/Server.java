package FileDownload;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.RemoteException;
import java.rmi.registry.*;

public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			RmiInterface skeleton=new Implementation();
			Registry registry=LocateRegistry.createRegistry(21);
			registry.rebind("ss",skeleton);
			
		}catch(Exception e) {
			System.out.println("Exception in Server:"+e);
		}
	}

}
