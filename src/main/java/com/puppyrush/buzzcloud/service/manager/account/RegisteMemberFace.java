package com.puppyrush.buzzcloud.service.manager.account;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.property.CommFunc;
import com.puppyrush.buzzcloud.property.enumSystem;

@Service("registeMemberFace")
public class RegisteMemberFace {

	@Autowired(required = false)
	private MemberController	mCtl;

	@Autowired(required = false)
	private DBManager		dbMng;

	private String			ext;
	private String			newfileName;
	private String			relativePath;
	private Member			member;

	public Map<String, Object> execute(String sId, MultipartHttpServletRequest rq)
			throws ControllerException, IllegalStateException, IOException, SQLException {

		Map<String, Object> returns = new HashMap<String, Object>();
		member = mCtl.getMember(sId);

		if (rq.getFileMap().size() != 1)
			throw new IllegalArgumentException("회원 이미지 등록에 파일은 하나만 있어야 합니다.");

		Iterator<String> iterator = rq.getFileNames();
		MultipartFile multiFile = null;
		while (iterator.hasNext()) {
			multiFile = rq.getFile(iterator.next());
			if (!multiFile.isEmpty()) {
				confirmRegistriationAndRemoveImage();
				setFileName(multiFile);
				File file = getFile(multiFile);

				multiFile.transferTo(file);
				saveFile(file);
				confirmImageSizeAndResize(file);

				returns.putAll(new InstanceMessage("이미지가 등록되었습니다.", enumInstanceMessage.SUCCESS)
						.getMessage());
				break;
			}
		}

		return returns;

	}

	private void confirmRegistriationAndRemoveImage() throws SQLException {

		Map<String, Object> where = new HashMap<String, Object>();
		List<String> set = new ArrayList<String>();
		where.put("memberId", member.getId());
		set.add("image");

		List<Map<String, Object>> res = dbMng.getColumnsOfPart("memberDetail", set, where);
		if (res.size() != 1) {
			throw new SQLException("이미지 등록 DB내부오류. 관리자에게 문의하세요.");
		}

		String savedImageName = (String) res.get(0).get("image");
		if (!savedImageName.equals("") || !savedImageName.contains("defaultImage")) {

			File oldFile = new File(CommFunc.toAbsolutePathFromImage(member.getId(), savedImageName));
			if (oldFile.exists() && !oldFile.isDirectory()) {
				oldFile.delete();
			}
		}

	}

	private void setFileName(MultipartFile file) {
		newfileName = UUID.randomUUID().toString();
		ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
	}

	private File getFile(MultipartFile multiFile) {

	
		File file = new File(CommFunc.toAbsolutePathFromImage(member.getId(), newfileName+ext));
		file.mkdirs();
		return file;
	}

	private void saveFile(File file) throws SQLException {

		HashMap<String, Object> where = new HashMap<String, Object>();
		HashMap<String, Object> set = new HashMap<String, Object>();

		where.put("memberId", member.getId());
		set.put("image", file.getName());
		dbMng.updateColumn("memberDetail", set, where);

	}

	private void confirmImageSizeAndResize(File img) throws IOException {
	
		Image newImage = getScaledImage(img);		
		ImageIO.write(toBufferedImageFrom(newImage, newImage.getWidth(null), newImage.getHeight(null)), "jpg", img);
	}

	private Image getScaledImage(File img) throws IOException{
		
		Image oldImage = ImageIO.read(img);
		Image newImage = null;
		final int maxWidth = enumSystem.MAX_MEMBER_IMAGE_WIDTH.toInt();
		final int maxHeight = enumSystem.MAX_MEMBER_IMAGE_HEIGHT.toInt();
		final int oldWidth = oldImage.getWidth(null);
		final int oldHeight = oldImage.getHeight(null);
		int newHeight = 0;
		int newWidth = 0;

		if (oldHeight < maxHeight	|| oldWidth < maxWidth)
			return oldImage;

		if (oldHeight >= maxHeight	|| oldWidth >= maxWidth) {
			int longer = Math.max(oldHeight, oldWidth);
			float ratio = Math.round((120f / (float) longer) * 100f) / 100f;
			newHeight = (int) (oldHeight * ratio);
			newWidth = (int) (oldWidth * ratio);
			newImage = oldImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		}
		if (newImage == null)
			throw new IOException("파일 크기 조절에 실패하였습니다.");

		return newImage;
	}
	
	private BufferedImage toBufferedImageFrom(Image img, int width, int height) {

		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		
		// Return the buffered image
		return bimage;
	}
}
