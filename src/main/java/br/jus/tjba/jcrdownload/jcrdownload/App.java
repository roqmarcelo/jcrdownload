package br.jus.tjba.jcrdownload.jcrdownload;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class App {
	
	private static final JCRConfiguration configuration;
	
	static {
		configuration = new JCRConfiguration();
		
		Authenticator.setDefault(new CustomAuthenticator());
	}
	
	public static void main(String[] args) {
		new JCRStorage(configuration).send();
	}
	
	static class CustomAuthenticator extends Authenticator {
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(configuration.getUsername(), configuration.getPassword().toCharArray());
		}
	}
}