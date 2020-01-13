package com.shihuc.up.func;

import java.util.List;

/**
 * @Author: chengsh05
 * @Date: 2020/1/8 16:35
 */
public class FuncElement {

    /**
     * 成对元素左侧的内容，主要针对括号类，引号不适应，即引号场景，此值为""
     */
    private String funcName;

    /**
     * 成对元素的左部分的序号
     */
    private int leftIdx;

    /**
     * 成对元素的右部分的序号
     */
    private int rightIdx;

    /**
     * 函数的参数列表
     */
    List<String> params;

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public int getLeftIdx() {
        return leftIdx;
    }

    public void setLeftIdx(int leftIdx) {
        this.leftIdx = leftIdx;
    }

    public int getRightIdx() {
        return rightIdx;
    }

    public void setRightIdx(int rightIdx) {
        this.rightIdx = rightIdx;
    }

    public static void main(String args[]) {
        String str1 = "a,";
        String arr1[] = str1.split(",");
        for (String ar: arr1) {
            System.out.println(ar);
        }
        String str2 = ",a";
        String arr2[] = str2.split(",");
        for (String ar: arr2) {
            System.out.println(ar);
        }
    }
}
