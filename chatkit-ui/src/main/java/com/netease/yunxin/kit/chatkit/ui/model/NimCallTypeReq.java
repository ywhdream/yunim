package com.netease.yunxin.kit.chatkit.ui.model;

import java.util.ArrayList;

/**
 * @Author: Yangwenhao
 * @CreateDate: 2023/1/31
 * @Description: 用于音视频的呼叫类型
 */
public class NimCallTypeReq {
    private String type;
    public DataRequest data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataRequest getData() {
        return data;
    }

    public void setData(DataRequest data) {
        this.data = data;
    }

    public static class DataRequest {
        private String teamId;
        private ArrayList<String> members;

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public ArrayList<String> getMembers() {
            return members;
        }

        public void setMembers(ArrayList<String> members) {
            this.members = members;
        }
    }
}
