package nccloud.web.so.saleorder.action;

import nc.bs.trade.business.HYPubBO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.lightapp.framework.web.action.attachment.AttachmentVO;
import nc.lightapp.framework.web.action.attachment.IUploadAction;
import nc.vo.ic.m4c.entity.SaleOutBodyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SoCalcPriceVO;
import nccloud.framework.core.io.WebFile;
import nccloud.framework.service.ServiceLocator;
import nccloud.web.so.saleorder.util.SoCalcPriceUtil;
import org.apache.commons.lang3.StringUtils;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleWeighReceiptImportAction implements IUploadAction {


    public SaleWeighReceiptImportAction() {
    }

    private static final Map<String, String> grade = new HashMap<>();    //状态映射，用于获取品位对应编码

    static {
        //单据状态做映射，已完成、申请中、部门审批完成、准备出发、出发、到达、返程、休息、值班。
        grade.put("1", "TFe");
        grade.put("2", "SiO2");
        grade.put("3", "S");
        grade.put("4", "P");
        grade.put("5", "TiO2");
        grade.put("6", "H2O");
    }

    @Override
    public Object doAction(AttachmentVO paras) {

        HYPubBO hypubbo = new HYPubBO();
        String title = "";//前端标题
        String whether = "success";//	成功:"success",警告:"warning",出错:"danger"
        String information = "";
        WebFile webFile;
        int rows;
        //操作excel2003以前版本.xls
        HSSFWorkbook readWorkBook;
        //表操作
        HSSFSheet sheet = null;
        //行操作
        HSSFRow readRow;
        //操作Excel2007的版本，扩展名是.xlsx
        XSSFWorkbook workbook;
        XSSFSheet sheetXlsx = null;
        XSSFRow readRow2;

        //遍历
        int count = 0;
        int errorCount = 0;
        StringBuilder message = new StringBuilder("Excel失败数据行数:");

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

                for (int j = 3; j < rows + 1; j++) {//从第三行开始导入
                    String checkjtime = "";//检斤时间
                    String batchno = "";//批号
                    String vehicleno = "";//车牌号
                    //称重回单指标
                    String S = "";
                    String SiO2 = "";
                    String TFe = "";
                    String TiO2 = "";
                    String H2O = "";
                    String sedreceiptnum = "";//第二次回单干重
                    String sz = "";//第二次回单湿重
                    String custName = "";//供应商名称
                    if (filename.endsWith(".xlsx")) {
                        readRow2 = sheetXlsx.getRow(j);
                        XSSFCell checkjtimeValue = readRow2.getCell(1);
                        if (checkjtimeValue != null) {
                            checkjtimeValue.setCellType(CellType.STRING);
                            checkjtime = checkjtimeValue.getStringCellValue();
                        }

                        XSSFCell dateValue = readRow2.getCell(2);
                        if (dateValue != null) {
                            dateValue.setCellType(CellType.STRING);
                        }

                        XSSFCell batchnoValue = readRow2.getCell(10);
                        if (batchnoValue != null) {
                            batchnoValue.setCellType(CellType.STRING);
                            batchno = batchnoValue.getStringCellValue();

                        }

                        XSSFCell vehiclenoValue = readRow2.getCell(11);
                        if (vehiclenoValue != null) {
                            vehiclenoValue.setCellType(CellType.STRING);
                            vehicleno = vehiclenoValue.getStringCellValue();
                        }

                        XSSFCell SValue = readRow2.getCell(13);
                        if (SValue != null) {
                            SValue.setCellType(CellType.STRING);
                            S = SValue.getStringCellValue();
                        }

                        XSSFCell SiO2Value = readRow2.getCell(14);
                        if (SiO2Value != null) {
                            SiO2Value.setCellType(CellType.STRING);
                            SiO2 = SiO2Value.getStringCellValue();
                        }
                        XSSFCell TFeValue = readRow2.getCell(15);
                        if (TFeValue != null) {
                            TFeValue.setCellType(CellType.STRING);
                            TFe = TFeValue.getStringCellValue();
                        }
                        XSSFCell TiO2Value = readRow2.getCell(17);
                        if (TiO2Value != null) {
                            TiO2Value.setCellType(CellType.STRING);
                            TiO2 = TiO2Value.getStringCellValue();
                        }
                        XSSFCell H2OValue = readRow2.getCell(18);
                        if (H2OValue != null) {
                            H2OValue.setCellType(CellType.STRING);
                            H2O = H2OValue.getStringCellValue();
                        }
                        XSSFCell sedreceiptnumValue = readRow2.getCell(20);
                        if (sedreceiptnumValue != null) {
                            sedreceiptnumValue.setCellType(CellType.STRING);
                            sedreceiptnum = sedreceiptnumValue.getStringCellValue();
                        }
                        XSSFCell szValue = readRow2.getCell(19);
                        if (szValue != null) {
                            szValue.setCellType(CellType.STRING);
                            sz = szValue.getStringCellValue();
                        }
                        XSSFCell custNameValue = readRow2.getCell(5);
                        if (custNameValue != null) {
                            custNameValue.setCellType(CellType.STRING);
                            custName = custNameValue.getStringCellValue();
                        }

                    } else if (filename.endsWith(".xls")) {
                        assert sheet != null;
                        readRow = sheet.getRow(j);
                        HSSFCell checkjtimeValue = readRow.getCell(1);
                        if (checkjtimeValue != null) {
                            checkjtimeValue.setCellType(CellType.STRING);//设置为字符串
                            checkjtime = checkjtimeValue.getStringCellValue();
                        }

                        HSSFCell dateValue = readRow.getCell(2);
                        if (dateValue != null) {
                            dateValue.setCellType(CellType.STRING);
                        }

                        HSSFCell batchnoValue = readRow.getCell(10);
                        if (batchnoValue != null) {
                            batchnoValue.setCellType(CellType.STRING);
                            batchno = batchnoValue.getStringCellValue();

                        }

                        HSSFCell vehiclenoValue = readRow.getCell(11);
                        if (vehiclenoValue != null) {
                            vehiclenoValue.setCellType(CellType.STRING);
                            vehicleno = vehiclenoValue.getStringCellValue();
                        }

                        HSSFCell SValue = readRow.getCell(13);
                        if (SValue != null) {
                            SValue.setCellType(CellType.STRING);
                            S = SValue.getStringCellValue();
                        }

                        HSSFCell SiO2Value = readRow.getCell(14);
                        if (SiO2Value != null) {
                            SiO2Value.setCellType(CellType.STRING);
                            SiO2 = SiO2Value.getStringCellValue();
                        }
                        HSSFCell TFeValue = readRow.getCell(15);
                        if (TFeValue != null) {
                            TFeValue.setCellType(CellType.STRING);
                            TFe = TFeValue.getStringCellValue();
                        }
                        HSSFCell TiO2Value = readRow.getCell(17);
                        if (TiO2Value != null) {
                            TiO2Value.setCellType(CellType.STRING);
                            TiO2 = TiO2Value.getStringCellValue();
                        }
                        HSSFCell H2OValue = readRow.getCell(18);
                        if (H2OValue != null) {
                            H2OValue.setCellType(CellType.STRING);
                            H2O = H2OValue.getStringCellValue();
                        }
                        HSSFCell sedreceiptnumValue = readRow.getCell(20);
                        if (sedreceiptnumValue != null) {
                            sedreceiptnumValue.setCellType(CellType.STRING);
                            sedreceiptnum = sedreceiptnumValue.getStringCellValue();
                        }
                        HSSFCell szValue = readRow.getCell(19);
                        if (szValue != null) {
                            szValue.setCellType(CellType.STRING);
                            sz = szValue.getStringCellValue();
                        }
                        HSSFCell custNameValue = readRow.getCell(5);
                        if (custNameValue != null) {
                            custNameValue.setCellType(CellType.STRING);
                            custName = custNameValue.getStringCellValue();
                        }
                    }
                    if (StringUtils.isEmpty(checkjtime) || StringUtils.isEmpty(vehicleno) || StringUtils.isEmpty(sz)) {

                        message.append("【").append(j + 1).append("】");
                        errorCount++;

                    } else {
                        //根据发货日期和车牌号，湿重查询销售订单表体
                        String sql =
                                " substr(vbdef18,0,10)= '" + checkjtime.substring(0, 10) + "' "
                                        + " and " +
                                        " REPLACE(vbdef2,' ','') ='" + vehicleno.replace(" ","")+ "'"//车号去空格
                                        + "and firstreceiptnum='" + sz + "'";
                        SaleOrderBVO[] bvos = (SaleOrderBVO[]) hypubbo.queryByCondition(SaleOrderBVO.class, sql);
                        if (bvos.length == 0) {
                            message.append("【").append(j + 1).append("】");
                            errorCount++;

                        } else {
                            SaleOrderBVO bvo = bvos[0];
                            String deliverDate = bvo.getVbdef18();//发货日期
                            //更新到到表体数据中
                            bvo.setBatchno(batchno);
                            bvo.setAttributeValue("vbdef1", batchno);
                            bvo.setVehicleno(vehicleno);
                            bvo.setCheckjtime(new UFDateTime(checkjtime));
                            bvo.setArrivalgrade_s(new UFDouble(S));
                            bvo.setArrivalgrade_sio2(new UFDouble(SiO2));
                            bvo.setArrivalgrade_tfe(new UFDouble(TFe));
                            bvo.setArrivalgrade_tio2(new UFDouble(TiO2));
                            bvo.setArrivalgrade_h2o(new UFDouble(H2O));
                            bvo.setAttributeValue("vbdef19", sz);//第二次回单湿重
                            bvo.setSedreceiptnum(new UFDouble(sedreceiptnum));//第二次回单干量

                            Map<String, Integer> zbMap = new HashMap<>(); // 存放指标的map
                            zbMap.put("TFe", 15);
                            zbMap.put("SiO2", 14);
                            zbMap.put("TiO2", 17);
                            zbMap.put("S", 13);
                            zbMap.put("批号", 10);
                            zbMap.put("入库日期", 0);
                            zbMap.put("水分", 10);
                            zbMap.put("供应商名称", 5);

                            UFDouble price = UFDouble.ZERO_DBL;
                            // 计算价格
                            price = this.calcPrice(deliverDate, sheet.getRow(j), zbMap);

                            bvo.setAttributeValue("unitprice", price);//计算单价
                            bvo.setAttributeValue("norigtaxprice", price);//计算单价
                            bvo.setAttributeValue("nqtorigtaxprice", price);//计算单价

                            UFDouble vbdef8 = bvo.getVbdef8() == null ? UFDouble.ZERO_DBL : new UFDouble(bvo.getVbdef8());//湿重
                            Object arrivalgrade_h2o = bvo.getArrivalgrade_h2o() == null ? UFDouble.ZERO_DBL : new UFDouble(bvo.getArrivalgrade_h2o());//水分
                            UFDouble gz = vbdef8.sub(new UFDouble(arrivalgrade_h2o.toString()).div(100));
                            bvo.setAttributeValue("nnum",gz);//主数量
                            bvo.setAttributeValue("nastnum",gz);//数量
                            bvo.setAttributeValue("dr", 0);
                            hypubbo.update(bvo);

                            //获取主键查看是否已经生成销售订单，如果生成，回写出库单批次号
                            String csaleorderbid = bvo.getCsaleorderbid();
                            String strWhere = " csourcebillbid='" + csaleorderbid + "'";
                            SaleOutBodyVO[] saleoutbodyvos = (SaleOutBodyVO[]) hypubbo.queryByCondition(SaleOutBodyVO.class, strWhere);
                            if (saleoutbodyvos != null && saleoutbodyvos.length > 0) {
                                saleoutbodyvos[0].setVbdef1(bvo.getVbdef1());
                            }
                            count++;
                        }
                    }
                }
                fIn.close();
            }

        } catch (Exception e) {
            title = "失败！";
            whether = "danger";
            information = "出错了！错误信息：" + e;
        }

        if ("success".equals(whether)) {
            title = "完成！";
            whether = "success";
            information = "导入完成" + count + "条数据 ; 失败" + errorCount + "条数据 ; ";

            if (errorCount > 0) {
                title = "提示！";
                whether = "warning";
                information += message;
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("whether", whether);
        map.put("information", information);
        map.put("title", title);
        return new JSONObject(map);
    }

    private UFDouble calcPrice(String deliverDate, HSSFRow hssfRow,
                               Map<String, Integer> zbMap) throws BusinessException {
        UFDouble price;
        UFDouble basePrice;
        List<String> zbList;
        String queryZB = "select distinct target from uapbd_addprice where dr=0";//查询维护的指标
        IUAPQueryBS queryBS = ServiceLocator.find(IUAPQueryBS.class);
        zbList = (List<String>) queryBS.executeQuery(queryZB, new ColumnListProcessor());
        SoCalcPriceVO[] calcPriceVOs = new SoCalcPriceVO[zbList.size()];
        for (int i = 0; i < zbList.size(); i++) {
            calcPriceVOs[i] = new SoCalcPriceVO();
            calcPriceVOs[i].setCustomer_name(hssfRow.getCell(zbMap.get("供应商名称")).toString());
            calcPriceVOs[i].setZbmc(zbList.get(i));
            calcPriceVOs[i].setValue(new UFDouble(hssfRow.getCell(zbMap.get(grade.get(zbList.get(i))))
                    .toString()));
            calcPriceVOs[i].setDate(deliverDate);
        }

        price = new SoCalcPriceUtil().calcPrice(calcPriceVOs);//变价查询
        try {
            basePrice = new SoCalcPriceUtil().queryBasePrice(calcPriceVOs[0]);//基价查询
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        return price.add(basePrice);
    }
}
