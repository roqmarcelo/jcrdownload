package br.jus.tjba.jcrdownload.jcrdownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PutMethod;

public class Resource {

	public static long getResourceSize(String uri, HttpClient http) throws HttpException, IOException {
		HeadMethod head = new HeadMethod(uri);
		try {
			int resp = http.executeMethod(head);
			if (resp == 404) {
				return -1L;
			}
			if (resp == 200) {
				Header length = head.getResponseHeader("Content-Length");
				return length == null ? -1L : Long.parseLong(length.getValue());
			}
			throw new HttpException(String.format("Retorno do servidor inválido %d", new Object[] { Integer.valueOf(resp) }));
		} finally {
			head.releaseConnection();
		}
	}

	public static int putResource(String uri, byte[] data, Range range, String mimetype, HttpClient http)
			throws HttpException, IOException {
		ByteArrayRequestEntity entity = new ByteArrayRequestEntity(Arrays.copyOf(data, range.getLength()), mimetype);

		PutMethod put = new PutMethod(uri);
		put.setRequestEntity(entity);
		put.setRequestHeader("Content-Range", range.toContentRange());
		put.setRequestHeader("Content-Length", Long.toString(range.getLength()));
		try {
			return http.executeMethod(put);
		} finally {
			put.releaseConnection();
		}
	}

	public static InputStream createStreamAfterPos(File file, long pos) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		stream.skip(pos);
		return stream;
	}

	public static int getResource(String uri, HttpClient http) throws HttpException, IOException {
		GetMethod get = new GetMethod(uri);
		return http.executeMethod(get);
	}
	
	public static String getResourceURI(String url, String path) {
		return String.format("%s/%s", new Object[] { url, path });
	}
}