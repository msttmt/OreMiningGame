package plugin.oremininggame.command;

import java.io.IOException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * コマンドを実行して動かすプラグイン処理の基底クラスです。
 */
public abstract class BaseCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player player) {
      try {
        return onMiningPlayerCommand(player, command, label, args);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      return onMiningNPCCommand(sender, command, label, args);
    }
  }

  /**
   * コマンド実行者がプレイヤーだった場合に実行
   *
   * @param player  プレイヤー
   * @param command コマンド
   * @param label   ラベル
   */
  public abstract boolean onMiningPlayerCommand(Player player, Command command, String label,
      String[] args) throws IOException;

  /**
   * コマンド実行者がプレイヤー以外だった場合
   *
   * @param sender  コマンド実行者
   * @param command コマンド
   * @param label   ラベル
   */
  public abstract boolean onMiningNPCCommand(CommandSender sender, Command command, String label,
      String[] args);

}
