package com.shihuc.up.func;


import java.util.ArrayList;
import java.util.List;

/**
 * @Author: chengsh05
 * @Date: 2020/1/10 10:22
 *
 * 类的主要功能： 将一个完整的输入字符串，进行按最外层逗号分隔，每个分隔后的部分，成为一个独立的path。
 * 典型的应用场景： 给定一个SQL语句，例如select xxxx from yyy;
 *      这里就是处理xxx部分的拆分，将每个查询部分拆分出来，每个部分是基于逗号分隔的。
 *
 * 分隔思路：
 *      1）将引号包含部分的内容，替换成仅仅含有字母的部分
 *      2）基于上一步，拆分函数部分，找出最外层的函数，然后，找出其中的逗号位置，进行逗号分隔
 */
public class PathHelper {

    /**
     * 用指定的字符，将单引号括起来的部分进行替换
     *
     * @param inputSrc 原始输入字符串
     * @param backup 指定的用来替换的字符
     * @return 替换后的字符串
     * @throws Exception
     */
    public String pathQuoteReplacement(String inputSrc, char backup) throws Exception {
        PairHelper ph = new PairHelper();
        List<Integer> quoteIdxList = new ArrayList<>();
        ph.quoteTravese(inputSrc, Symbol.CHAR_SINGLE_QUOTE, quoteIdxList);
        String backedSrc = ph.quoteBackup(inputSrc, quoteIdxList, backup);
        return backedSrc;
    }

    /**
     * 从给定的所有的函数列表中，找出最外层的函数，即剔除掉因作为参数存在的函数
     *
     * @param funcElementList 原始的全部函数的列表
     * @param funcBoundList 存储结算后得到的最外层函数的列表
     */
    private void calcTheFuncBound(List<FuncElement> funcElementList, List<FuncElement> funcBoundList) {
        int to = 0;
        for (int i = 0; i< funcElementList.size(); i++) {
            FuncElement fe = funcElementList.get(i);
            int fx = fe.getLeftIdx();
            int tx = fe.getRightIdx();
            if (fx > to) {
                funcBoundList.add(fe);
                to = tx;
            }
        }
    }

    /**
     * 按照逗号分隔，找出给定字数组中的逗号的下标
     *
     * @param backedSrcArr 替换后的字符数组
     * @param from 查找逗号下标的起点索引
     * @param to 查找逗号下标的终点索引
     * @param commaIdxList 逗号下标列表
     */
    private void calcPathCommaIdx(char backedSrcArr[], int from, int to, List<Integer> commaIdxList) {
        for (int i=from; i < to; i++) {
            if (backedSrcArr[i] == Symbol.COMMA) {
                commaIdxList.add(i);
            }
        }
    }

    /**
     * 基于给定字符串最外层的单引号的下标索引，将输入字符串内容进行分割
     *
     * @param inputSrc 原始输入字符串
     * @param commaIdxList 逗号下标索引列表
     * @return 分割后的字符串列表
     */
    private List<String> calcPathElement(String inputSrc, List<Integer> commaIdxList) {
        int from = 0;
        String path;
        List<String> paths = new ArrayList<>();
        if (commaIdxList.size() > 0) {
            for (int i = 0; i < commaIdxList.size(); i++) {
                int to = commaIdxList.get(i);
                path = inputSrc.substring(from, to);
                paths.add(path);
                from = to + 1;
            }
            path = inputSrc.substring(from);
            paths.add(path);
        }else{
            paths.add(inputSrc);
        }

        return  paths;
    }

    /**
     * 将给定的一个逗号分隔的字符串，按照最外层的逗号为基准，将字符串（字符串中含有函数，函数的参数有可能是单引号引起来的常量部分）依据逗号
     * 分隔，提取出逗号分隔的子字符串。
     *
     * @param inputSrc 元素的字符串
     * @param backedSrc 原始字符串中，单引号部分引起来的部分被替换后的字符串
     * @return 分隔后的字符串列表
     * @throws Exception
     */
    public List<String> retrievePath(String inputSrc, String backedSrc) throws Exception {
        PairHelper ph = new PairHelper();
        /**
         * 按照小括号进行定界函数的参数边界，计算出字符串中的函数列表
         */
        List<FuncElement> funcElementList = new ArrayList<>();
        ph.bracketTravese(backedSrc, Symbol.BRACKET_TYPE_SMALL, funcElementList);

        /**
         * 将上一步中获取到的函数列表，进行函数名的精确处理，去掉原始函数名中可能含有的逗号及逗号前面的部分
         */
        FuncHelper fh = new FuncHelper();
        fh.handleFuncName(funcElementList);

        /**
         * 从函数列表中，找出最外层的函数列表，即去除作为参数的函数（函数的调用允许嵌套调用）
         */
        List<FuncElement> funcBoundList = new ArrayList<>();
        calcTheFuncBound(funcElementList, funcBoundList);

        char backedSrcArr[] = backedSrc.toCharArray();
        int from = 0;
        List<Integer> commaIdxList = new ArrayList<>();
        for (int i = 0; i < funcBoundList.size(); i++) {
            FuncElement fe = funcBoundList.get(i);
            int fx = fe.getLeftIdx();
            int tx = fe.getRightIdx();
            String funcName = fe.getFuncName();
            fx = fx - funcName.length();
            calcPathCommaIdx(backedSrcArr, from, fx, commaIdxList);

            //动态调整下次分析的非函数部分的起点
            from = tx + 1;
        }
        List<String> paths = calcPathElement(inputSrc, commaIdxList);
        return paths;
    }

    public static void main(String args[]) throws Exception {
        String src = "f1(a, f2(), 'x,bmc,c'), h(), abc, w(a,b,c,) sbs";
        PathHelper ph = new PathHelper();
        String backedSrc = ph.pathQuoteReplacement(src, Symbol.CHAR_BACKUP);
        List<String> paths = ph.retrievePath(src, backedSrc);
        System.out.println("src: " + src);
        for (String path: paths) {
            System.out.println("path: " + path.trim());
        }
    }
}
