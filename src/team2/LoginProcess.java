package team2;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginProcess {
	
	private LoginView lv;
//	private AnalizeView av;
	
	private static Map<String, String> mapLoginData;
	private String id;
	private String pass;
	private JTextField jtfId;
	private JPasswordField jpfPassword;
	private boolean reportFlag;
	
	public LoginProcess(LoginView lv) {
		this.lv = lv;
		mapLoginData = new HashMap<String, String>();
		mapLoginData.put("admin", "1234");
		mapLoginData.put("root", "1111");
		mapLoginData.put("administrator", "12345");
	}	// LoginProcess
	
	public void chkNull() {
		jtfId = lv.getJtfId();
		jpfPassword = lv.getJpfPassword();
		
		id = jtfId.getText().trim();
		pass = new String(jpfPassword.getPassword()).trim();
		
		// 아이디 입력칸이 빈칸일 경우
		if("".equals(id)) {
			jtfId.requestFocus();
			return;
		} // end if
		
		// 비밀번호 입력칸이 빈칸일 경우
		if("".equals(pass)) {
			jpfPassword.requestFocus();
			return;
		} // end if
		
	} // chkNull
	
	public void login(String id, String pass) {
		boolean idFlag = false;
		boolean pwFlag = false;
		reportFlag = false;
		jtfId = lv.getJtfId();
		
		// 아이디가 Map에 key로 존재하는 경우
		
		if(!mapLoginData.containsKey(id) || !mapLoginData.get(id).equals(pass)) {
			// 아이디와 비밀번호 입력되었지만 Map의 데이터쌍과 일치하지 않을 때
			JOptionPane.showMessageDialog(lv, "로그인 실패.\n아이디와 비밀번호를 확인하세요.", "로그인 실패", 0);
			jtfId.requestFocus();
		} else {
			// 아이디에 해당하는 비밀번호가 해당 아이디를 key로 하는 value로 Map에 존재할 경우
			if(mapLoginData.containsKey(id)) {
				idFlag = !idFlag;
			} // end if
			// 아이디와 해당 아이디의 비밀번호가 가 한 쌍으로 Map에 존재할 경우
			if(mapLoginData.get(id).equals(pass)) {
				pwFlag = !pwFlag;
			} // end if
		} // end if
		
		if(idFlag && pwFlag) {
			
			switch(id) {
			// 아이디가 admin일 경우
			case AccAuthority.ADMIN:
				JOptionPane.showMessageDialog(lv, "로그인 성공.\n로그 분석 프로그램을 실행합니다.", "로그인 성공", 1);
				reportFlag = !reportFlag;
				lv.dispose();
				new AnalizeView(this);
				break;
			// 아이디가 root일 경우
			case AccAuthority.ROOT:
				JOptionPane.showMessageDialog
				(lv, "로그인 성공.\n로그 분석 프로그램을 실행합니다.\n(주의: 해당 계정은 report 파일 작성 권한이 없습니다.)", "로그인 성공", 1);
				lv.dispose();
				new AnalizeView(this);
				break;
			// 아이가 administrator일 경우
			case AccAuthority.ADMINISTRATOR:
				JOptionPane.showMessageDialog(lv, "로그인 성공.\n로그 분석 프로그램을 실행합니다.", "로그인 성공", 1);
				reportFlag = !reportFlag;
				lv.dispose();
				new AnalizeView(this);
			}	// end switch
		} //end if
		
		
	} // login
	
	public String getId() {
		return id;
	} // getId
	
	public String getPass() {
		return pass;
	} // getPass

	public boolean isReportFlag() {
		return reportFlag;
	}
	
} // class
