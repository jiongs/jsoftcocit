package com.jsoft.cocimpl.action;

import static com.jsoft.cocit.constant.UrlAPI.URL_CKFINDER;
import static com.jsoft.cocit.constant.UrlAPI.URL_UPLOAD;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.FieldMeta;
import org.nutz.mvc.upload.TempFile;

import com.jsoft.cocimpl.ckfinder.CKFinder;
import com.jsoft.cocimpl.mvc.nutz.CocUploadAdaptor;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.config.ICommonConfig;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.entity.log.IExtUploadLog;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.ui.model.datamodel.JSONModel;
import com.jsoft.cocit.util.DateUtil;
import com.jsoft.cocit.util.FileUtil;
import com.jsoft.cocit.util.ImageUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class UploadAction {

	@At({ URL_CKFINDER })
	@Ok("void")
	public void ckfinder() {
		HttpContext ctx = Cocit.me().getHttpContext();
		CKFinder.doUpload(ctx.getRequest(), ctx.getResponse());
	}

	@At({ URL_UPLOAD })
	@AdaptBy(type = CocUploadAdaptor.class)
	public JSONModel upload(//
	        @Param("upload") TempFile tmpfile,//
	        String moduleId, //
	        String fieldId//
	) {
		if (LogUtil.isDebugEnabled()) {
			LogUtil.debug("UploadAction.upload: ...[moduleId=%s, fieldId=%s, fileName=%s]", //
			        moduleId, //
			        fieldId, //
			        tmpfile == null ? "NULL" : tmpfile.getMeta().getFileLocalName()//
			);
		}

		Map map = new HashMap();

		int errorNumber;
		String customMsg;
		boolean success;

		// 保存文件
		try {
			if (tmpfile == null) {
				throw new CocException("上传的临时文件不存在! ");
			}

			IExtUploadLog info = save(tmpfile, moduleId, fieldId);
			String fileUrl = "";
			if (info.getFilePath() != null) {
				fileUrl = info.getFilePath().toString();
			}
			map.put("path", fileUrl);
			map.put("name", info.getLocalName());
			map.put("contentLength", info.getContentLength());

			map.put("fileUrl", fileUrl);
			map.put("fileName", info.getLocalName());

			// try {
			// if (Images.isImage(fileUrl.substring(fileUrl.lastIndexOf(".") +
			// 1).toLowerCase())) {
			// String zoomImgPath = zoomImgPath(fileUrl, 100, 75);
			// boolean isImg = Images.zoomImage(contextDir + fileUrl, contextDir
			// + zoomImgPath, 100, 75);
			// if (isImg) {
			// map.put("fileZoomImageUrl", zoomImgPath);
			// }
			// }
			// } catch (Throwable e) {
			// LogUtil.errorf("上传文件: 压缩图片LOGO出错! %s", e);
			// }

			errorNumber = 101;
			customMsg = "上传文件: 成功! [" + info.getLocalName() + "]";
			success = true;

			LogUtil.debug("UploadAction.upload: 上传文件成功. [moduleId=%s, fieldId=%s, fileUrl=%s]", //
			        moduleId, //
			        fieldId, //
			        fileUrl//
			);

		} catch (Throwable ex) {
			errorNumber = 1;
			customMsg = "上传文件出错: " + ex.getLocalizedMessage();
			success = false;

			LogUtil.error("UploadAction.upload: 上传文件出错: [moduleId=%s, fieldId=%s, fileName=%s] %s", //
			        moduleId, //
			        fieldId, //
			        tmpfile == null ? "NULL" : tmpfile.getMeta().getFileLocalName(),//
			        ex.getLocalizedMessage()//
			);
		} finally {
			if (tmpfile != null) {
				tmpfile.getFile().delete();
			}
		}

		map.put("success", success);
		map.put("errorNumber", errorNumber);
		map.put("customMsg", customMsg);

		return JSONModel.make(JsonUtil.toJson(map));
	}

	/**
	 * 保存上传的临时文件到系统上传目录，并返回文件的相对路径
	 * 
	 * @return
	 * @throws IOException
	 * @throws Throwable
	 */
	private IExtUploadLog save(TempFile model, String moduleId, String fieldId) throws CocException, IOException {
		LogUtil.debug("UploadAction.save...[model=%s]", model);

		/*
		 * 保存文件到磁盘
		 */
		FieldMeta meta = model.getMeta();
		File tmpfile = model.getFile();

		LogUtil.debug("UploadAction.save...[meta: %s, tmpfile: %s]", meta, tmpfile);

		String extName = meta.getFileExtension();
		if (extName != null && extName.startsWith(".")) {
			extName = extName.substring(1);
		}

		Cocit me = Cocit.me();
		
		LogUtil.debug("UploadAction.save...[me: %s]", me);
		
		ICommonConfig config = me.getConfig();
		HttpContext ctx = me.getHttpContext();
		Orm orm = me.orm();

		LogUtil.debug("UploadAction.save...[UPLOAD_FILTER: %s]", config.get(ICommonConfig.UPLOAD_FILTER));
		
		List<String> extList = StringUtil.toList(config.get(ICommonConfig.UPLOAD_FILTER).toLowerCase(), "|");

		if (!extList.contains(extName.toLowerCase())) {
			throw new CocException("非法文件类型! [%s]", meta.getFileLocalName());
		}

		// 计算文件存储目录
		String basePath = MVCUtil.getUploadPath();

		LogUtil.debug("UploadAction.save...[basePath: %s]", basePath);

		String folder = ctx.getParameterValue("folder", String.class, null);
		if (StringUtil.hasContent(folder)) {
			if (folder.startsWith("/"))
				basePath = basePath + folder;
			else
				basePath = basePath + "/" + folder;
		} else if (ImageUtil.isImage(extName)) {
			basePath = basePath + "/images";
		} else if (extName.equals("swf") || extName.equals("flv")) {
			basePath = basePath + "/flash";
		} else {
			basePath = basePath + "/files";
		}

		LogUtil.debug("UploadAction.save...[folder: %s]", folder);

		if (StringUtil.isBlank(folder))
			basePath = basePath + "/" + DateUtil.format(new Date(), "yyyyMM");

		String realFolder = me.getContextDir() + basePath;

		String localName = meta.getFileLocalName();
		localName = localName.replace("(", "_");
		localName = localName.replace(")", "_");
		localName = localName.replace(" ", "_");
		localName = localName.substring(0, localName.length() - extName.length() - 1);
		
		LogUtil.debug("UploadAction.save...[localName: %s]", localName);

		// 计算文件名
		String fileName;
		File file = null;
		int count = 0;
		while (true) {
			if (count > 0) {
				fileName = localName + "_" + count;
			} else {
				fileName = localName;
			}
			fileName = Integer.toHexString(fileName.hashCode()) + "." + extName;
			// fileName = fileName + "." + ext;
			file = new File(realFolder + "/" + fileName);
			if (file.exists()) {
				count++;
			} else {
				break;
			}
		}
		
		LogUtil.debug("UploadAction.save...[fileName: %s]", fileName);

		file.getParentFile().mkdirs();
		file.createNewFile();
		FileUtil.copy(tmpfile, file);

		/*
		 * 保存文件信息到数据库
		 */
		IExtUploadLog info = (IExtUploadLog) Mirror.me(EntityTypes.UploadLog).born();
		
		LogUtil.debug("UploadAction.save...[info: %s]", info);

		if (StringUtil.hasContent(ctx.getLoginTenantKey()))
			info.setTenantKey(ctx.getLoginTenantKey());
		info.setContentType(meta.getContentType());
		info.setExtName(extName);
		info.setLocalName(meta.getFileLocalName());
		info.setContentLength(file.length());
		String filePath = basePath + "/" + fileName;
		info.setFilePath(filePath);
		info.setKey(Integer.toHexString(filePath.hashCode()));
		info.setLocalNamePinYin(StringUtil.toPinyinFirstChar(info.getLocalName()));

		if (StringUtil.hasContent(moduleId))
			info.setUploadFromModule(moduleId);

		if (StringUtil.hasContent(fieldId))
			info.setUploadFromField(fieldId);

		orm.save(info);

		LogUtil.debug("UploadAction.save: SUCCESS![filePath=%s]", info.getFilePath());

		return info;
	}

}
