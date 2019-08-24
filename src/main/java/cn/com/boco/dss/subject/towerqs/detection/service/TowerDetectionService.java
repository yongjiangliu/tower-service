package cn.com.boco.dss.subject.towerqs.detection.service;

import cn.com.boco.dss.subject.towerqs.detection.domain.TowerDetection;
import cn.com.boco.dss.subject.towerqs.detection.repository.TowerDetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TowerDetectionService {

	@Autowired
	private TowerDetectionRepository towerDetectionRepository;

	public TowerDetection findById(Integer id) {
		return towerDetectionRepository.findById(id).orElse(null);
	}
	
	public TowerDetection save(TowerDetection detection) {
		return towerDetectionRepository.save(detection);
	}
	
	public List<TowerDetection> saveAll(List<TowerDetection> detectionList){
		return towerDetectionRepository.saveAll(detectionList);
	}


	public void updateStatusById(Integer status, Integer id) {
		towerDetectionRepository.updateStatusById(status, id);
	}

	public List<TowerDetection> findAll() {
		return towerDetectionRepository.findAll();
	}
}
