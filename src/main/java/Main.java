/**
 * @author Administrator
 * @Title: Controller
 * @Description:
 * @date 2020/2/27
 */

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *@ClassName Main
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/27 17:53
 *@Version 1.0
 **/
public class Main {


    public static void main(String [] args) {
        replaceJsp();
        replaceJava();
//        searchJspChinese();
//        searchJavaChinese();
//        ExcelReader.getDifference();
        //生成多语言sql
//        ExcelReader.generaterSql().forEach(System.out::println);

//        Map<String, String> chineseMap = ExcelReader.getChineseMap();

    }

    /**
     * 替换jsp页面中的中文
     */
    public static void replaceJsp() {
        Map<String, String> chineseMap = ExcelReader.getChineseMap();
        List<String> jspFiles = ListFile.fileList("D:\\workspace\\cnbots_bak\\src\\main\\webapp\\webpage","jsp",null);
        ReplaceChinese.replaceFiles(jspFiles, ReplaceChinese.ReplaceEnum.JSP_TITLE, chineseMap);
        ReplaceChinese.replaceFiles(jspFiles, ReplaceChinese.ReplaceEnum.JSP_EL, chineseMap);
        ReplaceChinese.replaceFiles(jspFiles, ReplaceChinese.ReplaceEnum.JSP_PLACEHOLDER_1, chineseMap);
        ReplaceChinese.replaceFiles(jspFiles, ReplaceChinese.ReplaceEnum.JSP_PLACEHOLDER_2, chineseMap);


//        javaFiles.addAll(ListFile.fileList("D:\\workspace\\cnbots_bak\\src\\main\\java\\com\\cnbot\\constant","Controller.java,Resolver.java,Interceptor.java",null));
//        javaFiles.addAll(ListFile.fileList("D:\\workspace\\cnbots_bak\\src\\main\\java\\com\\cnbot\\environmenta","Controller.java,Resolver.java,Interceptor.java",null));
//        javaFiles.addAll(ListFile.fileList("D:\\workspace\\cnbots_bak\\src\\main\\java\\com\\cnbot\\kindergarten","Controller.java,Resolver.java,Interceptor.java",null));
//        javaFiles.addAll(ListFile.fileList("D:\\workspace\\cnbots_bak\\src\\main\\java\\com\\cnbot\\common","Controller.java,Resolver.java,Interceptor.java",null));

    }

    public static void replaceJava() {
        Map<String, String> chineseMap = ExcelReader.getChineseMap();
        List<String> javaFiles = ListFile.fileList("D:\\workspace\\cnbots_bak\\src\\main\\java\\com\\cnbot","Controller.java,Resolver.java,Interceptor.java",null);
        ReplaceChinese.replaceJaveFiles(javaFiles, ReplaceChinese.ReplaceEnum.JAVA, chineseMap);
    }

    public static String readFile(String filePath) {
        try {
            return FileUtils.readFileToString(new File(filePath), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }



    /**
     * 生成sql
     */
    public static void generateLangSql() {
        //ExcelReader.getChineseMap()
    }

    /**
     * 搜索项目中的中文 且不包含在excel及数据库当中的
     */
    public static void searchJspChinese() {
        Map<String, String> excelChineseMap = ExcelReader.getChineseMap();

        List<String> files = ListFile.fileList("D:\\workspace\\cnbots_bak\\src\\main\\webapp\\webpage\\com\\cnbot","jsp",null);
        files.addAll(ListFile.fileList("D:\\workspace\\cnbots_bak\\src\\main\\webapp\\webpage\\login","jsp",null));
        files.addAll(ListFile.fileList("D:\\workspace\\cnbots_bak\\src\\main\\webapp\\webpage\\main","jsp",null));
        files.addAll(ListFile.fileList("D:\\workspace\\cnbots_bak\\src\\main\\webapp\\webpage\\system","jsp",null));
        Map<String, Set<String>> chinesesMap = SearchChinese.searchFiles(files, new String[]{SearchChinese.EL_CHINESE_STRING_REG,SearchChinese.JAVA_CHINESE_STRING_REG}, new String[]{SearchChinese.JSP_EXCLUDE_ROW_REG,SearchChinese.JAVA_EXCLUDE_ROW_REG});
        Set<String> chineseSet = new LinkedHashSet<>();
        chinesesMap.entrySet().forEach(o -> chineseSet.addAll(o.getValue()));
        excelChineseMap.entrySet().forEach(o -> chineseSet.remove(o.getKey()));
        chineseSet.forEach(System.out::println);
    }

    public static void searchJavaChinese() {
        Map<String, String> excelChineseMap = ExcelReader.getChineseMap();

        List<String> javaFiles = ListFile.fileList("D:\\workspace\\cnbots_bak\\src\\main\\java\\com\\cnbot","Controller.java,Resolver.java,Interceptor.java",null);
        Map<String, Set<String>> chinesesMap = SearchChinese.searchFiles(javaFiles, new String[]{SearchChinese.EL_CHINESE_STRING_REG,SearchChinese.JAVA_CHINESE_STRING_REG}, new String[]{SearchChinese.JSP_EXCLUDE_ROW_REG,SearchChinese.JAVA_EXCLUDE_ROW_REG});
        Set<String> chineseSet = new LinkedHashSet<>();
        chinesesMap.entrySet().forEach(o -> chineseSet.addAll(o.getValue()));
        excelChineseMap.entrySet().forEach(o -> chineseSet.remove(o.getKey()));
        chineseSet.forEach(System.out::println);
    }




    //文件 行数 上下文 中文


    //1.扫描规则
    //jsp页面
    //<title>机器人基础数据</title> <label for="cnbotsTypeId" class="col-sm-3 control-label">机器人类型：</label>  title="机器人类型" placeholder="请输入硬件编号SN" '错误'

    //1. 按文件扫描出文件中所有中文
    //输入 文件夹列表 扫描规则 正则规则
    //输出中文字段集合 去除重复

    //替换
    //1. 扫描中文及对应key值
    //输入 中文及对应key值的文件 扫描规则 正则规则
    //扫描及替换
}
