package com.newaim.core.utils.mail;

import com.newaim.core.utils.mail.api.HtmlHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {
    /**
     * 替换指定标签的属性和值
     *
     * @param str       需要处理的字符串
     * @param tag       标签名称
     * @param tagAttrib 要替换的标签属性值
     * @param startTag  新标签开始标记
     * @param endTag    新标签结束标记
     * @return
     */
    public static String replaceHtmlTag(HtmlHandler handler, String str, String tag, String tagAttrib, String startTag, String endTag) {
        HtmlHandler outHandle = handler;

        String regxpForTag = "<\\s*" + tag + "\\s+([^>]*)\\s*";
        String regxpForTagAttrib = tagAttrib + "=\\s*\"([^\"]+)\"";
        Pattern patternForTag = Pattern.compile(regxpForTag, Pattern.CASE_INSENSITIVE);
        Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib, Pattern.CASE_INSENSITIVE);
        Matcher matcherForTag = patternForTag.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result = matcherForTag.find();
        while (result) {
            StringBuffer sbreplace = new StringBuffer("<" + tag + " ");
            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));

            if (matcherForAttrib.find()) {
                String attributeStr = matcherForAttrib.group(1);
                attributeStr = outHandle.getPartContent(attributeStr);
                matcherForAttrib.appendReplacement(sbreplace, startTag + attributeStr + endTag);
            }
            matcherForAttrib.appendTail(sbreplace);
            matcherForTag.appendReplacement(sb, sbreplace.toString());
            result = matcherForTag.find();
        }
        matcherForTag.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        StringBuffer content = new StringBuffer();
        content.append("<ul class=\"imgBox\"><li><img id=\"160424\" src=\"uploads/allimg/160424/1-160424120T1-50.jpg\" class=\"src_class\"></li>");
        content.append("<li><img id=\"150628\" src=\"uploads/allimg/150628/1-15062Q12247.jpg\" class=\"src_class\"></li></ul>");
        System.out.println("原始字符串为:" + content.toString());
        String newStr = replaceHtmlTag(new HtmlHandler() {
            @Override
            public String getPartContent(String content) {
                return "XX:X";
            }
        },content.toString(), "img", "src", "src=\"http://junlenet.com/", "\"");
        System.out.println("替换后为:" + newStr);
    }

}