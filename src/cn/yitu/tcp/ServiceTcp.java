package cn.yitu.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cn.yitu.entity.User;
import cn.yitu.serverUI.ServerManager;
import cn.yitu.tcp.customer.Client;

public class ServiceTcp extends Thread implements Runnable{
	private int port;
	ServerSocket serverSocket = null;

	public ServiceTcp() {

	}

	public ServiceTcp(int port) {
		this.port = port;
	}

	private List<User> users = new ArrayList<User>();

//	public void start() {
//		ServerSocket serverSocket = null;
//		try {
//			serverSocket = new ServerSocket(port);
//			while (true) {
//				Socket socket = serverSocket.accept();
//				System.out.println("客户端已连接");
//				Client client = new Client(socket, users);
//				Thread thread = new Thread(client);
//				thread.start();
//			}
//		} catch (IOException e) {
//			System.out.println("启动失败!!!");
//			e.printStackTrace();
//		}
//	}
	public void run(){
		
		try {
			serverSocket = new ServerSocket(port);
			//System.out.println("服务器地址："+serverSocket.getInetAddress());
			//System.out.println("服务器已开启");
			ServerManager.textArea.append("服务器已开启\r\n");
			while (true) {
				Socket socket = serverSocket.accept();
				//System.out.println("客户端已连接");
				ServerManager.textArea.append("客户端已连接\r\n");
				Client client = new Client(socket, users);
				Thread thread = new Thread(client);
				thread.start();
			}
		} catch (IOException e) {
			ServerManager.textArea.append("连接出错！！！\r\n");
			e.printStackTrace();
		}
	}

	
//	class serverThread extends Thread {
//		public void run() {
//			ServerSocket serverSocket = null;
//			try {
//				serverSocket = new ServerSocket(port);
//				//System.out.println("服务器地址："+serverSocket.getInetAddress());
//				System.out.println("服务器已开启");
//				while (true) {
//					Socket socket = serverSocket.accept();
//					System.out.println("客户端已连接");
//					Client client = new Client(socket, users);
//					Thread thread = new Thread(client);
//					thread.start();
//				}
//			} catch (IOException e) {
//				System.out.println("连接出错!!!");
//				e.printStackTrace();
//			}
//		}
//	}
//	
////	public static void main(String[] args) {
////		ServiceTcp serviceTcp = new ServiceTcp(8080);
////		serviceTcp.start();
////	}
}
