package emojicon;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;

import emoji.sithagi.com.emojieverywhere.R;
import emojicon.emoji.Emojicon;
import emojicon.emoji.Nature;
import emojicon.emoji.Objects;
import emojicon.emoji.People;
import emojicon.emoji.Places;
import emojicon.emoji.Symbols;

//import com.actionbarsherlock.app.SherlockFragment;


/**
 * @author Chathura Wijesinghe (cdanasiri@gmail.com)
 */
public class EmojiconsFragment extends Fragment implements ViewPager.OnPageChangeListener {
    public static Emojicon[] RECENT_DATA = new Emojicon[]{};
    private static LinearLayout lin_emoji_popup_buttons;
    private OnEmojiconBackspaceClickedListener mOnEmojiconBackspaceClickedListener;
    private int mEmojiTabLastSelectedIndex = -1;
    private View[] mEmojiTabs;

    public static void input(EditText editText, Emojicon emojicon) {
        if (editText == null || emojicon == null) {
            return;
        }

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            editText.append(emojicon.getEmoji());
        } else {
            editText.getText().replace(Math.min(start, end), Math.max(start, end), emojicon.getEmoji(), 0, emojicon.getEmoji().length());
        }
    }

    public static void backspace(EditText editText) {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }

//    private void getRecentEmoji() {
//		ArrayList<Emojicon> emojicons = VoipemApplication.getInstance().mEmojicons;
//		if(emojicons.size()>0){
//			RECENT_DATA = new Emojicon[emojicons.size()];
//			for (int i=0;i<emojicons.size();i++) {
//				RECENT_DATA[i] = emojicons.get(i);
//			}
//		}
//
//	}

    public static void hideButtonsBar() {
        lin_emoji_popup_buttons.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//    	getRecentEmoji();

        View view = inflater.inflate(R.layout.emojicons, container, false);
        final ViewPager emojisPager = (ViewPager) view.findViewById(R.id.emojis_pager);
        emojisPager.setOnPageChangeListener(this);
        EmojisPagerAdapter emojisAdapter = new EmojisPagerAdapter(getFragmentManager(), Arrays.asList(
                EmojiconGridFragment.newInstance(RECENT_DATA),
                EmojiconGridFragment.newInstance(People.DATA),
                EmojiconGridFragment.newInstance(Nature.DATA),
                EmojiconGridFragment.newInstance(Objects.DATA),
                EmojiconGridFragment.newInstance(Places.DATA),
                EmojiconGridFragment.newInstance(Symbols.DATA)
        ));
        emojisPager.setAdapter(emojisAdapter);

        mEmojiTabs = new View[6];

        mEmojiTabs[0] = view.findViewById(R.id.emojis_tab_00_recent);
        mEmojiTabs[1] = view.findViewById(R.id.emojis_tab_0_people);
        mEmojiTabs[2] = view.findViewById(R.id.emojis_tab_1_nature);
        mEmojiTabs[3] = view.findViewById(R.id.emojis_tab_2_objects);
        mEmojiTabs[4] = view.findViewById(R.id.emojis_tab_3_cars);
        mEmojiTabs[5] = view.findViewById(R.id.emojis_tab_4_punctuation);
        for (int i = 0; i < mEmojiTabs.length; i++) {
            final int position = i;
            mEmojiTabs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emojisPager.setCurrentItem(position);
                }
            });
        }
        view.findViewById(R.id.img_chat_popup_back).setOnTouchListener(new RepeatListener(1000, 50, new View.OnClickListener() {
            //        	R.id.emojis_backspace
            @Override
            public void onClick(View v) {
                if (mOnEmojiconBackspaceClickedListener != null) {
                    mOnEmojiconBackspaceClickedListener.onEmojiconBackspaceClicked(v);
                }
            }
        }));

        view.findViewById(R.id.img_chat_popup_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEmojiconBackspaceClickedListener != null) {
                    mOnEmojiconBackspaceClickedListener.onEmojiconCallClicked(v);
                }
            }
        });

        view.findViewById(R.id.img_chat_popup_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEmojiconBackspaceClickedListener != null) {
                    mOnEmojiconBackspaceClickedListener.onEmojiconEmailClicked(v);
                }
            }
        });
        view.findViewById(R.id.img_chat_popup_filetransfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEmojiconBackspaceClickedListener != null) {
                    mOnEmojiconBackspaceClickedListener.onEmojiconFileTransferClicked(v);
                }
            }
        });

        final LinearLayout lin_chat_button_back;
        final LinearLayout lin_chat_button1;
        final LinearLayout lin_chat_button2;
        final LinearLayout lin_chat_button3;
        final LinearLayout lin_chat_button4;
        final LinearLayout lin_chat_button5;
        final LinearLayout lin_chat_button6;

        lin_emoji_popup_buttons = (LinearLayout) view
                .findViewById(R.id.lin_emoji_popup_buttons);

        lin_chat_button_back = (LinearLayout) view
                .findViewById(R.id.lin_chat_button_back);
        lin_chat_button1 = (LinearLayout) view
                .findViewById(R.id.lin_chat_button1);
        lin_chat_button2 = (LinearLayout) view
                .findViewById(R.id.lin_chat_button2);
        lin_chat_button3 = (LinearLayout) view
                .findViewById(R.id.lin_chat_button3);
        lin_chat_button4 = (LinearLayout) view
                .findViewById(R.id.lin_chat_button4);
        lin_chat_button5 = (LinearLayout) view
                .findViewById(R.id.lin_chat_button5);
        lin_chat_button6 = (LinearLayout) view
                .findViewById(R.id.lin_chat_button6);

        ViewTreeObserver vto = lin_chat_button_back.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                ViewGroup.MarginLayoutParams vlp = (ViewGroup.MarginLayoutParams) lin_chat_button_back
                        .getLayoutParams();
                int height = lin_chat_button_back.getMeasuredHeight();
                int width = lin_chat_button_back.getMeasuredWidth();
                LinearLayout.LayoutParams layoutParams;

                ViewGroup.MarginLayoutParams vmRight = (ViewGroup.MarginLayoutParams) lin_chat_button1
                        .getLayoutParams();

                layoutParams = new LinearLayout.LayoutParams(width
                        - vmRight.rightMargin, height);
                layoutParams.setMargins(0, 0, 2, 0);

                lin_chat_button1.setLayoutParams(layoutParams);
                lin_chat_button2.setLayoutParams(layoutParams);
                lin_chat_button3.setLayoutParams(layoutParams);
                lin_chat_button4.setLayoutParams(layoutParams);
                lin_chat_button5.setLayoutParams(layoutParams);
                lin_chat_button6.setLayoutParams(layoutParams);
            }
        });
        onPageSelected(0);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (getActivity() instanceof OnEmojiconBackspaceClickedListener) {
            mOnEmojiconBackspaceClickedListener = (OnEmojiconBackspaceClickedListener) getActivity();
        } else {
            throw new IllegalArgumentException(activity + " must implement interface " + OnEmojiconBackspaceClickedListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        mOnEmojiconBackspaceClickedListener = null;
        super.onDetach();
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        if (mEmojiTabLastSelectedIndex == i) {
            return;
        }
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                if (mEmojiTabLastSelectedIndex >= 0 && mEmojiTabLastSelectedIndex < mEmojiTabs.length) {
                    mEmojiTabs[mEmojiTabLastSelectedIndex].setSelected(false);
                }
                mEmojiTabs[i].setSelected(true);
                mEmojiTabLastSelectedIndex = i;
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    public interface OnEmojiconBackspaceClickedListener {
        void onEmojiconBackspaceClicked(View v);

        void onEmojiconCallClicked(View v);

        void onEmojiconEmailClicked(View v);

        void onEmojiconFileTransferClicked(View v);
    }

    private static class EmojisPagerAdapter extends FragmentStatePagerAdapter {
        private List<EmojiconGridFragment> fragments;

        public EmojisPagerAdapter(FragmentManager fm, List<EmojiconGridFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    /**
     * A class, that can be used as a TouchListener on any view (e.g. a Button).
     * It cyclically runs a clickListener, emulating keyboard-like behaviour. First
     * click is fired immediately, next before initialInterval, and subsequent before
     * normalInterval.
     * <p/>
     * <p>Interval is scheduled before the onClick completes, so it has to run fast.
     * If it runs slow, it does not generate skipped onClicks.
     */
    public static class RepeatListener implements View.OnTouchListener {

        private final int normalInterval;
        private final View.OnClickListener clickListener;
        private Handler handler = new Handler();
        private int initialInterval;
        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                if (downView == null) {
                    return;
                }
                handler.removeCallbacksAndMessages(downView);
                handler.postAtTime(this, downView, SystemClock.uptimeMillis() + normalInterval);
                clickListener.onClick(downView);
            }
        };

        private View downView;

        /**
         * @param initialInterval The interval before first click event
         * @param normalInterval  The interval before second and subsequent click
         *                        events
         * @param clickListener   The OnClickListener, that will be called
         *                        periodically
         */
        public RepeatListener(int initialInterval, int normalInterval, View.OnClickListener clickListener) {
            if (clickListener == null)
                throw new IllegalArgumentException("null runnable");
            if (initialInterval < 0 || normalInterval < 0)
                throw new IllegalArgumentException("negative interval");

            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downView = view;
                    handler.removeCallbacks(handlerRunnable);
                    handler.postAtTime(handlerRunnable, downView, SystemClock.uptimeMillis() + initialInterval);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    handler.removeCallbacksAndMessages(downView);
                    downView = null;
                    return true;
            }
            return false;
        }
    }
}
