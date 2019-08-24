package cn.com.boco.dss.subject.docmanage.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by yxy on 2019/08/13 15:01 Version 1.0.0 Description 保存/读取文件的路径信息
 */
@Component
public class DocUrlSettings {
	/**
	 * 四川铁塔质量管理平台 存储文档的路径
	 */
	@Value("${dss.tower.img.save-path:null}")
	private String docSavePath;
	/**
	 * 四川铁塔质量管理平台 当批量下载多个文档时，统一打包成一个文件名的路径，如：D:/sichuan/doc/档案中心.zip
	 */
	@Value("${dss.tower.docmanage.doc-download-packagename-path:null}")
	private String docDownloadPackageNamePath;

	public String getDocSavePath() {
		if (!docSavePath.endsWith("/")) {
			docSavePath = docSavePath + "/";
		}
		return docSavePath;
	}

	public void setDocSavePath(String docSavePath) {
		this.docSavePath = docSavePath;
	}

	public String getDocDownloadPackageNamePath() {
		return docDownloadPackageNamePath;
	}

	public void setDocDownloadPackageNamePath(String docDownloadPackageNamePath) {
		this.docDownloadPackageNamePath = docDownloadPackageNamePath;
	}
}
