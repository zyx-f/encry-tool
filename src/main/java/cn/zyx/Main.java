package cn.zyx;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @Author : ZhangYunXin
 * @CreateDate : 2023/8/22 9:41
 * @Description : 程序入口
 */
public class Main extends javax.swing.JFrame {
    public static void main(String[] args) {
        try {
            // 设置本属性将改变窗口边框样式定义
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
            // 隐藏设置按钮
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Main().initComponents();
    }

    private void initComponents() {
        this.setTitle("文件摘要计算工具（开发者：zyx，版本：" + getVersion() + "）");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setSize(new java.awt.Dimension(638, 200));

        JComboBox<String> jComboBox = new JComboBox<>();
        jComboBox.setSize(new java.awt.Dimension(100, 30));
        jComboBox.addItem("md5");
        jComboBox.addItem("sha1");
        jComboBox.addItem("sha256");

        this.add(jComboBox);

        String text = "<html>" +
                "<body style='text-align:center;font-family:fangsong;'>" +
                "<span style='font-size:28;'>请将要计算的文件拖动到此处</span>" +
                "<br>" +
                "<br>" +
                "<span style='font-size:18;'></span>" +
                "</body>" +
                "</html>";
        javax.swing.JLabel jLabel = new javax.swing.JLabel(text, JLabel.CENTER);
        jLabel.setSize(new java.awt.Dimension(638, 30));
        // 背景色
        // jLabel.setOpaque(true);
        // jLabel.setBackground(Color.BLACK);

        this.add(jLabel);
        // 禁止调整窗口大小
        this.setResizable(false);
        // 设置居中
        this.setLocationRelativeTo(null);

        DropTargetListener listener = new DropTargetListener() {

            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    jLabel.setText(text);
                    // 接收拖拽目标数据
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    try {
                        Object transferData = dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        List<String> array = JSONUtil.toList(JSONUtil.toJsonStr(transferData), String.class);
                        if (array.size() > 1) {
                            JOptionPane.showMessageDialog(null, "只支持拖拽单文件。", "提示", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        String way = (String) jComboBox.getSelectedItem();
                        way = way == null ? "" : way;
                        for (String path : array) {
                            String encry;
                            File file = new File(path);
                            switch (way) {
                                case "md5":
                                    encry = DigestUtil.md5Hex(file);
                                    break;
                                case "sha1":
                                    encry = DigestUtil.sha1Hex(file);
                                    break;
                                case "sha256":
                                    encry = DigestUtil.sha256Hex(file);
                                    break;
                                default:
                                    encry = "";
                                    break;
                            }
                            jLabel.setText(text.replace("<span style='font-size:18;'></span>",
                                    "<span style='font-size:18;'>" + file.getName() + "</span>"
                                            + "<br/><br/>"
                                            + "<span style='font-size:18;'>" + encry + "</span>"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    dtde.rejectDrop();//否则拒绝拖拽来的数据
                }
            }
        };
        new DropTarget(jLabel, DnDConstants.ACTION_COPY_OR_MOVE, listener, true);

        setVisible(true);
    }

    private String getVersion() {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("git.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties.get("git.total.commit.count").toString();
        } catch (Exception e) {
            System.err.println("获取Version失败：" + e.getMessage());
        }
        return null;
    }
}
