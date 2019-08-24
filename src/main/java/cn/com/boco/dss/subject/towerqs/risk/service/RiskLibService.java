package cn.com.boco.dss.subject.towerqs.risk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.boco.dss.subject.towerqs.risk.domain.RiskLib;
import cn.com.boco.dss.subject.towerqs.risk.repository.RiskLibRepository;

@Service
public class RiskLibService {
	@Autowired
	private RiskLibRepository riskLibRepository;

	public List<RiskLib> findAll(){
		return riskLibRepository.findAll();
	}
}
