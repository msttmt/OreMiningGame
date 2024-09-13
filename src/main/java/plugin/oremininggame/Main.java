package plugin.oremininggame;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.oremininggame.command.MiningStartCommand;

public final class Main extends JavaPlugin {

  @Override
  public void onEnable() {
    MiningStartCommand miningStartCommand = new MiningStartCommand(this);
    Bukkit.getPluginManager().registerEvents(miningStartCommand, this);
    getCommand("gameStart").setExecutor(miningStartCommand);
  }
}