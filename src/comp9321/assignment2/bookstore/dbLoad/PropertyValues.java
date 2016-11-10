package comp9321.assignment2.bookstore.dbLoad;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyValues {

	InputStream inputStream;

	public String getPropValues(String key) throws IOException {
		String value = new String();

		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(
					propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			}

			value = prop.getProperty(key);

		} catch (Exception e) {

		} finally {
			inputStream.close();
		}
		return value;
	}

}
