package br.jus.tjba.jcrdownload.jcrdownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TemporaryFileInputStream extends FileInputStream {
	
	private File file;

	public TemporaryFileInputStream(File file) throws FileNotFoundException {
		super(file);
		this.file = file;
	}

	public TemporaryFileInputStream(String name) throws IOException {
		this(createNewFile(name));
	}

	public File getFile() {
		return file;
	}
	
	public void close() throws IOException {
		try {
			super.close();
		} finally {
			this.file.delete();
		}
	}
	
	private static File createNewFile(String name) throws IOException {
		File file = new File(name);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		return file;
	}
}