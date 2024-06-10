package amalgama.system.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.util.Date;

public class QuartzService implements IQuartzService {
    private static QuartzService instance = new QuartzService();
    private Scheduler scheduler;

    private QuartzService() {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        try {
            scheduler = factory.getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static QuartzService getInstance() {
        return instance;
    }

    private JobDetail createJob(String name, String group, IQuartzJob object) {
        JobBuilder builder = JobBuilder.newJob(JobRunner.class).withIdentity(name, group);
        JobDetail job = builder.build();
        job.getJobDataMap().put(JobRunner.key, object);
        return job;
    }

    @Override
    public JobDetail addJobInterval(String name, String group, IQuartzJob object, TimeUnit timeUnit, long internal) {
        return addJobInterval(name, group, object, timeUnit, internal, -1);
    }

    @Override
    public JobDetail addJobInterval(String a, String b, IQuartzJob c, TimeUnit d, long e, int f) {
        JobDetail job = createJob(a, b, c);
        try {
            SimpleTrigger trigger = new SimpleTriggerImpl(a, b, f, d.time(e));
            this.scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException ex) {
            ex.printStackTrace();
        }
        return job;
    }

    @Override
    public JobDetail addJob(String name, String group, IQuartzJob object, TimeUnit timeUnit, long time) {
        JobDetail job = createJob(name, group, object);
        try {
            SimpleTrigger trigger = new SimpleTriggerImpl(name, group, new Date(System.currentTimeMillis() + timeUnit.time(time)));
            this.scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return job;
    }

    @Override
    public void deleteJob(String name, String group) {
        try {
            this.scheduler.deleteJob(new JobKey(name, group));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
