package Team2_private;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 이벤트 클래스. 7번작업의 jta에 온전히 라인이 전부다 입력되지 않으면 모든 라인에 대한 정보 출력 7번 작업의 jta에 온전히 라인이
 * 입력되면 해당 라인의 정보만 출력
 */
public class AnalizeEvt implements ActionListener {

	private AnalizeView view; // View와 has-a
	private AnalizeProcess process; // Process와 has-a

	public AnalizeEvt(AnalizeView view) {
		this.view = view;

		// 생성 시 자동으로 파일 로딩되도록 객체 생성
		this.process = new AnalizeProcess(this);

		// 초기 분석 결과를 View의 JTextArea에 설정
		view.jtaResult().setText(AnalizeResult());
	}// AnalizeEvt

	@Override
	// 7번 작업의 사용자 입력에 대한 액션이벤트
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == view.jb7Process()) {
			String strFirst = view.jtf7FirstInput().getText();
			String strLast = view.jtf7LastInput().getText();

			// 기존에서 7번 부분만 교체하여 업데이트
			// 원본 데이터
			String rawResult = AnalizeResult();

			// 사용자 입력x
			String old7thResult = analizeLineRange(null, null);

			// 사용자가 입력한 범위에 대한 결과를 새로 만듦
			String new7thResult = analizeLineRange(strFirst, strLast);
			String updatedResult = rawResult.replace(old7thResult, new7thResult);

			view.jtaResult().setText(updatedResult);
		} // end if
	}// actionPerformed

	// 1~7번 작업들

	// 1번
	public String getKeyNameAcc() {
		Map<String, Integer> mapKey = process.getMapKey();
		if (mapKey.isEmpty())
			return "데이터 없음";

		int maxValue = 0; // 가장 큰 키의 벨류 값 저장
		int nowValue = 0; // 현재 키의 벨류 값 저장
		String maxKey = ""; // 가장 큰 키값 저장

		for (String key : mapKey.keySet()) {
			nowValue = mapKey.get(key); // 현재 키의 횟수
			if (nowValue > maxValue) {
				maxValue = nowValue;
				maxKey = key;
			} // end if
		} // end for
		return String.format("최다 사용 key : %s  | 사용 횟수 : %d회", maxKey, maxValue);
	}// getKeyNameAcc

	// 2번
	public String getBrowserPerc() {
		Map<String, Integer> mapBrowser = process.getMapBrowser();
		List<String> fileList = process.getFileList();
		if (mapBrowser.isEmpty())
			return "데이터 없음";

		StringBuilder sb = new StringBuilder();
		long total = fileList.size();

		// mapBrowser의 모든 키(브라우저 이름)를 하나씩 반복
		for (String browserName : mapBrowser.keySet()) {
			int count = mapBrowser.get(browserName); // 해당 브라우저의 접속 횟수
			double percentage = ((double) count / total) * 100;
			sb.append(String.format("   - %-10s: %d회 (%.2f%%)\n", browserName, count, percentage));
		} // end for
		return sb.toString();
	}// getBrowserPerc

	// 3번
	public String getSuccessFailCnt() {
		Map<String, Integer> mapResponseResult = process.getMapResponseResult();
		if (mapResponseResult.isEmpty())
			return "데이터 없음";

		int success = mapResponseResult.getOrDefault("200", 0);
		int fail = mapResponseResult.getOrDefault("404", 0);

		return String.format("접속 성공(200) : %d회 | 실패(404) : %d회", success, fail);
	}// getSuccessFailCnt

	// 4번
	/**
	 * 요구사항4. 요청이 가장 많은 시간.<br>
	 * Hour만 뽑아내서 카운트하기 위해 별도의 Map 생성.<br>
	 * 2025-09-12 10:28:ora 같은 이상한 로그가 들어오면 예외처리 후 일단 카운트 안함.<br>
	 * Map 읽는 반복문을 mapTime.keySet()으로 직관적으로 바꿈.
	 */
	public String getMostRequsetTime() {

		StringBuilder sb = new StringBuilder();

		Map<String, Integer> mapTime = process.getMapTime();//
		Map<Integer, Integer> mapHourCount = new HashMap<Integer, Integer>(); //시간, 요청횟수만 추출할 map
		List<Integer> maxKeys = new ArrayList<>();// 최대 요청횟수의 시간이 2개 이상일 경우 List로 출력.

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");// 시간 포맷
		LocalDateTime ldt; //시간값을 저장할 LDT

		// map 데이터 유무 확인
		if (mapTime.isEmpty())
			return "데이터 없음";

		
		//mapTime의 모든 키(요청시간) 반복
		//key값을 시간 포맷에 넣고, Hour만 추출하여 별도의 map인 mapHourCount에 저장.
		for (String key : mapTime.keySet()) {
			try {
				// 로그에 있는 시간을 시간 포맷에 맞춰서 넣음. 형식이 아닐 경우 catch로.
				ldt = LocalDateTime.parse(key, dtf);

				// 뽑아낸 hour을 int에 저장.
				int hour = ldt.getHour();

				// map에 저장 (시간, 요청횟수)
				// getMapTime Map에는 value로 호출횟수가 들어있음. 기본값으로 그게 들어간다.
				mapHourCount.put(hour, mapHourCount.getOrDefault(hour, mapTime.get(key)) + 1);
			
			
			} catch (DateTimeParseException dtpe) {
				// 로그에 정수가 아닌 게 들어올 경우 예외처리.
			} // end catch
		} // end for

		// mapHourCount Map에서 최대 value를 뽑아냄.
		int maxValue = Collections.max(mapHourCount.values());



		// Map mapHourCount을 반복하면서 최대value(요청횟수)에 맞는 key(시간)을 찾아냄.
		// 시간이 중복일 경우를 위해 list에 추가.
		// Map.Entry에서 keySet만 써서 직관적으로 바꿈.
		for (Integer key : mapHourCount.keySet()) {
//			System.out.println(key + " @@@ "+mapHourCount.get(key));
			if(mapHourCount.get(key)==maxValue) {
				maxKeys.add(key);
			}// end if
		} // end for
		
		sb.append("\n").append("최대 요청시간: ");
		for( Integer hour : maxKeys) {
			sb.append(hour).append("시 ");
		}//end for
		sb.append("\n").append("최대 요청횟수 : " + maxValue);

		return sb.toString();
	}// getMostRequsetTime

	// 5번
	/**
	 * 요구사항5. 비정상적인 요청(403)이 발생한 횟수.
	 */
	public String getMost403() {
		String result = "";

		// map이 비어있을 때 처리
		if (process.getMapResponseResult().isEmpty())
			return "데이터 없음";

		int invaildCnt = process.getMapResponseResult().get("403");

		result = "비정상적인 요청(403)이 발생한 횟수: " + invaildCnt + "회";
		return result;
	}// showMostRequsetTime

	// 6번
	/**
	 * 요구사항6. books요청에 대한 에러(500)가 발생한 횟수. 두 개 조건에 대한 건 map으로 해결이 안되서 전체 list
	 * forEach로 읽고 조건 걸어서 카운트. 좀 더 효율적인 방법이 있을 것으로 생각함.
	 */
	public String getMost500ofBooks() {
		String result = "";
		int errCnt = 0;
		// map이 비어있을 때 처리
		if (process.getFileList().isEmpty())
			return "데이터 없음";

		for (String temp : process.getFileList()) {
			if (temp.contains("books") && temp.contains("[500]")) {
				errCnt++;
			} // end if
		} // end for
		
		result = "books요청에 대한 에러(500)가 발생한 횟수 " + errCnt + " 회";
		return result;

	}// showMostRequsetTime

	// 7번
	public String analizeLineRange(String startLineStr, String endLineStr) {
		List<String> fileList = process.getFileList();
		boolean isEmpty = startLineStr == null || startLineStr.trim().isEmpty() || endLineStr == null
				|| endLineStr.trim().isEmpty();

		List<String> targetList = fileList;
		String title = "모든 라인에 대한 최다 사용 키:";

		if (!isEmpty) {
			try {
				int start = Integer.parseInt(startLineStr);
				int end = Integer.parseInt(endLineStr);
				if (start < 1 || end > fileList.size() || start > end)
					return "잘못된 라인 범위입니다.";
				targetList = fileList.subList(start - 1, end);
				title = String.format("%d ~ %d 라인에 대한 최다 사용 키:", start, end);
			} catch (NumberFormatException e) {
				return "라인 번호는 숫자만 입력해주세요.";
			} // end catch
		} // end if

		Map<String, Integer> keyCounts = new HashMap<>();
		Pattern keyPattern = Pattern.compile("key=([^&]*)");
		for (String line : targetList) {
			Matcher keyMatcher = keyPattern.matcher(line);
			if (keyMatcher.find()) {
				String key = keyMatcher.group(1);
				keyCounts.put(key, keyCounts.getOrDefault(key, 0) + 1);
			} // end if
		} // end for

		if (keyCounts.isEmpty())
			return title + "\n   해당 범위에 검색 키가 없습니다.";

		String maxKey = Collections.max(keyCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
		return String.format("%s\n   %s (%d회)", title, maxKey, keyCounts.get(maxKey));
	}// analizeLineRange

	// 분석결과 메소드
	public String AnalizeResult() {
		if (process.getFileList() == null || process.getFileList().isEmpty())
			return "로그 파일을 불러오지 못했습니다.";
		StringBuilder sb = new StringBuilder();

		// 1번
		sb.append("1. ").append(getKeyNameAcc()).append("\n");
		sb.append("------------------------------------------\n");

		// 2번
		sb.append("2. 브라우저별 접속 횟수 및 비율\n").append(getBrowserPerc());
		sb.append("------------------------------------------\n");

		// 3번
		sb.append("3. ").append(getSuccessFailCnt()).append("\n");
		sb.append("------------------------------------------\n");

		// 4번

		sb.append("4. ").append(getMostRequsetTime()).append("\n");
		sb.append("------------------------------------------\n");

		// 5번
		sb.append("5. ").append(getMost403()).append("\n");
		sb.append("------------------------------------------\n");

		// 6번
		sb.append("6. ").append(getMostRequsetTime()).append("\n");
		sb.append("------------------------------------------\n");

		// 7번
		sb.append("7. 특정 라인 분석 결과\n").append(analizeLineRange(null, null)).append("\n");
		return sb.toString();
	}// AnalizeResult

}// class