// Copyright (c) 2022 NetEase, Inc. All rights reserved.
// Use of this source code is governed by a MIT license that can be
// found in the LICENSE file.

package com.netease.yunxin.kit.chatkit.ui.page.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.netease.yunxin.kit.chatkit.ui.R;
import com.netease.yunxin.kit.chatkit.ui.databinding.ChatMessageAitContactViewHolderBinding;
import com.netease.yunxin.kit.chatkit.ui.model.ait.AitUserInfo;
import com.netease.yunxin.kit.common.ui.utils.AvatarColor;
import java.util.ArrayList;
import java.util.List;

/** Team member @ adapter */
public class AitContactAdapter extends RecyclerView.Adapter<AitContactAdapter.AitContactHolder> {

  private List<AitUserInfo> members = new ArrayList<>();
  //@所有人的特殊类型
  private static final int SHOW_ALL_TYPE = 101;

  private OnItemListener listener;

  private AitContactConfig contactConfig;

  private boolean showAll = true;

  public void setMembers(List<AitUserInfo> userInfoWithTeams) {
    this.members.clear();
    this.members.addAll(userInfoWithTeams);
  }

  public void addMembers(List<AitUserInfo> userInfoWithTeams) {
    this.members.addAll(userInfoWithTeams);
  }

  public void setShowAll(boolean showAll) {
    this.showAll = showAll;
  }

  public void setOnItemListener(OnItemListener listener) {
    this.listener = listener;
  }

  public void setAitContactConfig(AitContactConfig config) {
    contactConfig = config;
  }

  @NonNull
  @Override
  public AitContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new AitContactHolder(
        ChatMessageAitContactViewHolderBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public int getItemViewType(int position) {
    if (showAll && position == 0) {
      return SHOW_ALL_TYPE;
    }
    return super.getItemViewType(position);
  }

  @Override
  public void onBindViewHolder(@NonNull AitContactHolder holder, int position) {

    if (contactConfig != null) {
      if (contactConfig.avatarCorner >= 0) {
        holder.binding.contactHeader.setCornerRadius(contactConfig.avatarCorner);
      }

      if (contactConfig.nameColor != 0) {
        holder.binding.contactName.setTextColor(contactConfig.nameColor);
      }
    }

    int dataPosition = position;
    if (showAll) {
      if (position == 0) {
        holder.binding.contactName.setText(R.string.chat_team_ait_all);
        holder.binding.contactHeader.setCertainAvatar(contactConfig.defaultAvatarRes);
        holder
            .binding
            .getRoot()
            .setOnClickListener(
                v -> {
                  if (listener != null) {
                    listener.onSelect(null);
                  }
                });
        return;
      }
      dataPosition = position - 1;
    }

    AitUserInfo member = members.get(dataPosition);
    if (member == null) {
      return;
    }
    String showName = member.getName();
    holder.binding.contactName.setText(showName);
    holder.binding.contactHeader.setData(
        member.getAvatar(), showName, AvatarColor.avatarColor(member.getAccount()));
    holder
        .binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (listener != null) {
                listener.onSelect(member);
              }
            });
  }

  @Override
  public int getItemCount() {
    // add ait all
    if (members == null || members.isEmpty()) {
      return 0;
    }

    return showAll ? members.size() + 1 : members.size();
  }

  public static class AitContactHolder extends RecyclerView.ViewHolder {
    ChatMessageAitContactViewHolderBinding binding;

    public AitContactHolder(@NonNull ChatMessageAitContactViewHolderBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  public interface OnItemListener {
    /** @param item null: @All */
    void onSelect(AitUserInfo item);
  }

  public static class AitContactConfig {
    float avatarCorner;
    int nameColor;

    int defaultAvatarRes;

    public AitContactConfig(float corner, int color, int avatarRes) {
      avatarCorner = corner;
      nameColor = color;
      defaultAvatarRes = avatarRes;
    }
  }
}
