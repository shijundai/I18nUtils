/**
 * @author Administrator
 * @Title: Controller
 * @Description:
 * @date 2020/3/14
 */

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *@ClassName FileSearch
 *@Description TODO
 *@Author Administrator
 *@Date 2020/3/14 17:34
 *@Version 1.0
 **/
public class FileSearch {


    public static Set<String> javaFileChineseSet() {
        List<String> files = ListFile.fileList("D:\\workspace\\cnbots\\src\\main\\java\\com\\cnbot\\kindergarten","Controller.java,Biz.java,Service.java",null);
        Map<String, Set<String>> res = SearchChinese.searchFiles(files, new String[]{SearchChinese.JAVA_CHINESE_STRING_REG}, new String[] {SearchChinese.JAVA_EXCLUDE_ROW_REG});
        Set<String> allChineseSet = new HashSet<>();
        res.entrySet().forEach(o -> o.getValue().forEach(allChineseSet::add));
        return allChineseSet;
    }

    public static Set<String> jspFileChineseSet() {
        List<String> files = ListFile.fileList("D:\\workspace\\cnbots\\src\\main\\webapp\\webpage\\com\\cnbot",".jsp",null);
        Map<String, Set<String>> res = SearchChinese.searchFiles(files, new String[]{SearchChinese.JAVA_CHINESE_STRING_REG, SearchChinese.EL_CHINESE_STRING_REG}, new String[] {SearchChinese.JAVA_EXCLUDE_ROW_REG, SearchChinese.JSP_EXCLUDE_ROW_REG});
        Set<String> allChineseSet = new HashSet<>();
        res.entrySet().forEach(o -> o.getValue().forEach(allChineseSet::add));
        return allChineseSet;
    }

    public static void findJavaFileChinese() {
        List<String> files = ListFile.fileList("D:\\workspace\\cnbots\\src\\main\\java\\com\\cnbot\\kindergarten","Controller.java",null);
        Map<String, Set<String>> res = SearchChinese.searchFiles(files, new String[]{SearchChinese.JAVA_CHINESE_STRING_REG}, new String[] {SearchChinese.JAVA_EXCLUDE_ROW_REG});
        Set<String> allChineseSet = new HashSet<>();
        res.entrySet().forEach(o -> o.getValue().forEach(allChineseSet::add));
        System.out.println(allChineseSet.size());
        allChineseSet.forEach(System.out::println);
    }


    public static void findJspFileChinesee() {
        List<String> files = ListFile.fileList("D:\\workspace\\cnbots\\src\\main\\webapp\\webpage",".jsp",null);
        Map<String, Set<String>> res = SearchChinese.searchFiles(files, new String[]{SearchChinese.JAVA_CHINESE_STRING_REG, SearchChinese.EL_CHINESE_STRING_REG}, new String[] {SearchChinese.JAVA_EXCLUDE_ROW_REG, SearchChinese.JSP_EXCLUDE_ROW_REG});
        Set<String> allChineseSet = new HashSet<>();
        res.entrySet().forEach(o -> o.getValue().forEach(allChineseSet::add));
        System.out.println(allChineseSet.size());
        allChineseSet.forEach(System.out::println);
    }


}
