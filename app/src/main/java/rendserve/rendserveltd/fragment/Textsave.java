package rendserve.rendserveltd.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import rendserve.rendserveltd.R;


public class Textsave extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    public EditText editText11;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this rendserve.rendserveltd.fragment
        view =  inflater.inflate(R.layout.fragment_textsave, container, false);

        initializer();
        return view;
    }

    private void initializer() {
        editText11= (EditText) view.findViewById(R.id.editText11 );
    }

    public String getText(){
        //editText11= (EditText) view.findViewById(R.id.editText11 );
        if (editText11==null)
            return "";
        else
        return editText11.getText().toString().trim();
    }

}
