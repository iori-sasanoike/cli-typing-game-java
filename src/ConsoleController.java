
//ConsoleController
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.Timer;

public class ConsoleController implements ActionListener {

	private final static int DELAY = 1000;
	private Model model;
	private Timer timer;
	private ConsoleView view;

	public ConsoleController(Model m, ConsoleView view) {
		this.model = m;
		this.view = view;
		timer = new Timer(DELAY, this);
	}

	public void run() throws IOException {
		timer.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while ((line = reader.readLine()) != null)
			model.process(line);
	}

	public void start() {
		timer.start();
		// 最初の1回目の画面描画を行う
		view.update();
		try {
			// 標準入力（read_key.sh からの文字流し込み）を受け付ける
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line;

			// 文字が入ってくるたびに無限ループ
			while ((line = reader.readLine()) != null) {
				// Modelに入力された文字を通知する
				model.process(line);

				// 状態が変わったらViewを再描画する
				view.update();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		model.process("TIME_ELAPSED");
		view.update();
	}
}