package com.shihuc.up.func;

/**
 * @Author: chengsh05
 * @Date: 2020/1/8 15:59
 */
public class Symbol {

    final static char CHAR_SINGLE_QUOTE = '\'';
    final static String STRING_SINGLE_QUOTE = "\'";

    final static char CHAR_DOUBLE_QUOTE = '\"';
    final static String STRING_DOUBLE_QUOTE = "\"";

    final static char COMMA = ',';
    final static String STR_COMMA = ",";
    final static char SPACE = ' ';

    final static String BRACKET_TYPE_SMALL = "small";
    final static char SMALL_BRACKET_LEFT = '(';
    final static char SMALL_BRACKET_RIGHT = ')';

    final static String BRACKET_TYPE_SQUARE = "square";
    final static char SQUARE_BRACKET_LEFT = '[';
    final static char SQUARE_BRACKET_RIGHT = ']';

    final static String BRACKET_TYPE_BIG = "big";
    final static char BIG_BRACKET_LEFT = '{';
    final static char BIG_BRACKET_RIGHT = '}';

    /**
     * 普通字符串的替换
     */
    final static char CHAR_BACKUP = 'x';
    /**
     * 针对函数位置字符串位置替换
     */
    final static char CHAR_FUNC_BACKUP = 'f';
}
