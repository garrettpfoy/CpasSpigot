package org.edgegamers.picklez.Storage;

import fr.xephi.authme.data.auth.PlayerCache;
import lombok.Getter;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.SimpleFlatDatabase;

import java.util.ArrayList;
import java.util.List;

public class CacheDatabase extends SimpleFlatDatabase<CpasPlayerCache> {

    @Getter
    private final static CacheDatabase instance = new CacheDatabase();

    public static CacheDatabase getInstance() {
        return instance;
    }

    @Override
    public void onLoad(SerializedMap serializedMap, CpasPlayerCache playerCache) {
        /*
        Loads the player's serializedMap, if it contains a value, we load it, if not, we create a default value as a placeholder
         */
        if(serializedMap.keySet().contains("Notes")) {

            List<String> notes = serializedMap.getStringList("Notes");
            playerCache.setNotes(notes);
        }
        else {
            SerializedMap map = new SerializedMap();
            map.put("Notes", new ArrayList<>());

            List<String> notes = map.getStringList("Notes");
            playerCache.setNotes(notes);
        }

        if(serializedMap.keySet().contains("Mutes")) {
            List<String> mutes = serializedMap.getStringList("Mutes");
            playerCache.setMutes(mutes);
        }
        else {
            SerializedMap map = new SerializedMap();
            map.put("Mutes", new ArrayList<>());
            List<String> mutes = map.getStringList("Mutes");
            playerCache.setMutes(mutes);
        }

        if(serializedMap.keySet().contains("Username")) {
            String username = serializedMap.getString("Username");
            playerCache.setUsername(username);
        }
        else {
            playerCache.setUsername("null");
        }

        if(serializedMap.keySet().contains("Forumname")) {
            String forumName = serializedMap.getString("Forumname");
            playerCache.setForumName(forumName);
        }
        else {
            playerCache.setForumName("null");
        }

        if(serializedMap.keySet().contains("Rankname")) {
            String rankName = serializedMap.getString("Rankname");
            playerCache.setRankName(rankName);
        }
        else {
            playerCache.setRankName("null");
        }

        if(serializedMap.keySet().contains("Nickname")) {
            String nickname = serializedMap.getString("Nickname");
            playerCache.setNickName(nickname);
        }
        else {
            playerCache.setNickName("null");
        }

        if(serializedMap.keySet().contains("Primarygroup")) {
            String primaryGroup = serializedMap.getString("Primarygroup");
            playerCache.setPrimaryGroup(primaryGroup);
        }
        else {
            playerCache.setPrimaryGroup("null");
        }

        if(serializedMap.keySet().contains("Division")) {
            String division = serializedMap.getString("Division");
            playerCache.setDivisionName(division);
        }
        else {
            playerCache.setDivisionName("null");
        }

        if(serializedMap.keySet().contains("Verification")) {
            boolean isVerified = serializedMap.getBoolean("Verification");
            playerCache.setVerificationExpired(isVerified);
        }
        else {
            playerCache.setVerificationExpired(false);
        }

        if(serializedMap.keySet().contains("DS")) {
            boolean isDS = serializedMap.getBoolean("DS");
            playerCache.setDedicatedSupporter(isDS);
        }
        else {
            playerCache.setDedicatedSupporter(false);
        }

        if(serializedMap.keySet().contains("Registered")) {
            boolean isRegistered = serializedMap.getBoolean("Registered");
            playerCache.setIsRegistered(isRegistered);
        }
        else {
            playerCache.setIsRegistered(false);
        }

        if(serializedMap.keySet().contains("Member")) {
            boolean isMember = serializedMap.getBoolean("Member");
            playerCache.setIsMember(isMember);
        }
        else {
            playerCache.setIsMember(false);
        }

        if(serializedMap.keySet().contains("Rank")) {
            int rank = serializedMap.getInteger("Rank");
            playerCache.setRank(rank);
        }
        else {
            playerCache.setRank(0);
        }

        if(serializedMap.keySet().contains("Groups")) {
            List<String> groups = serializedMap.getStringList("Groups");
            playerCache.setGroups(groups);
        }
        else {
            List<String> groups = new ArrayList<>();
            playerCache.setGroups(groups);
        }


    }

    @Override
    protected SerializedMap onSave(CpasPlayerCache playerCache) {
        SerializedMap map = new SerializedMap();

        map.put("Username", playerCache.getUsername());
        map.put("Forumname", playerCache.getForumName());
        map.put("Rankname", playerCache.getRankName());
        map.put("Nickname", playerCache.getNickName());
        map.put("Primarygroup", playerCache.getPrimaryGroup());
        map.put("Division", playerCache.getDivisionName());
        map.put("Groups", playerCache.getGroups());
        map.put("Verification", playerCache.isVerificationExpired());
        map.put("DS", playerCache.isDedicatedSupporter());
        map.put("Registered", playerCache.isRegistered());
        map.put("Member", playerCache.isMember());
        map.put("Rank", playerCache.getRank());
        map.put("Notes", playerCache.getNotes());
        map.put("Mutes", playerCache.getMutes());

        return map;
    }
}
