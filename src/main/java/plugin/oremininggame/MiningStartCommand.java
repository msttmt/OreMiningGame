package plugin.oremininggame;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.SplittableRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plugin.oremininggame.data.PlayerScore;

public class MiningStartCommand implements CommandExecutor , Listener {
  private Main main;
  private List<PlayerScore> playerScoreList = new ArrayList<>();
  private int gameTime;
  private int idleTime;

  public MiningStartCommand(Main main) {
    this.main = main;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String s,
      String[] strings) {
    if (sender instanceof Player player) {
      PlayerScore nowPlayer = getPlayerScore(player);
      World world = player.getWorld(); //playerのワールド情報を取得して変数として定義

      player.setHealth(20);
      player.setFoodLevel(20);

      idleTimer(player); //待機時間カウント
      setLocation(player, world); //playerの縦横30の距離で採掘場の生成

      player.sendMessage("チェストにアイテムを移動しました。ピッケルを渡しました。");
      player.getInventory().setItemInMainHand(new ItemStack(Material.NETHERITE_PICKAXE));
      player.sendMessage("このピッケルで鉱石の塊を採掘して高得点を狙ってください。");

      gameTime = 20; //初期化
      Bukkit.getScheduler().runTaskTimer(main,Runnable ->{
        if(gameTime <= 0){
          Runnable.cancel();
          player.sendTitle("ゲーム終了","スコア" + nowPlayer.getScore());
          nowPlayer.setScore(0);
          return;
        }
        gameTime--;
      },0,20);  //DAY17 タイムランナー

    }
    return false;
  }

  private void idleTimer(Player player) {
    idleTime = 6; //初期化
    Bukkit.getScheduler().runTaskTimer(main,Runnable ->{
      if(idleTime == 0){
        Runnable.cancel();
        player.sendTitle("Game Start!","何点とれるかな？");
        return;
      }else if(idleTime == 6) {
        idleTime--;
      }else{
        player.sendTitle("開始まで", " " + idleTime + " ");
        idleTime--;
      }
    },0,5 * 4);
  }


  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    Player player = e.getPlayer();
    Block block = e.getBlock();
    if(Objects.isNull(player) || playerScoreList.isEmpty()) {
      return;
    }

    playerScoreList.stream()
        .filter( p -> p.getPlayerName().equals(player.getName()))
        .findFirst()
        .ifPresent(p -> {int point = switch (block.getType()) {
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
          player.sendMessage(
              player.getName() + " " + point + "点。現在のスコア: " + p.getScore() + "点");
        });
  }

  private static void setLocation(Player player, World world) {
    Location playerLocation = player.getLocation();
    double x = playerLocation.getX();
    double y = playerLocation.getY();
    double z = playerLocation.getZ();

    //採掘ステージを作る
    for (int i = -2; i < 30; i++) {
      for (int j = -2; j < 30; j++) {
        for (int k = -2; k < 30; k++) {
          world.getBlockAt(new Location(world, x + i, y + j, z + k))
              .setType(Material.SEA_LANTERN); //ブロック出現（DAY10）+エンティティがスポーンする（DAY13）
        }
      }
    }

    //中を空洞
    for (int i = -1; i < 29; i++) {
      for (int j = -1; j < 29; j++) {
        for (int k = -1; k < 29; k++) {
            world.getBlockAt(new Location(world, x + i, y + j, z + k))
                .setType(Material.AIR);
        }
      }
    }

    //鉱石ブロックの塊を生成
    for (int i = 3; i < 25; i++) {
      for (int j = 0; j < 25; j++) {
        for (int k = 3; k < 25; k++) {
          int random = new SplittableRandom().nextInt(10); //ランダムに生成(DAY13)
          switch (random) {
            case 0 -> world.getBlockAt(new Location(world, x+i, y+j, z+k))
              .setType(Material.DIAMOND_ORE);
            case 1 -> world.getBlockAt(new Location(world, x+i, y+j, z+k))
                .setType(Material.GOLD_ORE);
            case 2 -> world.getBlockAt(new Location(world, x+i, y+j, z+k))
                .setType(Material.IRON_ORE);
            case 3 -> world.getBlockAt(new Location(world, x+i, y+j, z+k))
                .setType(Material.COAL_ORE);
            case 4 -> world.getBlockAt(new Location(world, x+i, y+j, z+k))
                .setType(Material.LAPIS_ORE);
            case 5 -> world.getBlockAt(new Location(world, x+i, y+j, z+k))
                .setType(Material.COPPER_ORE);
            case 6 -> world.getBlockAt(new Location(world, x+i, y+j, z+k))
                .setType(Material.SAND);
            case 7 -> world.getBlockAt(new Location(world, x+i, y+j, z+k))
                .setType(Material.EMERALD_ORE);
            case 8 -> world.getBlockAt(new Location(world, x+i, y+j, z+k))
                .setType(Material.STONE);
            case 9 -> world.getBlockAt(new Location(world, x+i, y+j, z+k))
                .setType(Material.REDSTONE_ORE);
            case 10 -> world.getBlockAt(new Location(world, x+i, y+j, z+k))
                .setType(Material.SMOOTH_STONE);
          }
        }
      }
    }

    //　チェスト2個設置
    Block block = playerLocation.getBlock().getRelative(player.getFacing());
    block.setType(Material.CHEST);
    Block secondBlock = block.getRelative(player.getFacing());
    secondBlock.setType(Material.CHEST);

    // チェストのインベントリを取得
    Chest firstChest = (Chest) block.getState();
    Inventory firstInventory = firstChest.getInventory();
    Chest secondChest = (Chest) secondBlock.getState();
    Inventory secondInventory = secondChest.getInventory();

    // アイテムを2つのチェストに分割して移動
    ItemStack[] playerInventory = player.getInventory().getContents();
    ItemStack[] playerArmorInventory = player.getInventory().getArmorContents();

    int inventorySize = playerInventory.length + playerArmorInventory.length;
    int halfSize = inventorySize / 2;

    for (int i = 0; i < inventorySize; i++) {
      ItemStack item = i < playerArmorInventory.length ? playerArmorInventory[i] : playerInventory[i - playerArmorInventory.length]; //三項演算子
      if (item != null) {
        Inventory targetInventory = i < halfSize ? firstInventory : secondInventory;
        targetInventory.addItem(item);
      }
    }

    // プレイヤーのインベントリをクリア
    player.getInventory().clear();


    }

  /**
   * 現在実行しているプレイヤーのスコア情報を取得
   * @param player　コマンドを実行したプレイヤー
   * @return 現在実行しているプレイヤーのスコア加算
   */

    private PlayerScore getPlayerScore(Player player) {
    if(playerScoreList.isEmpty()) {
      return addNewPlayer(player);
    }else{
      for(PlayerScore playerScore : playerScoreList){
        if(!playerScore.getPlayerName().equals(player.getName())){
          return addNewPlayer(player);
        }else{
          return playerScore;
        }
      }
    }
      return null;
    }


  /**
   新規プレイヤー情報をリストに追加
   */
  private PlayerScore addNewPlayer(Player player) {
    PlayerScore newPlayer = new PlayerScore();
    newPlayer.setPlayerName(player.getName()); //playerScoreリストにコマンド実行したプレイヤーの名前を取得
    playerScoreList.add(newPlayer); //追加
    return newPlayer;
  }
}

