package com.utils;

import android.app.Activity;

import java.util.Stack;

public class ScreenManager {  
    private static Stack<Activity> activityStack;  
    private static ScreenManager instance;  
    private  ScreenManager(){  
    }  
    public static ScreenManager getScreenManager(){  
        if(instance==null){  
            instance=new ScreenManager();  
        }  
        return instance;  
    }  
    public void popActivity(){  
        Activity activity=activityStack.lastElement();  
        if(activity!=null){  
            activity.finish();  
            activity=null;  
        }  
    }  
    public void popActivity(Activity activity){  
        if(activity!=null){  
            activity.finish();  
            activityStack.remove(activity);  
            activity=null;  
        }  
    }  
    public Activity currentActivity(){  
    	if (activityStack.size() == 0)
    		return null;
        Activity activity=activityStack.lastElement();  
        return activity;  
    }  
    public void pushActivity(Activity activity){  
        if(activityStack==null){  
            activityStack=new Stack<Activity>();  
        }  
        activityStack.add(activity);  
    }  
      
    public void popAllActivityExceptOne(Class cls){  
        while(true){  
            Activity activity=currentActivity();  
            if(activity==null){  
                break;  
            }  
            if(activity.getClass().equals(cls) ){  
                break;  
            }  
            popActivity(activity);  
        }  
    }  
    
    public void popAllActivity() {
    	while(true){  
            Activity activity=currentActivity();  
            if(activity==null){  
                break;  
            }  
            popActivity(activity);  
        }  
    }
    
}  
