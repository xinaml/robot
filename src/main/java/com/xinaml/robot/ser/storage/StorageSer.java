package com.xinaml.robot.ser.storage;

import com.xinaml.robot.base.ser.Ser;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.dto.storage.StorageDTO;
import com.xinaml.robot.entity.storage.Storage;
import com.xinaml.robot.to.storage.FileInfo;
import com.xinaml.robot.vo.storage.FileVO;
import com.xinaml.robot.vo.storage.TreeVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface StorageSer extends Ser<Storage,StorageDTO> {
    /**
     * 文件列表
     * @param path 路径
     * @return
     * @throws SerException
     */
    List<FileVO> list(String path) throws SerException;

    /**
     * 文件夹树
     * @param path 路径
     * @return
     * @throws SerException
     */
    List<TreeVO> tree(String path) throws SerException;

    /**
     * 分片文件保存
     * @param fileInfo 文件信息
     * @return
     * @throws SerException
     */
    Boolean savePartFile(FileInfo fileInfo) throws SerException;

    /**
     * 大文件上传（合并分片文件）
     * @param fileInfo 文件信息
     * @throws SerException
     */
    void bigUploaded(FileInfo fileInfo) throws SerException;

    /**
     * 小文件上传
     * @param multipartFiles 文件列表
     * @param path 路径
     * @return
     * @throws SerException
     */
    List<FileVO> upload(List<MultipartFile> multipartFiles, String path) throws SerException;

    /**
     * 文件是否存在
     * @param path 路径
     * @return
     * @throws SerException
     */
    Boolean existsFile(String path) throws SerException;

    /**
     * md5 是否存在
     * @param md5
     * @param path 路径
     * @param fileName 文件名
     * @param save 是否校验并秒传
     * @return
     * @throws SerException
     */
    Boolean md5Exist(String md5, String path, String fileName,boolean save) throws SerException;

    /**
     * 文件下载
     * @param path 路径
     * @param isFolder 是否为文件夹
     * @return
     * @throws SerException
     */
    File download(String path, boolean isFolder) throws SerException;

    /**
     * 创建目录
     * @param path 路径
     * @param dir 目录名
     * @throws SerException
     */
    void mkDir(String path, String dir) throws SerException;

    /**
     * 重命名文件或文件夹
     * @param path 路径
     * @param oldName 旧文件名
     * @param newName 新文件名
     * @return
     * @throws SerException
     */
    File rename(String path, String oldName, String newName) throws SerException;

    /**
     * 移动文件或文件夹
     * @param fromPaths 要移动的文件路径
     * @param toPath 移动到的文件路径
     * @return
     * @throws SerException
     */
    Boolean move(String[] fromPaths, String toPath) throws SerException;

    /**
     * 删除文件
     * @param paths 路径
     * @throws SerException
     */
    void delFile(String[] paths) throws SerException;

    /**
     * 复制文件
     * @param fromPaths 要复制的文件路径
     * @param toPath 复制到的文件路径
     * @return
     * @throws SerException
     */
    Boolean copy(String[] fromPaths, String toPath) throws SerException;

    /**
     * 缩略图
     * @param path 路径
     * @param width 宽
     * @param height 高
     * @return
     * @throws SerException
     */
    byte[] thumbnails(String path, String width, String height) throws SerException;
}
