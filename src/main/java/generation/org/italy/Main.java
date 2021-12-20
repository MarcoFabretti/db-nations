package generation.org.italy;

import java.sql.*;
import java.util.Scanner;

public class Main {
	private final static String DB_URL = "jdbc:mysql://localhost:3306/dump_nations";
	private final static String DB_USER = "root";
	private final static String DB_PASSWORD = "Marco19651973!";

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("inserire tag della nazione");
		String tag = in.nextLine();
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
			System.out.println(con.isClosed());
			String query = "select c.name, c.country_id, r.name, c2.name \r\n"
					+ "from countries c, continents c2, regions r \r\n"
					+ "where c2.continent_id = r.continent_id and c.region_id = r.region_id and c.name LIKE  ?  \r\n"
					+ "order by c.name;\r\n";

			try (PreparedStatement ps = con.prepareStatement(query)) {
				ps.setString(1, "%" + tag + "%");
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						System.out.print(rs.getString(1) + ", ");
						System.out.print(rs.getInt(2) + ", ");
						System.out.print(rs.getString(3) + ", ");
						System.out.println(rs.getString(4));
					}
				}
			}

			System.out.println("Inserire uno degli ID country precedenti");
			int id = in.nextInt();

			query = "select name" + " from  countries c " + "where country_id like ? ;";
			try (PreparedStatement ps = con.prepareStatement(query)) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						System.out.println("Details for country: " + rs.getString(1));
					}
				}
			}
			query = "select l.`language` \r\n" + "from  country_languages cl, languages l \r\n"
					+ "where l.language_id = cl.language_id and country_id = ?;\r\n";

			try (PreparedStatement ps = con.prepareStatement(query)) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					System.out.print("Languages: ");
					while (rs.next()) {
						System.out.print(rs.getString(1) + " ");
					}
					System.out.println("");
				}
			}

			query = "select  `year` , population , gdp \r\n"
					+ "from  country_stats cs \r\n"
					+ "where country_id = ?\r\n"
					+ "order by `year` desc \r\n"
					+ "limit 1\r\n";

			try (PreparedStatement ps = con.prepareStatement(query)) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					System.out.print("Year: ");
					while (rs.next()) {
						System.out.println(rs.getInt(1));
						System.out.println("Population: " + rs.getString(2));
						System.out.println("GDP: " + rs.getString(3));
					}
				}
			}

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		in.close();
	}
}
