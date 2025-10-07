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
public class AnalizeProcess {

	private Map<String, Integer> mapResponseResult;
	private Map<String, Integer> mapBrowser;
	private Map<String, Integer> mapTime;
	private Map<String, Integer> mapKey;

	private List<String> fileList; // txt파일 저장할 리스트

	private AnalizeEvt evt; // Function 클래스와 hasA 관계

	// 기본 생성자
	public AnalizeProcess(AnalizeEvt evt) {
		// 각 자리별 map 세팅.
		mapResponseResult = new HashMap<>();
		mapBrowser = new HashMap<>();
		mapTime = new HashMap<>();
		mapKey = new HashMap<>();
		fileList = new ArrayList<>();

		// hasA관계
		this.evt = evt;

		loadLogs("파일경로");
	}// AnalizeProcess

	/**
	 * 파일 주소를 받아서 텍스트 파일을 입력 받고<br>
	 * 각 줄을 List에 저장 / 줄의 대괄호 데이터를 Map에 저장.<br>
	 * "key=" 값도 별도 map에 저장.<br>
	 * 
	 * @param filePath 파일경로
	 */
	public void loadLogs(String filePath) {

		filePath = "C:/dev/temp/sist_input_1.log"; // 나중에 버튼으로 구현 예정.
		File file = new File(filePath);

		// 0.파일 존재를 확인
		if (!file.exists()) {
			System.out.println(file + " 파일의 경로를 확인하세요.");
			return;
		} // end if

		// 1.파일과 연결
		Path path = Path.of(file.getAbsolutePath());// JDK11부터 PAth만으로 가능.

		// 2.파일과 NIO를 사용하여 모든 행을 읽어 들인다.
		try {
			fileList = Files.readAllLines(path);
			sortMap();

		} catch (IOException ie) {
			ie.printStackTrace();
		} // end catch

	}// loadTxtFile

	// file에서 불러온 값들을 Map으로 분류하기
	public void sortMap() {
		// 정규식으로 대괄호 구분.
		Pattern bracketPattern = Pattern.compile("\\[(.*?)]");
		Pattern keyPattern = Pattern.compile("(?<=key=)[^&]+(?=&)");

		Matcher bracketMatcher;// 대괄호 구분용 matcher
		Matcher keyMatcher;// 최다 사용 key값에 쓸 matcher

		int idx; // 어떤 map에 넣을지 switch용
		String mline; // 읽어낸 문장에 정규식 적용

		// 3.List의 모든 방 반복
		for (String line : fileList) {

			// Line 한 줄에 대해서 대괄호 패턴 매칭
			bracketMatcher = bracketPattern.matcher(line);
			idx = 0; // idx 초기화

			// 한 줄 내의 모든 패턴 찾기
			while (bracketMatcher.find()) {
				mline = bracketMatcher.group(1); // 대괄호 안 내용 추출

				// switch으로 대괄호 순서대로 각각 map에 넣기.
				// map에 key:temp, value에 temp키의 값+1을 넣는다(값이 없으면 DefaultValue로 0을 넣는다.)
				switch (idx) {
				
				case 0: // 첫 번째 대괄호 (상태)
					mapResponseResult.put(mline, mapResponseResult.getOrDefault(mline, 0) + 1);
					break;
					
				case 1: // 두 번째 대괄호 (URL)
					//URL은 출력 안하도록하게 하기 위해
					//mapBrowser에 추가하지 않고 URL 안에서 key 값만 찾아서 mapKey에 추가.
					keyMatcher = keyPattern.matcher(mline);
					if (keyMatcher.find()) {
						mapKey.put(keyMatcher.group(), mapKey.getOrDefault(keyMatcher.group(), 0) + 1);
					}//end if
					break;
					
				case 2: // 세 번째 대괄호 (브라우저)
					mapBrowser.put(mline, mapBrowser.getOrDefault(mline, 0) + 1);
					break;
					
				case 3: // 네 번째 대괄호 (시간)
					mapTime.put(mline, mapTime.getOrDefault(mline, 0) + 1);
					break;
				}//end switch
				
				idx++;
				
			}//end while
		}//end for
	}//sortMap

	
	
	// AnalizeEvt에서 가져올 Getter들
	public Map<String, Integer> getMapResponseResult() {
		return mapResponseResult;
	}//getMapResponseResult

	public Map<String, Integer> getMapBrowser() {
		return mapBrowser;
	}//getMapBrowser

	public Map<String, Integer> getMapKey() {
		return mapKey;
	}//getMapKey

	public Map<String, Integer> getMapTime() {
		return mapTime;
	}//getMapTime

	public List<String> getFileList() {
		return fileList;
	}//getFileList

	// 임시 메인메소드 (로그인 구현 후 연결하고 삭제 예정)
	public static void main(String[] args) {
		new AnalizeView();
	}// main
}// class