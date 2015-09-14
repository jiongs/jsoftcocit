package com.jsoft.cocit.ckfinder;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Class to handle <code>QuickUpload</code> command.
 */
public class QuickUploadCommand extends FileUploadCommand {

	@Override
	protected void handleOnUploadCompleteResponse(final OutputStream out,
			final String errorMsg) throws IOException {
		out.write("window.parent.OnUploadCompleted(".getBytes("UTF-8"));
		out.write(("" + this.errorCode + ", ").getBytes("UTF-8"));
		if (uploaded) {
			out.write(("\'" + configuration.getTypes().get(this.type).getUrl()
					+ this.currentFolder + this.newFileName + "\', ").getBytes("UTF-8"));
			out.write(("\'" + this.newFileName + "\', ").getBytes("UTF-8"));
		} else {
			out.write("\'\', \'\', ".getBytes("UTF-8"));
		}
		out.write("\'\'".getBytes("UTF-8"));
		out.write(");".getBytes("UTF-8"));
	}


	@Override
	protected void handleOnUploadCompleteCallFuncResponse(final OutputStream out,
					final String errorMsg, final String path) throws IOException {
		this.ckEditorFuncNum = this.ckEditorFuncNum.replaceAll(
				"[^\\d]", "");
		out.write(("window.parent.CKEDITOR.tools.callFunction("
				+ this.ckEditorFuncNum + ", '"
				+ path
				+ this.newFileName + "', '" + errorMsg + "');")
				.getBytes("UTF-8"));
	}

	@Override
	protected boolean checkFuncNum() {
		return this.ckEditorFuncNum != null;
	}

}