package com.example.comunda;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.awt.*;
import java.net.URI;

/**
 * 外部任务处理器
 */
public class DrawMoneyProcessHandler implements ExternalTaskHandler {

    public static void main(String[] args) {
        ExternalTaskClient taskClient = ExternalTaskClient.create()
                .baseUrl("http://192.168.100.176:8081/engine-rest")
                .asyncResponseTimeout(10000)
                .build();
        taskClient.subscribe("draw-money")
                .lockDuration(1000)
                .handler(new DrawMoneyProcessHandler())
                .open();
    }


    /**
     * 外部任务处理器实现
     * @param externalTask
     * @param externalTaskService
     */
    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        String item = externalTask.getVariable("item");
        Integer amount = externalTask.getVariable("amount");

        try {
            Desktop.getDesktop().browse(new URI("https://docs.camunda.org/get-started/quick-start/complete"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 完成任务
        externalTaskService.complete(externalTask);
    }
}
