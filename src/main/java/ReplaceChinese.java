/**
 * @author Administrator
 * @Title: Controller
 * @Description:
 * @date 2020/2/28
 */

import com.sun.javafx.binding.StringFormatter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *@ClassName SearchChinese
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/28 17:03
 *@Version 1.0
 **/
public class ReplaceChinese {

    /**
     * jsp jstl title
     */
    public static String JSP_TITLE_CHINESE_STRING_REG = "(?<=title\\=\"|title\\=\')[\\s\\w]*[\\u3002\\uff1f\\uff01\\uff0c\\u3001\\uff1b\\uff1a\\u201c\\u201d\\u2018\\u2019\\uff08\\uff09\\u300a\\u300b\\u3008\\u3009\\u3010\\u3011\\u300e\\u300f\\u300c\\u300d\\ufe43\\ufe44\\u3014\\u3015\\u2026\\u2014\\uff5e\\ufe4f\\uffe5\\u4e00-\\u9fa5]+[\\s\\w]*(?=\"|\')";
    public static String JSP_TITLE_REPLACE_TEMP = "{0}";

    /**
     * jsp 输入框提示文本
     */
    public static String JSP_PLACEHOLDER_CHINESE_STRING_REG_1 = "(?<=placeholder\\=\")[\\s\\w]*[\\u3002\\uff1f\\uff01\\uff0c\\u3001\\uff1b\\uff1a\\u201c\\u201d\\u2018\\u2019\\uff08\\uff09\\u300a\\u300b\\u3008\\u3009\\u3010\\u3011\\u300e\\u300f\\u300c\\u300d\\ufe43\\ufe44\\u3014\\u3015\\u2026\\u2014\\uff5e\\ufe4f\\uffe5\\u4e00-\\u9fa5]+[\\s\\w]*(?=\")";
    public static String JSP_PLACEHOLDER_REPLACE_TEMP_1 = "<t:mutiLang langKey=''{0}''/>";

    /**
     * jsp 输入框提示文本
     */
    public static String JSP_PLACEHOLDER_CHINESE_STRING_REG_2 = "(?<=placeholder\\=\')[\\s\\w]*[\\u3002\\uff1f\\uff01\\uff0c\\u3001\\uff1b\\uff1a\\u201c\\u201d\\u2018\\u2019\\uff08\\uff09\\u300a\\u300b\\u3008\\u3009\\u3010\\u3011\\u300e\\u300f\\u300c\\u300d\\ufe43\\ufe44\\u3014\\u3015\\u2026\\u2014\\uff5e\\ufe4f\\uffe5\\u4e00-\\u9fa5]+[\\s\\w]*(?=\')";
    public static String JSP_PLACEHOLDER_REPLACE_TEMP_2 = "<t:mutiLang langKey=\"{0}\"/>";

    /**
     * jsp 标签
     */
    public static String JSP_EL_CHINESE_STRING_REG = "(?<=\\>)[\\s\\w]*[\\u3002\\uff1f\\uff01\\uff0c\\u3001\\uff1b\\uff1a\\u201c\\u201d\\u2018\\u2019\\uff08\\uff09\\u300a\\u300b\\u3008\\u3009\\u3010\\u3011\\u300e\\u300f\\u300c\\u300d\\ufe43\\ufe44\\u3014\\u3015\\u2026\\u2014\\uff5e\\ufe4f\\uffe5\\u4e00-\\u9fa5]+[\\s\\w]*(?=\\<|：)";
    public static String JSP_EL_REPLACE_TEMP = "<t:mutiLang langKey=\"{0}\"/>";

    /**
     * java
     */
    public static String JAVA_CHINESE_REG = ".*(\"[\\s\\w]*[\\u3002\\uff1f\\uff01\\uff0c\\u3001\\uff1b\\uff1a\\u201c\\u201d\\u2018\\u2019\\uff08\\uff09\\u300a\\u300b\\u3008\\u3009\\u3010\\u3011\\u300e\\u300f\\u300c\\u300d\\ufe43\\ufe44\\u3014\\u3015\\u2026\\u2014\\uff5e\\ufe4f\\uffe5\\u4e00-\\u9fa5]+[\\s\\w]*\")";
    public static String JAVA_REPLACE_TEMP = "MutiLangUtil.getLang(\"{0}\")";

    public static enum ReplaceEnum {

        JSP_TITLE(JSP_TITLE_CHINESE_STRING_REG,JSP_TITLE_REPLACE_TEMP),
        JSP_PLACEHOLDER_1(JSP_PLACEHOLDER_CHINESE_STRING_REG_1,JSP_PLACEHOLDER_REPLACE_TEMP_1),
        JSP_PLACEHOLDER_2(JSP_PLACEHOLDER_CHINESE_STRING_REG_2,JSP_PLACEHOLDER_REPLACE_TEMP_2),
        JSP_EL(JSP_EL_CHINESE_STRING_REG,JSP_EL_REPLACE_TEMP),
        JAVA(JAVA_CHINESE_REG, JAVA_REPLACE_TEMP);

        private ReplaceEnum(String pattern,String repTemp) {
            this.pattern=pattern;
            this.repTemp=repTemp;
        }

        public String getPattern() {
            return pattern;
        }

        public String getRepTemp() {
            return repTemp;
        }

        private String pattern;
        private String repTemp;

    }

    /**
     * java文件中需要排除的行号 @开头 //开头 或者带@的行中的中文
     */
    public static String JAVA_EXCLUDE_ROW_REG = "(^\\s*@|^\\s*//|^[\\w\\s\\(\\)\\<\\>\\?]*@).+";

    public static String JSP_EXCLUDE_ROW_REG = "(^\\s*\\<\\!\\-\\-|^\\s*//).*";


    public static void replaceFiles(String filePath, ReplaceEnum repEnum, Map<String,String> chineseMap) {
        StringBuffer content = new StringBuffer(readFile(filePath));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(repEnum.pattern,Pattern.MULTILINE|Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(content);
        boolean replaceFlag = false;
        while (m.find()){
            if(StringUtils.isNotEmpty(chineseMap.get(m.group(0).trim()))) {
                replaceFlag = true;
                m.appendReplacement(sb,MessageFormat.format(repEnum.repTemp,chineseMap.get(m.group(0).trim())));
            }
        }
        sb = m.appendTail(sb);
        try {
            if(replaceFlag) {
                FileUtils.writeStringToFile(new File(filePath), sb.toString());
                System.out.println("文件:"+filePath+" 替换成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void replaceJaveFiles(String filePath, ReplaceChinese.ReplaceEnum repEnum, Map<String,String> chineseMap) {
        StringBuffer content = new StringBuffer(readFile(filePath));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(repEnum.getPattern(),Pattern.MULTILINE|Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(content);

        Pattern exPattern = Pattern.compile(SearchChinese.JAVA_EXCLUDE_ROW_REG,Pattern.MULTILINE|Pattern.CASE_INSENSITIVE);
        boolean replaceFlag = false;
        while (m.find()){
            if(!exPattern.matcher(m.group()).matches()) {
                String row = m.group();
                String str = m.group(1);
                String chinese = str.substring(1,str.length()-1);
                String langKey = chineseMap.get(chinese.trim());
                if(StringUtils.isNotEmpty(langKey)) {
                    m.appendReplacement(sb,row.replaceAll(str,MessageFormat.format(repEnum.getRepTemp(),langKey)));
                    replaceFlag = true;
                }
            }
        }
        sb = m.appendTail(sb);
        //导包 import org.jeecgframework.core.util.MutiLangUtil;
        sb = importLangUtil(sb);
        try {
            if(replaceFlag) {
                FileUtils.writeStringToFile(new File(filePath), sb.toString());
                System.out.println("文件:"+filePath+" 替换成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StringBuffer importLangUtil(StringBuffer sb) {
        Pattern importLangUtilReg = Pattern.compile("^import\\s+org\\.jeecgframework\\.core\\.util\\.(MutiLangUtil|\\*);",Pattern.MULTILINE|Pattern.CASE_INSENSITIVE);
        Matcher impMatcher = importLangUtilReg.matcher(sb);
        if(!impMatcher.find()) {
            Pattern importJavaReg = Pattern.compile("^import\\s+org\\.jeecgframework\\.core\\.util.*;",Pattern.MULTILINE|Pattern.CASE_INSENSITIVE);
            impMatcher = importJavaReg.matcher(sb);
            StringBuffer nsb = new StringBuffer();
            if(impMatcher.find()) {
                impMatcher.appendReplacement(nsb,impMatcher.group()+"\r\n"+"import org.jeecgframework.core.util.MutiLangUtil;");
                return impMatcher.appendTail(nsb);
            } else {
                importJavaReg = Pattern.compile("^import.*;",Pattern.MULTILINE|Pattern.CASE_INSENSITIVE);
                impMatcher = importJavaReg.matcher(sb);
                nsb = new StringBuffer();
                if(impMatcher.find()) {
                    impMatcher.appendReplacement(nsb, impMatcher.group() + "\r\n" + "import org.jeecgframework.core.util.MutiLangUtil;");
                    return impMatcher.appendTail(nsb);
                }
            }
        }
        return sb;
    }


    public static void replaceJaveFiles(List<String> fileList, ReplaceEnum repEnum, Map<String,String> chineseMap) {
        fileList.stream().forEach(o -> replaceJaveFiles(o, repEnum, chineseMap));
    }
    /**
     * 查找文件中所有符合正则规则的字符
     * @param fileList
     * @return
     */
    public static void replaceFiles(List<String> fileList, ReplaceEnum repEnum, Map<String,String> chineseMap) {
        fileList.stream().forEach(o -> replaceFiles(o, repEnum, chineseMap));
    }

    public static String readFile(String filePath) {
        try {
            return FileUtils.readFileToString(new File(filePath), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
