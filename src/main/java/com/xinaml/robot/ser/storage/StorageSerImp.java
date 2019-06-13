package com.xinaml.robot.ser.storage;

import com.xinaml.robot.base.dto.RT;
import com.xinaml.robot.base.ser.ServiceImpl;
import com.xinaml.robot.common.constant.PathConst;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.thread.CopyFileThread;
import com.xinaml.robot.common.utils.DateUtil;
import com.xinaml.robot.common.utils.FileUtil;
import com.xinaml.robot.common.utils.UserUtil;
import com.xinaml.robot.dto.storage.StorageDTO;
import com.xinaml.robot.entity.storage.Storage;
import com.xinaml.robot.entity.user.User;
import com.xinaml.robot.ser.user.UserSer;
import com.xinaml.robot.to.storage.FileInfo;
import com.xinaml.robot.vo.storage.FileVO;
import com.xinaml.robot.vo.storage.TreeVO;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class StorageSerImp extends ServiceImpl<Storage, StorageDTO> implements StorageSer {

    private static final String[] IMAGE_SUFFIX = new String[]{"jpg", "png",
            "jpeg", "bmp", "gif"}; // 缩略图支持类型
    private static Map<String, Integer> uploadInfoList = new HashMap<String, Integer>(); // 上传文件分片缓存信息

    @Autowired
    private UserSer userSer;

    /**
     * 文件信息列表
     *
     * @param path 路径
     * @return 文件列表信息
     * @throws SerException
     */
    public List<FileVO> list(String path) throws SerException {
        String realPath = getRealPath(path);
        java.io.File dir = new java.io.File(realPath);
        java.io.File[] files = dir.listFiles();
        return getFileVO(files);
    }

    /**
     * 文件夹树
     *
     * @param path 文件路径
     * @return 文件夹树结构
     * @throws SerException
     */
    public List<TreeVO> tree(String path) throws SerException {
        String realPath = getRealPath(path);
        java.io.File dir = new java.io.File(realPath);
        java.io.File[] files = dir.listFiles();
        return getTreeVO(files);
    }

    /**
     * 上传文件
     *
     * @param mFiles 上传文件列表
     * @param path   上传路径
     * @return 返回上传文件信息
     * @throws SerException
     */
    @Transactional
    public List<FileVO> upload(List<MultipartFile> mFiles, String path) throws SerException {
        String realPath = getRealPath(path);
        File rootFolder = new File(realPath);
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
        File[] files = new File[mFiles.size()];
        try {
            int index = 0;
            for (MultipartFile mfile : mFiles) {
                File file = new File(realPath + PathConst.SEPARATOR
                        + mfile.getOriginalFilename());
                if (file.exists()) { // 文件存在，不覆盖
                    file = handleSameFile(file);
                }
                mfile.transferTo(file);

                files[index++] = file;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            throw new SerException("文件上传异常！");
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerException("文件上传异常！");
        }
        return getFileVO(files);
    }

    /**
     * 创建文件夹
     *
     * @param path 路径
     * @param dir  创建文件夹名
     * @throws SerException
     */
    public void mkDir(String path, String dir) throws SerException {
        if (dir.equals(PathConst.SEPARATOR)) {
            throw new SerException("非法的文件夹名！");
        }
        String realPath = getRealPath(path);
        java.io.File file = new java.io.File(realPath + PathConst.SEPARATOR
                + dir);
        if (!file.exists()) {
            file.mkdirs();
        } else {
            throw new SerException("该文件目录已存在！");
        }
    }

    /**
     * 删除文件
     *
     * @param paths 删除路径数组
     * @throws SerException
     */
    @Transactional
    public void delFile(String[] paths) throws SerException {
        if (paths.length > 1) {
            Set<String> tSet = new HashSet<String>(Arrays.asList(paths));
            paths = tSet.toArray(new String[]{});
        }
        for (String path : paths) {
            String savePath = getRealPath(path);
            java.io.File file = new java.io.File(savePath);
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete(); //删除db文件
                } else {// 删除目录及目录下的所有文件
                    try {
                        FileUtils.deleteDirectory(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new SerException(e.getMessage());
                    }
                }
            } else {
                throw new SerException("该文件目录不存在！");
            }
        }
    }


    /**
     * 重命名
     *
     * @param path    路径
     * @param newName 新文件名
     * @throws SerException
     */
    @Transactional
    public File rename(String path, String oldName, String newName)
            throws SerException {
        if (path.startsWith("/")) {
            if (PathConst.SEPARATOR.equals("\\")) {
                path = path.replaceAll("/", "\\\\");
            }
        }
        String oldPath = getRealPath((path + PathConst.SEPARATOR + oldName));
        String newPath = getRealPath((path + PathConst.SEPARATOR + newName));
        if (!oldName.equals(newName)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
            java.io.File oldFile = new java.io.File(oldPath);
            java.io.File newFile = new java.io.File(newPath);
            if (!oldFile.exists()) {// 重命名文件不存在
                throw new SerException("重命名文件不存在！");
            }
            if (newFile.exists())
                throw new SerException(newName + "已经存在！");
            else {
                oldFile.renameTo(newFile);
                return newFile;
            }
        } else {
            throw new SerException("新文件名和旧文件名相同！");
        }
    }

    /**
     * 下载
     *
     * @param path 文件路径
     * @return 文件对象
     * @throws SerException
     */
    public File download(String path, boolean isFolder) throws SerException {
        File file = null;
        if (isFolder) { // 文件夹下载
            String savePath = getRealPath(path);
            try {
                String zipSavePath = StringUtils.substringBeforeLast(savePath,
                        PathConst.SEPARATOR)
                        + PathConst.SEPARATOR
                        + StringUtils.substringAfterLast(path,
                        PathConst.SEPARATOR) + ".zip";
                file = new File(zipSavePath); // 创建压缩文件
                FileOutputStream outputStream = null;
                outputStream = new FileOutputStream(file);
                FileUtil.toZip(savePath, outputStream, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else { // 普通文件
            String realPath = getRealPath(path);
            file = new File(realPath);
        }
        return file;
    }


    /**
     * 获取缩略图
     *
     * @param path 文件路径
     * @return 缩略图流
     * @throws SerException
     */
    public byte[] thumbnails(String path, String width, String height)
            throws SerException {
        String realPath = getRealPath(path);
        String suffix = StringUtils.substringAfterLast(path, ".");
        boolean exist = false;
        for (String sx : IMAGE_SUFFIX) {
            if (sx.equalsIgnoreCase(suffix)) {
                exist = true;
            }
        }
        if (exist) {
            int w = 200;
            int h = 160;
            if (null != width && null != height) {
                w = Integer.parseInt(width);
                h = Integer.parseInt(height);
            }
            try {
                Thumbnails.Builder<java.io.File> fileBuilder = Thumbnails
                        .of(new File(realPath)).forceSize(w, h).outputQuality(0.35f)
                        .outputFormat(suffix);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                fileBuilder.toOutputStream(os);
                ImageIO.scanForPlugins();
                return os.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                throw new SerException("不支持该文件类型缩略图");

            }
        } else {
            throw new SerException("不支持该文件类型缩略图");
        }
    }


    /**
     * 文件是否存在
     *
     * @param path 文件路径
     * @return
     * @throws SerException
     */
    public Boolean existsFile(String path) throws SerException {
        String realPath = getRealPath(path);
        return new java.io.File(realPath).exists();
    }

    /**
     * 文件移动
     *
     * @param fromPath 复制的文件路径
     * @param toPath   复制到的文件路径
     * @return
     * @throws SerException
     */
    @Transactional
    public Boolean move(String[] fromPath, String toPath)
            throws SerException {
        String basePath = getRealPath(PathConst.SEPARATOR);
        String to = basePath + toPath;
        if (fromPath.length > 1) {
            Set<String> tSet = new HashSet<>(Arrays.asList(fromPath));
            fromPath = tSet.toArray(new String[]{});
        }
        for (String from : fromPath) {
            java.io.File fromFile = new java.io.File(basePath + from);
            java.io.File toFile = new java.io.File(to);
            if (toFile.isFile()) {
                throw new SerException("不允许移动文件或文件夹到文件下！");
            } else {
                try {
                    if (fromFile.isFile()) {
                        FileUtils.moveFileToDirectory(fromFile, toFile, true);
                    } else {
                        FileUtils.moveDirectoryToDirectory(fromFile, toFile,
                                true);
                    }
                } catch (IOException e) {
                    if (-1 != e.getMessage().indexOf("already exists")) {
                        throw new SerException("移动文件夹失败，" + "[" + toPath
                                + "]" + "目录下已存在相同的文件夹！");
                    }
                    throw new SerException(e.getMessage());
                }
            }
        }
        return true;
    }

    /**
     * 文件复制
     *
     * @param fromPath 复制的文件路径
     * @param toPath   复制到的文件路径
     * @return
     * @throws SerException
     */
    @Transactional
    public Boolean copy(String[] fromPath, String toPath)
            throws SerException {
        String basePath = getRealPath(PathConst.SEPARATOR);
        String to = basePath + toPath;
        if (fromPath.length > 1) {
            Set<String> tSet = new HashSet<String>(Arrays.asList(fromPath));
            fromPath = tSet.toArray(new String[]{});
        }
        for (String from : fromPath) {
            String fp = basePath + from;
            if (from.startsWith(PathConst.ROOT_PATH)) { //如果传入的是全路径，则不处理
                fp = from;
            }
            java.io.File fromFile = new java.io.File(fp);
            java.io.File toFile = new java.io.File(to);
            if (toFile.isFile()) {
                throw new SerException("不允许复制文件或文件夹到文件下！");
            } else {
                if (fromFile.length() > 200000000) { // 超过200m的文件
                    File f = new File(toFile.getPath() + PathConst.SEPARATOR
                            + fromFile.getName()); // 目标目录是否有相同文件名
                    if (f.exists()) {
                        toFile = handleSameFile(f);
                    }
                    new Thread(new CopyFileThread(fromFile, toFile, f.exists()))
                            .start();
                } else {
                    try {
                        if (fromFile.isFile()) {
                            File f = new File(toFile.getPath()
                                    + PathConst.SEPARATOR + fromFile.getName()); // 目标目录是否有相同文件名
                            if (f.exists()) {
                                toFile = handleSameFile(f);
                                FileCopyUtils.copy(fromFile, toFile);
                            } else {
                                FileUtils.copyFileToDirectory(fromFile, toFile,
                                        true);
                            }
                        } else {
                            String lastDir = StringUtils.substringAfterLast(
                                    from, PathConst.SEPARATOR);
                            if (new File(toFile + PathConst.SEPARATOR
                                    + lastDir).exists()) {
                                throw new SerException("复制文件夹失败，" + "["
                                        + toPath + "]" + "目录下已存在相同的文件夹！");
                            }
                            FileUtils
                                    .copyDirectoryToDirectory(fromFile, toFile);
                        }
                    } catch (IOException e) {
                        throw new SerException(e.getMessage());
                    }
                }
            }
        }
        return true;
    }

    /**
     * /** 保存分片文文件全名
     *
     * @param info 文件
     * @throws Exception
     */
    public Boolean savePartFile(FileInfo info) throws SerException {
        String savePath = PathConst.TMP_PATH + PathConst.SEPARATOR
                + info.getGuid();// 以文件名创建一个临时保存目录
        File uploadFile = new File(savePath + PathConst.SEPARATOR
                + info.getPartName());
        File fileDirectory = new File(savePath);
        synchronized (fileDirectory) { // 判断文件夹是否存在，不存在就创建一个
            if (!fileDirectory.exists()) {
                fileDirectory.mkdirs();
            }
        }
        try {
            info.getFile().transferTo(uploadFile);
        } catch (Exception e) {
            fileDirectory.deleteOnExit();
            throw new SerException("上传文件出错");
        }
        return uploadFile.exists();
    }

    /**
     * 完整上传
     */
    @Transactional
    public void bigUploaded(FileInfo f) throws SerException {
        synchronized (uploadInfoList) {
            if (f.getMd5value() != null) {
                Integer size = uploadInfoList.get(f.getMd5value());
                size = size != null ? size : 0;
                if (null != size) {
                    int v = size + 1;
                    if (v <= Integer.parseInt(f.getChunks())) { // 避免缓存错误导致文件上传完成后合并不成功
                        uploadInfoList.put(f.getMd5value(), v);
                    }
                }
            }
        }
        boolean allUploaded = isAllUploaded(f.getMd5value(), f.getChunks());
        if (allUploaded) { // 已上传完整
            mergeFile(f);// 合并文件
            File file = new File(getRealPath(f.getPath())
                    + PathConst.SEPARATOR + f.getName());
            if (file.exists()) {
                saveFileToDb(file, f.getMd5value());
            }
        }
    }

    /**
     * 查询md5是否存在，实现秒传
     *
     * @param md5
     * @param toPath 上传的文件路径
     * @return
     */
    @Transactional
    public Boolean md5Exist(String md5, String toPath, String fileName, boolean save) throws SerException {
        //查询数据库
        StorageDTO dto = new StorageDTO();
        dto.addRT(RT.eq("md5", md5));
        List<Storage> list = super.findByRTS(dto);
        if (0 > list.size() && save) {
            User u = userSer.findById(list.get(0).getId());
            String path = PathConst.ROOT_PATH + PathConst.SEPARATOR + u.getUsername() + list.get(0).getPath();
            copy(new String[]{path}, toPath);
            String name = StringUtils.substringAfterLast(list.get(0)
                    .getPath(), PathConst.SEPARATOR);
            if (!name.equals(fileName)) {
                File file = rename(toPath, name, fileName);
                saveFileToDb(file, md5);
            }
        }
        return list.size() > 0;
    }

    /**
     * 是否上传完成
     *
     * @param md5
     * @param chunks 分片数
     * @return
     */
    public Boolean isAllUploaded(String md5, String chunks) {
        Integer size = uploadInfoList.get(md5); // 上传完成的部分
        size = size != null ? size : 0;
        boolean bool = (size == Integer.parseInt(chunks)); // 上传的部分跟总的分片数一致则代表上传完成
        if (bool) {
            synchronized (uploadInfoList) {
                // 删除缓存
                for (Map.Entry<String, Integer> entry : uploadInfoList.entrySet()) {
                    if (entry.getKey().equals(md5)) {
                        uploadInfoList.remove(entry.getKey());
                    }
                }
            }
        }
        return bool;
    }


    /**
     * 合并分片文件
     *
     * @param info
     * @throws SerException
     */
    private void mergeFile(FileInfo info) throws SerException {
        /* 合并输入流 */
        String uploadPath = getRealPath(info.getPath());
        String mergePath = PathConst.TMP_PATH + PathConst.SEPARATOR + info.getGuid()
                + PathConst.SEPARATOR;

        SequenceInputStream s;
        int chunksNumber = Integer.parseInt(info.getChunks());
        try {
            InputStream s1 = new FileInputStream(mergePath + 0 + info.getExt());
            InputStream s2 = new FileInputStream(mergePath + 1 + info.getExt());
            s = new SequenceInputStream(s1, s2);
            for (int i = 2; i < chunksNumber; i++) {
                InputStream s3 = new FileInputStream(mergePath + i + info.getExt());
                s = new SequenceInputStream(s, s3);
            }
            File f = new File(uploadPath + PathConst.SEPARATOR + info.getName());
            if (f.exists()) {
                f = handleSameFile(f);
            }
            FileUtil.saveStreamToFile(s, f.getPath()); // 合并文件
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerException("合并文件失败");
        } finally {
            // 删除缓存
            for (Map.Entry<String, Integer> entry : uploadInfoList.entrySet()) {
                if (entry.getKey().equals(info.getMd5value())) {
                    uploadInfoList.remove(entry.getKey());
                }
            }
            try {
                FileUtils.deleteDirectory(new File(mergePath));// 删除保存分块文件的文件夹
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取真实路径
     *
     * @param path 路径
     * @return 真实的磁盘路径
     */
    private String getRealPath(String path) throws SerException {
        if (path.indexOf("##") != -1) {
            throw new SerException("【##】为非法字符！");
        }
        String username = UserUtil.getUser().getUsername();

        if (StringUtils.isNotBlank(path)) {
            if (PathConst.SEPARATOR.equals("\\")) {
                path = path.replaceAll("/", "\\\\");
            }
            if (path.equals(PathConst.SEPARATOR)) { // 如果是跟路径
                path = "";
            } else if (!StringUtils.startsWith(path, PathConst.SEPARATOR)) { // 如果不是跟路径并且不是/开头
                path = PathConst.SEPARATOR + path;
            }
            path = PathConst.ROOT_PATH + PathConst.SEPARATOR + username + path;

        } else {
            throw new SerException("path 不能为空！");
        }
        return path;
    }

    /**
     * 通过文件获取保存数据库对应的数据
     *
     * @param file
     * @return
     */
    private String getDbFilePath(java.io.File file) {
        User u = UserUtil.getUser();
        return PathConst.SEPARATOR + StringUtils.substringAfterLast(file.getPath(), PathConst.ROOT_PATH + PathConst.SEPARATOR + u.getUsername() + PathConst.SEPARATOR);
    }

    /**
     * 通过文件列表获取文件的详细信息
     *
     * @param files
     * @return
     */
    private List<FileVO> getFileVO(java.io.File[] files)
            throws SerException {
        String rootPath = getRealPath(PathConst.SEPARATOR);
        if (null != files) {
            List<FileVO> floders = new ArrayList<FileVO>(0);// 文件夹
            List<FileVO> fileVOS = new ArrayList<FileVO>(files.length);
            for (int i = 0; i < files.length; i++) {
                FileVO fileVO = new FileVO();
                java.io.File file = files[i];
                fileVO.setFileType(FileUtil.getFileType(file));
                fileVO.setPath(StringUtils.substringAfter(file.getPath(), rootPath));
                fileVO.setDir(file.isDirectory());
                fileVO.setName(file.getName());
                fileVO.setModifyTime(DateUtil.dateToString((DateUtil.dateToLocalDateTime(new Date(file.lastModified())))));
                if (file.isFile()) {
                    fileVO.setSize(FileUtil.getFileSize(file));
                    fileVO.setLength(file.length());
                    fileVOS.add(fileVO);
                } else {
                    floders.add(fileVO);
                }
            }
            Collections.sort(fileVOS);
            Collections.sort(floders);
            fileVOS.addAll(0, floders);
            return fileVOS;
        }
        return new ArrayList<>(0);
    }

    /**
     * 通过文件列表获取文件夹树的详细信息
     */
    private List<TreeVO> getTreeVO(java.io.File[] files) {
        List<TreeVO> folders = new ArrayList<TreeVO>(0);// 文件夹

        if (null != files) {
            for (int i = 0; i < files.length; i++) {
                java.io.File file = files[i];
                if (file.isDirectory()) {
                    TreeVO vo = new TreeVO();
                    vo.setId(getDbFilePath(file));
                    vo.setName(file.getName());
                    boolean isParent = false;
                    for (File f : file.listFiles()) {
                        if (f.isDirectory()) {
                            isParent = true;
                            break;
                        }
                    }
                    vo.setIsParent(isParent);
                    vo.setModifyTime(DateUtil.dateToString((DateUtil.dateToLocalDateTime(new Date(file.lastModified())))));
                    folders.add(vo);
                }
            }
        }

        return folders;
    }


    /**
     * 处理同名文件
     *
     * @param file
     * @return
     */
    String regex = "\\(.*?\\)";

    private File handleSameFile(File file) {
        File[] files = file.getParentFile().listFiles();
        int count = 0;
        String fileName = file.getName();
        String[] temps = fileName.split("\\.");
        String simpleName = temps[0];
        String ext = temps.length > 1 ? temps[temps.length - 1] : "";
        for (File f : files) {
            String name = f.getName();
            String na = name.split("\\.")[0];
            name = na.replaceAll(regex, "");
            if (name.equals(simpleName)) {
                count += 1;
            }
        }
        String basePath = StringUtils.substringBeforeLast(file.getPath(), PathConst.SEPARATOR);
        String path = basePath;
        if (StringUtils.isNotBlank(ext)) {
            ext = "." + ext;
        }
        path += (PathConst.SEPARATOR + simpleName + "(" + count + ")" + ext);
        File f = new File(path);
        while (f.exists()) {
            count += 1;
            path = basePath;
            path += (PathConst.SEPARATOR + simpleName + "(" + count + ")" + ext);
            f = new File(path);
        }
        return f;
    }

    private void saveFileToDb(File file, String md5) throws SerException {
        User user = UserUtil.getUser();
        Storage storage = new Storage();
        storage.setUser(user);
        storage.setMd5(md5);
        storage.setPath(StringUtils.substringAfterLast(file.getPath(), PathConst.ROOT_PATH+PathConst.SEPARATOR+user.getUsername()));
        storage.setFileName(file.getName());
        storage.setCreateDate(LocalDateTime.now());
        super.save(storage);
    }

}
