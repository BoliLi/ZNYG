package com.renhe.znyg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Medicine {
    private String category;
    private String name;
    private String description;
    private List<MedCntWithExpDate> medCntList = new ArrayList<>();
    private int expCnt;
    private int totalCnt;

    public Medicine() {

    }

    public Medicine(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    private class MedCntWithExpDate {
        private int cnt;
        private Date expDate;

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        public Date getExpDate() {
            return expDate;
        }

        public void setExpDate(Date expDate) {
            this.expDate = expDate;
        }
    }

    private class ForCompare implements Comparator<MedCntWithExpDate> {

        @Override
        public int compare(MedCntWithExpDate medCntWithExpDate, MedCntWithExpDate t1) {
            if(medCntWithExpDate.expDate.before(t1.expDate)) {
                return -1;
            } else if(medCntWithExpDate.expDate.after(t1.expDate)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MedCntWithExpDate> getMedCntList() {
        return medCntList;
    }

    public void setMedCntList(List<MedCntWithExpDate> medCntList) {
        this.medCntList = medCntList;
    }

    public int getExpCnt() {
        return expCnt;
    }

    public void setExpCnt(int expCnt) {
        this.expCnt = expCnt;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int checkExp() {
        Date now = new Date();
        int ret = 0;
        for(MedCntWithExpDate mcwed : medCntList) {
            if(mcwed.expDate.before(now)) {
                ret += mcwed.cnt;
            } else {
                break;
            }
        }
        expCnt = ret;
        return ret;
    }

    public void add(int count, Date expireDate) {
        MedCntWithExpDate mcwed = new MedCntWithExpDate();
        mcwed.cnt = count;
        mcwed.expDate = expireDate;
        medCntList.add(mcwed);
        Collections.sort(medCntList, new ForCompare());
        totalCnt += count;
    }

    public void sub(int count) {
        int remainCnt = count;
        Iterator<MedCntWithExpDate> it = medCntList.iterator();
        while(it.hasNext() && remainCnt > 0){
            MedCntWithExpDate x = it.next();
            if(x.cnt < remainCnt){
                remainCnt -= x.cnt;
                it.remove();
            } else if(x.cnt > remainCnt) {
                x.cnt -= remainCnt;
                break;
            } else {
                it.remove();
                break;
            }
        }
        totalCnt -= count;
    }

    public void handleExp() {
        sub(expCnt);
        expCnt = 0;
    }
}
