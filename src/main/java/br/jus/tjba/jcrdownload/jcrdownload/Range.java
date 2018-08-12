package br.jus.tjba.jcrdownload.jcrdownload;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Range {
	
	private final long start;
	private final long end;
	private final long total;
	private final int length;

	public Range(long start, int length, long total) {
		this.start = start;
		this.end = (start + length);
		this.length = length;
		this.total = total;
	}

	public long getStart() {
		return this.start;
	}

	public long getEnd() {
		return this.end;
	}

	public long getTotal() {
		return this.total;
	}

	public int getLength() {
		return this.length;
	}

	public boolean isLast() {
		return this.end == this.total;
	}

	public String toContentRange() {
		return String.format("bytes %d-%d/%d",
				new Object[] { Long.valueOf(this.start), Long.valueOf(this.end), Long.valueOf(this.total) });
	}

	public static Range parse(String str) throws ParseException {
		if (str == null) {
			throw new ParseException("O parametro não deve ser nulo", 0);
		}
		Pattern p = Pattern.compile("bytes (\\d+)-(\\d+)/(\\d+)");
		Matcher m = p.matcher(str);
		if (!m.matches()) {
			throw new ParseException("Formato inválido", 0);
		}
		try {
			long start = Long.parseLong(m.group(1));
			long end = Long.parseLong(m.group(2));
			long total = Long.parseLong(m.group(3));

			return new Range(start, (int) (end - start), total);
		} catch (IndexOutOfBoundsException e) {
			throw new ParseException("Formato inválido", 0);
		}
	}

	public String toString() {
		return String.format("Range {start = %d, end = %d, total = %d}",
				new Object[] { Long.valueOf(this.start), Long.valueOf(this.end), Long.valueOf(this.total) });
	}
}
