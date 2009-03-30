package org.ddialliance.ddiftp.util.log;

public class LogType
{
    private String logName;
    
    public static final LogType EXCEPTION = new LogType("Exception");
    public static final LogType SYSTEM = new LogType("System");
    public static final LogType PERSISTENCE = new LogType("Persistence");
    public static final LogType BUG = new LogType("Bug");
    
    /** Creates a new instance of LogType */
    protected LogType(String logName)
    {
        this.logName = logName;
    }
    
    public String getLogName()
    {
        return logName;
    }
}

