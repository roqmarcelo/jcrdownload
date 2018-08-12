package br.jus.tjba.jcrdownload.jcrdownload;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class JCRConfiguration {

	private final String filePath;
	private final String username;
	private final String password;
	private final String fromUrl;
	private final String toUrl;
	private final int chunkSize;

	private static Properties properties = new Properties();

	public JCRConfiguration() {
		final String workingDir = System.getProperty("user.dir");

		try (FileInputStream inputStream = FileUtils.openInputStream(FileUtils.getFile(workingDir + "/jcr-storage.properties"))) {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		

		this.filePath = workingDir + "/jcr-files";
		this.username = properties.getProperty("jcr.username");
		this.password = properties.getProperty("jcr.password");
		this.fromUrl = properties.getProperty("jcr.from.url");
		this.toUrl = properties.getProperty("jcr.to.url");
		this.chunkSize = Integer.valueOf(properties.getProperty("jcr.chunkSize"));
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFromUrl() {
		return fromUrl;
	}

	public String getToUrl() {
		return toUrl;
	}

	public String getFilePath() {
		return filePath;
	}

	public int getChunkSize() {
		return chunkSize;
	}
}