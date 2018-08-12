package br.jus.tjba.jcrdownload.jcrdownload;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class Connection {
	private final String url;
	private final String username = "admin";
	private final String password = "admin";
	private final int maxHostConn = 100;

	public Connection(String url) {
		this.url = url;
	}

	public HttpClient getHttpClient() {
		HostConfiguration host = new HostConfiguration();
		host.setHost(this.url);

		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setMaxConnectionsPerHost(host, this.maxHostConn);
		HttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		manager.setParams(params);

		Credentials cred = new UsernamePasswordCredentials(this.username, this.password);
		HttpClient client = new HttpClient(manager);
		client.getState().setCredentials(AuthScope.ANY, cred);
		client.setHostConfiguration(host);
		client.getParams().setAuthenticationPreemptive(true);
		client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);

		return client;
	}
}