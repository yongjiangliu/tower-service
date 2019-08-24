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
 * @Description: 页面表格excel导入模板下载/模板导入的接口模板
 * @author: lyj
 */
public interface TableTemplateImport {

    /**
     * 导入模板下载
     *
     * @param request
     * @param response
     */
    void templateDownload(HttpServletRequest request, HttpServletResponse response);

    /**
     * 表格数据导入
     *
     * @param request
     * @param response
     */
    void templateImport(HttpServletRequest request, HttpServletResponse response);


}
