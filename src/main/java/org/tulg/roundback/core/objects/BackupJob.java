package org.tulg.roundback.core.objects;

import org.tulg.roundback.core.RoundBackObject;
import org.tulg.roundback.master.MasterDB;

public class BackupJob extends RoundBackObject{
    public static int PENDING=0;
    public static int STARTED=1;
    public static int RUNNING=2;
    public static int COMP_SUC=3;
    public static int COMP_ERR=4;

    private String rbdbf_uuid; // unique identifier for the entry
    private int rbdbf_start_time; // when did the job start
    private int rbdbf_end_time; // when did the job end 
    private String rbdbf_start_by; // client or server? Maybe a UUID linking to who initiated it?
    private int rbdbf_status; // current status of the job
    private int rbdbf_size; // full size of the backup


    public BackupJob(){
        this.table = "backup_jobs";
        this.db = new MasterDB(this.table);
    }

}