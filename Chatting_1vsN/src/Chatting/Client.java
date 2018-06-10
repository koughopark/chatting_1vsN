package Chatting;

import java.awt.Component;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.ScrollPaneConstants;

public class Client  extends Frame implements ActionListener, Runnable {

	private JFrame frame;
	private final JScrollPane scrollPane = new JScrollPane();
	private JTextArea textArea;
	private JTextField textField;
	private JButton chat_button;
	
	private int port = 12345; //서버와의 연결 포트
	private String addr = "121.134.130.173"; // 서버의 아이피주소
	public String name = "Guest";
	
	private Socket socket;
	private JTextField id_textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Client chatClient = new Client();
		chatClient.frame.setVisible(true);
		chatClient.connect();
	}

	/**
	 * Create the application.
	 */
	public Client() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 452, 374);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		scrollPane.setBounds(0, 0, 434, 262);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea);
		
		textField = new JTextField();
		textField.setBounds(0, 305, 325, 30);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.addActionListener(this);
		
		chat_button = new JButton("입력");
		chat_button.setBounds(326, 305, 108, 28);
		chat_button.addActionListener(this);
		frame.getContentPane().add(chat_button);
		
		id_textField = new JTextField();
		id_textField.setBounds(198, 269, 127, 30);
		id_textField.setColumns(10);
		frame.getContentPane().add(id_textField);
		
		JButton id_button = new JButton("닉네임 입력");
		id_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				name = id_textField.getText().trim();
			}
		});
		id_button.setBounds(326, 269, 108, 28);
		frame.getContentPane().add(id_button);
		
		Client.this.frame.setTitle("손님");
	}
	
	public void connect() {			// 서버에 접속한다.
		try{
			socket = new Socket(addr,port); // 아이피, 포트번호
			textArea.setText("서버에 연결함...\n");
		
			Thread thred = new Thread(this);
			thred.start();
		
		}catch(Exception e){
			socket = null;
			textArea.setText(" 서버 연결중 오류 발생..\n");
		}
	}	
	
	@Override
	public void run() {
		String str;
		
		try{
			if(socket == null)
				return;		// 접속할 서버가 없으면
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));		//서버가 보낸정보를 읽어 들이는 입력 스트림 		//중요
			
			while((str = br.readLine()) != null){	//서버의 정보를 읽어 들인다.
				textArea.append(str + "\n");
			}
					
		}catch(Exception e){		//서버가 접속을 해제 한 경우
			textArea.append("서버가 연결을 해제함...\n");
			socket = null;
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
			String s = textField.getText().trim();
			if(s.length()==0)
				return;
		
			try{
				// 서버에 연결되지 않은 경우
				if(socket == null)
					return;
				
				//서버에 데이터 전송하기
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				pw.println(name + ">>" + s);
				textArea.append("보냄 : " + s + "\n");
				textField.setText("");
				textField.requestFocus();
				
			}catch(Exception e2){
				textArea.append("서버가 접속을 해제함");
			}
		}
}
