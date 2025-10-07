package team2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ReportView extends JFrame{
	
	private File folder;
	private File file;
	private LoginProcess lp;
	private AnalizeEvt ae;
	private AnalizeView av;
	private boolean reportFlag;
	
	public ReportView(AnalizeEvt ae) {
		this.ae=ae;
	}//ReportView
	
	/**
	 * 2번을 누르면 폴더와 파일을 생성한 후 1~6번 작업을 모두 출력하는 method
	 */
	public void createFolderAndFile(){ 
		folder=new File("c:/dev/report"); //c:/dev/report 폴더 생성을 위한 경로 설정
		folder.mkdirs(); //폴더 생성
		long currentTime = System.currentTimeMillis(); //파일명에 들어갈 생성날짜 선언	
		String path = "report_"+currentTime+".dat"; //파일명에 들어갈 이름 선언
		
		file=new File(folder.getAbsolutePath() + folder.separator + path); //위에 생성한 폴더에서 report_생성날짜.dat 파일 생성을 위한 경로 설정
		//파일 생성
		try(FileWriter fw=new FileWriter(file)) { //파일 생성을 위한 FileWriter의 객체 fw 생성
			fw.write(ae.analizeResult()); //파일에 1~6작업 작성
			fw.flush(); //파일에 내용 저장
		} catch(IOException ie) {
			ie.printStackTrace();
		}//end catch
		
		//파일 읽기
		try(BufferedReader br=new BufferedReader(new FileReader(file))) {
			StringBuilder sb=new StringBuilder();
			String line; //파일에 저장된 1~6작업을 읽기 위해 변수 설정
			while((line=br.readLine()) != null) { //파일에 작업 내용이 있으면
				sb.append(line); // StringBuilder 객체에 저장	
			}
		} catch(IOException ie) {
			ie.printStackTrace();
		}//end catch
	}//createFolderAndFile
	
	public void reportAuthority(AnalizeView av, LoginProcess lp) {
		this.lp=lp;
		reportFlag = lp.isReportFlag();
		String reportFlagStr = String.valueOf(reportFlag);
		
		switch(reportFlagStr) {
		case "true": createFolderAndFile(); break;
		case "false": JOptionPane.showMessageDialog(av, "문서를 생성할 수 있는 권한이 없습니다.", "파일 생성 실패", 0);
		}
		
	}


}//class