package plugin.oremininggame.mapper.data.data;

import lombok.Getter;
import lombok.Setter;

/**
 * ゲームを実行する際のplayer情報を扱うオブジェクト
 * プレイヤー名、合計点数、日時情報を持つ
 */
@Getter
@Setter

public class MiningPlayer {
 private String playerName;
 private int score;

}
