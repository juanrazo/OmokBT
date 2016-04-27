package edu.utep.cs.cs4330.hw5.view;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import edu.utep.cs.cs4330.hw5.R;
import edu.utep.cs.cs4330.hw5.control.activity.GameActivity;
import edu.utep.cs.cs4330.hw5.model.Board;

public class BoardView extends View {
    private int playerOneColorID = R.color.black;
    private int playerTwoColorID = R.color.white;
    private int lineColorID = R.color.black;
    private int backgroundColorID = R.color.beige;
    private char[][] board = new char[10][10];

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public BoardView(Context context) {
        super(context);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p>
     * <p>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #View(Context, AttributeSet, int)
     */
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @see #View(Context, AttributeSet)
     */
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute or style resource. This constructor of View allows
     * subclasses to use their own base style when they are inflating.
     * <p>
     * When determining the final value of a particular attribute, there are
     * four inputs that come into play:
     * <ol>
     * <li>Any attribute values in the given AttributeSet.
     * <li>The style resource specified in the AttributeSet (named "style").
     * <li>The default style specified by <var>defStyleAttr</var>.
     * <li>The default style specified by <var>defStyleRes</var>.
     * <li>The base values in this theme.
     * </ol>
     * <p>
     * Each of these inputs is considered in-order, with the first listed taking
     * precedence over the following ones. In other words, if in the
     * AttributeSet you have supplied <code>&lt;Button * textColor="#ff000000"&gt;</code>
     * , then the button's text will <em>always</em> be black, regardless of
     * what is specified in any of the styles.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @param defStyleRes  A resource identifier of a style resource that
     *                     supplies default values for the view, used only if
     *                     defStyleAttr is 0 or can not be found in the theme. Can be 0
     *                     to not look for defaults.
     * @see #View(Context, AttributeSet, int)
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paintLine = new Paint();
        Paint paintPlayerOne = new Paint();
        Paint paintPlayerTwo = new Paint();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setBackgroundColor(getResources().getColor(backgroundColorID, null));
            paintLine.setColor(getResources().getColor(lineColorID, null));
            paintPlayerOne.setColor(getResources().getColor(playerOneColorID, null));
            paintPlayerTwo.setColor(getResources().getColor(playerTwoColorID, null));
        } else {
            setBackgroundColor(getResources().getColor(backgroundColorID));
            paintLine.setColor(getResources().getColor(lineColorID));
            paintPlayerOne.setColor(getResources().getColor(playerOneColorID));
            paintPlayerTwo.setColor(getResources().getColor(playerTwoColorID));
        }
        paintLine.setStrokeWidth(10);

        int r;
        r = getWidth() > getHeight() ? getHeight() / 25 : getWidth() / 25;

        for (int i = 0; i <= getWidth(); i += getWidth() / 9) {
            canvas.drawLine(i, 0, i, getHeight(), paintLine);
        }
        for (int i = 0; i <= getHeight(); i += getHeight() / 9) {
            canvas.drawLine(0, i, getWidth(), i, paintLine);
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                char c = board[i][j];
                if (c == 'b') {
                    canvas.drawCircle(i * getWidth() / 9, j * getHeight() / 9, r, paintPlayerTwo);
                    canvas.drawCircle(i * getWidth() / 9, j * getHeight() / 9, r - 10, paintPlayerOne);
                }
                if (c == 'w') {
                    canvas.drawCircle(i * getWidth() / 9, j * getHeight() / 9, r, paintPlayerOne);
                    canvas.drawCircle(i * getWidth() / 9, j * getHeight() / 9, r - 10, paintPlayerTwo);
                }
                if (c == 'B') {
                    canvas.drawCircle(i * getWidth() / 9, j * getHeight() / 9, r, paintPlayerOne);
                }
                if (c == 'W') {
                    canvas.drawCircle(i * getWidth() / 9, j * getHeight() / 9, r, paintPlayerTwo);
                }
            }
        }
    }

    public int getPlayerOneColorID() {
        return playerOneColorID;
    }

    public void setPlayerOneColorID(int playerOneColorID) {
        this.playerOneColorID = playerOneColorID;
        invalidate();
    }

    public int getPlayerTwoColorID() {
        return playerTwoColorID;
    }

    public void setPlayerTwoColorID(int playerTwoColorID) {
        this.playerTwoColorID = playerTwoColorID;
        invalidate();
    }

    public int getLineColorID() {
        return lineColorID;
    }

    public void setLineColorID(int lineColorID) {
        this.lineColorID = lineColorID;
        invalidate();
    }

    public int getBackgroundColorID() {
        return backgroundColorID;
    }

    public void setBackgroundColorID(int backgroundColorID) {
        this.backgroundColorID = backgroundColorID;
        invalidate();
    }

    public void updateBoard(char board[][]) {
        this.board = board;
        invalidate();
    }
}
