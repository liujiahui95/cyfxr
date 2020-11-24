package com.cyfxr;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Test {

     public static void main(String[] args) throws Exception {
        String s = HttpClientUtils.sendPost("http://10.96.2.21:6000/sbd/query?date=20200907&content=20");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream("D:\\20200907.html", false), "gb2312"));
        bw.write(s);
        bw.close();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

    }
}
