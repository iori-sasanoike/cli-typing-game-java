
//Model
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Model {
	private ConsoleView view;
	private ConsoleController controller;
	private String typedLine = "";
	private Dictionary dictionary;
	private Word currentWord;
	private int hp = 5;
	public static final int TIME = 10;
	private int time = TIME;
	private boolean isShowingResult = false; // 結果（正答）を表示中かどうかのフラグ
	private int resultDisplayTime; // 結果を表示する残り秒数
	private String lastCorrectSpelling = ""; // 直前の正しいスペル
	private String lastJudgeResult = ""; // "CORRECT" または "MISS" を保持
	private boolean isGameOver = false;
	private int combo = 0; // 現在の連続正解数
	private int maxCombo = 0; // 最大連続正解数
	private int solvedCount = 0; // 正解した合計問題数
	public static final int REQUIRED_SOLVED_COUNT = 15;// クリアになるための正答数
	public static final int STATE_START = 0;
	public static final int STATE_COUNTDOWN = 1;
	public static final int STATE_PLAYING = 2;
	public static final int STATE_GAMEOVER = 3;
	public static final int STATE_CLEAR = 4;
	private int status = STATE_START; // 初期状態はSTART画面
	private int countdownTime = 3; // カウントダウン用の秒数
	private List<Word> usedWords = new ArrayList<>();
	private boolean isCorrectWordQuestion = false;

	public Model() {
		view = new ConsoleView(this);
		controller = new ConsoleController(this, view);
		this.dictionary = new Dictionary();
//		nextQuestion();
	}

	public void process(String line) {
		// タイマーからの通知は状態に関わらずTimer()に流す
		if ("TIME_ELAPSED".equals(line)) {
			Timer();
			return;
		}
		// スタート画面のとき：何かキーが押されたらカウントダウンへ移行
		if (status == STATE_START) {
			status = STATE_COUNTDOWN;
			countdownTime = 3; // 3秒からスタート
			return;
		}
		// カウントダウン中のとき：キー入力を無視
		if (status == STATE_COUNTDOWN) {
			return;
		}
		// ゲームオーバーのとき：キー入力を無視
		if (isGameOver || status == STATE_GAMEOVER || status == STATE_CLEAR) {
			return;
		}
		// プレイ中のとき（既存の入力処理）
		if (isShowingResult) {
			return;
		}
		if (line.equals("")) {
			checkAnswer();
			return;
		}
		char bsCode = (char) 127;
		if (line.equals(String.valueOf(bsCode)) || line.equals("\b")) {
			if (typedLine.length() > 0) {
				typedLine = typedLine.substring(0, typedLine.length() - 1);
			}
			return;
		}
		typedLine += line;
	}

	private void checkAnswer() {
		lastCorrectSpelling = currentWord.getCorrectSpelling();
		if (isCorrectWordQuestion) {
			// 正しいスペル出題のときは、何も入力せずにEnter（""）なら正解
			if (typedLine.equals("")) {
				onCorrect();
			} else {
				onMiss();
			}
		} else {
			// 通常の間違ったスペル出題のときは、正しいスペルを入力していれば正解
			if (currentWord.getCorrectSpelling().equals(typedLine)) {
				onCorrect();
			} else {
				onMiss();
			}
		}
		// 即座に次の問題に行かず、結果表示モードに入る
		isShowingResult = true;
		resultDisplayTime = 3;
		typedLine = "";
	}

	// 不正解時の処理
	public void onMiss() {
		hp--;
		lastJudgeResult = "MISS";
		combo = 0;
		if (hp <= 0) {
			hp = 0; // マイナス表示を防ぐ
			isGameOver = true;
			status = STATE_GAMEOVER;
		}
	}

	// 正解時の処理
	public void onCorrect() {
		lastJudgeResult = "CORRECT";
		combo++;
		solvedCount++;
		if (combo > maxCombo) {
			maxCombo = combo;
		}
		if (solvedCount >= REQUIRED_SOLVED_COUNT) {
			status = STATE_CLEAR;
		}
	}

	public void Timer() {
		// カウントダウン状態のとき：1秒ごとに数字を減らす
		if (status == STATE_COUNTDOWN) {
			countdownTime--;
			if (countdownTime <= 0) {
				status = STATE_PLAYING;
				nextQuestion();
				time = TIME;
			}
			return;
		}
		// スタート画面またはゲームオーバー時はタイマーを動かさない
		if (status == STATE_START || isGameOver || status == STATE_GAMEOVER) {
			return;
		}
		// プレイ中のとき
		if (isShowingResult) {
			resultDisplayTime--;
			if (resultDisplayTime <= 0) {
				isShowingResult = false;
				nextQuestion();
				time = TIME;
			}
			return;
		}

		time--;
		if (time <= 0) {
			lastCorrectSpelling = currentWord.getCorrectSpelling();
			onMiss();
			if (!isGameOver) {
				isShowingResult = true;
				resultDisplayTime = 4;
				typedLine = "";
			}
			return;
		}
	}

	// 打ち込んでいる文字列のゲッター
	public String getTypedInput() {
		return typedLine;
	}

	// 新しい（ランダムな）問題を選んでセットするメソッド
	public void nextQuestion() {
		Word newWord = null;
		while (newWord == null) {
			Word candidate = dictionary.getRandomWord();
			if (!usedWords.contains(candidate)) {
				newWord = candidate;
			}
		}
		this.currentWord = newWord;
		usedWords.add(newWord);
		isCorrectWordQuestion = (Math.random() < 0.20);
	}

	// 現在の問題（Word）をViewなどから取得するためのゲッター
	public Word getCurrentWord() {
		return currentWord;
	}

	// hpを取得するゲッター
	public int gethp() {
		return hp;
	}

	// timeを取得するゲッター
	public int getTime() {
		return time;
	}

	public boolean isShowingResult() {
		return isShowingResult;
	}

	public String getLastCorrectSpelling() {
		return lastCorrectSpelling;
	}

	public String getLastJudgeResult() {
		return lastJudgeResult;
	}

	public int getResultDisplayTime() {
		return resultDisplayTime;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public int getCombo() {
		return combo;
	}

	public int getMaxCombo() {
		return maxCombo;
	}

	public int getStatus() {
		return status;
	}

	public int getCountdownTime() {
		return countdownTime;
	}

	public int getSolvedCount() {
		return solvedCount;
	}

	public boolean isCorrectWordQuestion() {
		return isCorrectWordQuestion;
	}

	public void start() {
		controller.start();
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		Model model = new Model();
		model.start();
	}
}
