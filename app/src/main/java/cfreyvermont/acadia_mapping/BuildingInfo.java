package cfreyvermont.acadia_mapping;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BuildingInfo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_BUILDINGNAME = "buildingName";

    // TODO: Rename and change types of parameters
    private String buildingName;
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_building_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ImageView i = (ImageView) getView().findViewById(R.id.imageInfo);
        if (bundle != null) {
            Log.i("BuildingInfo->onCreate:", "have saved data");
            buildingName = bundle.getString(ARG_BUILDINGNAME);
            //mParam2 = getArguments().getString(ARG_PARAM2);
            Log.i("BUILDINGNAME:", buildingName);
            if (buildingName.equals("KCIC")) {
                Log.i("onCreate,BuildingInfo:", "Checked for KCIC");
                Drawable d = getResources().getDrawable(R.drawable.kcic);
                i.setImageDrawable(d);
            }
        }
    }
}
