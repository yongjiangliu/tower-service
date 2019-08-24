/**
 * Copyright © 2018亿阳信通. All rights reserved.
 *
 * @author: lyj
 * @version: V1.0
 */
package cn.com.boco.dss.subject.towerqs.common.table;

import cn.com.boco.dss.common.data.JsonData;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 页面表格删除的接口模板
 * @author: lyj
 */
public interface TableDelete {

    /**
     * 删除一条记录
     *
     * @param request
     * @return
     */
    JsonData delete(HttpServletRequest request);
}
