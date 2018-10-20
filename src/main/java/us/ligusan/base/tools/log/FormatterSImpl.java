package us.ligusan.base.tools.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FormatterSImpl extends SimpleFormatter
{
    private String format = "{6,date,yyyy-MM-dd_HH:mm:ss.SSS z} - {1} - {9}.{10} - {0} - {2}.{3}: {4}\n{7}";

    @Override
    public String format(final LogRecord pRecord)
    {
        String lStackTrace = StringUtils.EMPTY;
        Throwable lThrown = pRecord.getThrown();
        if(lThrown != null) try
        {
            try (StringWriter lStringWriter = new StringWriter(); PrintWriter lPrintWriter = new PrintWriter(lStringWriter))
            {
                lThrown.printStackTrace(lPrintWriter);
                lStackTrace = lStringWriter.toString();
            }
        }
        catch(IOException e)
        {
            lStackTrace = "Unable to print stacktrace!";
        }

        Thread lCurrentThread = Thread.currentThread();
        return MessageFormat.format(getFormat(), pRecord.getLevel(), pRecord.getSequenceNumber(), pRecord.getSourceClassName(), pRecord.getSourceMethodName(), formatMessage(pRecord), pRecord.getThreadID(),
            new Date(pRecord.getMillis()), lStackTrace, pRecord.getLoggerName(), lCurrentThread.getId(), lCurrentThread.getName());
    }

    protected String getFormat()
    {
        return format;
    }

    protected void setFormat(final String pFormat)
    {
        format = pFormat;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("format", format).toString();
    }
}
