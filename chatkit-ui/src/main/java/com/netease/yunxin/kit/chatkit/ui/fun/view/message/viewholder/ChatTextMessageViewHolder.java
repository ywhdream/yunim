// Copyright (c) 2022 NetEase, Inc. All rights reserved.
// Use of this source code is governed by a MIT license that can be
// found in the LICENSE file.

package com.netease.yunxin.kit.chatkit.ui.fun.view.message.viewholder;

import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageType;
import com.netease.yunxin.kit.chatkit.IMKitConfigCenter;
import com.netease.yunxin.kit.chatkit.ui.R;
import com.netease.yunxin.kit.chatkit.ui.common.MessageHelper;
import com.netease.yunxin.kit.chatkit.ui.databinding.ChatBaseMessageViewHolderBinding;
import com.netease.yunxin.kit.chatkit.ui.databinding.FunChatMessageTextViewHolderBinding;
import com.netease.yunxin.kit.chatkit.ui.model.ChatMessageBean;
import com.netease.yunxin.kit.chatkit.ui.textSelectionHelper.SelectableTextHelper;

/** view holder for Text message */
public class ChatTextMessageViewHolder extends FunChatBaseMessageViewHolder {

  FunChatMessageTextViewHolderBinding textBinding;

  public ChatTextMessageViewHolder(@NonNull ChatBaseMessageViewHolderBinding parent, int viewType) {
    super(parent, viewType);
  }

  @Override
  public void addViewToMessageContainer() {
    textBinding =
        FunChatMessageTextViewHolderBinding.inflate(
            LayoutInflater.from(parent.getContext()), getMessageContainer(), true);
  }

  @Override
  public void bindData(ChatMessageBean message, ChatMessageBean lastMessage) {
    super.bindData(message, lastMessage);
    if (properties.getMessageTextSize() != null) {
      textBinding.messageText.setTextSize(properties.getMessageTextSize());
    }
    if (properties.getMessageTextColor() != null) {
      textBinding.messageText.setTextColor(properties.getMessageTextColor());
    }
    if (message.getMessageData().getMessage().getMessageType()
        == V2NIMMessageType.V2NIM_MESSAGE_TYPE_TEXT) {

      if (isForwardMsg() || !IMKitConfigCenter.getEnableAtMessage()) {
        MessageHelper.identifyFaceExpression(
            textBinding.getRoot().getContext(),
            textBinding.messageText,
            message.getMessageData().getMessage().getText(),
            ImageSpan.ALIGN_BOTTOM);
      } else {
        MessageHelper.identifyExpression(
            textBinding.getRoot().getContext(),
            textBinding.messageText,
            message.getMessageData().getMessage());
      }
    } else {
      //暂不支持消息展示提示信息
      textBinding.messageText.setText(
          parent.getContext().getResources().getString(R.string.chat_message_not_support_tips));
    }
    //    设置选中文本监听回调
    SelectableTextHelper.getInstance()
        .setSelectableOnChangeListener(
            (view, pos, msg, text, isSelectAll) -> {
              if (itemClickListener != null) {
                itemClickListener.onTextSelected(view, pos, msg, text.toString(), isSelectAll);
              }
            });
    //    设置长按事件
    if (!isMultiSelect) {
      textBinding.messageText.setOnLongClickListener(
          v -> {
            if (isMultiSelect) {
              return true;
            }
            SelectableTextHelper.getInstance()
                .showSelectView(
                    textBinding.messageText,
                    textBinding.messageText.getLayout(),
                    position,
                    message);
            return true;
          });
    } else {
      textBinding.messageText.setOnLongClickListener(null);
    }

    //    设置点击事件
    if (!isMultiSelect) {
      textBinding.messageText.setOnClickListener(v -> SelectableTextHelper.getInstance().dismiss());
    } else {
      textBinding.messageText.setOnClickListener(this::clickSelect);
    }
  }

  @Override
  public void onMessageRevokeStatus(ChatMessageBean data) {
    super.onMessageRevokeStatus(data);
    if (revokedViewBinding != null) {
      if (!MessageHelper.revokeMsgIsEdit(data)) {
        revokedViewBinding.tvAction.setVisibility(View.GONE);
      }
    }
  }
}
