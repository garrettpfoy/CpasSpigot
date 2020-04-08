package org.edgegamers.picklez.Storage;

import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.YamlConfig;

public class PlayerData extends YamlConfig {
    String name;
    String nickname;
    boolean isDS;
    int rank;
    String search;
    String forumName;

    public PlayerData(String uuid) {
        loadConfiguration("player.yml", "Players/" + uuid + ".yml");
    }

    @Override
    public void onLoadFinish() {
        name = getString("name");
        nickname = getString("nickname");
        isDS = getBoolean("isDS");
        rank = getInteger("rank");
        forumName = getString("forumName");

    }

    public String getForumName() {
        return getString("forumName");
    }

    public void setForumName(String forumName) {
        save("forumName", forumName);
    }
    public String getName() {
        return getString("name");
    }

    public String getNickname() {
        return getString("nickname");
    }

    public boolean isDS() {
        return getBoolean("isDS");
    }

    public int getRank() {
        return getInteger("rank");
    }

    public void setName(String newName) {
        save("name", newName);
    }

    public void setNickname(String newNick) {
        save("nickname", newNick);
    }

    public void setDS(boolean isDS) {
        save("isDS", isDS);
    }

    public void setRank(int newRank) {
        save("rank", newRank);
    }

    public void setSearch(String newSearch) {
        save("search", newSearch);
    }

    public String getSearch() {
        return getString("search");
    }
}
