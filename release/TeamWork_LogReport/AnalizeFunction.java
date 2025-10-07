package TeamWork_LogReport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AnalizeProcessMK2와 hasA 관계로 map,list 데이터를 받아와서<br>
 * 요구사항에 필요한 기능을 구현하는 클래스.<br>
 * map,list는 getter 메서드로 가져올 것.<br>
 */
public class AnalizeFunction {
	private AnalizeProcessMK2 apmk2;

	public AnalizeFunction() {
		apmk2 = new AnalizeProcessMK2(this);
//		printAllMap();
		System.out.println("-----------------------");
		showMost403();
		System.out.println("-----------------------");
		showMost500ofBooks();
		System.out.println("-----------------------");
		showMostRequsetTime();
		System.out.println("-----------------------");
	}// AnalizeFunction

	/**
	 * 테스트용 : 뽑아낸 키 값 전부 출력 URL은 생략.
	 */
	public void printAllMap() {
		System.out.println("-----------------------");
		apmk2.getMapStatus().forEach((k, v) -> System.out.println(k + " : " + v));
		System.out.println("-----------------------");
//		apmk2.getMapURL().forEach((k, v) -> System.out.println(k + " : " + v));
//		System.out.println("-----------------------");
		apmk2.getMapKey().forEach((k, v) -> System.out.println(k + " : " + v));
		System.out.println("-----------------------");
		apmk2.getMapBrowser().forEach((k, v) -> System.out.println(k + " : " + v));
		System.out.println("-----------------------");
		apmk2.getMapTime().forEach((k, v) -> System.out.println(k + " : " + v));
	}// printAllMap

//요구사항 메서드 구현할 파트.
//임시이름

	/**
	 * 요구사항1. 최대사용 키의 이름과 횟수. ( log파일의 모든 행 분석)
	 */
	public void showRequirements1() {

	}//

	/**
	 * 요구사항2. 브라우저별 접속횟수와 비율.
	 */
	public void showRequirements2() {

	}//

	/**
	 * 요구사항3. 서비스를 성공적으로 수행한 횟수(200)와 실패(404)횟수.
	 */
	public void showRequirements3() {

	}//

	/**
	 * 요구사항4. 요청이 가장 많은 시간.
	 * 2025-09-12 10:28:ora 같은 이상한 로그가 들어오면 예외처리 후 일단 카운트 안함.
	 */
	public void showMostRequsetTime() {

		Map<Integer, Integer> MostAccessTime = new HashMap<>();//요청시간, 요청횟수
		List<Integer> maxKeys = new ArrayList<>();//최대 요청횟수의 시간이 2개 이상일 경우 List로 출력.

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//시간 포맷

		//mapTime의 반복문
		apmk2.getMapTime().forEach((key, value) -> {
			try {
				//로그에 있는 시간을 시간 포맷에 맞춰서 넣음. 형식이 아닐 경우 catch로.
				LocalDateTime ldt = LocalDateTime.parse(key, dtf);
				
//				System.out.println(ldt);
				
				//뽑아낸 hour을 int에 저장.
				int hour = ldt.getHour(); 
				
				//map에 저장 (시간, 요청횟수)
				//getMapTime Map에는 value로 호출횟수가 들어있음. 기본값으로 그게 들어간다.
				MostAccessTime.put(hour, MostAccessTime.getOrDefault(hour, value) + 1);
			} catch (DateTimeParseException dtpe) {
				//로그에 정수가 아닌 게 들어올 경우 예외처리.
//				System.err.println("비정상 시간 형식 :" + dtpe);
			} // end catch
		});// end forEach

		//MostAccessTime Map에서 최대 value를 뽑아냄.
		int maxValue = Collections.max(MostAccessTime.values());

		//Map.Entry로 최대value(요청횟수)에 맞는 key(시간)을 찾아냄.
		//시간이 중복일 경우를 위해 list에 추가.
		for (Map.Entry<Integer, Integer> entry : MostAccessTime.entrySet()) {
		    if (entry.getValue() == maxValue) {
		        maxKeys.add(entry.getKey());
		    }//end if
		}//end for

		
		System.out.println("요청이 가장 많은 시간 : " + maxKeys+" 시");
		System.out.println("최대 요청횟수 : " + maxValue);
	}// showMostRequsetTime

	
	
	
	/**
	 * 요구사항5. 비정상적인 요청(403)이 발생한 횟수.
	 */
	public void showMost403() {
		int invaildCnt = apmk2.getMapStatus().get("403");
		System.out.println("비정상적인 요청(403)이 발생한 횟수: " + invaildCnt+" 회");
	}// showMostRequsetTime

	
	
	
	/**
	 * 요구사항6. books요청에 대한 에러(500)가 발생한 횟수. 두 개 조건에 대한 건 map으로 해결이 안되서 전체 list
	 * forEach로 읽고 조건 걸어서 카운트. 좀 더 효율적인 방법이 있을 것으로 생각함.
	 */
	public void showMost500ofBooks() {
		int errCnt = 0;
		for (String temp : apmk2.getFileList()) {
			if (temp.contains("books") && temp.contains("[500]")) {
				errCnt++;
			} // end if
		} // end for
		System.out.println("books요청에 대한 에러(500)가 발생한 횟수 " + errCnt+" 회");

	}// showMostRequsetTime

	/**
	 * 요구사항7. 입력라인에 대한 부분 데이터 정보출력.( log파일의 특정 행 분석)
	 */
	public void showRequirements7() {

	}//

	/**
	 * 요구사항8. Report 파일생성.
	 */
	public void showRequirements8() {

	}//

}// class
