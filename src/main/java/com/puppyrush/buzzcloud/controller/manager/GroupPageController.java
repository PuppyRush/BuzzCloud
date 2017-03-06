package com.puppyrush.buzzcloud.controller.manager;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.Band.BundleBand;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.property.tree.Tree;
import com.puppyrush.buzzcloud.service.band.GettingSelectedBandMembers;
import com.puppyrush.buzzcloud.service.band.SearchedBandInfo;
import com.puppyrush.buzzcloud.service.manager.band.InitMyBandInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller("groupPage")
@RequestMapping("/managerPage/groupConfig/*")
public class GroupPageController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required = false)
	private BandManager			bandMng;

	@Autowired(required = false)
	private BandDB				bandDB;

	@Autowired(required = false)
	private MemberDB			mDB;

	@Autowired(required = false)
	private MemberController		mCtl;

	@Autowired(required = false)
	private AuthorityManager		authMng;

	@Autowired(required = false)
	private DBManager			dbAccess;

	@Autowired(required = false)
	private InitMyBandInfo			bandInfo;

	@Autowired(required = false)
	private GettingSelectedBandMembers	gettingSelectedBandMembers;

	@Autowired(required = false)
	private SearchedBandInfo		searhcedBandInfo;

	public GroupPageController() {

	}

	@ResponseBody
	@RequestMapping(value = "/initMyBandInformation.ajax", method = RequestMethod.GET)
	public Map<String, Object> initMyBandInformation(HttpServletRequest rq) {

		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = bandInfo.execute(mCtl.getMember(rq.getRequestedSessionId()).getId());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returns;

	}

	@ResponseBody
	@RequestMapping(value = "/getBandAll.ajax", method = RequestMethod.POST)
	public Map<String, Object> getBandall(@RequestParam("bandId") int bandId) {

		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> where = new HashMap<String, Object>();
		List<Map<String, Object>> bandInfo = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> bandDetail = new ArrayList<Map<String, Object>>();

		where.put("bandId", bandId);

		bandInfo = dbAccess.getColumnsOfAll("band", where);
		bandDetail = dbAccess.getColumnsOfAll("bandDetail", where);

		result.put("bandId", bandId);
		result.put("bandName", bandInfo.get(0).get("name"));
		result.put("bandCapacity", bandDetail.get(0).get("maxCapacity"));
		result.put("bandContains", bandDetail.get(0).get("bandContains"));

		int ownerId = (int) bandInfo.get(0).get("owner");
		int adminId = (int) bandInfo.get(0).get("administrator");
		int upperBandId = bandMng.getUpperBand(bandId);

		String ownerNickname = mDB.getNicknameOfId(ownerId);
		String adminNickname = mDB.getNicknameOfId(adminId);
		String upperBandName = bandDB.getBandNameOf(upperBandId);

		result.put("ownerId", ownerId);
		result.put("adminId", adminId);
		result.put("upperBandId", upperBandId);
		result.put("upperBandName", upperBandName);
		result.put("ownerNickname", ownerNickname);
		result.put("adminNickname", adminNickname);

		List<Map<Integer, String>> memberAry = new ArrayList<Map<Integer, String>>();
		ArrayList<Member> members = bandMng.getMembersOf(bandId);
		for (Member member : members) {
			HashMap<Integer, String> temp = new HashMap<Integer, String>();
			temp.put(member.getId(), member.getNickname());
			memberAry.add(temp);
		}
		result.put("bandMembers", memberAry);

		BandAuthority ba = authMng.getBandAuthority(bandId);
		List<String> bandAuth = new ArrayList<String>();
		for (enumBandAuthority auth : ba.toArray())
			bandAuth.add(auth.toString());

		result.put("bandAuthority", bandAuth);

		return result;

	}

	@ResponseBody
	@RequestMapping(value = "/updateBand.ajax", method = RequestMethod.POST)
	public Map<String, Object> updateBand(@RequestParam("memberId") int memberId) {

		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = bandInfo.execute(memberId);
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returns;

	}

	@ResponseBody
	@RequestMapping(value = "/getSelectedBandMembers.ajax", method = RequestMethod.POST)
	public Map<String, Object> gettingSelectedBandMembers(@RequestParam("bandId") int bandId) {

		Map<String, Object> returns = new HashMap<String, Object>();
		returns = gettingSelectedBandMembers.execute(bandId);

		return returns;

	}

	@ResponseBody
	@RequestMapping(value = "/searhcedBandInfo.ajax", method = RequestMethod.POST)
	public Map<String, Object> searhcedBandInfo(@RequestParam("bandId") int bandId) {

		Map<String, Object> returns = new HashMap<String, Object>();
		returns = searhcedBandInfo.execute(bandId);

		return returns;

	}
}
