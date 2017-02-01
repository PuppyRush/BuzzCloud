package com.puppyrush.buzzcloud.dbAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.javadude.annotation.Default;
import com.mysql.jdbc.ResultSetMetaData;
import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("DBManager")
public final class DBManager {

	private Connection conn = ConnectMysql.getConnector();
	
	
	public List<Map<String,Object>> getColumnsOfAll( String tableName, Map<String,Object> whereCaluse){
		
		List<Object> whereAry = new ArrayList<Object>();
		List<Map<String,Object> > ary = new ArrayList<Map<String,Object> >();
					
		try {
			
			StringBuilder sql;
			if(whereCaluse.size()>0){
				sql = new StringBuilder("select * from ").append(tableName).append(" where ");
				
				Iterator<String> it = whereCaluse.keySet().iterator();
				while(it.hasNext()){
					String where = it.next();
					whereAry.add(whereCaluse.get(where));
					sql.append(where).append(" = ?");
					
					if(it.hasNext()){
						sql.append(" , ");
					}
				}
				
			}
			else
				sql = new StringBuilder("select * from ").append(tableName);
			

			PreparedStatement ps = conn.prepareStatement(sql.toString());
			int i=0;
			for(i=0 ; i < whereAry.size() ; i++)
				ps.setObject(i+1, whereAry.get(i));
			
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
				ary.add(info);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ary;
		
	}
	
	public List<Map<String,Object>> getColumnsOfPart(String tableName, List<String> selectCaluse,  Map<String,Object> whereCaluse){
		
		if(selectCaluse.size()==0)
			return getColumnsOfAll(tableName, whereCaluse);
		
		List<Object> whereAry = new ArrayList<Object>();
		List<Map<String,Object> > ary = new ArrayList<Map<String,Object> >();
		
			
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
						sql.append(" , ");
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
				
				ary.add(info);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ary;
		
	}
	
	
	
	public void updateColumn(String tableName, Map<String,Object> set, Map<String,Object> whereCaluse){
		
		ArrayList<Object> whereAry = new ArrayList<Object>();
		ArrayList<Object> valuesAry = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("update ").append(tableName).append(" ").append(" set ");
		
		if(set.size()>0){
			Iterator<String> it = set.keySet().iterator();
			while(it.hasNext()){
				String str = it.next();
				valuesAry.add(set.get(str));
				
				sql.append(str).append(" = ?");
				
				if(it.hasNext()){
					sql.append(",");
				}
			}		
		}
		else
			return;
		
		
		if(whereCaluse.size()>0){
			sql.append(" where ");
			
			Iterator<String> it = whereCaluse.keySet().iterator();
			while(it.hasNext()){
				String where = it.next();
				whereAry.add(whereCaluse.get(where));				

				sql.append(where).append(" = ?");
				
				if(it.hasNext()){
					sql.append(" , ");
				}
			}	
		}
		
		try {
			
			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			
			int i=0;
			for(i=0 ; i < valuesAry.size() ; i++)
				ps.setObject(i+1, valuesAry.get(i));
			
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
	
	public void insertColumn(String tableName, Map<String,Object> values){
		
		ArrayList<Object> valuesAry = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("insert into ").append(tableName).append("(");
		
		Iterator<String> it = values.keySet().iterator();
		while(it.hasNext()){
			String str = it.next();
			valuesAry.add(values.get(str));
			
			sql.append(str);
			
			if(it.hasNext())
				sql.append(" , ");			
			else
				sql.append(")");
		}
		
		sql.append(" values (");
		for(int i=0 ; i < values.size() ; i++){
			
			sql.append("?");
			
			if(i==values.size()-1)
				continue;			
			else
				sql.append(",");
		}
		sql.append(")");
		
		try {
			
			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			for(int i=0 ; i < valuesAry.size() ; i++)
				ps.setObject(i+1, valuesAry.get(i));
				
			ps.executeUpdate();
			
			conn.commit();
			conn.setAutoCommit(true);
			ps.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
					whereAry.add(where.get(where));
					sql.append(str).append(" = ? ");
					
					if(it.hasNext()){
						sql.append(" , ");
					}
				}
				
			}
			else
				throw new IllegalArgumentException("조건절이 없습니다.");
		
			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			for(int i=0 ; i < where.size() ; i++)
				ps.setObject(i+1, where.get(i));
				
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
