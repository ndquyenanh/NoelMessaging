package com.qnv96.merrytext;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by qnv96 on 15-Dec-15.
 */
public class SizeDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private SeekBar mSeekBar;
    private TextView mTextView;
    private int mSize = -1;

    public SizeDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Select size for text");

        View view = View.inflate(getActivity(),R.layout.size_dialog,container);

        mSeekBar = (SeekBar) view.findViewById(R.id.pb_size);
        mTextView= (TextView) view.findViewById(R.id.tv_size);

        mSeekBar.setOnSeekBarChangeListener(this);

        int init = Utils.getInt(getActivity(),Define.SIZE);
        if (init != -1){
            mSeekBar.setProgress(init);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, init);
        }else {
            mSeekBar.setProgress(50);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        }

        Button ok_btn = (Button) view.findViewById(R.id.ok_size_btn);
        ok_btn.setOnClickListener(this);

        Button cancel_btn = (Button) view.findViewById(R.id.cancel_size_btn);
        cancel_btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
          mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ok_size_btn:
                mSize = mSeekBar.getProgress();
                if (mSize <= 1){
                    mSize = 1;
                }

                Utils.setInt(getActivity(), Define.SIZE, mSize);
                mOnSizeTextChanged.onChanged(mSize);
                dismiss();
                break;

            case R.id.cancel_size_btn:
                dismiss();
                break;

            default:
                break;
        }
    }

    public int getSize(){
        return mSize;
    }

    private OnSizeTextChanged mOnSizeTextChanged;


    public void setOnSizeTextChanged(OnSizeTextChanged onSizeTextChanged){
        mOnSizeTextChanged = onSizeTextChanged;
    }

    public interface OnSizeTextChanged{
        /**
         *
         * @param size
         */
        public void onChanged(int size);
    }
}
