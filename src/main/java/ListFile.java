/**
 * @author Administrator
 * @Title: Controller
 * @Description:
 * @date 2020/2/27
 */

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *@ClassName ListFile
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/27 18:27
 *@Version 1.0
 **/
public class ListFile {

    public static List<String> fileList(String file) {
        return fileList(file,null, null);
    }

    public static List<String> fileList(List<String> fileList) {
        return fileList(fileList,null, null);
    }


    public static List<String> fileList(String file,String includes, String excludes) {
        List<String> fileListRes = new ArrayList<>();
        fileList(file,fileListRes,includes,excludes);
        return fileListRes;
    }

    public static List<String> fileList(List<String> fileList,String includes, String excludes) {
        List<String> fileListRes = new ArrayList<>();
        fileList.stream().forEach(file -> fileList(file,fileListRes,includes,excludes));
        return fileListRes;
    }

    public static void fileList(String file,List<String> fileList, String include, String exclude) {
        File f = new File(file);
        if(f.isFile()) {
            fileList.add(file);
        } else {
            String[] includes = StringUtils.split(include, ",");
            String[] excludes = StringUtils.split(exclude, ",");
            List<String> excludeList = excludes != null ? Arrays.asList(excludes) : null;
            List<String> includeList = includes != null ? Arrays.asList(includes) : null;
            File[] fs = f.listFiles(fp -> {
                String exName = getExName(fp.getAbsolutePath());
                if(fp.isDirectory()) {
                    return true;
                }
                if(exName == null || (CollectionUtils.isEmpty(excludeList) && CollectionUtils.isEmpty(includeList))) {
                    return true;
                } else if(CollectionUtils.isNotEmpty(includeList) && (includeList.contains(exName) || includeList.stream().anyMatch(o -> StringUtils.contains(fp.getAbsolutePath(),o)))) {
                    return true;
                } else if(CollectionUtils.isNotEmpty(excludeList) && (excludeList.contains(exName) || excludeList.stream().anyMatch(o -> StringUtils.contains(fp.getAbsolutePath(),o)))) {
                    return false;
                }
               return false;
            });
            Arrays.asList(fs).stream().forEach(o -> fileList(o.getAbsolutePath(),fileList,include,exclude));
        }
    }

    public static String getExName(String filePath) {
        String exName = StringUtils.substringAfterLast(filePath, ".");
        if(StringUtils.isNotEmpty(exName)) {
            return exName;
        }
        return null;
    }

}
