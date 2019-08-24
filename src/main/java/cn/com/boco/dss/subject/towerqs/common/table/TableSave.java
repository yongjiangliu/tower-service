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
 * @Description: 页面表格保存/修改的接口模板
 * @author: lyj
 */
public interface TableSave {

    /**
     * 获取一条记录的详细信息
     *
     * @param requesto
     * @return
     */
    JsonData findOne(HttpServletRequest request);

    /**
     * 新增/更新数据
     *
     * @param request
     * @return
     */
    JsonData save(HttpServletRequest request);

}
