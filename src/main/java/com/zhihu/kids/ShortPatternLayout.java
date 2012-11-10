package com.zhihu.kids;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created with IntelliJ IDEA.
 * User: shenchen
 * Date: 9/11/12
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShortPatternLayout extends PatternLayout {
    private static final String sysInfo = Util.getHostName() + ":" + Util.getPid();

    @Override
    protected PatternParser createPatternParser(String pattern) {
        return new SpecialPatternParser(((pattern==null) ? PatternLayout.DEFAULT_CONVERSION_PATTERN:pattern));
    }

    class SpecialPatternParser extends PatternParser {


        public SpecialPatternParser(String pattern) {
            super(pattern);
        }

        @Override
        protected  void finalizeConverter(char arg0) {
            PatternConverter pc = null;
            switch (arg0) {
                case 'Z':
                    pc = new ShortLevelConverter(formattingInfo);
                    break;
                case 'S':
                    pc = new SysInfoConverter(formattingInfo);
                    break;
                default:
                    super.finalizeConverter(arg0);
            }
            if(pc != null) addConverter(pc);
        }
    }

    class ShortLevelConverter extends PatternConverter {

        public ShortLevelConverter(FormattingInfo fi) {
            super(fi);
        }

        @Override
        protected String convert(LoggingEvent event) {
            return event.getLevel().toString().substring(0,1);
        }
    }

    class SysInfoConverter extends PatternConverter {

        public SysInfoConverter(FormattingInfo fi) {
            super(fi);
        }

        @Override
        protected String convert(LoggingEvent event) {
            return sysInfo;
        }
    }
}



