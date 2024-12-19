package com.netease.yunxin.kit.chatkit.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.netease.yunxin.kit.chatkit.ui.R;
import com.netease.yunxin.kit.chatkit.ui.util.ConvertUtils;
import com.netease.yunxin.kit.chatkit.ui.util.DeviceUtils;
import com.netease.yunxin.kit.chatkit.ui.util.ResourceUtil;
import com.netease.yunxin.kit.chatkit.ui.util.StringUtil;


public class DialogHelper {
    public static DialogHelper dialogHelper;

    public static final int NO_THEME = -99;
    public static final int TRANS_THEME = -98;
    public static final int SIMPLE_OK = 0;
    private AlertDialog dlg;
    private boolean dialogOpened;

    private DialogInterface.OnDismissListener onDismissListener;

    /****************************************** Listener ******************************************/
    public interface DialogListener {
        void onPositiveButtonClicked(DialogHelper instance,
                                     DialogInterface dialog,
                                     int id,
                                     int requestCode);

        void onNegativeButtonClicked(DialogHelper instance,
                                     @Nullable DialogInterface dialog,
                                     int id,
                                     int requestCode);
    }

    /************************************* Dialog Controller **************************************/

    public static DialogHelper getInstance() {
        if (dialogHelper == null) {
            dialogHelper = new DialogHelper();
        }

        return dialogHelper;
    }

    public void closeDialog() {
        if (this.dialogOpened && dlg != null) {
            try {
                dlg.dismiss();
                dlg = null;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        this.dialogOpened = false;
    }

    public void openDialog() {
        if (!this.dialogOpened && dlg != null) {
            //try catch to avoid activity isFinishing();
            try {
                this.dialogOpened = true;
                dlg.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isDialogOpened() {
        return dialogOpened;
    }

    public void setDialogSize(int width, int height) {
        try {
            dlg.getWindow().setLayout(width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**************************************** Dialog Helper ***************************************/

    public static int transparentBg(Context context) {
        return ResourceUtil.getStyleId(context, "transparentBackgroundDialog");
    }

    public static int transparentBgLandscape(Context context) {
        return ResourceUtil.getStyleId(context, "transparentBackgroundDialogLandscape");
    }


    private AlertDialog.Builder getDialogBuilder(Context ctx, int themeResId) {
        switch (themeResId) {
            case NO_THEME:
                return new AlertDialog.Builder(ctx);
            case TRANS_THEME:
                return new AlertDialog.Builder(ctx, transparentBg(ctx));
            default:
                return new AlertDialog.Builder(ctx, themeResId);
        }
    }


    /************************************* Pending To Clean Up ************************************/

    public void showConfirmation(Context ctx,
                                 final int requestCode,
                                 String dialogTitle,
                                 String dialogMessage,
                                 boolean cancelable,
                                 String positiveButton,
                                 String negativeButton,
                                 final DialogListener listener,
                                 int layout) {

        //Close previous dialog
        if (ctx instanceof Activity && ((Activity) ctx).isFinishing()) {
            return;
        }

        closeDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, DialogHelper.transparentBg(ctx));

        View alertDialogView = View.inflate(ctx, layout, null);

        TextView tvDialogTitle = alertDialogView.findViewById(R.id.tvDialogTitle);

        if (dialogTitle != null) {
            tvDialogTitle.setText(dialogTitle);
        } else {
            tvDialogTitle.setVisibility(View.GONE);
        }

        TextView tvDialogMessage = alertDialogView.findViewById(R.id.tvDialogMessage);

        if (dialogMessage != null) {
            tvDialogMessage.setText(dialogMessage);
        } else {
            tvDialogMessage.setVisibility(View.GONE);
        }

        TextView positiveBtn = alertDialogView.findViewById(R.id.btnPositive);
        positiveBtn.setText(positiveButton);
        positiveBtn.setOnClickListener(
                view -> listener.onPositiveButtonClicked(this, null, 0, requestCode));


        if (!StringUtil.isEmpty(negativeButton)) {
            TextView negativeBtn = alertDialogView.findViewById(R.id.btnNegative);
            negativeBtn.setText(negativeButton);
            negativeBtn.setVisibility(View.VISIBLE);
            negativeBtn.setOnClickListener(
                    view -> listener.onNegativeButtonClicked(this, null, 0, requestCode));
        }

        ImageButton ibCloseButton = alertDialogView.findViewById(R.id.ibCloseButton);
        if (ibCloseButton != null) {
            ibCloseButton.setOnClickListener(view -> {
                if (dlg != null) {
                    dlg.dismiss();
                }
            });
        }

        builder.setView(alertDialogView);
        builder.setCancelable(cancelable);


        builder.setOnCancelListener(dialog -> closeDialog());
        dlg = builder.create();
        openDialog();
    }

    public void showAlert(Context ctx,
                          final int requestCode,
                          String dialogMessage,
                          boolean cancelable,
                          String positiveButton,
                          final DialogListener listener) {

        if (ctx instanceof Activity && ((Activity) ctx).isFinishing()) {
            return;
        }

        closeDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, DialogHelper.transparentBg(ctx));

        View alertDialogView = View.inflate(ctx, R.layout.dialog_common_alert, null);

        TextView title = alertDialogView.findViewById(R.id.tvDialogTitle);
        TextView message = alertDialogView.findViewById(R.id.tvDialogMessage);

        if (!TextUtils.isEmpty(dialogMessage)) {
            message.setText(dialogMessage);
        } else {
            message.setVisibility(View.GONE);
        }

        if (requestCode == 0) {
            title.setText(R.string.server_kicked_out);
        } else {
            title.setVisibility(View.GONE);
        }

        TextView firstBtn = alertDialogView.findViewById(R.id.btnFirstButton);
        firstBtn.setText(positiveButton);

        builder.setView(alertDialogView);
        builder.setCancelable(cancelable);

        //Set positive button
        if (requestCode != SIMPLE_OK) {
            firstBtn.setOnClickListener(
                    view -> listener.onPositiveButtonClicked(this, null, 0, requestCode));
        } else {
            firstBtn.setOnClickListener(view -> closeDialog());
        }

        builder.setOnCancelListener(dialog -> closeDialog());
        dlg = builder.create();
        openDialog();
    }

    public void showImageAlertDialog(Context ctx,
                                     final int requestCode,
                                     String dialogTitle,
                                     int dialogImage,
                                     String dialogContent,
                                     boolean cancelable,
                                     String positiveButton,
                                     final DialogListener listener) {
        //Close previous dialog

        if (ctx instanceof Activity && ((Activity) ctx).isFinishing()) {
            return;
        }

        closeDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, DialogHelper.transparentBg(ctx));

        View alertDialogView = View.inflate(ctx, R.layout.dialog_common_image_alert, null);

        TextView title = alertDialogView.findViewById(R.id.tvDialogTitle);

        if (!TextUtils.isEmpty(dialogTitle)) {
            title.setText(dialogTitle);
        } else {
            title.setVisibility(View.GONE);
        }

        TextView tvDialogMessage = alertDialogView.findViewById(R.id.tvDialogMessage);
        if (!TextUtils.isEmpty(dialogContent)) {
            tvDialogMessage.setText(dialogContent);
        } else {
            tvDialogMessage.setVisibility(View.GONE);
        }


        ImageButton ibCloseButton = alertDialogView.findViewById(R.id.ibCloseButton);
        ibCloseButton.setOnClickListener(view -> {
            if (dlg != null) {
                dlg.dismiss();
            }
        });
        if (!cancelable) {
            ibCloseButton.setVisibility(View.GONE);
        }

        AppCompatImageView ivImage = alertDialogView.findViewById(R.id.ivMessageImage);
        ivImage.setImageResource(dialogImage);

        TextView firstBtn = alertDialogView.findViewById(R.id.btnFirstButton);
        firstBtn.setText(positiveButton);

        builder.setView(alertDialogView);
        builder.setCancelable(cancelable);

        //Set positive button
        if (requestCode != SIMPLE_OK) {
            firstBtn.setOnClickListener(
                    view -> listener.onPositiveButtonClicked(this, null, 0, requestCode));
        } else {
            firstBtn.setOnClickListener(view -> closeDialog());
        }

        builder.setOnCancelListener(dialog -> closeDialog());
        dlg = builder.create();
        openDialog();
    }

    public void showCustomDialog(Context ctx,
                                 final int requestCode,
                                 View customView,
                                 boolean cancelable,
                                 View positiveButton,
                                 View negativeButton,
                                 final DialogListener listener,
                                 int themeResId) {

        if (ctx instanceof Activity && ((Activity) ctx).isFinishing()) {
            return;
        }

        closeDialog();

        AlertDialog.Builder builder = getDialogBuilder(ctx, themeResId);
        builder.setView(customView);
        builder.setCancelable(cancelable);

        //Set positive button
        if (requestCode != SIMPLE_OK && positiveButton != null) {
            positiveButton.setOnClickListener(
                    view -> listener.onPositiveButtonClicked(this, null, 0, requestCode));

        }
        //Set negative button
        if (requestCode != SIMPLE_OK && negativeButton != null) {
            negativeButton.setOnClickListener(
                    view -> listener.onNegativeButtonClicked(this, null, 0, requestCode));
        }

        builder.setOnCancelListener(dialog -> closeDialog());

        if (onDismissListener != null) {
            builder.setOnDismissListener(onDismissListener);
        }

        dlg = builder.create();
        openDialog();
    }

    public void showCustomDialog(Context context,
                                 View dialogView,
                                 View dismissButton,
                                 View dismissView,
                                 boolean cancelable) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }

        if (dialogView == null)
            return;

        //check and close any present dialog
        closeDialog();

        if (dismissButton != null) {
            dismissButton.setOnClickListener(view -> {
                closeDialog();
            });
        }

        if (dismissView != null) {
            dismissView.setOnClickListener(view -> {
                closeDialog();
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context,
                DialogHelper.transparentBg(context));
        builder.setView(dialogView);
        builder.setCancelable(cancelable);
        builder.setOnCancelListener(dialog -> closeDialog());
        dlg = builder.create();


        openDialog();
    }

    public void showAppDetailSettings(Activity activity, String title, String itemOne) {
        showAppDetailSettings(activity, title, itemOne, (dialogInterface, i) -> {
                    DeviceUtils.openAppDetail(activity);
                    dialogInterface.dismiss();
                }
        );
    }

    public void showAppDetailSettings(Activity activity, String title, String itemOne, DialogInterface.OnClickListener positive) {
        new AlertDialog.Builder(activity, R.style.AlertDialogStyle)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(itemOne)
                .setPositiveButton(R.string.setting_permission, positive)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    public void showStickerOfTheDayResult(Context mContext,
                                          int downloadPoints,
                                          float tipPoints,
                                          float totalPoints) {
        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return;
        }

        closeDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                DialogHelper.transparentBg(mContext));

        View alertDialogView = View.inflate(mContext, R.layout.dialog_common_sticker_of_the_day,
                null);

        TextView tvDownloaderCount = alertDialogView.findViewById(R.id.tvDownloaderCount);
        TextView tvTotalTipsCount = alertDialogView.findViewById(R.id.tvTotalTipsCount);
        TextView tvTotalPointCount = alertDialogView.findViewById(R.id.tvTotalPointCount);
        ImageButton ibCloseButton = alertDialogView.findViewById(R.id.ibCloseButton);

        tvDownloaderCount.setText(ConvertUtils.numberConvert(downloadPoints));
        tvTotalTipsCount.setText(ConvertUtils.numberConvert(Math.round(tipPoints)));
        tvTotalPointCount.setText(ConvertUtils.numberConvert((int) totalPoints));

        ibCloseButton.setOnClickListener(view -> {
            if (dlg != null) {
                dlg.dismiss();
            }
        });

        builder.setView(alertDialogView);
        builder.setCancelable(true);

        builder.setOnCancelListener(dialog -> closeDialog());
        dlg = builder.create();
        openDialog();
    }

    public void showInvalidTransactionDialog(
            Context mContext,
            String messageDescription,
            final DialogListener listener
    ) {
        View dialogView = View.inflate(mContext, R.layout.dialog_common_confirmation_vertical_new2, null);

        ImageButton ibCloseButton = dialogView.findViewById(R.id.ibCloseButton);
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        TextView tvDialogMessage = dialogView.findViewById(R.id.tvDialogMessage);
        CheckBox checkBox = dialogView.findViewById(R.id.checkbox_term);
        Button btnPositive = dialogView.findViewById(R.id.btnPositive);
        TextView btnNegative = dialogView.findViewById(R.id.btnNegative);

        btnNegative.setVisibility(View.GONE);
        ibCloseButton.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);
        btnPositive.setEnabled(true);
        btnPositive.setBackground(ContextCompat.getDrawable(mContext, R.drawable.solid_bg_blue_xr));

        tvDialogTitle.setVisibility(View.VISIBLE);
        tvDialogTitle.setText(mContext.getString(R.string.rw_invalid_transaction_text));

        tvDialogMessage.setVisibility(View.VISIBLE);
        tvDialogMessage.setText(messageDescription);

        btnPositive.setText(mContext.getString(R.string.ok));
        btnPositive.setOnClickListener(view -> {
                    listener.onPositiveButtonClicked(this, null, 0, 0);
                    ibCloseButton.performClick();
                }
        );

        showCustomDialog(
                mContext,
                dialogView,
                ibCloseButton,
                null,
                false
        );
    }

    public void setDialogOnDismissListener(DialogInterface.OnDismissListener listener) {
        onDismissListener = listener;
    }
}
