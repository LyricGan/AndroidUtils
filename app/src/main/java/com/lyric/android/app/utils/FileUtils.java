package com.lyric.android.app.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 *
 * @author Lyric Gan
 */
public class FileUtils {
    public final static String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * 文件大小单位
     * <li>BYTE：Byte与Byte的倍数(1)</li>
     * <li>KB：KB与Byte的倍数(1024)</li>
     * <li>MB：MB与Byte的倍数(1048576)</li>
     * <li>GB：GB与Byte的倍数(1073741824)</li>
     */
    public enum Unit {
        BYTE(1), KB(1024), MB(1048576), GB(1073741824);

        int value;

        Unit(int value) {
            this.value = value;
        }
    }

    private FileUtils() {
    }

    /**
     * read file
     *
     * @param filePath the file to be opened for reading.
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws IOException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) throws IOException {
        File file = new File(filePath);
        if (!file.isFile()) {
            return null;
        }
        StringBuilder fileContent = new StringBuilder("");
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } finally {
            closeQuietly(reader);
        }
    }

    public static byte[] readBytes(InputStream in) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } finally {
            closeQuietly(out);
        }
        return out.toByteArray();
    }

    public static byte[] readBytes(InputStream in, long skip, long size) throws IOException {
        ByteArrayOutputStream out = null;
        try {
            if (skip > 0) {
                long skipSize;
                while (skip > 0 && (skipSize = in.skip(skip)) > 0) {
                    skip -= skipSize;
                }
            }
            out = new ByteArrayOutputStream();
            for (int i = 0; i < size; i++) {
                out.write(in.read());
            }
        } finally {
            closeQuietly(out);
        }
        return out.toByteArray();
    }

    public static String readString(InputStream in, String charset) throws IOException {
        if (TextUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        Reader reader = new InputStreamReader(in, charset);
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024];
        int len;
        while ((len = reader.read(buf)) >= 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    public static void writeString(OutputStream out, String str, String charset) {
        if (TextUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(out, charset);
            writer.write(str);
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                closeQuietly(writer);
            }
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        if (!(out instanceof BufferedOutputStream)) {
            out = new BufferedOutputStream(out);
        }
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param content the content to be write
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            closeQuietly(fileWriter);
        }
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param contentList the content list to be write
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (contentList == null || contentList.isEmpty()) {
            return false;
        }
        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            closeQuietly(fileWriter);
        }
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath the file to be opened for writing.
     * @param stream the input stream
     * @return returns true
     * @see #writeFile(String, InputStream, boolean)
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file
     *
     * @param file the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream outputStream = null;
        try {
            makeDirs(file.getAbsolutePath());
            outputStream = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                outputStream.write(data, 0, length);
            }
            outputStream.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            closeQuietly(outputStream);
            closeQuietly(stream);
        }
    }

    /**
     * copy file
     *
     * @param sourceFilePath the source file path
     * @param destFilePath the dest file path
     * @return return true or throws exception
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }

    /**
     * read file to string list, a element of list is a line
     *
     * @param filePath file path
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        if (!file.isFile()) {
            return null;
        }
        List<String> fileContent = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            closeQuietly(reader);
        }
    }

    /**
     * get file name from path, not include suffix
     *
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath file path
     * @return file name from path, not include suffix
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * get file name from path, include suffix
     *
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath file path
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * get folder name from path
     *
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath file path
     * @return folder name from path
     */
    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePos = filePath.lastIndexOf(File.separator);
        return (filePos == -1) ? "" : filePath.substring(0, filePos);
    }

    /**
     * get suffix of file from path
     *
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath file path
     * @return suffix of file from path
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int extensionPos = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePos = filePath.lastIndexOf(File.separator);
        if (extensionPos == -1) {
            return "";
        }
        return (filePos >= extensionPos) ? "" : filePath.substring(extensionPos + 1);
    }

    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create Users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     *
     * @param filePath file path
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     *         the directories can not be created.
     *         <ul>
     *         <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
     *         <li>if target directory already exists, return true</li>
     *         <li>return {@link java.io.File#mkdirs}</li>
     *         </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }


    /**
     * 删除缓存文件
     * @param dir 文件目录
     * @param lastModified 时间戳
     * @return 被删除文件数量
     */
    public static int deleteFolder(File dir, long lastModified) {
        int deletedFileCount = 0;
        if (dir != null && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    deletedFileCount += deleteFolder(file, lastModified);
                }
                if (file.lastModified() < lastModified) {
                    if (file.delete()) {
                        deletedFileCount++;
                    }
                }
            }
        }
        return deletedFileCount;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断sd卡是否存在
     * @return true or false
     */
    public static boolean isSdcardExists() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static File getExternalStorageDirectory() {
        if (isSdcardExists()) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    public static File getExternalStoragePublicDirectory(String type) {
        if (isSdcardExists()) {
            return Environment.getExternalStoragePublicDirectory(type);
        }
        return null;
    }

    public static File getRootDirectory() {
        return Environment.getRootDirectory();
    }

    public static File getDataDirectory() {
        return Environment.getDataDirectory();
    }

    public static File getDownloadCacheDirectory() {
        return Environment.getDownloadCacheDirectory();
    }

    /**
     * 获取文件缓存目录，存放临时缓存数据<br/>
     * Context#getExternalCacheDir() SDCard/Android/data/com.xxx.xxx/cache，存放临时缓存数据<br/>
     * Context#getCacheDir() /data/data/com.xxx.xxx/cache<br/>
     * @param context 上下文
     * @return 文件缓存目录
     * @see Context#getExternalCacheDir()
     * @see Context#getCacheDir()
     */
    public static File getExternalCacheDir(Context context) {
        File cacheDir = null;
        if (isSdcardExists()) {
            cacheDir = context.getExternalCacheDir();
        }
        if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
            return cacheDir;
        }
        return null;
    }

    public static File getCacheDir(Context context) {
        File cacheDir = context.getCacheDir();
        if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
            return cacheDir;
        }
        return null;
    }

    /**
     * 获取文件目录，存放长时间保存数据<br/>
     * Context#getExternalFilesDir(String) SDCard/Android/data/com.xxx.xxx/files，存放长时间保存的数据<br/>
     * Context#getFilesDir() /data/data/com.xxx.xxx/files<br/>
     * @param context 上下文
     * @param type The type of files directory to return. May be {@code null}
     *            for the root of the files directory or one of the following
     *            constants for a subdirectory:
     *            {@link android.os.Environment#DIRECTORY_MUSIC},
     *            {@link android.os.Environment#DIRECTORY_PODCASTS},
     *            {@link android.os.Environment#DIRECTORY_RINGTONES},
     *            {@link android.os.Environment#DIRECTORY_ALARMS},
     *            {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *            {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *            {@link android.os.Environment#DIRECTORY_MOVIES}.
     * @return 文件目录
     * @see Context#getExternalFilesDir(String)
     * @see Context#getFilesDir()
     */
    public static File getExternalFilesDir(Context context, String type) {
        File filesDir = null;
        if (isSdcardExists()) {
            filesDir = context.getExternalFilesDir(type);
        }
        if (filesDir != null && (filesDir.exists() || filesDir.mkdirs())) {
            return filesDir;
        } else {
            return null;
        }
    }

    public static File getFilesDir(Context context) {
        File filesDir = context.getFilesDir();
        if (filesDir != null && (filesDir.exists() || filesDir.mkdirs())) {
            return filesDir;
        } else {
            return null;
        }
    }

    /**
     * 获取sd卡可用空间，单位为字节
     * @return sd卡可用空间大小
     */
    public static long getExternalStorageAvailableSize() {
        File file = getExternalStorageDirectory();
        if (file == null) {
            return 0;
        }
        StatFs statFs = new StatFs(file.getAbsolutePath());
        return statFs.getAvailableBlocks() * statFs.getBlockSize();
    }

    /**
     * 获取文件大小
     * @param file 文件
     * @return 文件大小
     */
    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        if (!file.isDirectory()) {
            return file.length();
        }
        long length = 0;
        File[] listFiles = file.listFiles();
        // 文件夹被删除时，子文件正在被写入，文件属性异常返回null
        if (listFiles != null) {
            for (File itemFile : listFiles) {
                length += getFileSize(itemFile);
            }
        }
        return length;
    }

    public static String formatFileSize(long fileSize) {
        if (fileSize <= 0) {
            fileSize = 0;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String str;
        if (fileSize < Unit.BYTE.value) {
            str = decimalFormat.format(fileSize) + "B";
        } else if (fileSize < Unit.KB.value) {
            str = decimalFormat.format(fileSize / Unit.KB.value) + "KB";
        } else if (fileSize < Unit.MB.value) {
            str = decimalFormat.format(fileSize / Unit.MB.value) + "MB";
        } else {
            str = decimalFormat.format(fileSize / Unit.GB.value) + "GB";
        }
        return str;
    }

    /**
     * 复制文件到指定文件
     *
     * @param fromPath 源文件
     * @param toPath   复制到的文件
     * @return true 成功，false 失败
     */
    public static boolean copy(String fromPath, String toPath) {
        boolean result = false;
        File from = new File(fromPath);
        if (!from.exists()) {
            return false;
        }
        File toFile = new File(toPath);
        deleteFile(toFile);
        File toDir = toFile.getParentFile();
        if (toDir.exists() || toDir.mkdirs()) {
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(from);
                out = new FileOutputStream(toFile);
                copy(in, out);
                result = true;
            } catch (Throwable ex) {
                result = false;
            } finally {
                closeQuietly(in);
                closeQuietly(out);
            }
        }
        return result;
    }

    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File itemFile : files) {
                deleteFile(itemFile);
            }
        }
        return file.delete();
    }

    /**
     * 删除指定目录下文件及目录文件
     *
     * @param filePath 文件路径
     * @param isDeleteDir 是否删除目录文件
     */
    public static boolean deleteFile(String filePath, boolean isDeleteDir) {
        if (TextUtils.isEmpty(filePath)) {
            return true;
        }
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File childFile : files) {
                    deleteFile(childFile.getAbsolutePath(), isDeleteDir);
                }
            }
            if (isDeleteDir) {
                return file.delete();
            }
        }
        return true;
    }

    /**
     * 格式化单位
     * @param size 大小
     * @return 指定格式的字符串
     */
    public static String formatFileSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result = new BigDecimal(Double.toString(kiloByte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result = new BigDecimal(Double.toString(megaByte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result = new BigDecimal(Double.toString(gigaByte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result = new BigDecimal(teraBytes);
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }

    /**
     * 保存图片到系统相册
     * @param context 上下文
     * @param imagePath 图片路径
     * @param name 图片指定名称
     * @param description 图片描述
     * @param isNotifyAlbum 是否通知相册更新<br/>
     *                      发送广播通知相册更新，扫描SD卡的广播：Intent.ACTION_MEDIA_MOUNTED，扫描期间SD卡无法访问影响体验，
     *                      使用Intent.ACTION_MEDIA_SCANNER_SCAN_FILE 扫描单个文件提升访问速度<br/>
     *                      context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(imagePathData))));
     * @return 图片的URI，格式类似：content://media/external/images/media/123456，扫描时需要转换为文件的URI再处理
     */
    public static String insertImage(Context context, String imagePath, String name, String description, boolean isNotifyAlbum) {
        if (context == null || TextUtils.isEmpty(imagePath)) {
            return null;
        }
        ContentResolver cr = context.getContentResolver();
        String imageUri = null;
        try {
            imageUri = MediaStore.Images.Media.insertImage(cr, imagePath, name, description);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (isNotifyAlbum) {
            if (!TextUtils.isEmpty(imageUri)) {
                Cursor cursor = null;
                try {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    cursor = cr.query(Uri.parse(imageUri), projection, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        String imagePathData = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                        MediaScannerConnection.scanFile(context, new String[]{imagePathData}, null, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }
        return imageUri;
    }

    public static List<String> queryExternalImages(Context context) {
        return queryImages(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    public static List<String> queryInternalImages(Context context) {
        return queryImages(context, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
    }

    public static List<String> queryImages(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        List<String> imagePaths = new ArrayList<>();
        final String[] selectionArgs = {"image/jpeg", "image/png", "image/bmp"};
        try {
            cursor = contentResolver.query(uri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? OR "
                            + MediaStore.Images.Media.MIME_TYPE + "=? OR "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    selectionArgs, MediaStore.Images.Media.DATE_ADDED + " DESC");
            if (cursor == null) {
                return imagePaths;
            }
            String imagePath;
            while (cursor.moveToNext()) {
                imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                if (!TextUtils.isEmpty(imagePath)) {
                    imagePaths.add(imagePath);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return imagePaths;
    }

    /**
     * query file dirs with file paths
     * @param filePaths file paths
     * @return file dirs
     */
    public static List<File> queryDirs(List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            return null;
        }
        List<File> fileDirs = new ArrayList<>();
        String filePath;
        for (int i = 0; i < filePaths.size(); i++) {
            filePath = filePaths.get(i);
            if (TextUtils.isEmpty(filePath)) {
                continue;
            }
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                continue;
            }
            File fileDir = file.getParentFile();
            if (fileDir != null && fileDir.exists() && fileDir.isDirectory()) {
                if (fileDirs.contains(fileDir)) {
                    continue;
                }
                fileDirs.add(fileDir);
            }
        }
        return fileDirs;
    }

    /**
     * 获取文件存储路径字符串，例如：
     * filesDir:/data/data/com.app.allscore/files,4096
     * cacheDir:/data/data/com.app.allscore/cache,4096
     * packageCodePath:/data/app/com.app.allscore-1/base.apk
     * codeCacheDir:/data/data/com.app.allscore/code_cache
     * rootDirectory:/system,4096
     * dataDirectory:/data,4096
     * downloadCacheDirectory:/cache,4096
     * externalStorageState:mounted
     * externalStorageDirectory:/storage/emulated/0,4096
     * externalFilesDir:/storage/emulated/0/Android/data/com.app.allscore/files/Download,4096
     * externalFilesMusicDir:/storage/emulated/0/Android/data/com.app.allscore/files/Music,4096
     * externalFilesPictureDir:/storage/emulated/0/Android/data/com.app.allscore/files/Pictures,4096
     * externalCacheDir:/storage/emulated/0/Android/data/com.app.allscore/cache,4096
     * @param context 上下文
     * @return 文件存储路径字符串
     */
    public static String toFileString(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");

        File filesDir = context.getFilesDir();
        if (filesDir != null) {
            builder.append("filesDir:").append(filesDir.getPath()).append(",").append(filesDir.length()).append("\n");
        }
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            builder.append("cacheDir:").append(cacheDir.getPath()).append(",").append(cacheDir.length()).append("\n");
        }
        builder.append("packageCodePath:").append(context.getPackageCodePath()).append("\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.append("codeCacheDir:").append(context.getCodeCacheDir()).append("\n");
        }

        File rootDirectory = Environment.getRootDirectory();
        if (rootDirectory != null) {
            builder.append("rootDirectory:").append(rootDirectory.getPath()).append(",").append(rootDirectory.length()).append("\n");
        }
        File dataDirectory = Environment.getDataDirectory();
        if (dataDirectory != null) {
            builder.append("dataDirectory:").append(dataDirectory.getPath()).append(",").append(dataDirectory.length()).append("\n");
        }
        File downloadCacheDirectory = Environment.getDownloadCacheDirectory();
        if (downloadCacheDirectory != null) {
            builder.append("downloadCacheDirectory:").append(downloadCacheDirectory.getPath()).append(",").append(downloadCacheDirectory.length()).append("\n");
        }
        String externalStorageState = Environment.getExternalStorageState();
        builder.append("externalStorageState:").append(externalStorageState).append("\n");
        if (TextUtils.equals(externalStorageState, Environment.MEDIA_MOUNTED)) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (externalStorageDirectory != null) {
                builder.append("externalStorageDirectory:").append(externalStorageDirectory.getPath()).append(",").append(externalStorageDirectory.length()).append("\n");
            }
            File externalFilesDownloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            if (externalFilesDownloadDir != null) {
                builder.append("externalFilesDownloadDir:").append(externalFilesDownloadDir.getPath()).append(",").append(externalFilesDownloadDir.length()).append("\n");
            }
            File externalFilesMusicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            if (externalFilesMusicDir != null) {
                builder.append("externalFilesMusicDir:").append(externalFilesMusicDir.getPath()).append(",").append(externalFilesMusicDir.length()).append("\n");
            }
            File externalFilesPictureDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (externalFilesPictureDir != null) {
                builder.append("externalFilesPictureDir:").append(externalFilesPictureDir.getPath()).append(",").append(externalFilesPictureDir.length()).append("\n");
            }
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                builder.append("externalCacheDir:").append(externalCacheDir.getPath()).append(",").append(externalCacheDir.length()).append("\n");
            }
        }
        return builder.toString();
    }
}
