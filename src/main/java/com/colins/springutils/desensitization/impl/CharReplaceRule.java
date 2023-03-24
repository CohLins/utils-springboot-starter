package com.colins.springutils.desensitization.impl;


import com.colins.springutils.desensitization.DesensitizationRule;

public class CharReplaceRule implements DesensitizationRule {
    private int startInclude;
    private int endExclude;
    private char replacedChar;

    public CharReplaceRule(int startInclude, int endExclude, char replacedChar) {
        this.startInclude = startInclude;
        this.endExclude = endExclude;
        this.replacedChar = replacedChar;
    }

    public CharReplaceRule(int startInclude, char replacedChar) {
        this.startInclude = startInclude;
        this.replacedChar = replacedChar;
    }

    public CharReplaceRule(int startInclude) {
        this.startInclude = startInclude;
    }

    public CharReplaceRule(int startInclude, int endExclude) {
        this.startInclude = startInclude;
        this.endExclude = endExclude;
    }

    public int getStartInclude() {
        return this.startInclude;
    }

    public int getEndExclude() {
        return this.endExclude;
    }

    public char getReplacedChar() {
        return this.replacedChar;
    }
}
