/*
 * Created by JFormDesigner on Sat Jan 23 14:54:11 CST 2021
 */

package com.dxhy.order.util;

import org.springframework.util.StringUtils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author unknown
 */
public class sql extends JFrame {
    public sql() {
        initComponents();
    }


    //格式化sql
    private void formatesqlMouseClicked(MouseEvent e) {
        String sqlStr = sql.getText();
        if(StringUtils.isEmpty(sqlStr)){
            alert("sql不能为空");
            return;
        }
        if(StringUtils.isEmpty(params.getText())){
            alert("参数不能为空");
            return;
        }
        String replace = SqlReplaceUtil.replace(sqlStr, params.getText());

        if(isFh.isSelected()){
            replace = replace + ";";
        }
        sqlresult.setText(replace);
//        System.out.println(sql.getText());
    }

    private void alert(String msg) {
        JOptionPane.showMessageDialog(null, msg, "提示", JOptionPane.INFORMATION_MESSAGE);
    }


    //sql带()的执行
    private void formateinButtonMouseClicked(MouseEvent e) {
        String replaceStr = paraminArea.getText();
        if (org.apache.commons.lang3.StringUtils.isBlank(replaceStr)) {
//            return getFailRtn("要替换的内容必须传");
            alert("要替换的内容必须传");
            return;
        }
        String replace = SqlReplaceUtil.replaceStr(replaceStr);
        if(formateInkh.isSelected()){
            replace = "("+replace + ")";
        }
        formateinResultArea.setText(replace);
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        scrollPane1 = new JScrollPane();
        sql = new JTextArea();
        label2 = new JLabel();
        scrollPane2 = new JScrollPane();
        params = new JTextArea();
        formatesql = new JButton();
        isFh = new JCheckBox();
        scrollPane5 = new JScrollPane();
        sqlresult = new JTextArea();
        label3 = new JLabel();
        scrollPane3 = new JScrollPane();
        paraminArea = new JTextArea();
        formateinButton = new JButton();
        formateInkh = new JCheckBox();
        scrollPane4 = new JScrollPane();
        formateinResultArea = new JTextArea();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(null);

            //======== contentPanel ========
            {
                contentPanel.setLayout(null);

                //---- label1 ----
                label1.setText("\u8f93\u5165sql");
                contentPanel.add(label1);
                label1.setBounds(5, 0, 110, label1.getPreferredSize().height);

                //======== scrollPane1 ========
                {

                    //---- sql ----
                    sql.setWrapStyleWord(true);
                    sql.setLineWrap(true);
                    scrollPane1.setViewportView(sql);
                }
                contentPanel.add(scrollPane1);
                scrollPane1.setBounds(5, 20, 555, 250);

                //---- label2 ----
                label2.setText("\u8f93\u5165\u53c2\u6570\uff1a");
                contentPanel.add(label2);
                label2.setBounds(new Rectangle(new Point(10, 290), label2.getPreferredSize()));

                //======== scrollPane2 ========
                {

                    //---- params ----
                    params.setWrapStyleWord(true);
                    params.setLineWrap(true);
                    scrollPane2.setViewportView(params);
                }
                contentPanel.add(scrollPane2);
                scrollPane2.setBounds(0, 320, 560, 70);

                //---- formatesql ----
                formatesql.setText("\u683c\u5f0f\u5316");
                formatesql.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        formatesqlMouseClicked(e);
                    }
                });
                contentPanel.add(formatesql);
                formatesql.setBounds(new Rectangle(new Point(5, 395), formatesql.getPreferredSize()));

                //---- isFh ----
                isFh.setText("\u7ed3\u679c\u5e26;");
                isFh.setSelected(true);
                contentPanel.add(isFh);
                isFh.setBounds(new Rectangle(new Point(125, 400), isFh.getPreferredSize()));

                //======== scrollPane5 ========
                {

                    //---- sqlresult ----
                    sqlresult.setWrapStyleWord(true);
                    sqlresult.setLineWrap(true);
                    scrollPane5.setViewportView(sqlresult);
                }
                contentPanel.add(scrollPane5);
                scrollPane5.setBounds(0, 435, 560, 180);

                //---- label3 ----
                label3.setText("\u8f93\u5165\u5185\u5bb9");
                contentPanel.add(label3);
                label3.setBounds(new Rectangle(new Point(645, 5), label3.getPreferredSize()));

                //======== scrollPane3 ========
                {

                    //---- paraminArea ----
                    paraminArea.setLineWrap(true);
                    paraminArea.setWrapStyleWord(true);
                    scrollPane3.setViewportView(paraminArea);
                }
                contentPanel.add(scrollPane3);
                scrollPane3.setBounds(645, 25, 585, 245);

                //---- formateinButton ----
                formateinButton.setText("\u683c\u5f0f\u5316");
                formateinButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        formateinButtonMouseClicked(e);
                    }
                });
                contentPanel.add(formateinButton);
                formateinButton.setBounds(new Rectangle(new Point(645, 290), formateinButton.getPreferredSize()));

                //---- formateInkh ----
                formateInkh.setText("\u7ed3\u679c\u5e26()");
                formateInkh.setSelected(true);
                contentPanel.add(formateInkh);
                formateInkh.setBounds(new Rectangle(new Point(770, 290), formateInkh.getPreferredSize()));

                //======== scrollPane4 ========
                {

                    //---- formateinResultArea ----
                    formateinResultArea.setWrapStyleWord(true);
                    formateinResultArea.setLineWrap(true);
                    scrollPane4.setViewportView(formateinResultArea);
                }
                contentPanel.add(scrollPane4);
                scrollPane4.setBounds(645, 340, 590, 260);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < contentPanel.getComponentCount(); i++) {
                        Rectangle bounds = contentPanel.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = contentPanel.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    contentPanel.setMinimumSize(preferredSize);
                    contentPanel.setPreferredSize(preferredSize);
                }
            }
            dialogPane.add(contentPanel);
            contentPanel.setBounds(12, 12, 1249, contentPanel.getPreferredSize().height);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < dialogPane.getComponentCount(); i++) {
                    Rectangle bounds = dialogPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = dialogPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                dialogPane.setMinimumSize(preferredSize);
                dialogPane.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JScrollPane scrollPane1;
    private JTextArea sql;
    private JLabel label2;
    private JScrollPane scrollPane2;
    private JTextArea params;
    private JButton formatesql;
    private JCheckBox isFh;
    private JScrollPane scrollPane5;
    private JTextArea sqlresult;
    private JLabel label3;
    private JScrollPane scrollPane3;
    private JTextArea paraminArea;
    private JButton formateinButton;
    private JCheckBox formateInkh;
    private JScrollPane scrollPane4;
    private JTextArea formateinResultArea;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        new sql().setVisible(true);
    }
}
