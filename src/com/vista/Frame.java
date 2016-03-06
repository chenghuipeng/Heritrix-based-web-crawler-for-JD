package com.vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.archive.crawler.event.CrawlStatusListener;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.settings.XMLSettingsHandler;

import test.BeyesClassification;

public class Frame extends JFrame
{

	private JPanel contentPane;
	public static JTable table;
	public static DefaultTableModel tmodel;
	public static Vector<CrawlResult> crawlResults = new Vector<CrawlResult>();
	public static Vector<String> url = new Vector<String>();
	public static JTextField textseed = new JTextField();

	public static BeyesClassification bayes = new BeyesClassification();
	public static DBhelper dBhelper = new DBhelper();

	private Object[][] data = null;
	private Object[] column = { "标题", "URL", "类别", "概率", "正文" };

	String orderFile = "E:\\Documents\\Projects\\eclip luna\\heritrix\\jobs\\new\\order.xml";// order.xml�ļ�·��
	File file = null; // order.xml�ļ�

	CrawlStatusListener listener = null;// ������
	XMLSettingsHandler handler = null; // ��ȡorder.xml�ļ��Ĵ�����
	CrawlController controller = null; // Heritrix�Ŀ�����
	private JTextField txt_time;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame()
	{
		crawlthread ct = new crawlthread();
		final Thread h1 = new Thread(ct);

		setTitle("\u4E3B\u9898\u722C\u866B");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 804, 331);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel toppanel = new JPanel();
		contentPane.add(toppanel, BorderLayout.NORTH);
		toppanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel lblurl = new JLabel("爬取到的页面数：");
		toppanel.add(lblurl);

		textseed.setEditable(false);
		toppanel.add(textseed);
		textseed.setColumns(10);

		JButton btnstart = new JButton("开始");
		btnstart.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				h1.start();

			}
		});

		JLabel lblms = new JLabel("训练用时（ms）：");
		toppanel.add(lblms);

		txt_time = new JTextField();
		txt_time.setEditable(false);
		toppanel.add(txt_time);
		txt_time.setColumns(10);
		toppanel.add(btnstart);

		JButton btnStop = new JButton("停止");
		btnStop.addMouseListener(new MouseAdapter()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e)
			{

			}
		});
		toppanel.add(btnStop);

		JButton btnSettrainpath = new JButton("设置训练集");
		btnSettrainpath.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int n = chooser.showOpenDialog(getContentPane());
				if (n == JFileChooser.APPROVE_OPTION)
				{
					bayes.setTrainPath(chooser.getSelectedFile().getPath());
					bayes.train();
					System.out.println(bayes.getTrainingTime());
					txt_time.setText("" + bayes.getTrainingTime());
				}
			}
		});
		toppanel.add(btnSettrainpath);

		tmodel = new DefaultTableModel(data, column);

		table = new JTable(tmodel);

		JScrollPane jscrollpane = new JScrollPane(table);
		contentPane.add(jscrollpane, BorderLayout.CENTER);

	}

}
