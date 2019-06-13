package com.xinaml.robot.action.storage;

import com.xinaml.robot.base.atction.BaseAct;
import com.xinaml.robot.common.custom.annotation.Login;
import com.xinaml.robot.common.custom.exception.ActException;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.custom.result.Result;
import com.xinaml.robot.common.utils.ASEUtil;
import com.xinaml.robot.common.utils.FileUtil;
import com.xinaml.robot.common.utils.ResponseUtil;
import com.xinaml.robot.ser.storage.StorageSer;
import com.xinaml.robot.to.storage.FileInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@RequestMapping(value = "storage")
@RestController
@Login
public class StorageAct extends BaseAct {

    @Autowired
    private StorageSer storageSer;

    @GetMapping("page")
    public ModelAndView page() throws ActException {
        return new ModelAndView("/storage/storage");
    }

    /**
     * 文件列表
     * <p>
     * 文件路径
     *
     * @return
     */
    @GetMapping("list")
    public Result list(HttpServletRequest request) throws ActException {
        try {
            String path = getParameter(request, "path", true);
            return new ActResult(storageSer.list(path));
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 文件列表
     * <p>
     * 文件路径
     *
     * @return
     */
    @GetMapping("tree")
    public ActResult tree(HttpServletRequest request) throws ActException {
        try {
            String path = getParameter(request, "path", true);
            return new ActResult(storageSer.tree(path));
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 文件上传（支持大文件） 大文件没有关联id(relevanceId)，照样会保存数据库,以提供MD5
     *
     * @param info 上传文件信息
     * @return
     */
    @PostMapping("upload")
    public ActResult upload(HttpServletRequest request, FileInfo info) throws ActException {
        try {
            initFileInfo(info, request); // 初始化文件上传信息
            if (info.getChunks() != null && info.getChunk() != null) { // 大文件分片上传
                storageSer.savePartFile(info); // 将文件分块保存到临时文件夹里，便于之后的合并文件
                storageSer.bigUploaded(info); // 完整的上传
                return new ActResult(SUCCESS);
            } else { // 普通小文件上传
                try {
                    storageSer.upload(FileUtil.getMultipartFile(request), info.getPath());
                    return new ActResult(SUCCESS);
                } catch (Exception e) {
                    throw new ActException(e.getMessage());
                }
            }
        } catch (SerException e) {
            ResponseUtil.writeData(new ActResult(e.getMessage()));
        }
        return new ActResult(SUCCESS);
    }

    /**
     * 检测文件是否存在
     */
    @GetMapping("exists")
    public ActResult exist(HttpServletRequest request) throws ActException {
        try {
            String path = getParameter(request, "path", true);
            return new ActResult(storageSer.existsFile(path));
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 检测MD5是否存在，实现秒传
     */
    @GetMapping("md5/exist")
    public ActResult md5Exist(HttpServletRequest request) throws ActException {
        try {
            String fileMd5 = getParameter(request, "fileMd5", true);
            String toPath = getParameter(request, "toPath", true);
            String fileName = getParameter(request, "fileName", true);
            return new ActResult(storageSer.md5Exist(fileMd5, toPath, fileName, true));
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 文件下载（支持大文件下载）
     * <p>
     * 文件路径
     *
     * @param isFolder 是否为文件夹，默认为文件(下载文件夹设置为true)
     * @return
     */
    @GetMapping("download")
    public void downLoad(HttpServletRequest request, HttpServletResponse response, boolean isFolder) throws ActException {
        try {
            String path = getParameter(request, "path", true);
            File file = storageSer.download(path, isFolder);
            if (file.exists()) {
                FileUtil.writeOutFile(response, file);
                if (isFolder) {
                    file.delete(); // 删除压缩文件
                }
            }

        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }


    /**
     * 创建文件夹
     * <p>
     * 文件路径
     * 文件夹名
     *
     * @return
     */
    @PostMapping("mkdir")
    public ActResult mkdir(HttpServletRequest request) throws ActException {
        try {
            String path = getParameter(request, "path", true);
            String dir = getParameter(request, "dir", true);
            storageSer.mkDir(path, dir);
            return new ActResult(SUCCESS);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 重命名文件或文件夹
     * <p>
     * 文件路径
     * 新的文件名
     *
     * @return
     */
    @PostMapping("rename")
    public ActResult rename(HttpServletRequest request) throws ActException {
        try {
            String path = getParameter(request, "path", true);
            String newName = getParameter(request, "newName", true);
            String oldName = getParameter(request, "oldName", true);
            storageSer.rename(path, oldName, newName);
            return new ActResult(SUCCESS);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 移动文件至文件夹
     * <p>
     * 文件路径
     * 移动到的文件路径
     *
     * @return
     */
    @PostMapping("move")
    public ActResult move(HttpServletRequest request) throws ActException {
        try {
            String from = getParameter(request, "fromPath", true);
            String toPath = getParameter(request, "toPath", true);
            String[] fromPath = from.split("##");
            storageSer.move(fromPath, toPath);
            return new ActResult(SUCCESS);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 删除文件
     * <p>
     * 多个文件路径
     *
     * @return
     * @throws SerException
     */
    @PostMapping("delfile")
    public ActResult delFile(HttpServletRequest request) throws ActException {
        try {
            String values = getParameter(request, "paths", true);
            String[] paths = values.split("##");
            if (null != paths) {
                storageSer.delFile(paths);
                return new ActResult(SUCCESS);
            } else {
                throw new SerException("paths不能为空");
            }
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 复制到
     * <p>
     * 文件路径
     * 文件夹名
     *
     * @return
     */
    @PostMapping("copy")
    public ActResult copy(HttpServletRequest request) throws ActException {
        try {
            String from = getParameter(request, "fromPath", true);
            String toPath = getParameter(request, "toPath", true);
            String[] fromPaths = from.split("##");
            storageSer.copy(fromPaths, toPath);
            return new ActResult(SUCCESS);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 获取缩略图
     * <p>
     * 文件路径
     *
     * @param response
     * @throws SerException
     */
    @GetMapping("thumbnails")
    public void thumbnails(HttpServletRequest request, HttpServletResponse response) throws ActException {
        try {
            String path = getParameter(request, "path", true);
            String width = getParameter(request, "width", false);
            String height = getParameter(request, "height", false);
            writeImage(response, storageSer.thumbnails(path, width, height));
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        } catch (IOException e) {
            throw new ActException("获取缩略图错误！");
        }
    }

    /**
     * 获取gif
     * <p>
     * 文件路径
     *
     * @param response
     * @throws SerException
     */
    @GetMapping(value = "gif")
    public void gif(HttpServletRequest request, HttpServletResponse response) throws ActException {
        try {
            String path = getParameter(request, "path", true);
            FileUtil.writeOutFile(response, storageSer.download(path, false));
        } catch (SerException e) {
            throw new ActException("获取缩略图错误！");
        }
    }

    /**
     * 通过name获取参数
     *
     * @param request
     * @return
     * @throws SerException
     */
    private String getParameter(HttpServletRequest request, String name, boolean notNull) throws SerException {
        String parameter = request.getParameter(name);
        if (StringUtils.isNotBlank(parameter)) {
            if (StringUtils.isNumeric(parameter)) {
                return parameter;
            }
            parameter = ASEUtil.decrypt(parameter);
            return parameter;
        } else {
            if (notNull) {
                throw new SerException(name + "不能为空！");
            }
            return null;
        }
    }

    /**
     * 输出图片
     *
     * @param response
     * @param bytes
     * @throws IOException
     */
    private void writeImage(HttpServletResponse response, byte[] bytes) throws IOException {
        response.reset();
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        ByteArrayInputStream in = new ByteArrayInputStream(bytes); // 将b作为输入流；
        BufferedImage image = ImageIO.read(in);
        ImageIO.scanForPlugins();
        ImageIO.write(image, "jpg", response.getOutputStream());

    }


    /**
     * 初始化上传文件信息
     *
     * @param info    文件信息
     * @param request
     */
    private void initFileInfo(FileInfo info, HttpServletRequest request) throws SerException {
        String value = request.getParameter("relevanceId");
        Long relevanceId = null;
        if (null != value) {
            relevanceId = Long.parseLong(value);
        }
        int ext_index = info.getName().lastIndexOf(".");
        String ext = null;
        if (ext_index != -1) {
            ext = info.getName().substring(ext_index);// 后缀
        } else {
            ext = null == ext ? "" : ext;
        }
        info.setExt(ext);
        if (null != info.getChunk()) {
            int index = Integer.parseInt(info.getChunk()); // 文件分片序号
            String partName = String.valueOf(index) + ext; // 分片文件保存名
            info.setPartName(partName);
        }
        info.setRelevanceId(relevanceId);
        String path = ASEUtil.decrypt(info.getPath());
        info.setPath(path);
    }
}
