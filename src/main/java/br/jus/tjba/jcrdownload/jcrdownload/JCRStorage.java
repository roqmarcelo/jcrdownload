package br.jus.tjba.jcrdownload.jcrdownload;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.io.FileUtils;

public class JCRStorage {

	private static final String DEFAULT_CONTENT_TYPE = "application/pdf";
	
	private static final char FILE_SEPARATOR = '/';
	
	private final JCRConfiguration configuration;
	
	public JCRStorage(final JCRConfiguration configuration) {
		this.configuration = configuration;
	}

	public void send() {
		try (Stream<String> jcrfiles = Files.lines(Paths.get(configuration.getFilePath()))) {
			jcrfiles.forEach(this::performSend);
		} catch (IOException e) {
			System.out.println("I/O Error: " + e.getMessage());
		}
	}
	
	private void performSend(String jcrfile) {
		final String retrieveUri = configuration.getFromUrl() + FILE_SEPARATOR + jcrfile;
		final String sendUri = configuration.getToUrl() + FILE_SEPARATOR + jcrfile;
		
		if (checkResourceAlreadyExists(sendUri, getHttpClient(configuration.getToUrl()))) {
			System.out.println("Resource already exists: " + jcrfile);
			return;
		}
		
		try (TemporaryFileInputStream temporaryFile = new TemporaryFileInputStream(jcrfile)) {
			URL jcrUrl = new URL(retrieveUri);
			
			FileUtils.copyURLToFile(jcrUrl, temporaryFile.getFile());

			sendPut(sendUri, temporaryFile, DEFAULT_CONTENT_TYPE);
			
			System.out.println("Resource succesfully sent: " + jcrfile);
		} catch (IOException e) {
			System.out.println("I/O Error: " + e.getMessage());
		}
	}
	
	private boolean checkResourceAlreadyExists(String uri, HttpClient client) {
		try {
			int statusCode = Resource.getResource(uri, client);
			
			if (statusCode == 200 || statusCode == 409) {
				System.out.println("Response status code: " + statusCode);
				return true;
			}
			
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	private void sendPut(String uri, TemporaryFileInputStream content, String contentType) throws HttpException, IOException {
		long startPos = 0L;
		
		long fileSize = content.getFile().length();
		
		int chunkSize = configuration.getChunkSize();
		
		try (InputStream stream = Resource.createStreamAfterPos(content.getFile(), startPos)) {
			byte[] buffer = new byte[chunkSize];
			
			for (long pos = startPos; pos < fileSize; pos += chunkSize) {
				int read = stream.read(buffer);
				int resp = Resource.putResource(uri, buffer, new Range(pos, read, fileSize), contentType, getHttpClient(configuration.getToUrl()));
				
				System.out.println("Response status code: " + resp);
			}
		}
	}

	private HttpClient getHttpClient(String url) {
		return new Connection(url).getHttpClient();
	}
}