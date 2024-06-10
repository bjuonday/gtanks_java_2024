package amalgama.system.quartz;

import org.quartz.JobDetail;

public interface IQuartzService {
    JobDetail addJobInterval(String paramString1, String paramString2, IQuartzJob paramQuartzJob, TimeUnit paramTimeType, long paramLong);
    JobDetail addJobInterval(String paramString1, String paramString2, IQuartzJob paramQuartzJob, TimeUnit paramTimeType, long paramLong, int paramInt);
    JobDetail addJob(String paramString1, String paramString2, IQuartzJob paramQuartzJob, TimeUnit paramTimeType, long paramLong);
    void deleteJob(String paramString1, String paramString2);
}
