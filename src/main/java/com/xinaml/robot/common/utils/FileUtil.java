package com.xinaml.robot.common.utils;

import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.custom.exception.SerException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class FileUtil {

    /**
     * 获取文件类型
     *
     * @param file
     * @return
     */
    public static String getFileType(File file) {
        if (file.isFile()) {
            try {
                String suffix = StringUtils.substringAfterLast(file.getName(), ".");
                suffix = suffix.toLowerCase();
                return suffix;
            } catch (Exception e) {
                return "UNKNOW";
            }
        } else {
            return "FOLDER";
        }
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     */
    public static String getFileSize(File file) {

        long size = file.length();
        if (size > 1024) {
            double kb = size / 1024.0;
            if (kb > 1024) {
                double mb = kb / 1024.0;
                if (mb > 1024) {
                    return getBySeconds(mb / 1024.0) + "GB";
                } else {
                    return getBySeconds(mb) + "MB";
                }
            } else {
                return getBySeconds(kb) + "KB";
            }

        } else {
            return size + "B";
        }
    }

    /**
     * 保留两位小数
     *
     * @return
     */
    private static double getBySeconds(double val) {
        return new BigDecimal(val).setScale(1, RoundingMode.UP).doubleValue();
    }

    /**
     * 文件转字节
     *
     * @param filePath 文件路径
     * @return
     * @throws SerException
     */
    public static byte[] FileToByte(String filePath) throws ServiceException {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (!file.isDirectory()) {
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];
                    int n;
                    while ((n = fis.read(b)) != -1) {
                        bos.write(b, 0, n);
                    }
                    fis.close();
                    bos.close();
                    buffer = bos.toByteArray();
                }
            } else {
                throw new ServiceException("文件不存在！");
            }

        } catch (FileNotFoundException e) {
            throw new ServiceException(e.getMessage());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
        return buffer;
    }

    /**
     * 通过request获取上传文件
     *
     * @param request
     * @return
     * @throws SerException
     */
    public static List<MultipartFile> getMultipartFile(HttpServletRequest request) throws SerException {

        if (null != request && !isMultipartContent(request)) {
            throw new ServiceException("上传表单不是multipart/form-data类型！");
        }
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request; // 转换成多部分request
        Map<String, MultipartFile> map = multiRequest.getFileMap();
        List<MultipartFile> files = new ArrayList<MultipartFile>(map.size());
        for (Map.Entry<String, MultipartFile> entry : map.entrySet()) {
            files.add(entry.getValue());
        }
        return files;

    }

    public static void saveStreamToFile(InputStream inputStream, String filePath)
            throws Exception {
        /* 创建输出流，写入数据，合并分块 */
        OutputStream outputStream = new FileOutputStream(filePath);
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            outputStream.close();
            inputStream.close();
        }
    }

    /**
     * 上传是否合理
     *
     * @param request
     * @return
     */
    private static boolean isMultipartContent(HttpServletRequest request) {
        if (!"post".equals(request.getMethod().toLowerCase())) {
            return false;
        }

        String contentType = request.getContentType();  //获取Content-Type
        if ((contentType != null) && (contentType.toLowerCase().startsWith("multipart/"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 压缩成ZIP
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static File toZip(String srcDir, FileOutputStream out, boolean KeepDirStructure)
            throws RuntimeException {
        ZipOutputStream zos = null;

        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            return sourceFile;
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    /**
     * 递归压缩方法
     *
     * @param sourceFile 源文件
     * @param zos         zip输出流
     * @param name         压缩后的名称
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
     * false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static final int BUFFER_SIZE = 2 * 1024;

    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }

            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }

                }
            }
        }
    }


    public static void writeOutFile(HttpServletResponse response, File file) {
        try {
            if (file.exists()) {
                String dfileName = file.getName();
                InputStream fis = new BufferedInputStream(new FileInputStream(file));
                response.reset();
                response.setContentType("application/x-download");
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(dfileName.getBytes(), "iso-8859-1"));
                response.addHeader("Content-Length", "" + file.length());
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                byte[] buffer = new byte[1024 * 1024 * 4];
                int i = -1;
                while ((i = fis.read(buffer)) != -1) {
                    toClient.write(buffer, 0, i);
                }
                fis.close();
                toClient.flush();
                toClient.close();

            } else {
                PrintWriter out = response.getWriter();
                out.print("<script>alert(\"not find the file\")</script>");
            }
        } catch (IOException ex) {
            ResponseUtil.writeData("<script>alert(\"not find the file\")</script>");
        }
    }

}
