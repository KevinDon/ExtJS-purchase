package com.newaim.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;


import com.newaim.core.beans.StreamGobbler;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.purchase.admin.system.entity.MyDocument;
import org.jodconverter.JodConverter;
import org.jodconverter.office.*;


public class FileConvertUtils extends ControllerBase{
	/** 上传目录名 */
	public static final String uploadFolderName = "var/upload/";
	
	/** 允许上传的扩展名 */
	public static final String[] extPermit = { 
		"txt", "htm", "html", "js", "css",
		"jpg", "png", "gif", "bmp",
		"xls", "xlsx", "doc", "docx", "ppt", "pptx", "vsd", "vsdx", "pdf",
		"mp3", //"wma", "wav",
		"wmv", //"rm", "rmvb", "avi",
		"swf", "flv",
		"mp4", "webm", //"ogg", "mov", 
		"zip", "rar", "7z"
	};

    /**
	 * 转换 office文件为 swf格式
	 * @param request
	 * @param files 文件对象
	 * @return
	 */
	public String docToSwf(ServletRequest request, MyDocument files) {
		String mark = "no";
		String path = request.getServletContext().getRealPath("/") + files.getPath();
		path = path.replace("\\", "/");
		
		String swfPath = getOutputFilePath(path, "swf");
		File file = new File(swfPath);

		// 源文件对应的 SWF文件存在就直接返回，不用转换
		if (file.exists()) {
			mark = "yes";
		} else {
			// 源文件存在就去转换
			int num = path.lastIndexOf(".");
			String pdfPath = "";
			// 如果是PDF的附件，就不用再转换成PDF文件
			if ("pdf".equals(path.substring(num + 1, path.length()).toLowerCase())) {
				pdfPath = path;
			} else {
				pdfPath = getOutputFilePath(path, "pdf"); // 替换成pdf的文件名称
				file = new File(pdfPath);
				// 对应的PDF文件存在就不用把源文件转成PDF文件
				if (!file.exists()) {
					pdfPath = convertOfficeToPdf(path); // 把 PDF 转换成 SWF文件
				}
			}

			try {
				
				swfPath = convertPdfToSwf(pdfPath); 
				if (StringUtils.isNotEmpty(swfPath)) {
					if (!"pdf".equals(path.substring(num + 1, path.length()).toLowerCase())) { // 如果是源文件是PDF的附件就不用删除
						File filePdf = new File(pdfPath);
						filePdf.delete(); // 有生成SWF文件后，可以删除PDF文件，避免文件过多
											// （可以不删除）
					}
					mark = "yes";
				}

			} catch (Exception e) {
                e.printStackTrace();
			}
		}
		
		return mark;
	}
	
	/**
	 * 转换pdf 为 swf
	 * @param request
	 * @param files  文件对象
	 * @return
	 */
	public String pdfToSwf(ServletRequest request, MyDocument files) {
		String mark = "no";
		String path = request.getServletContext().getRealPath("/") + files.getPath();
		path = path.replace("\\", "/");
		String swfPath = getOutputFilePath(path, "swf");
		File file = new File(swfPath);

		// 源文件对应的 SWF文件存在就直接返回，不用转换
		if (file.exists()) {
			mark = "yes";
		} else {
			// 源文件存在就去转换
			String pdfPath = path;

			try {
				swfPath = convertPdfToSwf(pdfPath);
				mark = "yes";
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
		
		return mark;
	}

	/**
	 * 转换格式文件的名称，inputFilePath 为文件全称，type 为要变成的类型
	 * @param inputFilePath
	 * @param type
	 * @return
	 */
	public String getOutputFilePath(String inputFilePath, String type) {
		String outputFilePath = "";
		int num = inputFilePath.lastIndexOf(".");
		if (num > 0) {
			outputFilePath = inputFilePath.substring(0, num) + "." + type;
		} else {
			outputFilePath = inputFilePath + "." + type;
		}
		return outputFilePath;
	}

    /**
     * 新的转换方法
     * @param inputFilePath
     * @return
     */
    public String convertOfficeToPdf(String inputFilePath) {
        String outputFilePath = "";
        int num = inputFilePath.lastIndexOf(".");
        String type = inputFilePath.substring(num + 1, inputFilePath.length()).toLowerCase();
        logger.info("officeToPdf文件的格式：" + type + " | " + inputFilePath);

        try {
            if ("doc".equals(type) || "docx".equals(type) || "ppt".equals(type)
                    || "pptx".equals(type) || "xls".equals(type) || "xlsx".equals(type)) {

                File inputFile = new File(inputFilePath);

                if (inputFile.exists()) { // 找到源文件
                    outputFilePath = getOutputFilePath(inputFilePath, "pdf");
                    logger.info("输出路径 outputFilePath | " + outputFilePath);
                    File outputFile = new File(outputFilePath);
                    if (!outputFile.getParentFile().exists()) { // 假如目标路径不存在,
                        outputFile.getParentFile().mkdirs();
                    }

                    final LocalOfficeManager officeManager = LocalOfficeManager.install();
                    try {

                        officeManager.start();

                        // Convert
                        JodConverter.convert(inputFile)
                                .to(outputFile)
                                .execute();
                        Thread.sleep(1000);
                    } finally {
                        // Stop the office process
                        officeManager.stop();

                    }

                    logger.info("转换完成 | " + outputFile.getPath());
                } else {
                    logger.info("源文件不存在，OpenOffice转换失败！ " + inputFile);
                }
            } else {
                logger.info("转换失败，OpenOffice仅能转换 Office文件为Pdf格式！");
            }
        } catch (Exception e) {
            logger.info("pdf文件转换失败");
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return outputFilePath;
    }

	/**
	 * @param sourceFile
	 * @return
	 */
	private String convertPdfToSwf(String sourceFile) {
		int num = sourceFile.lastIndexOf(".");
		String type = sourceFile.substring(num + 1, sourceFile.length()).toLowerCase();
		
		logger.info("convertPdf2Swf文件的格式：" + type);

		if ("pdf".equals(type)) {
			File file = new File(sourceFile);
			if (file.exists()) { // 是否找到源文件
				String pdfToSwfHome = getPdfToSwfHome();
				String outFile = getOutputFilePath(sourceFile, "swf");
				String command = "";

				String osName = System.getProperty("os.name").toLowerCase();
				if (Pattern.matches("windows.*", osName)) {
					command = pdfToSwfHome + "\\pdf2swf.exe \"" + sourceFile + "\" -o  \"" + outFile + "\" -s flashversion=9 ";
				} else {
					command = pdfToSwfHome + "/pdf2swf " + sourceFile + " -o " + outFile + " -s flashversion=9 ";
				}

				try {
					// logger.info(command);
					Process process = Runtime.getRuntime().exec(command);
					// Thread.sleep(5000);
					// process.destroy();
					// 非要读取一遍cmd的输出，要不不会flush生成文件（多线程）
					StreamGobbler sg1 = new StreamGobbler(process.getInputStream(), "Console");
					StreamGobbler sg2 = new StreamGobbler(process.getErrorStream(), "Error");
					sg1.start();
					sg2.start();
					try {
						process.waitFor();
					} catch (InterruptedException e) {
						logger.info("swf 转换过程出错！" + e.getMessage());
					}
					File swfFile = new File(outFile);
					if (swfFile.exists()) {
						if (process != null) {
							process.destroy();
						}
						// logger.info("swf 转换成功:" + outFile);
						return outFile;
					}
					return "";
				} catch (Exception e) {
					logger.info("swf 转换失败" + e.getMessage());
				}
				return "";
			} else {
				logger.info("源文件不存在，swftools转换失败！ " + sourceFile);
				return "";
			}
		} else {
			logger.info("swftools 转换失败！");
			return "";
		}
	}

	private static String getOfficeHome() {
		String osName = System.getProperty("os.name").toLowerCase();
		InputStream in = FileConvertUtils.class.getClassLoader().getResourceAsStream("application.properties");
		Properties pp = new Properties();
		try {
			pp.load(in);

			if (Pattern.matches("linux.*", osName)) {
				return pp.getProperty("linux.officeToPdf");
				// return "/opt/openoffice.org3";
			} else if (Pattern.matches("windows.*", osName)) {
				return pp.getProperty("windows.officeToPdf").toString().replace("\\", "/");
				// return "C:/Program Files/OpenOffice.org 3";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * swftools的安装路径可以存放在一个自定义的属性文件或者保存数据库中存取！
	 * @return
	 */
	private String getPdfToSwfHome() {
		String osName = System.getProperty("os.name").toLowerCase();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("application.properties");
		Properties p = new Properties();
		try {
			p.load(in);
			if (Pattern.matches("linux.*", osName)) {
				return p.getProperty("linux.pdfToSwf");
				// return "/usr/local/bin"; //待确定
			} else if (Pattern.matches("windows.*", osName)) {

				return p.getProperty("windows.pdfToSwf").replace("\\", "/");
				// return "C:/Program Files/SWFTools";
			} else if (Pattern.matches("mac.*", osName)) {
				return p.getProperty("mac.pdfToSwf");
				// return "/Application/SWFTools.app/Contents"; //待确定
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
