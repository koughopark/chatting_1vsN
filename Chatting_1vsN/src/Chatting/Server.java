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
	private JTextArea textArea; // ä�� ���
	private JTextField textField; // ä�� �Է�
	private JButton btnNewButton; // ���� ��ư
	
	private int port = 12345; //��Ʈ��ȣ
	
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
	
		btnNewButton = new JButton("�Է�");
		btnNewButton.addActionListener(this);				
		btnNewButton.setBounds(326, 272, 108, 28);
		frame.getContentPane().add(btnNewButton);
		
		Server.this.frame.setTitle("����");
	}
	
	public void serverStart(){
		try{
			serverSocket = new ServerSocket(port);	//�������� ����
			textArea.setText("���� ����....\n");
			
			socket = serverSocket.accept();		//Ŭ���̾�Ʈ ���� 		//���⼭ �ȵ�
		
			Thread thread = new Thread(this);		//Ŭ���̾�Ʈ�� �����ϸ� ������ ����
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
				return;		// ������ Ŭ���̾�Ʈ�� ������
						
			ip = socket.getInetAddress().getHostAddress();	// ������ Ŭ���̾�Ʈ�� IP�ּ� ���
			textArea.append(ip + " ���� ������ ������..\n");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));		//Ŭ���̾�Ʈ�� ���������� �о� ���̴� �Է� ��Ʈ�� 		//�߿�
			
			while((str = br.readLine()) != null){	//Ŭ���̾�Ʈ�� ������ �о� ���δ�.
				textArea.append(str + "\n");
			}
		}catch(Exception e){		//Ŭ���̾�Ʈ�� ������ ���� �� ���
			textArea.append(ip + " �� ������ �����Ͽ����ϴ�.");
			socket=null;
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
			String s=textField.getText().trim();
			if(s.length()==0)
				return;
			
			try{
				if(socket==null)		//Ŭ���̾�Ʈ�� ������ �ȵǸ�
					return;
				
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);			//������ ä�ù��ڿ��� Ŭ���̾�Ʈ���� ������			//�����͸� ������ ���� ��� ��Ʈ���� �̿��Ѵ�.
				pw.println("����>> " + s); //ln�� �ؾ� ������ �Ѿ��.
				textArea.append("����>> " + s + "\n");
				textField.setText("");
				textField.requestFocus();
				
			}catch(Exception e2){
				textArea.append("Ŭ���̾�Ʈ�� ������ ������");
			}
	}


}
