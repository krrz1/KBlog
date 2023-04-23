package com.krrz.service.impl;

import com.google.gson.Gson;
import com.krrz.domain.ResponseResult;
import com.krrz.enums.AppHttpCodeEnum;
import com.krrz.exception.SystemException;
import com.krrz.service.UploadService;
import com.krrz.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Data
@ConfigurationProperties(prefix = "oss")
@Service
public class OssUploadService implements UploadService {
    private String accessKey;
    private String secretKey;
    private String bucket;


    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //判断文件类型 或者 大小
        //获取原始文件名
        String filename = img.getOriginalFilename();
        //对文件名进行判断
        if(!filename.endsWith(".jpg") ){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        //如果判断通过 上传文件道oss
        String filePath= PathUtils.generateFilePath(filename);
        String url=uploadOSS(img,filePath);

        return ResponseResult.okResult(url);
    }


    private String uploadOSS(MultipartFile imgFile,String filePath){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huadong());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        String key =filePath;
        try {
            InputStream is=imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(is,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return  "http://cdn.krrz.club/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "www";
    }
}
