package net.justugh.customsbblocks.sb;

import com.bgsoftware.superiorskyblock.api.key.CustomKeyParser;
import com.bgsoftware.superiorskyblock.api.key.Key;
import net.justugh.customsbblocks.CustomSBBlocks;
import net.justugh.customsbblocks.database.BlockData;
import org.bukkit.Location;

import javax.annotation.Nullable;

public class KeyParser implements CustomKeyParser {

    private final CustomSBBlocks plugin;

    public KeyParser(CustomSBBlocks plugin) {
        this.plugin = plugin;
    }

    @Nullable
    @Override
    public Key getCustomKey(Location location) {
        BlockData data = plugin.getBlockDatabase().getBlockData(location);
        return data == null ? Key.of(location.getBlock().getType().name()) : plugin.getSkyblockKeys().get(data.getKey());
    }

    @Override
    public boolean isCustomKey(Key key) {
        return plugin.getSkyblockKeys().values().stream().anyMatch(key::equals);
    }

}
