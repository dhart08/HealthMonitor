package com.example.daveh.healthmonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {

    private GraphView myGraphView;
    private String[] horLabels  = {"2700", "2750", "2800", "2850", "2900", "2950",
                                    "3000", "3050", "3100"};;
    private String[] verLabels = {"0", "500", "1000", "1500", "2000"};
    private float[] values = {500, 1000, 2000, 1500, 1000, 500};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.myLayout);

        //Creates new GraphView in the layout
        myGraphView = new GraphView(this, layout, null, "Health Monitor", horLabels, verLabels, true);
        myGraphView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        myGraphView.setTag("myGraphView");
        layout.addView(myGraphView);

        //Add the controls to the graph
        myGraphView.addControls(this, layout);

    }
}
