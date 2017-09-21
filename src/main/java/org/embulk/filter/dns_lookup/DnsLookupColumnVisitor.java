package org.embulk.filter.dns_lookup;

import org.embulk.filter.dns_lookup.DnsLookupFilterPlugin.PluginTask;
import org.embulk.spi.Column;
import org.embulk.spi.ColumnVisitor;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageReader;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class DnsLookupColumnVisitor
        implements ColumnVisitor
{
    private final PageReader pageReader;
    private final PageBuilder pageBuilder;
    private final PluginTask task;

    DnsLookupColumnVisitor(PageReader reader, PageBuilder builder, PluginTask task)
    {
        this.pageReader = reader;
        this.pageBuilder = builder;
        this.task = task;
    }

    @Override
    public void booleanColumn(Column column)
    {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setBoolean(column, pageReader.getBoolean(column));
        }
    }

    @Override
    public void longColumn(Column column)
    {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setLong(column, pageReader.getLong(column));
        }
    }

    @Override
    public void doubleColumn(Column column)
    {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setDouble(column, pageReader.getDouble(column));
        }
    }

    @Override
    public void stringColumn(Column column)
    {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column);
        }
        else {
            List<String> columns = task.getColumns();
            if (columns != null && columns.contains(column.getName())) {
                String url = pageReader.getString(column);
                try {
                    InetAddress inetAddress = InetAddress.getByName(url);
                    url = inetAddress.getHostName();
                }
                catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                pageBuilder.setString(column.getIndex(), url);
            }
            else {
                pageBuilder.setString(column.getIndex(), pageReader.getString(column));
            }
        }
    }

    @Override
    public void timestampColumn(Column column)
    {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setTimestamp(column, pageReader.getTimestamp(column));
        }
    }

    @Override
    public void jsonColumn(Column column)
    {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setJson(column, pageReader.getJson(column));
        }
    }
}
