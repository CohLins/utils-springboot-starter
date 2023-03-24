package com.colins.springutils.desensitization.impl;



import com.colins.springutils.desensitization.DesensitizationRule;

import java.util.function.Function;
import java.util.regex.Matcher;

public class RegularReplaceRule implements DesensitizationRule {

    private final String regex;
    private String replacedString;
    private Function<Matcher, String> replaceFunction;

    public RegularReplaceRule(String regex, Function<Matcher, String> replaceFunction) {
        this.regex = regex;
        this.replaceFunction = replaceFunction;
    }

    public RegularReplaceRule(String regex, String replacedString) {
        this.regex = regex;
        this.replacedString = replacedString;
    }

    public RegularReplaceRule(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return this.regex;
    }

    public String getReplacedString() {
        return this.replacedString;
    }

    public Function<Matcher, String> getReplaceFunction() {
        return this.replaceFunction;
    }
}
