package com.lyric.android.library.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;

public class CloseUtils {

	private CloseUtils() {
	}

    /**
     * 关闭输入流
     * @param inputStream InputStream
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
     * @param outputStream OutputStream
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
	 * @param randomAccessFile RandomAccessFile
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
     * @param writer Writer
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
     * @param reader Reader
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
