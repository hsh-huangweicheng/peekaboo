import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

public class WOSImport {
	/**
	 * @param args
	 *            AU 作者简称/Multiline AF 作者全称/Multiline TI 标题/Multiline SO 来源（期刊名称） DT
	 *            文献类型 DE 杂志提供的主题词/Multiline ID WOS抽取的关键词/Multiline AB 摘要/Multiline
	 *            C1 作者地址/Multiline RP 通讯作者及地址 EM 电子邮件 FU 基金信息 FX 基金补充信息 CR
	 *            参考文献/Multiline TC 总被引次数 PY 出版年份 DI DOI WC WOS类别 UT 编号
	 */

	private static final String URL = "jdbc:mysql://localhost:3306/gm?characterEncoding=utf8&useSSL=true";
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "121109";

	public static void main(String[] args) {

		System.out.println(System.getProperty("user.dir"));

		Connection conn = null;
		try {
			// ����sql
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);

			String insertString = "insert into wos values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement insertPS = conn.prepareStatement(insertString);

			ArrayList<String> docIdList = new ArrayList<String>();

			Statement sta = conn.createStatement();
			ResultSet rs = sta.executeQuery("select UT from wos");
			while (rs.next()) {
				docIdList.add(rs.getString(1));
			}
			rs.close();

			System.out.println("正在读取文件列表...");

			String[] titles = { "AU", "AF", "TI", "SO", "DT", "DE", "ID", "AB", "C1", "RP", "EM", "FU", "FX", "CR",
					"TC", "PY", "DI", "WC", "UT" };

			try {
				Vector<File> files = new Vector<File>();

				String dir = "F:\\毕业论文\\wos数据";
				// String dir = System.getProperty("user.dir") + "\\data\\articles";
				collection(dir, files);

				System.out.println("正在导入文件...");
				for (int i = 0; i < files.size(); i++) {

					System.out.println("共有" + files.size() + "个文件,正在导入第" + (i + 1) + "个文件...");

					File file = files.elementAt(i);
					String encoding = "UTF-8";
					String strLine;

					InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
					BufferedReader bt = new BufferedReader(reader);

					int count = 0;
					int duplicates = 0;

					String[] values = new String[titles.length];
					boolean reread = true;
					strLine = "";
					long timestamp = System.currentTimeMillis();
					while (true) {
						if (reread)
							if ((strLine = bt.readLine()) == null)
								break;

						if (strLine == null)
							break;

						if (strLine.equals("PT J") || strLine.equals("PT S")) { // ||strLine.equals("PT B")
							for (int j = 0; j < values.length; j++)
								values[j] = "";
							reread = true;
							continue;
						} else {
							int j = 0;
							for (; j < titles.length; j++) {
								if (strLine.startsWith(titles[j] + " ")) {
									values[j] = strLine.replaceFirst(titles[j] + " ", "");

									while ((strLine = bt.readLine()) != null && strLine.startsWith("   ")) {

										values[j] = values[j] + "::" + strLine.trim();
									}

									break;
								}
							}

							if (j < titles.length)
								reread = false;
							else
								reread = true;

							if (strLine != null && !strLine.equals("ER"))// if false,then start a new record.
								continue;
							else
								reread = true;

							if (docIdList.contains(values[values.length - 1])) {
								duplicates++;
								continue;
							} else {
								count++;
								docIdList.add(new String(values[values.length - 1]));
							}

						}

						if (values[values.length - 1].trim().length() > 1) {

							try {
								for (int j = 0; j < values.length; j++) {
									// System.out.print(j+"|");
									String temp = values[j];
									insertPS.setString(j + 1, temp);
								}
								// System.out.println(insertPS.toString());
								insertPS.addBatch();
								// insertPS.executeUpdate();

							} catch (Exception e) {
								System.out.println(e.toString());
								writetofile(System.getProperty("user.dir") + "\\data\\error.txt", e.toString());
							}
						}

						reread = true;
					}

					try {
						int[] counts = insertPS.executeBatch();
					} catch (Exception e) {
						System.out.println(e.toString());
						writetofile(System.getProperty("user.dir") + "\\data\\error.txt", e.toString());
					}
					System.out.println("-------第" + (i + 1) + "个文件共导入记录条数:" + count + "		重复:" + duplicates + "  "
							+ (System.currentTimeMillis() - timestamp) / 1000 + "秒");
					bt.close();
					reader.close();
				}

			} catch (Exception e) {
				System.out.println(e.toString());
			}

			conn.close();
			System.out.println("OVER!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void collection(String filePath, Vector<File> files) {
		ArrayList<String> filelist = new ArrayList<String>();
		// System.out.println(filePath);
		getFiles(filePath, filelist);
		for (String text : filelist) {
			files.add(new File(text));
		}

	}

	public static void getFiles(String filePath, ArrayList<String> filelist) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {

			if (file.isDirectory()) {
				getFiles(file.getAbsolutePath(), filelist);
				// filelist.add(file.getAbsolutePath());
				// System.out.println("��ʾ"+filePath+"��������Ŀ¼�����ļ�"+file.getAbsolutePath());
			} else {
				filelist.add(file.getAbsolutePath());
				// System.out.println("��ʾ"+filePath+"��������Ŀ¼"+file.getAbsolutePath());
			}

		}
	}

	public static void writetofile(String filename, String content) {
		FileWriter writer = null;
		try {
			// ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
			writer = new FileWriter(filename, true);
			writer.write(content + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
