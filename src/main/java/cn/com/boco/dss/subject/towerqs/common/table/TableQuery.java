/**
 * Copyright © 2018亿阳信通. All rights reserved.
 *
 * @author: lyj
 * @version: V1.0
 */
package cn.com.boco.dss.subject.towerqs.common.table;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 页面表格查询的接口模板
 * @author: lyj
 */
public interface TableQuery {
    /**
     * 加载表格数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    String getTableData(HttpServletRequest request) throws Exception;

}
