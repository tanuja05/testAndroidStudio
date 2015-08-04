package com.example.tanujagupta.demostudio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GraphAChartEngineActivity extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_graph);
    }
    // Line Graph craete using enginechart.jar
    public void lineGraphHandler (View view)
    {
    	LineGraph line = new LineGraph();
    	Intent lineIntent = line.getIntent(GraphAChartEngineActivity.this);
        startActivity(lineIntent);
    }

//bar chart

    public void barGraphHandler (View view)
    {
    	BarGraph bar = new BarGraph();
    	Intent lineIntent = bar.getIntent(GraphAChartEngineActivity.this);
        startActivity(lineIntent);
    }
    //pi chart

    
    public void pieGraphHandler (View view)
    {
    	PieGraph pie = new PieGraph();
    	Intent lineIntent = pie.getIntent(GraphAChartEngineActivity.this);
        startActivity(lineIntent);
    }
    // scattered graph  testing studio okkkkkk
    // check it on git
    
    public void scatterGraphHandler (View view)
    {
    	ScatterGraph scatter = new ScatterGraph();
    	Intent lineIntent = scatter.getIntent(GraphAChartEngineActivity.this);
        startActivity(lineIntent);
    }
    
}