package com.zyh.upload;

import com.qiniu.http.Response;
import com.zyh.entity.Msg;
import com.zyh.sysLog.Logweb;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传的相关接口
 */
@RestController
@RequestMapping("/qiniu")
public class QiniuController {

    @Autowired
    private QiniuService qiniuService;

    /**
     * 以流的形式上传图片
     *
     * @param file
     * @return 返回访问路径
     * @throws IOException
     */
    @PostMapping("/upload")
    public Msg uploadFile(@RequestParam(value = "file") MultipartFile file) throws IOException {
        String s = qiniuService.uploadFile(file.getInputStream());
        return Msg.success().add("key",s);
    }

    @PostMapping("/upAvatar")
    public Msg upAvatar(@RequestParam(value = "file") MultipartFile file)throws IOException{
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if(suffix.equals("JPG")||suffix.equals("jpg")){
            String s = qiniuService.upAvatar(file.getInputStream());
            return Msg.success().add("fileName",s);
        }
        return Msg.fail("只支持jpg格式的文件");
    }


    /**
     * 删除文件
     *
     * @param key
     * @return
     * @throws IOException
     */
    @GetMapping("delete/{key}")
    public Response deleteFile(@PathVariable String key) throws IOException {
        return qiniuService.delete(key);
    }
}