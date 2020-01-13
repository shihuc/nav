package com.shihuc.up.func;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author: chengsh05
 * @Date: 2019/12/31 13:40
 *
 * 主要基于栈数据结构，检测给定的输入字符串中，是否含有合法的函数，正常的函数定义为：函数名，小括号，然后在小括号内出现可能的参数列表，
 * 注意，这里的参数列表，可能也是函数， 也可能是变量，也可能是常量。
 *
 * 结合引号，小括号检查后得到的PairElement的列表，计算出小括号左边是否有有效字符串，有则认为是函数名称，没有则不合法
 *
 * 注意：不允许多余的小括号，必须的小括号有且仅有一层，如下面则不合法(参数“b”不需要外围的小括号)：
 * func1(a,("b"),c)
 */
public class FuncHelper {

    /**
     * 函数名规则的正则表达式，只能是以下划线、大小写英文字母开头；整个名称的组成只能是英文的字母或者数字或者下划线组成
     */
    private static String FUNC_NAME_REGX = "^(_|[a-z]|[A-Z])+([a-zA-Z0-9_])*";


    /**
     * 去右空格
     * @param str
     * @return
     */
    public String trimRight(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("[ ]+$", "");
        }
    }

    /**
     * 去左空格
     * @param str
     * @return
     */
    public String trimLeft(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("^[ ]+", "");
        }
    }

    /**
     * 校验经过小括号分割后的输入参数对中，函数名是否合法，每一个PairElement在这里表示一个可能的函数, 校验通过后，将函数名写入leftCont字段
     *
     * @param funcElementList 粗粒度的函数列表（函数名可能存在不合规的字符）
     * @return 是否存在函数
     * @throws Exception
     */
    public boolean handleFuncName(List<FuncElement> funcElementList) throws Exception {
        if (funcElementList.size() > 0) {
            for (FuncElement pe : funcElementList) {
                String cont = pe.getFuncName();
                String parts[] = cont.split(",");
                String rawFunc = parts[parts.length - 1];
                String newRawFunc = trimLeft(rawFunc);
                boolean isMatch = Pattern.matches(FUNC_NAME_REGX, newRawFunc);
                if (!isMatch) {
                    throw new Exception("函数名: '" + newRawFunc + "' 不合法，组成字符必须是英文字母数字或者下划线，且不能以数字开头");
                }
                pe.setFuncName(rawFunc);
            }
            return true;
        }else {
            return false;
        }
    }

    /**
     * 基于当前函数的左括号下标，从当前路径的函数列表中，找出其右括号的下标位置
     *
     * @param funcElementList 当前路径的函数列表信息
     * @param leftBracketPos 当前函数的左括号的下标位置
     * @return 当前函数的右括号下标位置
     */
    private int getTheRightBracketPos(List<FuncElement> funcElementList, int leftBracketPos) {
        int targetRightPos = -1;
        for (FuncElement fe: funcElementList) {
            int leftPos = fe.getLeftIdx();
            if (leftPos == leftBracketPos) {
                targetRightPos = fe.getRightIdx();
                break;
            }
        }
        return targetRightPos;
    }

    /**
     * 基于当前路径的指定函数的参数的参数列表中逗号的下标，将当前函数的参数部分进行分割，得出该函数的参数列表
     *
     * @param inputSrc 原生的路径信息
     * @param fromBorder 当前函数左括号的位置
     * @param toBorder 当前函数右括号的位置
     * @param commaIdxList 当前函数参数部分中逗号的下标列表
     * @param funcName 函数名称，用于错误提醒信息用
     * @return 当前函数的参数列表
     * @throws Exception
     */
    private List<String> splitBasedCommaIdx(String inputSrc, int fromBorder, int toBorder, List<Integer>commaIdxList, String funcName) throws Exception {
        boolean hasComma = commaIdxList.size() > 0 ? true : false;
        List<String> params = new ArrayList<>();
        if (!hasComma) {
            String rawParamStr = inputSrc.substring(fromBorder+1, toBorder);

            if (rawParamStr.trim().length() > 0) {
                params.add(rawParamStr);
            }
            return params;
        } else {
            int fx = fromBorder+1;
            for (int i = 0; i < commaIdxList.size(); i++) {
                int tx = commaIdxList.get(i);
                String param = inputSrc.substring(fx, tx);
                String trueParam = param.trim();
                if (trueParam.length() <= 0) {
                    throw new Exception("函数：‘" + funcName + "’参数不合法，参数缺失");
                }
                params.add(param);
                fx = tx + 1;
            }
            //处理最后一个逗号后面的部分是否合法
            String lastPart = inputSrc.substring(fx, toBorder);
            String trueLastPart = lastPart.trim();
            if (trueLastPart.length() <=0) {
                throw new Exception("函数：‘" + funcName + "’参数不合法，参数缺失");
            }else{
                params.add(lastPart);
            }
        }
        return params;
    }

    /**
     * 从给定路径信息以及含有的函数列表，基于当前函数的左右括号的边界，计算出该函数的参数列表
     *
     * @param inputSrc 原生的路径信息
     * @param backedSrc 引号括起来部分替换后的路径信息
     * @param from 当前函数左括号在原始路径字符数组中的下标
     * @param to 当前函数右括号在原始路径字符数组中的下标
     * @param funcName 函数的名称，用于提示信息用
     * @param funcElementList 当前路径的函数列表
     * @return 当前路径中函数的参数列表
     * @throws Exception
     */
    private List<String> calcFuncParams(String inputSrc, String backedSrc, int from, int to, String funcName, List<FuncElement> funcElementList) throws Exception {
        char rawParamsArr[] = backedSrc.toCharArray();
        List<Integer> commaIdxList = new ArrayList<>();
        //这里i的起点不能从当前函数的左括号起，必须是其右边的第一个字符开始，因为函数的括号部分不能当做参数
        for (int i = from+1; i < to; ){
            char c = rawParamsArr[i];
            if (c == Symbol.COMMA) {
                commaIdxList.add(i);
            }
            if (c == Symbol.SMALL_BRACKET_LEFT) {
                i = getTheRightBracketPos(funcElementList, i) + 1;
            }else {
                i++;
            }
        }
        List<String> params = splitBasedCommaIdx(inputSrc, from, to, commaIdxList, funcName);
        return params;
    }

    /**
     * 基于给定的路径信息，以及函数列表，计算出每一个函数的参数列表信息
     *
     * @param inputSrc 原生的路径信息
     * @param backedSrc 将引号部分的内容进行替换后的路径信息
     * @param funcElementList 函数列表
     * @throws Exception
     */
    public void retrieveFuncParam(String inputSrc, String backedSrc, List<FuncElement> funcElementList) throws Exception {
        int size = funcElementList.size();
        for (int i = 0; i < size; i++) {
            FuncElement fe = funcElementList.get(i);
            int from = fe.getLeftIdx();
            int to = fe.getRightIdx();
            String funcName = fe.getFuncName();
            List<String> params = calcFuncParams(inputSrc, backedSrc, from, to, funcName, funcElementList);
            fe.setParams(params);
        }
    }

    /**
     * 打印出当前路径的函数列表中的函数名及参数信息
     *
     * @param funcElementList
     */
    public void displayFuncInfo(List<FuncElement> funcElementList) {
        for (FuncElement fe: funcElementList) {
            String funcName = fe.getFuncName();
            List<String> params = fe.getParams();
            System.out.println("FuncName:" + funcName.trim());
            if (params.size() <= 0) {
                System.out.println("      此路径函数没有参数");
            }else {
                for (String pm : params) {
                    System.out.println("      param:" + pm.trim());
                }
            }
        }
    }

    public static void main(String args[]){
        String regx = "absdassdas0_";
        System.out.println("String matches: " + regx.matches(FUNC_NAME_REGX));
        System.out.println("Pattern matches: " + Pattern.matches(FUNC_NAME_REGX, regx));
    }
}
