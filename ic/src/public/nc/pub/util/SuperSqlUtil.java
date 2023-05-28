package nc.pub.util;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.component.RemoteProcessComponent;
import nc.bs.framework.component.RemoteProcessComponetFactory;
import nc.bs.logging.Logger;
import nc.jdbc.framework.*;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.jdbc.framework.exception.DbException;
import nc.pub.base.TempTableCleaner;
import nc.vo.pub.BusinessException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * ���ݼ��ϻ������� in ('')��䣬����300��ϵͳ�Զ�ʹ����ʱ��
 * 
 * @author zhaoweic
 * 
 * @version NC6.5
 * 
 * @date 2017-9-1
 */
public class SuperSqlUtil { 
	
	public static int inMinLimitCount = 300;// in(...)����Ԫ�ص���С��ֵ�������÷�ֵ��ʱ�򣬿�����Ҫʹ����ʱ���������ʽ
	public static int inMaxLimitCount = 800;// in(...)����Ԫ�ص����ֵ�������÷�ֵ��ʱ��SQL�������ܻ��������ʹ����ʱ��������취

	/**
	 * ���In ���
	 * 
	 * @param fieldName
	 *            �ֶ���
	 * @param pks
	 *            ��������
	 * @return String
	 *            ���� in ('')
	 * @throws BusinessException
	 *            ��ѯ�������׳��쳣
	 * @example   		
	 * 		List<String> pklist = new ArrayList<String>();<br />
	 *	 	pklist.add("1001A910000000000355");<br />
	 *	 	pklist.add("1001A91000000000034T");<br />  
	 *	 	DeptVO deptVo = (DeptVO) superDao.executeQuery(DeptVO.class,SuperSqlUtil.getInStr(DeptVO.PK_DEPT, pklist, false));   
	 */
	public static String getInStr(String fieldName, List<String> pks, boolean autoUseTempTable) throws BusinessException {
		String rsStr = null;

		if (fieldName == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1017pub_0", "01017pub-0067")/* @res "�ֶ�����������ֵ��" */);
		}

		if (pks == null || pks.isEmpty()) {
			rsStr = fieldName + " in ('') ";
		} else {
			int length = pks.size();
			if (length == 0) {
				rsStr = fieldName + " in ('') ";
			} else {
				if (length > inMinLimitCount) {
					if (autoUseTempTable) {
						boolean canCreateTable = false;// ֻ���ڷ����������е�ʱ��ſ��Դ�����ʱ��
						try {
							canCreateTable = RuntimeEnv.getInstance().isRunningInServer();
						} catch (Exception e) {
							Logger.debug(e);
						} catch (Error e) {
							Logger.debug(e);
						}
						if (canCreateTable) {
							try {
								String tablename = getTempTablename(fieldName);
								String colname = "pk";
								String coltype = "varchar(60)";
								tablename = createTempTable(tablename, colname, coltype);
								if (tablename == null) {
									// ��ʱ����ʧ�ܣ����³��Դ���һ��
									tablename = getTempTablename(fieldName);
									tablename = createTempTable(tablename, colname, coltype);
								}
								if (tablename == null) {
									throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1017pub_0", "01017pub-0068")/* @res "������ʱ��ʧ��" */);
								} else {
									insertIntoTable(tablename, colname, pks);
								}
								rsStr = fieldName + " in (select " + colname + " from " + tablename + ") ";
							} catch (Exception e) {
								Logger.error("������ʱ��ʧ�ܡ�����ʹ��OR��ʽ��", e);
								if (length > inMaxLimitCount) {
									// ����in(...)�����ֵ��ʹ��( field in(...) or field in(...) or ...)�ķ�ʽ��ʵ�֣�Ч�ʷǳ���
									StringBuffer sb = new StringBuffer();
									sb.append(" (");
									for (int i = 0; i < length;) {
										sb.append(getInStr(fieldName, pks, i, i + inMaxLimitCount - 1));
										sb.append(" or");
										i = i + inMaxLimitCount;
									}
									rsStr = sb.substring(0, sb.length() - 3) + ") ";
								} else {
									// û�г������ֵ������д��һ��in(...)����
									rsStr = getInStr(fieldName, pks, 0, length - 1);
								}
							}
						} else {
							if (length > inMaxLimitCount) {
								// ����in(...)�����ֵ��ʹ��( field in(...) or field in(...) or ...)�ķ�ʽ��ʵ�֣�Ч�ʷǳ���
								StringBuffer sb = new StringBuffer();
								sb.append(" (");
								for (int i = 0; i < length;) {
									sb.append(getInStr(fieldName, pks, i, i + inMaxLimitCount - 1));
									sb.append(" or");
									i = i + inMaxLimitCount;
								}
								rsStr = sb.substring(0, sb.length() - 3) + ") ";
							} else {
								// û�г������ֵ������д��һ��in(...)����
								rsStr = getInStr(fieldName, pks, 0, length - 1);
							}
						}
					} else {
						if (length > inMaxLimitCount) {
							// ����in(...)�����ֵ��ʹ��( field in(...) or field in(...) or ...)�ķ�ʽ��ʵ�֣�Ч�ʷǳ���
							StringBuffer sb = new StringBuffer();
							sb.append(" (");
							for (int i = 0; i < length;) {
								sb.append(getInStr(fieldName, pks, i, i + inMaxLimitCount - 1));
								sb.append(" or");
								i = i + inMaxLimitCount;
							}
							rsStr = sb.substring(0, sb.length() - 3) + ") ";
						} else {
							// û�г������ֵ������д��һ��in(...)����
							rsStr = getInStr(fieldName, pks, 0, length - 1);
						}
					}
				} else {
					rsStr = getInStr(fieldName, pks, 0, length - 1);
				}
			}
		}
		return rsStr;
	}

	/**
	 * ���In ���
	 * 
	 * @param fieldName
	 *            �ֶ���
	 * @param pks
	 *            ��������
	 * @return String
	 *            ���� in ('')
	 * @throws BusinessException
	 */
	public static String getInStr(String fieldName, String[] pks, boolean autoUseTempTable) throws BusinessException {
		String rsStr = null;

		if (fieldName == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1017pub_0", "01017pub-0067")/* @res "�ֶ�����������ֵ��" */);
		}

		if (pks == null) {
			rsStr = fieldName + " in ('') ";
		} else {
			int length = pks.length;
			if (length == 0) {
				rsStr = fieldName + " in ('') ";
			} else {
				if (length > inMinLimitCount) {
					if (autoUseTempTable) {
						boolean canCreateTable = false;// ֻ���ڷ����������е�ʱ��ſ��Դ�����ʱ��
						try {
							canCreateTable = RuntimeEnv.getInstance().isRunningInServer();
						} catch (Exception e) {
							Logger.debug(e);
						} catch (Error e) {
							Logger.debug(e);
						}
						if (canCreateTable) {
							try {
								String tablename = getTempTablename(fieldName);
								String colname = "pk";
								String coltype = "varchar(60)";
								tablename = createTempTable(tablename, colname, coltype);
								if (tablename == null) {
									// ��ʱ����ʧ�ܣ����³��Դ���һ��
									tablename = getTempTablename(fieldName);
									tablename = createTempTable(tablename, colname, coltype);
								}
								if (tablename == null) {
									throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1017pub_0", "01017pub-0068")/* @res "������ʱ��ʧ��" */);
								} else {
									insertIntoTable(tablename, colname, pks);
								}
								rsStr = fieldName + " in (select " + colname + " from " + tablename + ") ";
							} catch (Exception e) {
								Logger.error("������ʱ��ʧ�ܡ�����ʹ��OR��ʽ��", e);
								if (length > inMaxLimitCount) {
									// ����in(...)�����ֵ��ʹ��( field in(...) or field in(...) or ...)�ķ�ʽ��ʵ�֣�Ч�ʷǳ���
									StringBuffer sb = new StringBuffer();
									sb.append(" (");
									for (int i = 0; i < length;) {
										sb.append(getInStr(fieldName, pks, i, i + inMaxLimitCount - 1));
										sb.append(" or");
										i = i + inMaxLimitCount;
									}
									rsStr = sb.substring(0, sb.length() - 3) + ") ";
								} else {
									// û�г������ֵ������д��һ��in(...)����
									rsStr = getInStr(fieldName, pks, 0, length - 1);
								}
							}
						} else {
							if (length > inMaxLimitCount) {
								// ����in(...)�����ֵ��ʹ��( field in(...) or field in(...) or ...)�ķ�ʽ��ʵ�֣�Ч�ʷǳ���
								StringBuffer sb = new StringBuffer();
								sb.append(" (");
								for (int i = 0; i < length;) {
									sb.append(getInStr(fieldName, pks, i, i + inMaxLimitCount - 1));
									sb.append(" or");
									i = i + inMaxLimitCount;
								}
								rsStr = sb.substring(0, sb.length() - 3) + ") ";
							} else {
								// û�г������ֵ������д��һ��in(...)����
								rsStr = getInStr(fieldName, pks, 0, length - 1);
							}
						}
					} else {
						if (length > inMaxLimitCount) {
							// ����in(...)�����ֵ��ʹ��( field in(...) or field in(...) or ...)�ķ�ʽ��ʵ�֣�Ч�ʷǳ���
							StringBuffer sb = new StringBuffer();
							sb.append(" (");
							for (int i = 0; i < length;) {
								sb.append(getInStr(fieldName, pks, i, i + inMaxLimitCount - 1));
								sb.append(" or");
								i = i + inMaxLimitCount;
							}
							rsStr = sb.substring(0, sb.length() - 3) + ") ";
						} else {
							// û�г������ֵ������д��һ��in(...)����
							rsStr = getInStr(fieldName, pks, 0, length - 1);
						}
					}
				} else {
					rsStr = getInStr(fieldName, pks, 0, length - 1);
				}
			}
		}
		return rsStr;
	}

	private static String getInStr(String fieldName, List<String> pks, int start, int end) {
		start = Math.min(start, end);
		end = Math.max(start, end);
		StringBuffer sb = new StringBuffer();
		sb.append(" ");
		sb.append(fieldName);
		sb.append(" in (");
		String key = null;
		for (int i = start; i < pks.size(); i++) {
			if (i > end) {
				break;
			}
			if (pks.get(i) == null)
				continue;
			key = pks.get(i).trim();
			sb.append("'");
			sb.append(key);
			sb.append("',");
		}
		String inStr = sb.substring(0, sb.length() - 1) + ") ";
		return inStr;
	}

	private static String getInStr(String fieldName, String[] pks, int start, int end) {
		start = Math.min(start, end);
		end = Math.max(start, end);
		StringBuffer sb = new StringBuffer();
		sb.append(" ");
		sb.append(fieldName);
		sb.append(" in (");
		String key = null;
		for (int i = start; i < pks.length; i++) {
			if (i > end) {
				break;
			}
			if (pks[i] == null)
				continue;
			key = pks[i].trim();
			sb.append("'");
			sb.append(key);
			sb.append("',");
		}
		String inStr = sb.substring(0, sb.length() - 1) + ") ";
		return inStr;
	}

	/**
	 * 
	 * ������ʱ����, ���ر�������<=18λ
	 * 
	 * @param fieldName
	 * @return
	 */
	private static String getTempTablename(String fieldName) {
		StringBuffer tableName = new StringBuffer();
		tableName.append("t_");
		String tempStr = fieldName;
		int index = fieldName.indexOf(".");
		if (index >= 0) {
			tempStr = fieldName.substring(index+1);
		}
		if (tempStr.length() > 7) {
			tempStr = tempStr.substring(tempStr.length() - 7);
		}
		tableName.append(tempStr);
		tableName.append(new Random().nextInt(9));
		long currtime = System.currentTimeMillis();
		// ȡ��ǰʱ���ȡ��8Ϊ��Ϊ������
		// 10000000����=10000��=2.7Сʱ����������Ϊһ���̴߳�������ʱ�����û����2.7Сʱ��ʧЧӦ���ǳ��������
		// ���⣬��ʹ���߳�û�н������ٴ�������ʱ���ظ��ĸ��ʣ�ʱ���������10000000ms����random������Ҳ��ȫһ����Ҳ�Ǽ���������������Բ�����
		String tempStr2 = String.valueOf(currtime);
		if (tempStr2.length() > 8) {
			tempStr2 = tempStr2.substring(tempStr2.length() - 8);
		}
		tableName.append(tempStr2);
		return tableName.toString();
	}

	/**
	 * ������ʱ��
	 * 
	 * <p>
	 * �޸ļ�¼��
	 * </p>
	 * 
	 * @param fieldName
	 * @return
	 * @throws SQLException
	 * @see
	 * @since V6.0
	 */
	private static String createTempTable(String tablename, String colname, String coltype) throws SQLException {
		Connection con = null;
		try {
			con = ConnectionFactory.getConnection();
			nc.bs.mw.sqltrans.TempTable tt = new nc.bs.mw.sqltrans.TempTable();
			tablename = tt.createTempTable(con, tablename, " " + colname + " " + coltype + " ", null);

			RemoteProcessComponetFactory irc = (RemoteProcessComponetFactory) NCLocator.getInstance().lookup("RemoteProcessComponetFactory");
			String processname = TempTableCleaner.class.getName();
			RemoteProcessComponent threadScopePostProcess = irc.getThreadScopePostProcess(processname);
			if (threadScopePostProcess == null) {
				TempTableCleaner cleaner = new TempTableCleaner();
				cleaner.addTable(tablename);
				irc.addThreadScopePostProcess(processname, cleaner);
			} else {
				if (threadScopePostProcess instanceof TempTableCleaner) {
					((TempTableCleaner) threadScopePostProcess).addTable(tablename);
				} else {
					TempTableCleaner cleaner = new TempTableCleaner();
					cleaner.addTable(tablename);
					irc.addThreadScopePostProcess(processname + tablename, cleaner);
				}
			}
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
		return tablename;
	}

	/**
	 * �����ݲ�����ʱ��
	 * 
	 * <p>
	 * �޸ļ�¼��
	 * </p>
	 * 
	 * @param tableName
	 * @param colName
	 * @param datas
	 * @throws SQLException
	 * @throws DbException
	 * @see
	 * @since V6.0
	 */
	private static void insertIntoTable(String tableName, String colName, String[] datas) throws SQLException, DbException {
		Connection con = null;
		JdbcSession session = null;
		try {
			PersistenceManager manager = PersistenceManager.getInstance(DataSourceCenter.getInstance().getSourceName());
			manager.setAddTimeStamp(false);
			session = manager.getJdbcSession();
			con = session.getConnection();
			if (con instanceof CrossDBConnection) {
				((CrossDBConnection) con).setAddTimeStamp(false);
			}
			String sql_insert = "insert into " + tableName + " (" + colName + ")  values( ? ) ";

			for (int i = 0; i < datas.length; i++) {
				SQLParameter sqlParam = new SQLParameter();
				sqlParam.addParam(datas[i]);
				session.addBatch(sql_insert, sqlParam);
			}
			session.executeBatch();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
			try {
				if (session != null)
					session.closeAll();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * �����ݲ�����ʱ��
	 * 
	 * <p>
	 * �޸ļ�¼��
	 * </p>
	 * 
	 * @param tableName
	 * @param colName
	 * @param datas
	 * @throws SQLException
	 * @throws DbException
	 * @see
	 * @since V6.0
	 */
	private static void insertIntoTable(String tableName, String colName, Collection<String> datas) throws SQLException, DbException {
		Connection con = null;
		JdbcSession session = null;
		try {
			PersistenceManager manager = PersistenceManager.getInstance(DataSourceCenter.getInstance().getSourceName());
			manager.setAddTimeStamp(false);
			session = manager.getJdbcSession();
			con = session.getConnection();
			if (con instanceof CrossDBConnection) {
				((CrossDBConnection) con).setAddTimeStamp(false);
			}
			String sql_insert = "insert into " + tableName + " (" + colName + ")  values( ? ) ";
			for (String string : datas) {
				SQLParameter sqlParam = new SQLParameter();
				sqlParam.addParam(string);
				session.addBatch(sql_insert, sqlParam);
			}
			session.executeBatch();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
			try {
				if (session != null)
					session.closeAll();
			} catch (Exception e) {
			}
		}
	}

}
