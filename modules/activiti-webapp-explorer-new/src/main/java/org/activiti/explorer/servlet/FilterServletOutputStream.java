package org.activiti.explorer.servlet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class FilterServletOutputStream extends ServletOutputStream {

	private DataOutputStream stream;
	
	@SuppressWarnings("unused")
	private WriteListener writeListener;

	public FilterServletOutputStream(OutputStream output) {
		stream = new DataOutputStream(output);
	}

	public void write(int b) throws IOException {
		stream.write(b);
	}

	public void write(byte[] b) throws IOException {
		stream.write(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		stream.write(b, off, len);
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		this.writeListener = writeListener;
	}

	@Override
	public boolean isReady() {
		return true;
	}
}