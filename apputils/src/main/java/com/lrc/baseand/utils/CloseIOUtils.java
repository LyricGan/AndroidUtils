package com.lrc.baseand.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;

public class CloseIOUtils {

	private CloseIOUtils() {
	}

    /**
     * 关闭输入流
     * @param inputStream
     */
    public static void close(InputStream inputStream) {
    	if (inputStream != null) {
    		try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    /**
     * 关闭输出流
     * @param outputStream
     */
	public static void close(OutputStream outputStream) {
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭RandomAccessFile
	 * @param randomAccessFile
	 */
    public static void close(RandomAccessFile randomAccessFile) {
        if (randomAccessFile != null) {
        	try {
				randomAccessFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
    
    /**
     * 关闭Writer
     * @param writer
     */
    public static void close(Writer writer) {
        if (writer != null) {
        	try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    /**
     * 关闭Reader
     * @param reader
     */
    public static void close(Reader reader) {
        if (reader != null) {
        	try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
