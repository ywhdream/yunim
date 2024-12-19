// Copyright (c) 2022 NetEase, Inc. All rights reserved.
// Use of this source code is governed by a MIT license that can be
// found in the LICENSE file.

package com.netease.yunxin.kit.chatkit.ui.normal.page;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.netease.yunxin.kit.chatkit.ui.R;
import com.netease.yunxin.kit.chatkit.ui.databinding.ChatReadStateLayoutBinding;
import com.netease.yunxin.kit.chatkit.ui.normal.page.fragment.ChatReaderFragment;
import com.netease.yunxin.kit.chatkit.ui.page.ChatReaderBaseActivity;
import com.netease.yunxin.kit.chatkit.ui.page.fragment.ChatReaderBaseFragment;

/** 标准皮肤，消息已读未读成员列表页面。 */
public class ChatReaderActivity extends ChatReaderBaseActivity {

  ChatReadStateLayoutBinding binding;

  @Override
  public ChatReaderBaseFragment getReadFragment() {
    return new ChatReaderFragment();
  }

  @Override
  public ChatReaderBaseFragment getUnreadFragment() {
    return new ChatReaderFragment();
  }

  @Override
  public void initViewAndSetContentView(@Nullable Bundle savedInstanceState) {
    changeStatusBarColor(R.color.color_white);
    binding = ChatReadStateLayoutBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    titleBarView = binding.title;
    tabLayout = binding.tabLayout;
    fragmentViewPager = binding.viewPager;
  }
}
