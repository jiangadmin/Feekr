package com.tl.film.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by jiangmac
 * on 15/12/23.
 * Email: www.fangmu@qq.com
 * Phone：186 6120 1018
 * Purpose:HTTP工具类
 * update：细分发送方式 全部使用despost
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";

    private static final int TIMEOUT_IN_MILLIONS = 15 * 1000;

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DEL = "DELETE";

    /**
     * 网络请求
     *
     * @param type 请求类型
     * @param url  请求地址
     * @param o    请求数据
     * @return
     */

    public static String request(String type, String url, Object o) {

        //定义stringbuffer  方便后面读取网页返回字节流信息时的字符串拼接
        StringBuffer stringBuffer = new StringBuffer();

        //创建url_connection
        URLConnection http_url_connection;
        HttpURLConnection urlConn = null;
        try {

            //get 请求需要的请求头
            switch (type) {
                case GET:
                    StringBuffer params = new StringBuffer();
                    if (o != null && o instanceof Map) {
                        for (Map.Entry<String, String> entry : ((Map<String, String>) o).entrySet()) {
                            if (TextUtils.isEmpty(entry.getValue())) {
                                continue;
                            }
                            params.append(entry.getKey() + "=" + entry.getValue() + "&");
                        }
                        url += "?" + params.toString();
                    }
                    http_url_connection = (new URL(url)).openConnection();
                    urlConn = (HttpURLConnection) http_url_connection;
                    LogUtil.e(TAG, type + "请求");
                    urlConn.setRequestMethod("GET");
                    urlConn.setRequestProperty("accept", "*/*");
                    urlConn.setRequestProperty("connection", "Keep-Alive");
                    urlConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    break;
                case POST:
                    http_url_connection = (new URL(url)).openConnection();
                    urlConn = (HttpURLConnection) http_url_connection;
                    LogUtil.e(TAG, type + "请求");

                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);

                    // 设置通用的请求属性
                    urlConn.setRequestMethod("POST");
                    urlConn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
                    urlConn.setRequestProperty("accept", "*/*");
                    urlConn.setRequestProperty("Server", "Apache-Coyote/1.1");
//                    urlConn.setRequestProperty("connection", "Keep-Alive");
//                    urlConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    break;
                case PUT:
                    http_url_connection = (new URL(url)).openConnection();
                    urlConn = (HttpURLConnection) http_url_connection;
                    LogUtil.e(TAG, type + "请求");
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setRequestMethod("PUT");
                    urlConn.setRequestProperty("Accept", "*/*");
                    urlConn.setRequestProperty("Connection", "keep-alive");
                    urlConn.setRequestProperty("Proxy-Connection", "keep-alive");
                    urlConn.setRequestProperty("Charset", "UTF-8");
                    break;
                case DEL:
                    http_url_connection = (new URL(url)).openConnection();
                    urlConn = (HttpURLConnection) http_url_connection;
                    LogUtil.e(TAG, type + "请求");

                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);

                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestProperty("Accept", "application/json");
                    urlConn.setRequestProperty("Charset", "UTF-8");
                    break;
            }

            //超时时间
            urlConn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            urlConn.setConnectTimeout(TIMEOUT_IN_MILLIONS);

            urlConn.setRequestMethod(type);//设置请求方式。可以是delete put post get
            urlConn.setUseCaches(false);


            if (o instanceof Map) {

                StringBuilder builder = new StringBuilder();
                for (Map.Entry<String, Object> para : ((Map<String, Object>) o).entrySet()) {
                    if (para.getValue() == null || para.getValue().equals("")) {
                        continue;
                    }
                    try {
                        builder.append(para.getKey()).append("=").append(URLEncoder.encode(String.valueOf(para.getValue()), "UTF-8")).append("&");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }


                LogUtil.e(TAG, "HTTP内容:" + builder.toString());
                if (type == GET) {

                } else {
                    // 获取URLConnection对象对应的输出流
                    PrintWriter out = new PrintWriter(urlConn.getOutputStream());
                    out.print(builder);
                    // flush输出流的缓冲
                    out.flush();

                }

            }

            InputStreamReader input_stream_reader;
            BufferedReader buffered_reader;

            LogUtil.e(TAG, "HTTP码：" + urlConn.getResponseCode());

            if (urlConn.getResponseCode() == 200) {

                if (type == GET) {
                    InputStream is;
                    ByteArrayOutputStream baos;

                    is = urlConn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    int len = -1;
                    byte[] buf = new byte[128];

                    while ((len = is.read(buf)) != -1) {
                        baos.write(buf, 0, len);
                    }
                    baos.flush();

                    return baos.toString();

                } else {
                    input_stream_reader = new InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8);
                    buffered_reader = new BufferedReader(input_stream_reader);
                    stringBuffer = new StringBuffer();
                    String line;
                    while ((line = buffered_reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    input_stream_reader.close();
                    buffered_reader.close();
                }
            } else {

                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String res = stringBuffer.toString();

        return res;
    }

    /**
     * @param uploadUrl      上传路径参数
     * @param uploadFilePath 文件路径
     * @category 上传文件至Server的方法
     * @author ylbf_dev
     */
    public static String uploadFile(String uploadUrl, String uploadFilePath) {

        //定义stringbuffer  方便后面读取网页返回字节流信息时的字符串拼接
        StringBuffer stringBuffer = new StringBuffer();

        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestMethod("POST");

            urlConn.setRequestProperty("Connection", "Keep-Alive");
            urlConn.setRequestProperty("Charset", "UTF-8");
            urlConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"test.jpg\"" + end);
//          dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
//                  + uploadFilePath.substring(uploadFilePath.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);
            // 文件通过输入流读到Java代码中-++++++++++++++++++++++++++++++`````````````````````````
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(uploadFilePath);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);

                }
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Exception e) {
                        LogUtil.e(TAG, e.getMessage());
                    }
                }
            }
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStreamReader input_stream_reader = new InputStreamReader(urlConn.getInputStream(), "utf-8");
            BufferedReader buffered_reader = new BufferedReader(input_stream_reader);
            stringBuffer = new StringBuffer();
            String line;
            while ((line = buffered_reader.readLine()) != null) {
                stringBuffer.append(line);
            }
            line = null;
            input_stream_reader.close();
            input_stream_reader = null;
            buffered_reader.close();
            buffered_reader = null;
            //  http_url_connection.disconnect();

            // 读取服务器返回结果
//            InputStream is = urlConn.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is, "utf-8");
//            BufferedReader br = new BufferedReader(isr);
//            String result = br.readLine();
            dos.close();
//            is.close();

            LogUtil.e(TAG, stringBuffer.toString());
            return stringBuffer.toString();

        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
