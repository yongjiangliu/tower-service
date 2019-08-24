package cn.com.boco.dss.subject.towerqs.index.respository;


import cn.com.boco.dss.subject.towerqs.tower.domain.Tower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yxy on 2019/08/16 11:13
 * Version 1.0.0
 * Description 铁塔信息dao类
 */
@Repository
public interface TowerInfoRespository extends JpaRepository<Tower, String> {

    /**
     * 获取AB两级隐患每个级别对应的隐患数量(地市)
     *
     * @return
     */
    @Query(value = "SELECT sum(case when t.RiskLevel=1 then 1 else 0 end) \"A\",sum(case when t.RiskLevel=2 then 1 else 0 end) \"B\"  FROM dim_ne_tower t where t.AreaID in (select  d.areaId   from dim_geo_area d  where d.provinceId=?1) ", nativeQuery = true)
    public List<Object> findRiskNumGroupByRiskLevelOfArea(Integer provinceId);

    /**
     * 获取AB两级隐患每个级别对应的隐患数量(区县)
     *
     * @return
     */
    @Query(value = "SELECT sum(case when t.RiskLevel=1 then 1 else 0 end) \"A\",sum(case when t.RiskLevel=2 then 1 else 0 end) \"B\"  FROM dim_ne_tower t where t.Location in (?1) ", nativeQuery = true)
    public List<Object> findRiskNumGroupByRiskLevelOfCity(List<String> cityNames);


    /**
     * 获取每个隐患部位的隐患数量(地市)
     *
     * @return
     */
    @Query(value = "SELECT tr.RiskPosition,sum(1) Num FROM dw_tower_risk tr ,dim_risk r, dim_ne_tower t where tr.RiskID=r.RiskID and tr.TowerID=t.TowerID and tr.riskstatus!=2 and t.AreaID in (select  d.areaId   from dim_geo_area d  where d.provinceId=?1) group by tr.RiskPosition", nativeQuery = true)
    public List<Object> findRiskNumGroupByRiskPositionOfArea(Integer provinceId);

    /**
     * 获取每个隐患部位的隐患数量(区县)
     *
     * @return
     */
    @Query(value = "SELECT tr.RiskPosition,sum(1) Num FROM dw_tower_risk tr ,dim_risk r, dim_ne_tower t where tr.RiskID=r.RiskID and tr.TowerID=t.TowerID and tr.riskstatus!=2 and t.Location in (?1) group by tr.RiskPosition", nativeQuery = true)
    public List<Object> findRiskNumGroupByRiskPositionOfCity(List<String> cityNames);


    /**
     * 获取省端用户视角下每个地市下对应的AB类隐患数量（地市）
     *
     * @return
     */
    @Query(value = "select tt.areaName,tt.A,tt.B from (select dim.areaName,sum(case when t.RiskLevel=1 then 1 else 0 end) \"A\",sum(case when t.RiskLevel=2 then 1 else 0 end) \"B\"  from (select d.areaName,d.areaId from dim_geo_area d  where d.provinceId=?1) dim LEFT JOIN dim_ne_tower t on dim.areaId=t.AreaID GROUP BY dim.areaId  ) tt order by  (tt.A+tt.B) desc ", nativeQuery = true)
    public List<Object> findTowerNumOfArea(Integer provinceId);

    /**
     * 获取地市用户视角下某个地市下每个区县AB类隐患数量（区县）
     *
     * @return
     */
    @Query(value = "select tt.Location,tt.A,tt.B from ( select  t.Location,sum(case when t.RiskLevel=1 then 1 else 0 end) \"A\",sum(case when t.RiskLevel=2 then 1 else 0 end) \"B\" from   dim_ne_tower t WHERE t.Location in (?1) GROUP BY t.Location  ) tt order by  (tt.A+tt.B) desc", nativeQuery = true)
    public List<Object> findTowerNumOfCity(List<String> cityNames);


    /**
     * 获取每个处理状态下的铁塔数量(地市)
     *
     * @return
     */
    @Query(value = "SELECT (case when t.Status=0 then '正常' when t.Status=1 then '已下发巡检工单' when t.Status=2 then '已下发检测工单' when t.Status=3 then '整治改造中' else '其它' end ) as \"处理结果\" ,count(1) as \"数量\" FROM dim_ne_tower t where t.AreaID in (select  d.areaId   from dim_geo_area d  where d.provinceId=?1)  group by t.Status", nativeQuery = true)
    public List<Object> findTowerNumGroupByStatusOfArea(Integer provinceId);

    /**
     * 获取每个处理状态下的铁塔数量(区县)
     *
     * @return
     */
    @Query(value = "SELECT (case when t.Status=0 then '正常' when t.Status=1 then '已下发巡检工单' when t.Status=2 then '已下发检测工单' when t.Status=3 then '整治改造中' else '其它' end ) as \"处理结果\" ,count(1) as \"数量\" FROM dim_ne_tower t where t.Location in (?1) group by t.Status", nativeQuery = true)
    public List<Object> findTowerNumGroupByStatusOfCity(List<String> cityNames);


    /**
     * 显示热力图(地市)
     *
     * @return
     */
    @Query(value = "select  t.TowerName,t.CoordinateE,t.CoordinateN ,(case when t.RiskLevel=1 then \"A\" when t.RiskLevel=2 then \"B\" else \"其它\" end) RiskLevel,t.CheckRiskCount,t.CheckRiskCountA,t.DetectionRiskCount,t.DetectionRiskCountA  from  dim_ne_tower t where  t.RiskLevel!=0 and t.AreaID in (select  d.areaId   from dim_geo_area d  where d.provinceId=?1) ", nativeQuery = true)
    public List<Object> findMapDataOfArea(Integer provinceId);

    /**
     * 显示热力图(区县)
     *
     * @return
     */
    @Query(value = "select  t.TowerName,t.CoordinateE,t.CoordinateN ,(case when t.RiskLevel=1 then \"A\" when t.RiskLevel=2 then \"B\" else \"其它\" end) RiskLevel,t.CheckRiskCount,t.CheckRiskCountA,t.DetectionRiskCount,t.DetectionRiskCountA  from  dim_ne_tower t where  t.RiskLevel!=0 and t.Location in (?1)", nativeQuery = true)
    public List<Object> findMapDataOfCity(List<String> cityNames);

}
