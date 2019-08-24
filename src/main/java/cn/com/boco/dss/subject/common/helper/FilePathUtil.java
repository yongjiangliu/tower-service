package cn.com.boco.dss.subject.common.helper;

import org.springframework.beans.factory.annotation.Value;

import cn.com.boco.dss.common.util.StringUtil;


public class FilePathUtil {


    @Value("${dss.tower.img.save-path}")
    private static String savePath;
    @Value("${dss.tower.img.read-path}")
    private static String readPath;


    private static String repair_path = path("repair");

    private static String check_path = path("check");

    private static String detection_path = path("detection");

    private static String towerImg = path("tower");


//整治图片文件路径
    /**
     * 整治塔体照片
     */
    private final static String repair_towerImg_path = repair_path + "加固施工/照片/塔体照片/";

    /**
     * 整治隐患加固前照片
     */
    private final static String repair_riskImg_before_path = repair_path + "加固施工/照片/记录照片/加固前/";

    /**
     * 整治隐患加固中照片
     */
    private final static String repair_riskImg_ing_path = repair_path + "加固施工/照片/记录照片/加固中/";

    /**
     * 整治隐患加固后照片
     */
    private final static String repair_riskImg_after_path = repair_path + "加固施工/照片/记录照片/加固后/";

    /**
     * 整治文件
     */
    private final static String repair_report_path = repair_path + "加固施工/文件/";


    //检测图片文件路径
    /**
     * 检测塔体照片
     */
    private final static String detection_towerImg_path = detection_path + "照片/塔体照片/";

    /**
     * 检测隐患照片
     */
    private final static String detection_riskImg_path = detection_path + "照片/问题照片/";

    /**
     * 检测天线照片
     */
    private final static String detection_antennaImg_path = detection_path + "照片/天线照片/";

    /**
     * 检测报告
     */
    private final static String detection_report_path = detection_path + "报告/";

    /**
     * 检测图纸
     */
    private final static String detection_drawingImg_path = detection_path + "复原图纸/";


    //巡检图片文件路径
    /**
     * 巡检塔体照片检修前
     */
    private final static String check_towerImg_before_path = check_path + "照片/塔体照片/检修前/";

    /**
     * 巡检塔体照片检修后
     */
    private final static String check_towerImg_after_path = check_path + "照片/塔体照片/检修后/";

    /**
     * 巡检隐患照片检修前
     */
    private final static String check_riskImg_before_path = check_path + "照片/记录照片/检修前/";

    /**
     * 巡检隐患照片检修中
     */
    private final static String check_riskImg_ing_path = check_path + "照片/记录照片/检修中/";

    /**
     * 巡检隐患照片检修后
     */
    private final static String check_riskImg_after_path = check_path + "照片/记录照片/检修后/";


    private final static String check_questionImg = check_path + "照片/问题照片/";

    /**
     * 巡检报告
     */
    private final static String check_report_path = check_path + "报告/";


    private static String path(String workOrderType) {
        StringBuilder sb = new StringBuilder();
        sb.append("通信塔资料/中华人民共和国/{provinceName}/{areaName}/{location}/");
        sb.append("{towerName}+{addRessCode}/{towerName}+{addRessCode}+");
        if (StringUtil.isEqual(workOrderType, "repair")) {
            sb.append("加固改造/加固改造+{constractCode}/");
        } else if (StringUtil.isEqual(workOrderType, "check")) {
            sb.append("运营维护/代维检修/代维检修+{dateTime}/");
        } else if (StringUtil.isEqual(workOrderType, "detection")) {
            sb.append("运营维护/安全评估/安全评估+{constractCode}/");
        } else if (StringUtil.isEqual(workOrderType, "tower")) {
            sb.append("塔体照片/");
        }
        return sb.toString();
    }


    private static String strReplace(String str, String provinceName, String areaName,
                                     String location, String towerName, String addRessCode,
                                     String constractCode, String dateTime) {
        str = str.replace("{provinceName}", provinceName);
        str = str.replace("{areaName}", areaName);
        str = str.replace("{location}", location);
        str = str.replace("{towerName}", towerName);
        str = str.replace("{addRessCode}", addRessCode);
        str = str.replace("{constractCode}", constractCode);
        str = str.replace("{dateTime}", dateTime);
        return str;
    }

    public static String getFilePath(FileTypeEnum typeEnum, String provinceName, String areaName,
                                     String location, String towerName, String addRessCode,
                                     String constractCode, String dateTime) {
        String path = "";//savePath;
//        if (StringUtil.isEqual(type, "read")) {
//            path = readPath;
//        }

        switch (typeEnum) {
            case repair_towerImg:
                path += strReplace(repair_towerImg_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case repair_riskImg_before:
                path += strReplace(repair_riskImg_before_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case repair_riskImg_ing:
                path += strReplace(repair_riskImg_ing_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case repair_riskImg_after:
                path += strReplace(repair_riskImg_after_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case repair_report:
                path += strReplace(repair_report_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case detection_towerImg:
                path += strReplace(detection_towerImg_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case detection_riskImg:
                path += strReplace(detection_riskImg_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case detection_antennaImg:
                path += strReplace(detection_antennaImg_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;
            case detection_report:
                path += strReplace(detection_report_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case detection_drawingImg:
                path += strReplace(detection_drawingImg_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case check_towerImg_before:
                path += strReplace(check_towerImg_before_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case check_towerImg_after:
                path += strReplace(check_towerImg_after_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case check_riskImg_before:
                path += strReplace(check_riskImg_before_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case check_riskImg_ing:
                path += strReplace(check_riskImg_ing_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case check_riskImg_after:
                path += strReplace(check_riskImg_after_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;

            case check_questionImg:
                path += strReplace(check_questionImg, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;
            case check_report:
                path += strReplace(check_report_path, provinceName, areaName, location, towerName, addRessCode, constractCode, dateTime);
                break;
        }
        return path;

    }

    private static String strReplace(String path, FilePath filePath) {
        path = path.replace("{provinceName}", filePath.getProvinceName());
        path = path.replace("{areaName}", filePath.getAreaName());
        path = path.replace("{location}", filePath.getLocation());
        path = path.replace("{towerName}", filePath.getTowerName());
        path = path.replace("{addRessCode}", filePath.getAddRessCode());
        path = path.replace("{constractCode}", filePath.getConstractCode());
        path = path.replace("{dateTime}", filePath.getDateTime());
        return path;
    }

    public static String getFilePath(FilePath filePath, FileTypeEnum typeEnum) {
        String path = "";
//        if (StringUtil.isEqual(filePath.getType(), "read")) {
//            path = readPath;
//        }

        switch (typeEnum) {
            case repair_towerImg:
                path += strReplace(repair_towerImg_path, filePath);
                break;

            case repair_riskImg_before:
                path += strReplace(repair_riskImg_before_path, filePath);
                break;

            case repair_riskImg_ing:
                path += strReplace(repair_riskImg_ing_path, filePath);
                break;

            case repair_riskImg_after:
                path += strReplace(repair_riskImg_after_path, filePath);
                break;

            case repair_report:
                path += strReplace(repair_report_path, filePath);
                break;

            case detection_towerImg:
                path += strReplace(detection_towerImg_path, filePath);
                break;

            case detection_riskImg:
                path += strReplace(detection_riskImg_path, filePath);
                break;

            case detection_antennaImg:
                path += strReplace(detection_antennaImg_path, filePath);
                break;
            case detection_report:
                path += strReplace(detection_report_path, filePath);
                break;

            case detection_drawingImg:
                path += strReplace(detection_drawingImg_path, filePath);
                break;

            case check_towerImg_before:
                path += strReplace(check_towerImg_before_path, filePath);
                break;

            case check_towerImg_after:
                path += strReplace(check_towerImg_after_path, filePath);
                break;

            case check_riskImg_before:
                path += strReplace(check_riskImg_before_path, filePath);
                break;

            case check_riskImg_ing:
                path += strReplace(check_riskImg_ing_path, filePath);
                break;

            case check_riskImg_after:
                path += strReplace(check_riskImg_after_path, filePath);
                break;

            case check_questionImg:
                path += strReplace(check_questionImg, filePath);
                break;

            case check_report:
                path += strReplace(check_report_path, filePath);
                break;

            case towerImg:
                path += strReplace(towerImg, filePath);
                break;
        }
        return path;

    }

    public static String getTowerImgPath(FilePath filePath) {
        String imgPath = path("tower");
        imgPath = imgPath.replace("{provinceName}", filePath.getProvinceName());
        imgPath = imgPath.replace("{areaName}", filePath.getAreaName());
        imgPath = imgPath.replace("{location}", filePath.getLocation());
        imgPath = imgPath.replace("{towerName}", filePath.getTowerName());
        imgPath = imgPath.replace("{addRessCode}", filePath.getAddRessCode());
        return imgPath;

    }


}
