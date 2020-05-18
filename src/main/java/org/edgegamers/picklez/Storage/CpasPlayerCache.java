package org.edgegamers.picklez.Storage;

import org.edgegamers.picklez.API.CpasPlayerCacheAPI;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CpasPlayerCache extends YamlSectionConfig implements CpasPlayerCacheAPI {

    private static Map<UUID, CpasPlayerCache> cacheMap = new HashMap<>();

    /*
    As of CPAS version 3.0, we have migrated all data storage into an external database.
    The information we store can thus be similar on all servers without the need of new flat files on each (saves space!)
    Information MAUL gives us will update the playerCache onLogin events (and disconnect). This allows for a more in-depth API
    with playercache

    API Notes will be the best documentation on this information, however this class is also a good documentation on the API
    CPAS provides

     */
    private String username; //Player username (Only used for messages, UUID is preferred method)
    private String forumName; //Get a player's forum username, null if not set
    private String rankName; //String rank name, not integer used to define values in config
    private String nickName; //User's nickname, not given by maul but rather in-game
    private String primaryGroup; //Player's primary group (e, eg, ego, manager, etc)
    private String divisionName; //String of formatted division name (Minecraft not mc)
    private List<String> groups; //List of users inherited groups (e, eg, ego, at, ec, rc, etc)
    private List<String> notes; //Not supported by maul, Minecraft defined notes
    private List<String> mutes; //Not supported by maul, Minecraft defined mutes (past and present)
    private boolean verificationExpired; //Checks if the player has been inactive on the forums for too long
    private boolean isDedicatedSupporter; //Checks whether a player is a dedicated supporter
    private boolean isRegistered; //Checks whether play has forum account tied to their Game ID
    private boolean isMember; //Checks whether a player is a member (can also be done via rank integer > 0)
    private int rank; //A user's rank in integer form (10 is e, 20 is g, 30 is o, etc)


    protected CpasPlayerCache(UUID uuid) {
        //Initiates a configuration in data.db and creates a "header" section that mirrors the database
        super(uuid.toString());

        loadConfiguration(null, "data.db");
    }

    @Override
    protected void onLoadFinish() {
        //Will, if set, load and initiate all variables defined in the playercache
        if(isSet("Username")) {
            username = getString("Username");
        }
        if(isSet("Forumname")) {
            forumName = getString("Forumname");
        }
        if(isSet("Rankname")) {
            rankName = getString("Rankname");
        }
        if(isSet("Nickname")) {
            nickName = getString("Nickname");
        }
        if(isSet("Primarygroup")) {
            primaryGroup = getString("Primarygroup");
        }
        if(isSet("Division")) {
            divisionName = getString("Division");
        }
        if(isSet("Groups")) {
            groups = getStringList("Groups");
        }
        if(isSet("Verification")) {
            verificationExpired = getBoolean("Verification");
        }
        if(isSet("DS")) {
            isDedicatedSupporter = getBoolean("DS");
        }
        if(isSet("Registered")) {
            isRegistered = getBoolean("Registered");
        }
        if(isSet("Member")) {
            isMember = getBoolean("Member");
        }
        if(isSet("Rank")) {
            rank = getInteger("Rank");
        }
        if(isSet("Notes")) {
            notes = getStringList("Notes");
        }
        if(isSet("Mutes")) {
            mutes = getStringList("Mutes");
        }
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
        save();
    }

    @Override
    public void setForumName(String forumName) {
        this.forumName = forumName;
        save();
    }

    @Override
    public void setRankName(String rankName) {
        this.rankName = rankName;
        save();
    }

    @Override
    public void setNickName(String nickName) {
        this.nickName = nickName;
        save();
    }

    @Override
    public void setPrimaryGroup(String primaryGroup) {
        this.primaryGroup = primaryGroup;
        save();
    }

    @Override
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
        save();
    }

    @Override
    public void setVerificationExpired(boolean isVerificationExpired) {
        this.verificationExpired = isVerificationExpired;
        save();
    }

    @Override
    public void setDedicatedSupporter(boolean isDedicatedSupporter) {
        this.isDedicatedSupporter = isDedicatedSupporter;
        save();
    }

    @Override
    public void setIsRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
        save();
    }

    @Override
    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
        save();
    }

    @Override
    public void setRank(int rank) {
        this.rank = rank;
        save();
    }

    @Override
    public void setNotes(List<String> notes) {
        this.notes = notes;
        save();
    }

    @Override
    public void setMutes(List<String> mutes) {
        this.mutes = mutes;
        save();
    }

    @Override
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getForumName() {
        return forumName;
    }

    @Override
    public String getRankName() {
        return rankName;
    }

    @Override
    public String getNickName() {
        return nickName;
    }

    @Override
    public String getPrimaryGroup() {
        return primaryGroup;
    }

    @Override
    public String getDivisionName() {
        return divisionName;
    }

    @Override
    public boolean isVerificationExpired() {
        return verificationExpired;
    }

    @Override
    public boolean isDedicatedSupporter() {
        return isDedicatedSupporter;
    }

    @Override
    public boolean isRegistered() {
        return isRegistered;
    }

    @Override
    public boolean isMember() {
        return isMember;
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public List<String> getNotes() {
        return notes;
    }

    @Override
    public List<String> getMutes() {
        return mutes;
    }

    @Override
    public List<String> getGroups() {
        return groups;
    }

    @Override
    public SerializedMap serialize() {
        SerializedMap map = new SerializedMap();
        map.putIfExist("Username", username);
        map.putIfExist("Forumname", forumName);
        map.putIfExist("Rankname", rankName);
        map.putIfExist("Nickname", nickName);
        map.putIfExist("Primarygroup", primaryGroup);
        map.putIfExist("Division", divisionName);
        map.putIfExist("Groups", groups);
        map.putIfExist("Verification", verificationExpired);
        map.putIfExist("DS", isDedicatedSupporter);
        map.putIfExist("Registered", isRegistered);
        map.putIfExist("Member", isMember);
        map.putIfExist("Rank", rank);
        map.putIfExist("Notes", notes);
        map.putIfExist("Mutes", mutes);
        return map;
    }


    public static CpasPlayerCache getCache(final UUID uuid) {
        if(cacheMap.keySet().contains(uuid)) {
            CpasPlayerCache cache = cacheMap.get(uuid);
            return cache;
        }
        else {
            CpasPlayerCache cache = new CpasPlayerCache(uuid);
            cacheMap.put(uuid, cache);
            return cache;
        }
    }
}
