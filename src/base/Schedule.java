package base;

import java.util.Calendar;

public class Schedule implements Comparable<Schedule>{
	
	
	protected static final int NumofData = 7;


	public static final int ONCE = 0;
	public static final int DAY = 1;
	public static final int WEEK = 2;
	public static final int MONTH = 3;
	public static final int YEAR = 4;
	
	protected Date StartDate = new Date();
	protected Date EndDate = new Date();
	

	protected String Explanation;
	protected String Place;
	
	protected int Color;
	protected int Repeat = 0;
	protected boolean FullTime = false;
	

	int viewno = -1;
	
	
	Schedule(){
		
	}
	
	
	void setStartDate(Date StartDate) {
		this.StartDate = StartDate;
	}
	void setEndDate(Date EndDate) {
		this.EndDate = EndDate;
	}
	
	void setExplanation(String Explanation) {
		this.Explanation = Explanation;
	}
	void setPlace(String Place) {
		this.Place = Place;
	}
	void setColor(int color) {
		Color = color;
	}
	void setFull(boolean b) {
		FullTime = b;
	}
	void setRepeat(int i) {
		Repeat = i;
	}
	
	boolean getFull() {
		return FullTime;
	}

	Date getStartDate() {
		return StartDate;
	}
	Date getEndDate() {
		return EndDate;
	}

	String getExplanation() {
		return Explanation;
	}
	String getPlace() {
		return Place;
	}
	int getColor() {
		return Color;
	}
	int getRepeat() {
		return Repeat;
	}
	
	int getDateDiffer() {
		int d = EndDate.get(Calendar.DAY_OF_YEAR) - StartDate.get(Calendar.DAY_OF_YEAR);
		
		d += (EndDate.get(EndDate.YEAR) - StartDate.get(StartDate.YEAR)) * 365;
		return d;
	}
	
	
	void Import(String[] data) {
		if(data.length != NumofData) {
			return;
		}

		StartDate.Import(data[0]);
		EndDate.Import(data[1]);;
		Explanation = data[2];
		Place = data[3];
		Color = Integer.parseInt(data[4]);
		
		
		Repeat = Integer.parseInt(data[5]);
		FullTime = Boolean.parseBoolean(data[6]);
		
		
	}
	
	String[] Export() {
		String[] data = new String[NumofData];
		data[0] = StartDate.Export();
		data[1] = EndDate.Export();
		data[2] = Explanation;
		data[3] = Place;
		data[4] = String.valueOf(Color);
		
		data[5] = String.valueOf(Repeat);
		data[6] = String.valueOf(FullTime);
		
		return data;
	}
	
	

	@Override
	public int compareTo(Schedule arg0) {
		if(this.getDateDiffer() < arg0.getDateDiffer()) {
			return 1;
		}
		else if(this.getDateDiffer() > arg0.getDateDiffer()) {
			return -1;
		}
		return 0;
	}
	
}

class OnceSchedule extends Schedule {
	
}

class RepeatSchedule extends Schedule {
	Date Starting;
	Date Ending;
	
	RepeatSchedule(){
		Starting = new Date();
		Starting.set(DataBase.StartLimit, 0, 1);
		Ending = new Date();
		Ending.set(DataBase.EndLimit, 11, 31);
	}

	
	void setStarting(Date c) {
		Starting = c;
	}
	void setEnding(Date c) {
		Ending = c;
	}
	
	Date getStarting() {
		return Starting;
	}
	Date getEnding() {
		return Ending;
	}
	
	void setRepeat(int no) {
		Repeat = no;
		if(no < 1 || no > 4) {
			Repeat = 1;
		}
	}
	int getRepeat() {
		return Repeat;
	}
	
	
	@Override
	void Import(String[] data) {
		if(data.length != NumofData + 2) {
			return;
		}
		StartDate.Import(data[0]);;
		EndDate.Import(data[1]);;
		Explanation = data[2];
		Place = data[3];
		Color = Integer.parseInt(data[4]);
		
		Repeat = Integer.parseInt(data[5]);
		
		Starting.Import(data[6]);
		Ending.Import(data[7]);
		if(StartDate.getch()) {
			Starting.ConverttoChineseCalendar();
			Ending.ConverttoChineseCalendar();
		}
	}
	
	@Override
	String[] Export() {
		String[] data = new String[NumofData + 2];
		
		data[0] = StartDate.Export();
		data[1] = EndDate.Export();
		data[2] = Explanation;
		data[3] = Place;
		data[4] = String.valueOf(Color);
		
		data[5] = String.valueOf(Repeat);
		
		data[6] = Starting.Export();
		data[7] = Ending.Export();
		return data;
	}
}

class BasisSchedule extends RepeatSchedule{
	boolean holiday;
	
	BasisSchedule(){

		Repeat = YEAR;
		Color = -1;
		FullTime = true;
		Starting = new Date();
		Starting.set(DataBase.StartLimit, 0, 1);
		Ending = new Date();
		Ending.set(DataBase.EndLimit, 11, 31);
	}
	
	@Override
	void Import(String[] data) {
		StartDate.Import(data[0]);
		EndDate.Import(data[1]);;
		Explanation = data[2];
		holiday = Boolean.valueOf(data[3]);
		if(StartDate.getch()) {
			Starting.ConverttoChineseCalendar();
			Ending.ConverttoChineseCalendar();
		}
	}
	
	@Override
	String[] Export() {
		String[] data = new String[4];
		
		data[0] = StartDate.Export();
		data[1] = EndDate.Export();
		data[2] = Explanation;
		data[3] = String.valueOf(holiday);
		
		return data;
	}
	
	
}
