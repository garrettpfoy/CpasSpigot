package org.edgegamers.picklez.API;

import java.util.List;

public interface CpasPlayerCacheAPI {
    /*
    Creating this API as many things (admin mode, recruitME) need a lot of the CPAS library utils,
    but lets dumb it down into events and such future developers can use with ease.
     */

    /*
    Usernames are a player's name before any modifications. Raw name, unmodified. Useful for administration purposes
     */
    void setUsername(String username);
    String getUsername();

    /*
    Forum names are set onLogin when a player logs in to any of the servers (see onLogin listener)
     */
    void setForumName(String forumname);
    String getForumName();

    /*
    Rank names are given via MAUL, and probably shouldn't be touched
     */
    void setRankName(String rankName);
    String getRankName();

    /*
    Nicknames  are set via maul, but can be overriden here.
     */
    void setNickName(String nickName);
    String getNickName();

    /*
    Primary group is another thing set by MAUL at the beginning of the onLogin listener, probably shouldn't be changed
     */
    void setPrimaryGroup(String primaryGroup);
    String getPrimaryGroup();

    /*
    Division name is another thing set by MAUL at the beginning of the onLogin listener, probably shouldn't be changed
     */
    void setDivisionName(String divisionName);
    String getDivisionName();

    /*
    Verification is whether or not the GAME ID process has been expired, unsure it's true purpose but thought it may be useful
     */
    void setVerificationExpired(boolean isVerificationExpired);
    boolean isVerificationExpired();

    /*
    Returns a boolean as to whether or not a player is a dedicated supporter
     */
    void setDedicatedSupporter(boolean isDedicatedSupporter);
    boolean isDedicatedSupporter();

    /*
    Is the player registered on the forums (linked basically)
     */
    void setIsRegistered(boolean isRegistered);
    boolean isRegistered();

    /*
    Is the player a member? (int rank must be higher than 0)
     */
    void setIsMember(boolean isMember);
    boolean isMember();

    /*
    Gets / sets the integer rank associated with a player.
     */
    void setRank(int rank);
    int getRank();

    /*
    Set's a players notes, MAKE SURE TO FOLLOW THE FORMAT!
     */
    void setNotes(List<String> notes);
    List<String> getNotes();

    /*
    Sets a player's mutes, MAKE SURE TO FOLLOW THE FORMAT!
     */
    void setMutes(List<String> mutes);
    List<String> getMutes();

    /*
    Gets a list of a player's groups (in string form)
     */
    void setGroups(List<String> groups);
    List<String> getGroups();


}
