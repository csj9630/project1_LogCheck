package team2;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends JFrame {
	
	private JTextField jtfId;
	private JPasswordField jpfPassword;
	private JButton jbtnLogin;
	
	public LoginView() {
		// 윈도우 컴포넌트 제목
		super("로그 분석 시스템");
		
		// 로그인 창 요소 선언
		JLabel jlblTitle = new JLabel("로그 분석 시스템");
		jtfId = new JTextField("");
		jpfPassword = new JPasswordField("");
		jbtnLogin = new JButton("Login");
		JLabel jlblId = new JLabel("ID");
		JLabel jlblPassword = new JLabel("PW");
		
		
		// 아이디 및 비밀번호 입력 칸 폰트 설정
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		jtfId.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		jpfPassword.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		jbtnLogin.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		jlblId.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		jlblPassword.setFont(new Font("맑은 고딕", Font.BOLD, 20));
	
		
		// Label 배경 불투명화
		jlblTitle.setOpaque(false);
		jlblId.setOpaque(false);
		jlblPassword.setOpaque(false);
		
		// 각 요소 색상 지정
		jlblTitle.setBackground(new Color(0xFFFFFF));
		jlblId.setBackground(new Color(0xFFFFFF));
		jlblPassword.setBackground(new Color(0xFFFFFF));
		jbtnLogin.setBackground(new Color(0xC0C0C0C0));
		
		// 각 요소 크기 및 위치 수동 설정
		setLayout(null);
		
		jlblTitle.setBounds(150, 30, 300, 60);
		jtfId.setBounds(100, 120, 250, 50);
		jpfPassword.setBounds(100, 190, 250, 50);
		jbtnLogin.setBounds(380, 140, 120, 80);
		jlblId.setBounds(50, 120, 50, 50);
		jlblPassword.setBounds(50, 190, 50, 50);
		
		// 디자인 클래스와 이벤트 클래스를 has a 관계로 객체화
		LoginProcess lp = new LoginProcess(this);
		LoginEvent le = new LoginEvent(this, lp);
		
		// 키보드 이벤트 등록
		jtfId.addKeyListener(le);
		jpfPassword.addKeyListener(le);
		jbtnLogin.addActionListener(le);
		
		// 각 요소를 윈도우 컴포넌트에 추가
		add(jlblTitle);
		add(jtfId);
		add(jpfPassword);
		add(jbtnLogin);
		add(jlblId);
		add(jlblPassword);
		
		// 윈도우 이벤트 등록
		addWindowListener(le);
		
		setBounds(1020, 320, 580, 400);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}	// LoginForm
	
	public JTextField getJtfId() {
		return jtfId;
	}	// getJtfId
	
	public JPasswordField getJpfPassword() {
		return jpfPassword;
	}	// getJpfPassword
	
}	// class
