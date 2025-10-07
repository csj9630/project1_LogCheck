package Team2_private;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * - Txt파일을 N/IO로 입력 받고<br>
 * - 모든 줄을 List에 저장하고<br>
 * - 각 대괄호 별로 map에 저장한다.<br>
 * - List와 Map의 데이터를 HasA관계로 기능 클래스로 넘긴다.<br>
 */
public class AnalizeProcessMK2 {


	private Map<String, Integer> mapStatus;
	private Map<String, Integer> mapURL;
	private Map<String, Integer> mapBrowser;
	private Map<String, Integer> mapTime;
	private Map<String, Integer> mapKey;
	
	private List<String> fileList; // txt파일 저장할 리스트

	
	private AnalizeFunction af;// Function 클래스와 hasA 관계

	public AnalizeProcessMK2(AnalizeFunction af) {
		// 각 자리별 map 세팅.
		mapStatus = new HashMap<>();
		mapURL = new HashMap<>();
		
		mapBrowser = new HashMap<>();
		mapTime = new HashMap<>();
		mapKey = new HashMap<>();
		
		fileList = new ArrayList<String>();

		
		// hasA관계
		this.af = af;

		loadTxtFile("파일경로");
	}// AnalizeProcessMK2


	
	/**
	 * 파일 주소를 받아서 텍스트 파일을 입력 받고<br>
	 * 각 줄을 List에 저장 / 줄의 대괄호 데이터를 Map에 저장.<br>
	 * "key=" 값도 별도 map에 저장.<br> 
	 * 
	 * @param filePath 파일경로
	 */
	public void loadTxtFile(String filePath) {

		filePath = "C:/dev/temp/sist_input_1.log";// 임시로 고정파일 주소 받음, 나중에 지울 것.
		File file = new File(filePath);
		
		// 0.파일 존재를 확인.
		if (!file.exists()) {
			System.out.println(file + " 파일의 경로를 확인하세요.");
			return;
		}//end if

		// 1.파일과 연결
		Path path = Path.of(file.getAbsolutePath());// JDK11부터 PAth만으로 가능.

		// 2.파일과 NIO를 사용하여 모든 행을 읽어 들인다.
		try {
			fileList = Files.readAllLines(path);
			sortMap();
			
		} catch (IOException ie) {
			// TODO Auto-generated catch block
			ie.printStackTrace();
		} // end catch
		
	}// loadTxtFile
	
	
	// file에서 불러온 값들을 Map으로 분류하기
	public void sortMap() {
		// 정규식으로 대괄호 구분.
		Pattern patternBrackets = Pattern.compile("\\[(.*?)]");//대괄호 안의 내용만 추출할 정규식
		Pattern patternKey = Pattern.compile("(?<=key=)[^&]+(?=&)"); // 최다 사용 key 값 문제에 사용할 정규식
		
		
		Matcher matcherBrackets;//대괄호 구분용 matcher
		Matcher matcherKey;// 최다 사용 key값에 쓸 matcher
		
		int idx = 0; // 어떤 map에 넣을지 switch 구문용
		String mline = ""; // 읽어낸 문장에 정규식 적용 후 임시 저장
		
		// 3.List의 모든 방 반복
		for (String line : fileList) {

			// Line 한 줄에 대해서 대괄호 패턴 매칭
			matcherBrackets = patternBrackets.matcher(line);
			idx = 0; // idx 초기화

			// 한 줄 내의 모든 패턴 찾기
			while (matcherBrackets.find()) {
				mline = matcherBrackets.group(1);// 대괄호 안의 내용만 추출

				// switch으로 대괄호 순서대로 각각 map에 넣기.
				// map에 key:temp, value에 temp키의 값+1을 넣는다(값이 없으면 DefaultValue로 0을 넣는다.)
				switch (idx) {
				case 0:
					mapStatus.put(mline, mapStatus.getOrDefault(mline, 0) + 1);
					break;
				case 1:
					mapURL.put(mline, mapURL.getOrDefault(mline, 0) + 1);
					
					//URL 내용에서 'key=' 구분해서 별도의 map에 카운트.
					matcherKey = patternKey.matcher(mline);
					if(matcherKey.find()) {
						mapKey.put(matcherKey.group(), mapKey.getOrDefault(matcherKey.group(), 0) + 1);
					}//end if
					break;
				case 2:
					mapBrowser.put(mline, mapBrowser.getOrDefault(mline, 0) + 1);
					break;
				case 3:
					mapTime.put(mline, mapTime.getOrDefault(mline, 0) + 1);
					break;
				}// end switch

				idx++;

			} // end while

		} // end for

	}//sortMap


	
//------------------Getter------------------------------------------------------	
	public Map<String, Integer> getMapStatus() {
		return mapStatus;
	}

	public Map<String, Integer> getMapURL() {
		return mapURL;
	}
	public Map<String, Integer> getMapKey() {
		return mapKey;
	}

	public Map<String, Integer> getMapBrowser() {
		return mapBrowser;
	}

	public Map<String, Integer> getMapTime() {
		return mapTime;
	}

	public List<String> getFileList() {
		return fileList;
	}

}
// class
