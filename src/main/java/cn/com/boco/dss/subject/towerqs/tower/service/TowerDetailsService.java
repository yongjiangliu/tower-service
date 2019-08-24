package cn.com.boco.dss.subject.towerqs.tower.service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.boco.dss.common.data.SortType;
import cn.com.boco.dss.subject.common.helper.FilePath;
import cn.com.boco.dss.subject.common.helper.FilePathUtil;
import cn.com.boco.dss.subject.towerqs.check.domain.TowerCheck;
import cn.com.boco.dss.subject.towerqs.common.constant.TowerRiskStatusConstant;
import cn.com.boco.dss.subject.towerqs.common.geo.area.domain.Area;
import cn.com.boco.dss.subject.towerqs.common.geo.area.service.AreaService;
import cn.com.boco.dss.subject.towerqs.detection.domain.TowerDetection;
import cn.com.boco.dss.subject.towerqs.repair.domain.TowerRepair;
import cn.com.boco.dss.subject.towerqs.risk.domain.TowerRisk;
import cn.com.boco.dss.subject.towerqs.risk.service.TowerRiskService;
import cn.com.boco.dss.subject.towerqs.tower.domain.Tower;
import cn.com.boco.dss.subject.towerqs.tower.domain.TowerDetails;
import cn.com.boco.dss.subject.towerqs.tower.repository.TowerDetailsForMysql;
import cn.com.boco.dss.webcore.data.commondata.CommonData;

/**
 * @program: TowerService
 * @description:
 * @author: lyj
 * @create: 2019-08-21 11:20
 **/
@Service
public class TowerDetailsService {

	@Autowired
	private TowerRiskService towerRiskService;

	@Autowired
	private TowerService towerService;

	@Autowired
	private TowerDetailsForMysql towerDetailsForMysql;

	@Autowired
	private AreaService areaService;

	public Object getPageHead(String towerID) throws IllegalAccessException {
		PageHead pageHead = new PageHead();
		Tower tower = towerService.findById(towerID);
		Area area = areaService.findById(tower.getAreaID());
		HashMap<String, Object> map = new HashMap<>();
		FilePath filePath = new FilePath();
		filePath.setAddRessCode(tower.getAddressCode());
		filePath.setAreaName(area.getAreaName());
		filePath.setLocation(tower.getLocation());
		filePath.setProvinceName(area.getProvinceName());
		filePath.setTowerName(tower.getTowerName());
		String imgPath = FilePathUtil.getTowerImgPath(filePath);
		pageHead.setImg(imgPath);
		TowerDetails towerDetails = new TowerDetails(tower);
		map = objectToMap(towerDetails);
		pageHead.setMap(map);
		return pageHead;
	}

	private HashMap<String, Object> objectToMap(Object obj) throws IllegalAccessException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Class<?> clazz = obj.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			String fieldName = field.getName();
			Object value = field.get(obj);
			map.put(fieldName, value);
		}
		return map;
	}

	public Object getPageBody(String towerID) {
		List<TowerRisk> riskList = towerRiskService.findByTowerID(towerID);
		List<TowerRisk> list = riskList.stream()
				.filter(item -> !TowerRiskStatusConstant.RISK_REPAIRED.equals(item.getRiskStatus()))
				.collect(Collectors.toList());
		return list;
	}

	public long queryCheckCount(String param1) throws Exception {
		return towerDetailsForMysql.queryCheckCount(param1);
	}

	public long queryDetectionCount(String param1) throws Exception {
		return towerDetailsForMysql.queryDetectionCount(param1);
	}

	public long queryRepairCount(String param1) throws Exception {
		return towerDetailsForMysql.queryRepairCount(param1);
	}

	public CommonData queryCheckData(String param1, int pageIndex, int pageSize, int sortColIndex, SortType sortType)
			throws Exception {
		return towerDetailsForMysql.queryCheckData(param1, pageIndex, pageSize, sortColIndex, sortType);
	}

	public CommonData queryDetectionData(String param1, int pageIndex, int pageSize, int sortColIndex,
			SortType sortType) throws Exception {
		return towerDetailsForMysql.queryDetectionData(param1, pageIndex, pageSize, sortColIndex, sortType);
	}

	public CommonData queryRepairData(String param1, int pageIndex, int pageSize, int sortColIndex, SortType sortType)
			throws Exception {
		return towerDetailsForMysql.queryRepairData(param1, pageIndex, pageSize, sortColIndex, sortType);
	}

	public class PageFoot {
		private List<TowerCheck> checkList;
		private List<TowerDetection> detectionList;
		private List<TowerRepair> repairList;

		public PageFoot() {
		}

		public PageFoot(List<TowerCheck> checkList, List<TowerDetection> detectionList, List<TowerRepair> repairList) {
			this.checkList = checkList;
			this.detectionList = detectionList;
			this.repairList = repairList;
		}

		public List<TowerCheck> getCheckList() {
			return checkList;
		}

		public void setCheckList(List<TowerCheck> checkList) {
			this.checkList = checkList;
		}

		public List<TowerDetection> getDetectionList() {
			return detectionList;
		}

		public void setDetectionList(List<TowerDetection> detectionList) {
			this.detectionList = detectionList;
		}

		public List<TowerRepair> getRepairList() {
			return repairList;
		}

		public void setRepairList(List<TowerRepair> repairList) {
			this.repairList = repairList;
		}
	}

	/**
	 * 每个隐患列表的字段
	 */
	public class DisplayItem {
		private String img;
		/**
		 * 与巡检表关联
		 */
		private String checkDataIndex;
		/**
		 * 与检查表关联
		 */
		private String detectionDataIndex;

		private String riskName;
		private String recordDate;
		private String riskStatus;
		private String dataIndex;
		/**
		 * 关联整治表
		 */
		private String repaireDateIndex;

		public DisplayItem() {
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

		public String getRiskName() {
			return riskName;
		}

		public void setRiskName(String riskName) {
			this.riskName = riskName;
		}

		public String getRecordDate() {
			return recordDate;
		}

		public void setRecordDate(String recordDate) {
			this.recordDate = recordDate;
		}

		public String getRiskStatus() {
			return riskStatus;
		}

		public void setRiskStatus(String riskStatus) {
			this.riskStatus = riskStatus;
		}

		public String getDataIndex() {
			return dataIndex;
		}

		public void setDataIndex(String dataIndex) {
			this.dataIndex = dataIndex;
		}

		public String getCheckDataIndex() {
			return checkDataIndex;
		}

		public void setCheckDataIndex(String checkDataIndex) {
			this.checkDataIndex = checkDataIndex;
		}

		public String getDetectionDataIndex() {
			return detectionDataIndex;
		}

		public void setDetectionDataIndex(String detectionDataIndex) {
			this.detectionDataIndex = detectionDataIndex;
		}

		public String getRepaireDateIndex() {
			return repaireDateIndex;
		}

		public void setRepaireDateIndex(String repaireDateIndex) {
			this.repaireDateIndex = repaireDateIndex;
		}
	}

	/**
	 * 页面头部的数据结构
	 */
	public class PageHead {
		private String img;
		private HashMap<String, Object> map;

		public PageHead() {
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

		public HashMap<String, Object> getMap() {
			return map;
		}

		public void setMap(HashMap<String, Object> map) {
			this.map = map;
		}
	}
}
