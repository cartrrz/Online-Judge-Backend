package com.spoky.patito.gateway.util;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Service
public class FileUtil {

    public String getContentFile(String filePath){
        String content = "";
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            content = IOUtils.toString(inputStream);
            inputStream.close();
        } catch (Exception ex) {
            // Do nothing
        }
        return content;
    }
    public boolean createFileWithContent(String filePath, String content){
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(filePath));
            IOUtils.write(content, outputStream);
            IOUtils.closeQuietly(outputStream);
            return true;
        }catch (Exception e){
            //logger
        }
        return false;
    }

}
