package ar.com.textillevel.gui.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class RemoteFileUtil {

	private static RemoteFileUtil instance = new RemoteFileUtil();
	
	private String server;
	private String userName;
	private String password;

	private RemoteFileUtil() {
		this.server = "127.0.0.1";
		this.userName = "GTL";
		this.password = "diego";
	}

	public static RemoteFileUtil getInstance() {
		return instance;
	}

	public void uploadFile(String fileName, File source) throws MalformedURLException, IOException {
		FTPUtil.upload(server, userName, password, fileName, source);
	}

	public void downloadFile(String fileName, File destination) throws MalformedURLException, IOException {
		FTPUtil.download(server, userName, password, fileName, destination);
	}

	public void uploadFileCreatingFolder(String fileNameToTransfer, File file) throws MalformedURLException, IOException {
		FTPUtil.uploadFileCreatingFolder(server, userName, password, fileNameToTransfer, file);
	}

}