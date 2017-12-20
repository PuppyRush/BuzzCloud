package com.puppyrush.buzzcloud.dbAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mysql.jdbc.ResultSetMetaData;
import com.puppyrush.buzzcloud.bzexception.BZException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("DBManager")
public final class DBManager {


	public static class ColumnHelper{
		private List<Map<String,Object>> columns;
		private int columnCount;
		private boolean isEmpty;
		
		private ColumnHelper(){
			isEmpty = true;
			columnCount = 0;
			columns = new ArrayList<Map<String,Object>>();
		}
		
		private void addColumn(Map<String,Object> column){
			columns.add(column);
			columnCount++;
			isEmpty = true;
		}
		
		private boolean isValidation(int colIdx){
			if(isEmpty || columns.size()<=colIdx){
				return false;
			}
			return true;
		}
		
		public int columnSize(){
			return columns.size();
		}
		
		public boolean isEmpty(){
			return columns.isEmpty();
		}
		
		public List<Map<String,Object>> getColumns(){
			return columns;
		}
		
		@SuppressWarnings("unchecked")
		public <T> void getValue(int columnIdx, String fieldName, T value){
			if(!isValidation(columnIdx)){
				value = null;
			}
			else
				value = (T)columns.get(columnIdx).get(fieldName);
		}
		
		public String getString(int colIdx, String fieldName){
			if(!isValidation(colIdx)){
				return null;
			}
			else
				return (String)columns.get(colIdx).get(fieldName);
		}
		
		public Integer getInteger(int colIdx, String fieldName){
			if(!isValidation(colIdx)){
				return null;
			}
			else
				return (Integer)columns.get(colIdx).get(fieldName);
		}
		
		public Timestamp getTimestamp(int colIdx, String fieldName){
			if(!isValidation(colIdx)){
				return null;
			}
			else
				return (Timestamp)columns.get(colIdx).get(fieldName);
		}
	}
	
	private Connection conn = ConnectMysql.getConnector();
	
	public ColumnHelper getColumnsOfAll( String tableName, Map<String,Object> whereCaluse){
		
		List<Object> whereValue = new ArrayList<Object>();
		ColumnHelper ch = new ColumnHelper();
		try {
			
			StringBuilder sql;
			if(whereCaluse.size()>0){
				sql = new StringBuilder("select * from ").append(tableName).append(" where ");
				
				Iterator<String> it = whereCaluse.keySet().iterator();
				while(it.hasNext()){
					String where = it.next();
					whereValue.add(whereCaluse.get(where));
					sql.append(where).append(" = ? ");
					
					if(it.hasNext()){
						sql.append(" AND ");
					}
				}
				
			}
			else
				sql = new StringBuilder("select * from ").append(tableName);
			

			PreparedStatement ps = conn.prepareStatement(sql.toString());
			int i=0;
			for(i=0 ; i < whereValue.size() ; i++)
				ps.setObject(i+1, whereValue.get(i));
			
			ResultSet rs = ps.executeQuery();			
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			ArrayList<String> colName = new ArrayList<String>();
			for(int l=1 ; l <= rsmd.getColumnCount() ; l++){
				colName.add( rsmd.getColumnName(l) );
			}
				
			
			
			while(rs.next()){
				
				HashMap<String,Object> info = new HashMap<String,Object>();
				
				for(String key : colName)
					info.put(key, rs.getObject(key));
				ch.addColumn(info);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return ch;
		
	}
	
	public ColumnHelper getColumnsOfPart(String tableName, List<String> selectCaluse,  Map<String,Object> whereCaluse){
		
		if(selectCaluse.size()==0)
			return getColumnsOfAll(tableName, whereCaluse);
		
		List<Object> whereAry = new ArrayList<Object>();
		ColumnHelper ch = new ColumnHelper();
		
		try {
			
			StringBuilder sql = new StringBuilder("select ");
			
			if(whereCaluse.size()>0){
								
				Iterator<String> it = selectCaluse.iterator();
				while(it.hasNext()){
					String select = it.next();
					
					sql.append(select);
					
					if(it.hasNext()){
						sql.append(" , ");
					}
				}
				
			}
			
			sql.append(" from ").append(tableName).append(" ");
			
			if(whereCaluse.size()>0){
				sql.append(" where ");
				
				Iterator<String> it = whereCaluse.keySet().iterator();
				while(it.hasNext()){
					String where = it.next();
					whereAry.add(whereCaluse.get(where));
					sql.append(where).append(" = ?");
					
					if(it.hasNext()){
						sql.append(" AND ");
					}
				}
				
			}
			else
				sql = new StringBuilder(" from").append(tableName);
			
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			int i=0;
			for(i=0 ; i < whereAry.size() ; i++)
				ps.setObject(i+1, whereAry.get(i));
						
			ResultSet rs = ps.executeQuery();			
							
			while(rs.next()){
				HashMap<String,Object> info = new HashMap<String,Object>();
				for(String colName : selectCaluse)
					info.put(colName, rs.getObject(colName));
				
				ch.addColumn(info);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ch;
		
	}	
	
	public void updateColumn(String tableName, Map<String,Object> set, Map<String,Object> whereCaluse) throws SQLException{
		
		ArrayList<Object> whereAry = new ArrayList<Object>();
		ArrayList<Object> setAry = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ");
		
		if(set.size()>0){
			Iterator<String> it = set.keySet().iterator();
			while(it.hasNext()){
				String str = it.next();
				setAry.add(set.get(str));
				
				sql.append(str).append(" = ? ");
				
				if(it.hasNext()){
					sql.append(" , ");
				}
			}		
		}
		else
			throw new SQLException("update 명령에 set은 한 개 이상은 있어야합니다.");
		
		
		if(whereCaluse.size()>0){
			sql.append(" where ");
			
			Iterator<String> it = whereCaluse.keySet().iterator();
			while(it.hasNext()){
				String where = it.next();
				whereAry.add(whereCaluse.get(where));				

				sql.append(where).append(" = ?");
				
				if(it.hasNext()){
					sql.append(" AND ");
				}
			}	
		}
		
		try {
			
			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			
			int i=0;
			for(i=0 ; i < setAry.size() ; i++)
				ps.setObject(i+1, setAry.get(i));
			
			for(int l=0 ; l < whereAry.size() ; l++)
				ps.setObject(i+l+1, whereAry.get(l));
				
			ps.executeUpdate();
			
			ps.close();				
			conn.commit();
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public List<Integer>  insertColumn(String tableName, List<String> columns, List<List<Object>> values){
		
		List<Integer> keys = new ArrayList<Integer>();
		StringBuilder sql = new StringBuilder("insert into ").append(tableName).append(" (");
		
		Iterator<String> it = columns.iterator();
		while(it.hasNext()){
			String str = it.next();
			sql.append(str);
			
			if(it.hasNext())
				sql.append(" , ");			
			else
				sql.append(")");
		}
		
		sql.append(" values (");
		for(int i=0; i < values.size() ; i++){
			
			for(int l=0 ; l < values.get(i).size() ; l++){
				if(l==values.get(i).size()-1)
					sql.append("?");
				else
					sql.append("?,");
			}
			if(i==values.size()-1)
				sql.append(")");
			else
				sql.append("),(");
		}
		
		
		try {
			
			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement(sql.toString(),PreparedStatement.RETURN_GENERATED_KEYS);
			
			int count = 1;
			for(int i=0; i < values.size() ; i++){
				for(int l=0 ; l < values.get(i).size() ; l++){
					ps.setObject(count++, values.get(i).get(l));
				}
			}
				
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();		
			
			while(rs.next()){
				java.sql.ResultSetMetaData rsmd = rs.getMetaData();
				int keyCount = rsmd.getColumnCount();
				for(int i=1 ; i <= keyCount ; i++)
					keys.add(rs.getInt(i));
				
			}
			
			conn.commit();
			conn.setAutoCommit(true);
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keys;
		
	}

	/**
	 * 
	 * 
	 * @param where 조건절이 한개 이상은 있어야 한다.
	 */
	public void deleteColumns(String tableName, Map<String,Object> where){

		try {
			
			ArrayList<Object> whereAry = new ArrayList<Object>();
			StringBuilder sql;
			
			if(where.size()>0){
				
				sql = new StringBuilder("delete from ").append(tableName).append(" where ");
				
				Iterator<String> it = where.keySet().iterator();
				while(it.hasNext()){
					String str = it.next();
					whereAry.add(where.get(str));
					sql.append(str).append(" = ? ");
					
					if(it.hasNext()){
						sql.append(" AND ");
					}
				}
				
			}
			else
				throw new IllegalArgumentException("조건절이 없습니다.");
		
			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			for(int i=0 ; i < whereAry.size() ; i++)
				ps.setObject(i+1, whereAry.get(i));
				
			ps.executeUpdate();
			
			conn.commit();
			conn.setAutoCommit(true);
			ps.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
