package cn.edu.zucc.booklib.ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FrmFileChoose extends JFrame{

public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmFileChoose frame = new FrmFileChoose();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static String text;
    public FrmFileChoose() {
        JFrame jf = new JFrame("�ļ�ѡȡ����");
        jf.setSize(400, 250);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();

        final JTextArea msgTextArea = new JTextArea(10, 30);
        msgTextArea.setLineWrap(true);
        panel.add(msgTextArea);

        JButton openBtn = new JButton("·������");
        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileOpenDialog(jf, msgTextArea);
            }
        });
        panel.add(openBtn);

        JButton saveBtn = new JButton("ȷ��");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(text);
                jf.setVisible(false);
            }
        });
        panel.add(saveBtn);

        jf.setContentPane(panel);
        jf.setVisible(true);
    }

    private static void showFileOpenDialog(Component parent, JTextArea msgTextArea) {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("."));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);

        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip(*.zip, *.rar)", "zip", "rar"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));

        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            text = file.getAbsolutePath();
            msgTextArea.append("��ѡȡ��:-------" + text + "-------����Ƭ���Ѹ��Ƶ������壡\n\n");
            
        }
    }

}