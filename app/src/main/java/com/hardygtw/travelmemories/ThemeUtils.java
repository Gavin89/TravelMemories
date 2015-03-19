package com.hardygtw.travelmemories;

/**
 * Created by gavin on 19/03/2015.
 */
import android.app.Activity;

import android.content.Intent;


public class ThemeUtils

{

    private static int cTheme;



    public final static int BLUE = 0;

    public final static int RED = 1;

    public final static int ORANGE = 2;

    public final static int PURPLE = 3;

    public static void changeToTheme(Activity activity, int theme)

    {

        cTheme = theme;

        activity.finish();



        activity.startActivity(new Intent(activity, activity.getClass()));


    }

    public static void onActivityCreateSetTheme(Activity activity)

    {

        switch (cTheme)

        {

            default:

            case BLUE:

                activity.setTheme(R.style.AppTheme);

                break;

            case RED:

                activity.setTheme(R.style.AppThemeRed);

                break;

            case ORANGE:

                activity.setTheme(R.style.AppThemeOrange);

                break;

            case PURPLE:

                activity.setTheme(R.style.AppThemePurple);

                break;
        }

    }

}