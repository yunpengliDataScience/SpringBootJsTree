package org.dragon.yunpeng.jstree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class DataInitializer implements ApplicationRunner {

	@Autowired
	private DataSource dataSource;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try (Connection connection = dataSource.getConnection();
				Statement stmt1 = connection.createStatement();
				ResultSet rsLV1Symb = stmt1.executeQuery("SELECT COUNT(*) AS count FROM LV1_SYMB");

				Statement stmt2 = connection.createStatement();
				ResultSet rsLV2Symb = stmt2.executeQuery("SELECT COUNT(*) AS count FROM LV2_SYMB");

				Statement stmt3 = connection.createStatement();
				ResultSet rsLV3Symb = stmt3.executeQuery("SELECT COUNT(*) AS count FROM LV3_SYMB");

				Statement stmt4 = connection.createStatement();
				ResultSet rsLV4Symb = stmt4.executeQuery("SELECT COUNT(*) AS count FROM LV4_SYMB");

		) {

			if (rsLV1Symb.next() && rsLV1Symb.getInt("count") == 0 && rsLV2Symb.next() && rsLV2Symb.getInt("count") == 0
					&& rsLV3Symb.next() && rsLV3Symb.getInt("count") == 0 && rsLV4Symb.next()
					&& rsLV4Symb.getInt("count") == 0) {
				ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false,
						"UTF-8", new ClassPathResource("data-h2.sql"));

				resourceDatabasePopulator.execute(dataSource);
			}
		}
	}
}