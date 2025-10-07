package Team2_private;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class AnalizeView extends JFrame {

	private JTextField jtf7First, jtf7Last;
	private JButton jb7Process;
	private JTextArea jtaResult;

	public AnalizeView() {
		super("2조");

		JPanel jpNorth = new JPanel(new FlowLayout(FlowLayout.LEFT)); //7번작업 입력패널
		
		//분석 결과 jta
		jtaResult = new JTextArea(30, 70);
		jtaResult.setEditable(false); //수정 못하도록.
		JScrollPane jspCenter = new JScrollPane(jtaResult); //분석 결과창

		//7번작업 입력 창
		jpNorth.setBorder(new TitledBorder("7. 분석하고 싶은 라인을 적으세요"));
		jtf7First = new JTextField(8);
		jtf7Last = new JTextField(8);
		jb7Process = new JButton("분석");
		jpNorth.add(new JLabel("시작 라인"));
		jpNorth.add(jtf7First);
		jpNorth.add(new JLabel("끝 라인"));
		jpNorth.add(jtf7Last);
		jpNorth.add(jb7Process);

		//분석 결과 jsp
		jspCenter.setBorder(new TitledBorder("분석 결과"));
		
		
		add(jpNorth, BorderLayout.NORTH);
		add(jspCenter, BorderLayout.CENTER);

		AnalizeEvt ae = new AnalizeEvt(this);
		jb7Process.addActionListener(ae);

		
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}//AnalizeView

	
	
	// Evt에서 받을 getter들

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
	
}//class