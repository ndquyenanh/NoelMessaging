package com.qnv96.merrytext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qnv96.floatingmenu.FloatingActionButton;
import com.qnv96.floatingmenu.FloatingActionMenu;
import com.qnv96.utils.ColorPickerDialog;
import com.qnv96.utils.ColorPickerSwatch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QActivity extends AppCompatActivity implements SizeDialog.OnSizeTextChanged, FontsDialog.OnFontChanged {

    private TextView mTextView;
    private EditText mEditText;

    private int mColor = -1;
    private ColorPickerDialog mPickerDialog;

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;

    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();
    private Context mContext;

    private SizeDialog mSizeDialog;
    private FontsDialog mFontsDialog;

    private ShareActionProvider mActionProvider;

    private File mFile;
    private FloatingActionMenu mActionMenu;
    private int mNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q);

        mTextView = (TextView) findViewById(R.id.tvMsg);
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        mEditText = (EditText) findViewById(R.id.evTxt);

        int[] val = Utils.initValues(this);
        if (val[0] != -1 && val[0] <= 50) {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, val[0]);
        } else {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
            Utils.setInt(this,Define.SIZE, 50);
        }

        if (val[1] != -1) {
            mTextView.setTypeface(Utils.loadFonts(this).get(val[1]));
        } else {
            mTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/f1.ttf"));
            Utils.setInt(this, Define.FONT, 1);
        }

        if (val[2] != -1) {
            mColor = val[2];
            mTextView.setTextColor(mColor);
        } else {
            mColor = Color.BLUE;
            mTextView.setTextColor(mColor);
            Utils.setInt(this, Define.COLOR, mColor);
        }

        mSizeDialog = new SizeDialog();
        mSizeDialog.setOnSizeTextChanged(this);

        mFontsDialog = new FontsDialog();
        mFontsDialog.setOnFontChanged(this);


        mPickerDialog = new ColorPickerDialog();
        mPickerDialog.initialize(R.string.select_color, Utils.COLORS, mColor, 4, 2);
        mPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int i) {
                mColor = i;
                mTextView.setDrawingCacheEnabled(false);
                mTextView.setTextColor(mColor);
                Utils.setInt(mContext, Define.COLOR, mColor);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextView.setDrawingCacheEnabled(false);

                CharSequence c = s.toString().trim();
                if (c.length() > 60) {
                    c = s.subSequence(0, 60);
                    // Toast.makeText(mContext, "Please don't write message overcome 50 letters", Toast.LENGTH_SHORT).show();
                }

                mTextView.setText(c + " ");
            }
        });

        init_floating_menu();

        mNum = Utils.getInt(this,Define.NUM_SMS);
        mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/noel_sms" + mNum + ".png");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mNum == -1)
            Toast.makeText(this,"On Android M, please enable Storage permission in App Setting", Toast.LENGTH_LONG).show();
    }

    private void init_floating_menu() {
        mContext = this;

        mActionMenu = (FloatingActionMenu) findViewById(R.id.menu1);
        mActionMenu.setOnMenuButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mTextView.setDrawingCacheEnabled(false);
                if (mActionMenu.isOpened()) {
                    Toast.makeText(mContext, mActionMenu.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                mActionMenu.toggle(true);
            }
        });

        android.view.ContextThemeWrapper context = new android.view.ContextThemeWrapper(this, R.style.MenuButtonsStyle);
        menus.add(mActionMenu);

        mActionMenu.hideMenuButton(false);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {

            mUiHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);

            delay += 150;
        }

        mActionMenu.setClosedOnTouchOutside(true);


        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        // fab1.setEnabled(false);

        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);
        fab4.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String text = "";

            switch (v.getId()) {
                case R.id.fab1:
                    text = fab1.getLabelText();
                    mPickerDialog.show(getSupportFragmentManager(), Define.COLOR_DIALOG);
                    mActionMenu.close(true);
                    break;

                case R.id.fab2:
                    text = fab2.getLabelText();
                    mFontsDialog.show(getSupportFragmentManager(), Define.FONT_DIALOG);
                    mActionMenu.close(true);
                    break;

                case R.id.fab3:
                    text = fab3.getLabelText();
                    mSizeDialog.show(getSupportFragmentManager(), Define.SIZE_DIALOG);
                    mActionMenu.close(true);
                    break;

                case R.id.fab4:
                    text = fab4.getLabelText();
                    sendImg();
                    mActionMenu.close(true);
                    break;

                default:
                    break;
            }

            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_q, menu);

        MenuItem item = menu.findItem(R.id.action_share);
        mActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);

        Intent intent = createShareIntent();
        if (intent != null)
            mActionProvider.setShareIntent(intent);

        mActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {

            @Override
            public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {

                String msg = mTextView.getText().toString();

                if (msg.trim().equals("")) {
                    Toast.makeText(mContext, "Please write a message", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (resetFile()) {
                    saveImg();
                }

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChanged(int size) {
        if (size != -1) {
            mTextView.setDrawingCacheEnabled(false);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

    /**
     * changed font
     *
     * @param typeface
     */
    @Override
    public void onChanged(Typeface typeface) {
        mTextView.setDrawingCacheEnabled(false);
        mTextView.setTypeface(typeface);

    }

    private void sendImg() {
        String msg = mTextView.getText().toString();

        if (msg.trim().equals("")) {
            Toast.makeText(this, "Please write a message", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!resetFile()) {
            return;
        }

        saveImg();

        Uri uri = Uri.fromFile(mFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        // shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(shareIntent, "Select an app"));
    }

    private Intent createShareIntent() {
        String msg = mTextView.getText().toString();

        if (msg.trim().equals("")) {
            Toast.makeText(this, "Please write a message", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (!resetFile()) {
            return null;
        }

        saveImg();

        Uri uri = Uri.fromFile(mFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        // shareIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        return shareIntent;
    }

    private void saveImg() {
        mTextView.setDrawingCacheEnabled(true);

        mTextView.buildDrawingCache();

        try {
            FileOutputStream stream = new FileOutputStream(mFile);
            Bitmap bitmap = mTextView.getDrawingCache();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            stream.flush();
            stream.close();
            // bitmap.recycle();
        } catch (Exception ex) {
            Log.e("qnv96", ex.getLocalizedMessage());
        }
    }

    @Override
    protected void onDestroy() {

        if (mFile.exists()) {
            // mFile.delete();
        }

        mNum++;
        Utils.setInt(this,Define.NUM_SMS,mNum);
        super.onDestroy();
    }

    private boolean resetFile() {
        if (mFile.exists()) {
            mFile.delete();
        }

        try {
            boolean b = mFile.createNewFile();
            if (!b) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean mIsBack = false;

    @Override
    public void onBackPressed() {
        if (mIsBack) {
            super.onBackPressed();
            return;
        }

        mIsBack = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mIsBack = false;
            }
        }, 2000);
    }
}
