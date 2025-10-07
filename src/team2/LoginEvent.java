package team2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

public class LoginEvent extends WindowAdapter implements KeyListener, ActionListener {
	
	private LoginView lv;
	private LoginProcess lp;
	
	private String id;
	private String pass;
	
	public LoginEvent(LoginView lv, LoginProcess lp) {
		this.lv = lv;
		this.lp = lp;
	}	// LoginFormEvent
	
	@Override
	public void windowClosing(WindowEvent e) {
		
	}	// windowClosing
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}	// keyTyped
	
	@Override
	public void keyPressed(KeyEvent e) {
		
	}	// keyPressed
	
	@Override
	public void keyReleased(KeyEvent ke) {
		switch(ke.getKeyCode()) {
		case KeyEvent.VK_ENTER: lp.chkNull();
		}
		
	}	// keyReleased
	
	@Override
	public void actionPerformed(ActionEvent e) {
		id = lv.getJtfId().getText().trim();
		pass = new String((lv.getJpfPassword()).getPassword()).trim();
		
		// 아이디와 비밀번호가 있는 경우
		if(!"".equals(id) && !"".equals(pass)) {
			lp.login(id, pass);
		} else {
			JOptionPane.showMessageDialog(lv, "아이디와 비밀번호를 모두 입력해야 합니다.", "로그인 실패", 0);
		}
	}	// actionPerformed
	
}	// class