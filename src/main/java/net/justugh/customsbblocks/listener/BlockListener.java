package net.justugh.customsbblocks.listener;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import net.justugh.customsbblocks.CustomSBBlocks;
import net.justugh.customsbblocks.database.BlockData;
import net.justugh.japi.util.ItemStackUtil;
import net.justugh.japi.util.location.SafeLocation;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Map;

public class BlockListener implements Listener {

    private final CustomSBBlocks plugin;

    public BlockListener(CustomSBBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (plugin.getItems().values().stream().noneMatch(item -> ItemStackUtil.isSimilar(item, event.getItemInHand(), true, true, true))) {
            return;
        }

        Island island = SuperiorSkyblockAPI.getIslandAt(event.getBlock().getLocation());

        if(island == null) {
            return;
        }

        for (Map.Entry<String, ItemStack> entry : plugin.getItems().entrySet()) {
            if (!ItemStackUtil.isSimilar(entry.getValue(), event.getItemInHand(), true, true, true)) {
                continue;
            }

            BlockData data = new BlockData(SafeLocation.fromBukkitLocation(event.getBlock().getLocation()).toString(), entry.getKey());
            plugin.getBlockDatabase().save(data, rs -> {});
            plugin.getBlockDatabase().getData().add(data);
            int value = plugin.getValues().get(data.getKey());
            island.setBonusWorth(island.getBonusWorth().add(BigDecimal.valueOf(value)));
            break;
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        BlockData data = plugin.getBlockDatabase().getBlockData(event.getBlock().getLocation());

        if (data == null) {
            return;
        }

        Island island = SuperiorSkyblockAPI.getIslandAt(event.getBlock().getLocation());
        int stacked = SuperiorSkyblockAPI.getSuperiorSkyblock().getStackedBlocks().getStackedBlockAmount(event.getBlock().getLocation());
        int value = plugin.getValues().get(data.getKey());

        if(island == null) {
            return;
        }

        if (stacked > 0) {
            event.setCancelled(true);

            if (stacked > 1) {
                SuperiorSkyblockAPI.getIslandAt(event.getBlock().getLocation()).handleBlockBreak(event.getBlock());
                SuperiorSkyblockAPI.getSuperiorSkyblock().getStackedBlocks().setStackedBlock(event.getBlock(), stacked - 1);
                island.setBonusWorth(island.getBonusWorth().subtract(BigDecimal.valueOf(value)));
            } else {
                SuperiorSkyblockAPI.getIslandAt(event.getBlock().getLocation()).handleBlockBreak(event.getBlock());
                SuperiorSkyblockAPI.getSuperiorSkyblock().getStackedBlocks().setStackedBlock(event.getBlock(), 0);
                event.getBlock().setType(Material.AIR);
                plugin.getBlockDatabase().delete(data);
                plugin.getBlockDatabase().getData().remove(data);
                island.setBonusWorth(island.getBonusWorth().subtract(BigDecimal.valueOf(value)));
            }

            event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), plugin.getItems().get(data.getKey()));
            return;
        }

        plugin.getBlockDatabase().delete(data);
        plugin.getBlockDatabase().getData().remove(data);
        island.setBonusWorth(island.getBonusWorth().subtract(BigDecimal.valueOf(value)));
        SuperiorSkyblockAPI.getIslandAt(event.getBlock().getLocation()).handleBlockBreak(event.getBlock());
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        BlockData data = plugin.getBlockDatabase().getBlockData(event.getBlock().getLocation());

        if (data == null) {
            return;
        }

        Island island = SuperiorSkyblockAPI.getIslandAt(event.getBlock().getLocation());
        int stacked = SuperiorSkyblockAPI.getSuperiorSkyblock().getStackedBlocks().getStackedBlockAmount(event.getBlock().getLocation());
        int value = plugin.getValues().get(data.getKey());

        if (island == null) {
            return;
        }

        if (stacked > 0) {
            SuperiorSkyblockAPI.getIslandAt(event.getBlock().getLocation()).handleBlockBreak(event.getBlock());
            event.getBlock().setType(Material.AIR);
            plugin.getBlockDatabase().delete(data);
            plugin.getBlockDatabase().getData().remove(data);
            ItemStack item = plugin.getItems().get(data.getKey());
            item.setAmount(stacked);
            island.setBonusWorth(island.getBonusWorth().subtract(BigDecimal.valueOf((long) value * stacked)));
            event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), plugin.getItems().get(data.getKey()));
            SuperiorSkyblockAPI.getSuperiorSkyblock().getStackedBlocks().setStackedBlock(event.getBlock(), 0);
            return;
        }

        plugin.getBlockDatabase().delete(data);
        plugin.getBlockDatabase().getData().remove(data);
        SuperiorSkyblockAPI.getIslandAt(event.getBlock().getLocation()).handleBlockBreak(event.getBlock());
        island.setBonusWorth(island.getBonusWorth().subtract(BigDecimal.valueOf((long) value)));
    }

}
