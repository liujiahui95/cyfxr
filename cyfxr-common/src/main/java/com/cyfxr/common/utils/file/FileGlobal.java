package com.cyfxr.common.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class FileGlobal {

    /**
     * 递归遍历,获取文件夹里面的所有文件
     *
     * @param File         file
     * @param List<String> fs
     * @return List<String>
     */
    public static List<String> getFiles(File file, List<String> fs) {
        File flist[] = file.listFiles();
        if (flist == null || flist.length == 0) {
            return null;
        }
        for (File f : flist) {
            if (f.isDirectory()) {
                getFiles(f, fs);
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
     * 生成文件
     *
     * @param String data
     * @param String path
     * @param String fileName
     */
    public static void writerFile(String data, String path, String fileName) {
        File file = new File(path);
        //判断目录是否存在
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs(); //创建多个，目录
        }
        file = new File(path + File.separator + fileName);
        FileOutputStream outStream = null;
        OutputStreamWriter outStreamWriter = null;
        BufferedWriter output = null;
        try {
            outStream = new FileOutputStream(file, true);//文件追加写入
            //outStream = new FileOutputStream(file);
            outStreamWriter = new OutputStreamWriter(outStream, "gbk");
            output = new BufferedWriter(outStreamWriter);
            output.write(data);
            output.flush();
            outStream.close();
            outStreamWriter.close();
            output.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     *
     * @param String filePath
     * @return List<String> list
     * @author lqp
     */
    public static List<String> readFileByLine(String filePath) {
        List<String> list = new ArrayList<String>();
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行(以回车换行为标志)：");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader ins = new InputStreamReader(fis, "gbk");
            //reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),"gbk"));
            reader = new BufferedReader(ins, 10 * 1024 * 1024);//缓存
            String lineStr = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((lineStr = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + lineStr);
                if (lineStr != null && !lineStr.equals("")) {
                    list.add(lineStr);
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return list;
    }

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public void readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            System.out.println("以字节为单位读取文件内容，一次读一个字节：");
            // 一次读一个字节
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(fileName);
            FileGlobal.showAvailableBytes(in);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 显示输入流中还剩的字节数
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("当前字节输入流中的字节数为:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     */
    public void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            System.out.println("以字符为单位读取文件内容，一次读一个字节：");
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file), "gb2312");
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("以字符为单位读取文件内容，一次读多个字节：");
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName), "gb2312");
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 获取文件内容总行数
     * 速度快
     */
    public static int getLineCount(String filePath) {
        long a = System.currentTimeMillis();
        int count = 0;
        File file = new File(filePath);
        try {
            String line = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"), 10 * 1024 * 1024);
            while ((line = br.readLine()) != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("用时：" + (System.currentTimeMillis() - a) + "ms" + ",共计:" + count);
        return count;
    }

    /**
     * 获取文件内容总行数
     * 速度慢
     */
    public static int getLineCount1(String filePath) {
        long a = System.currentTimeMillis();
        int count = 0;
        File file = new File(filePath);
        long filelength = file.length();
        try {
            LineNumberReader linereader = new LineNumberReader(new FileReader(file));
            linereader.skip(filelength);
            count = linereader.getLineNumber();
            linereader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("用时：" + (System.currentTimeMillis() - a) + "ms" + ",共计:" + count);
        return count;
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
        //new FileUtil().writerFile("adasd", "d://test", "test.dat");
        //FileUtil.readFileByLine("D://Delphi//收入审核系统//kzsh//客杂审核修改文件//客里程//191009.hdx");
        String filepath = "D:\\srfx\\b538002.txt";
        FileGlobal.getLineCount(filepath);
        FileGlobal.getLineCount1(filepath);
    }
}
