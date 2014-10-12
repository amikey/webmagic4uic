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
	 * private static String dbDriver = "com.mysql.jdbc.Driver"; // 与本地设置相同
	 * private static String dbUrl =
	 * "jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_uicfoodsafety"; //
	 * app_yanzel为新浪app数据库名称，开通mysql服务后，通过[服务管理]-〉[MySql]->[管理MySql]中，查看数据库名称
	 * private static String dbUser = "lwx2lzxzj2"; //
	 * 为[应用信息]->[汇总信息]->[key]中的access key private static String dbPassword =
	 * "30xj5ijjxy13wx44z3iix12l54yki2hywk1hlkmk"; //
	 * 为[应用信息]->[汇总信息]->[key]中的secret key
	 */

	private static String dbDriver = "com.mysql.jdbc.Driver"; // 与本地设置相同
	private static String dbUrl = "jdbc:mysql://localhost:3306/fsa_db"; // app_yanzel为新浪app数据库名称，开通mysql服务后，通过[服务管理]-〉[MySql]->[管理MySql]中，查看数据库名称
	private static String dbUser = "root"; // 为[应用信息]->[汇总信息]->[key]中的access key
	private static String dbPassword = "root"; // 为[应用信息]->[汇总信息]->[key]中的secret
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

		// 遍历所有结果，输出到控制台，上面例子中的"author"、"name"、"readme"都是一个key，其结果则是对应的value
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

	/* 插入数据记录，并输出插入的数据记录数 */
	public static void insert(FoodMateRepo repo) {

		conn = getConnection(); // 首先要获取连接，即连接到数据库

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
					+ repo.getUrl() + "')"; // 插入数据的sql语句

			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象

			int count = st.executeUpdate(sql); // 执行插入操作的sql语句，并返回插入数据的个数

			System.out.println("向staff表中插入 " + count + " 条数据"); // 输出插入操作的处理结果

			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			System.out.println("插入数据失败" + e.getMessage());
		}
	}

	/* 更新符合要求的记录，并返回更新的记录数目 */
	public static void update() {
		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "update staff set wage='2200' where name = 'lucy'";// 更新数据的sql语句

			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量

			int count = st.executeUpdate(sql);// 执行更新操作的sql语句，返回更新数据的个数

			System.out.println("staff表中更新 " + count + " 条数据"); // 输出更新操作的处理结果

			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			System.out.println("更新数据失败");
		}
	}

	/* 查询数据库，输出符合要求的记录的情况 */
	public static void query() {

		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "select * from staff"; // 查询数据的sql语句
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量

			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
			System.out.println("最后的查询结果为：");
			while (rs.next()) { // 判断是否还有下一个数据

				// 根据字段名获取相应的值
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String sex = rs.getString("sex");
				String address = rs.getString("address");
				String depart = rs.getString("depart");
				String worklen = rs.getString("worklen");
				String wage = rs.getString("wage");

				// 输出查到的记录的各个字段的值
				System.out.println(name + " " + age + " " + sex + " " + address
						+ " " + depart + " " + worklen + " " + wage);

			}
			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			System.out.println("查询数据失败");
		}
	}

	/* 删除符合要求的记录，输出情况 */
	public static void delete() {

		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "delete from staff  where name = 'lili'";// 删除数据的sql语句
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量

			int count = st.executeUpdate(sql);// 执行sql删除语句，返回删除数据的数量

			System.out.println("t_article_info表中删除 " + count + " 条数据\n"); // 输出删除操作的处理结果

			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			System.out.println("删除数据失败");
		}

	}

	/* 获取数据库连接的函数 */
	public static Connection getConnection() {
		Connection con = null; // 创建用于连接数据库的Connection对象
		try {
			Class.forName(dbDriver);// 加载Mysql数据驱动

			con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);// 创建数据连接

		} catch (Exception e) {
			System.out.println("数据库连接失败" + e.getMessage());
		}
		return con; // 返回所建立的数据库连接
	}
}
