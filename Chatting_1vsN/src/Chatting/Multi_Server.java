package Chatting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Multi_Server {		// 1:�� ä��(����)
	private Vector<Socket> client=new Vector<Socket>(); 	// Ŭ���̾�Ʈ ���� ��ü�� �����ϱ� ���� ��ü	//�迭�� Ŭ���̾�Ʈ ���� ����
	
	class Worker extends Thread {
		private Socket socket = null;
		
		public Worker(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			String ip = null;
			String str;
			
			try {
				client.add(socket);		// ������ Ŭ���̾�Ʈ�� ���Ϳ� �����Ѵ�.
							
				ip = socket.getInetAddress().getHostAddress();			// ���� Ŭ���̾�Ʈ�� ip
				
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));		// Ŭ���̾�Ʈ�� ������ �о� ���̱� ���� �Է� ��Ʈ��
				
				str = ip + "���� ������..\n";		// �ٸ� Ŭ���̾�Ʈ���� ���� ����� �˸�
				for(Socket s : client) {
					if(socket == s)
						continue;
					
					PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
					pw.println(str);
				}
				System.out.println(str);
				
				while((str = br.readLine()) != null) {		// Ŭ���̾�Ʈ�� ���� �޽��� �ϱ�
					for(Socket s: client) {		// �ٸ� Ŭ���̾�Ʈ���� ����
						if(socket == s)
							continue;
						
						PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
						pw.println(str);
					}
				}
				
			} catch (Exception e) {
				str = ip + " �� ������ �����Ͽ����ϴ�.";
				System.out.println(ip);
				try {
					for(Socket s : client) {		// �ٸ� Ŭ���̾�Ʈ���� ���� ��� �˸�
						if(s == socket)
							continue;
						PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
						pw.println(str);
					}
					
				} catch (Exception e2) {
					System.out.println(e2.toString());
				}
				
				client.remove(socket);
				socket = null;
				
			}
		}
	}
	
	public void serverStart() {
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(12345);
			System.out.println("���� ����...");
			
			while(true) {
				Socket socket = serverSocket.accept();		// Ŭ���̾�Ʈ�� �����ؼ� �����⸦ �����
				
				Worker th = new Worker(socket);		// ������ Ŭ���̾�Ʈ�� ó���ϱ� ����  ������ ��ü
				th.start();
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	public static void main(String[] args) {
		new Multi_Server().serverStart();
	}
}
