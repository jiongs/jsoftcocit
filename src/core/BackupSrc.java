import java.io.File;
import java.util.Date;
import java.util.List;

import org.nutz.lang.Files;

import com.jsoft.cocit.util.DateUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.ZipUtil;

public class BackupSrc {

	public static String userdir = System.getProperty("user.dir") + File.separator;

	public static String sftName = new File(userdir).getName().toUpperCase();

	public static String backupDate = null;

	public static void main(String[] args) {

		// backupDate = Dates.getCurrentDate(dateFormat);
		// backupDate = backupDate.substring(0, 8) + "0101";
		backupDate = "201504020101";

		String backupDirs = "";
		int count = 0;
		for (String str : args) {
			switch (count) {
				case 0:
					sftName = str;
					break;
				case 1:
					backupDate = args[1];
					break;
				default:
					backupDirs += "," + str;
					break;
			}
			count++;
		}
		if (backupDirs.length() > 0)
			backupDirs = backupDirs.substring(1);
		try {
			String srcDirName = userdir;// userdir + "src";
			File srcDir = new File(srcDirName);

			String destDirName = userdir + "build" + File.separator + "tmp" + File.separator + srcDir.getName();
			File backupDir = new File(destDirName);
			Files.deleteDir(backupDir);
			backupDir.mkdirs();
			File destDir = new File(destDirName);

			BackupUtils.backup(srcDir, backupDate, new File(srcDirName).getAbsolutePath(), destDir.getAbsolutePath());

			List<String> srcList = StringUtil.toList(backupDirs, ",");
			for (int i = 0; i < srcList.size(); i++) {
				String bkName = srcList.get(i);
				i++;
				String srcDirName1 = srcList.get(i);

				File srcDir1 = new File(srcDirName1);
				BackupUtils.backup(srcDir1, backupDate, srcDirName1, destDirName + File.separator + bkName);
			}

			String backupName = sftName + "-backup(" + DateUtil.format(new Date(), BackupUtils.dateFormat) + "-" + backupDate + ")";

			String zip = userdir + "build" + File.separator + "backup" + File.separator + backupName + ".zip";
			ZipUtil.zip(destDir.getAbsolutePath(), zip);

			Files.deleteDir(destDir);

			System.out.print(backupName);
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
