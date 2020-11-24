package com.cyfxr.common.utils.poi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.cyfxr.common.utils.StringUtil;
import com.cyfxr.common.utils.file.FileGlobal;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelUtils {

    /**
     * 2020年5月15日15:02:23
     * 根据模板生成Excel
     *
     * @param tempPath                        模板文件路径
     * @param path                            文件路径
     * @param list                            数据集合
     * @param rownum                          正式数据从第几行开始写入，行号从0开始
     * @param colnum                          正式数据从第几列开始写入，列从0开始
     * @param filename                        指定生成的文件名
     * @param strs，存储日期\站名等一些查询条件，写入excel表第二行
     */
    public void createExcel(String tempPath, String path, List<Object[]> list,
                            int rownum, int colnum, String filename, String strs) {
        File newFile = createFileBymb(tempPath, path, filename);
        InputStream is = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        try {
            is = new FileInputStream(newFile);// 将excel文件转为输入流
            workbook = new XSSFWorkbook(is);// 创建个workbook，
            // 获取第一个sheet
            sheet = workbook.getSheetAt(0);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (sheet != null) {
            try {
                // 写入数据
                FileOutputStream fos = new FileOutputStream(newFile);
                XSSFRow row = sheet.getRow(1);//excel第二行，描述信息所在行，行号默认从0开始
                if (row == null) {
                    row = sheet.createRow(1);
                }
                XSSFCell cell = row.getCell(0);
                if (cell == null) {
                    cell = row.createCell(0);
                }
                cell.setCellValue(strs);

                for (int i = 0; i < list.size(); i++) {
                    //行号、列号默认从0开始，0对应第一行
                    //强制使用excel里面的公式
                    sheet.setForceFormulaRecalculation(true);
                    row = sheet.getRow(i + rownum - 1);
                    Object[] objs = list.get(i);
                    for (int j = colnum - 1; j < objs.length; j++) {
                        cell = row.getCell(j);
                        String data = StringUtil.NullToStirng(objs[j]).toString();
                        String flag = StringUtil.checkDataType(data);
                        if (flag.equals("int")) {
                            cell.setCellValue(Integer.parseInt(data));
                        } else if (flag.equals("double")) {
                            cell.setCellValue(Double.parseDouble(data));
                        } else {
                            cell.setCellValue(data);
                        }
                    }
                }
                workbook.write(fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据excel模板，
     * 并复制到新文件中供写入
     *
     * @param tempPath 模板路径
     * @param rpath    生成文件路径
     * @return
     */
    public File createFileBymb(String tempPath, String rPath, String filename) {
        // 读取模板，并赋值到新文件
        File file = new File(tempPath);
        String newFileName = filename + ".xlsx";
        // 判断路径是否存在
        File dir = new File(rPath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        // 写入到新的excel
        File newFile = new File(rPath, newFileName);
        try {
            newFile.createNewFile();
            // 复制模板到新文件
            FileGlobal.fileChannelCopy(file, newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile;
    }

    /**
     * 2020年5月15日15:02:23
     * 根据模板生成Excel
     *
     * @param tempPath                        模板文件路径
     * @param path                            文件路径
     * @param list                            数据集合
     * @param rownum                          正 式数据从第几行开始写入，行号从0开始
     * @param colnum                          正式数据从第几列开始写入，列从0开始
     * @param filename                        指定生成的文件名
     * @param strs，存储日期\站名等一些查询条件，写入excel表第二行
     */
    public void exportExcel(String tempPath, String path, List<Object[]> list,
                            int rownum, int colnum, String filename, String strs, HttpServletResponse response) {
        File newFile = createNewFile(tempPath, path);
        InputStream is = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        try {
            is = new FileInputStream(newFile);// 将excel文件转为输入流
            workbook = new XSSFWorkbook(is);// 创建个workbook，
            // 获取第一个sheet
            sheet = workbook.getSheetAt(0);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (sheet != null) {
            try {
                // 写数据
                FileOutputStream fos = new FileOutputStream(newFile);
                XSSFRow row = sheet.getRow(1);
                if (row == null) {
                    row = sheet.createRow(0);
                }
                XSSFCell cell = row.getCell(0);
                if (cell == null) {
                    cell = row.createCell(0);
                }
                cell.setCellValue(strs);


                for (int i = 0; i < list.size(); i++) {
                    //行号、列号默认从0开始，0对应第一行
                    //强制使用excel里面的公式
                    sheet.setForceFormulaRecalculation(true);
                    //row = sheet.createRow(i+rownum-1); //参数从0开始，0对应第一行
                    row = sheet.getRow(i + rownum - 1);
                    Object[] objs = list.get(i);
                    for (int j = colnum - 1; j < objs.length; j++) {
                        cell = row.getCell(j);
                        String data = StringUtil.NullToStirng(objs[j]).toString();
                        String flag = StringUtil.checkDataType(data);
                        if (flag.equals("int")) {
                            cell.setCellValue(Integer.parseInt(data));
                        } else if (flag.equals("double")) {
                            cell.setCellValue(Double.parseDouble(data));
                        } else {
                            cell.setCellValue(data);
                        }
                    }
                }
                workbook.write(fos);
                fos.flush();
                fos.close();

                // 下载
                InputStream fis = new BufferedInputStream(new FileInputStream(newFile));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                response.reset();
                response.setContentType("text/html;charset=UTF-8");
                OutputStream toClient = new BufferedOutputStream(
                        response.getOutputStream());
                response.setContentType("application/x-msdownload");
                String newName = URLEncoder.encode(tempPath.substring(tempPath.lastIndexOf("/") + 1, tempPath.lastIndexOf("."))
                                + System.currentTimeMillis() + ".xlsx",
                        "UTF-8");
                response.addHeader("Content-Disposition",
                        "attachment;filename=\"" + newName + "\"");
                response.addHeader("Content-Length", "" + newFile.length());
                toClient.write(buffer);
                toClient.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // 删除创建的新文件
        this.deleteFile(newFile);
    }

    /**
     * 根据当前row行，来创建index标记的列数,并赋值数据
     */
    public void createRowAndCell(Object obj, XSSFRow row, XSSFCell cell, int index) {
        cell = row.getCell(index);
        if (cell == null) {
            cell = row.createCell(index);
        }

        if (obj != null)
            cell.setCellValue(obj.toString());
        else
            cell.setCellValue("");
    }

    /**
     * 复制文件
     *
     * @param s        源文件
     * @param t复制到的新文件
     */

    public void fileChannelCopy(File s, File t) {
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


    /**
     * 读取excel模板，并复制到新文件中供写入和下载
     *
     * @return
     */
    public File createNewFile(String tempPath, String rPath) {
        // 读取模板，并赋值到新文件
        // 文件模板路径
        String path = (tempPath);
        File file = new File(path);
        // 保存文件的路径
        String realPath = rPath;
        // 新的文件名
        String newFileName = System.currentTimeMillis() + ".xlsx";
        // 判断路径是否存在
        File dir = new File(realPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 写入到新的excel
        File newFile = new File(realPath, newFileName);
        try {
            newFile.createNewFile();
            // 复制模板到新文件
            fileChannelCopy(file, newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile;
    }

    /**
     * 下载成功后删除
     *
     * @param files
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static void main(String[] args) {
        new ExcelUtils().createNewFile("d://承运效益评价表样20181017.xlsx", "d:");
    }
}