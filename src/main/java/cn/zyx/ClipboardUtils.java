package cn.zyx;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * @Author : ZhangYunXin
 * @CreateDate : 2022/12/16 17:32
 * @Description : 剪切板工具类
 */
public class ClipboardUtils {

    /**
     * 把文本设置到剪贴板（复制）
     *
     * @param text
     * @param message
     * @param title
     * @param show
     */
    public static void setClipboardString(String text, String message, String title, boolean show) {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        Transferable trans = new StringSelection(text);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
        // 提示
        if (show)
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);
    }
}
