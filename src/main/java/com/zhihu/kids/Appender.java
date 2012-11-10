package com.zhihu.kids;

/**
 * Created with IntelliJ IDEA.
 * User: shenchen
 * Date: 8/8/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
import redis.clients.jedis.Jedis;


public class Appender extends AppenderSkeleton {

    private String host ;
    private int    port ;
    private String topic;

    private Jedis client = null;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void activateOptions() {
        client = new Jedis(host, port);
        super.activateOptions();
    }

    @Override
    protected void append(LoggingEvent event) {
        if(client != null) {

            StringBuffer buf = new StringBuffer();
            buf.append(layout.format(event));
            if(layout.ignoresThrowable()) {
                String[] s = event.getThrowableStrRep();
                if (s != null) {
                    for(int i = 0; i < s.length; i++) {
                        buf.append(s[i]);
                        buf.append("\r\n");
                    }
                }
            }

            for (int i = 0; i < 3; i ++) {
                try{
                    client.publish(topic.getBytes(), buf.toString().getBytes());
                    break;
                } catch (Exception e) {
                    errorHandler.error("Error publish to kids", e, ErrorCode.FLUSH_FAILURE);
                    synchronized (this) {
                        try{
                            client.disconnect();
                            client.connect();
                        } catch (Exception ee) {
                            errorHandler.error("Error in kids reconnect", e, ErrorCode.FLUSH_FAILURE);
                        }
                    }
                }
            }

        }
    }

    @Override
    public void close() {
        try{
            client.disconnect();
        } catch (Exception e) {
            errorHandler.error("Error in kids close", e, ErrorCode.CLOSE_FAILURE);
        }
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
}

