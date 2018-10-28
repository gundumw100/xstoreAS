package com.base.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

public class IOUtil {

	/**
	 * 将异常写入SDCard
     * 追加文件：使用FileWriter
     * 
     * @param fileName
     * @param content
     */
    public static void writeToSDCard(String fileName, String content) {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
        	File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            String filePath=sdDir+File.separator+fileName;
            try {
            	// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            	FileWriter writer = new FileWriter(filePath, true);
            	writer.write(content);
            	writer.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
        
    }
    
}
