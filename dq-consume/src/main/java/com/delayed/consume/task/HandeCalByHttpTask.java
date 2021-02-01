package com.delayed.consume.task;

import com.alibaba.fastjson.JSON;
import com.delayed.base.bean.ComResponseBean;
import com.delayed.base.bean.Job;
import com.delayed.base.enumeration.CommonKeyEnum;
import com.delayed.base.model.DqTopicConfig;
import com.delayed.base.notify.NotifyService;
import com.delayed.base.repository.DqTopicConfigRepository;
import com.delayed.base.utils.RedisUtils;
import com.delayed.base.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: tjx
 * @描述: 处理 利用 http请求消费
 * @创建时间: 创建于15:30 2020/9/9
 **/
@Slf4j
public class HandeCalByHttpTask implements Runnable {

    /**
     * 当前消费对象
     */
    private Job job;

    private DqTopicConfigRepository dqTopicConfigRepository;
    private NotifyService notifyService;

    public HandeCalByHttpTask(Job job, DqTopicConfigRepository dqTopicConfigRepository, NotifyService notifyServic) {
        this.job = job;
        this.dqTopicConfigRepository = dqTopicConfigRepository;
        this.notifyService = notifyServic;
    }

    @Override
    public void run() {
        if (job == null) {
            return;
        }

        if (StringUtil.isNull(job.getTopic())) {
            return;
        }
        //判断topic是否状态正常
        List<DqTopicConfig> topics = dqTopicConfigRepository.findByTopic(job.getTopic());
        if (topics.size() == 0) {
            return;
        }
        DqTopicConfig topicConfig = topics.get(0);
        try {

            URL restUrl = new URL(topicConfig.getCallBack());
            /*
             * 此处的urlConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类 的子类HttpURLConnection
             */
            HttpURLConnection conn = (HttpURLConnection) restUrl.openConnection();
            //请求方式
            conn.setRequestMethod("POST");
            //设置是否从httpUrlConnection读入
            conn.setDoOutput(true);
            //allowUserInteraction 如果为 true，则在允许用户交互（例如弹出一个验证对话框）的上下文中对此 URL 进行检查。
            conn.setAllowUserInteraction(false);

            //处理参数
            String content = "";
            String body = job.getBody();
            if (StringUtil.isNotNull(body)) {
                HashMap<String, String> parameters = JSON.parseObject(body, HashMap.class);
                if (null != parameters && parameters.size() > 0) {
                    for (Map.Entry<String, String> entry : parameters.entrySet()) {
                        content += "&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "utf-8");
                    }
                    DataOutputStream out = new DataOutputStream(conn
                            .getOutputStream());
                    out.writeBytes(content.replaceFirst("&", ""));
                    //流用完记得关
                    out.flush();
                }
            }

            BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line, resultStr = "";

            while (null != (line = bReader.readLine())) {
                resultStr += line;
            }
            bReader.close();
            log.info("jobId:" + job.getId() + "\t" + resultStr);
            //解析成数据
            ComResponseBean response = JSON.parseObject(resultStr, ComResponseBean.class);
            String retryMark = topicConfig.getRetryMark();
            int succeeCode = Integer.parseInt(retryMark);
            if (response.getCode() == succeeCode) {
                //执行成功 删除job 状态
                RedisUtils.del(job.getId());
                RedisUtils.del(job.getId() + CommonKeyEnum.incr);
            } else {
                String msg = "http结果不符合,接口返回结果" + response.getCode() + "(" + job.toString() + ")";
                log.error(msg);
                notifyService.notifyMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("job执行失败(" + job.toString() + ")");
        }
    }


}
