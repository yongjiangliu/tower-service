package cn.com.boco.dss.subject.towerqs.risk.web;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.subject.towerqs.risk.domain.RiskLib;
import cn.com.boco.dss.subject.towerqs.risk.service.RiskLibService;
import cn.com.boco.dss.webcore.tree.TreeNode;

@Controller
public class RiskLibController {
	@Autowired
	private RiskLibService riskLibService;

	@GetMapping(value = "dss/TowerService/risk/risklib/list")
	@ResponseBody
	public JsonData findAll() {
		List<RiskLib> riskLibList = riskLibService.findAll();
		List<TreeNode> nodeList = new LinkedList<TreeNode>();
		for(RiskLib lib:riskLibList) {
			if(lib.getParentID()==null||"NULL".contains(lib.getParentID())) {
				TreeNode node = new TreeNode();
				node.setId(lib.getRiskID());
				node.setName(lib.getRiskName());
				node.setpId(null);
				node.setOther1(String.valueOf(lib.getRiskLevel()));
				node.setOther2(lib.getRiskDescription());
				node.setChildren(new LinkedList<TreeNode>());
				addLibChildList(riskLibList,node);
				nodeList.add(node);
			}
		}
		JsonData jd = new JsonData();
		jd.setData(nodeList);
		return jd;
	}
	
	private void addLibChildList(List<RiskLib> riskLibList,TreeNode pNode){
		for(RiskLib lib:riskLibList) {
			if(pNode.getId().equals(lib.getParentID())) {
				TreeNode node = new TreeNode();
				node.setId(lib.getRiskID());
				node.setName(lib.getRiskName());
				node.setpId(lib.getParentID());
				node.setOther1(String.valueOf(lib.getRiskLevel()));
				node.setOther2(lib.getRiskDescription());
				pNode.getChildren().add(node);
				addLibChildList(riskLibList,node);
			}
		}
	}
}
