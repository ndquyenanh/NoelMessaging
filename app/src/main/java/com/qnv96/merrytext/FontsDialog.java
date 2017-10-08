package com.qnv96.merrytext;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qnv96 on 15-Dec-15.
 */
public class FontsDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    private GridView mGridView;
    private FontAdapter mAdapter;
    private List<Typeface> mTypefaces;

    public FontsDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Select font for text");
        View view = inflater.inflate(R.layout.fonts_layout, container, false);
        mGridView = (GridView) view.findViewById(R.id.font_gv);

        Activity activity = getActivity();
        mTypefaces = Utils.loadFonts(activity);

        mAdapter = new FontAdapter(mTypefaces, activity);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(this);
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

    private OnFontChanged mOnFontChanged;

    public void setOnFontChanged(OnFontChanged onFontChanged){
        mOnFontChanged = onFontChanged;
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (mOnFontChanged != null){
            mOnFontChanged.onChanged(mTypefaces.get(position));
            Utils.setInt(getActivity(),Define.FONT, position);
        }

        dismiss();
    }

    public interface OnFontChanged{

        /**
         * changed font
         * @param typeface
         */
        public void onChanged(Typeface typeface);
    }

}
