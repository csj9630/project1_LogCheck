package team2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class AnalizeView extends JFrame {

	private JTextField jtf7First, jtf7Last;
	private JButton jb7Process;
	private JTextArea jtaResult;
	private JButton jbtnDialog;
	private JButton jbtnReport;
	private LoginProcess lp;
	private JRadioButton jrbtnLog1;
	private JRadioButton jrbtnLog2;

	public AnalizeView(LoginProcess lp) {
		super("2조");

		JPanel jpNorth = new JPanel(new FlowLayout(FlowLayout.LEFT)); //7번작업 입력패널
		JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));	//결과출력 패널
		
		//분석 결과 jta
		jtaResult = new JTextArea(30, 70);
		jtaResult.setEditable(false); //수정 못하도록.
		JScrollPane jspCenter = new JScrollPane(jtaResult); //분석 결과창
		JLabel jlblExportTitle = new JLabel("결과 내보내기");
		jbtnDialog = new JButton("메시지 창으로 보기");
		jbtnReport = new JButton("파일로 내보내기");

		//7번작업 입력 창
		
		jpNorth.setBorder(new TitledBorder("7. 분석하고 싶은 라인을 적으세요"));
		jrbtnLog1 = new JRadioButton("Sist_Log1");
		jrbtnLog2 = new JRadioButton("Sist_Log2");
		jtf7First = new JTextField(8);
		jtf7Last = new JTextField(8);
		jb7Process = new JButton("분석");
		jpNorth.add(jrbtnLog1);
		jpNorth.add(jrbtnLog2);
		jpNorth.add(new JLabel("시작 라인"));
		jpNorth.add(jtf7First);
		jpNorth.add(new JLabel("끝 라인"));
		jpNorth.add(jtf7Last);
		jpNorth.add(jb7Process);
		
		//분석 결과 jsp
		jspCenter.setBorder(new TitledBorder("분석 결과"));
		
		jpSouth.add(jbtnDialog);
		jpSouth.add(jbtnReport);
		
		ButtonGroup jrbtnGroup = new ButtonGroup();
		jrbtnGroup.add(jrbtnLog1);
		jrbtnGroup.add(jrbtnLog2);
		
		add(jpNorth, BorderLayout.NORTH);
		add(jspCenter, BorderLayout.CENTER);
		add(jpSouth, BorderLayout.SOUTH);
		
		this.lp = lp;
		AnalizeEvt ae = new AnalizeEvt(this, lp);
		jb7Process.addActionListener(ae);
		jbtnDialog.addActionListener(ae);
		jbtnReport.addActionListener(ae);
		
		//-----JRadioButton의 액션 리스너 추가.------
		jrbtnLog1.addActionListener(ae);
		jrbtnLog2.addActionListener(ae);
		
		
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}//AnalizeView

	
	
	// Evt에서 받을 getter들
	
	public JRadioButton getJrbtnLog1() {
		return jrbtnLog1;
	}//getJrbtnLog1
	
	public JRadioButton getJrbtnLog2() {
		return jrbtnLog2;
	}//getJrbtnLog2
	
	public JTextField jtf7FirstInput() {
		return jtf7First;
	}//jtf7FirstInput

	public JTextField jtf7LastInput() {
		return jtf7Last;
	}//jtf7LastInput

	public JButton jb7Process() {
		return jb7Process;
	}//jb7Process

	public JTextArea jtaResult() {
		return jtaResult;
	}//jtaResult

	public JButton getJbtnDialog() {
		return jbtnDialog;
	}	// getJbtnDialog

	public JButton getJbtnReport() {
		return jbtnReport;
	}	// getJbtnDialog
	
}//class