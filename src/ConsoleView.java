//ConsoleView
public class ConsoleView {
	public final static int WIDTH = 80;
	public final static int HEIGHT = 24;
	private char[][] screen;
	private Model model;

	// コンストラクタ、画面の縦横幅を設定
	public ConsoleView(Model model) {
		this.model = model;
		screen = new char[HEIGHT][WIDTH];
		clear();
	}

	// 画面のクリア
	public void clear() {
		for (int i = 0; i < screen.length; i++) {
			for (int j = 0; j < screen[i].length; j++)
				screen[i][j] = ' ';
		}
	}

	// 配列screenに格納されている文字を出力
	public void paint() {
		StringBuffer s = new StringBuffer();
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++)
				s.append(screen[y][x]);
			s.append("\n");
		}
		System.out.println(s);
	}

	// 一文字配置
	void put(char c, int x, int y) {
		if (x >= 0 && x < screen[0].length && y >= 0 && y < screen.length) {
			screen[y][x] = c;
		}
	}

	// 指定の座標から右に向かって文字列sを一文字ずつput()で書き込む
	void drawString(String s, int x, int y) {
		for (int i = 0; i < s.length(); i++)
			put(s.charAt(i), x + i, y);
	}

	// 四角の描画
	void drawRect(char c, int x, int y, int w, int h) {
		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++)
				if (i == 0 || i == w - 1 || j == 0 || j == h - 1)
					put(c, x + i, y + j);
	}

	// 枠付き文字の描写
	void drawFramedString(String s, char c, int x, int y) {
		drawRect(c, x - 1, y - 1, s.length() + 2, 3);
		drawString(s, x, y);
	}

	// モデルに変更があったときに画面に反映
	public void update() {
		// 画面クリア
		clear();
		int status = model.getStatus();

		// 1. スタート画面の描画
		if (status == Model.STATE_START) {
//			drawRect('=', 10, 8, 65, 13);
			drawString("=== TYPING GAME ===", 30, 8);
			drawString("[ HOW TO PLAY ]", 32, 12);
			drawString("- 間違った英単語を10秒以内に正しく直そう！", 18, 13);
			drawString("- 最初から正しい単語の時は、何も打たずにENTER！", 18, 14);
			drawString("- " + Model.REQUIRED_SOLVED_COUNT + " 問正解でゲームクリア！", 18, 15);
			drawString("Press Enter Key to Start !!", 26, 19);
			paint();
			return;
		}

		// 2. カウントダウン画面の描画
		if (status == Model.STATE_COUNTDOWN) {
			drawString("READY......" + model.getCountdownTime(), 35, 11);
			paint();
			return;
		}
		// 3. ゲームオーバー画面の描画
		if (status == Model.STATE_GAMEOVER || model.isGameOver()) {
//			drawString("HP: " + model.gethp(), 25, 2);
//			drawString("TIME: --", 35, 2);
//			drawString("COMBO: --", 48, 2);
			drawRect('=', 22, 8, 36, 6);
			drawString("GAME OVER !!", 33, 10);
			drawString("Max Combo : " + model.getMaxCombo() + "", 33, 11);
			drawString("Please Ctrl+C to exit.", 29, 17);
			paint();
			return;
		}
		// 4. ゲームクリア画面の描画
		if (status == Model.STATE_CLEAR) {
			drawRect('=', 22, 8, 36, 7);
			drawString("☆★ GAME CLEAR !! ★☆", 30, 10);
			drawString("Max Combo : " + model.getMaxCombo() + "", 33, 11);
			drawString("HP : " + model.gethp() + "", 36, 12);
			drawString("Please Ctrl+C to exit.", 29, 17);
			paint();
			return;
		}
		// 5. 通常のゲームプレイ画面の描画
		drawString("HP: " + model.gethp(), 15, 2);
		drawString("SOLVED: " + model.getSolvedCount() + "/" + Model.REQUIRED_SOLVED_COUNT, 28, 2);
		drawString("COMBO: " + model.getCombo(), 48, 2);
		String meter = "";
		for (int i = 0; i < model.getTime(); i++) {
			meter += "■";
		}
		if (model.isShowingResult()) {
			drawString("TIME: --", 25, 4);
			if ("CORRECT".equals(model.getLastJudgeResult())) {
				drawString("== CORRECT !! ==", 30, 9);
			} else {
				drawString("== MISS !! ==", 30, 9);
			}
			drawString("Correct Answer: " + model.getLastCorrectSpelling(), 25, 10);
			drawString("Next question in " + model.getResultDisplayTime() + " seconds...", 25, 13);
		} else {
			drawString("TIME: " + model.getTime(), 25, 4);
			drawString(meter, 34, 4);
			Word currentWord = model.getCurrentWord();
			if (currentWord != null) {
				if (model.isCorrectWordQuestion()) {
					// 正しい単語そのまま
					drawString("Question: " + currentWord.getCorrectSpelling(), 25, 10);
				} else {
					// 通常の間違った単語を出す
					drawString("Question: " + currentWord.getMisspelledSpelling(), 25, 10);
				}
			}
			drawString("Your Input: " + model.getTypedInput(), 25, 13);
		}
		paint();
	}
}