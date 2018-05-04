package com.newaim.purchase.admin.system.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.*;
import com.newaim.core.utils.export.ExcelUtil;
import com.newaim.core.utils.export.WordUtil;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.MyDocument;
import com.newaim.purchase.admin.system.vo.ExportColumnsVo;
import com.newaim.purchase.admin.system.vo.MyDocumentCategoryVo;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.org.apache.poi.util.IOUtils;
import org.docx4j.services.client.ConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@Service
public class ExportService {

    @Autowired
    MyDocumentCategoryService myDocumentCategoryService;

    @Autowired
    MyDocumentService myDocumentService;
    private XWPFDocument doc;

    /**
     * 导出xls、pdf、csv
     *
     * @param request
     * @param act
     * @param fileTitle
     * @param fileName
     * @param id
     * @param columns
     * @param data
     * @return
     * @throws Exception
     */
    public MyDocumentVo exportFile(ServletRequest request, String act, String fileTitle, String fileName, String id, ExportColumnsVo columns, List<?> data)
            throws Exception {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();//使用日历类
        int year = cal.get(Calendar.YEAR);//得到年
        int month = cal.get(Calendar.MONTH) + 1;//得到月，因为从0开始的，所以要加1
        UserVo user = SessionUtils.currentUserVo();

        String sheetName = DateFormatUtil.formatTime(now).replace(" ", "_").replace(":", "");
        fileName += "-" + sheetName + ".xls";
        fileTitle += "-" + sheetName + "." + act;

        List<String[]> columNames = new ArrayList();
        columNames.add(columns.getTexts());

        List<String[]> fieldNames = new ArrayList();
        fieldNames.add(columns.getKeys());

        LinkedHashMap<String, List<?>> map = new LinkedHashMap();
        map.put(sheetName, data);

        ExcelUtil.ExcelExportData setInfo = new ExcelUtil.ExcelExportData();
        setInfo.setDataMap(map);
        setInfo.setTitles(new String[]{});
        setInfo.setFieldNames(fieldNames);
        setInfo.setColumnNames(columNames);

        String relateFilePath = "/var/upload/" + user.getId() + "/export/" + year + month + "/" + fileName;
        String realPath = request.getServletContext().getRealPath(relateFilePath).toString();

        // 将需要导出的数据输出到文件
        ExcelUtil.export2File(setInfo, realPath);

        File file = new File(realPath);
        MyDocument mydoc = new MyDocument();
        if (file.exists()) {
            //转PDF
            if ("pdf".equals(act)) {
                FileConvertUtils fcu = new FileConvertUtils();
                realPath = fcu.convertOfficeToPdf(realPath);
                file.delete();
                relateFilePath = fcu.getOutputFilePath(relateFilePath, "pdf");
            }

            MyDocumentCategoryVo mdcv = myDocumentCategoryService.getByPath("export");
            mydoc.setBytes(FileUtil.getFileSize(realPath));
            mydoc.setExtension(FileUtil.getFileExt(realPath));
            mydoc.setName("E" + fileTitle);
            mydoc.setPath(relateFilePath.substring(1));
            mydoc.setNote(FileUtil.getStrFileSize(mydoc.getBytes()));
            mydoc.setStatus(1);
            mydoc.setShared(1);
            mydoc.setCategoryId(mdcv.getId());
            mydoc.setType(FileUtil.getFileCatByExt(mydoc.getExtension()));
            mydoc = myDocumentService.add(mydoc);
        }

        return BeanMapper.map(mydoc, MyDocumentVo.class);
    }

    /**
     * 导出docx、pdf
     * @param request
     * @param act
     * @param fileTitle
     * @param fileName
     * @param data
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> MyDocumentVo exportFileForWordByXml(HttpServletRequest request, String act, String fileTitle, String fileName, T data)
            throws Exception {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();//使用日历类
        int year = cal.get(Calendar.YEAR);//得到年
        int month = cal.get(Calendar.MONTH) + 1;//得到月，因为从0开始的，所以要加1
        UserVo user = SessionUtils.currentUserVo();

        String sheetName = DateFormatUtil.formatTime(now).replace(" ", "_").replace(":", "");
        String fileNameOutResource = fileName + "-" + sheetName + ".docx";
        String fileNameOutTemp = fileName + "-" + sheetName + ".xml";
        String[] fileTitles = fileTitle.length()>0 ? fileTitle.split("-") : new String[1];
        fileTitle = fileTitles[0].trim() + "-" + sheetName + ".docx";

        String relateFilePath = "/var/upload/" + user.getId() + "/export/" + year + month + "/";
        String realFilePath = request.getServletContext().getRealPath(relateFilePath + fileNameOutResource).toString();
        fileNameOutTemp = request.getServletContext().getRealPath(relateFilePath + fileNameOutTemp).toString();

        //转换对象
        Map<String, Object> params = BeanMapper.toMap((T) data);
        //模板替换
        String a = FreeMarkerUtil.instance().geneFileStr(request, "export/docx/" + fileName + ".xml", params);
        //写文件
        FileUtil.write(fileNameOutTemp, a.getBytes(), true,true);

        //压缩转存
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(fileNameOutTemp));
        SaveToZipFile saver = new SaveToZipFile(wordMLPackage);
        saver.save(realFilePath);
        //删除转存临时文件
        File tempFile = new File(fileNameOutTemp);
        if(tempFile.exists()) tempFile.delete();

        //文件存存储
        File file = new File(realFilePath);
        MyDocument mydoc = new MyDocument();
        if (file.exists()) {
            //转PDF
            if ("pdf".equals(act)) {
                FileConvertUtils fcu = new FileConvertUtils();
                realFilePath = fcu.convertOfficeToPdf(realFilePath);
                file.delete();
                relateFilePath = fcu.getOutputFilePath(relateFilePath, "pdf");
            }

            MyDocumentCategoryVo mdcv = myDocumentCategoryService.getByPath("export");
            mydoc.setBytes(FileUtil.getFileSize(realFilePath));
            mydoc.setExtension(FileUtil.getFileExt(realFilePath));
            mydoc.setName("E" + fileTitle);
            mydoc.setPath(relateFilePath.substring(1) + fileNameOutResource);
            mydoc.setNote(FileUtil.getStrFileSize(mydoc.getBytes()));
            mydoc.setStatus(1);
            mydoc.setShared(1);
            mydoc.setCategoryId(mdcv.getId());
            mydoc.setType(FileUtil.getFileCatByExt(mydoc.getExtension()));
            mydoc = myDocumentService.add(mydoc);
        }

        return BeanMapper.map(mydoc, MyDocumentVo.class);
    }

    /**
     * 没启用
     *
     * @param request
     * @param act
     * @param fileTitle
     * @param fileName
     * @param bean
     * @param data
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> MyDocumentVo exportFileForWordByPoi(HttpServletRequest request, String act, String fileTitle, String fileName, Class<T> bean, T data)
            throws Exception {
        MyDocument mydoc = new MyDocument();
        Map<String, Object> params = BeanMapper.toMapForVar((T) data);

        String a = FreeMarkerUtil.instance().geneFileStr(request, "export/docx/" + fileName + ".xml", params);

        WordUtil xwpfTUtil = new WordUtil();
        XWPFDocument doc;
        String fileNameInResource = "e:\\FlowNewProductViewFormPanelID.docx";
        String fileNameOutResource = "e:\\" + fileName + ".docx";
        InputStream is = new FileInputStream(fileNameInResource);
        doc = new XWPFDocument(is);

        //替换段落内容
        xwpfTUtil.replaceInPara(doc, params);
        //替换表格里面的变量
        xwpfTUtil.replaceInTable(doc, params);
        OutputStream os = new FileOutputStream(fileNameOutResource);
        doc.write(os);
        xwpfTUtil.close(os);
        xwpfTUtil.close(is);

        return BeanMapper.map(mydoc, MyDocumentVo.class);
    }

}
