package com.curonsys.army.DataClass;

import java.util.Date;
/**
 * Created by Leejuhwan on 2018-06-01.
 */

public class PollData {
    private String CODE;
    private String A_POLL;
    private String B_POLL;
    private String C_POLL;


    public PollData(String code, String a_poll, String b_poll, String c_poll){
        this.CODE = code;
        this.A_POLL = a_poll;
        this.B_POLL = b_poll;
        this.C_POLL = c_poll;
    }

    public void setCODE(String CODE) { this.CODE = CODE;}
    public void setA_POLL(String A_POLL) { this.CODE = A_POLL;}
    public void setB_POLL(String B_POLL) { this.CODE = B_POLL;}
    public void setC_POLL(String C_POLL) { this.CODE = C_POLL;}


    public String getCODE(String CODE) { return CODE;}
    public String getA_POLL(String A_POLL) { return A_POLL;}
    public String getB_POLL(String B_POLL) { return B_POLL;}
    public String getC_POLL(String C_POLL) { return C_POLL;}


}
