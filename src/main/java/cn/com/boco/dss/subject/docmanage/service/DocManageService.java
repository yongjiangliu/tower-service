package cn.com.boco.dss.subject.docmanage.service;

import cn.com.boco.dss.common.DateHelper;
import cn.com.boco.dss.common.caching.CacheManager;
import cn.com.boco.dss.common.crc.CRC64;
import cn.com.boco.dss.common.data.FlowUnit;
import cn.com.boco.dss.common.data.FluxFormatter;
import cn.com.boco.dss.common.data.JsonData;
import cn.com.boco.dss.common.util.StringUtil;
import cn.com.boco.dss.subject.common.helper.DssHelper;
import cn.com.boco.dss.subject.common.helper.ZipUtil;
import cn.com.boco.dss.subject.docmanage.pojo.DocListWithUserZoneCacheId;
import cn.com.boco.dss.subject.docmanage.pojo.DocProperty;
import cn.com.boco.dss.subject.docmanage.pojo.DocUrlSettings;
import cn.com.boco.dss.subject.towerqs.index.pojo.CheckUserZoneUtil;
import cn.com.boco.dss.subject.towerqs.index.pojo.UserZone;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by yxy on 2019/08/13 15:00
 * Version 1.0.0
 * Description 铁塔质量管理平台-档案中心
 */
@Service
public class DocManageService {

    @Autowired
    private DocUrlSettings docUrlSettings;
    @Autowired
    private CheckUserZoneUtil checkUserZoneUtil;


    /**
     * 获取文件列表
     *
     * @param jsonObject
     * @return
     */
    public JsonData getDocList(JSONObject jsonObject, HttpServletRequest request) {
        JsonData jd = new JsonData();
        try {
            String filePath = jsonObject.getString("path");
            UserZone userZone = checkUserZoneUtil.checkUserZone(request);
            if (StringUtil.isNullOrEmpty(filePath)) {
                filePath = docUrlSettings.getDocSavePath() + userZone.getUserDocUrl();//用户对应的默认扫描路径
            }
            //检查登陆用户归属的区域 -1：全部数据；1：省端用户；:2：地市端用户；3：区县；999：是其他用户组；
            int userStatus = userZone.getUserStatus();
            if (userStatus == -1 || userStatus == 1 || userStatus == 2) {
                File file = new File(filePath);
                if (file.exists()) {
                    List<DocProperty> docList = scanFiles(filePath, false, "");
                    jd.setData(docList);
                }
                else {
                    String zoneName = userZone.getZone().getName();//当前用户归属的地市 比如 成都
                    jd.setStatus("101");
                    jd.setData("您当前归属的区域为【" + zoneName + "】，档案资料路径不存在，路径：" + filePath);
                }
            }
            else {//3：区县；999：是其他用户组 没有权限查看档案资料
                jd.setStatus("101");
                jd.setData("您没有权限查看当前目录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            DssHelper.log("档案中心获取文件列表异常，异常信息：" + e.getMessage(), this.getClass());
            jd.setStatus("-1");
            jd.setData("获取文件列表失败，" + e.getMessage());
        }
        return jd;
    }


    /**
     * 获取文件列表(优化版本)
     *
     * @param jsonObject
     * @return
     */
    public JsonData getDocListWithUserZoneCache(JSONObject jsonObject, HttpServletRequest request) {
        JsonData jd = new JsonData();
        try {
            String filePath = jsonObject.getString("path");
            String userZoneCacheId = jsonObject.getString("userZoneCacheId");
            UserZone userZone;
            //缓存key值为空
            if (StringUtil.isNullOrEmpty(userZoneCacheId)) {
                userZone = setUserZoneCache(request);
            }
            else {//缓存key值不为空
                Object obj = CacheManager.get(userZoneCacheId);
                //缓存存在
                if (obj != null) {
                    userZone = (UserZone) obj;
                }
                else {//缓存不存在，重新设置
                    userZone = setUserZoneCache(request);
                }
            }
            if (StringUtil.isNullOrEmpty(filePath)) {
                filePath = docUrlSettings.getDocSavePath() + userZone.getUserDocUrl();//用户对应的默认扫描路径
            }
            //检查登陆用户归属的区域 -1：全部数据；1：省端用户；:2：地市端用户；3：区县；999：是其他用户组；
            int userStatus = userZone.getUserStatus();
            if (userStatus == -1 || userStatus == 1 || userStatus == 2) {
                File file = new File(filePath);
                if (file.exists()) {
                    DocListWithUserZoneCacheId docListWithUserZoneCacheId = new DocListWithUserZoneCacheId();
                    List<DocProperty> docList = scanFiles(filePath, false, "");
                    docListWithUserZoneCacheId.setUserZoneCacheId(userZone.getUserZoneCacheId());
                    docListWithUserZoneCacheId.setDocList(docList);
                    jd.setData(docListWithUserZoneCacheId);
                }
                else {
                    String zoneName = userZone.getZone().getName();//当前用户归属的地市 比如 成都
                    jd.setStatus("101");
                    jd.setData("您当前归属的区域为【" + zoneName + "】，档案资料路径不存在，路径：" + filePath);
                }
            }
            else {//3：区县；999：是其他用户组 没有权限查看档案资料
                jd.setStatus("102");
                jd.setData("您没有权限查看当前目录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            DssHelper.log("档案中心获取文件列表异常，异常信息：" + e.getMessage(), this.getClass());
            jd.setStatus("-1");
            jd.setData("获取文件列表失败，" + e.getMessage());
        }
        return jd;
    }


    /**
     * 根据指定的文件名称搜索文件
     *
     * @param jsonObject
     * @return
     */
    public JsonData search(JSONObject jsonObject, HttpServletRequest request) {
        JsonData jd = new JsonData();
        try {
            String kw = jsonObject.getString("fileName");
            List<DocProperty> list = scanFiles(null, true, kw);
            jd.setStatus("0");
            jd.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            String result = StringUtil.isNullOrEmpty(e.getMessage()) ? "查询失败" : "查询失败，" + e.getMessage();
            jd.setData(result);
            jd.setStatus("-1");
            DssHelper.log("档案中心获取文件列表异常，异常信息：" + e.getMessage(), this.getClass());
        }
        return jd;
    }

    /**
     * 非递归实现获取指定【目录】下的【文件或文件夹】列表
     *
     * @param dirPath            指定的目录路径
     * @param isScanSubDirectory 是否扫描子文件夹及其子子文件夹
     * @param kw                 搜索关键字
     * @return
     * @throws Exception
     */
    public List<DocProperty> scanFiles(String dirPath, boolean isScanSubDirectory, String kw) throws Exception {
        List<DocProperty> fileList = new ArrayList<DocProperty>();
        LinkedList<File> list = new LinkedList<File>();
        DssHelper.log("档案管理--扫描查找的路径：" + dirPath, this.getClass());
        boolean isEnabledSearch = !StringUtil.isNullOrEmpty(kw) ? true : false;
        if (!StringUtil.isNullOrEmpty(dirPath)) {
            File dir = new File(dirPath);
            if (null != dir) {
                if (dir.exists()) {
                    File[] files = dir.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isDirectory()) {
                            list.add(files[i]);// 把第一层的目录，全部放入链表
                        }
                        //开启了关键字查询
                        if (isEnabledSearch) {
                            if (files[i].getName().indexOf(kw) > -1) {
                                fileList.add(setDocProperty(files[i]));
                            }
                        }
                        else {
                            fileList.add(setDocProperty(files[i]));
                        }
                    }
                    // 循环遍历链表
                    while (!list.isEmpty() && isScanSubDirectory == true) {
                        // 把链表的第一个记录删除且返回删除的对象
                        File tmp = list.removeFirst();
                        // 如果删除的目录是一个路径的话 (其实list中存放的都是目录路径)
                        if (tmp.isDirectory()) {

                            // 列出这个目录下的文件到数组中
                            files = tmp.listFiles();
                            if (null == files || files.length == 0) {// 空目录
                                continue;
                            }
                            // 遍历文件数组
                            for (int i = 0; i < files.length; ++i) {
                                // 如果遍历到的是目录，则将继续被加入链表
                                if (files[i].isDirectory()) {
                                    list.add(files[i]);
                                }
                                //开启了关键字查询
                                if (isEnabledSearch) {
                                    if (files[i].getName().indexOf(kw) > -1) {
                                        fileList.add(setDocProperty(files[i]));
                                    }
                                }
                                else {
                                    fileList.add(setDocProperty(files[i]));
                                }
                            }
                        }
                    }
                }
                else {
                    throw new Exception("查找的目录路径不存在，当前路径：" + dirPath);
                }
            }
            else {
                throw new Exception("查找的目录路径不能为空");
            }
        }
        else {
            throw new Exception("查找的目录路径不能为空");
        }
        return fileList;
    }

    /**
     * 设置文件属性
     *
     * @param file 文件对象
     */
    private DocProperty setDocProperty(File file) throws Exception {
        DocProperty doc = new DocProperty();
        String fName = "--";//不含后缀名的文件名
        String suffix = "--";
        String originalFileName = file.getName();
        String path = file.getCanonicalPath();//真正的路径

        //最后修改时间
        String editTime = DateHelper.dateToLongString(new Date(file.lastModified()));
        if (!StringUtil.isNullOrEmpty(originalFileName)) {
            int lastDot = originalFileName.lastIndexOf(".");
            if (lastDot > -1) {
                suffix = originalFileName.substring(lastDot + 1).toLowerCase();
                fName = originalFileName.substring(0, lastDot);
            }
            else {
                fName = originalFileName;
            }
        }
        doc.setFileName(fName);
        doc.setFileType(suffix);
        doc.setPath(path);
        doc.setEditTime(editTime);
        if (file.isDirectory()) {//文件夹
            doc.setIsDir(1);
            doc.setSize("--");
        }
        else {//文件
            doc.setIsDir(0);
            double d = (double) file.length();
            FlowUnit bestUnit = FluxFormatter.findBestUnit(d, FlowUnit.Byte);
            double b = DssHelper.parseDouble(FluxFormatter.format(d, FlowUnit.Byte, bestUnit), 2);
            doc.setSize(b + bestUnit.toString());
        }
        return doc;
    }


    /**
     * 下载文件 同时支持单个或多个文件下载，如果是多个文件则进行打包下载zip格式；
     *
     * @param response response对象
     * @throws Exception
     */
    public String downloadFiles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String downloadFiles = request.getParameter("sign");//下载文件信息
        if (StringUtil.isNullOrEmpty(downloadFiles)) {
            throw new Exception("没有要下载的文件。");
        }

        //打包成功后，返回打包后文件路径
        String docDownloadPackageNamePath = "";
        JSONArray arrFiles = JSON.parseArray(downloadFiles);
        List<File> files = new ArrayList<>();
        for (int i = 0; i < arrFiles.size(); i++) {
            JSONObject jsonObject = arrFiles.getJSONObject(i);
            String filePath = jsonObject.getString("path");//eg:D:\test\b1.txt
            //String originFileName = jsonObject.getString("fileName");
            //String fileType = jsonObject.getString("fileType");
            File file = new File(filePath);
            if (null != file) {
                files.add(file);
            }
        }
        if (files.size() > 0) {
            if (files.size() == 1) {
                ZipUtil.downloadSingleFile(files.get(0), request, response);
            }
            else {
                docDownloadPackageNamePath = docUrlSettings.getDocDownloadPackageNamePath();
                DssHelper.log("档案管理--打包下载时生成的文件路径：" + docDownloadPackageNamePath, this.getClass());
                if (StringUtil.isNullOrEmpty(docDownloadPackageNamePath)) {
                    throw new Exception("铁塔质量管理平台系统之档案资料批量下载文件时，打包文件名的绝对路径不能为空，请核实。");
                }
                ZipUtil.createZipFile(files, docDownloadPackageNamePath, response);
            }
        }
        return docDownloadPackageNamePath;
    }

    /**
     * 下载zip文件
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void downloadZip(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String downloadFiles = request.getParameter("sign");//下载文件信息
            if (StringUtil.isNullOrEmpty(downloadFiles)) {
                throw new Exception("没有要下载的文件。");
            }
            JSONArray arrFiles = JSON.parseArray(downloadFiles);
            if (arrFiles.size() > 0) {
                File file = new File(arrFiles.getJSONObject(0).getString("path"));
                ZipUtil.downloadSingleFile(file, request, response);
            }
            else {
                throw new Exception("没有要下载的文件。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 删除目录及其目录下的所有文件
     *
     * @param fileList 文件路径或文件夹路径 eg:[“D:\test\a1\a11\b12.xlsx”,” D:\test\b1”]
     * @throws IOException
     */
    public JsonData delFileOrDirectory(JSONArray fileList) {
        JsonData jd = new JsonData();
        if (fileList.size() > 0) {
            try {
                for (int i = 0; i < fileList.size(); i++) {
                    String fileOrDirectoryPath = fileList.getString(i);
                    ZipUtil.delFileOrDirectory(fileOrDirectoryPath);
                    ZipUtil.delFileOrDirectory(fileOrDirectoryPath);
                }
                jd.setData("删除成功");
            } catch (IOException e) {
                e.printStackTrace();
                jd.setStatus("-1");
                jd.setData("删除失败");
            }
        }
        else {
            jd.setStatus("-1");
            jd.setData("没有需要删除的文件");
        }
        return jd;
    }


    /**
     * 创建文件夹
     *
     * @param jsonObject eg:{path: “D:\test\文件夹1”,isRoot:0/1 }
     * @return
     */
    public JsonData makeDir(JSONObject jsonObject) {
        JsonData jd = new JsonData();
        try {
            String dirPath = jsonObject.getString("path");
            //            isRoot：当前操作是否在根目录下创建文件夹；
            //            1：在根目录下创建文件夹；0：不在根目录下创建文件夹；
            String isRoot = jsonObject.getString("isRoot");
            DssHelper.log("方法makeDir--创建文件夹---isRoot：" + isRoot + "；path=" + dirPath, this.getClass());
            if (!StringUtil.isNullOrEmpty(isRoot)) {
                File file;
                if (isRoot.equals("1")) {//在根目录下创建文件夹
                    file = new File(docUrlSettings.getDocSavePath(), dirPath);
                }
                else {
                    file = new File(dirPath);
                }
                if (!file.exists()) {
                    file.mkdir();
                    jd.setData("创建成功");
                }
                else {
                    jd.setStatus("101");
                    jd.setData("已存在同名文件夹，请修改文件夹名称");
                }
            }
            else {
                jd.setStatus("102");
                jd.setData("请求参数中isRoot属性不能为空");
            }

        } catch (Exception e) {
            jd.setStatus("-1");
            jd.setData("创建失败，" + e.getMessage());
        }
        return jd;
    }


    /**
     * 修改文件夹和文件名
     *
     * @param jsonObject eg: {"path": "/bbb/01_20171227210235.JPG",	"newname": "2.JPG"}
     * @return
     */
    public JsonData rename(JSONObject jsonObject) {
        JsonData jd = new JsonData();
        try {
            String filePath = jsonObject.getString("path");
            String newName = jsonObject.getString("newname");
            if (!StringUtil.isNullOrEmpty(filePath) && !StringUtil.isNullOrEmpty(newName)) {
                File oldFile = new File(filePath);
                //如果修改文件，输入的文件名不含后缀名，则自动获取文件后缀名
                if (oldFile.isFile()) {
                    String fileName = oldFile.getName();
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    //如果修改文件，输入的文件名不含后缀名，则自动获取文件后缀名
                    if (newName.indexOf(".") < 0) {
                        newName = newName + "." + suffix;
                    }
                    else {//如果含有后缀名，则判断是否和之前的后缀名相同；即不能出现a.txt修改为b.xlsx的情况
                        String suffixNew = newName.substring(newName.lastIndexOf(".") + 1);
                        if (!suffix.toLowerCase().equals(suffixNew.toLowerCase())) {
                            newName = newName + "." + suffix;
                        }
                    }
                }
                String fileType = oldFile.isFile() ? "文件" : "文件夹";
                if (oldFile.exists()) {
                    File parentFile = oldFile.getParentFile();
                    if (parentFile != null) {
                        File newFile = new File(parentFile, newName);
                        if (!newFile.exists()) {
                            if (oldFile.renameTo(newFile)) {
                                jd.setData("修改成功");
                            }
                            else {
                                jd.setStatus("-1");
                                jd.setData("修改失败");
                            }
                        }
                        else {
                            jd.setStatus("-1");
                            jd.setData("修改失败，已存在同名" + fileType + "名称，请修改名称");
                        }
                    }
                }
                else {
                    jd.setStatus("-1");
                    jd.setData("修改失败，修改的" + fileType + "路径不存在");
                }

            }
            else {
                jd.setStatus("-1");
                jd.setData("请求参数中path和newname属性均不能为空");
            }

        } catch (Exception e) {
            e.printStackTrace();
            jd.setStatus("-1");
            jd.setData("修改失败，" + e.getMessage());
        }
        return jd;
    }

    /**
     * 文件上传（支持批量上传）
     *
     * @param request request对象
     * @return
     */
    public JsonData upload(HttpServletRequest request) {
        JsonData jd = new JsonData();
        MultipartHttpServletRequest multipartRequest = ((MultipartHttpServletRequest) request);
        List<MultipartFile> files = multipartRequest.getFiles("file");//文件对象
        String isRoot = multipartRequest.getParameter("isRoot");//当前操作是否在根目录下上传文件； 1：在根目录下上传文件；0：不在根目录下上传文件
        String uploadPath = multipartRequest.getParameter("path");//要上传到目标文件夹下
        //记录重复的文件名；--- 做批量上传文件 如果同级目录有重复文件名 提示哪些文件重复  不重复的创建文件
        List<String> existedFileNames = new ArrayList<String>();
        try {
            if (files.size() > 0) {
                if (!StringUtil.isNullOrEmpty(isRoot)) {
                    if (isRoot.equals("1")) {//在根目录下上传
                        if (!uploadPath.contains(docUrlSettings.getDocSavePath())) {
                            uploadPath = docUrlSettings.getDocSavePath();
                        }
                    }
                    else {//在非根目录下上传文件
                        if (StringUtil.isNullOrEmpty(uploadPath)) {
                            jd.setStatus("-1");
                            jd.setData("请求参数isRoot=0时path属性不能为空");
                            return jd;
                        }
                    }
                    DssHelper.log("上传信息：文件个数：" + files.size() + ";是否在根目录下上传isRoot=" + isRoot + ";上传路径：uploadPath=" + uploadPath, this.getClass());
                    for (MultipartFile file : files) {
                        String fileName = file.getOriginalFilename();
                        File newFile = new File(uploadPath, fileName);
                        //判断目标文件夹是否存在
                        if (newFile.getParentFile().exists()) {
                            if (newFile.exists()) {
                                //记录重复文件名
                                String shortFileName = fileName.substring(0, fileName.lastIndexOf("."));
                                existedFileNames.add(shortFileName);
                            }
                            else {
                                file.transferTo(newFile);
                            }
                            //上传成功
                            if (existedFileNames.size() == 0) {
                                jd.setStatus("0");
                                jd.setData("上传成功");
                            }
                            else {//上传成功，但是有同名文件已忽略
                                jd.setStatus("101");
                                jd.setData(existedFileNames);
                            }
                        }
                        else {
                            jd.setStatus("-1");
                            jd.setData("要上传的目标文件夹路径[" + uploadPath + "]不存在");
                            break;
                        }

                    }

                }
                else {
                    jd.setStatus("-1");
                    jd.setData("请求参数中isRoot属性不能为空");
                }
            }
            else {
                jd.setStatus("-1");
                jd.setData("没有需要上传的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jd.setStatus("-1");
            jd.setData("上传失败，" + e.getMessage());
        }
        return jd;

    }


    /**
     * 设置userZone缓存并返回UserZone对象
     *
     * @param request
     * @return
     * @throws Exception
     */
    private UserZone setUserZoneCache(HttpServletRequest request) throws Exception {
        UserZone userZone = checkUserZoneUtil.checkUserZone(request);
        String userZoneCacheId = String.valueOf(CRC64.GetCRC64(UUID.randomUUID().toString()));
        CacheManager.put(userZoneCacheId, userZone);
        userZone.setUserZoneCacheId(userZoneCacheId);
        return userZone;
    }
}
