package nc.pub.base;

import nc.bs.framework.component.RemoteProcessComponent;
import nc.bs.logging.Logger;
import nc.bs.mw.sqltrans.TempTable;
import nc.jdbc.framework.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TempTableCleaner implements RemoteProcessComponent {
	private List<String> tablelist = new ArrayList<String>();

	@Override
	public void preProcess() {
	}
 
	@Override
	public void postProcess() {
		dropTables();
	}

	@Override
	public void postErrorProcess(Throwable thr) {
		dropTables();
	}

	private void dropTables() {
		if (!tablelist.isEmpty()) {
			Connection con = null;
			try {
				con = ConnectionFactory.getConnection();
				TempTable tt = new TempTable();
				for (String tablename : tablelist) {
					try {
						tt.dropTempTable(con, tablename);
					} catch (SQLException e) {
						Logger.error("", e);
					}
				}
			} catch (SQLException e) {
				Logger.error("", e);
			} finally {
				try {
					if (con != null)
						con.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public synchronized void addTable(String tablename) {
		tablelist.add(tablename);
	}
}
