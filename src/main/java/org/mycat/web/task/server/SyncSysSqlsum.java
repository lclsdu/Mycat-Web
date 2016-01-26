package org.mycat.web.task.server;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hx.rainbow.common.core.SpringApplicationContext;
import org.mycat.web.service.ShowService;
import org.mycat.web.task.common.ITask;
import org.mycat.web.util.DataSourceUtils;

/*
 * 异步持久化mycat中数据
 */

public class SyncSysSqlsum implements ITask {
	
	private static final String NAMESPACE = "SYSSQLSUM";

	private static final String SYSPARAM_NAMESPACE = "SYSSHOW";
	
	@Override
	public void excute(String dbName, Date nowDate) {  
 		ShowService showService = (ShowService)SpringApplicationContext.getBean("showService"); 
		List<Map<String,Object>> list = showService.getDao().query(dbName, SYSPARAM_NAMESPACE, "sqlsum");   
		for(Map<String,Object> entry : list){ 
			entry.put("LAST_TM", new Date((long) entry.get("LAST_TIME"))); 
			entry.put("PERCENT_R", entry.get("R%"));
			entry.remove("R%"); 
			entry.put("DB_NAME", DataSourceUtils.getInstance().getDbName(dbName));
			showService.getDao().insert(NAMESPACE, "insert", entry);  
		} 
 
		list.clear(); 
	}

}
