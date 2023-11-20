package com.yhk.reggie.controller;

import com.yhk.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/6 11:56
 * @注释 文件上传和下载
 */
@RequestMapping("/common")
@RestController
@Slf4j
public class CommonController {

    @Value("${reggie.path}")//自动装配yml文件中的自定义参数
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){//file必须与前端请求中的name参数名一致
        //file是一个临时文件，需要转存到指定位置，否则本次请求后临时文件会删除
        log.info(file.toString());
        //获取原先文件名
        String originalFilename = file.getOriginalFilename();
        //截取原先文件名后面的文件后缀
        String suffix=originalFilename.substring(originalFilename.lastIndexOf("."));//.jpg,.png
        //使用UUID重新生成文件名，防止文件名重复进行覆盖
        String fileName= UUID.randomUUID().toString()+suffix;//不能直接在后面加.jpg因为图片形式也有可能是png
        //创建文件目录
        File dir = new File(basePath);
        //判断文件目录是否存在
        if(!dir.exists()){
            //没有对应目录就创建
            dir.mkdirs();
        }
        try {
            //将文件转存到指定位置
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.success(fileName);//将文件名返回给前端，方便后期开发
    }

    /**
     * 文件下载
     * @param name
     * @param response
     * @return
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //文件输入流读取文件
        FileInputStream fileInputStream=null;
        //浏览器文件输出流
        ServletOutputStream servletOutputStream=null;
        try {
            //通过输入流读取文件内容
            fileInputStream=new FileInputStream(new File(basePath+name));//读取对应磁盘下的文件
            //通过输出流将文件写回浏览器,在浏览器展示图片
            servletOutputStream=response.getOutputStream();
            //设置响应的格式为图片
            response.setContentType("image/jpeg");
            //字节数组的方式读取文件流
            int len=0;
            byte[] bytes = new byte[1024];
            while((len=fileInputStream.read(bytes))!=-1){
                servletOutputStream.write(bytes, 0, len);
                //刷新
                servletOutputStream.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            //关闭资源
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(servletOutputStream!=null){
                try {
                    servletOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
