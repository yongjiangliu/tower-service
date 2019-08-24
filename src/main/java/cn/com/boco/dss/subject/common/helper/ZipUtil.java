package cn.com.boco.dss.subject.common.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by yxy on 2019/08/19 15:36
 * Version 1.0.0
 * Description 下载或打包文件
 */
public class ZipUtil {

    /**
     * 创建zip文件
     *
     * @param files           待打包的文件集合
     * @param packageNamePath 当批量下载多个文档时，统一打包成一个压缩文件名的路径，如：D:/sichuan/doc/档案中心.zip
     */
    public static void createZipFile(List<File> files, String packageNamePath, HttpServletResponse response) throws Exception {
        try {
            //List<File> 作为参数传进来，就是把多个文件的路径放到一个list里面
            //创建一个临时压缩文件
            //临时文件可以放在CDEF盘中，但不建议这么做，因为需要先设置磁盘的访问权限，
            // 最好是放在服务器上，方法最后有删除临时文件的步骤

            File file = new File(packageNamePath);
            file.deleteOnExit();
            file.createNewFile();//创建文件
            file.setExecutable(true);//设置可执行权限
            file.setReadable(true);//设置可读权限
            file.setWritable(true);//设置可写权限

            //重置
            response.reset();
            //创建文件输出流
            FileOutputStream fileOutput = new FileOutputStream(file);
            ZipOutputStream zipOutput = new ZipOutputStream(fileOutput);
            zipFile(files, zipOutput);
            zipOutput.close();
            fileOutput.close();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 下载单个文件
     *
     * @param file
     * @param originFileName 下载的压缩包的文件名 (注意：不是路径)，如 "铁塔资料.zip"
     * @param request
     * @param response
     */


    /**
     * 下载单个文件(支持各种文件类型 如zip/doc/xlsx/ppt/txt等)
     *
     * @param file     要下载的文件对象
     * @param request  request对象
     * @param response response对象
     * @throws Exception
     */
    public static void downloadSingleFile(File file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (file != null) {
            if (file.exists()) {
                if (file.isFile()) {
                    try {
                        // 以流的形式下载文件。
                        InputStream input = new BufferedInputStream(new FileInputStream(file.getPath()));
                        byte[] buffer = new byte[input.available()];
                        input.read(buffer);
                        input.close();
                        // 清空response
                        response.reset();
                        generate(file, request, response);

                        OutputStream output = new BufferedOutputStream(response.getOutputStream());
                        output.write(buffer);
                        output.flush();
                        output.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        throw ex;
                    }
                }
                else {
                    throw new Exception("输入的路径必须是具体的【文件路径】（不能是目录），请核实。");
                }
            }
            else {
                throw new Exception("下载的文件不存在，请核实。");
            }
        }
        else {
            throw new Exception("输入的文件路径不存在，请核实。");
        }

    }


    /**
     * 多个文件打成压缩包
     */
    private static void zipFile(List<File> files, ZipOutputStream outputStream) {
        for (Object file1 : files) {
            File file = (File) file1;
            zipFile(file, outputStream);
        }
    }

    /**
     * 单个文件打包，根据输入的文件与输出流对文件进行打包
     */
    private static void zipFile(File inputFile, ZipOutputStream outputStream) {
        try {
            if (inputFile.exists()) {
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    outputStream.putNextEntry(entry);

                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, nNumber);
                    }

                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                }
                else {
                    try {
                        File[] files = inputFile.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                zipFile(file, outputStream);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置文件属性和返回数据
     *
     * @param request  request对象
     * @param response response对象
     * @throws Exception
     */
    private static void generate(File file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //octet-stream 自动匹配文件类型
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("success", "true");

        String userAgent = request.getHeader("User-Agent");
        String originFileName = file.getName();//只获取文件名
        String formFileName = DssHelper.encodeFileName(request, originFileName);
        response.setHeader("Content-Disposition", "attachment;" + formFileName);
    }


    /**
     * 删除目录及其目录下的所有文件
     *
     * @param fileOrDirectoryPath 文件路径或文件夹路径
     * @throws IOException
     */
    public static void delFileOrDirectory(String fileOrDirectoryPath) throws IOException {
        File f = new File(fileOrDirectoryPath);//定义文件路径
        if (f != null && f.exists()) {
            if (f.isDirectory()) {//目录
                if (f.listFiles().length == 0) {//若目录下没有文件则直接删除
                    f.delete();
                }
                else {//若目录下有文件
                    File[] delFile = f.listFiles();
                    int len = f.listFiles().length;
                    for (int j = 0; j < len; j++) {
                        //                        delFileOrDirectory(delFile[j].getCanonicalPath());//递归调用del方法并取得子目录路径
                        if (delFile[j].isDirectory()) {
                            delFileOrDirectory(delFile[j].getAbsolutePath());//递归调用del方法并取得子目录路径
                        }
                        delFile[j].delete();//删除文件
                    }
                }
            }
            else {//文件
                f.delete();
            }
        }
        else {
            System.out.println("警告：要删除的路径【" + fileOrDirectoryPath + "】不存在");
        }
    }
}
