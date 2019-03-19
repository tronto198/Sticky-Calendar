package base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import base.DataBase.year.month.date;

public class DataBase {
	
	private DataBase() {
		UserSetting = new UserSetting();
		Image = new Image();
		ScheduleColor = new ScheduleColor();
		FSBasisSchedule = new FileStreamer(1);
		FSSchedule = new FileStreamer(2);
		FSUserSetting = new FileStreamer(3);
		
		
		UserSetting.Import();
		LoadBasisSchedule();
		LoadSchedule();

	}
	
	class Image{
		//이미지 저장
		private String Directary = "data\\";
		ImageIcon Xicon = new ImageIcon(Directary + "Xicon.png");
		ImageIcon Plusicon = new ImageIcon(Directary + "plus.png");
		ImageIcon Lefticon = new ImageIcon(Directary + "left.png");
		ImageIcon Righticon = new ImageIcon(Directary + "right.png");
		ImageIcon Trashicon = new ImageIcon(Directary + "trashicon.png");
		ImageIcon Checkicon = new ImageIcon(Directary + "ocheck.png");
		ImageIcon Backicon = new ImageIcon(Directary + "11back.png");
		ImageIcon Modifyicon = new ImageIcon(Directary + "modify2.png");
		ImageIcon Returnicon = new ImageIcon(Directary + "turn.png");
		ImageIcon Xicon2 = new ImageIcon(Directary + "Xicon3.png");
		ImageIcon Underlefticon = new ImageIcon(Directary + "under_left.png");
		ImageIcon Underrighticon = new ImageIcon(Directary + "under_right.png");
	}
	
	class ScheduleColor{
		//색상 저장
		class Colornode{
			//색상과 이름 저장
			final String name;
			final Color color;
			Colornode(String name, int R, int G, int B){
				this.name = name;
				this.color = new Color(R,G,B);
			}
		}
		final int no = 7;
		private final Colornode[] Colornodearr = new Colornode[no];
		private final Colornode BasisColor = new Colornode("Red", 255, 182, 193); 
		
		ScheduleColor() {
			MakeColorarr();
			
		}
		
		private void MakeColorarr() {
			Colornodearr[0] = new Colornode("Peach", 255, 218, 185);
			Colornodearr[1] = new Colornode("Pink", 255, 220, 233);
			Colornodearr[2] = new Colornode("LightGreen", 161, 227, 161);
			Colornodearr[3] = new Colornode("Lemon", 255, 250, 205);
			Colornodearr[4] = new Colornode("LightBlue", 173, 216, 230);
			Colornodearr[5] = new Colornode("Lavender", 230, 230, 250);
			Colornodearr[6] = new Colornode("Gray", 220, 220, 220);
		}
		
		Colornode get(int no) {
			if(no > this.no - 1) {
				no = 0;
			}
			else if(no == -1) {
				return BasisColor;
			}
			return Colornodearr[no];
		}
	}
	
 	class UserSetting{
		public final int X = 0;
		public final int Y = 1;
		public final int Width = 2;
		public final int Height = 3;
		public final int LOCATION = 0;
		public final int SIZE = 1;
		
		private int x, y;
		private int width = 400, height = 500;

		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		UserSetting(){
			
			x = screenSize.width - 450;
			y = 50;
		}
		
		void Set(int Option, int Value1, int Value2) {
			switch(Option) {
			case LOCATION:
				x = Value1;
				y = Value2;
				break;
			case SIZE:
				width = Value1;
				height = Value2;
				break;
			}
			
		}
		
		int get(int Option) {
			
			switch(Option) {
			case X:
				return x;
			case Y:
				return y;
			case Width:
				return width;
			case Height:
				return height;
				
				default:
					return 0;
			}
		}
		
		ArrayList<String[]> Export() {
			ArrayList<String[]> data = new ArrayList<String[]>();
			String[] Location = new String[2];
			Location[0] = String.valueOf(x);
			Location[1] = String.valueOf(y);
			data.add(Location);
			
			String[] Size = new String[2];
			Size[0] = String.valueOf(width);
			Size[1] = String.valueOf(height);
			data.add(Size);
			return data;
		}
		
		void save() {
			FSUserSetting.write(this.Export());
		}
		
		void Import() {
			ArrayList<String[]> data = FSUserSetting.read();
			
			try {
				x = Integer.parseInt(data.get(0)[0]);
				y = Integer.parseInt(data.get(0)[1]);
				
				width = Integer.parseInt(data.get(1)[0]);
				height = Integer.parseInt(data.get(1)[1]);
			}
			catch(Exception e) {
				return;
			}
		}
		
	}
	
	class year{
		final int value;
		private ArrayList<month> monthlist = new ArrayList<month>();
		
		year(int value){
			this.value = value;
		}
		
		void AddSchedule(int month, int date, ScheduleNode s) {
			int[] montharr = new int[monthlist.size()];
			for(int i = 0; i< montharr.length; i++) {
				montharr[i] = monthlist.get(i).value;
			}
			
			int i = forcesearch(montharr, month);
			
			
			if(i == montharr.length) {
				monthlist.add(new month(month));
			}
			else if(montharr[i] != month) {
				monthlist.add(i, new month(month));
			}
			
			
			monthlist.get(i).AddSchedule(date, s);
			
//			for(int i = 0 ; i< monthlist.size(); i++) {
//				if(month < monthlist.get(i).value) {
//					monthlist.add(i, new month(month));
//					monthlist.get(i).AddSchedule(date, s);
//					return;
//				}
//				else if(month == monthlist.get(i).value) {
//					monthlist.get(i).AddSchedule(date,s);
//					return;
//				}
//			}
//			
//			monthlist.add(new month(month));
//			monthlist.get(monthlist.size() - 1).AddSchedule(date, s);
			
			
		}
		void AddSchedule(int month, int date, BasisScheduleNode s) {
			int[] montharr = new int[monthlist.size()];
			for(int i = 0; i< montharr.length; i++) {
				montharr[i] = monthlist.get(i).value;
			}
			
			int i = forcesearch(montharr, month);
			
			if(i == montharr.length) {
				monthlist.add(new month(month));
			}
			else if(montharr[i] != month) {
				monthlist.add(i, new month(month));
			}
			
			monthlist.get(i).AddSchedule(date, s);
			
		}

		ArrayList<Schedule> find(int month, int date){
			int[] montharr = new int[monthlist.size()];
			for(int i = 0; i< montharr.length; i++) {
				montharr[i] = monthlist.get(i).value;
			}
			int i = search(montharr, month);

			if( i == -1)
				return null;
			else {
				return monthlist.get(i).find(date);
			}
		}
		
		
		class month{
			final int value;
			private ArrayList<date> datelist = new ArrayList<date>();
			
			month(int value){
				this.value = value;
			}
			
			void AddSchedule(int date, ScheduleNode s) {
				int[] datearr = new int[datelist.size()];
				for(int i = 0; i< datearr.length; i++) {
					datearr[i] = datelist.get(i).value;
				}
				
				int i = forcesearch(datearr, date);
				
				if(i == datearr.length) {
					datelist.add(new date(date, this));
				}
				else if(datearr[i] != date) {
					datelist.add(i, new date(date, this));
				}
				
				
				datelist.get(i).AddSchedule(s);
				
//				for(int i = 0 ; i< datelist.size(); i++) {
//					if(date < datelist.get(i).value) {
//
//						datelist.add(i, new date(date, this));
//						datelist.get(i).addSchedule(s);
//						break;
//					}
//					else if(date == datelist.get(i).value) {
//						datelist.get(i).addSchedule(s);
//						return;
//					}
//				}
//				
//				datelist.add(new date(date, this));
//				datelist.get(datelist.size() - 1).addSchedule(s);
			}
			
			void AddSchedule(int date, BasisScheduleNode s) {
				int[] datearr = new int[datelist.size()];
				for(int i = 0; i< datearr.length; i++) {
					datearr[i] = datelist.get(i).value;
				}
				
				int i = forcesearch(datearr, date);
				
				if(i == datearr.length) {
					datelist.add(new date(date, this));
				}
				else if(datearr[i] != date) {
					datelist.add(i, new date(date, this));
				}
				
				
				datelist.get(i).AddSchedule(s);
				
			}
			
			ArrayList<Schedule> find(int date){
				int[] datearr = new int[datelist.size()];
				for(int i = 0; i< datearr.length; i++) {
					datearr[i] = datelist.get(i).value;
				}
				int i = search(datearr, date);

				if( i == -1)
					return null;
				else {
					return datelist.get(i).getScheduleList();
				}
			}
			
			void delete(date date) {
				datelist.remove(date);
			}
			
			class date{
				final int value;
				final month parent;
				private ArrayList<Schedule> ScheduleList = new ArrayList<Schedule>();
				
				date(int value, month parent){
					this.value = value;
					this.parent = parent;
				}
				
				void AddSchedule(ScheduleNode s) {
					s.add(this);
					ScheduleList.add(s.getSchedule());
				}
				
				void AddSchedule(BasisScheduleNode s) {
					s.add(this);
					ScheduleList.add(s.getSchedule());
				}
				
				ArrayList<Schedule> getScheduleList(){
					return ScheduleList;
				}
				
				void delete(Schedule s) {
					ScheduleList.remove(s);
					if(ScheduleList.isEmpty()) {
						parent.delete(this);
					}
				}
			}
		}
	}
	
	class ScheduleNode{
		private final Schedule s;
		private ArrayList<year.month.date> datelist = new ArrayList<year.month.date>();
		
		ScheduleNode(Schedule s){
			this.s = s;
		}
		
		Schedule getSchedule() {
			return s;
		}
		
		void add(year.month.date d) {
			datelist.add(d);
		}
		
		void delete() {
			for(date i : datelist) {
				i.delete(s);
			}
		}
	}
	
	class BasisScheduleNode{
		
		private final BasisSchedule s;
		private ArrayList<year.month.date> datelist = new ArrayList<year.month.date>();
		
		
		BasisScheduleNode(BasisSchedule s){
			this.s = s;
		}
		
		BasisSchedule getSchedule() {
			return s;
		}
		
		void add(year.month.date d) {
			datelist.add(d);
		}
		
		void delete() {
			for(date i : datelist) {
				i.delete(s);
			}
		}
	}
	
	/*
	 *  프로그램에 필요한 모든 데이터 저장
	 *  
	 */
	
	
	private static DataBase DBinstance = null;
	private ArrayList<ScheduleNode> ScheduleNodeList = new ArrayList<ScheduleNode>();
	private ArrayList<BasisScheduleNode> BasisScheduleNodeList = new ArrayList<BasisScheduleNode>();
	private ArrayList<year> yearlist = new ArrayList<year>();
	
	public static final int StartLimit = 1990;		//반복 일정 최소년도
	public static final int EndLimit = 2030;		//반복 일정 최대년도

	private final Image Image;
	private final ScheduleColor ScheduleColor;
	private final UserSetting UserSetting;
	private final FileStreamer FSUserSetting;
	private final FileStreamer FSBasisSchedule;
	private final FileStreamer FSSchedule;
	
	
 	public static DataBase getInstance() {	//데이터베이스 하나로 유지
		if(DBinstance == null) {
			DBinstance = new DataBase();
		}
		return DBinstance;
	}	
	
 	UserSetting getUserSetting() {
 		return UserSetting;
 	}
 	ScheduleColor getColor() {
 		return ScheduleColor;
 	}
 	Image getImage() {
 		return Image;
 	}
 	
	void AddSchedule(Schedule s) {		//일정 추가
		
		ScheduleNode node = new ScheduleNode(s);
		ScheduleNodeList.add(node);
		
		if(s instanceof RepeatSchedule) {
			RepeatSchedule rs = (RepeatSchedule)s;
			int which;
			switch(((RepeatSchedule) s).getRepeat()) {
			case Schedule.DAY:
				which = Date.DATE;
				break;
				
			case Schedule.WEEK:
				which = Date.WEEK_OF_MONTH;
				break;
			
			case Schedule.MONTH:
				which = Date.MONTH;
				break;
			
			case Schedule.YEAR:
				which = s.getStartDate().YEAR;
				break;
				
				default:
					return;
			}
			
			Date f = rs.getStartDate().clone();
			Date e = rs.getEndDate().clone();
			
			while(Date.Dateafter(f, rs.getStarting())) {
				f.add(which, -1);
				e.add(which, -1);
			}
			f.add(which, 1);
			e.add(which, 1);
			
			Date End = (Date)((RepeatSchedule) s).getEnding().clone();
			
			while(Date.Datebefore(f, End)) {
				Date c = (Date)f.clone();
				
				while(Date.Datebefore(c, e)) {
					Add(c, node);
					c.add(Date.DATE, 1);
				}
				
				f.add(which, 1);
				e.add(which, 1);
			}
			
		}
		else {
			Date c = s.getStartDate().clone();
			
			while(Date.Datebefore(c, s.getEndDate())) {
				Add(c, node);
				c.add(Date.DATE, 1);
			}
			
		}
		
	}

	void AddBasisSchedule(BasisSchedule rs) {	//기본 일정 추가
		BasisScheduleNode node = new BasisScheduleNode(rs);
		BasisScheduleNodeList.add(node);
		
		int which = rs.getStartDate().YEAR;
		
		Date f = rs.getStartDate().clone();
		Date e = rs.getEndDate().clone();
		
		while(Date.Dateafter(f, rs.getStarting())) {
			f.add(which, -1);
			e.add(which, -1);
		}
		f.add(which, 1);
		e.add(which, 1);
		
		Date End = rs.getEnding().clone();
		
		while(Date.Datebefore(f, End)) {
			Date c = (Date)f.clone();
			
			while(Date.Datebefore(c, e)) {
				Add(c, node);
				c.add(Date.DATE, 1);
			}
			
			f.add(which, 1);
			e.add(which, 1);
		}
		
	}
	
	private void Add(Date c, ScheduleNode node) {

		int[] no = new int[yearlist.size()];
		for(int i = 0; i < no.length; i++) {
			no[i] = yearlist.get(i).value;
		}
		
		int i = forcesearch(no, c.getS(c.YEAR));
		
		if(i == no.length) {
			yearlist.add(i, new year(c.getS(c.YEAR)));
		}
		else if(no[i] != c.getS(c.YEAR)) {
			yearlist.add(new year(c.getS(c.YEAR)));
		}
		
		yearlist.get(i).AddSchedule(c.getS(Date.MONTH) + 1, c.getS(Date.DATE), node);

	}
	
	private void Add(Date c, BasisScheduleNode node) {
		int[] no = new int[yearlist.size()];
		for(int i = 0; i < no.length; i++) {
			no[i] = yearlist.get(i).value;
		}
		
		int i = forcesearch(no, c.getS(c.YEAR));
		
		if(i == no.length) {
			yearlist.add(i, new year(c.getS(c.YEAR)));
		}
		else if(no[i] != c.getS(c.YEAR)) {
			yearlist.add(new year(c.getS(c.YEAR)));
		}
		
		yearlist.get(i).AddSchedule(c.getS(Date.MONTH) + 1, c.getS(Date.DATE), node);
		
	}
	
	void RemoveSchedule(Schedule s) {		//일정 삭제
		for(ScheduleNode i : ScheduleNodeList) {
			if(i.s == s) {
				i.delete();
				ScheduleNodeList.remove(i);
				break;
			}
		}
		SaveSchedule();
	}
	
	public boolean SaveSchedule() {			//일정 저장
		try {
			ArrayList<String[]> data = new ArrayList<String[]>(); 
			for(ScheduleNode i : ScheduleNodeList) {
				data.add(i.s.Export());
			}
			FSSchedule.write(data);
			return true;
		}
		catch(Exception e) {
			
			return false;
		}
	}
	
	private void LoadBasisSchedule() {		//기본 일정 로드
		ArrayList<String[]> data = FSBasisSchedule.read();
		
		try {
			for(String[] i : data) {
				BasisSchedule s = new BasisSchedule();
				s.Import(i);
				AddBasisSchedule(s);
			}
		}
		catch(Exception e) {
			return;
		}
		
	}
	private void LoadSchedule() {			//일정 로드
		ArrayList<String[]> data = FSSchedule.read();

		try {
			for(String[] i : data) {
				Schedule s;
				if(i[5].equals(String.valueOf(Schedule.ONCE))) {
					s = new OnceSchedule();
				}
				else {
					s = new RepeatSchedule();
				}
				s.Import(i);
				AddSchedule(s);
			}
		}
		catch(Exception e) {
			return;
		}
	}


	ArrayList<Schedule> find(int year, int month, int date){	//일정리스트 찾기
		int[] yeararr = new int[yearlist.size()];
		for(int i = 0; i< yeararr.length; i++) {
			yeararr[i] = yearlist.get(i).value;
		}
		
		int i = search(yeararr, year);
		
		if( i == -1)
			return null;
		else {
			return yearlist.get(i).find(month, date);
		}
	}
	
	public int search(int[] arr, int target) {		// target과 같은 index 출력
        int first = 0;
        int last = arr.length - 1;
        int mid;

        while (first <= last) {
            mid = (first + last) / 2;
            if (target == arr[mid]) {
                return mid;
            }
            else {
            	if (target < arr[mid])
                    last = mid - 1;
                else
                    first = mid + 1;
            }
        }
        //없으면 -1 리턴
        return -1;
	}
	
	public int forcesearch(int[] arr, int target) {	// target보다 같거나 큰 index 출력
        int first = 0;
        int last = arr.length - 1;
        int mid;

        while (first <= last) {
            mid = (first + last) / 2;
            if (target == arr[mid]) {
                return mid;
            }
            else if(arr[last] < target ) {
            	return last + 1;
            }
            else if(target < arr[first] ) {
            	return first;
            }
            else {
                if (target < arr[mid])
                    last = mid - 1;
                else
                    first = mid + 1;
            }
        }
        return 0;
	}
	
}



