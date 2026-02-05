package net.ooder.web;

import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.common.expression.function.AbstractFunction;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;

@EsbBeanAnnotation(id = "currChromeDriver")
public class GetChromeDriver extends AbstractFunction {

    public ChromeProxy perform() {
        ChromeProxy chromeProxy = chromeProxy = new LogSetpLog();
        return chromeProxy;
    }
}