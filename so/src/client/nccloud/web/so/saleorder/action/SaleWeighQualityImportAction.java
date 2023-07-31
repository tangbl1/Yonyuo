package nccloud.web.so.saleorder.action;

import nc.itf.so.ISaleWeighQuality;
import nc.lightapp.framework.web.action.attachment.AttachmentVO;
import nc.lightapp.framework.web.action.attachment.IUploadAction;
import nccloud.framework.core.io.WebFile;
import nccloud.framework.service.ServiceLocator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 导入质量数据
 */
public class SaleWeighQualityImportAction implements IUploadAction {

    ISaleWeighQuality server = ServiceLocator.find(ISaleWeighQuality.class);

    public SaleWeighQualityImportAction() {
    }

    @Override
    public Object doAction(AttachmentVO paras) {

        String title = "";//前端标题
        String whether = "success";//	成功:"success",警告:"warning",出错:"danger"
        String information = "";
        Map<String, String[]> params = paras.getParameters();
        WebFile webFile;
        int rows = 500;
        //操作excel2003以前版本.xls
        HSSFWorkbook readWorkBook = null;
        //表操作
        HSSFSheet sheet = null;
        //行操作
        HSSFRow readRow = null;
        //操作Excel2007的版本，扩展名是.xlsx
        XSSFWorkbook workbook = null;
        XSSFSheet sheetXlsx = null;
        XSSFRow readRow2 = null;

        try {
            WebFile[] files = paras.getFiles();
            if (files != null && files.length > 0) {
                webFile = files[0];
                InputStream fIn = webFile.getInputStream();
                String filename = webFile.getFileName();
                if (filename.endsWith(".xlsx")) {
                    workbook = new XSSFWorkbook(fIn);
                    sheetXlsx = workbook.getSheetAt(0);
                    rows = sheetXlsx.getLastRowNum();
                } else {
                    readWorkBook = new HSSFWorkbook(fIn);
                    sheet = readWorkBook.getSheetAt(0);
                    rows = sheet.getLastRowNum();
                }
                //遍历
                int count = 0;
                int errorCount = 0;

                String[] lshs = new String[rows - 3];
                String[] tfes = new String[rows - 3];
                String[] vehiclenos = new String[rows - 3];
                String[] h2os = new String[rows - 3];
                String[] Technicians = new String[rows - 3];
                for (int i = 0; i < rows - 2; i++) {

                    String lsh = "";//流水号
                    String vehicleno = "";//车号
                    //回单指标
                    String factorygrade_tfe = "";//TFe
                    String factorygrad_h2o = "";//水份%
                    String Laboratory_Technician = "";//化验员
                    if (filename.endsWith(".xlsx")) {
                        readRow2 = sheetXlsx.getRow(i);
                        //流水号
                        XSSFCell lshValue = readRow2.getCell(0);
                        if (lshValue != null) {
                            lshValue.setCellType(CellType.STRING);
                            lsh = lshValue.getStringCellValue();
                        }
                        //车牌号
                        XSSFCell vehiclenoValue = readRow2.getCell(1);

                        if (vehiclenoValue != null) {
                            vehiclenoValue.setCellType(CellType.STRING);
                            vehicleno = vehiclenoValue.getStringCellValue();
                        }
                        //TFe
                        XSSFCell factorygrade_tfeValue = readRow2.getCell(5);
                        if (factorygrade_tfeValue != null) {
                            factorygrade_tfeValue.setCellType(CellType.STRING);
                            factorygrade_tfe = factorygrade_tfeValue.getStringCellValue();
                        }
                        //水分
                        XSSFCell factorygrad_h2oValue = readRow2.getCell(6);
                        if (factorygrad_h2oValue != null) {
                            factorygrad_h2oValue.setCellType(CellType.STRING);
                            factorygrad_h2o = factorygrad_h2oValue.getStringCellValue();
                        }
                        //化验员
                        XSSFCell Laboratory_TechnicianValue = readRow2.getCell(7);
                        if (Laboratory_TechnicianValue != null) {
                            Laboratory_TechnicianValue.setCellType(CellType.STRING);
                            Laboratory_Technician = Laboratory_TechnicianValue.getStringCellValue();
                        }


                    } else if (filename.endsWith(".xls")) {
                        readRow = sheet.getRow(i);
                        //流水号
                        HSSFCell lshValue = readRow.getCell(0);
                        if (lshValue != null) {
                            lshValue.setCellType(CellType.STRING);
                            lsh = lshValue.getStringCellValue();
                        }
                        //车牌号
                        HSSFCell vehiclenoValue = readRow.getCell(1);
                        if (vehiclenoValue != null) {
                            vehiclenoValue.setCellType(CellType.STRING);
                            vehicleno = vehiclenoValue.getStringCellValue();
                        }
                        //TFe
                        HSSFCell factorygrade_tfeValue = readRow.getCell(5);
                        if (factorygrade_tfeValue != null) {
                            factorygrade_tfeValue.setCellType(CellType.STRING);
                            factorygrade_tfe = factorygrade_tfeValue.getStringCellValue();
                        }
                        //水分
                        HSSFCell factorygrad_h2oValue = readRow.getCell(6);
                        if (factorygrad_h2oValue != null) {
                            factorygrad_h2oValue.setCellType(CellType.STRING);
                            factorygrad_h2o = factorygrad_h2oValue.getStringCellValue();
                        }
                        //化验员
                        HSSFCell Laboratory_TechnicianValue = readRow.getCell(7);
                        if (Laboratory_TechnicianValue != null) {
                            Laboratory_TechnicianValue.setCellType(CellType.STRING);
                            Laboratory_Technician = Laboratory_TechnicianValue.getStringCellValue();
                        }

                    }
                    if (i == 0) {
                        if ("车号".equals(vehicleno.replaceAll(" ", "")) && "流水号".equals(lsh.replaceAll(" ", "")) &&
                                "TFe".equals(factorygrade_tfe.replaceAll(" ", "")) && "水份%".equals(factorygrad_h2o.replaceAll(" ", "")) &&
                                "化验员".equals(Laboratory_Technician)) {
                            continue;
                        } else {
                            title = "失败！";
                            whether = "danger";
                            information = "标准excel表A列为“流水号”，B列为“车号”，F列为“TFe”，G列为“水分”，H列为“化验员”，请对照！";
                            break;
                        }
                    }

                    tfes[i - 1] = factorygrade_tfe;
                    vehiclenos[i - 1] = vehicleno;
                    lshs[i - 1] = lsh;
                    h2os[i - 1] = factorygrad_h2o;
                    Technicians[i - 1] = Laboratory_Technician;

                }
                if ("success".equals(whether)) {
                    String message = "Excel失败数据行数:";
                    //更新数据
                    int[] results = server.importdata(tfes, vehiclenos, lshs, h2os, Technicians);
                    //统计成功和失败行数
                    for (int k = 0; k < results.length; k++) {
                        if (results[k] == 0) {
                            message += "【" + (k + 2) + "】";
                            errorCount++;

                        } else {
                            count++;
                        }
                    }
                    title = "成功！";
                    whether = "success";
                    information = "导入完成" + count + "条数据 ; 失败" + errorCount + "条数据 ; ";
                    if (errorCount > 0) {
                        title = "提示！";
                        whether = "warning";
                        information += "" + message + "";
                    }


                }
                fIn.close();


            }


        } catch (IOException e) {
            title = "失败！";
            whether = "danger";
            information = "出错了！";
        }

        Map<String, String> map = new HashMap<>();
        map.put("whether", whether);
        map.put("information", information);
        map.put("title", title);
        return new JSONObject(map);

    }


}
