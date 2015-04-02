import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import com.jsoft.cocit.util.DateUtil;
import com.jsoft.cocit.util.FileUtil;

public class BackupUtils {
	public static String dateFormat = "yyyyMMddHHmm";

	public static void backup(File file, String backupDate, String srcDirName, String destDirName) throws ParseException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				backup(files[i], backupDate, srcDirName, destDirName);
			}
		} else {
			// 拷贝文件
			String path = file.getAbsolutePath();// 原来的文件

			// 删除垃圾文件
			if (path.indexOf(File.separator + "Thumbs.db") > -1) {
				file.delete();
				return;
			}

			// Mac 文件夹标识
			if (file.getName().startsWith(".")) {
				return;
			}

			// 项目WEB目录标志
			String pathPost = path.substring(srcDirName.length());
			if (pathPost.startsWith(File.separator + "build" + File.separator + "tmp") || //
			        pathPost.startsWith(File.separator + "build" + File.separator + "backup") || //
			        pathPost.startsWith(File.separator + "build" + File.separator + "patch")//
			)//
			{
				return;
			}

			// 备份相关文件
			String distFilePath = path.replace(srcDirName, destDirName);// 拷贝过去的文件
			long today = DateUtil.parse(backupDate, dateFormat).getTime();
			long lastModified = file.lastModified();
			if (lastModified > today) {
				File distFile = new File(distFilePath);
				try {
					if (file.exists()) {
						distFile.getParentFile().mkdirs();
						distFile.createNewFile();
						FileUtil.copy(file, distFile);
					}
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
	}
}
