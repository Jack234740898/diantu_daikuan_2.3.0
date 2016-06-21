package com.img.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class StreamUtil {

	/**
	 * 根据文件路径new�?个文件输入流
	 */
	public static synchronized InputStream loadStreamFromFile(
			String filePathName) throws FileNotFoundException, IOException {
		return new FileInputStream(filePathName);
	}

	/**
	 * 将String保存到指定的文件�?
	 */
	public static void saveStringToFile(String text, String filePath)
			throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(
				text.getBytes("UTF-8"));
		saveStreamToFile(in, filePath);
	}

	/**
	 * @author songhai 将String保存到指定的文件�?
	 */
	public static String loadStringFromFile(String filePath) throws IOException {
		return getStringFromInputStream(new FileInputStream(filePath));
	}

	/**
	 * 将InputStream保存到指定的文件�?
	 */
	public static synchronized void saveStreamToFile(InputStream in,
			String filePath) throws IOException {
		try {
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			} else {
				File parent = file.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
				file.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(file);
			copyStream(in, fos);
			fos.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 从输入流里面读出byte[]数组
	 */
	public static byte[] readStream(InputStream in) throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = in.read(buf)) != -1) {
			byteOut.write(buf, 0, len);
		}
		byteOut.close();
		return byteOut.toByteArray();
	}

	/**
	 * 从输入流里面记载String
	 */
	public static String loadStringFromStream(InputStream in)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(16384);
		copyStream(in, baos);
		baos.close();
		return baos.toString("UTF-8");
	}

	/**
	 * 从输入流里面读出每行文字
	 */
	public static Vector loadStringLinesFromStream(InputStream in)
			throws IOException {
		InputStreamReader reader = new InputStreamReader(in, "UTF-8");
		BufferedReader br = new BufferedReader(reader);
		String row;
		Vector<String> lines = new Vector<String>();
		while ((row = br.readLine()) != null) {
			lines.add(row);
		}
		return lines;
	}

	/**
	 * 拷贝�?
	 */
	public static void copyStream(InputStream in, OutputStream out)
			throws IOException {
		BufferedInputStream bin = new BufferedInputStream(in);
		BufferedOutputStream bout = new BufferedOutputStream(out);

		byte[] buffer = new byte[4096];

		while (true) {
			int doneLength = bin.read(buffer);
			if (doneLength == -1)
				break;
			bout.write(buffer, 0, doneLength);
		}
		bout.flush();
	}

	/**
	 * 刷新输入�?
	 */
	public static ByteArrayInputStream flushInputStream(InputStream in)
			throws IOException {
		BufferedInputStream bin = new BufferedInputStream(in);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bout = new BufferedOutputStream(baos);

		byte[] buffer = new byte[4096];
		while (true) {
			int doneLength = bin.read(buffer);
			if (doneLength == -1)
				break;
			bout.write(buffer, 0, doneLength);
		}
		bout.flush();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		return bais;
	}

	/**
	 * 将输入流转化为字符串输出
	 */
	public static final String getStringFromInputStream(InputStream is) {

		return getStringFromInputStream(is, "UTF-8");
	}

	public static final String getStringFromInputStream(InputStream is,
			String code) {
		if (is != null) {
			BufferedReader br;
			StringBuffer buf = new StringBuffer();
			try {
				br = new BufferedReader(new InputStreamReader(is, code));
				String rs = "";
				while ((rs = br.readLine()) != null) {
					buf.append(rs);
				}
				br.close();
			} catch (UnsupportedEncodingException e) {
			} catch (IOException e) {
			}
			return buf.toString();
		}
		return "";
	}

	/**
	 * 获得指定文件的byte数组
	 */
	private byte[] getBytes(String filePath) {
		if(!StringUtil.checkStr(filePath))
			return null;
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * 根据byte数组，生成文�?
	 */
	public static void getFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + "\\" + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
