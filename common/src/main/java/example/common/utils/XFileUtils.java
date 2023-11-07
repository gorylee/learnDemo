package example.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/11/7
 */
public class XFileUtils {

    /**
     * 从网络Url中下载文件
     * @param urlStr url的路径
     * @throws IOException
     */
    public static String  downLoadByUrl(String urlStr,String savePath, String fileName) {
        if (StrUtil.isBlank(fileName)) {
            fileName = getFileName(urlStr);
        }
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(urlStr);
            // 防止https被证书校验报错，信任所有证书
//            if(urlStr.contains("https")){
//                SslUtils.ignoreSsl();
//            }
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //得到输入流
            inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);
            //文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) { // 没有就创建该文件
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName);
            fos = new FileOutputStream(file);
            fos.write(getData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(fos);
            IoUtil.close(inputStream);
        }
        return fileName;
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[4*1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 从src文件路径获取文件名
     * @param srcRealPath src文件路径
     * @return 文件名
     */
    private static String getFileName(String srcRealPath){
        return StringUtils.substringAfterLast(srcRealPath,"/");
    }

}
