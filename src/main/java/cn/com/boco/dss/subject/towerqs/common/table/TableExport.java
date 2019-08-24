/**
 * Copyright © 2018亿阳信通. All rights reserved.
 *
 * @author: lyj
 * @version: V1.0
 */
package cn.com.boco.dss.subject.towerqs.common.table;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 页面表格导出的接口模板
 * @author: lyj
 */
public interface TableExport {

    /**
     * 表格数据导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    void tableDataExport(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
