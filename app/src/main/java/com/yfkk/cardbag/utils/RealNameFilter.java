package com.yfkk.cardbag.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 只能输入英文，汉字，数字
 */
public class RealNameFilter implements InputFilter {

    // 是否只能是中文
    boolean isChinese;

    public RealNameFilter() {
        super();
    }

    public RealNameFilter(boolean isChinese) {
        super();
        this.isChinese = isChinese;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            if (isChinese && !isChinese(c)) {
                return "";
            }
            if (!Character.isLetterOrDigit(source.charAt(i))) {
                return "";
            }
        }
        return source;
    }


    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
