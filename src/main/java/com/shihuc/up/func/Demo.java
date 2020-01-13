package com.shihuc.up.func;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: chengsh05
 * @Date: 2020/1/10 8:30
 */
public class Demo {

    public void testCase_validateFunction() {
        PairHelper pairHelper = new PairHelper();
        FuncHelper funcHelper = new FuncHelper();
        List<Integer> quoteList = new ArrayList<>();

//        String inputSrc = "hello('abc', 'efg', world(f1(), ha), ass)";
        String inputSrc = "hello( world(f(1) , ga(a,s ), h('')) ,s('ab (as c',c),  ass[, X(A) )";
//        String inputSrc = "hello(s('ab (as c',x))";
        List<FuncElement> funcElementList = new ArrayList<>();
        System.out.println(inputSrc);

        try {
            pairHelper.quoteTravese(inputSrc, Symbol.CHAR_SINGLE_QUOTE, quoteList);
            String backupSrc = pairHelper.quoteBackup(inputSrc, quoteList, Symbol.CHAR_BACKUP);
            System.out.println(backupSrc);

            for (int i = 0; i < quoteList.size(); i=i+2) {
                System.out.println("      quote ==> left: " + quoteList.get(i) + ", right: " + quoteList.get(i+1));
            }
            pairHelper.bracketTravese(backupSrc, Symbol.BRACKET_TYPE_SMALL, funcElementList);
            for (FuncElement fe: funcElementList) {
                System.out.println("      small ==> leftCont: " + fe.getFuncName() + ", left: " + fe.getLeftIdx() + ", right: " + fe.getRightIdx());
            }
            boolean hasFunc = funcHelper.handleFuncName(funcElementList);
            if (hasFunc) {
                funcHelper.retrieveFuncParam(inputSrc, backupSrc, funcElementList);
                funcHelper.displayFuncInfo(funcElementList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testCase_validatePaths() {
        //String inputSrc = "f1(a, f2(), 'x,bmc,c'), h(), abc, w(a,b,c) sbs";
        String inputSrc = "hello( world(f(1) , ga(a,s ), h('')) ,s('ab (as c',c),  ass[, X(A) )";
        PathHelper ph = new PathHelper();
        PairHelper pairHelper = new PairHelper();
        FuncHelper funcHelper = new FuncHelper();

        try {
            String rawBackedSrc = ph.pathQuoteReplacement(inputSrc, Symbol.CHAR_BACKUP);
            List<String> paths = ph.retrievePath(inputSrc, rawBackedSrc);
            System.out.println("用户输入信息: " + inputSrc);
            for (String path: paths) {
                System.out.println("     path: " + path.trim());
            }
            for (String path: paths) {
                List<FuncElement> funcElementList = new ArrayList<>();
                List<Integer> quoteList = new ArrayList<>();
                pairHelper.quoteTravese(path, Symbol.CHAR_SINGLE_QUOTE, quoteList);
                String backupSrc = pairHelper.quoteBackup(path, quoteList, Symbol.CHAR_BACKUP);
                System.out.println("==========================================================");
                System.out.println("原始路径:" + path.trim());
                System.out.println("替换路径:" + backupSrc.trim());

                pairHelper.bracketTravese(backupSrc, Symbol.BRACKET_TYPE_SMALL, funcElementList);
                for (FuncElement fe: funcElementList) {
                    System.out.println("     small ==> leftCont: " + fe.getFuncName() + ", left: " + fe.getLeftIdx() + ", right: " + fe.getRightIdx());
                }
                boolean hasFunc = funcHelper.handleFuncName(funcElementList);
                if (hasFunc) {
                    funcHelper.retrieveFuncParam(path, backupSrc, funcElementList);
                    funcHelper.displayFuncInfo(funcElementList);
                }else{
                    System.out.println("原始路径:" + path.trim() + " 不存在函数");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Demo demo = new Demo();
        demo.testCase_validateFunction();
        //demo.testCase_validatePaths();
    }
}
