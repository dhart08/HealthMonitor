package com.example.daveh.healthmonitor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * GraphView creates a scaled line or bar graph with x and y axis labels.
 * Author: David Hartzog
 * Class: CSE535
 */

public class GraphView extends View implements View.OnClickListener {

    public static boolean BAR = false;
    public static boolean LINE = true;

    private GraphView myGraph;
    private Paint paint;
    private Context context;
    private Canvas canvas;
    private RelativeLayout layout;
    private ArrayList<Float> values = null;
    private String[] horlabels;
    private String[] verlabels;
    private String title;
    private boolean type;
    private float border, leftGraphBorder, rightGraphBorder, topGraphBorder, bottomGraphBorder;
    private float height, width, diff;
    private float graphHeight, graphWidth;
    private float min, max;
    private boolean cancel = true;
    private Random rand = new Random();

    private final int array_size = 50;


    public GraphView(Context context, RelativeLayout layout, ArrayList<Float> vals, String title,
                     String[] horlabels, String[] verlabels, boolean type) {
        super(context);
        if (vals == null) {
            values = new ArrayList<Float>();

            //Fill the line graph data with dummy values
            for (int i = 0; i < array_size; i++) {
                values.add((float) rand.nextInt(2000));
            }
        }
        else
            values = vals;

        if (title == null)
            title = "";
        else
            this.title = title;

        if (horlabels == null)
            this.horlabels = new String[0];
        else
            this.horlabels = horlabels;

        if (verlabels == null)
            this.verlabels = new String[0];
        else
            this.verlabels = verlabels;

        this.type = type;
        paint = new Paint();
        this.context = context;
        this.layout = layout;
        myGraph = this;
    }

    public void setValues(ArrayList<Float> newValues)
    {
        this.values = newValues;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        border = 60;
        rightGraphBorder = border / 3;
        leftGraphBorder = border * 2;
        topGraphBorder = border * 6;
        bottomGraphBorder = border;
        height = getHeight();
        width = getWidth();
        diff = max - min;
        graphHeight = height - (topGraphBorder + bottomGraphBorder);
        graphWidth = width - (leftGraphBorder + rightGraphBorder);
        max = getMax();
        min = getMin();

        //Draw view background
        Rect rect = new Rect(0,0,getWidth(), getHeight());
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);

        //Draw top control box
        rect = new Rect(0, 0, getWidth(), getHeight() / 4);
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);

        //Draw control labels
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText("Patient ID", 150, 75, paint);
        canvas.drawText("Age", 405, 75, paint);
        canvas.drawText("Name", 620, 75, paint);
        canvas.drawText("Sex", 960, 60, paint);

        //Draw graph background
        drawGraphBackground(leftGraphBorder, topGraphBorder, width - rightGraphBorder,
                height - bottomGraphBorder);

        //Draw graph lines with labels
        drawGraphGrid();

        //Draw graph title
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(70);
        paint.setColor(Color.LTGRAY);
        canvas.drawText(title, (graphWidth / 2) + leftGraphBorder, topGraphBorder - border + 30, paint);

        if (this.cancel == false)
            drawLineGraph(canvas);
    }

    private float getMax() {
        float largest = Integer.MIN_VALUE;

        if (values == null)
            largest = 0;
        else {
            for (int i = 0; i < values.size(); i++)
                if (values.get(i) > largest)
                    largest = values.get(i);
        }
        return largest;
    }

    private float getMin() {
        float smallest = Integer.MAX_VALUE;

        if (values == null)
            smallest = 0;
        else {
            for (int i = 0; i < values.size(); i++)
                if (values.get(i) < smallest)
                    smallest = values.get(i);
        }

        return smallest;
    }

    //Draws the gray background
    private void drawGraphBackground(float startX, float startY, float endX, float endY){
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(startX, startY, endX, endY, paint);
    }

    //Draws the graph grid/labels
    private void drawGraphGrid(){
        float width = getWidth();
        float height = getHeight();

        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(2);
        paint.setTextSize(50);

        int vers = verlabels.length - 1;
        for (int i = 0; i < verlabels.length; i++) {
            //Draw horizontal lines
            float y = ((graphHeight / vers) * i) + topGraphBorder;
            canvas.drawLine(leftGraphBorder, y, width - rightGraphBorder, y, paint);

            //Draw y labels
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(verlabels[vers - i], 0, y, paint);
        }

        int hors = horlabels.length - 1;
        for (int i = 0; i < horlabels.length; i++) {
            //Draw vertical lines
            float x = ((graphWidth / hors) * i) + leftGraphBorder;
            canvas.drawLine(x, topGraphBorder, x, height - bottomGraphBorder, paint);

            //Draw x labels
            paint.setTextAlign(Paint.Align.CENTER);
            if (i==horlabels.length-1)
                paint.setTextAlign(Paint.Align.RIGHT);
            if (i==0)
                paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(horlabels[i], x, height - 4, paint);
        }
    }

    //Creates the text boxes and buttons
    public void addControls(Context context, RelativeLayout layout){
        final Button runButton, stopButton;
        EditText idEditText, ageEditText, nameEditText;

        final float button_y = 30;
        final float button_width = (float)0.9;
        final float edit_text_y = 60;


        runButton = new Button(context);
        runButton.setTag("btnRun");
        runButton.setText("Run");
        runButton.setBackgroundColor(Color.GREEN);
        runButton.setTextColor(Color.BLACK);
        runButton.setTextSize(20);
        runButton.setX(1230);
        runButton.setY(button_y);
        runButton.setScaleX(button_width);
        runButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        runButton.setOnClickListener(this);

        stopButton = new Button(context);
        stopButton.setTag("btnStop");
        stopButton.setText("Stop");
        stopButton.setBackgroundColor(Color.RED);
        stopButton.setTextColor(Color.BLACK);
        stopButton.setTextSize(20);
        stopButton.setX(1500);
        stopButton.setY(button_y);
        stopButton.setScaleX(button_width);
        stopButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        stopButton.setOnClickListener(this);

        //Create EditText boxes
        idEditText = new EditText(context);
        idEditText.setWidth(285);
        idEditText.setX(30);
        idEditText.setY(edit_text_y);

        ageEditText = new EditText(context);
        ageEditText.setWidth(160);
        ageEditText.setX(350);
        ageEditText.setY(edit_text_y);

        nameEditText = new EditText(context);
        nameEditText.setWidth(300);
        nameEditText.setX(540);
        nameEditText.setY(edit_text_y);

        //Create radio buttons
        RadioButton maleRadioButton = new RadioButton(context);
        maleRadioButton.setTag("radbtnMale");
        maleRadioButton.setText("Male");

        RadioButton femaleRadioButton = new RadioButton(context);
        femaleRadioButton.setTag("radbtnFemale");
        femaleRadioButton.setText("Female");
        femaleRadioButton.setY(-20);

        //Create radio group and add radio buttons
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setX(900);
        radioGroup.setY(60);
        radioGroup.addView(maleRadioButton);
        radioGroup.addView(femaleRadioButton);

        //Add controls to the layout
        layout.addView(idEditText);
        layout.addView(ageEditText);
        layout.addView(nameEditText);
        layout.addView(runButton);
        layout.addView(stopButton);
        layout.addView(radioGroup);

    }

    //Starts the running of the graph
    private void runGraph(){
        final long speed = 500L;

        this.cancel = false;

        //Start scrolling of the line graph
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (cancel == false){
                    //Change data in the line graph
                    values.remove(0);
                    values.add((float)rand.nextInt(2000));
                    postInvalidate();

                    try{
                        Thread.sleep(speed);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //Stops the running of the graph
    private void stopGraph(){
        this.cancel = true;
    }

    //Draws the line graph
    private void drawLineGraph(Canvas canvas){
        float startX, startY, stopX, stopY;
        int leftX = (int)(leftGraphBorder);
        int rightX = (int)(getWidth() - rightGraphBorder);
        int topY = (int)(topGraphBorder);
        int bottomY = (int)(getHeight() - bottomGraphBorder);

        //REdraw the graph
        drawGraphBackground(leftGraphBorder, topGraphBorder, width - rightGraphBorder, height - bottomGraphBorder);
        drawGraphGrid();

        int pointWidth = (rightX - leftX) / (values.size() - 1);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);

        //Draw the line graph
        for (int i = 0; i < (values.size() - 1); i++){
            startX = (i * pointWidth) + leftX;
            startY = (bottomY - ((values.get(i) / 2000) * (graphHeight)));

            if ((i + 1) == (values.size() - 1))
                stopX = rightX;
            else
                stopX = ((i + 1) * pointWidth) + leftX;

            stopY = (bottomY - ((values.get(i + 1) / 2000) * (graphHeight)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    //Handles control events
    @Override
    public void onClick(View v){
        if (v.getTag() == "btnRun"){
            if (cancel == true){
                runGraph();
                Toast.makeText(getContext(), "Running...", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Graph already running", Toast.LENGTH_SHORT).show();
            }

        } else if((v.getTag() == "btnStop")){
            if (cancel == false) {
                stopGraph();
                Toast.makeText(getContext(), "Stopping...", Toast.LENGTH_SHORT).show();
            }
        }
    }

}