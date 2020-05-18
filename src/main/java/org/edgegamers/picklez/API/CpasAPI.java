package org.edgegamers.picklez.API;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.edgegamers.picklez.Storage.CpasPlayerCache;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CpasAPI {

    public static CpasPlayerCacheAPI getCache(UUID uuid) {
        return CpasPlayerCache.getCache(uuid);
    }

}
