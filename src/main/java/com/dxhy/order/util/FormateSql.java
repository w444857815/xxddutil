package com.dxhy.order.util;

import javafx.scene.control.Alert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @ClassName FormateSql
 * @Description TODO
 * @Author wangruwei
 * @Date 2021/1/20 10:59
 * @Version 1.0
 */
public class FormateSql {
    private JPanel panel1;
    private JButton button1;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JCheckBox checkBox1;

    public FormateSql() {
        panel1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panel1.setLayout(null);
            }
        });
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JOptionPane.showMessageDialog(null, "呵呵", "标题条文字串", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FormateSql");
        frame.setContentPane(new FormateSql().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(600,500);
        frame.setPreferredSize(new Dimension(600,500));
        String a = "{\n" +
                "    \"DDPCXX\": {\n" +
                "        \"DDQQPCH\": \"e30d13ada992413c8b936e98476dd8b5\",\n" +
                "        \"FPLXDM\": \"026\",\n" +
                "        \"NSRSBH\": \"15000120561127953X\"\n" +
                "    },\n" +
                "    \"DDZXX\": [\n" +
                "        {\n" +
                "            \"DDMXXX\": [\n" +
                "                {\n" +
                "                    \"DJ\": \"0.01\",\n" +
                "                    \"FPHXZ\": \"0\",\n" +
                "                    \"HSBZ\": \"1\",\n" +
                "                    \"JE\": \"0.01\",\n" +
                "                    \"SL\": \"0.060\",\n" +
                "                    \"SPBM\": \"3070102000000000000\",\n" +
                "                    \"SPSL\": \"1\",\n" +
                "                    \"XMMC\": \"教学费用\",\n" +
                "                    \"YHZCBS\": \"0\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"DDTXX\": {\n" +
                "                \"AUTHORDERID\": \"404337814987382784\",\n" +
                "                \"DDH\": \"404337814987382784\",\n" +
                "                \"DDQQLSH\": \"81514057389b48ac91769c1b1bc85135\",\n" +
                "                \"GMFDH\": \"020-34813616\",\n" +
                "                \"GMFDZ\": \"广州市番禺区东环街新骏一街6号1座624房、625房\",\n" +
                "                \"GMFDZYX\": \"1287402350@qq.com\",\n" +
                "                \"GMFLX\": \"01\",\n" +
                "                \"GMFMC\": \"广州德邦生物科技有限公司\",\n" +
                "                \"GMFSBH\": \"91440113689332864Q\",\n" +
                "                \"GMFYH\": \"\",\n" +
                "                \"GMFZH\": \"\",\n" +
                "                \"HJJE\": \"0\",\n" +
                "                \"HJSE\": \"0\",\n" +
                "                \"JSHJ\": \"0.01\",\n" +
                "                \"KPLX\": \"0\",\n" +
                "                \"LYXT\": \"融创文旅\",\n" +
                "                \"NSRMC\": \"15000120561127953X-小企业5\",\n" +
                "                \"NSRSBH\": \"15000120561127953X\",\n" +
                "                \"XHFDH\": \"18500481586\",\n" +
                "                \"XHFDZ\": \"小企业5\",\n" +
                "                \"XHFMC\": \"15000120561127953X-小企业5\",\n" +
                "                \"XHFSBH\": \"15000120561127953X\",\n" +
                "                \"XHFYH\": \"测试环境开户行\",\n" +
                "                \"XHFZH\": \"8110601012000819226\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
