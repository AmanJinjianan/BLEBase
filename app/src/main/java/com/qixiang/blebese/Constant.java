package com.qixiang.blebese;

/**
 * Created by Administrator on 2020/4/21.
 */

public class Constant {

    //写数据命令字1Byte
    public static byte CLEAR_HOLZER = 0x01;//清空霍尔器件的计数
    public static byte SET_MOTOR_SHORT = 0x02;//设置电机短震（300ms）
    public static byte MOTOR_DOUBLE = 0x03;//设置电机双震
    public static byte SET_MOTOR_TRIPLE = 0x04;//设置电机三震
    public static byte SET_MOTOR_LONG = 0x05;// 设置电机长震（1s）
    public static byte START_1MIN = 0x06;//开启1min 跳绳模式
    public static byte START_COUNT = 0x07;//开启计数跳模式
    public static byte SHUT_DOWN = 0x08;//关机
    public static byte OPEN_Holzer = 0x09;//开启霍尔管（可以计数）
    public static byte CLOSE_Holzer = 0x0A;//关闭霍尔管（不可计数）
    public static byte SET_SKIP_COUNT = 0x0B;//设置计数跳的计数值（后边跟2Byte的数值）
    public static byte GET_TOTLE_COUNT = 0x0C;//获取总跳数
    public static byte GET_BUFF_DATA = 0x0D;//获取flash中的数据
    public static byte GET_BUFF_SIZE = 0x0E;//获取flash中数据的条数
    public static byte CLEAR_PAGE = 0x0f;//
    public static byte GET_COUNT_VALUE = 0x10;//获取计数跳的数值

    /*读数据头（2Byte的高6bit）
    例如:
    {
        data[2] = {0x12.0x34}
        uint16_t Head  = ((data[0]<<8 )+ data[1])>>10;
        Head 取以下数据
    }*/
    public static byte NO_DATA = 63;//fc00  flash读空
    public static byte SKIP_1MIN_HEAD = 1;//0400 flash中读到的1min跳数据
    public static byte SKIP_FREE_HEAD = 2;//0800 flash中读到的自由跳数据
    public static byte SKIP_COUNT_HEAD = 3;//0c00 flash中读到的计数跳数据
    //以上三个读到的

    public static byte SKIP_1MIN_NTY_HEAD = 4;//1000 1min跳实时数据通知
    public static byte SKIP_FREE_NTY_HEAD = 5;//1400 自由跳实时数据通知
    public static byte SKIP_COUNT_NTY_HEAD = 6;//1800 计数跳实时数据通知

    public static byte SKIP_1MIN_NTY_START = 7;//1c00 1min跳模式开启通知
    // public static byte SKIP_FREE_NTY_START	8	   ;//2000  	自由跳模式开启通知(此条无用)
    public static byte SKIP_COUNT_NTY_START = 9;//2400 计数跳模式开启通知
    //以上6个数据由通知获取

    public static byte TOTLE__HEAD = 10;//2800 总跳数数据获取
    public static byte BUFF_SIZE__HEAD = 11;//2C00 flash数据条数的个数获取
    public static byte COUNT_SKIP__HEAD = 12;//3000 计数跳的计数值获取
    //以上三条读取获得

    public static byte SKIP_1MIN_END_NTY_HEAD = 13;//3400	1min跳结束通知
    public static byte SKIP_COUNT_END_NTY_HEAD = 14;//3800	计数跳结束通知
    //以上三条通知获取

}
