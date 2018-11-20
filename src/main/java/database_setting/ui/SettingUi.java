package database_setting.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import database_setting.service.ExportService;
import database_setting.service.ImportService;
import database_setting.service.InitService;

@SuppressWarnings("serial")
public class SettingUi extends JFrame implements ActionListener {
	private JPanel contentPane;
	private List<String> btnNames;
	private InitService initService;
	private ImportService importService;
	private ExportService exportService;
	
	public SettingUi() {
		initService = new InitService();
		importService = new ImportService();
		exportService = new ExportService();
		
		btnNames = Arrays.asList("초기화", "백업", "복원");
		
		setTitle("DBSetting");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 10, 0));
		
		JPanel btnPannel = new JPanel();
		btnPannel.setBorder(new TitledBorder(null, "Coffee 관리지 설정", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(btnPannel);
		btnPannel.setLayout(new GridLayout(1, 0, 0, 0));
		
		for(String btnTitle : btnNames) {
			JButton btn = new JButton(btnTitle);
			btn.addActionListener(this);
			btnPannel.add(btn);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
/*		String filePath = filePath();
		LogUtil.prnLog(filePath);*/
		switch(e.getActionCommand()) {
			case "초기화"	:
				initService.service("db.properties", "db2.properties");
				break;
			case "백업":
				exportService.service("db2.properties");
				break;
			case "복원":
				importService.service("db2.properties");
				break;
		}
		JOptionPane.showMessageDialog(null, e.getActionCommand() + " 완료");
	}

	public String filePath() {
		JFileChooser chooser = new JFileChooser();
//		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//디렉터리 선택
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("import file", "txt");
		chooser.setFileFilter(filter);
		
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int ret = chooser.showOpenDialog(null);
		if (ret != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다", "경고", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		return chooser.getSelectedFile().getPath();
	}
}
