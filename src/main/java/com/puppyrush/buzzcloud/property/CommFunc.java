package com.puppyrush.buzzcloud.property;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.puppyrush.buzzcloud.entity.member.enums.enumMemberStandard;
import com.puppyrush.buzzcloud.mail.enumMailType;

public class CommFunc {

	public enum CalendarKind{
		DAY(Calendar.DATE),
		MINUTE(Calendar.MINUTE),
		HOUR(Calendar.HOUR);
		
		private int kind;
		
		CalendarKind(int k){
			kind = k;
		}
		
		int GetCalendarKind(){
			return kind;
		}
	}
	
	private CommFunc(){}
	
	public static boolean isPassingDateFromToday(Timestamp from, int dayCount, CalendarKind dateKind) throws SQLException{
				
		return isPassingDate(from,  new Timestamp(System.currentTimeMillis()),dayCount, dateKind);
		
	}
	
	
	public static boolean isPassingDate(Timestamp from, Timestamp to, int dayCount, CalendarKind dateKind) throws SQLException{
	
		Calendar cal = Calendar.getInstance ( );
		cal.setTime ( to );// 오늘로 설정. 
		 
		 
		Calendar cal2 = Calendar.getInstance ( );
		cal2.setTime(from);
	 
		 
		int count = 0;
		while ( !cal2.after ( cal ) )
		{
			count++;
			cal2.add ( dateKind.kind, 1 ); // 다음날로 바뀜					
		}
		
		if(dayCount > count)
			return false;
				
		return true;
					
	}

	public static String toAbsolutePathFromImage(int memberId, String imageName){
		
		return new StringBuilder(enumSystem.RESOURCE_FOLDER_ABS_PATH.toString())
				.append(enumSystem.MEMBERS_FOLDER_NAME.toString()).append("/")
				.append(String.valueOf(memberId)).append("/").append(imageName).toString();
		
		
	}
	
	public static String toRelativePathFromImage(int memberId, String imageName){
		
		return new StringBuilder(enumSystem.RESOURCE_FOLDER_RAT_PATH.toString())
				.append(enumSystem.MEMBERS_FOLDER_NAME.toString()).append("/")
				.append(String.valueOf(memberId)).append("/").append(imageName).toString();
		
		
	}
	
}
