package cn.com.boco.dss.subject.docmanage.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2019/08/24 19:29
 * Version 1.0.0
 * Description 返回带缓存cache的key值（缓存对象是UserZone）
 */
public class DocListWithUserZoneCacheId {
    /**
     * 当前用户的UserZone缓存key
     */
    private String userZoneCacheId;
    private List<DocProperty> docList;

    public DocListWithUserZoneCacheId() {
        this.docList = new ArrayList<>();
    }

    public String getUserZoneCacheId() {
        return userZoneCacheId;
    }

    public void setUserZoneCacheId(String userZoneCacheId) {
        this.userZoneCacheId = userZoneCacheId;
    }

    public List<DocProperty> getDocList() {
        return docList;
    }

    public void setDocList(List<DocProperty> docList) {
        this.docList = docList;
    }
}
