package plugin.oremininggame.command;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import plugin.oremininggame.BossBarManager;
import plugin.oremininggame.Main;
import plugin.oremininggame.scoredata.PlayerScoreData;
import plugin.oremininggame.scoredata.ScoreboardManager;
import plugin.oremininggame.createStage;
import plugin.oremininggame.mapper.data.PlayerScore;
import plugin.oremininggame.mapper.data.data.MiningPlayer;

public class MiningStartCommand extends BaseCommand implements Listener {

  private final Main main;
  private final List<MiningPlayer> miningPlayerList = new ArrayList<>();
  private int nowGameTime;
  private int idleTime;
  private boolean isPointCountEnd = true;
  private BossBarManager bossBarManager;
  private final ScoreboardManager scoreboardManager = new ScoreboardManager();
  private final SqlSessionFactory sqlSessionFactory;
  private final PlayerScoreData playerScoreData = new PlayerScoreData();

  public MiningStartCommand(Main main) {
    this.main = main;

    try {
      InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean onMiningPlayerCommand(Player player, Command command,String label,String args[]) {
    if(args.length == 1 && args[0].equals("list")){
      List<PlayerScore> playerScoreList = playerScoreData.selectList();
      for (PlayerScore playerScore : playerScoreList) {
        player.sendMessage(playerScore.getId() + " | "
            + playerScore.getPlayerName() + " | "
            + playerScore.getScore() + " | "
            + playerScore.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      }
      return false;
    }

    MiningPlayer nowPlayer = getPlayerScore(player);
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



  private void timeScheduler(Player player, MiningPlayer nowPlayer) {
    nowGameTime = 120; //初期化
    bossBarManager = new BossBarManager();
    bossBarManager.showBossBar(player);
    Bukkit.getScheduler().runTaskTimer(main, Runnable -> {
      if(!isPointCountEnd) {
        bossBarManager.updateBossBar(nowGameTime);
        if (nowGameTime == 0) {
          Runnable.cancel();
          scoreboardManager.updateCurrentScore(player, nowPlayer.getScore());
          player.sendTitle("Score " + nowPlayer.getScore() + "点","ゲーム終了～！");

          playerScoreData.insert(new PlayerScore(nowPlayer.getPlayerName(), nowPlayer.getScore()));
          nowPlayer.setScore(0);
          bossBarManager.hideBossBar(player);
          isPointCountEnd = true;
          scoreboardManager.clearScore(player);
          return;
        }else if(nowGameTime < 6) {
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
    if (Objects.isNull(player) || miningPlayerList.isEmpty() || isPointCountEnd) {
      return;
    }

    miningPlayerList.stream().filter(p -> p.getPlayerName().equals(player.getName())).findFirst()
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

  private MiningPlayer getPlayerScore(Player player) {
    if (miningPlayerList.isEmpty()) {
      return addNewPlayer(player);
    } else {
      for (MiningPlayer miningPlayer : miningPlayerList) {
        if (miningPlayer.getPlayerName().equals(player.getName())) {
          return miningPlayer;
        } else {
          return addNewPlayer(player);
        }
      }
    }
    return null;
  }


  /**
   * 新規プレイヤー情報をリストに追加
   */
  private MiningPlayer addNewPlayer(Player player) {
    MiningPlayer newPlayer = new MiningPlayer();
    newPlayer.setPlayerName(player.getName()); //playerScoreリストにコマンド実行したプレイヤーの名前を取得
    miningPlayerList.add(newPlayer); //追加
    return newPlayer;
  }

  @Override
  public boolean onMiningNPCCommand(CommandSender sender,Command command, String label, String[] args) {
    return false;
  }
}



