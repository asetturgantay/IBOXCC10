package www.kaznu.kz.projects.ibox.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import www.kaznu.kz.projects.ibox.R;

public class HomeDialogFragment extends DialogFragment {
    private ImageView infImage;
    private TextView infText;
    private TextView infnameText;



    public static HomeDialogFragment newInstance(boolean visible, String nameinfo, String info){
        Bundle bundle = new Bundle();
        bundle.putString("nameinfo", nameinfo);
        bundle.putString("info", info);
        bundle.putBoolean("visible", visible);
        HomeDialogFragment fragment = new HomeDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_dialog_fragment, container, false);

        infImage = root.findViewById(R.id.infImage);
        infText = root.findViewById(R.id.infText);
        infnameText = root.findViewById(R.id.infnameText);

        String nameinfo = getArguments().getString("nameinfo");
        String info = getArguments().getString("info");
        Boolean visible = getArguments().getBoolean("visible");
        if (info != null){
            infText.setText(info);
            infnameText.setText(nameinfo);
        }
        if(!visible)
            infImage.setVisibility(View.GONE);
        return root;
    }
}

