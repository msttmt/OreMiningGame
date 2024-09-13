package plugin.oremininggame;

import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class createStage {

  public static void setLocation(Player player, World world) {
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
          world.getBlockAt(new Location(world, x + i, y + j, z + k)).setType(Material.AIR);
        }
      }
    }

    //鉱石ブロックの塊を生成
    for (int i = 3; i < 25; i++) {
      for (int j = 0; j < 25; j++) {
        for (int k = 3; k < 25; k++) {
          int random = new SplittableRandom().nextInt(10); //ランダムに生成(DAY13)
          switch (random) {
            case 0 -> world.getBlockAt(new Location(world, x + i, y + j, z + k))
                .setType(Material.DIAMOND_ORE);
            case 1 -> world.getBlockAt(new Location(world, x + i, y + j, z + k))
                .setType(Material.GOLD_ORE);
            case 2 -> world.getBlockAt(new Location(world, x + i, y + j, z + k))
                .setType(Material.IRON_ORE);
            case 3 -> world.getBlockAt(new Location(world, x + i, y + j, z + k))
                .setType(Material.COAL_ORE);
            case 4 -> world.getBlockAt(new Location(world, x + i, y + j, z + k))
                .setType(Material.LAPIS_ORE);
            case 5 -> world.getBlockAt(new Location(world, x + i, y + j, z + k))
                .setType(Material.COPPER_ORE);
            case 6 ->
                world.getBlockAt(new Location(world, x + i, y + j, z + k)).setType(Material.SAND);
            case 7 -> world.getBlockAt(new Location(world, x + i, y + j, z + k))
                .setType(Material.EMERALD_ORE);
            case 8 ->
                world.getBlockAt(new Location(world, x + i, y + j, z + k)).setType(Material.STONE);
            case 9 -> world.getBlockAt(new Location(world, x + i, y + j, z + k))
                .setType(Material.REDSTONE_ORE);
            case 10 -> world.getBlockAt(new Location(world, x + i, y + j, z + k))
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
      ItemStack item = i < playerArmorInventory.length ? playerArmorInventory[i]
          : playerInventory[i - playerArmorInventory.length]; //三項演算子
      if (item != null) {
        Inventory targetInventory = i < halfSize ? firstInventory : secondInventory;
        targetInventory.addItem(item);
      }
    }

    // プレイヤーのインベントリをクリア
    player.getInventory().clear();
    player.sendMessage("チェストにアイテムを移動しました。");
  }
}
