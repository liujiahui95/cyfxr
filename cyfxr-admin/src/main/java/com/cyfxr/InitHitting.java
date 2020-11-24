package com.cyfxr;

import java.util.Random;

public class InitHitting {

    public static void main(String[] args) {
        int max = 0;
        int min = 1000;
        for (int i = 0; i < 1000; i++) {
            int sum = 0;
            int count = 0;
            while (sum < 8) {
                Random r = new Random();
                int a = r.nextInt(2);
                if (a == 0) {
                    sum += 1;
                } else if (a == 1) {
                    sum += -1;
                }
                count += 1;
            }
            if (max < count) {
                max = count;
            }
            if (min > count) {
                min = count;
            }
            System.out.println("进行了" + count + "次之后，结果为：" + sum);
        }
        System.out.println("最大值：" + max + "---   最小值：" + min);
    }
}
