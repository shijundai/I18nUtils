/**
 * @author Administrator
 * @Title: Controller
 * @Description:
 * @date 2020/3/2
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.*;

/**
 *@ClassName ExcelReader
 *@Description TODO
 *@Author Administrator
 *@Date 2020/3/2 9:55
 *@Version 1.0
 **/
public class ExcelReader {

    public static List<Map<String,String>> readExcel(String file) {
        return readExcel(file,0,1,(row)-> {
                String chinese = row.getCell(0).getStringCellValue();
                String key = row.getCell(1).getStringCellValue();
                return Arrays.asList(ExcelReader.createRowMap(chinese,null,key));
        });
    }

    public static List<Map<String,String>> readExcel(String file,int sheetIndex, int skipRowIndex, IRowsToMap rowsToMap) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            List<Map<String,String>> list = new ArrayList<>(sheet.getLastRowNum());
            for(int i=skipRowIndex;i<=sheet.getLastRowNum();i++) {
                HSSFRow row = sheet.getRow(i);
                list.addAll(rowsToMap.exceRowAnalysis(row));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String,String> createRowMap(String chinese, String english, String key) {
        Map<String,String> rowMap = new HashMap<>();
        rowMap.put("chinese",chinese);
        rowMap.put("english",english);
        rowMap.put("key",key.replaceAll("\r","").replaceAll("\n",""));
        return rowMap;
    }

    public static List<Map<String,String>> createRowMapList(String[] chineses, String[] englishs, String[] keys) {
        List<Map<String,String>> result = new ArrayList<>();
        for(int i=0;i<chineses.length;i++) {
            Map<String,String> rowMap = new HashMap<>();
            String chinese = chineses[i];
            String english = englishs != null && englishs.length > i ? englishs[i] : null;
            String key = keys != null && keys.length > i ? keys[i] : null;
            if(StringUtils.isEmpty(key)) {
                continue;
            }
            result.add(createRowMap(chinese,english,key));
        }
        return result;
    }

    public static List<Map<String,String>> getNewExcel() {
        //任志荣整理的excel
        return ExcelReader.readExcel("D:\\workspace\\I18nUtils\\src\\main\\resources\\20200311.xls",1,1,(row)->{
            String chinese = row.getCell(2).getStringCellValue();
            HSSFCell englishCell = row.getCell(3);
            String english = englishCell != null ? englishCell.getStringCellValue() : null;
            HSSFCell keyCell = row.getCell(5);
            String key = keyCell != null ?keyCell.getStringCellValue() : null;
            String[] chineses = StringUtils.splitPreserveAllTokens(chinese, "\n");
            String[] englishs = StringUtils.splitPreserveAllTokens(english, "\n");
            String[] keys = StringUtils.splitPreserveAllTokens(key, "\n");
            return ExcelReader.createRowMapList(chineses,englishs,keys);
        });
    }

    /**
     *
     * @return
     */
    public static Map<String,String> getChineseMap() {
        //Map 中文 key
        //两张excel表格如何合并成一张
        Map<String,String> chineseMap = new LinkedHashMap<>();
        List<Map<String, String>> excel = getNewExcel();

        //目前系统存在的
        List<Map<String, String>> existLangs = ExcelReader.readExcel("D:\\workspace\\I18nUtils\\src\\main\\resources\\mutilang.xls");
        excel.forEach(o -> {
            if(StringUtils.isNotEmpty(o.get("key")) && !chineseMap.containsKey(o.get("chinese"))) {
                chineseMap.put(o.get("chinese"),o.get("key"));
            }
        });
        existLangs.forEach(o -> {
            if(StringUtils.length(o.get("key")) > 3) {
                chineseMap.put(o.get("chinese"),o.get("key"));
            }
        });
        return chineseMap;
    }

    /**
     *
     */
    public static Map<String,String[]> getDifference() {
        List<Map<String, String>> newExcel = getNewExcel();
        List<Map<String, String>> existLangs = ExcelReader.readExcel("D:\\workspace\\I18nUtils\\src\\main\\resources\\mutilang.xls");
        //检查key是否有重复
        Map<String,String[]> langMap = new LinkedHashMap<>();
        newExcel.forEach(o -> {
            if(!langMap.containsKey(o.get("key"))) {
                langMap.put(o.get("key"),new String[]{o.get("chinese"),o.get("english")});
            }
        });
        //key值不能重复
        existLangs.forEach(o -> {
            if(langMap.containsKey(o.get("key"))) {
                langMap.remove(o.get("key"));
            }
        });

        return langMap;
    }

    public static List<String> generaterSql() {
        String sql = "insert into t_s_muti_lang(id,lang_key,lang_context,lang_code) values (\"{0}\",\"{1}\",\"{2}\",\"{3}\");";
        List<String> list = new ArrayList<>();
        getDifference().entrySet().forEach(o -> {
            list.add(MessageFormat.format(sql,MD51(o.getKey()+"_zh"),o.getKey(),o.getValue()[0],"zh-cn"));
            if(StringUtils.isNotEmpty(o.getValue()[1])) {
                list.add(MessageFormat.format(sql,MD51(o.getKey()+"_en"),o.getKey(),o.getValue()[1],"en"));
            }

        });
        return list;
    }

    public static String MD51(String input) {
        if(input == null || input.length() == 0) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            byte[] byteArray = md5.digest();

            BigInteger bigInt = new BigInteger(1, byteArray);
            // 参数16表示16进制
            String result = bigInt.toString(16);
            // 不足32位高位补零
            while(result.length() < 32) {
                result = "0" + result;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    interface IRowsToMap {

        List<Map<String,String>> exceRowAnalysis(HSSFRow row);

    }

}
