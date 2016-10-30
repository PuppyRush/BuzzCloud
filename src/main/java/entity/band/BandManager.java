package entity.band;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.ControllerException;
import entity.EntityException;
import entity.authority.AuthorityManager;
import entity.authority.FileAuthority;
import entity.authority.BandAuthority;
import entity.authority.MemberAuthority;
import entity.authority.enums.enumBandAuthority;
import entity.band.Band.AuthoritedMember;
import entity.member.Member;
import entity.member.MemberManager;
import page.enums.enumPage;
import property.ConnectMysql;
import property.tree.Node;
import property.tree.Tree;

public class BandManager {

	protected Connection conn = ConnectMysql.getConnector();

	private static class Singleton {
		private static final BandManager instance = new BandManager();
	}

	public static BandManager getInstance() {
		return Singleton.instance;
	}
		
	public ArrayList<Band> getMyOwneredBands(int memberId) throws EntityException{
	
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Band> ownerdBands = new ArrayList<Band>();
		try{
			
			ArrayList<Integer> bandsAry = new ArrayList<Integer>();
			
			ps = conn.prepareStatement("select bandId from band where owner = ?");
			ps.setInt(1, memberId);
			rs = ps.executeQuery();
			
			while(rs.next())
				bandsAry.add(rs.getInt(1));
			
			for(int owneredBandId : bandsAry){
				if(BandController.getInstance().containsEntity(owneredBandId) == false){
					Band band = getBand(owneredBandId);
					ownerdBands.add(band);
				}
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null)
					ps.close();
				if(rs!=null)
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}		
		
		return ownerdBands;
		
	}
	
	public ArrayList<Band> getMyAdministeredBands(int memberId) throws EntityException{
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Band> rootBands = new ArrayList<Band>();
		try{
			
			ArrayList<Integer> bandsAry = new ArrayList<Integer>();
			
			ps = conn.prepareStatement("select bandId from band where administrator = ?");
			ps.setInt(1, memberId);
			rs = ps.executeQuery();
			
			while(rs.next())
				bandsAry.add(rs.getInt(1));
				
			ArrayList<Integer> rootBandsId = getRootBandOf(bandsAry);
			
			for(int rootBandId : rootBandsId){
				if(BandController.getInstance().containsEntity(rootBandId) == false){
					Band band = getBand(rootBandId);
					rootBands.add(band);
				}
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return rootBands;
		
	}
	
	private ArrayList<Integer> getRootBandOf(ArrayList<Integer> bands){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Integer> rootBands = new ArrayList<Integer>();
		
		try{
			
			for(int bandId : bands){
				
				ps = conn.prepareStatement("select isRoot from bandAuthority where bandId = ?");
				ps.setInt(1, bandId);
				rs = ps.executeQuery();
				rs.next();
				if(rs.getInt(1)==1)
					rootBands.add(bandId);
								
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	
		return rootBands;
	}
	
	public boolean isRootBand(int bandId){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isEqual = false;
		try{
				
			ps = conn.prepareStatement("select * from bandRelation where fromBand = ?");
			ps.setInt(1, bandId);
			rs = ps.executeQuery();
			
			while(rs.next()){
				if(rs.getInt(1)==rs.getInt(2)){
					isEqual = true;
					break;
				}
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
					
		return isEqual;
	}
		
	public boolean isExistSubBand(int bandId){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExist = false;
		try{
			
			ps = conn.prepareStatement("select toBand from bandRelation where fromBand = ?");
			ps.setInt(1, bandId);
			rs = ps.executeQuery();

			isExist = rs.next();
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null)
					ps.close();
				if(rs!=null)
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}		
		
		return isExist;
	}
	
	public int getUpperBand(int bandId) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int upperBandId = bandId;
		try {

			ps = conn.prepareStatement("select * from bandRelation where toBand = ?");
			ps.setInt(1, bandId);
			rs = ps.executeQuery();
			rs.next();
			upperBandId = rs.getInt("fromBand");

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return upperBandId;
	}

	public ArrayList<Integer> getSubBand(int bandId) {

		ArrayList<Integer> subBands = new ArrayList<Integer>();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = conn.prepareStatement("select * from bandRelation where fromBand = ?");
			ps.setInt(1, bandId);
			rs = ps.executeQuery();
			while (rs.next()){
				int toBand = rs.getInt("toBand");
				if(bandId==toBand)
					continue;
				
				subBands.add(rs.getInt("toBand"));
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return subBands;

	}

	public int getRootBandIdOf(int bandId){
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int gId = bandId;
		
		try {
			
			while(true){
				ps = conn.prepareStatement("select fromBand from bandRelation where toBand = ?");
				ps.setInt(1, gId);
				rs = ps.executeQuery();
				if(rs.next())
					gId = rs.getInt("fromBand");
				else
					break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return gId;
	}
	
	public Tree<Band> getSubBands(int bandId) throws EntityException{
		
		Tree<Band> tree = null;
		
		try{
			Band band = BandController.getInstance().containsEntity(bandId) ?  BandController.getInstance().getEntity(bandId) : BandManager.getInstance().getBand(bandId);
			Node<Band> node =  BandManager.getInstance().getSubBandsRecursive(band.getBandId());
			
			tree = new Tree<Band>(band);
			tree.SetPN(node);
			
		}catch(ControllerException e){
			e.printStackTrace();
		}
		
		return tree;
	}
	
	private Node<Band> getSubBandsRecursive(int upperBandId) throws EntityException{
	
		Node<Band> upperNode = null;
		
		try{		
			
			List<Band> siblingBandsId = new ArrayList<Band>();
		
			PreparedStatement ps = conn.prepareStatement("select toBand from bandRelation where fromBand = ?");
			ps.setInt(1, upperBandId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
	
				int tobandId = rs.getInt("toBand");
				if(upperBandId==tobandId)
					continue;
				
				Band _band = BandController.getInstance().containsEntity(tobandId) ?  BandController.getInstance().getEntity(tobandId) : BandManager.getInstance().getBand(tobandId);			
				siblingBandsId.add(_band);
	
			}
			if(siblingBandsId.size()>0){
				
				Band band = BandController.getInstance().containsEntity(upperBandId) ?  BandController.getInstance().getEntity(upperBandId) : BandManager.getInstance().getBand(upperBandId);
				upperNode = new Node<Band>(band);
				
				ArrayList<Node<Band>> sibligBands = new ArrayList<Node<Band>>();
				Node<Band> childNodeOfUpperNode = new Node<Band>(siblingBandsId.get(0));
				
				upperNode.SetChild(childNodeOfUpperNode);
				sibligBands.add(childNodeOfUpperNode);
				
				for(int i=1 ; i <siblingBandsId.size() ; i++){
					Node<Band> newNode = new Node<Band>(siblingBandsId.get(i));
					sibligBands.get(i-1).SetSibling(newNode);
					sibligBands.add(newNode);
					
				}
				
				for(Node<Band> node : sibligBands)
					node.SetChild( getSubBandsRecursive(node.GetData().getBandId()));
				
			}				
				
		}
		catch(SQLException e){
			e.printStackTrace();
		}catch (ControllerException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return upperNode==null ? null : upperNode.GetChild();
		
	}

	public void getUpperBandsTree(int bandId){
		
		
	}

	public Band getBand(int bandId) throws EntityException{
		
		String bandName = "";
		int ownerId = -1;
		Band band = null;
		
		try{
			PreparedStatement ps = conn.prepareStatement("select * from band where bandId = ? ");
			ps.setInt(1, bandId);
			ResultSet rs = ps.executeQuery();
			
			rs.next();

			ownerId = rs.getInt("owner");
			bandName = rs.getString("name");
			
			ps.close();
			rs.close();
									
			int upperBandId = BandManager.getInstance().getUpperBand(bandId);
			
			BandAuthority bandAuthority = AuthorityManager.getInstance().getBandAuthority(bandId);
			MemberAuthority mAuth= AuthorityManager.getInstance().getMemberAuthority(ownerId, bandId);
			FileAuthority fAuth = AuthorityManager.getInstance().getFileAuthoirty(ownerId, bandId);
			
			Member member = MemberManager.getInstance().getMember(ownerId);
			HashMap<Integer,AuthoritedMember > members = new HashMap<Integer, AuthoritedMember>(); 
			
			members.put(member.getId(), new AuthoritedMember(member, mAuth, fAuth));
			
			boolean isFinal=false, isRoot=false;
			if(bandAuthority.getAuthorityType().get(enumBandAuthority.FINAL))
				isFinal = true;
			
			if(bandAuthority.getAuthorityType().get(enumBandAuthority.ROOT))
				isRoot = true;
			
			band = new Band.Builder(bandId,ownerId,bandName).bandAuhority(bandAuthority).isFinal(isFinal).isRoot(isRoot)
					.members(members).upperBandId(upperBandId).build();
			
			if(BandController.getInstance().containsEntity(bandId) == false)
				BandController.getInstance().addEntity(bandId, band);
			
		}catch(SQLException e){
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return band; 

	}
	
}
