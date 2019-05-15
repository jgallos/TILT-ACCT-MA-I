/*
TILT-ACCT - Transparency in Learning and Teaching through Android and Cloud Computing Technologies
Programmer: Joseph M. Gallos
Date: May 2019
Software License: GNU-General Public License
*/
package com.programmer.jgallos.ma_i;

public class SubjectRecords {
    private String subject;

    public SubjectRecords(String subject) {
        this.subject = subject;
    }

    public SubjectRecords() {

    }

    public void setSubject(String subject) {
        this.subject=subject;
    }


    public String getSubject() {
        return subject;
    }
}

