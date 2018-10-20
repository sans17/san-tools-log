package us.ligusan.base.tools.log;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class LogTracer implements MethodInterceptor
{
    @Override
    public Object invoke(final MethodInvocation pMethodInvocation) throws Throwable
    {
        Object lThis = pMethodInvocation.getThis();
        String lClassName = lThis.getClass().getName();
        Logger lLogger = Logger.getLogger(lClassName);
        Method lMethod = pMethodInvocation.getMethod();
        String lMethodName = lMethod.getName();

        if(lLogger.isLoggable(Level.FINEST)) lLogger.logp(Level.FINEST, lClassName, lMethodName, "object={0}, method={1}, arguments={2}", new Object[] {lThis, lMethod, Arrays.asList(pMethodInvocation.getArguments())});
        try
        {
            Object ret = pMethodInvocation.proceed();

            if(lLogger.isLoggable(Level.FINEST)) lLogger.logp(Level.FINEST, lClassName, lMethodName, "object={0}, method={1}, ret={2}", new Object[] {lThis, lMethod, ret});

            return ret;
        }
        catch(Throwable t)
        {
            if(lLogger.isLoggable(Level.FINER)) lLogger.logp(Level.FINER, lClassName, lMethodName, "object={0}, method={1}, throwable={2}", new Object[] {lThis, lMethod, t});

            throw t;
        }
    }
}
