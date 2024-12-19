// Copyright (c) 2022 NetEase, Inc. All rights reserved.
// Use of this source code is governed by a MIT license that can be
// found in the LICENSE file.

package com.netease.yunxin.kit.chatkit.ui.normal.view.message.viewholder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageLocationAttachment;
import com.netease.yunxin.kit.alog.ALog;
import com.netease.yunxin.kit.chatkit.ui.ChatKitClient;
import com.netease.yunxin.kit.chatkit.ui.ChatKitUIConstant;
import com.netease.yunxin.kit.chatkit.ui.R;
import com.netease.yunxin.kit.chatkit.ui.common.MessageHelper;
import com.netease.yunxin.kit.chatkit.ui.databinding.ChatBaseMessageViewHolderBinding;
import com.netease.yunxin.kit.chatkit.ui.databinding.NormalChatMessageLocationViewHolderBinding;
import com.netease.yunxin.kit.chatkit.ui.model.ChatMessageBean;

public class ChatLocationMessageViewHolder extends NormalChatBaseMessageViewHolder {
  public static final String TAG = "ChatLocationMessageViewHolder";
  NormalChatMessageLocationViewHolderBinding binding;
  V2NIMMessageLocationAttachment attachment;

  public ChatLocationMessageViewHolder(
      @NonNull ChatBaseMessageViewHolderBinding parent, int viewType) {
    super(parent, viewType);
  }

  @Override
  public void addViewToMessageContainer() {
    binding =
        NormalChatMessageLocationViewHolderBinding.inflate(
            LayoutInflater.from(parent.getContext()), getMessageContainer(), true);
  }

  @Override
  public void bindData(ChatMessageBean message, ChatMessageBean lastMessage) {
    ALog.d(
        ChatKitUIConstant.LIB_TAG,
        TAG,
        "bindData" + "title" + message.getMessageData().getMessage().getText());
    super.bindData(message, lastMessage);
    attachment =
        (V2NIMMessageLocationAttachment) message.getMessageData().getMessage().getAttachment();
    if (attachment == null) {
      return;
    }
    binding.locationItemTitle.setText(message.getMessageData().getMessage().getText());
    binding.locationItemAddress.setText(attachment.getAddress());
    if (itemClickListener != null) {
      binding.locationClick.setOnClickListener(
          v -> itemClickListener.onMessageClick(v, position, currentMessage));
    }

    String path = null;
    if (ChatKitClient.getMessageMapProvider() != null) {
      path =
          ChatKitClient.getMessageMapProvider()
              .getChatMpaItemImage(attachment.getLatitude(), attachment.getLongitude());
      if (!TextUtils.isEmpty(path)) {
        binding.locationMapMarkerIv.setVisibility(View.VISIBLE);
        Glide.with(binding.locationItemMapView.getContext())
            .load(path)
            .into(binding.locationItemMapIv)
            .onLoadFailed(
                parent
                    .getContext()
                    .getResources()
                    .getDrawable(R.drawable.ic_chat_location_default));
      }
    }

    if (TextUtils.isEmpty(path)) {
      binding.locationItemMapIv.setImageResource(R.drawable.ic_chat_location_default);
      binding.locationMapMarkerIv.setVisibility(View.GONE);
    }
  }

  @Override
  protected void onMessageBackgroundConfig(ChatMessageBean messageBean) {
    super.onMessageBackgroundConfig(messageBean);
    if (!messageBean.isRevoked()) {
      boolean isReceivedMsg = MessageHelper.isReceivedMessage(messageBean);
      if (isReceivedMsg) {
        baseViewBinding.contentWithTopLayer.setBackgroundResource(
            R.drawable.chat_message_stroke_other_bg);
      } else {
        baseViewBinding.contentWithTopLayer.setBackgroundResource(
            R.drawable.chat_message_stoke_self_bg);
      }
    }
    baseViewBinding.messageContainer.setPadding(0, 0, 0, 0);
  }
}
