package base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class FileStreamer {
	

	private static final String Directary = "data";
	private static final String basis_schedule = "BasisSchedule.data";
	private static final String Schedule = "Schedule.data";
	private static final String UserSetting = "UserSetting.data";
	
	private String path;
	private static final String Write_Seperator = "||";
	private static final String Read_Seperator = "\\|\\|";
	private final File file;
	
	
	FileStreamer(int value) {
		path = Directary + "\\";
		switch (value) {
	    case 1:
	    	path += basis_schedule;
	        break;
	        
	   case 2:
	        path += Schedule;
	        break;
	        
	   case 3:
	        path += UserSetting;
	        break;
	        
	        default:
	        	path = "not";
	            break;
		}
	    
		File dir = new File(Directary);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		file = new File(path);
		
	}
	
	public ArrayList<String[]> read(){
		ArrayList<String[]> Data = new ArrayList<String[]>();
	      
		
		if(!file.exists()) {
			return null;
		}
		try {
			FileReader filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			String line = "";
			while ((line = bufReader.readLine()) != null) {
				String[] d = line.split(Read_Seperator);
				Data.add(d);
	        }
			bufReader.close();
	    }
		catch(Exception e) {
			return null;
		}
		
		return Data;
	}

	public void write(ArrayList<String[]> data) {
		
	    try {
	        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
	        for (int i = 0; i < data.size(); i++) {
	        	String str = data.get(i)[0];
	        	for (int j =1; j < data.get(i).length ; j++) {
	        		str += Write_Seperator;
	        		str += data.get(i)[j] ;
	        	}
	        	bufferedWriter.write(str);
	        	bufferedWriter.newLine();
	        }
	        bufferedWriter.close();
	        
	    }
	    catch(Exception e) {
	        
	    }
	    return;
	}

}