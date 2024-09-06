package plugin.oremininggame;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * コマンドを実行して動かすプラグイン処理の基底クラスです。
 */
public abstract class BaseCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
    if (sender instanceof Player player) {
      return onMiningPlayerCommand(player);
    } else {
      return onMiningNPCCommand(sender);
    }
  }

  /**
   * コマンド実行者がプレイヤーだった場合に実行
   * @param player
   * @return　処置の実行有無
   */
  public abstract boolean onMiningPlayerCommand(Player player);

  /**
   * コマンド実行者がプレイヤー以外だった場合
   * @param sender コマンド実行者
   * @return　処理の実行有無
   */
  public abstract boolean onMiningNPCCommand(CommandSender sender);

}
