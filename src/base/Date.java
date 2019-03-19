package base;

import java.util.Calendar;
import com.ibm.icu.util.ChineseCalendar;

public class Date {
	
	/*
	 * 	양력, 음력 동시 사용 가능
	 */
	
	public int YEAR = 1;
	public static final int SYEAR = Calendar.YEAR;
	public static final int LYEAR = ChineseCalendar.EXTENDED_YEAR;
	public static final int MONTH = Calendar.MONTH;
	public static final int WEEK = Calendar.DAY_OF_WEEK;
	public static final int DATE = Calendar.DATE;
	public static final int HOUR = Calendar.HOUR_OF_DAY;
	public static final int MINUTE = Calendar.MINUTE;
	private static final int EXYEAR = 2637;
	private static final String separator = "/";
	public static final int WEEK_OF_MONTH = Calendar.WEEK_OF_MONTH;
	
	private static final Calendar base_calendar = Calendar.getInstance();
	private static final ChineseCalendar base_chinesecalendar = new ChineseCalendar();
	
	private static Calendar getCalendar() {
		return (Calendar)base_calendar.clone();
	}
	private static ChineseCalendar getChineseCalendar() {
		return (ChineseCalendar)base_chinesecalendar.clone();
	}
	
	private boolean Chinese = false;
	private Calendar c;
	private ChineseCalendar cc;
	
	Date(){
		c = getCalendar();
		cc = getChineseCalendar();
	}
	private Date(Calendar c, ChineseCalendar cc, boolean Chinese){
		this.c = c;
		this.cc = cc;
		this.Chinese = Chinese;
		if(Chinese) {
			YEAR = LYEAR;
		}
	}
	
	public boolean getch() {	//음력이면 true
		return Chinese;
	}
	
	public int get(int field) {	//양력 음력에 맞는 날 리턴
		if(Chinese) {
			if(field == YEAR) {
				return cc.get(field) - EXYEAR;
			}
			return cc.get(field);
		}
		else {
			return c.get(field);
		}
		
	}
	
	public int getS(int field) {	//강제로 양력 날짜 리턴
		if(Chinese) {
			c.setTimeInMillis(cc.getTimeInMillis());
			if(YEAR == field) {
				field = SYEAR;
			}
			return c.get(field);
		}
		else {
			return get(field);
		}
	}
	
	public int getL(int field) {	//강제로 음력 날짜 리턴
		if(Chinese) {
			return get(field);
		}
		else {
			cc.setTimeInMillis(c.getTimeInMillis());
			if(YEAR == field) {
				return cc.get(LYEAR) - EXYEAR;
			}
			return cc.get(field);
		}
	}
	
	public int getActualMaximum(int field) {	//최대 가능 값 리턴
		if(Chinese) {
			return cc.getActualMaximum(field);
		}
		else {
			return c.getActualMaximum(field);
		}
	}
	public void add(int field, int value) {
		if(Chinese) {
			cc.add(field, value);
		}
		else {
			c.add(field, value);
		}
	}

	public void set(int field, int value) {
		if(Chinese) {
			if(field == YEAR) value += EXYEAR;
			cc.set(field, value);
		}
		else {
			c.set(field, value);
		}
	}
	
	public void set(int year, int month, int date) {
		if(Chinese) {
			year += EXYEAR;
			cc.set(LYEAR, year);
			cc.set(MONTH, month);
			cc.set(DATE, date);
		}
		else {
			c.set(year, month, date);
		}
	}
	public void set(int year, int month, int date, int hour, int minute) {
		if(Chinese) {
			year += EXYEAR;
			cc.set(LYEAR, year);
			cc.set(MONTH, month);
			cc.set(DATE, date);
			cc.set(HOUR, hour);
			cc.set(MINUTE, minute);
		}
		else {
			c.set(year, month, date, hour, minute);
		}
	}

	
	public Date clone() {	//복사된 객체 리턴
		return new Date((Calendar)c.clone(), (ChineseCalendar)cc.clone(), Chinese);
	}
	
	
	static boolean Dateafter(Date c1, Date c2) {	//크거나 같을때 true
		
		if(c1.getS(c1.YEAR) < c2.getS(c2.YEAR)) {
			return false;
		}
		else if(c1.getS(Date.MONTH) < c2.getS(Date.MONTH)) {
			return false;
		}
		else if(c1.getS(Date.DATE) < c2.getS(Date.DATE)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	static boolean Datebefore(Date c1, Date c2) {	//작거나 같을때 true
		
		if(c1.getS(c1.YEAR) > c2.getS(c2.YEAR)) {
			return false;
		}
		else if(c1.getS(Date.MONTH) > c2.getS(Date.MONTH)) {
			return false;
		}
		else if(c1.getS(Date.DATE) > c2.getS(Date.DATE)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	static boolean DateEqual(Date c1, Date c2) {	//같을때 true
		if(c1.getS(c1.YEAR) == c2.getS(c2.YEAR) 
				&& c1.getS(Date.MONTH) == c2.getS(Date.MONTH)
				&& c1.getS(Date.DATE) == c2.getS(Date.DATE)) {
			
			return true;
		}
		else {
			return false;
		}
	}
	
	public void ConverttoChineseCalendar() {	//음력으로 전환
		if(!Chinese) {
			Chinese = true;
			YEAR = ChineseCalendar.EXTENDED_YEAR;
	        
	        cc.setTimeInMillis(c.getTimeInMillis());
		}
	}
	public void ConverttoCalendar() {		//양력으로 전환
		if(Chinese) {
			Chinese = false;
			YEAR = Calendar.YEAR;
			
	        c.setTimeInMillis(cc.getTimeInMillis());
		}
	}

	public String Export() {	//데이터 저장용

		int[] data = new int[5];
		data[0] = get(YEAR);
		data[1] = get(MONTH) + 1;
		data[2] = get(DATE);
		data[3] = get(HOUR);
		data[4] = get(MINUTE);
		
		String str = String.valueOf(Chinese);
		for(int i = 0 ; i < 5; i++) {
			str += separator + data[i];
		}

		return str;
	}
	
	public void Import(String str) {	//데이터 적용
		
		String[] data = str.split(separator);
		
		Chinese = Boolean.valueOf(data[0]);
		if(Chinese) {
			YEAR = ChineseCalendar.EXTENDED_YEAR;
		}
		
		int[] dataint = new int[data.length - 1];
		for(int i = 0 ; i< dataint.length; i++) {
			dataint[i] = Integer.parseInt(data[i + 1]);
		}
		
		set(dataint[0], dataint[1] - 1, dataint[2], dataint[3], dataint[4]);
	}

	
}
