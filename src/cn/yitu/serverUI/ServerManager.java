package cn.yitu.serverUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import cn.yitu.tcp.ServiceTcp;
import cn.yitu.test.serverStart;

public class ServerManager extends JFrame implements ActionListener {

	ServiceTcp serviceTcp = new ServiceTcp(8080);

	JButton btnOpen = new JButton("开启服务器");
	JButton btnClose = new JButton("关闭服务器");
	JPanel southPanel;
	public static JTextArea textArea;// 消息显示区
	JScrollPane scroll;
	JFrame frame;

	public ServerManager() {
		// TODO Auto-generated constructor stub
		frame=new JFrame("服务器");
		btnClose.setEnabled(false);
		JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		jPanel.add(btnOpen);
		jPanel.add(btnClose);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setForeground(Color.blue);
		scroll = new JScrollPane(textArea);
		scroll.setBorder(new TitledBorder("消息显示区"));
		southPanel=new JPanel(new BorderLayout());
		southPanel.add(scroll,"Center");
		btnOpen.addActionListener(this);
		btnClose.addActionListener(this);
		frame.add(jPanel, "North");
		frame.add(southPanel,"Center");
		

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		//setTitle("共享位置服务器");
		frame.setSize(500, 400);
		//setVisible(true);
		int screen_width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int screen_height=Toolkit.getDefaultToolkit().getScreenSize().height;
		frame.setLocation((screen_width-frame.getWidth())/2, (screen_height - frame.getHeight()) / 2);
		frame.setVisible(true);
	}

	public static void main(String args[]) {
		//JFrame.setDefaultLookAndFeelDecorated(true);
		new ServerManager();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnOpen) {
			btnOpen.setEnabled(false);
			ServiceTcp serviceTcp = new ServiceTcp(8080);
			serviceTcp.start();
			btnClose.setEnabled(true);
		}
		if (e.getSource() == btnClose) {
			dispose();
			System.exit(0);

		}
	}

}
