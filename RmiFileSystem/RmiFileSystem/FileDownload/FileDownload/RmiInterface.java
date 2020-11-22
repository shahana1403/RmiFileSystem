package FileDownload;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiInterface extends Remote {
	public void FTPUtility(String host, int port, String user, String pass) throws RemoteException;
	public void connect() throws RemoteException, IOException;
	public long getFileSize(String filePath) throws RemoteException,FTPException;
	public void downloadFile(String downloadPath) throws RemoteException,FTPException;
	public void finish() throws RemoteException,IOException;
	public void disconnect() throws RemoteException;
	public InputStream getInputStream()throws RemoteException;
	
	
	
	}
