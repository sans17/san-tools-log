package us.ligusan.base.tools.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

public class FormatterSImpl extends Formatter
{
    private String format;

    public FormatterSImpl()
    {
        format = LogManager.getLogManager().getProperty(getClass().getName() + ".format");
        if(format == null)
            // san - Nov 21, 2021 6:44:02 PM : 1 - timestamp
            // san - Nov 7, 2021 8:32:17 PM : 7 - sequence number
            // san - Nov 7, 2021 8:41:29 PM : 11 - real thread id
            // san - Nov 7, 2021 8:41:29 PM : 12 - thread name
            // san - Nov 7, 2021 8:32:17 PM : 4 - level
            // san - Nov 7, 2021 8:32:17 PM : 8 - source class name
            // san - Nov 7, 2021 8:32:17 PM : 9 - source method name
            // san - Nov 7, 2021 8:32:17 PM : 5 - message
            // san - Nov 7, 2021 8:32:17 PM : 6 - stacktrace
            format = "%1$tF_%1$tT.%1$tL %1$tZ - %7$d - %11$d.%12$s - %4$s - %8$s.%9$s: %5$s%n%6s";
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
    public String format(final LogRecord pRecord)
    {
        // san - Nov 7, 2021 7:58:32 PM : logic similar to SimpleFormatter
        String lLoggerName = pRecord.getLoggerName();

        String lSourceClassName = pRecord.getSourceClassName();
        String lSourceMethodName = pRecord.getSourceMethodName();

        String lSource = lLoggerName;
        if(lSourceClassName != null)
        {
            lSource = lSourceClassName;

            if(lSourceMethodName != null) lSource += ' ' + lSourceMethodName;
        }

        String lStackTrace = "";
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
            lStackTrace = "Unable to print stacktrace!" + System.lineSeparator();
        }

        Thread lCurrentThread = Thread.currentThread();
        // san - Nov 7, 2021 8:32:17 PM : 11 - logger name

        return String.format(getFormat(),
            // san - Nov 7, 2021 8:29:50 PM : 1 - time
            ZonedDateTime.ofInstant(pRecord.getInstant(), ZoneId.systemDefault()),
            // san - Nov 7, 2021 8:30:21 PM : 2 - source
            lSource,
            // san - Nov 7, 2021 8:32:17 PM : 3 - logger name
            lLoggerName,
            // san - Nov 7, 2021 8:32:17 PM : 4 - level
            pRecord.getLevel(),
            // san - Nov 7, 2021 8:32:17 PM : 5 - message
            formatMessage(pRecord),
            // san - Nov 7, 2021 8:32:17 PM : 6 - stacktrace
            lStackTrace,
            // san - Nov 7, 2021 9:00:38 PM : additional parameters not present in SimpleFormatter
            // san - Nov 7, 2021 8:32:17 PM : 7 - sequence number
            pRecord.getSequenceNumber(),
            // san - Nov 7, 2021 8:32:17 PM : 8 - source class name
            lSourceClassName,
            // san - Nov 7, 2021 8:32:17 PM : 9 - source method name
            lSourceMethodName,
            // san - Nov 7, 2021 8:32:17 PM : 10 - thread id from record
            pRecord.getLongThreadID(),
            // san - Nov 7, 2021 8:41:29 PM : 11 - real thread id
            lCurrentThread.getId(),
            // san - Nov 7, 2021 8:41:29 PM : 12 - thread name
            lCurrentThread.getName());
    }
}
