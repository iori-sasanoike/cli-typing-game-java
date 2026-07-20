//Dictionary

import java.util.Random;

public class Dictionary {
	private Word[] words;
	private Random rand = new Random();

	public Dictionary() {
		// 問題データ
		words = new Word[] {
				// 【文字数不変・入れ替えなし・特定の文字が別の文字に化けているミスのみ（計30個）】
				// (前半15個：主に子音の置き換わりミス / 後半15個：主に母音の置き換わりミス)
				new Word("excited", "exsited"), // 1. c が s に化ける (子音・7文字)
				new Word("station", "stasion"), // 2. t が s に化ける (子音・7文字)
				new Word("pencil", "pencel"), // 3. i が e に化ける (母音・6文字)
				new Word("because", "becouse"), // 4. a が o に化ける (母音・7文字)
				new Word("calendar", "calender"), // 5. a が e に化ける (母音・8文字)
				new Word("computer", "computor"), // 6. e が o に化ける (母音・8文字)
				new Word("popular", "populer"), // 7. a が e に化ける (母音・7文字)
				new Word("vegetable", "vegitable"), // 8. e が i に化ける (母音・9文字)
				new Word("doctor", "docter"), // 9. o が e に化ける (母音・6文字)
				new Word("grammar", "grammer"), // 10. a が e に化ける (母音・7文字)
				new Word("orange", "orenge"), // 11. a が e に化ける (母音・6文字)
				new Word("yellow", "yallow"), // 12. e が a に化ける (母音・6文字)
				new Word("winter", "wintar"), // 13. e が a に化ける (母音・6文字)
				new Word("summer", "summar"), // 14. e が a に化ける (母音・6文字)
				new Word("family", "femily"), // 15. a が e に化ける (母音・6文字)
				new Word("parent", "parant"), // 16. e が a に化ける (母音・6文字)
				new Word("street", "streat"), // 17. e が a に化ける (母音・6文字)
				new Word("market", "markat"), // 18. e が a に化ける (6文字)
				new Word("second", "sacond"), // 19. e が a に化ける (母音・6文字)
				new Word("guitar", "giitar"), // 20. u が i に化ける (母音・6文字)
				new Word("camera", "camara"), // 21. e が a に化ける (母音・6文字)
				new Word("animal", "animel"), // 22. a が e に化ける (母音・6文字)
				new Word("hospital", "hospitel"), // 23. a が e に化ける (母音・8文字)
				new Word("umbrella", "ambrella"), // 24. u が a に化ける (母音・8文字)
				new Word("jacket", "jackat"), // 25. e が a に化ける (母音・6文字)
				new Word("advice", "advise"), // 26. c が s に化ける (子音・6文字)
				new Word("music", "musik"), // 27. c が k に化ける (子音・5文字)
				new Word("kitchen", "kitcken"), // 28. h が c に化ける (子音・7文字)
				new Word("subject", "subzect"), // 29. j が z に化ける (子音・7文字)
				new Word("perfect", "perfekt") // 30. c が k に化ける (子音・7文字)
		};
	}

	// ランダムに単語（問題）を1つ選んで返すメソッド
	public Word getRandomWord() {
		int index = rand.nextInt(words.length);
		return words[index];
	}
}