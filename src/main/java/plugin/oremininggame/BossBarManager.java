package plugin.oremininggame;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarManager {

  private final BossBar bossBar;

  public BossBarManager() {
    bossBar = Bukkit.createBossBar("残り時間: ", BarColor.BLUE, BarStyle.SOLID);
  }

  public void showBossBar(Player player) {
    bossBar.addPlayer(player);
  }

  public void updateBossBar(int time) {
    bossBar.setProgress((double) time / 120);
    bossBar.setTitle("残り時間: " + time + "秒");
  }

  public void hideBossBar(Player player) {
    bossBar.removePlayer(player);
  }
}