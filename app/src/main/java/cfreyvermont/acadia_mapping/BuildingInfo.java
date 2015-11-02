package cfreyvermont.acadia_mapping;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class BuildingInfo extends Fragment {
    private static final String ARG_BUILDINGNAME = "buildingName";
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }

    /*
     * Our buildingInfo images were designed using the same colour scheme as the Google
     * Map in order to complement the map, and provide a consistent user experience.
     * We outlined the image of the building in order to define an edge between the
     * building and the map. The black on tan text is easily readable on all screen sizes
     * and the indentation of the text along the diagonal makes it clear which text should
     * be read first. We also aimed for consistency with the buildingInfo pop-ups, so they
     * all look the same, allowing for a user familiar with our app to quickly identify
     * the data they are looking for.
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        return inflater.inflate(R.layout.fragment_building_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ImageView i = (ImageView) getView().findViewById(R.id.imageInfo);
        if (bundle != null) {
            Log.i("BuildingInfo->onCreate:", "have saved data");
            String buildingName = bundle.getString(ARG_BUILDINGNAME);
            Log.i("BUILDINGNAME:", buildingName);
            int resourceID;

            if (buildingName != null) {
                try {
                    Class draw = R.drawable.class;
                    Field field = draw.getField(buildingName.toLowerCase());
                    resourceID = field.getInt(null);
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "No infographic found for " + buildingName,
                            Toast.LENGTH_SHORT).show();
                    Log.e("Error:", "resource not found");
                    return;
                }
                Drawable d = getResources().getDrawable(resourceID);
                i.setImageDrawable(d);
            }
        }
    }
}
