/*
 * Copyright (c) 2017, Tyler Bucher
 * Copyright (c) 2017, Orion Stanger
 * Copyright (c) 2019, (Contributors)
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.edgegamers.picklez.Storage;

import net.cpas.Cpas;
import net.cpas.model.CpasGroupModel;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.YamlConfig;

public class Config extends YamlConfig {

    private CpasGroupModel atLeastAdminGroup;
    private String apiUrl;
    private String apiKey;
    private String serverIP;
    private String port;
    private String groupsPrefix;
    private boolean usePrimaryGroups;
    private SerializedMap primaryGroups;
    private boolean useNoGroup;
    private String noGroupGroup;
    private boolean useDivisionGroups;
    private SerializedMap divisionGroups;
    private boolean useSecondaryGroups;
    private SerializedMap secondaryGroups;
    private boolean useDsGroup;
    private String dsGroup;
    private SerializedMap adminLevel;
    private boolean overrideBanCommand;
    private int banHistoryCount;
    private int banRankThreshold;

    public Config() {
        //Loads a new Configuration file. If it doesn't exist, it will create a new one
        //that follows the default defined in resources/config.yml
        loadConfiguration("config.yml", "config.yml");
    }

    @Override
    public void onLoadFinish() {
        //This runs as soon as the configuration is done loading
        //will be used to set variables and such
        atLeastAdminGroup = new CpasGroupModel(
                getString("CPAS.adminLevel.name"),
                getInteger("CPAS.adminLevel.rank")
        );
        apiUrl = getString("CPAS.apiUrl");
        apiKey = getString("CPAS.apiKey");
        serverIP = getString("CPAS.serverIP");
        port = getString("CPAS.port");
        groupsPrefix = getString("CPAS.groupsPrefix");
        usePrimaryGroups = getBoolean("CPAS.usePrimaryGroups");
        primaryGroups = getMap("CPAS.primaryGroups");
        useNoGroup = getBoolean("CPAS.useNoGroup");
        noGroupGroup = getString("CPAS.noGroupGroup");
        useDivisionGroups = getBoolean("CPAS.useDivisionGroups");
        divisionGroups = getMap("CPAS.divisionGroups");
        useSecondaryGroups = getBoolean("CPAS.useSecondaryGroups");
        secondaryGroups = getMap("CPAS.secondaryGroups");
        useDsGroup = getBoolean("CPAS.useDsGroup");
        dsGroup = getString("CPAS.dsGroup");
        overrideBanCommand = getBoolean("commands.overrideBanCommand");
        banHistoryCount = getInteger("commands.banHistoryCount");
        banRankThreshold = getInteger("commands.banRankThreshold");
    }

    public void configCpas() {
        Cpas.getInstance().configure(getApiUrl(), getApiKey(), getServerIP(), getPort());
    }

    public CpasGroupModel getAtLeastAdminGroup() {
        return atLeastAdminGroup;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getServerIP() {
        return serverIP;
    }

    public String getPort() {
        return port;
    }

    public String getGroupsPrefix() {
        return groupsPrefix;
    }

    public boolean isUsePrimaryGroups() {
        return usePrimaryGroups;
    }

    public SerializedMap getPrimaryGroups() {
        return primaryGroups;
    }

    public boolean isUseNoGroup() {
        return useNoGroup;
    }

    public String getNoGroupGroup() {
        return noGroupGroup;
    }

    public boolean isUseDivisionGroups() {
        return useDivisionGroups;
    }

    public SerializedMap getDivisionGroups() {
        return divisionGroups;
    }

    public boolean isUseSecondaryGroups() {
        return useSecondaryGroups;
    }

    public SerializedMap getSecondaryGroups() {
        return secondaryGroups;
    }

    public boolean isUseDsGroup() {
        return useDsGroup;
    }

    public String getDsGroup() {
        return dsGroup;
    }

    public SerializedMap getAdminLevel() {
        return adminLevel;
    }

    public boolean isOverrideBanCommand() {
        return overrideBanCommand;
    }

    public int getBanHistoryCount() {
        return banHistoryCount;
    }

    public int getBanRankThreshold() {
        return banRankThreshold;
    }
}
