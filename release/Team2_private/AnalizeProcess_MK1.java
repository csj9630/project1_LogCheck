package Team2_private;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 폐기된 파일.
 * 
 * @author tmdwn
 *		
 *         로그 예시
 *         [200][http://sist.co.kr/find/video?key=java&query=sist][ie][2025-09-12
 *         09:13:17]
 */
public class AnalizeProcess_MK1 {

	public AnalizeProcess_MK1() {
		super();
//		useNIO();
		useNIOwithReguExMap();
//		 regTest();
	}// AnalizeProcess

	public void useNIO() {
		File file = new File("C:/temp/sist_input_1.log");
		// 파일이 존재하는지 전처리

		// 1.파일과 연결
		Path path = Path.of(file.getAbsolutePath());// JDK11부터 PAth만으로 가능.
		System.out.println(path.getFileName());

		Map<String, Integer> map = new HashMap<>();

		String temp = "";

		String key = "key=";
		String errCode = "[500]";
		String invalid = "[403]";

		// 2.파일과 NIO를 사용하여 모든 행을 읽어 들인다.
		try {
			List<String> list = Files.readAllLines(path);

			// 3.List의 모든 방 반복
			for (String line : list) {

				// "key="가 있으면 & 직전까지 뽑아내서 temp에 할당
				// 사용 키의 이름과 횟수.
				if (line.contains(key)) {

					temp = line.substring(line.indexOf(key) + key.length(), line.indexOf("&"));

					// map에 key:temp, value에 temp키의 값+1을 넣는다(값이 없으면 DefaultValue로 0을 넣는다.)
					map.put(temp, map.getOrDefault(temp, 0) + 1);

				} // end if

				// books요청에 대한 에러(500)가 발생한 횟수.
				if (line.contains("books") && line.contains(errCode)) {
					map.put(errCode, map.getOrDefault(errCode, 0) + 1);
				} // end if

				// 비정상적인 요청(403)이 발생한 횟수.
				if (line.contains(invalid)) {
					map.put(invalid, map.getOrDefault(invalid, 0) + 1);
				} // end if

//				temp = line.replaceAll("[\\[\\]]", ",");
			} // end for

			System.out.println("books요청에 대한 에러(500)가 발생한 횟수 : " + map.get(errCode));
			System.out.println("비정상적인 요청(403)이 발생한 횟수 : " + map.get(invalid));

			// 뽑아낸 키 값 전부 출력
			System.out.println("-----------------------");
			map.forEach((k, v) -> System.out.println(k + " : " + v));
		} catch (IOException ie) {
			// TODO Auto-generated catch block
			ie.printStackTrace();
		}

	}// useNIO

	/**
	 * 
	 * @author 최승준
	 * 
	 * useNIO 기반으로 수정
	 * NIO로 텍스트파일을 읽어내고
	 * 위의 코드에 정규식과 Map을 도입.
	 * 
	 * 정규식으로 대괄호 안의 내용을 추출
	 * 상태, URL, 사이트, 접속시간 별로 Map을 만들어서
	 * put(대괄호에서 추출한 내용, 횟수)로 각 map에 삽입.
	 * 
	 * 이후 키 값을 전부 출력함.
	 */
	public void useNIOwithReguExMap() {
		//임시로 로그 20개만 넣은 임의파일 주소
		File file = new File("C:/temp/sist_input_3.log");

		
		// 1.파일과 연결
		Path path = Path.of(file.getAbsolutePath());// JDK11부터 PAth만으로 가능.
		System.out.println(path.getFileName());

		//각 자리별 map 분비
		Map<String, Integer> mapStatus = new HashMap<>();
		Map<String, Integer> mapURL = new HashMap<>();
		Map<String, Integer> mapBrowser = new HashMap<>();
		Map<String, Integer> mapTime = new HashMap<>();
		
		//정규식으로 대괄호 구분.
		Pattern p = Pattern.compile("\\[(.*?)]");
		Matcher m;

		int idx = 0; //어떤 map에 넣을지 switch 구문용
		String mline = ""; //읽어낸 문장에 정규식 적용 후 임시 저장.

		// 2.파일과 NIO를 사용하여 모든 행을 읽어 들인다.
		try {
			List<String> list = Files.readAllLines(path);

			// 3.List의 모든 방 반복
			for (String line : list) {

				// Line 한 줄에 대해서 대괄호 패턴 매칭
				m = p.matcher(line);
				idx = 0; //idx 초기화
				
				//한 줄 내의 모든 패턴 찾기
				while (m.find()) {
					mline = m.group(1);//대괄호 안의 내용만 추출

					//switch으로 대괄호 순서대로 각각 map에 넣기.
					//map에 key:temp, value에 temp키의 값+1을 넣는다(값이 없으면 DefaultValue로 0을 넣는다.)
					switch (idx) {
					case 0:
						mapStatus.put(mline, mapStatus.getOrDefault(mline, 0) + 1);
						break;
					case 1:
						mapURL.put(mline, mapURL.getOrDefault(mline, 0) + 1);
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

			//book요청은 미완.
			//System.out.println("books요청에 대한 에러(500)가 발생한 횟수 : "+ mapStatus.get("500"));
			
			System.out.println("비정상적인 요청(403)이 발생한 횟수 : "+ mapStatus.get("403"));
			
			// 뽑아낸 키 값 전부 출력
			System.out.println("-----------------------");
			mapStatus.forEach((k, v) -> System.out.println(k + " : " + v));
			System.out.println("-----------------------");
			mapURL.forEach((k, v) -> System.out.println(k + " : " + v));
			System.out.println("-----------------------");
			mapBrowser.forEach((k, v) -> System.out.println(k + " : " + v));
			System.out.println("-----------------------");
			mapTime.forEach((k, v) -> System.out.println(k + " : " + v));
			
		} catch (IOException ie) {
			// TODO Auto-generated catch block
			ie.printStackTrace();
		}//end catch

	}// useNIOwithReguExMap

	public void regTest() {
		String log = "[200][http://sist.co.kr/find/img?key=front&query=sist][ie][2025-09-12 09:13:20]";

		Map<String, Integer> map = new HashMap<>();

		Pattern p = Pattern.compile("\\[(.*?)]");
		Matcher m = p.matcher(log);

		while (m.find()) {
			System.out.println(m.group(1));
		}

	}// regTest

	public void analize() {

	}// analize
}// class
