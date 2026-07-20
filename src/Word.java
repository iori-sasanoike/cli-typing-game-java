//Word

public class Word {
	private String correct;
	private String miss;

	// コンストラクタ
	public Word(String c, String m) {
		this.correct = c;
		this.miss = m;
	}

	// 正しいスペルを取得するゲッター
	public String getCorrectSpelling() {
		return correct;
	}

	// ミススペルを取得するゲッター
	public String getMisspelledSpelling() {
		return miss;
	}
}
