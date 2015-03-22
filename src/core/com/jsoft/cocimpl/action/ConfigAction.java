package com.jsoft.cocimpl.action;

import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.jsoft.cocit.mvc.UIModelView;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class ConfigAction {
	//
	// @At("/config/clrmapping")
	// public synchronized Status clearMapping() throws DemsyException {
	// log.debugf("清空实体映射...");
	// try {
	// security.checkLoginUserOwnRole(SecurityManager.ROLE_DP_SUPPORT);
	//
	// orm().clearMapping();
	//
	// log.debugf("清空实体映射成功.");
	// return new Status(true, "清除实体映射缓存成功.");
	// } catch (Throwable e) {
	//
	// if (log.isDebugEnabled())
	// log.debugf("清空实体映射失败! %s", ExceptionUtil.msg(e));
	//
	// return new Status(false, "清空实体映射失败!" + ExceptionUtil.msg(e));
	// }
	// }
	//
	// private IDemsySoft getSoft(String softID) throws DemsyException {
	// IDemsySoft soft = null;
	// if (!Str.isEmpty(softID)) {
	// soft = moduleEngine.getSoft(Long.parseLong(softID));
	// if (soft == null) {
	// throw new DemsyException("应用系统不存在!");
	// }
	// }
	// return soft;
	// }
	//
	// /**
	// * 升级SFT功能模块到DEMSY平台中
	// *
	// * @return
	// */
	// @At("/config/upgradeModules/*")
	// public synchronized Status upgradeModule(String softID) {
	// log.debugf("升级功能菜单...");
	// try {
	// security.checkLoginUserOwnRole(SecurityManager.ROLE_DP_SUPPORT);
	//
	// moduleEngine.upgradeModules(getSoft(softID));
	//
	// log.debugf("升级功能菜单成功.");
	// return new Status(true, "升级功能菜单成功.");
	// } catch (Throwable e) {
	// String title = "升级功能菜单出错! ";
	//
	// if (log.isDebugEnabled())
	// log.debugf("%s %s", title, ExceptionUtil.msg(e));
	//
	// return new Status(false, title + ExceptionUtil.msg(e));
	// }
	// }
	//
	// @At("/config/upgradeWebInfo/*")
	// public synchronized Status upgradeWebInfo(String softID) {
	// log.debugf("升级网站栏目信息...");
	// try {
	// security.checkLoginUserOwnRole(SecurityManager.ROLE_DP_SUPPORT);
	//
	// moduleEngine.upgradeWebContent(getSoft(softID));
	//
	// log.debugf("升级网站栏目信息成功.");
	// return new Status(true, "升级网站栏目信息成功.");
	// } catch (Throwable e) {
	// String title = "升级网站栏目信息出错! ";
	//
	// if (log.isDebugEnabled())
	// log.debugf("%s %s", title, ExceptionUtil.msg(e));
	//
	// return new Status(false, title + ExceptionUtil.msg(e));
	// }
	// }

	//
	// @At("/config/validateSystems/*")
	// public synchronized Status validateSystems(String softID) {
	// log.debugf("验证业务系统...");
	// try {
	// security.checkLoginUserOwnRole(SecurityManager.ROLE_DP_SUPPORT);
	//
	// bizEngine.validateSystems(getSoft(softID));
	//
	// log.debugf("验证业务系统成功.");
	// return new Status(true, "验证业务系统成功.");
	// } catch (Throwable e) {
	// String title = "验证业务系统出错! ";
	//
	// if (log.isDebugEnabled())
	// log.debugf("%s %s", title, ExceptionUtil.msg(e));
	//
	// return new Status(false, title + ExceptionUtil.msg(e) + " 详情请查看错误日志！");
	// }
	// }
	//
	// @At("/config/exportToJson")
	// @Ok("void")
	// public synchronized void exportToJson(@Param("soft") String softID, @Param("date") String dateStr, @Param("includeUpload") Boolean includeUpload) {
	// log.debugf("导出业务数据... [softID: %s, date: %s]", softID, dateStr);
	// String folder = null;
	// try {
	// security.checkLoginUserOwnRole(SecurityManager.ROLE_DP_SUPPORT);
	//
	// // 导出数据到文件夹并压缩成ZIP文件
	// Date date;
	// if (!Str.isEmpty(dateStr)) {
	// date = Dates.parse(dateStr);
	// } else {
	// date = Dates.getToday();
	// }
	//
	// folder = appconfig.getLogsDir() + File.separator + "data" + File.separator + "data" + Dates.formatDate(new Date(), "yyMMddHHmm") + "(" + Dates.formatDate(date, "yyMMdd") + ")";
	// String zip = folder + ".zip";
	// File zipfile = new File(zip);
	// Files.deleteDir(new File(folder));
	// Files.deleteFile(zipfile);
	// log.tracef("准备临时文件夹和ZIP文件 [folder: %s, zip: %s]", folder, zip);
	//
	// bizEngine.exportToJson(getSoft(softID), folder, Expr.ge(LibConst.F_UPDATED, date));
	//
	// if (includeUpload) {
	// String destUploadFolder = folder + File.separator + "upload";
	// File uploadDir = new File(Demsy.contextDir + File.separator + "upload");
	// BackupUtils.backup(uploadDir, Dates.formatDate(date, BackupUtils.dateFormat), uploadDir.getAbsolutePath(), new File(destUploadFolder).getAbsolutePath());
	// }
	//
	// Zips.zip(folder, zip);
	// log.trace("打包目录到ZIP文件成功.");
	//
	// // 发送文件到客户端浏览器
	// Http.write(zipfile);
	//
	// // 清楚临时文件夹和文件
	// // Files.deleteFile(zipfile);
	// log.debugf("导出业务数据成功.");
	// } catch (Throwable e) {
	// String title = "导出业务数据出错! ";
	//
	// if (log.isDebugEnabled())
	// log.debugf("%s %s", title, ExceptionUtil.msg(e));
	//
	// throw new RuntimeException(ExceptionUtil.msg(e));
	// } finally {
	// Files.deleteDir(new File(folder));
	// }
	// }
	//
	// @At("/config/importFromJson")
	// public synchronized Status importFromJson(@Param("importDataToSoft") String softID, @Param("importDataFromZip") String importDataFromZip) {
	//
	// log.debugf("导入业务数据... [softID: %s, importDataFromZip: %s]", softID, importDataFromZip);
	// try {
	// security.checkLoginUserOwnRole(SecurityManager.ROLE_DP_SUPPORT);
	//
	// // 准备文件夹和ZIP文件
	// String folder = appconfig.getTempDir() + File.separator + "data" + new Date().getTime();
	// String zipFilename = Demsy.contextDir + importDataFromZip;
	// Files.deleteDir(new File(folder));
	//
	// // 解压数据文件到目录
	// Zips.unzip(zipFilename, folder + File.separator, true);
	//
	// // 导入数据到数据库
	// bizEngine.importFromJson(getSoft(softID), folder);
	// copyUploadDir(new File(folder));
	//
	// // 清除文件夹
	// Files.deleteDir(new File(folder));
	//
	// log.debugf("导入业务数据成功.");
	// return new Status(true, "导入业务数据成功.");
	// } catch (Throwable e) {
	// String title = "导入业务数据出错! ";
	//
	// if (log.isDebugEnabled())
	// log.debugf("%s %s", title, ExceptionUtil.msg(e));
	//
	// return new Status(false, title + ExceptionUtil.msg(e) + " 详情请查看错误日志！");
	// }
	// }
	//
	// protected void copyUploadDir(File ffolder) throws IOException {
	// File[] files = ffolder.listFiles();
	//
	// for (File file : files) {
	// if (file.isDirectory()) {
	// if (file.getName().equals("upload")) {
	// Files.copyDir(file, new File(Demsy.contextDir + File.separator + file.getName()));
	// } else {
	// copyUploadDir(file);
	// }
	// }
	// }
	// }
	//
	// /**
	// * 升级模块菜单
	// *
	// * @return
	// */
	// @At("/config/patch")
	// public Status patch(@Param("filePath") String filePath) {
	// log.debugf("升级DEMSY平台... [filePath: %s]", filePath);
	// try {
	// security.checkLoginUserOwnRole(SecurityManager.ROLE_DP_SUPPORT);
	//
	// PatchTask task = null;
	// if (Str.isEmpty(filePath)) {
	// task = new PatchTask(null, null, true);
	// } else {
	// if (Zips.getType(filePath) == Zips.UNKNOWN) {
	// throw new DemsyException("升级失败! 非法文件类型[%s]", filePath);
	// }
	// task = new PatchTask(contextDir + filePath, contextDir + "/", true);
	// }
	// task.setDaemon(false);
	// task.start();
	//
	// log.debugf("升级DEMSY平台结束. [filePath: %s]", filePath);
	// } catch (Throwable e) {
	// String title = "升级DEMSY平台失败! \n错误详情";
	//
	// if (log.isDebugEnabled())
	// log.debugf("%s %s", title, ExceptionUtil.msg(e));
	//
	// return new Status(false, title + ExceptionUtil.msg(e));
	// }
	//
	// return new Status(true, "正在升级DEMSY平台... 请稍后刷新你的页面.");
	// }
	//
	// static class PatchTask extends Thread {
	//
	// protected String targetDir;
	//
	// protected String fileName;
	//
	// protected boolean restart;
	//
	// private PatchTask(String fileName, String targetDir, boolean r) {
	// this.fileName = fileName;
	// this.targetDir = targetDir;
	// this.restart = r;
	// }
	//
	// private void execute() {
	// try {
	// Cocit.me().setUpgradding(true);
	// if (!StringUtil.isEmpty(fileName))
	// ZipUtil.unzip(fileName, targetDir, true);
	// if (restart) {
	// // String cmd = contextDir + "/../../DEMSY.bat";
	// // File cmdFile = new File(cmd);
	// // if (!cmdFile.exists() || cmdFile.isDirectory()) {
	// // log.info("系统不能自动重新启动! [" + cmd + " 不存在]");
	// // return;
	// // }
	// log.info("系统正在重新启动...");
	// try {
	// System.exit(0);
	// } catch (Throwable e) {
	// log.errorf("系统退出失败！%s", ExceptionUtil.msg(e));
	// }
	// // runNativeCommand(cmd);
	// }
	// } catch (Throwable e) {
	// log.errorf("系统自动重启失败! %s", ExceptionUtil.msg(e));
	// } finally {
	// Cocit.me().setUpgradding(false);
	// }
	// }
	//
	// 
	// public void run() {
	// super.run();
	// this.execute();
	// }
	//
	// protected Integer runNativeCommand(String command) {
	// String[] cmd = null;
	// String[] args = new String[2];
	// Integer result = null;
	// args[0] = command;
	//
	// try {
	// // with this variable will be done the swithcing
	// String osName = System.getProperty("os.name");
	//
	// // only will work with Windows NT
	// if (osName.equals("Windows NT")) {
	// if (cmd == null) {
	// cmd = new String[args.length + 2];
	// }
	// cmd[0] = "cmd.exe";
	// cmd[1] = "/C";
	// for (int i = 0; i < args.length; i++) {
	// cmd[i + 2] = args[i];
	// }
	// } else if (osName.equals("Windows 95")) { // only will work
	// // with Windows 95
	// if (cmd == null) {
	// cmd = new String[args.length + 2];
	// }
	// cmd[0] = "command.com";
	// cmd[1] = "/C";
	// for (int i = 0; i < args.length; i++) {
	// cmd[i + 2] = args[i];
	// }
	// } else if (osName.equals("Windows 2003")) { // only will work
	// // with Windows 2003
	// if (cmd == null) {
	// cmd = new String[args.length + 2];
	// }
	// cmd[0] = "cmd.exe";
	// cmd[1] = "/C";
	//
	// for (int i = 0; i < args.length; i++) {
	// cmd[i + 2] = args[i];
	// }
	// } else if (osName.equals("Windows 2000")) { // only will work
	// // with Windows 2000
	// if (cmd == null) {
	// cmd = new String[args.length + 2];
	// }
	// cmd[0] = "cmd.exe";
	// cmd[1] = "/C";
	//
	// for (int i = 0; i < args.length; i++) {
	// cmd[i + 2] = args[i];
	// }
	// } else if (osName.equals("Windows XP")) { // only will work
	// // with Windows XP
	// if (cmd == null) {
	// cmd = new String[args.length + 2];
	// }
	// cmd[0] = "cmd.exe";
	// cmd[1] = "/C";
	//
	// for (int i = 0; i < args.length; i++) {
	// cmd[i + 2] = args[i];
	// }
	// } else if (osName.equals("Linux")) {
	// if (cmd == null) {
	// cmd = new String[3];
	// }
	// cmd[0] = "/bin/sh";
	// cmd[1] = "-c";
	// cmd[2] = args[0] + " " + args[1];
	// } else { // try this...
	// cmd = args;
	// }
	//
	// Runtime rt = Runtime.getRuntime();
	//
	// // Executes the command
	// rt.exec(cmd);
	// } catch (Exception x) {
	// x.printStackTrace();
	// }
	//
	// return result;
	// }
	// }
}
