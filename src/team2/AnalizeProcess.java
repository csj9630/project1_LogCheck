package team2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
	
	//각 데이터별 Map <데이터명, 요청횟수>
	private Map<String, Integer> mapResponseResult;//상태
	private Map<String, Integer> mapBrowser;//브라우저
	private Map<String, Integer> mapTime;//접속시간
	private Map<String, Integer> mapKey;//key값
	private List<String> fileList; // txt파일 내용 저장할 리스트

	private AnalizeEvt evt; // AnalizeEvt 클래스와 hasA 관계
	private String logTitle; //로그 제목에 들어갈 String ( 파일명(sist_input_1) log ( 생성된 날짜 년-월-일 시간:분:초 ) )


	/**
	 * 생성자 매개변수(이벤트처리 클래스,String 파일주소)
	 * AnalizeEvt에서 받은 파일주소로 메서드 처리
	 * @param evt AnalizeEvt의 객체
	 * @param filePath 불러올 로그의 파일주소.
	 */
	public AnalizeProcess(AnalizeEvt evt, String filePath) {
		// 각 자리별 map 세팅.
		mapResponseResult = new HashMap<>();
		mapBrowser = new HashMap<>();
		mapTime = new HashMap<>();
		mapKey = new HashMap<>();
		fileList = new ArrayList<>();

		// hasA관계
		this.evt = evt;

		//파일 경로의 로그 파일 입력.
		loadLogs(filePath);
	}// AnalizeProcess
	
	/**
	 * 파일 주소를 받아서 텍스트 파일을 입력 받고<br>
	 * 각 줄을 List에 저장<br>
	 *  sortmap() : 각 라인에 대하여 데이터 처리 후 Map에 저장.<br>
	 * setLogTitle(file, path) : 로그제목을 인스턴스변수에 할당.<br>
	 * 
	 * @param filePath 파일경로
	 */
	public void loadLogs(String filePath) {

		File file = new File(filePath);

		// 0.파일 존재를 확인
		if (!file.exists()) {
			System.out.println(file + " 파일을 찾을 수 없습니다. 경로를 확인하세요.");
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
		
		//로그제목을 인스턴스변수에 할당.
		logTitle = setLogTitle(file, path);

	}// loadLogs

	
	/**
	 * file에서 불러온 값들을 Map으로 분류하기<br>
	 * 
	 * 정규식으로 대괄호 속 내용을 추출<br>
	 * 대괄호 순서대로 map에 <데이터명,요청횟수>로 삽입.<br>
	 */
	public void sortMap() {
		// 정규식으로 대괄호 구분.
		Pattern bracketPattern = Pattern.compile("\\[(.*?)]");//대괄호를 인식하는 정규식
		Pattern keyPattern = Pattern.compile("(?<=key=)[^&]+(?=&)");//"key== ~~ &"를 인식하는 정슈기

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

	
	/**
	 * 로그 제목 생성<br>
	 * 예) 파일명(sist_input_1) log ( 생성된 날짜 년-월-일 시간:분:초 )<br>
	 */
	public String setLogTitle(File file, Path path) {
		FileTime creationTime;
		String result = "";
		try {
			creationTime = (FileTime) Files.getAttribute(path, "creationTime");
			ZonedDateTime zdt = creationTime.toInstant().atZone(ZoneId.systemDefault());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd E요일 a hh:mm:ss", Locale.KOREAN);
			result = "파일명 : " + file.getName() + " 생성된 날짜 : " + zdt.format(formatter);
		} catch (IOException e) {
			e.printStackTrace();
		} // end catch
		
		return result;
	}// getLogTitle
	
	// -------AnalizeEvt에서 가져올 Getter Method--------------
	public String getLogTitle() {
		return logTitle;
	}//getLogTitle
	
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

	

}// class