package com.lyric.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.os.StatFs;
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
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 *
 * @author ganyu
 * @version 2015-9-14
 */
public class FileUtils {
    public final static String FILE_EXTENSION_SEPARATOR = ".";
    /** gb to byte **/
    public static final long GB_2_BYTE = 1073741824;
    /** mb to byte **/
    public static final long MB_2_BYTE = 1048576;
    /** kb to byte **/
    public static final long KB_2_BYTE = 1024;

    FileUtils() {
    }

    /**
     * 删除缓存文件
     * @param context Context
     */
    public static void deleteCacheFolder(Context context) {
        clearCacheFolder(context.getCacheDir(), System.currentTimeMillis());
    }

    /**
     * 删除缓存文件
     * @param dir          文件目录
     * @param lastModified 时间戳
     * @return 被删除文件数量
     */
    public static int clearCacheFolder(File dir, long lastModified) {
        int deletedFileCount = 0;
        if (dir != null && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    deletedFileCount += clearCacheFolder(file, lastModified);
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

    /**
     * 判断文件是否存在
     * @param filePath 文件路径
     * @return boolean
     */
    public static boolean isExists(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeQuietly(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
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
                long skipSize = 0;
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

    public static String readStr(InputStream in) throws IOException {
        return readStr(in, "UTF-8");
    }

    public static String readStr(InputStream in, String charset) throws IOException {
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

    public static void writeStr(OutputStream out, String str) throws IOException {
        writeStr(out, str, "UTF-8");
    }

    public static void writeStr(OutputStream out, String str, String charset) throws IOException {
        if (TextUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }
        Writer writer = new OutputStreamWriter(out, charset);
        writer.write(str);
        writer.flush();
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        if (!(out instanceof BufferedOutputStream)) {
            out = new BufferedOutputStream(out);
        }
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
    }

    public static boolean deleteFileOrDir(File path) {
        if (path == null || !path.exists()) {
            return true;
        }
        if (path.isFile()) {
            return path.delete();
        }
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteFileOrDir(file);
            }
        }
        return path.delete();
    }

    public static File getCacheDir(Context context) {
        File file;
        // 判断SD卡是否存在，或者SD卡不可被移除
        if (isSdcardExists() || !Environment.isExternalStorageRemovable()) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                file = externalCacheDir;
            } else {
                final String defaultSystemFileDir = "Android/data/" + context.getPackageName() + "/cache";
                file = new File(Environment.getExternalStorageDirectory(), defaultSystemFileDir);
            }
        } else {
            file = context.getCacheDir();
        }
        if (file.exists() || file.mkdirs()) {
            return file;
        } else {
            return null;
        }
    }

    public static String getCacheDirPath(Context context) {
        File file = getCacheDir(context);
        if (file != null) {
            return file.getPath();
        }
        return null;
    }

    /**
     * 获取磁盘缓存文件目录
     * @param context 上下文
     * @param dirName 路径下目录名称
     * @return 磁盘缓存文件目录
     */
    public static String getCacheDirPath(Context context, String dirName) {
        File file = getCacheDir(context, dirName);
        if (file != null && file.exists()) {
            return file.getPath();
        }
        return null;
    }

    /**
     * 获取磁盘缓存文件
     * @param context 上下文
     * @param dirName 路径下目录名称
     * @return 磁盘缓存文件
     */
    public static File getCacheDir(Context context, String dirName) {
        File file = null;
        File cacheDir = getCacheDir(context);
        if (cacheDir != null && cacheDir.isDirectory()) {
            file = new File(cacheDir, dirName);
        }
        if (file != null && (file.exists() || file.mkdir())) {
            return file;
        }
        return null;
    }

    /**
     * 检查磁盘空间是否大于10mb
     *
     * @return true 大于
     */
    public static boolean isDiskAvailable() {
        long size = getDiskAvailableSize();
        return size > 10 * 1024 * 1024; // > 10bm
    }

    /**
     * 获取磁盘可用空间
     *
     * @return byte 单位 kb
     */
    public static long getDiskAvailableSize() {
        if (!isSdcardExists()) return 0;
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getAbsolutePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
        // (availableBlocks * blockSize)/1024 KIB 单位
        // (availableBlocks * blockSize)/1024 /1024 MIB单位
    }

    /**
     * 判断SD卡是否存在
     * @return true or false
     */
    public static boolean isSdcardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getFileOrDirSize(File file) {
        if (!file.exists()) return 0;
        if (!file.isDirectory()) return file.length();

        long length = 0;
        File[] list = file.listFiles();
        if (list != null) { // 文件夹被删除时, 子文件正在被写入, 文件属性异常返回null.
            for (File item : list) {
                length += getFileOrDirSize(item);
            }
        }

        return length;
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
        deleteFileOrDir(toFile);
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

    public static String getFileFromAssets(Context context, String fileName) {
        if (context == null || StringUtils.isEmpty(fileName)) {
            return null;
        }
        StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFileFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }
        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * read file
     *
     * @param filePath the file to be opened for reading.
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (!file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param content
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param contentList
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (ListUtils.isEmpty(contentList)) {
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
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath the file to be opened for writing.
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * write file, the string list will be written to the begin of the file
     *
     * @param filePath the file to be opened for writing.
     * @param contentList
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath the file to be opened for writing.
     * @param stream the input stream
     * @return returns true
     * @see {@link #writeFile(String, InputStream, boolean)}
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
     * write file, the bytes will be written to the begin of the file
     *
     * @param file he file to be opened for writing.
     * @param stream the input stream
     * @return returns true
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
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
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
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
        InputStream inputStream = null;
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
        List<String> fileContent = new ArrayList<String>();
        if (!file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
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
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
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
        if (StringUtils.isEmpty(filePath)) {
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
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
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
        if (StringUtils.isBlank(filePath)) {
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
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
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
        if (StringUtils.isEmpty(folderName)) {
            return false;
        }
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }

    /**
     * @param filePath file path
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     *         the directories can not be created.
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     *
     * @param filePath file path
     * @return returns true if file exists
     */
    public static boolean isFileExist(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     *
     * @param directoryPath directory path
     * @return returns true if directory exists
     */
    public static boolean isFolderExist(String directoryPath) {
        if (StringUtils.isBlank(directoryPath)) {
            return false;
        }
        File folder = new File(directoryPath);
        return (folder.exists() && folder.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     *
     * @param path file or directory path
     * @return returns true if delete success
     */
    public static boolean deleteFile(String path) {
        if (StringUtils.isBlank(path)) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File childFile : file.listFiles()) {
            if (childFile.isFile()) {
                childFile.delete();
            } else if (childFile.isDirectory()) {
                deleteFile(childFile.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     *
     * @param path file path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        if (StringUtils.isBlank(path)) {
            return -1;
        }
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }
}
