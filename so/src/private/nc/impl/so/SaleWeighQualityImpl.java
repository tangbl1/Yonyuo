package nc.impl.so;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.itf.so.ISaleWeighQuality;
import nc.jdbc.framework.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SaleWeighQualityImpl implements ISaleWeighQuality {

    private final String DEFAULT_DS = "design";
    @Override
    public int[] importdata(String[] tfes, String[] vehiclenos, String[] lshs, String[] h2os, String[] Technicians) {
        int [] j = new int [tfes.length];
        String sourceName = getSourceName();
        try {
            //获取数据库连接
            Connection con = ConnectionFactory.getConnection(sourceName);
            //创建statement对象
            Statement stm = con.createStatement();
            //循环
            for(int i =0;i<vehiclenos.length;i++){
                String vehicleno = vehiclenos[i].replace(" ","");
                String lsh = lshs[i];
                String tfe = tfes[i];
                String Technician = Technicians[i];
                String h2o = h2os[i];
//				 stm.addBatch("update so_saleorder_b set vbdef3='"+tfe+"',vbdef4='"+h2o+"',vbdef5='"+Technician+"' where vbdef1='"+vehicleno+"' and vbdef2='"+checkjtime+"' and dr = 0");
                stm.addBatch("update so_saleorder_b set factorygrade_tfe='"+tfe+"',factorygrad_h2o='"+h2o+"',vbdef20='"+Technician+"' where REPLACE(vbdef2,' ','')='"+vehicleno+"' and csaleorderid in (select csaleorderid from so_saleorder where vdef2 ='"+lsh+"' and fstatusflag = 1 and dr = 0) and dr = 0");//子表vdef2  车牌号 主表 流水号
            }
            j = stm.executeBatch();

        } catch (SQLException e1) {
            // TODO 自动生成的 catch 块
            e1.printStackTrace();
        }

        return j;
    }

    /**
     * 得到数据源名称
     *
     * @return 数据源名称
     */
    public String getSourceName() {
        String dataSource = InvocationInfoProxy.getInstance()
                .getUserDataSource();
        return dataSource == null ? DEFAULT_DS : dataSource;
    }


}
