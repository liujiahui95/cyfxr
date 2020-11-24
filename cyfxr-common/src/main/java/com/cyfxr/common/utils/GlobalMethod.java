package com.cyfxr.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GlobalMethod {

    /**
     * 返回文件内存大小
     *
     * @param long fils
     */
    public static String formatFileSize(long fils) {
        DecimalFormat df = new DecimalFormat("#");
        String filstr = "";
        if (fils < 1024) {
            filstr = df.format(fils) + "B";
        } else if (fils < 1024 * 1024) {
            filstr = df.format(fils / 1024) + "KB";
        } else if (fils < 1073741824) {
            filstr = df.format(fils / 1048576) + "M";
        } else {
            filstr = df.format(fils / 1073741824) + "G";
        }
        return filstr;
    }


    /**
     * 判断文件编码格式
     *
     * @param File file
     */
    public static String get_charset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            ;
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                // int len = 0;
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }

            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return charset;
    }

    /**
     * 递归遍历,删除文件夹内的文件
     *
     * @param Stirng path="F:\\Server_U"
     *               *
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        File flist[] = file.listFiles();
        if (flist == null || flist.length == 0) {
            return;
        }
        for (File f : flist) {
            if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            } else {
                f.delete();
            }
        }
    }

    /**
     * 递归遍历,文件夹下面所有文件的绝对路径
     *
     * @param Stirng       path="F:\\Server_U"
     * @param List<String> fs
     */
    public static List<String> getFiles(String path, List<String> fs) {
        File file = new File(path);
        File flist[] = file.listFiles();
        if (flist == null || flist.length == 0) {
            return null;
        }
        for (File f : flist) {
            if (f.isDirectory()) {
                getFiles(f.getAbsolutePath(), fs);
            } else {
                File filetem = new File(f.getAbsolutePath());
                if (filetem.length() > 0) {
                    fs.add(f.getAbsolutePath());
                }
            }
        }
        return fs;
    }

    /**
     * 判断字符串的编码
     *
     * @param str
     * @return
     */
    public static String getEncoding(String str) {
        String encode[] = new String[]{
                "UTF-8",
                "ISO-8859-1",
                "GB2312",
                "GBK",
                "GB18030",
                "Big5",
                "Unicode",
                "ASCII"
        };
        for (int i = 0; i < encode.length; i++) {
            try {
                if (str.equals(new String(str.getBytes(encode[i]), encode[i]))) {
                    return encode[i];
                }
            } catch (Exception ex) {
            }
        }
        return "";
    }

    /**
     * 四舍五入,向上入
     *
     * @param data double
     * @param int  保留小数位数
     */
    public static double round(double data, int n) {
        BigDecimal b = new BigDecimal(Double.toString(data));
        data = b.setScale(n, RoundingMode.HALF_UP).doubleValue();
        return data;

    }

    /**
     * 复制文件
     *
     * @param s        源文件
     * @param t复制到的新文件
     */

    public static void fileChannelCopy(File s, File t) {
        try {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(s), 1024);
                out = new BufferedOutputStream(new FileOutputStream(t), 1024);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        round(171.12389, 3);
        List<String> fs = new ArrayList<String>();
        String path = "D:\\logs\\";
        getFiles(path, fs);
        String newpath = "D:\\test\\";
        for (String str : fs) {
            File file = new File(str);
            System.out.println(file.getParent());
            String s = newpath + File.separator + file.getParent().substring(3);
            File newfile = new File(s + File.separator);
            if (!newfile.exists() && !newfile.isDirectory()) {
                newfile.mkdirs(); //创建多个，目录
            }
            File newfile2 = new File(s + File.separator + file.getName());
            fileChannelCopy(file, newfile2);
        }
    }
}
