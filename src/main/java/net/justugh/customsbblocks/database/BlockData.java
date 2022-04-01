package net.justugh.customsbblocks.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.justugh.japi.database.hikari.data.HikariObject;
import net.justugh.japi.util.location.SafeLocation;

@Getter
@AllArgsConstructor
public class BlockData extends HikariObject {

    private String location;
    private String key;

    public SafeLocation getSafeLocation() {
        return SafeLocation.fromString(location);
    }

    @Override
    public Object[] getDataObjects() {
        return new Object[]{location, key};
    }

    @Override
    public String getDataId() {
        return location.toString();
    }

}
