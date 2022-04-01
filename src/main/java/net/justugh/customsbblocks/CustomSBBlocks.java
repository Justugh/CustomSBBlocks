package net.justugh.customsbblocks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.key.Key;
import com.google.common.collect.Lists;
import lombok.Getter;
import net.justugh.customsbblocks.command.CustomBlockCommand;
import net.justugh.customsbblocks.database.BlockDatabase;
import net.justugh.customsbblocks.listener.BlockListener;
import net.justugh.customsbblocks.sb.KeyParser;
import net.justugh.japi.database.hikari.HikariAPI;
import net.justugh.japi.util.ItemStackUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

@Getter
public class CustomSBBlocks extends JavaPlugin {

    @Getter
    private static CustomSBBlocks instance;

    private BlockDatabase blockDatabase;
    private HashMap<String, ItemStack> items;
    private HashMap<String, Integer> values;
    private HashMap<String, Key> skyblockKeys;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        instance = this;
        HikariAPI hikariAPI = new HikariAPI(
                getConfig().getString("SQL-Credentials.Address"),
                getConfig().getString("SQL-Credentials.Database"),
                getConfig().getString("SQL-Credentials.Port"),
                getConfig().getString("SQL-Credentials.Username"),
                getConfig().getString("SQL-Credentials.Password"));

        blockDatabase = new BlockDatabase(hikariAPI);
        getCommand("customsbblocks").setExecutor(new CustomBlockCommand());

        items = new HashMap<>();
        skyblockKeys = new HashMap<>();
        values = new HashMap<>();

        for (String key : getConfig().getConfigurationSection("Custom-Blocks").getKeys(false)) {
            skyblockKeys.put(key, Key.of(key.toUpperCase()));
            values.put(key, getConfig().getInt("Custom-Blocks." + key + ".Value"));
            items.put(key, ItemStackUtil.getItemStackFromConfig(getConfig().getConfigurationSection("Custom-Blocks." + key)));
        }

        getServer().getPluginManager().registerEvents(new BlockListener(this), this);

        getServer().getScheduler().runTask(this, () -> {
            List<Key> validKeys = Lists.newArrayList();
            items.values().forEach(item -> validKeys.add(Key.of(item)));
            SuperiorSkyblockAPI.getBlockValues().registerKeyParser(new KeyParser(this), validKeys.toArray(new Key[]{}));
        });
    }

    @Override
    public void onDisable() {

    }

}
