package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Grph extends ApplicationFrame {

    public Grph(String applicationTitle) throws IOException {
        super(applicationTitle);
        this.add(new ChartPanel(createChart(createDataset())));
    }

    public XYSeriesCollection createDataset() {
        final XYSeries series = new XYSeries("X");
        series.add(1, 2);
        series.add(2,4);
        series.add(3, 9);
        series.add(4, 16);
        series.add(5, 25);
        
        return new XYSeriesCollection(series);
    }

    public JFreeChart createChart(XYDataset dataset)
        throws NumberFormatException, IOException {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Acceleration vs Time", "Time", "Acceleration", dataset,
            true, true, false);
        return chart;
    }

    public static void main(String[] args) throws IOException {
        final Grph demo = new Grph("Test Time Series Chart");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}