package mywebmagic;

import java.sql.*;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class FoodMatePipeline implements Pipeline {

	/*
	 * private static String dbDriver = "com.mysql.jdbc.Driver"; // �뱾��������ͬ
	 * private static String dbUrl =
	 * "jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_uicfoodsafety"; //
	 * app_yanzelΪ����app���ݿ����ƣ���ͨmysql�����ͨ��[�������]-��[MySql]->[����MySql]�У��鿴���ݿ�����
	 * private static String dbUser = "lwx2lzxzj2"; //
	 * Ϊ[Ӧ����Ϣ]->[������Ϣ]->[key]�е�access key private static String dbPassword =
	 * "30xj5ijjxy13wx44z3iix12l54yki2hywk1hlkmk"; //
	 * Ϊ[Ӧ����Ϣ]->[������Ϣ]->[key]�е�secret key
	 */

	private static String dbDriver = "com.mysql.jdbc.Driver"; // �뱾��������ͬ
	private static String dbUrl = "jdbc:mysql://localhost:3306/fsa_db"; // app_yanzelΪ����app���ݿ����ƣ���ͨmysql�����ͨ��[�������]-��[MySql]->[����MySql]�У��鿴���ݿ�����
	private static String dbUser = "root"; // Ϊ[Ӧ����Ϣ]->[������Ϣ]->[key]�е�access key
	private static String dbPassword = "root"; // Ϊ[Ӧ����Ϣ]->[������Ϣ]->[key]�е�secret
												// key

	private static Connection conn;

	private static Statement st;

	public void process(ResultItems resultItems, Task task) {
		System.out.println("get page: " + resultItems.getRequest().getUrl());

		/*
		 * try { Class.forName(this.dbDriver); } catch (ClassNotFoundException
		 * e) { e.printStackTrace(); }
		 * 
		 * try { this.conn = DriverManager.getConnection(dbUrl, dbUser,
		 * dbPassword); } catch (SQLException e) { e.printStackTrace();
		 * 
		 * }
		 */

		// �������н�������������̨�����������е�"author"��"name"��"readme"����һ��key���������Ƕ�Ӧ��value
		for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
			FoodMateRepo value = (FoodMateRepo) entry.getValue();
			System.out.println(entry.getKey() + ":\t" + entry.getValue()
					+ value.getSourcetitle());
			String key = entry.getKey();
			insert(value);
		}

		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* �������ݼ�¼���������������ݼ�¼�� */
	public static void insert(FoodMateRepo repo) {

		conn = getConnection(); // ����Ҫ��ȡ���ӣ������ӵ����ݿ�

		try {
			String sql = "INSERT INTO t_article_info(url_md5, title, time,source_name, content,url)"
					+ " VALUES ('"
					+ MD5Generator.getMD5(repo.getUrl().getBytes())
					+ "', '"
					+ repo.getTitle()
					+ "', '"
					+ repo.getTime()
					+ "', '"
					+ repo.getSourcetitle()
					+ "','"
					+ repo.getContent()
					+ "','"
					+ repo.getUrl() + "')"; // �������ݵ�sql���

			st = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����

			int count = st.executeUpdate(sql); // ִ�в��������sql��䣬�����ز������ݵĸ���

			System.out.println("��staff���в��� " + count + " ������"); // �����������Ĵ�����

			conn.close(); // �ر����ݿ�����

		} catch (SQLException e) {
			System.out.println("��������ʧ��" + e.getMessage());
		}
	}

	/* ���·���Ҫ��ļ�¼�������ظ��µļ�¼��Ŀ */
	public static void update() {
		conn = getConnection(); // ͬ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		try {
			String sql = "update staff set wage='2200' where name = 'lucy'";// �������ݵ�sql���

			st = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����st���ֲ�����

			int count = st.executeUpdate(sql);// ִ�и��²�����sql��䣬���ظ������ݵĸ���

			System.out.println("staff���и��� " + count + " ������"); // ������²����Ĵ�����

			conn.close(); // �ر����ݿ�����

		} catch (SQLException e) {
			System.out.println("��������ʧ��");
		}
	}

	/* ��ѯ���ݿ⣬�������Ҫ��ļ�¼����� */
	public static void query() {

		conn = getConnection(); // ͬ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		try {
			String sql = "select * from staff"; // ��ѯ���ݵ�sql���
			st = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����st���ֲ�����

			ResultSet rs = st.executeQuery(sql); // ִ��sql��ѯ��䣬���ز�ѯ���ݵĽ����
			System.out.println("���Ĳ�ѯ���Ϊ��");
			while (rs.next()) { // �ж��Ƿ�����һ������

				// �����ֶ�����ȡ��Ӧ��ֵ
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String sex = rs.getString("sex");
				String address = rs.getString("address");
				String depart = rs.getString("depart");
				String worklen = rs.getString("worklen");
				String wage = rs.getString("wage");

				// ����鵽�ļ�¼�ĸ����ֶε�ֵ
				System.out.println(name + " " + age + " " + sex + " " + address
						+ " " + depart + " " + worklen + " " + wage);

			}
			conn.close(); // �ر����ݿ�����

		} catch (SQLException e) {
			System.out.println("��ѯ����ʧ��");
		}
	}

	/* ɾ������Ҫ��ļ�¼�������� */
	public static void delete() {

		conn = getConnection(); // ͬ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		try {
			String sql = "delete from staff  where name = 'lili'";// ɾ�����ݵ�sql���
			st = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����st���ֲ�����

			int count = st.executeUpdate(sql);// ִ��sqlɾ����䣬����ɾ�����ݵ�����

			System.out.println("t_article_info����ɾ�� " + count + " ������\n"); // ���ɾ�������Ĵ�����

			conn.close(); // �ر����ݿ�����

		} catch (SQLException e) {
			System.out.println("ɾ������ʧ��");
		}

	}

	/* ��ȡ���ݿ����ӵĺ��� */
	public static Connection getConnection() {
		Connection con = null; // ���������������ݿ��Connection����
		try {
			Class.forName(dbDriver);// ����Mysql��������

			con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);// ������������

		} catch (Exception e) {
			System.out.println("���ݿ�����ʧ��" + e.getMessage());
		}
		return con; // ���������������ݿ�����
	}
}
