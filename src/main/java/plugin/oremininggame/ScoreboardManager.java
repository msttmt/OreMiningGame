package plugin.oremininggame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

  public void setupScoreboard(Player player) {
    // スコアボードの作成
    Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    // オブジェクトの作成
    Objective objective = scoreboard.registerNewObjective("itemScores", "dummy", "得点");
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    // スコアの設定
    setScore(objective, "現在のスコア", 0); // プレイヤーの現在のスコアは0で初期化
    setScore(objective, ChatColor.AQUA +"ダイヤモンド", 100);
    setScore(objective, ChatColor.GRAY +"鉄鉱石", 1);
    setScore(objective, ChatColor.DARK_GRAY +"石炭", 1);
    setScore(objective, ChatColor.GOLD +"銅鉱石", 10);
    setScore(objective, ChatColor.YELLOW +"金鉱石", 20);
    setScore(objective, ChatColor.BLUE +"ラピスラズリ", 30);
    setScore(objective, ChatColor.GREEN +"エメラルド", 80);
    setScore(objective, ChatColor.RED +"レッドストーン", 5);

    // プレイヤーにスコアボードを設定
    player.setScoreboard(scoreboard);
  }

  private void setScore(Objective objective, String entry, int scoreValue) {
    Score score = objective.getScore(entry);
    score.setScore(scoreValue);
  }

  // プレイヤーの現在のスコアを更新するメソッドの例
  public void updateCurrentScore(Player player, int currentScore) {
    Scoreboard scoreboard = player.getScoreboard();
    Objective objective = scoreboard.getObjective("itemScores");

    if (objective != null) {
      // 現在のスコアを更新
      setScore(objective, "現在のスコア", currentScore);
    }
  }

  public void clearScore(Player player){
    Scoreboard newScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    player.setScoreboard(newScoreboard);
  }
}