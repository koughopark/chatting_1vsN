package Chatting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Multi_Server {		// 1:다 채팅(서버)
	private Vector<Socket> client=new Vector<Socket>(); 	// 클라이언트 소켓 객체를 저장하기 위한 객체	//배열에 클라이언트 정보 넣음
	
	class Worker extends Thread {
		private Socket socket = null;
		
		public Worker(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			String ip = null;
			String str;
			
			try {
				client.add(socket);		// 접속한 클라이언트를 백터에 저장한다.
							
				ip = socket.getInetAddress().getHostAddress();			// 접속 클라이언트의 ip
				
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));		// 클라이언트의 정보를 읽어 들이기 위한 입력 스트림
				
				str = ip + "에서 접속함..\n";		// 다른 클라이언트에게 접속 사실을 알림
				for(Socket s : client) {
					if(socket == s)
						continue;
					
					PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
					pw.println(str);
				}
				System.out.println(str);
				
				while((str = br.readLine()) != null) {		// 클라이언트가 보낸 메시지 일기
					for(Socket s: client) {		// 다른 클라이언트에게 전송
						if(socket == s)
							continue;
						
						PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
						pw.println(str);
					}
				}
				
			} catch (Exception e) {
				str = ip + " 가 접속을 해제하였습니다.";
				System.out.println(ip);
				try {
					for(Socket s : client) {		// 다른 클라이언트에게 퇴장 사실 알림
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
			System.out.println("서버 시작...");
			
			while(true) {
				Socket socket = serverSocket.accept();		// 클라이언트가 접속해서 들어오기를 대기함
				
				Worker th = new Worker(socket);		// 접속한 클라이언트를 처리하기 위한  스레드 객체
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
