package com.newaim.core.utils;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtil {

	private static Log logger = LogFactory.getLog(FileUtil.class);

	public static String generateFilename(String originalFilename) {

		SimpleDateFormat dirSdf = new SimpleDateFormat("yyyyMM");
		String filePre = dirSdf.format(new Date());

		String fileExt = "";
		int lastIndex = originalFilename.lastIndexOf('.');
		// 取得文件的扩展名
		if (lastIndex != -1) {
			fileExt = originalFilename.substring(lastIndex);
		}

		String filename = filePre + "/" + UUID.randomUUID().toString() + fileExt;

		return filename;
	}

	/**
	 * 把数据写至文件中
	 * 
	 * @param filePath
	 * @param data
	 */
	public static void writeFile(String filePath, String data) {
		FileOutputStream fos = null;
		OutputStreamWriter writer = null;
		try {
			fos = new FileOutputStream(new File(filePath));
			writer = new OutputStreamWriter(fos, "UTF-8");
			writer.write(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readFile(String filePath) {
		StringBuffer buffer = new StringBuffer();
		// 读出这个文件的内容
		try {
			File file = new File(filePath);
			FileInputStream fis = null;
			BufferedReader breader = null;
			try {
				fis = new FileInputStream(file);
				InputStreamReader isReader = new InputStreamReader(fis, "UTF-8");
				breader = new BufferedReader(isReader);
				String line;
				while ((line = breader.readLine()) != null) {
					buffer.append(line);
					buffer.append("\r\n");
				}
				breader.close();
				isReader.close();
				fis.close();

			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return buffer.toString();
	}

	/**
	 * stream 转为字符串
	 * 
	 * @param input
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream input, String charset)
			throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(input,
				charset));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line + "\n");
		}
		return buffer.toString();

	}

	/**
	 * 获取文件的字符集
	 * 
	 * @param file
	 * @return
	 */
	public static String getCharset(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();

			if (!checked) {
				// int loc = 0;
				while ((read = bis.read()) != -1) {
					// loc++;
					if (read >= 0xF0)
						break;
					// 单独出现BF以下的，也算是GBK
					if (0x80 <= read && read <= 0xBF)
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF)// 双字节 (0xC0 - 0xDF)
							// (0x80 -
							// 0xBF),也可能在GB编码内
							continue;
						else
							break;
						// 也有可能出错，但是几率较小
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}

			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}

	/**
	 * 判断一个文件是否存在
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 存在返回true，否则返回false
	 */
	public static boolean isExist(String filePath) {
		return new File(filePath).exists();
	}

	public static String getClassesPath() {
		String templatePath = FileUtil.class.getClassLoader().getResource("/").getPath();
		String rootPath = "";
		//windows下
		if("\\".equals(File.separator)){
			rootPath = templatePath.replace("/", "\\");
		}
		
		//linux下
		if("/".equals(File.separator)){
			rootPath = templatePath.replace("\\", "/");
		}
		
		System.out.println("测试路径："+rootPath);
		return rootPath;
	}
	
	
	/**
	 * 写入文件
	 * 
	 * @param fileName
	 * @param is
	 * @throws IOException
	 */
	public static void writeFile(String fileName, InputStream is)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		byte[] bs = new byte[512];
		int n = 0;
		while ((n = is.read(bs)) != -1) {
			fos.write(bs, 0, n);
		}
		is.close();
		fos.close();
	}
	
	/**
	 * 指定字符集，写入文件。
	 * @param fileName
	 * @param content
	 * @param charset
	 */
	public static void writeFile(String fileName, String content,String charset) {
		Writer out;
		try {
			createFolder(fileName, true);
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), charset));
			out.write(content);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据文件路径创建文件夹,如果路径不存在则创建.
	 * 
	 * @param path
	 */
	public static void createFolderFile(String path) {
		createFolder(path, true);
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @param isFile
	 */
	public static void createFolder(String path, boolean isFile) {
		if (isFile) {
			path = path.substring(0, path.lastIndexOf(File.separator));
		}
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
	}

	
	/**
	 * 取得文件扩展名
	 * 
	 * @return
	 */
	public static String getFileExt(File file) {
		if (file.isFile()) {
			return getFileExt(file.getName());
		}
		return "";
	}
	
	/**
	 * 根据文件名获取扩展名称。
	 * @param fileName
	 * @return
	 */
	public static String getFileExt(String fileName){
		int pos=fileName.lastIndexOf(".");
		if(pos>-1){
			return fileName.substring(pos + 1).toLowerCase();
		}
		return "";
	}
	
	public static Long getFileSize(String filePath){
		Long size = (long) 0;
		
		File f= new File(filePath);
	    if (f.exists() && f.isFile()){  
	    	size =f.length();  
	    }
	    
		return size;
	}
		
	public static String getStrFileSize(Long size){
	    DecimalFormat df=new DecimalFormat("0.00");
		if(size>1024*1024){
			 double ss=size/(1024*1024);
		 	 return df.format(ss)+" Mb";
		}else if(size>1024){
			double ss=size/1024;
			return df.format(ss)+" Kb";
		}else{
			return size+" Bytes";
		}
    }

    /**
     * 根据文件扩展名获取文件类型编码
     * @param ext
     * @return
     */
    public static int getFileCatByExt(String ext){
        int result = 0;

        if(ext != null){
            ext = ext.toLowerCase();
            if(ext.equals("jpg") || ext.equals("gif") || ext.equals("png") || ext.equals("bmp")){
                result = 1;
            }else if(ext.equals("xls") || ext.equals("xlsx") || ext.equals("doc") || ext.equals("docx") || ext.equals("ppt") || ext.equals("pptx")){
                result = 2;
            }else if(ext.equals("mp3") || ext.equals("wma") || ext.equals("wav")){
                result = 3;
            }else if(ext.equals("mp4") || ext.equals("webm") || ext.equals("rm") || ext.equals("rmvb") || ext.equals("wmv") || ext.equals("avi") || ext.equals("ogg") || ext.equals("mov")){
                result = 4;
            }else if(ext.equals("pdf")){
                result = 5;
            }else if(ext.equals("txt") || ext.equals("htm") || ext.equals("html") || ext.equals("js") || ext.equals("css")){
                result = 6;
            }else if(ext.equals("flv") || ext.equals("swf")){
                result = 7;
            }else if(ext.equals("zip") || ext.equals("rar") || ext.equals("7z")){
                result = 8;
            }else{
                result = 9;
            }
        }

        return result;
    }

    public static void removeFile(String filePath) {
        File f = new File(filePath);
        if(f.isFile()) f.delete();
    }




    /**
     * 左填充
     *
     * @param str
     * @param length
     * @param ch
     * @return
     */
    public static String leftPad(String str, int length, char ch) {
        if (str.length() >= length) {
            return str;
        }
        char[] chs = new char[length];
        Arrays.fill(chs, ch);
        char[] src = str.toCharArray();
        System.arraycopy(src, 0, chs, length - src.length, src.length);
        return new String(chs);

    }

    /**
     * 删除文件
     *
     * @param fileName  待删除的完整文件名
     * @return
     */
    public static boolean delete(String fileName) {
        boolean result = false;
        File f = new File(fileName);
        if (f.exists()) {
            result = f.delete();

        } else {
            result = true;
        }
        return result;
    }

    /***
     * 递归获取指定目录下的所有的文件（不包括文件夹）
     *
     * @param dirPath
     * @return
     */
    public static ArrayList<File> getAllFiles(String dirPath) {
        File dir = new File(dirPath);

        ArrayList<File> files = new ArrayList<File>();

        if (dir.isDirectory()) {
            File[] fileArr = dir.listFiles();
            for (int i = 0; i < fileArr.length; i++) {
                File f = fileArr[i];
                if (f.isFile()) {
                    files.add(f);
                } else {
                    files.addAll(getAllFiles(f.getPath()));
                }
            }
        }
        return files;
    }

    /**
     * 获取指定目录下的所有文件(不包括子文件夹)
     *
     * @param dirPath
     * @return
     */
    public static ArrayList<File> getDirFiles(String dirPath) {
        File path = new File(dirPath);
        File[] fileArr = path.listFiles();
        ArrayList<File> files = new ArrayList<File>();

        for (File f : fileArr) {
            if (f.isFile()) {
                files.add(f);
            }
        }
        return files;
    }

    /**
     * 获取指定目录下特定文件后缀名的文件列表(不包括子文件夹)
     *
     * @param dirPath
     *            目录路径
     * @param suffix
     *            文件后缀
     * @return
     */
    public static ArrayList<File> getDirFiles(String dirPath, final String suffix) {
        File path = new File(dirPath);
        File[] fileArr = path.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowerName = name.toLowerCase();
                String lowerSuffix = suffix.toLowerCase();
                if (lowerName.endsWith(lowerSuffix)) {
                    return true;
                }
                return false;
            }

        });
        ArrayList<File> files = new ArrayList<File>();

        for (File f : fileArr) {
            if (f.isFile()) {
                files.add(f);
            }
        }
        return files;
    }

    /**
     * 读取文件内容
     *
     * @param fileName
     *            待读取的完整文件名
     * @return 文件内容
     * @throws IOException
     */
    public static String read(String fileName) throws IOException {
        File f = new File(fileName);
        FileInputStream fs = new FileInputStream(f);
        String result = null;
        byte[] b = new byte[fs.available()];
        fs.read(b);
        fs.close();
        result = new String(b);
        return result;
    }

    /**
     * 写文件
     *
     * @param fileName
     *            目标文件名
     * @param fileContent
     *            写入的内容
     * @return
     * @throws IOException
     */
    public static boolean write(String fileName, String fileContent) throws IOException {
        return write(fileName, fileContent, true, true);
    }

    /**
     * 写文件
     *
     * @param fileName
     *            完整文件名(类似：/usr/a/b/c/d.txt)
     * @param fileContent
     *            文件内容
     * @param autoCreateDir
     *            目录不存在时，是否自动创建(多级)目录
     * @param autoOverwrite
     *            目标文件存在时，是否自动覆盖
     * @return
     * @throws IOException
     */
    public static boolean write(String fileName, String fileContent, boolean autoCreateDir, boolean autoOverwrite) throws IOException {
        return write(fileName, fileContent.getBytes(), autoCreateDir,autoOverwrite);
    }

    /**
     * 写文件
     *
     * @param fileName
     *            完整文件名(类似：/usr/a/b/c/d.txt)
     * @param contentBytes
     *            文件内容的字节数组
     * @param autoCreateDir
     *            目录不存在时，是否自动创建(多级)目录
     * @param autoOverwrite
     *            目标文件存在时，是否自动覆盖
     * @return
     * @throws IOException
     */
    public static boolean write(String fileName, byte[] contentBytes, boolean autoCreateDir, boolean autoOverwrite) throws IOException {
        boolean result = false;
        if (autoCreateDir) {
            createDirs(fileName);
        }
        if (autoOverwrite) {
            delete(fileName);
        }
        File f = new File(fileName);
        FileOutputStream fs = new FileOutputStream(f);
        fs.write(contentBytes);
        fs.flush();
        fs.close();
        result = true;
        return result;
    }

    /**
     * 追加内容到指定文件
     *
     * @param fileName
     * @param fileContent
     * @return
     * @throws IOException
     */
    public static boolean append(String fileName, String fileContent)
            throws IOException {
        boolean result = false;
        File f = new File(fileName);
        if (f.exists()) {
            RandomAccessFile rFile = new RandomAccessFile(f, "rw");
            byte[] b = fileContent.getBytes();
            long originLen = f.length();
            rFile.setLength(originLen + b.length);
            rFile.seek(originLen);
            rFile.write(b);
            rFile.close();
        }
        result = true;
        return result;
    }

    /**
     * 拆分文件
     *
     * @param fileName
     *            待拆分的完整文件名
     * @param byteSize
     *            按多少字节大小拆分
     * @return 拆分后的文件名列表
     * @throws IOException
     */
    public List<String> splitBySize(String fileName, int byteSize)
            throws IOException {
        List<String> parts = new ArrayList<String>();
        File file = new File(fileName);
        int count = (int) Math.ceil(file.length() / (double) byteSize);
        int countLen = (count + "").length();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(count,
                count * 3, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(count * 2));

        for (int i = 0; i < count; i++) {
            String partFileName = file.getPath() + "."
                    + leftPad((i + 1) + "", countLen, '0') + ".part";
            threadPool.execute(new SplitRunnable(byteSize, i * byteSize,
                    partFileName, file));
            parts.add(partFileName);
        }
        return parts;
    }

    /**
     * 合并文件
     *
     * @param dirPath
     *            拆分文件所在目录名
     * @param partFileSuffix
     *            拆分文件后缀名
     * @param partFileSize
     *            拆分文件的字节数大小
     * @param mergeFileName
     *            合并后的文件名
     * @throws IOException
     */
    public void mergePartFiles(String dirPath, String partFileSuffix,
                               int partFileSize, String mergeFileName) throws IOException {
        ArrayList<File> partFiles = FileUtil.getDirFiles(dirPath,
                partFileSuffix);
        Collections.sort(partFiles, new FileComparator());

        RandomAccessFile randomAccessFile = new RandomAccessFile(mergeFileName,
                "rw");
        randomAccessFile.setLength(partFileSize * (partFiles.size() - 1)
                + partFiles.get(partFiles.size() - 1).length());
        randomAccessFile.close();

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                partFiles.size(), partFiles.size() * 3, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(partFiles.size() * 2));

        for (int i = 0; i < partFiles.size(); i++) {
            threadPool.execute(new MergeRunnable(i * partFileSize,
                    mergeFileName, partFiles.get(i)));
        }

    }

    /**
     * 根据文件名，比较文件
     *
     * @author yjmyzz@126.com
     *
     */
    private class FileComparator implements Comparator<File> {
        public int compare(File o1, File o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    /**
     * 创建(多级)目录
     *
     * @param filePath      完整的文件名(类似：/usr/a/b/c/d.xml)
     */
    public static void createDirs(String filePath) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

    }

    /**
     * 分割处理Runnable
     *
     * @author yjmyzz@126.com
     *
     */
    private class SplitRunnable implements Runnable {
        int byteSize;
        String partFileName;
        File originFile;
        int startPos;

        public SplitRunnable(int byteSize, int startPos, String partFileName,
                             File originFile) {
            this.startPos = startPos;
            this.byteSize = byteSize;
            this.partFileName = partFileName;
            this.originFile = originFile;
        }

        public void run() {
            RandomAccessFile rFile;
            OutputStream os;
            try {
                rFile = new RandomAccessFile(originFile, "r");
                byte[] b = new byte[byteSize];
                rFile.seek(startPos);// 移动指针到每“段”开头
                int s = rFile.read(b);
                os = new FileOutputStream(partFileName);
                os.write(b, 0, s);
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并处理Runnable
     *
     * @author yjmyzz@126.com
     *
     */
    private class MergeRunnable implements Runnable {
        long startPos;
        String mergeFileName;
        File partFile;

        public MergeRunnable(long startPos, String mergeFileName, File partFile) {
            this.startPos = startPos;
            this.mergeFileName = mergeFileName;
            this.partFile = partFile;
        }

        public void run() {
            RandomAccessFile rFile;
            try {
                rFile = new RandomAccessFile(mergeFileName, "rw");
                rFile.seek(startPos);
                FileInputStream fs = new FileInputStream(partFile);
                byte[] b = new byte[fs.available()];
                fs.read(b);
                fs.close();
                rFile.write(b);
                rFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
