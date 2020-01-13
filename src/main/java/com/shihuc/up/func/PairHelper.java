package com.shihuc.up.func;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author: chengsh05
 * @Date: 2019/12/31 13:40
 *
 * 这个工具助手，主要用来检测给定的输入字符串中是否含有编程语法中定义的成对出现的元素，典型的的成对元素，这里要考虑的有：
 * 单引号（‘’），双引号（“”），小括号（（）），中括号（[]），大括号（{}）
 *
 * 核心思想，就是基于栈这个数据结构，通过遇到成对元素的左边部分则入栈，右边部分则出栈，以此来监测整体输入的合法性。
 */
public class PairHelper {



    /**
     * 对待处理的字符串，进行处理，找出其中引号的位置，通过列表记录其引号在字符串中的序号
     *
     * @param src 待处理字符串
     * @param quote 引号类型
     * @param quotesIdxList 引号序号列表
     * @throws Exception 参数异常
     */
    public void quoteTravese(String src, char quote, List<Integer> quotesIdxList) throws Exception{
        if (StringUtils.isEmpty(src)) {
            throw new Exception("输入参数不能为空");
        }
        if (quote != Symbol.CHAR_SINGLE_QUOTE && quote != Symbol.CHAR_DOUBLE_QUOTE) {
            throw new Exception("输入参数引号类型不合法，只能是英文单引号或者英文双引号");
        }
        if (quotesIdxList == null){
            throw new Exception("入参引号位置记录器未指定");
        }
        char srcArr[] = src.toCharArray();
        for (int i=0; i<srcArr.length; i++){
            char c = srcArr[i];
            if (c == quote){
                quotesIdxList.add(i);
            }
        }
    }

    private boolean validateQuote(char srcArr[], List<Integer> quotesIdxList) {

        int srcLen = srcArr.length;
        for (int j = 0; j < quotesIdxList.size(); j = j + 2) {
            int left = quotesIdxList.get(j);
            int right = quotesIdxList.get(j + 1);
            if (left > 0) {
                char cl = srcArr[left - 1];
                if (cl != Symbol.COMMA && cl != Symbol.SMALL_BRACKET_LEFT && cl != Symbol.SPACE) {
                    return false;
                }
            }
            if (right < srcLen - 1) {
                char cl = srcArr[right + 1];
                if (cl != Symbol.COMMA && cl != Symbol.SMALL_BRACKET_LEFT && cl != Symbol.SPACE) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 将原字符串中引号包含的部分进行替换，用指定的字符替换，替换后的字符串返回，同时记录下引号内部原本的内容，方便后面还原
     *
     * @param src 原始字符串
     * @param quotesIdxList 引号下标列表
     * @param backup 指定的替换字符
     * @return 完整的输入字符串中引号被替换后的内容
     * @throws Exception
     */
    public String quoteBackup(String src, List<Integer> quotesIdxList, char backup) throws Exception {
        if (quotesIdxList.size() % 2 != 0){
            throw new Exception("引号不成对出现");
        }
        char srcArr[] = src.toCharArray();
        for (int j = 0; j < quotesIdxList.size(); j=j+2) {
            int from = quotesIdxList.get(j);
            int to = quotesIdxList.get(j+1);
            for (int i = from+1; i < to; i++) {
                srcArr[i] = backup;
            }
        }
        return String.valueOf(srcArr);
    }

    private String handleBracketBasedType(char []srcArr, String bracket, Stack<FuncElement> brackets, List<FuncElement> FuncElementList) throws Exception {
        String ret = null;
        try {
            if (bracket.equalsIgnoreCase(Symbol.BRACKET_TYPE_SMALL)) {
                ret = "小括号";
                bracketProcess(srcArr, Symbol.SMALL_BRACKET_LEFT, Symbol.SMALL_BRACKET_RIGHT, brackets, FuncElementList);
            }else if (bracket.equalsIgnoreCase(Symbol.BRACKET_TYPE_SQUARE)){
                ret = "中括号";
                bracketProcess(srcArr, Symbol.SQUARE_BRACKET_LEFT, Symbol.SQUARE_BRACKET_RIGHT, brackets, FuncElementList);
            }else if (bracket.equalsIgnoreCase(Symbol.BRACKET_TYPE_BIG)) {
                ret = "大括号";
                bracketProcess(srcArr, Symbol.BIG_BRACKET_LEFT, Symbol.BIG_BRACKET_RIGHT, brackets, FuncElementList);
            }
        } catch (Exception e) {
            throw new Exception("输入待检测的字符串'" + ret + "'" + e.getMessage());
        }
        return ret;
    }

    private Stack<FuncElement> bracketProcess(char []srcArr, char left, char right, Stack<FuncElement> brackets, List<FuncElement> FuncElementList) throws Exception {
        StringBuffer leftCont = new StringBuffer();
        for(int i=0; i < srcArr.length; i++){
            char c = srcArr[i];
            if (c == left){
                FuncElement fe = new FuncElement();
                fe.setLeftIdx(i);
                fe.setFuncName(leftCont.toString());
                brackets.push(fe);
                FuncElementList.add(fe);
                //清空buffer的内容
                leftCont.setLength(0);
            }else if (c == right) {
                if (brackets.empty()) {
                    throw new Exception("在没有出现左括号的情况下，先出现了右括号");
                }
                FuncElement fe = brackets.pop();
                fe.setRightIdx(i);
                //清空buffer的内容
                leftCont.setLength(0);
            }else {
                leftCont.append(c);
            }
        }
        return brackets;
    }

    /**
     * 遍历拆分出一个给定字符串中，括号配对情况，以及左括号左边的字符串信息，典型的，这里处理函数串的分析。
     * 注意： 函数嵌套的处理，函数参数可能是变量，常量，或者是函数
     *
     * @param backupedSrc 输入已经完成引号分析后的字符串
     * @param bracketType 括号类型，典型的是小括号，中括号，大括号
     * @param FuncElementList 配对项列表
     * @throws Exception
     */
    public void bracketTravese(String backupedSrc, String bracketType, List<FuncElement> FuncElementList) throws Exception{
        if (StringUtils.isEmpty(backupedSrc)) {
            throw new Exception("输入参数不能为空");
        }
        if (bracketType != Symbol.BRACKET_TYPE_SMALL && bracketType != Symbol.BRACKET_TYPE_BIG && bracketType != Symbol.BRACKET_TYPE_BIG) {
            throw new Exception("输入参数括号类型不合法，只能是英文小括号，中括号，大括号");
        }
        if (FuncElementList == null){
            throw new Exception("入参位置记录器未指定");
        }
        char srcArr[] = backupedSrc.toCharArray();
        Stack<FuncElement> brackets = new Stack<>();
        String type = handleBracketBasedType(srcArr, bracketType, brackets, FuncElementList);

        if (!brackets.empty()) {
            throw new Exception("输入待检测的字符串'" + type + "'没有成对出现");
        }
    }
}
