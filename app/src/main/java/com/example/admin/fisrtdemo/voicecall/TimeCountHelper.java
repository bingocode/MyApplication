package com.example.admin.fisrtdemo.voicecall;

/**
 * 创建时间: 2018/08/13 22:52 <br>
 * 作者: zengbin <br>
 * 描述:计时器
 */
public class TimeCountHelper implements Runnable{
  public int time = 0;
  public ITimeUpdate mTimeUpdate;
  private boolean isStop = false;
  private volatile static TimeCountHelper instance;

  private TimeCountHelper(){}

  public  static TimeCountHelper getInstance(){
    if(instance ==null) {
      synchronized (TimeCountHelper.class) {
        if (instance == null)
          instance = new TimeCountHelper();
      }
    }
    return instance;
  }

  public static String getFormatHMS(long time){
    time=time/1000;//总秒数
    int s= (int) (time%60);//秒
    int m= (int) (time/60);//分
    int h = (int) (time/3600); //时
    if (h == 0) {
      return String.format("%02d:%02d",m,s);
    } else {
      return String.format("%02d:%02d:%02d",h,m,s);
    }
  }

  public void setTimeUpdate(ITimeUpdate timeUpdate) {
    this.mTimeUpdate = timeUpdate;
  }

  //退出
  public void exitTime() {
    time = 0;
    mTimeUpdate = null;
    isStop = false;
  }

  public boolean isCounting() {
    return time !=0;
  }

  @Override public void run() {
    if(mTimeUpdate != null) {
      if(!isStop) {
        time += 1000;
        mTimeUpdate.updatetime(getFormatHMS(time));
      }
    }
  }

  public interface ITimeUpdate {
    void updatetime(String timeStr);
  }

}
