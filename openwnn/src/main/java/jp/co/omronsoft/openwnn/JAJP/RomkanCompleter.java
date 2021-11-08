/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

package jp.co.omronsoft.openwnn.JAJP;

import java.util.HashMap;
import java.util.Locale;
import java.util.Iterator;

/**
 * RomkanCompleter
 *
 * @author harry
 * @since 2021-06-29
 */
public class RomkanCompleter {

    /** completion Table */
    private static final HashMap<String, String> COMPLETION_TABLE = new HashMap<String, String>() {
        {
            put("n", "\u3093"); // n --> ん(nn)
        }
    };

    /**
     * CompleteType
     *
     * @since 2021-06-29
     */
    public enum CompleteType {
        /**
         * NONE
         */
        NONE,
        /**
         * N2NN
         */
        N2NN
    }

    /** completion Type */
    private CompleteType mCurrentCompleteType = CompleteType.NONE;

    /** the original hiragana; For example, こn */
    private String mOriHiragana = "";

    /** the completed hiragana; For example, こん */
    private String mCompletedHiragana = "";

    /**
     * Default constructor
     */
    public RomkanCompleter() {
    }

    /**
     * complete the hiragana from the ComposingText layer #1
     * <br>
     * This conversion is used before predict word
     * For example, こn --> こん
     *
     * @param inputHiragana The hiragana data ends with Romaji
     * @return the completed hiragana
     */
    public String completeHiragana(String inputHiragana) {

        mOriHiragana = inputHiragana;
        mCompletedHiragana = inputHiragana;
        Iterator<String> iter = COMPLETION_TABLE.keySet().iterator();
        while (iter.hasNext()) {
            String oriTail = iter.next();
            String newTail = COMPLETION_TABLE.get(oriTail);
            if (inputHiragana.toLowerCase(Locale.JAPAN).endsWith(oriTail)) {
                mCompletedHiragana = inputHiragana.substring(0, inputHiragana.length() - oriTail.length()) + newTail;
                mCurrentCompleteType = CompleteType.N2NN;
            } else {
                mCurrentCompleteType = CompleteType.NONE;
            }
        }
        return mCompletedHiragana;
    }
}
