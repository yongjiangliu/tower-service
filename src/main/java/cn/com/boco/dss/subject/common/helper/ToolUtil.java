package cn.com.boco.dss.subject.common.helper;

import cn.com.boco.dss.common.data.DataRow;
import cn.com.boco.dss.common.data.DataTable;
import cn.com.boco.dss.common.util.StringUtil;
import cn.com.boco.dss.config.xml.ConfigList;
import cn.com.boco.dss.config.xml.ItemInfo;
import org.springframework.util.ClassUtils;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class ToolUtil {


    public static ItemInfo findItemInfoByXml(String name, String xmlPath) {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String fileName = path + xmlPath;
        ItemInfo itemInfo = new ItemInfo();
        ConfigList cl = null;
        try {
            cl = ConfigList.fromFile(fileName);
        } catch (Exception e) {
            e.getMessage();
        }
        List<ItemInfo> items = cl.getLists().get(0).getItems();
        for (ItemInfo item : items) {
            if (StringUtil.isEqual(item.getName(), name)) {
                itemInfo = item;
                break;
            }
        }
        return itemInfo;

    }

//    public static DataTable setImgPath(DataTable dt, String savePath, String readPath, int colIndex) {
//        for (DataRow dr : dt.getRows()) {
//            StringBuilder sb = new StringBuilder();
//            //sb.append(readPath).append("/");
//            sb.append("通信塔资料").append("/").append("中华人民共和国").append("/");
//            sb.append(dr.getValue("ProvinceName")).append("/");
//            sb.append(dr.getValue("areaName")).append("/");
//            sb.append(dr.getValue("location")).append("/");
//            sb.append(dr.getValue("towerName")).append("+");
//            sb.append(dr.getValue("addressCode")).append("/");
//            sb.append(dr.getValue("towerName")).append("+");
//            sb.append(dr.getValue("addressCode")).append("+");
//            sb.append("运营维护").append("/");
//            String workOrderType = String.valueOf(dr.getValue("workOrderType"));
//            if (StringUtil.isEqual(workOrderType, "check")) {
//                sb.append("代维检修").append("/").append("代维检修+");
//                sb.append(String.valueOf(dr.getValue("recordDate")).replace("-", ""));
//                sb.append("/");
//                sb.append(upLoadFileType.check_riskImg_before);
//                String name = String.valueOf(dr.getValue("riskName"));
//                String path = savePath + sb.toString();
//                List<File> files = ToolUtil.getFileSort(path, name);
//                if (files.size() > 0) {
//                    sb.append(files.get(0).getName());
//                }
//            } else if (StringUtil.isEqual(workOrderType, "detection")) {
//
//                sb.append("安全评估").append("/").append("安全评估+");
//                sb.append(String.valueOf(dr.getValue("constractCode")));
//                sb.append("/");
//                sb.append(upLoadFileType.detection_riskImg);
//                String name = String.valueOf(dr.getValue("riskName"));
//                String path = savePath + sb.toString();
//                List<File> files = ToolUtil.getFileSort(path, name);
//                if (files.size() > 0) {
//                    sb.append(files.get(0).getName());
//                }
//            }
//
//
//            dr.setValue(colIndex, readPath + sb.toString());
//
//        }
//        return dt;
//    }



    /**
     * 获取目录下所有文件(按时间排序)
     *
     * @param path
     * @return
     */
    public static List<File> getFileSort(String path, String name) {

        List<File> list = getFiles(path, name, new ArrayList<File>());

        if (list != null && list.size() > 0) {

            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return 1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return -1;
                    }

                }
            });

        }

        return list;
    }

    /**
     * 获取目录下所有文件
     *
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getFiles(String realpath, String name, List<File> files) {

        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), name, files);
                } else {
                    if (file.getName().contains(name)) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }


}
