package edu.umd.rhsmith.diads.meater.core.config.sql;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.sql.SqlInfo;
import edu.umd.rhsmith.diads.meater.core.app.sql.SqlInfoSource;
import edu.umd.rhsmith.diads.meater.core.app.sql.SqlLoadException;

public class XmlSqlInfoSource implements SqlInfoSource {
	@Override
	public SqlInfo getSqlInfo(String name) throws SqlLoadException {
		SqlConfig sql = new SqlConfig();

		try {
			XMLConfiguration xml = new XMLConfiguration(name + ".xml");
			sql.loadConfigurationFrom(xml);
		} catch (ConfigurationException e) {
			throw new SqlLoadException(String
					.format(MSG_ERR_BAD_FILE_FMT, name), e);
		} catch (MEaterConfigurationException e) {
			throw new SqlLoadException(String
					.format(MSG_ERR_BAD_FILE_FMT, name), e);
		}

		return sql;
	}

	private static final String MSG_ERR_BAD_FILE_FMT = "Invalid SQL configuration file %s";
}
