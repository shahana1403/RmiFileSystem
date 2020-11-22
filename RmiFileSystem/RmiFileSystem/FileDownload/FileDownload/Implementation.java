package FileDownload;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class Implementation extends UnicastRemoteObject implements RmiInterface {

	// FTP server information
	private String host;
	private int port;
	private String username;
	private String password;

	private FTPClient ftpClient = new FTPClient();
	private int replyCode;

	private InputStream inputStream;
	
	Implementation()throws RemoteException{
		super();
	}

	public void FTPUtility(String host, int port, String user, String pass) throws RemoteException {
		
		this.host = host;
		this.port = port;
		this.username = user;
		this.password = pass;
	}

	// Connect and login to the server.
	public void connect() throws RemoteException, IOException{
		try {
			ftpClient.connect(host, port);
			replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("FTP server refused connection.");
			}

			boolean logged = ftpClient.login(username, password);
			if (!logged) {
				// failed to login
				ftpClient.disconnect();
				System.out.println("Could not login to the server.");
			}

			ftpClient.enterLocalPassiveMode();

		} catch (IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	
	public long getFileSize(String filePath) throws RemoteException, FTPException{
		try {
			FTPFile file = ftpClient.mlistFile(filePath);
			if (file == null) {
				throw new FTPException("The file may not exist on the server!");
			}
			return file.getSize();
		} catch (IOException ex) {
			throw new FTPException("Could not determine size of the file: "
					+ ex.getMessage());
		}
	}

	public void downloadFile(String downloadPath) throws RemoteException {
		try {

			boolean success = ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			if (!success) {
				System.out.println("Could not set binary file type.");
			}

			inputStream = ftpClient.retrieveFileStream(downloadPath);

			if (inputStream == null) {
				System.out.println("Could not open input stream. The file may not exist on the server.");
			}
		} catch (IOException ex) {
			System.out.println("Error downloading file: " + ex.getMessage());
		}
	}

	
	public void finish() throws RemoteException,IOException{
		inputStream.close();
		ftpClient.completePendingCommand();
	}

	
	public void disconnect() throws RemoteException {
		if (ftpClient.isConnected()) {
			try {
				if (!ftpClient.logout()) {
					System.out.println("Could not log out from the server");
				}
				ftpClient.disconnect();
			} catch (IOException ex) {
				System.out.println("Error disconnect from the server: "
						+ ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	public InputStream getInputStream() throws RemoteException {
		return inputStream;
	}

	 

}