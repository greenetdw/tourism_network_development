package com.beifeng.transformer.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.beifeng.common.EventLogConstants;

/**
 * transformer相关mapper reducer job代码中mapper公用父类，主要提供计数和hbase value的获取
 * 
 * @author gerry
 *
 * @param <KEYOUT>
 * @param <VALUEOUT>
 */
public class TransformerBaseMapper<KEYOUT, VALUEOUT> extends TableMapper<KEYOUT, VALUEOUT> {
    private static final Logger logger = Logger.getLogger(TransformerBaseMapper.class);
    private long startTime = System.currentTimeMillis();
    protected Configuration conf = null;
    protected int inputRecords = 0; // 输入记录数
    protected int filterRecords = 0; // 过滤的记录数, 要求输入的记录没有进行任何输出
    protected int outputRecords = 0; // 输出的记录条数
    protected byte[] family = Bytes.toBytes(EventLogConstants.EVENT_LOGS_FAMILY_NAME); // hbase的family名称

    /**
     * 初始化方法
     */
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        this.conf = context.getConfiguration();
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
        // 不希望这块代码影响整体功能，所以用try-catch括起来
        try {
            // 打印提示信息格式为: jobid 运行时间 输入记录数 过滤记录数 输出记录数
            long endTime = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            sb.append("job_id:").append(context.getJobID().toString());
            sb.append("; start_time:").append(this.startTime);
            sb.append("; end_time:").append(endTime);
            sb.append("; using_time:").append((endTime - this.startTime)).append("ms");
            sb.append("; input records:").append(this.inputRecords);
            sb.append("; filter records:").append(this.filterRecords);
            sb.append("; output records:").append(this.outputRecords);

            logger.info(sb.toString());
        } catch (Throwable e) {
            // nothing
        }
    }

    /**
     * 获取uuid
     * 
     * @param value
     * @return
     */
    public String getUuid(Result value) {
        return this.fetchValue(value, EventLogConstants.LOG_COLUMN_NAME_UUID);
    }

    /**
     * 获取平台名称对应的value
     * 
     * @param value
     * @return
     */
    public String getPlatform(Result value) {
        return this.fetchValue(value, EventLogConstants.LOG_COLUMN_NAME_PLATFORM);
    }

    /**
     * 获取服务器时间戳
     * 
     * @param value
     * @return
     */
    public String getServerTime(Result value) {
        return this.fetchValue(value, EventLogConstants.LOG_COLUMN_NAME_SERVER_TIME);
    }

    /**
     * 获取浏览器名称
     * 
     * @param value
     * @return
     */
    public String getBrowserName(Result value) {
        return this.fetchValue(value, EventLogConstants.LOG_COLUMN_NAME_BROWSER_NAME);
    }

    /**
     * 获取浏览器版本号
     * 
     * @param value
     * @return
     */
    public String getBrowserVersion(Result value) {
        return this.fetchValue(value, EventLogConstants.LOG_COLUMN_NAME_BROWSER_VERSION);
    }

    /**
     * 公用方法，提取value中的column列的值
     * 
     * @param value
     * @param column
     * @return
     */
    private String fetchValue(Result value, String column) {
        return Bytes.toString(value.getValue(family, Bytes.toBytes(column)));
    }

}
