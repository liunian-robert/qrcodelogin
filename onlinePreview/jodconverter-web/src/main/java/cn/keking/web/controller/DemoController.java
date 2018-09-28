package cn.keking.web.controller;

import cn.keking.model.ReturnResponse;
import cn.keking.utils.FileUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author yudian-it
 * @date 2017/12/1
 */
@RestController
public class DemoController {
    @Value("${file.dir}")
    String fileDir;
    @Autowired
    FileUtils fileUtils;
    String demoDir = "demo";
    String demoPath = demoDir + File.separator;
    @GetMapping(value = "demo/{downloadName:.+}")
    public void download(HttpServletResponse resp, @PathVariable("downloadName") String downloadName) throws JsonProcessingException {
        this.download(resp,downloadName,downloadName);
    }

    public void download(HttpServletResponse resp, String name, String downloadName) {
        String fileName = null;
        try {
            fileName = new String(downloadName.getBytes("GBK"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String path = fileDir + demoPath + name;
        File file = new File(path);
        resp.reset();
        resp.setContentType("application/octet-stream");
        resp.setCharacterEncoding("utf-8");
        resp.setContentLength((int) file.length());
        resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = resp.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
