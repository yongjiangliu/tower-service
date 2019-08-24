package cn.com.boco.dss.subject.docmanage.pojo;

/**
 * Created by yxy on 2019/08/13 15:23
 * Version 1.0.0
 * Description 文档有关的属性
 */
public class DocProperty {
    /**
     * 文件或文件夹名称
     */
    private String fileName = "";
    /**
     * 文件类型，eg:txt/rar/zip/doc/docx/ppt/pptx/xls/xlsx/jpg/png
     */
    private String fileType = "";
    /**
     * 文件或文件夹的路径
     */
    private String path = "";
    /**
     * 是否是文件夹
     */
    private Integer isDir = 1;
    /**
     * 文件或文件夹最后编辑时间
     */
    private String editTime;
    /**
     * 文件或文件夹大小
     */
    private String size = "--";

    /**
     * 当前文件夹是否可删除
     */
    private Integer isDelete=0;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getIsDir() {
        return isDir;
    }

    public void setIsDir(Integer isDir) {
        this.isDir = isDir;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
