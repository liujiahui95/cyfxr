package com.cyfxr.common.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户字符串操作
 *
 * @author kcy
 */
public class StringUtil {

    /**
     * 判断是否是整数
     *
     * @param integer
     * @return
     */
    public static boolean isInteger(String integer) {
        Pattern p = Pattern.compile("\\d*");
        Matcher m = p.matcher(integer);
        boolean b = m.matches();
        return b;
    }

    /**
     * 根据字符串转换，如果为null，则变成0
     *
     * @param strName
     * @return
     */
    public static int getSafeInt(Object obj) {
        if (obj == null || "".equals(obj))
            return 0;
        else
            return Integer.parseInt(String.valueOf(obj));
    }

    /**
     * 判断是否是正整数
     *
     * @param integer
     * @return
     */
    public static boolean isInteger2(String integer) {
        Pattern p = Pattern.compile("^[0-9]*[1-9][0-9]*$");
        Matcher m = p.matcher(integer);
        boolean b = m.matches();
        return b;
    }

    public static String getAdm(String code) {
        if (code.equals("B"))
            return "哈尔滨铁路局";
        else if (code.equals("T"))
            return "沈阳铁路局";
        else if (code.equals("P"))
            return "北京铁路局";
        else if (code.equals("V"))
            return "太原铁路局";
        else if (code.equals("C"))
            return "呼和浩特铁路局";
        else if (code.equals("F"))
            return "郑州铁路局";
        else if (code.equals("N"))
            return "武汉铁路局";
        else if (code.equals("Y"))
            return "西安铁路局";
        else if (code.equals("K"))
            return "济南铁路局";
        else if (code.equals("H"))
            return "上海铁路局";
        else if (code.equals("G"))
            return "南昌铁路局";
        else if (code.equals("Q"))
            return "广铁集团";
        else if (code.equals("Z"))
            return "南宁铁路局";
        else if (code.equals("W"))
            return "成都铁路局";
        else if (code.equals("M"))
            return "昆明铁路局";
        else if (code.equals("J"))
            return "兰州铁路局";
        else if (code.equals("R"))
            return "乌鲁木齐铁路局";
        else if (code.equals("O"))
            return "青藏铁路公司";
        else if (code.equals(""))
            return "";
        else
            return "其他";

    }

    /**
     * 根据报表类型，报表日期取得要显示的报表日期
     *
     * @param repType
     * @param repDate
     * @return
     */
    public static String getOperaDate(String repType, String repDate) {
        if (repType.equals("D")) {
            return repDate.substring(0, 4) + "年" + repDate.substring(4, 6) + "月" + repDate.substring(6, 8) + "日";
        } else if (repType.equals("T")) {
            String xun = "";
            String day = repDate.substring(6, 8);
            if (Integer.parseInt(day) <= 10)
                xun = "上旬";
            else if (Integer.parseInt(day) <= 20)
                xun = "中旬";
            else if (Integer.parseInt(day) <= 31)
                xun = "下旬";
            return repDate.substring(0, 4) + "年" + repDate.substring(4, 6) + "月" + xun;
        } else if (repType.equals("M")) {
            return repDate.substring(0, 4) + "年" + repDate.substring(4, 6) + "月";
        } else if (repType.equals("Q")) {
            String month = repDate.substring(4, 6);
            String quarter = "";
            if (month.equals("01") || month.equals("02") || month.equals("03"))
                quarter = "一季度";
            else if (month.equals("04") || month.equals("05") || month.equals("06"))
                quarter = "二季度";
            else if (month.equals("07") || month.equals("08") || month.equals("09"))
                quarter = "三季度";
            else if (month.equals("10") || month.equals("11") || month.equals("12"))
                quarter = "四季度";
            return repDate.substring(0, 4) + "年" + quarter;
        } else if (repType.equals("Y")) {
            return repDate.substring(0, 4) + "年";
        } else
            return "";
    }

    /**
     * 处理报表类型在report_description中的字段
     *
     * @param kind
     * @return
     */
    public static String dealReportKind(String kind) {
        if (kind.equalsIgnoreCase("D"))
            return "isD=1";
        else if (kind.equalsIgnoreCase("T"))
            return "isT=1";
        else if (kind.equalsIgnoreCase("M"))
            return "isM=1";
        else
            return "";
    }

    /**
     * 根据字段序号取得数据表对应的字段名称
     *
     * @param id
     * @return
     */
    public static String dealFieldStr(int id) {
        if (id < 10)
            return "data_00" + id;
        else if (id < 100)
            return "data_0" + id;
        else
            return "data_" + id;
    }

    public static String dealFieldStr(String report_name, int id) {
        if (id < 10)
            return report_name + "_00" + id;
        else if (id < 100)
            return report_name + "_0" + id;
        else
            return report_name + "_" + id;
    }

    /**
     * 把前台传过来的含中文的url字符串转换成标准中文，
     * 比如%25E5%258C%2597%25E4%25BA%25AC转换成北京
     */
    public static String decodeUrl(String url) {
        String str = "";
        try {
            str = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 取字符除最后一位的子串，比如 aaa,bbb, 返回aaa,bbb
     *
     * @param str
     * @return
     */
    public static String subTract(String str) {
        if (str.length() == 0)
            return "";
        else
            return str.substring(0, str.length() - 1);
    }

    /**
     * 判断字符串是null或空
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.equals(""))
            return true;
        else
            return false;
    }

    /**
     * 把字符串里面的\r\n替换掉，json处理
     *
     * @param str
     * @return
     */
    public static String dealJsonFormat(String str) {
        str = str.replace("\r", "");
        str = str.replace("\n", "");
        return str;
    }

    /**
     * 把字符串里面的"-"和空格" "替换掉，并截取年月日成八位数日期字符串（18点日期格式），日期处理
     *
     * @param str
     * @return
     */
    public static String dealDateFormat(String str) {
        str = str.replace("-", "");
        str = str.replace(" ", "");
        str = str.substring(0, 8);
        return str;
    }

    public static boolean checkFileExist(String path) {
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            return true;
        } else
            return false;
    }

    /**
     * 判断字符串是null或空
     *
     * @param str
     * @return
     */
    public static boolean isNotNullOrEmpty(String str) {
        if (str != null && !str.equals(""))
            return true;
        else
            return false;
    }

    /**
     * 如果为null不trim
     *
     * @param str
     * @return
     */
    public static String isNullNoTrim(String str) {
        if (!(str == null)) {

            return str.trim();
        } else {
            return str;
        }
    }

    /**
     * 格式化小数
     *
     * @param val
     * @param point小数位
     * @return
     */
    public static String formatDouble(String val, int point) {
        String str = "";
        DecimalFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(point);
        str = nf.format(Double.parseDouble(val));
        return str.replace(",", "");
    }

    /**
     * 格式化小数
     *
     * @param val
     * @param point小数位
     * @return
     */
    public static double formatDouble(double val, int point) {
        String str = "";
        DecimalFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(point);
        str = nf.format(val);
        return Double.parseDouble(str.replace(",", ""));
    }

    /**
     * 格式化两位小数
     *
     * @param val
     * @param point
     * @return
     */
    public static String formatDouble(String val) {
        String str = "";
        DecimalFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);
        str = nf.format(Double.parseDouble(val));
        return str.replace(",", "");
    }

    /**
     * 格式化两位小数
     *
     * @param val
     * @param point
     * @return
     */
    public static double formatDouble(double val) {
        String str = "";
        DecimalFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);
        str = nf.format(val);
        return Double.parseDouble(str.replace(",", ""));
    }

    /**
     * 格式化金钱
     *
     * @param val
     * @param point
     * @return
     */
    public static String formatAmount(double val) {
        NumberFormat nf = new DecimalFormat("#,###.##");
        String str = nf.format(val);
        return str;
    }

    /**
     * 判断是否是ajax请求
     *
     * @param request
     * @return
     */
    public static boolean checkAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else
            return false;
    }

    /**
     * 把数字补零，比如传入T001，需要处理成T002，如果超出最大长度，返回"";
     *
     * @param 代码前缀
     * @param code已经存在的最大代码
     * @param 数字长度
     * @return
     */
    public static String addZero(String pre, String code, int numLength) {
        String str = "";
        if (StringUtil.isNullOrEmpty(code)) {
            str = pre;
            for (int i = 0; i < numLength - 1; i++) {
                str += "0";
            }
            str += "1";
        } else {
            str = pre;
            int num = Integer.parseInt(code.substring(pre.length(), code.length())) + 1;
            for (int i = 0; i < numLength - String.valueOf(num).length(); i++) {
                str += "0";
            }
            str += num;
        }
        if (str.length() > pre.length() + numLength)
            return "";
        else
            return str;
    }

    /**
     * 把输入框内的文字转换成html
     *
     * @param str
     * @return
     */
    public static String text2Html(String str) {
        if (str == null) {
            return "";
        } else if (str.length() == 0) {
            return "";
        }
        str = str.replaceAll("\n", "<br/>");
        str = str.replaceAll("\r", "<br/>");
        str = str.replaceAll(" ", "&nbsp");
        str = str.replaceAll("\t", "&nbsp&nbsp");
        return str;
    }

    /**
     * 转换提交的中文参数 ，只有当url中含有中文时才需要转换，ajax提交的param带中文不需要转换
     *
     * @param value
     * @return
     */
    public static String decodeParam(String value) {
        String str = "";
        if (value == null)
            return "";
        else {
            try {
                str = URLDecoder.decode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    /**
     * 数字补齐
     *
     * @param num
     * @param length 结果字符串总长度
     * @param pre    true-前补零 false-后补零
     * @return
     */
    public static String paddingZero(int num, int length, boolean pre) {
        String numStr = num + "";
        if (numStr.length() >= length)
            return numStr;

        if (pre) { // 前补零
            for (int i = 0, len = length - numStr.length(); i < len; i++) {
                numStr = "0" + numStr;
            }
        } else {
            for (int i = 0, len = length - numStr.length(); i < len; i++) {
                numStr = numStr + "0";
            }
        }

        return numStr;
    }

    /**
     * 同 paddingZero(int num, int length, boolean pre),默认前补零
     *
     * @param num
     * @param length
     * @return
     */
    public static String paddingZero(int num, int length) {
        return paddingZero(num, length, true);
    }


    /**
     * 过滤null 并返回string字符串
     *
     * @param str
     * @return
     */
    public static String NullToStirng(Object str) {
        if (str != null) {
            return str.toString();
        } else {
            return "";
        }

    }

    /**
     * 过滤null 并返回string字符串
     *
     * @param str
     * @return
     */
    public static Double NullToZero(Object str) {
        if (str != null && !str.toString().equals("")) {
            return Double.valueOf(str.toString());
        } else {
            return Double.valueOf(0);
        }

    }

    public static Integer NullToZero1(Object str) {
        if (str != null && !str.toString().equals("")) {
            return Integer.valueOf(str.toString());
        } else {
            return Integer.valueOf(0);
        }

    }

    /**
     * 把字符串里面的"-"和空格" "和冒号":"替换掉，
     * 并截取年月日成八位数日期字符串（18点日期格式），日期处理
     *
     * @param str
     * @return
     */
    public static String dealDateFormat1(String str) {
        str = str.replace("-", "");
        str = str.replace(" ", "");
        str = str.replace(":", "");
        str = str.substring(0, 12);
        return str;
    }

    /**
     * 判断参数的格式  ：
     * 数值型
     * 整数
     * 百分数
     */
    public static String checkDataType(String data) {
        String flag = "";
        if (!(data == null) && !"".equals(data)) {
            //判断data是否为数值型
            if (data.toString().matches("^0-?\\d+$")) {
                flag = "text";
            } else if (data.toString().matches("^-?\\d+$")) {
                flag = "int";
            } else if (data.toString().matches("^(-?\\d+)(\\.\\d+)?$")) {
                //判断data是否为整数（小数部分是否为0）
                flag = "double";
            } else if (data.toString().matches("^-?((\\d+\\.?\\d*)|(\\d*\\.\\d+))\\%$")) {
                //判断data是否为百分数（是否包含“%”）
                flag = "percent";
            } else {
                //如果以上判断都不存在，则判断为文本
                flag = "text";
            }
        }
        return flag;
    }

    /**
     * 根据单位代码判断选择的属于货运中心or公司
     * 1、货运中心， 2、 公司
     *
     * @param 单位代码dwdm
     */
    public static String checkZXorGS(String dwdm) {
        String lx = "";
        if (dwdm == null || dwdm.equals("") || dwdm.equals("0")) {
            return lx;
        }
        String[] hyzx = {"1", "2", "3", "4", "5", "6", "7"};//货运中心代码
        String[] gs = {"195", "196", "197", "217", "256", "350", "a17"};//公司代码
        for (int i = 0; i < hyzx.length; i++) {
            if (hyzx[i].equals(dwdm)) {
                lx = "1";
                return lx;
            }
        }
        for (int i = 0; i < gs.length; i++) {
            if (gs[i].equals(dwdm)) {
                lx = "2";
                return lx;
            }
        }
        return lx;
    }

    public static String nullToDouble(String str) {

        if (str == null || str.trim().length() == 0) {
            str = "0.0";
        } else {
            str = str.replace("\"", "");
        }
        return str;

    }

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#.00");
        System.out.println(df.format(49.4862801867912));
    }
}
