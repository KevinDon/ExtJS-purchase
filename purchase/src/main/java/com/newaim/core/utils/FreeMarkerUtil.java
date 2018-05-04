package com.newaim.core.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class FreeMarkerUtil {

    private static FreeMarkerUtil instance;
    private Configuration config;

    String templatePath = "/templates/";

    /**
     * instance FreeMarkerUtil
     * @return
     */
    public static FreeMarkerUtil instance() {
        if (instance == null) {
            instance = new FreeMarkerUtil();
        }
        return instance;
    }

    /**
     * instance Configuration
     * @param request
     */
    private void configInstance(HttpServletRequest request) throws IOException {
        if (this.config == null) {
            this.config = new Configuration();
            this.config.setDirectoryForTemplateLoading(new File(Thread.currentThread().getContextClassLoader().getResource("").getPath() + templatePath));
        }
    }

    /**
     * 通过模板文件生成字符串
     * @param request
     * @param templateFileName
     * @param propMap
     * @return
     */
    public String geneFileStr(HttpServletRequest request,String templateFileName, Map<String, Object> propMap) throws IOException {
        String result = "";
        StringWriter out = new StringWriter();
        Template template;
        try {
            configInstance(request);
            template = this.config.getTemplate(templateFileName,"UTF-8");
            template.setEncoding("utf-8");
            template.process(propMap, out);
            result = out.getBuffer().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }

        return result;
    }

    public Writer geneFileForWriter(HttpServletRequest request,String templateFileName, Map<String, Object> propMap, Writer out) {
        Template template;
        try {
            configInstance(request);
            template = this.config.getTemplate(templateFileName,"UTF-8");
            template.setEncoding("utf-8");
            template.process(propMap, out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return out;
    }

}
