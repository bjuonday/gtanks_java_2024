package amalgama.system.quartz;

import org.quartz.JobExecutionContext;

public interface IQuartzJob {
    void run(JobExecutionContext ctx);
}
