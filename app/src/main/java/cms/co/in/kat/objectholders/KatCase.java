package cms.co.in.kat.objectholders;



public class KatCase {


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title, hearingDate, courtNo, status, proceeding, judgementDate;

    public String getHearingDate() {
        return hearingDate;
    }

    public void setHearingDate(String hearingDate) {
        this.hearingDate = hearingDate;
    }

    public String getCourtNo() {
        return courtNo;
    }

    public void setCourtNo(String courtNo) {
        this.courtNo = courtNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProceeding() {
        return proceeding;
    }

    public void setProceeding(String proceeding) {
        this.proceeding = proceeding;
    }

    public String getJudgementDate() {
        return judgementDate;
    }

    public void setJudgementDate(String judgementDate) {
        this.judgementDate = judgementDate;
    }

    public KatCase(String title, String hearingDate, String courtNo, String status, String proceeding, String judgementDate) {

        this.title = title;
        this.hearingDate = hearingDate;
        this.courtNo = courtNo;
        this.status = status;
        this.proceeding = proceeding;
        this.judgementDate = judgementDate;
    }


}
