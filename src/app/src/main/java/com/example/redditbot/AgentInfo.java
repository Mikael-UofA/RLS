package com.example.redditbot;

import java.io.Serializable;

public class AgentInfo implements Serializable {
    private String agentUsername;
    private String agentPass;
    private String agentClientId;
    private String agentClientSecret;
    private String agentReceiver;
    private String agentAppName;
    private String agentAuthorName;

    public AgentInfo(){
    }
    public AgentInfo(String agentUsername, String agentPass, String agentClientId, String agentClientSecret, String agentAppName, String agentAuthorName) {
        this.agentUsername = agentUsername;
        this.agentPass = agentPass;
        this.agentClientId = agentClientId;
        this.agentClientSecret = agentClientSecret;
        this.agentAppName = agentAppName;
        this.agentAuthorName = agentAuthorName;
    }

    public String getAgentUsername() {
        return agentUsername;
    }

    public String getAgentPass() {
        return agentPass;
    }

    public String getAgentClientId() {
        return agentClientId;
    }

    public String getAgentClientSecret() {
        return agentClientSecret;
    }

    public String getAgentReceiver() {
        return agentReceiver;
    }

    public String getAgentAppName() {
        return agentAppName;
    }

    public String getAgentAuthorName() {
        return agentAuthorName;
    }

    public void setAgentUsername(String agentUsername) {
        this.agentUsername = agentUsername;
    }

    public void setAgentPass(String agentPass) {
        this.agentPass = agentPass;
    }

    public void setAgentClientId(String agentClientId) {
        this.agentClientId = agentClientId;
    }

    public void setAgentClientSecret(String agentClientSecret) {
        this.agentClientSecret = agentClientSecret;
    }

    public void setAgentReceiver(String agentReceiver) {
        this.agentReceiver = agentReceiver;
    }

    public void setAgentAppName(String agentAppName) {
        this.agentAppName = agentAppName;
    }

    public void setAgentAuthorName(String agentAuthorName) {
        this.agentAuthorName = agentAuthorName;
    }
}
