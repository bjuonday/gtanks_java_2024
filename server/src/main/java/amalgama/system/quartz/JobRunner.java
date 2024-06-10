package amalgama.system.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobRunner implements Job {
    public static String key = "runnable";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        IQuartzJob run = (IQuartzJob) context.getJobDetail().getJobDataMap().get(key);
        run.run(context);
    }
}
