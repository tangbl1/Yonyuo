import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class filedemo {
    public static void main(String[] args) {


        try {
            ///Users/tangbilong/yonyouhome/YonBIP_V3/nccloudUploadTemp/upload_f843d057_4922_4dc3_8981_97fd6c97cb3b_00000003.tmp
            File file = new File("/Users/tangbilong/project management/hw project/罕王地磅销售/质量数据0506.xlsx");
//            File file = new File("Users/tangbilong/yonyouhome/YonBIP_V3/nccloudUploadTemp/upload_f843d057_4922_4dc3_8981_97fd6c97cb3b_00000003.tmp");
            String path = file.toString();
//            InputStream fIn = new FileInputStream(file);
            int rows = 500;
            //操作excel2003以前版本.xls
            HSSFWorkbook readWorkBook= null;
            //表操作
            HSSFSheet sheet = null;
            //行操作
            HSSFRow readRow = null;
            //操作Excel2007的版本，扩展名是.xlsx
            XSSFWorkbook workbook = null;
            XSSFSheet sheetXlsx = null;
            XSSFRow readRow2 = null;

            OPCPackage opcPackage = OPCPackage.open(file);

            if (path.endsWith(".xlsx")) {
                workbook =  new XSSFWorkbook(opcPackage);
                sheetXlsx = workbook.getSheetAt(0);
                rows = sheetXlsx.getLastRowNum();
            } else if(path.endsWith(".xls")){
//                readWorkBook = new HSSFWorkbook(fIn);
//                sheet = readWorkBook.getSheetAt(0);
//                rows = sheet.getLastRowNum();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }


    }
}
