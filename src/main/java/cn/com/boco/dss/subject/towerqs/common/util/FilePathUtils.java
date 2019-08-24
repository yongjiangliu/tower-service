package cn.com.boco.dss.subject.towerqs.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.com.boco.dss.subject.towerqs.detection.domain.TowerDetection;
import cn.com.boco.dss.subject.towerqs.risk.domain.TowerRisk;
import cn.com.boco.dss.subject.towerqs.tower.domain.Tower;

@Component
public class FilePathUtils {

	@Value("${dss.tower.img.save-path:/home/tower/tomcat_8681_tower/webapps/towerfile/}")
	private String fileSavePath;

	@Value("${dss.tower.img.read-path:http://10.12.1.216:8681/towerfile}")
	private String fileUrlPath;

	public String getTowerPhotoUrl(Tower tower) {
		return "";
	}

	public String getDetectionTowerPhotoFileNameToSave(TowerDetection detection, String photoFileName) {
		return "";
	}

	public String getDetectionRiskPhotoFileNameToSave(TowerDetection detection, TowerRisk risk) {
		return "";
	}
}
