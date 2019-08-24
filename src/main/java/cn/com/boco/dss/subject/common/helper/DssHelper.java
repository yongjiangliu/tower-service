package cn.com.boco.dss.subject.common.helper;

import cn.com.boco.dss.common.data.DataTable;
import cn.com.boco.dss.common.excel.ExcelUtil;
import cn.com.boco.dss.common.util.StringUtil;
import cn.com.boco.dss.webcore.data.DataSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yxy on 2019/08/13 18:54
 * Version 1.0.0
 * Description 常用开发帮助类
 */

public class DssHelper {
    /**
     * 把JSON字符串反序列化对象
     *
     * @param jsonStr  json字符串
     * @param classOfT 类对象
     * @return
     * @throws Exception
     */
    public static <T> T fromJson(String jsonStr, Class<T> classOfT) throws Exception {
        return fromJson(jsonStr, false, classOfT);
    }

    /**
     * 把JSON字符串反序列化对象
     *
     * @param jsonStr
     * @param isNeedEncode
     * @param classOfT
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T fromJson(String jsonStr, boolean isNeedEncode, Class<T> classOfT) throws Exception {
        if (StringUtil.isNullOrEmpty(jsonStr)) {
            return null;
        }
        if (isNeedEncode) {
            jsonStr = java.net.URLDecoder.decode(jsonStr, "UTF-8");
        }
        jsonStr = jsonStr.replace("\"\"", "null");
        Logger log = LoggerFactory.getLogger(classOfT);
        log.error(classOfT.getName() + "：" + jsonStr);
        System.out.println(classOfT.getName() + "：" + jsonStr);
        return new GsonBuilder().serializeNulls().create().fromJson(jsonStr, classOfT);
    }

    /**
     * 写入日志功能(示例：DssHelper.log("XXX", this.getClass());)
     *
     * @param message  日志内容
     * @param classOfT 当前类
     */
    public static <T> void log(String message, Class<T> classOfT) {
        log(message, classOfT, false);
    }

    /**
     * 写入日志功能(示例：DssHelper.log("XXX", this.getClass());)
     *
     * @param message              日志内容
     * @param classOfT             当前类
     * @param isOnlySystemOutPrint 是否只控制台打印（不写入日志）
     */
    public static <T> void log(String message, Class<T> classOfT, boolean isOnlySystemOutPrint) {
        Logger log = LoggerFactory.getLogger(classOfT);
        if (isOnlySystemOutPrint) {
            System.out.println(classOfT.getName() + " ====控制台输出====：" + message);
        }
        else {
            log.error(classOfT.getName() + " ====日志内容====：" + message);
            System.out.println(classOfT.getName() + " ====控制台输出====：" + message);
        }
        log.error("\r\n\r\n");
    }

    /**
     * 把JSON字符串反序列化为List
     *
     * @param jsonStrList 格式：[{"id":"1","measureName":null},{"id":"2","measureName":"m1"},{"id":"3","measureName":"m2"}]
     * @param classOfT    数组中的元素对象，如返回的是List<A>，传入A.class即可。
     * @return
     * @throws Exception
     */
    public static <T> ArrayList<T> fromJsonList(String jsonStrList, Class<T> classOfT) throws Exception {
        if (StringUtil.isNullOrEmpty(jsonStrList)) {
            return null;
        }
        ArrayList<T> listOfT = new ArrayList<>();
        try {
            jsonStrList = jsonStrList.replace("\"\"", "null");
            Type type = new TypeToken<ArrayList<JsonObject>>() {}.getType();
            ArrayList<JsonObject> jsonObjs = new Gson().fromJson(jsonStrList, type);

            for (JsonObject jsonObj : jsonObjs) {
                listOfT.add(new Gson().fromJson(jsonObj, classOfT));
            }
        } catch (JsonSyntaxException e) {
            if (e.getMessage().contains("Expected BEGIN_ARRAY but was BEGIN_OBJECT")) {
                String eg = "[{\"id\":1},{\"id\":2}]";
                throw new JsonSyntaxException("需要传入数组格式的json字符串，而不是对象格式。应传入格式：" + eg);
            }
        } catch (Exception e) {
            throw new Exception();
        }
        return listOfT;
    }

    /**
     * 对象转JSON字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(object);
    }

    /**
     * 根据传入的【列名称】和【表名称】构造DataTable
     *
     * @param arrColNames String[] arrColNames={"col1","col2","col3" };
     * @param tableName   可以为空或null值
     * @return
     */
    public static DataTable getDataTableByColNamesAndTableName(String[] arrColNames, String tableName) {
        DataTable dt = null;
        try {
            if (arrColNames.length == 0) {
                return null;
            }
            if (StringUtil.isNullOrEmpty(tableName)) {
                dt = new DataTable();
            }
            else {
                dt = new DataTable(tableName);
            }
            for (String colname : arrColNames) {
                dt.addColumn(colname, java.sql.Types.VARCHAR);
            }
        } catch (Exception e) {
            dt = null;
        }
        return dt;
    }

    /**
     * 根据传入的【列名称】构造DataSource
     *
     * @param arrColNames
     * @return
     */
    public static DataSource getDataSourceByColNames(String[] arrColNames) {
        if (arrColNames.length == 0) {
            return null;
        }
        DataSource ds = new DataSource();
        List<String> colNames = new ArrayList<String>();
        for (String colName : arrColNames) {
            colNames.add(colName);
        }
        ds.setColNames(colNames);
        return ds;
    }

    /**
     * 获取uri中的最后1个“/”之后的字符串action。
     * 如：http://localhost:8080/foshan/subject/grid/workorderfinish/abcde
     * 则获取的action值是:abcde;
     *
     * @param request
     * @return
     */
    public static String getActionByRequest(HttpServletRequest request) throws Exception {
        String action = null;
        String uri = request.getRequestURI();
        int pos = uri.lastIndexOf("/");
        if (pos > -1) {
            action = request.getRequestURI().substring(pos + 1, uri.length());
        }
        if (action == null) {
            throw new Exception("未获取到action的值");
        }
        return action;
    }

    /**
     * 获取两个日期的差值（返回枚举类型约定的单位值）
     *
     * @param startDate    开始日期
     * @param endDate      结束日期
     * @param enumTimeType 枚举类型
     * @return 差值
     */
    public static long getDiffTime(Date startDate, Date endDate, EnumTimeType enumTimeType) {
        // 毫秒
        long different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long result = 0;
        switch (enumTimeType) {
            case DAY:
                result = different / daysInMilli;
                break;
            case HOUR:
                result = different / hoursInMilli;
                break;
            case MINUTE:
                result = different / minutesInMilli;
                break;
            case SECOND:
                result = different / secondsInMilli;
                break;
            case MILLISECOND:
                result = different;
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 返回2个日期差值的字符串形式，如：01日09时05分45秒
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 差值
     */
    public static String getDiffTimeStr(Date startDate, Date endDate) {
        // 毫秒
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return String.format("%s日%s时%s分%s秒", elapsedDays < 10 ? "0" + elapsedDays : elapsedDays, elapsedHours < 10 ? "0" + elapsedHours : elapsedHours, elapsedMinutes < 10 ? "0" + elapsedMinutes : elapsedMinutes, elapsedSeconds < 10 ? "0" + elapsedSeconds : elapsedSeconds);
    }

    /**
     * double类型的字符串转N为小数的数字
     *
     * @param str    double类型的字符串
     * @param digits 小数位数 必须大于0
     * @return
     */
    public static Double parseDouble(String str, int digits) {
        Double b = Double.valueOf(str).doubleValue();
        if (digits > 0) {
            String pattern = "#.";
            while (digits > 0) {
                pattern += "0";
                digits--;
            }
            DecimalFormat df = new DecimalFormat(pattern);// 此为保留1位小数，若想保留2位小数，则填写#.00 ，以此类推
            String temp = df.format(b);
            b = Double.valueOf(temp);
        }
        return b;
    }

    /**
     * double类型的字符串转double（四舍五入）
     *
     * @param str
     * @return double类型的字符串
     */
    public static Double parseDouble(String str) {
        Double result = 0.0;
        if (!StringUtil.isNullOrEmpty(str)) {
            result = Double.parseDouble(str);
        }
        return result;
    }

    /**
     * double保留N为小数（四舍五入）
     *
     * @param d
     * @param digits 保留小数位数
     * @return
     */
    public static Double parseDouble(Double d, int digits) {
        Double result = 0.0;
        if (d != null && d > 0) {
            result = new BigDecimal(d).setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return result;
    }

    /**
     * X除以Y后的结果是多少 double类型
     *
     * @param X      被除数
     * @param Y      除数
     * @param digits 保留小数位数
     * @return
     * @throws Exception
     */
    public static Double XbeDividedByY(int X, int Y, int digits) throws Exception {
        if (Y == 0) {
            throw new Exception("除数不能为0");
        }
        Double d = new BigDecimal((float) X / Y).setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

    /**
     * X除以Y后的结果是多少 double类型
     *
     * @param X      被除数
     * @param Y      除数
     * @param digits 保留小数位数
     * @return
     * @throws Exception
     */
    public static Double XbeDividedByY(Double X, Double Y, int digits) throws Exception {
        Double result = 0.0;
        if (X == null || Y == null) {
            return result;
        }
        if (Y == 0) {
            throw new Exception("除数不能为0");
        }
        result = new BigDecimal(X / Y).setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

    /**
     * X除以Y并乘以100后的结果（一般计算百分比使用） X/Y*100
     *
     * @param X      被除数
     * @param Y      除数
     * @param digits 保留小数位数
     * @return
     * @throws Exception
     */
    public static Double XbeDividedByY100(Double X, Double Y, int digits) throws Exception {
        Double result = 0.0;
        if (X == null || Y == null) {
            return result;
        }
        if (Y == 0) {
            throw new Exception("除数不能为0");
        }
        result = new BigDecimal(X / Y).multiply(new BigDecimal("100")).setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

    /**
     * X除以Y并乘以100后的结果（一般计算百分比使用） X/Y*100
     *
     * @param X      被除数
     * @param Y      除数
     * @param digits 保留小数位数
     * @return
     * @throws Exception
     */
    public static Double XbeDividedByY100(Integer X, Integer Y, int digits) throws Exception {
        Double result = 0.0;
        if (X == null || Y == null) {
            return result;
        }
        if (Y == 0) {
            throw new Exception("除数不能为0");
        }
        result = new BigDecimal((float) X / Y).multiply(new BigDecimal("100")).setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

    /**
     * 获取当前时间戳带毫秒（格式：yyyyMMddHHmmssSSS，毫秒）
     *
     * @return
     */
    public static String getNowTimeMS() {
        return getNowTime("yyyyMMddHHmmssSSS");
    }

    /**
     * 获取当前时间
     *
     * @param format 格式，如：yyyyMMddHHmmss
     * @return
     */
    public static String getNowTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);// 设置日期格式
        String now = df.format(new Date());
        return now;
    }

    /**
     * 导出功能
     *
     * @param response       响应对象
     * @param dt             导出数据源
     * @param exportFileName 导出的文件名
     * @return
     * @throws Exception
     */
    public static void export(HttpServletResponse response, DataTable dt, String exportFileName) throws Exception {
        try (OutputStream outputStream = response.getOutputStream()) {
            // 文件名为空时，按照时间戳生成一个文件名
            if (StringUtil.isNullOrEmpty(exportFileName)) {
                exportFileName = getNowTimeMS();
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(exportFileName + ".xlsx", "UTF-8"));
            dt.setTableName(exportFileName);
            ExcelUtil.toExcel(dt, outputStream);
        } catch (Exception e) {
            String resultStr = StringUtil.isNullOrEmpty(e.getMessage()) ? "导出失败" : "导出失败，" + e.getMessage();
            throw new Exception(resultStr);
        }
    }

    /**
     * 获取指定月份的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month, String format) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    /**
     * 获取指定月份的最大天数
     *
     * @param year
     * @param month
     * @return 返回最大天数，如 28 29 30 31
     */
    public static int getMaxDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return lastDay;
    }

    /**
     * 将字符串格式yyyyMMdd的字符串转为日期，格式"yyyy-MM-dd"
     *
     * @param date 日期字符串
     * @return 返回格式化的日期
     * @throws ParseException 分析时意外地出现了错误异常
     */

    /**
     * 将originalformat格式的字符串dateStr，转为targetFormat格式的字符串
     *
     * @param dateStr        时间字符串
     * @param originalformat 时间字符串本身的格式
     * @param targetFormat   要转成的字符串格式
     * @return
     * @throws ParseException
     */
    public static String dateFormat(String dateStr, String originalformat, String targetFormat) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(originalformat);
        formatter.setLenient(false);
        Date newDate = formatter.parse(dateStr);
        formatter = new SimpleDateFormat(targetFormat);
        return formatter.format(newDate);
    }

    /**
     * 字符串是否是【整型】（为空或null是false）
     *
     * @param str
     * @return
     */
    public static boolean stringIsInt(String str) {
        if (StringUtil.isNullOrEmpty(str)) {
            return false;
        }
        //方法一：
        //      Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        //      Matcher matcher = pattern.matcher(str);
        //      return matcher.matches();

        //方法二：
        try {
            Integer.parseInt(str, 10);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * 判断是否是【数字】类型
     *
     * @param str
     * @return
     */
    public static boolean stringIsNumber(String str) {
        if (StringUtil.isNullOrEmpty(str)) {
            return false;
        }
        //方法一：
        //      Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        //      Matcher matcher = pattern.matcher(str);
        //      return matcher.matches();

        //方法二：
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * 获取客户端浏览器类型，获取编码后的文件名
     *
     * @param request
     * @param fileName
     * @return String
     */
    public static String encodeFileName(HttpServletRequest request, String fileName) {
        String userAgent = request.getHeader("User-Agent");
        String rtn = "";
        try {
            String new_filename = URLEncoder.encode(fileName, "UTF-8");
            // 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
            rtn = "filename=\"" + new_filename + "\"";
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                // IE浏览器，只能采用URLEncoder编码
                if (userAgent.indexOf("msie") != -1) {
                    rtn = "filename=\"" + new_filename + "\"";
                }
                // Opera浏览器只能采用filename*
                else if (userAgent.indexOf("opera") != -1) {
                    rtn = "filename*=UTF-8''" + new_filename;
                }
                // Safari浏览器，只能采用ISO编码的中文输出
                else if (userAgent.indexOf("safari") != -1) {
                    rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
                }
                // Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
                else if (userAgent.indexOf("applewebkit") != -1) {
                    // new_filename = MimeUtility.encodeText(fileName, "UTF-8", "B");
                    // rtn = "filename=\"" + new_filename + "\"";
                    rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
                }
                // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
                else if (userAgent.indexOf("mozilla") != -1) {
                    rtn = "filename*=UTF-8''" + new_filename;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    private enum EnumTimeType {
        /// <summary>
        /// 差值N天
        /// </summary>
        DAY,
        /// <summary>
        /// 差值N小时
        /// </summary>
        HOUR,
        /// <summary>
        /// 差值N分钟
        /// </summary>
        MINUTE,
        /// <summary>
        /// 差值N秒
        /// </summary>
        SECOND,
        /// <summary>
        /// 差值N毫秒
        /// </summary>
        MILLISECOND;

        // 获取枚举实例
        public static EnumTimeType fromName(String name) {
            for (EnumTimeType statusEnum : EnumTimeType.values()) {
                if (Objects.equals(name.trim().toLowerCase(), statusEnum.name().trim().toLowerCase())) {
                    return statusEnum;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
