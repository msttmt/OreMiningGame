package plugin.oremininggame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import plugin.oremininggame.data.PlayerScore;

public class MiningStartCommand extends BaseCommand implements Listener {

  private Main main;
  private final List<PlayerScore> playerScoreList = new ArrayList<>();
  private int nowGameTime;
  private int idleTime;
  private boolean isPointCountEnd = true;
  private BossBarManager bossBarManager;
  private final ScoreboardManager scoreboardManager = new ScoreboardManager();
  private final String LIST = "list";

  public MiningStartCommand(Main main) {
    this.main = main;
  }

  @Override
  public boolean onMiningPlayerCommand(Player player) {
    PlayerScore nowPlayer = getPlayerScore(player);
    player.setHealth(20);
    player.setFoodLevel(20);
    idleTimer(player); //待機時間カウント
    createStage.setLocation(player, player.getWorld()); //playerの縦横30の距離で採掘場の生成

    player.getInventory().setItemInMainHand(new ItemStack(Material.NETHERITE_PICKAXE));
    player.sendMessage("ピッケルを渡しました。鉱石の塊を採掘して高得点を狙ってください。");

    scoreboardManager.setupScoreboard(player);
    timeScheduler(player, nowPlayer);
    return true;
  }



  private void timeScheduler(Player player, PlayerScore nowPlayer) {
    nowGameTime = 60; //初期化
    bossBarManager = new BossBarManager();
    bossBarManager.showBossBar(player);
    Bukkit.getScheduler().runTaskTimer(main, Runnable -> {
      if(!isPointCountEnd) {
        bossBarManager.updateBossBar(nowGameTime);
        if (nowGameTime == 0) {
          Runnable.cancel();
          scoreboardManager.updateCurrentScore(player, nowPlayer.getScore());
          player.sendTitle("Score " + nowPlayer.getScore() + "点","ゲーム終了～！");
          nowPlayer.setScore(0);
          bossBarManager.hideBossBar(player);
          isPointCountEnd = true;
          scoreboardManager.clearScore(player);
          return;
        }else if(nowGameTime < 6 && nowGameTime != 0) {
          player.sendTitle( " " + nowGameTime + " ","");
        }
        nowGameTime--;
      }
      }, 0L, 20L);  //DAY17 タイムランナー
  }

  /**
   * カウントダウン時間の設定です。5からカウントダウンします。
   */
  private void idleTimer(Player player) {
    idleTime = 6; //初期化
    Bukkit.getScheduler().runTaskTimer(main, Runnable -> {
      if (idleTime == 0) {
        Runnable.cancel();
        player.sendTitle("Game Start!", "", 10, 30, 10);
        isPointCountEnd = false; //点数加算フラグ
      } else if (idleTime == 6) {
        idleTime--;
      } else {
        player.sendTitle( " " + idleTime + " ","");
        idleTime--;
      }
    }, 0, 20);

  }


  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    Player player = e.getPlayer();
    Block block = e.getBlock();
    if (Objects.isNull(player) || playerScoreList.isEmpty() || isPointCountEnd) {
      return;
    }

    playerScoreList.stream().filter(p -> p.getPlayerName().equals(player.getName())).findFirst()
        .ifPresent(p -> {
          int point = switch (block.getType()) {
            case DIAMOND_ORE -> 100;
            case IRON_ORE, COAL_ORE -> 1;
            case COPPER_ORE -> 10;
            case GOLD_ORE -> 20;
            case LAPIS_ORE -> 30;
            case EMERALD_ORE -> 80;
            case REDSTONE_ORE -> 5;
            default -> 0;
          };
          p.setScore(p.getScore() + point);
          if(point != 0) {
            scoreboardManager.updateCurrentScore(player, p.getScore());
          }
        });
  }



  /**
   * 現在実行しているプレイヤーのスコア情報を取得
   *
   * @param player 　コマンドを実行したプレイヤー
   * @return 現在実行しているプレイヤーのスコア加算
   */

  private PlayerScore getPlayerScore(Player player) {
    if (playerScoreList.isEmpty()) {
      return addNewPlayer(player);
    } else {
      for (PlayerScore playerScore : playerScoreList) {
        if (!playerScore.getPlayerName().equals(player.getName())) {
          return addNewPlayer(player);
        } else {
          return playerScore;
        }
      }
    }
    return null;
  }


  /**
   * 新規プレイヤー情報をリストに追加
   */
  private PlayerScore addNewPlayer(Player player) {
    PlayerScore newPlayer = new PlayerScore();
    newPlayer.setPlayerName(player.getName()); //playerScoreリストにコマンド実行したプレイヤーの名前を取得
    playerScoreList.add(newPlayer); //追加
    return newPlayer;
  }

  @Override
  public boolean onMiningNPCCommand(CommandSender sender) {
    return false;
  }
}



