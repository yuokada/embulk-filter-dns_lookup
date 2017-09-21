package org.embulk.filter.dns_lookup;

import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.ConfigSource;
import org.embulk.config.Task;
import org.embulk.config.TaskSource;
import org.embulk.spi.Exec;
import org.embulk.spi.FilterPlugin;
import org.embulk.spi.Page;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageOutput;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;
import org.slf4j.Logger;

import java.util.List;

public class DnsLookupFilterPlugin
        implements FilterPlugin
{
    public interface PluginTask
            extends Task
    {
        @Config("columns")
        @ConfigDefault("null")
        public List<String> getColumns();
    }

    @Override
    public void transaction(ConfigSource config, Schema inputSchema,
            FilterPlugin.Control control)
    {
        PluginTask task = config.loadConfig(PluginTask.class);

        Schema outputSchema = inputSchema;

        control.run(task.dump(), outputSchema);
    }

    @Override
    public PageOutput open(TaskSource taskSource, final Schema inputSchema,
            final Schema outputSchema, final PageOutput output)
    {
        final PluginTask task = taskSource.loadTask(PluginTask.class);
        return new PageOutput()
        {
            final Logger logger = Exec.getLogger(this.getClass());
            private final PageReader reader = new PageReader(inputSchema);
            private final PageBuilder builder = new PageBuilder(Exec.getBufferAllocator(), outputSchema, output);
            private final DnsLookupColumnVisitor columnVisitor = new DnsLookupColumnVisitor(reader, builder, task);

            @Override
            public void add(Page page)
            {
                reader.setPage(page);
                while (reader.nextRecord()) {
                    outputSchema.visitColumns(this.columnVisitor);
                    builder.addRecord();
                }
            }

            @Override
            public void finish()
            {
                builder.finish();
            }

            @Override
            public void close()
            {
                builder.close();
            }
        };
    }
}
