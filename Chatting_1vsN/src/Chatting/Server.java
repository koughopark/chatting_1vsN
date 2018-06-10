package Chatting;

import java.awt.Component;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;

public class Server extends Frame implements ActionListener, Runnable {

	private JFrame frame;
	private final JScrollPane scrollPane = new JScrollPane();
	private JTextArea textArea; // 채팅 출력
	private JTextField textField; // 채팅 입력
	private JButton btnNewButton; // 전송 버튼
	
	private int port = 12345; //포트번호
	
	private ServerSocket serverSocket = null;
	private Socket socket = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Server ChatServer = new Server();
		ChatServer.frame.setVisible(true);
		ChatServer.serverStart();
	}

	/**
	 * Create the application.
	 */
	public Server() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 449, 336);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().setLayout(null);
		scrollPane.setBounds(0, 0, 434, 262);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		textField = new JTextField();
		textField.setBounds(0, 272, 323, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.addActionListener(this);
	
		btnNewButton = new JButton("입력");
		btnNewButton.addActionListener(this);				
		btnNewButton.setBounds(326, 272, 108, 28);
		frame.getContentPane().add(btnNewButton);
		
		Server.this.frame.setTitle("서버");
	}
	
	public void serverStart(){
		try{
			serverSocket = new ServerSocket(port);	//서버소켓 생성
			textArea.setText("서버 시작....\n");
			
			socket = serverSocket.accept();		//클라이언트 접속 		//여기서 안됨
		
			Thread thread = new Thread(this);		//클라이언트가 접속하면 스레드 실행
			thread.start();
			
		}catch(Exception e){
			System.out.println(e.toString());
			textArea.setText(e.toString());
		}
	}
	
	@Override
	public void run() {
		String ip = null;
		String str;
		
		try{			
			if(socket == null)	
				return;		// 접속한 클라이언트가 없으면
						
			ip = socket.getInetAddress().getHostAddress();	// 접속한 클라이언트의 IP주소 얻기
			textArea.append(ip + " 에서 서버에 접속함..\n");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));		//클라이언트가 보낸정보를 읽어 들이는 입력 스트림 		//중요
			
			while((str = br.readLine()) != null){	//클라이언트의 정보를 읽어 들인다.
				textArea.append(str + "\n");
			}
		}catch(Exception e){		//클라이언트가 접속을 해제 한 경우
			textArea.append(ip + " 가 접속을 해제하였습니다.");
			socket=null;
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
			String s=textField.getText().trim();
			if(s.length()==0)
				return;
			
			try{
				if(socket==null)		//클라이언트와 접속이 안되면
					return;
				
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);			//서버가 채팅문자열을 클라이언트에게 보낸다			//데이터를 전솔할 때는 출력 스트림을 이용한다.
				pw.println("서버>> " + s); //ln을 해야 정보가 넘어간다.
				textArea.append("보냄>> " + s + "\n");
				textField.setText("");
				textField.requestFocus();
				
			}catch(Exception e2){
				textArea.append("클라이언트가 접속을 해제함");
			}
	}


}
