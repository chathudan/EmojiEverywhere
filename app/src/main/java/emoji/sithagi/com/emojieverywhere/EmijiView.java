package emoji.sithagi.com.emojieverywhere;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import emojicon.EmojiconGridFragment;
import emojicon.EmojiconsFragment;
import emojicon.emoji.Emojicon;


public class EmijiView extends FragmentActivity implements View.OnTouchListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener {

    // this should app glob for recent emoji
    public final ArrayList<Emojicon> mEmojicons = new ArrayList<Emojicon>();
    /**
     * Checking keyboard height and keyboard visibility
     */
    int previousHeightDiffrence = 0;
    private EditText messageEd;
    private LinearLayout emojiIconsCover;
    private LinearLayout footer_layout_main;
    private ImageView emoticonsButton;
    private int keyboardHeight;
    private boolean isKeyBoardVisible;
    private boolean isEmojiVisible;
    private LinearLayout parentLayout;
    private ImageView sendButton;
    private String mChatMessage;
    private Handler mShowEmojiHandler = new Handler();
    private TextView messageTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_fragment);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        messageEd = (EditText) findViewById(R.id.edit_chat_message);
        messageTx = (TextView) findViewById(R.id.txt_sentMessage);
        parentLayout = (LinearLayout) findViewById(R.id.chatfragment_parent);
        emojiIconsCover = (LinearLayout) findViewById(R.id.footer_for_emojiicons);
        sendButton = (ImageView) findViewById(R.id.btn_chat_send);

        messageEd.setOnTouchListener(this);
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mChatMessage = messageEd.getText().toString();
                mChatMessage = mChatMessage.trim();
                if (!mChatMessage.equals("")) {
                    sendMessage(mChatMessage);
                    messageEd.setText("");
                }
                if (isEmojiVisible)
                    changeEmojiLayout();
            }
        });

        emoticonsButton = (ImageView) findViewById(R.id.btn_chat_emoji);
        emoticonsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmojiLayout();
            }
        });

        checkKeyboardHeight(parentLayout);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_emiji_view, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void sendMessage(String message) {
        messageTx.setText(message);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.edit_chat_message:
                if (isEmojiVisible) {
                    emoticonsButton
                            .setBackgroundResource(R.drawable.ic_vp_smileys);
                    emojiIconsCover
                            .setVisibility(LinearLayout.GONE);
                    isEmojiVisible = false;
                }
                break;

//            case R.id.list_chat:
//                if (isEmojiVisible) {
//                    emoticonsButton
//                            .setBackgroundResource(R.drawable.ic_vp_smileys);
//                    emojiIconsCover
//                            .setVisibility(LinearLayout.GONE);
//                    isEmojiVisible = false;
//                }
//                break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(messageEd);
    }

    @Override
    public void onEmojiconCallClicked(View v) {

    }

    @Override
    public void onEmojiconEmailClicked(View v) {

    }

    @Override
    public void onEmojiconFileTransferClicked(View v) {

    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(messageEd, emojicon);

        if (!mEmojicons.contains(emojicon))
            mEmojicons.add(emojicon);
    }

    private void showKeyboard(View view) {
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(view, 0);
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                .getWindowToken(), 0);
    }

    protected void changeEmojiLayout() {
        final InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		keyboard.showSoftInput(message, 0);
        if (isEmojiVisible && !isKeyBoardVisible) {
            emoticonsButton
                    .setBackgroundResource(R.drawable.ic_vp_smileys);
            emojiIconsCover
                    .setVisibility(LinearLayout.GONE);
            isEmojiVisible = false;
            mShowEmojiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    messageEd.requestFocus();
                    keyboard.showSoftInput(messageEd, 0);
                    checkKeyboardHeight(parentLayout);
                }
            }, 70);

        } else if (isEmojiVisible && isKeyBoardVisible) {

        } else if (!isEmojiVisible && isKeyBoardVisible) {
            hideKeyboard();
            mShowEmojiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    emoticonsButton
                            .setBackgroundResource(R.drawable.ic_vp_keypad);

                    emojiIconsCover
                            .setVisibility(LinearLayout.VISIBLE);
                    isEmojiVisible = true;
                }
            }, 70);
        } else if (!isEmojiVisible && !isKeyBoardVisible) {
            emoticonsButton
                    .setBackgroundResource(R.drawable.ic_vp_keypad);

            emojiIconsCover
                    .setVisibility(LinearLayout.VISIBLE);
            isEmojiVisible = true;
        }
    }

    private void checkKeyboardHeight(final View parentLayout) {

        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        parentLayout.getWindowVisibleDisplayFrame(r);

                        int screenHeight = parentLayout.getRootView()
                                .getHeight();
                        int heightDifference = screenHeight - (r.bottom);

                        if (previousHeightDiffrence - heightDifference > 50) {
//							popupWindow.dismiss(); TODO
                            emoticonsButton.setBackgroundResource(R.drawable.ic_vp_smileys);
                            emojiIconsCover.setVisibility(LinearLayout.GONE);
                        }
                        previousHeightDiffrence = heightDifference;
                        if (heightDifference > 100) {
                            isKeyBoardVisible = true;
                            changeKeyboardHeight(heightDifference);
                        } else {
                            isKeyBoardVisible = false;
                        }
                    }
                });

    }

    /**
     * change height of emoticons keyboard according to height of actual
     * keyboard
     *
     * @param height minimum height by which we can make sure actual keyboard is
     *               open or not
     */
    private void changeKeyboardHeight(int height) {

        if (height > 100) {
            keyboardHeight = height;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, keyboardHeight);
            emojiIconsCover.setLayoutParams(params);
        }

    }
}
