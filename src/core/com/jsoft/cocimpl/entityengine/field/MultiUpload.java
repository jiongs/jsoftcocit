package com.jsoft.cocimpl.entityengine.field;

import org.nutz.mvc.upload.UploadInfo;

public class MultiUpload extends FakeSubSystem<UploadInfo> {
	public MultiUpload() {
		this("");
	}

	public MultiUpload(String str) {
		super(str, UploadInfo.class);
	}

	public MultiUpload(String str, Class type) {
		super(str, type);
	}
}