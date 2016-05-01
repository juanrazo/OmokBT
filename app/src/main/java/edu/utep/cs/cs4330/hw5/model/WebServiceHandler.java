package edu.utep.cs.cs4330.hw5.model;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.utep.cs.cs4330.hw5.view.BoardView;

/**
 * Created by juanrazo on 4/5/16.
 * This class will connect to the server and initiate a game. The class will also send the human
 * coordinates to the server and call a touch event on the boardview to place the coordinates from
 * the server on the method onPostExecute. The OmokServer also handles the Json strings to get
 * response and server coordinates.
 */
public class WebServiceHandler {

    private Coordinates coordinates;
    private boolean isWin = false;
    private boolean isDraw = false;
    private String pid = "";
    private String strategy = "random";
    private String url = "";
    private String server = "";
    private BoardView view;
    public WebServiceHandler(){
        coordinates = new Coordinates();
    }

    public void passCoordinates(int x, int y,View view) {
        this.view = (BoardView) (BoardView) view;
        OmokServer sendCoordinates = new OmokServer();
        url = "http://www.cs.utep.edu/cheon/cs4330/project/omok/play?pid="+pid+"&move="+x+","+y;
        Log.i("URL", url);
        sendCoordinates.execute(url);
    }

    public void setStrategy(String strategy){
        // server = new OmokServer();
        //this.strategy = "http://www.cs.utep.edu/cheon/cs4330/project/omok/new?strategy="+strategy;
        this.strategy= server+strategy;
        Log.i("Set Strategy", strategy);
    }

    public void executeStrategy(){
        Log.i("Execute Strategy", strategy);
        new OmokServer().execute(strategy);
    }

    public void setServer(String server){
        this.server = server;
    }

    public class OmokServer extends AsyncTask<String, Void, String > {

        @Override
        protected String doInBackground(String... params) {
            Log.i("Do Called", "Enter");
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url =new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data!=-1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("Do Called", "END");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("PostEx Called", "Enter");
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.i("JSON object", s);
                if(jsonObject.has("response") && jsonObject.has("pid")){
                    String response = jsonObject.getString("response");
                    pid = jsonObject.getString("pid");
                    Log.i("Response", response);
                    Log.i("PID", pid);
                }

                if(jsonObject.has("ack_move")){
                    String ack = jsonObject.getString("ack_move");
                    JSONObject win = new JSONObject(ack);
                    isWin = Boolean.valueOf(win.getString("isWin"));
                    isDraw = Boolean.valueOf(win.getString("isDraw"));
                    // win.getString("row"));
                }
                if(jsonObject.has("move")){
                    String ack = jsonObject.getString("move");
                    JSONObject win = new JSONObject(ack);
                    isWin = Boolean.valueOf(win.getString("isWin"));
                    isDraw = Boolean.valueOf(win.getString("isDraw"));
                    coordinates.setX(Integer.parseInt(win.getString("x")));
                    coordinates.setY(Integer.parseInt(win.getString("y")));
                    Log.i("Omok Coordinates ", "" + coordinates.getX() + ", " + coordinates.getY());

                    long downTime = SystemClock.uptimeMillis();
                    long eventTime = SystemClock.uptimeMillis() + 100;
                    float x = 0.0f;
                    float y = 0.0f;
                    x = (view.getWidth()/9) * Float.parseFloat(win.getString("x"));
                    y = (view.getHeight()/9) * Float.parseFloat(win.getString("y"));
                    Log.i("to float x: ", Float.toString(x));
                    Log.i("to float y: ", Float.toString(y));

                    int metaState = 0;
                    MotionEvent motionEvent = MotionEvent.obtain(
                            downTime,
                            eventTime,
                            MotionEvent.ACTION_UP,
                            x,
                            y,
                            metaState
                    );

                    view.dispatchTouchEvent(motionEvent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("PostEx Called", "END");
        }
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public boolean isWin(){
        return isWin;
    }

    public boolean isDraw(){
        return isDraw;
    }

    public String getPid(){
        return pid;
    }
}
