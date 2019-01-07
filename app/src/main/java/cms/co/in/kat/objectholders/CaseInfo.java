package cms.co.in.kat.objectholders;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Happy on 16-03-2017.
 */

public class CaseInfo {

    private String caseType;
    private String dateOfEfiling;
    private String caseDivision;
    private String caseLevel;
    private ArrayList<String> status;
    private ArrayList<String> courtHall;
    private String caseId;
    private String branch;
    private String caseNo;
    private String lcoDate;
    private String lco;
    private ArrayList<String> respondaneInfo;
    private ArrayList<String> notice;
    private ArrayList<String> hearingDate;
    private ArrayList<String> proceeding;
    private String judgmentDate;
    private ArrayList<String> appelentInfo;
    private String section;
    private String judgmentURL;
    private String respondaneInfoName;
    private String respondaneInfoAddress;
    private String appelentInfoName;
    private String appelentInfoAddress;
    private String lcoNo;
    private String judgementURL;
    private String provisionlaw;
    private String authPassOrder;
    private String provisionLco;
    private String otherAuthPassOrder;
    private String hearingDateLast;
    private String caseFileWith;
    private ArrayList<HashMap> proceedingList;
    private ArrayList<String> hearingDateArry;
    private ArrayList<HashMap> procedingArry;
    private ArrayList<String> procedingArryTree;
    private HashMap<String, String> prodedingTreeHashmap;
    private ArrayList<HashMap> judgmentArry;

    public void setDateOfEfiling(String dateOfEfiling) {
        this.dateOfEfiling = dateOfEfiling;
    }

    public String getDateOfEfiling() {
        return dateOfEfiling;
    }

    public void setCaseLevel(String caseLevel) {
        this.caseLevel = caseLevel;
    }

    public String getCaseLevel() {
        return caseLevel;
    }

    public void setStatus(ArrayList<String> status) {
        this.status = status;
    }

    public ArrayList<String> getStatus() {
        return status;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getCaseNo() {
        return caseNo;
    }


    public void setJudgmentURL(String judgmentURL) {
        this.judgmentURL = judgmentURL;
    }

    public String getJudgmentURL() {
        return judgmentURL;
    }

    public void setRespondaneInfoName(String respondaneInfoName) {
        this.respondaneInfoName = respondaneInfoName;
    }

    public String getRespondaneInfoName() {
        return respondaneInfoName;
    }

    public void setRespondaneInfoAddress(String respondaneInfoAddress) {
        this.respondaneInfoAddress = respondaneInfoAddress;
    }

    public String getRespondaneInfoAddress() {
        return respondaneInfoAddress;
    }

    public void setAppelentInfoName(String appelentInfoName) {
        this.appelentInfoName = appelentInfoName;
    }

    public String getAppelentInfoName() {
        return appelentInfoName;
    }

    public void setAppelentInfoAddress(String appelentInfoAddress) {
        this.appelentInfoAddress = appelentInfoAddress;
    }

    public String getAppelentInfoAddress() {
        return appelentInfoAddress;
    }

    public void setLcoNo(String lcoNo) {
        this.lcoNo = lcoNo;
    }

    public String getLcoNo() {
        return lcoNo;
    }

    public void setProvisionlaw(String provisionlaw) {
        this.provisionlaw = provisionlaw;
    }

    public String getProvisionlaw() {
        return provisionlaw;
    }

    public void setAuthPassOrder(String authPassOrder) {
        this.authPassOrder = authPassOrder;
    }

    public String getAuthPassOrder() {
        return authPassOrder;
    }

    public void setProvisionLco(String provisionLco) {
        this.provisionLco = provisionLco;
    }

    public void setOtherAuthPassOrder(String otherAuthPassOrder) {
        this.otherAuthPassOrder = otherAuthPassOrder;
    }

    public String getOtherAuthPassOrder() {
        return otherAuthPassOrder;
    }


    public void setCaseFileWith(String caseFileWith) {
        this.caseFileWith = caseFileWith;
    }

    public String getCaseFileWith() {
        return caseFileWith;
    }


    public void setProcedingArry(ArrayList<HashMap> procedingArry) {
        this.procedingArry = procedingArry;
    }

    public ArrayList<HashMap> getProcedingArry() {
        return procedingArry;
    }

    public void setProcedingArryTree(ArrayList<String> procedingArryTree) {
        this.procedingArryTree = procedingArryTree;
    }

    public ArrayList<String> getProcedingArryTree() {
        return procedingArryTree;
    }

    public void setProdedingTreeHashmap(HashMap<String, String> prodedingTreeHashmap) {
        this.prodedingTreeHashmap = prodedingTreeHashmap;
    }

    public HashMap<String, String> getProdedingTreeHashmap() {
        return prodedingTreeHashmap;
    }

    public void setJudgmentArry(ArrayList<HashMap> judgmentArry) {
        this.judgmentArry = judgmentArry;
    }

    public ArrayList<HashMap> getJudgmentArry() {
        return judgmentArry;
    }
}
