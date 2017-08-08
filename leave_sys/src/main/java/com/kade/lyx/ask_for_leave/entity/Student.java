package com.kade.lyx.ask_for_leave.entity;

import java.util.List;

/**
 * Created by lyx on 2016/9/30 0030.
 */


public class Student {


    private String data;
    private String cnumber;
    private String name;
    private String sex;
    private String idCard;
    private String dname;
    private String pic;
    private String card_info;
    private String card_state;
    private String card_recharge;
    private String card_subsidy;
    private String card_times;
    private String card_start;
    private String card_end;
    private List<Subsidy> subsidyList;
    private String total;
    private String summary;
    private List<Details> detailsList;
    private String result;
    private List<GetReceivable> receivableList;
    private List<GetFAQ> faqList;
    private List<GetNoticeList> noticeListList;
    private List<P_N_Details> p_n_detailsList;
    private List<Identity> identityList;
    private List<EWallet> eWalletList;
    private List<LeaveDetails> leaveDetailsList;
    private List<ThdPayDetails> thdPayDetailsList;

    public List<ADAddress> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<ADAddress> addressList) {
        this.addressList = addressList;
    }

    private List<ADAddress> addressList;




    public class Subsidy {


        private String subsidy_type;
        private String subsidy_money;
        private String subsidy_state;
        private String subsidy_start;
        private String subsidy_state1;
        private String subsidy_balance;

        public String getSubsidy_balance() {
            return subsidy_balance;
        }

        public void setSubsidy_balance(String subsidy_balance) {
            this.subsidy_balance = subsidy_balance;
        }


        public String getSubsidy_time() {
            return subsidy_time;
        }

        public void setSubsidy_time(String subsidy_time) {
            this.subsidy_time = subsidy_time;
        }

        private String subsidy_time;

        public String getSubsidy_row() {
            return subsidy_row;
        }

        public void setSubsidy_row(String subsidy_row) {
            this.subsidy_row = subsidy_row;
        }

        public String getSubsidy_money() {
            return subsidy_money;
        }

        public void setSubsidy_money(String subsidy_money) {
            this.subsidy_money = subsidy_money;
        }

        public String getSubsidy_type() {
            return subsidy_type;
        }

        public void setSubsidy_type(String subsidy_type) {
            this.subsidy_type = subsidy_type;
        }

        public String getSubsidy_state() {
            return subsidy_state;
        }

        public void setSubsidy_state(String subsidy_state) {
            this.subsidy_state = subsidy_state;
        }

        public String getSubsidy_start() {
            return subsidy_start;
        }

        public void setSubsidy_start(String subsidy_start) {
            this.subsidy_start = subsidy_start;
        }

        public String getSubsidy_state1() {
            return subsidy_state1;
        }

        public void setSubsidy_state1(String subsidy_state1) {
            this.subsidy_state1 = subsidy_state1;
        }

        private String subsidy_row;


    }

    public class Details {

        private String ar_nd;
        private String di_name;
        private String ar_opTypeName;
        private String ar_amount;
        private String ar_balance;
        private String ar_occDate;

        public String getAr_nd() {
            return ar_nd;
        }

        public void setAr_nd(String ar_nd) {
            this.ar_nd = ar_nd;
        }

        public String getDi_name() {
            return di_name;
        }

        public void setDi_name(String di_name) {
            this.di_name = di_name;
        }

        public String getAr_opTypeName() {
            return ar_opTypeName;
        }

        public void setAr_opTypeName(String ar_opTypeName) {
            this.ar_opTypeName = ar_opTypeName;
        }

        public String getAr_amount() {
            return ar_amount;
        }

        public void setAr_amount(String ar_amount) {
            this.ar_amount = ar_amount;
        }

        public String getAr_balance() {
            return ar_balance;
        }

        public void setAr_balance(String ar_balance) {
            this.ar_balance = ar_balance;
        }

        public String getAr_occDate() {
            return ar_occDate;
        }

        public void setAr_occDate(String ar_occDate) {
            this.ar_occDate = ar_occDate;
        }
    }

    public class TradeSummary {
        private String intype;
        private String tcount;
        private String operdate;

        public String getIntype() {
            return intype;
        }

        public void setIntype(String intype) {
            this.intype = intype;
        }

        public String getTcount() {
            return tcount;
        }

        public void setTcount(String tcount) {
            this.tcount = tcount;
        }

        public String getOperdate() {
            return operdate;
        }

        public void setOperdate(String operdate) {
            this.operdate = operdate;
        }
    }

    public class GetReceivable {

        private String r_row;
        private String r_type;
        private String r_date;
        private String r_money;

        public String getR_row() {
            return r_row;
        }

        public void setR_row(String r_row) {
            this.r_row = r_row;
        }

        public String getR_type() {
            return r_type;
        }

        public void setR_type(String r_type) {
            this.r_type = r_type;
        }

        public String getR_date() {
            return r_date;
        }

        public void setR_date(String r_date) {
            this.r_date = r_date;
        }

        public String getR_money() {
            return r_money;
        }

        public void setR_money(String r_money) {
            this.r_money = r_money;
        }
    }

    public class GetFAQ {

        private String f_ID;
        private String f_Title;

        public String getF_AddTime() {
            return f_AddTime;
        }

        public void setF_AddTime(String f_AddTime) {
            this.f_AddTime = f_AddTime;
        }

        private String f_AddTime;

        public String getF_ID() {
            return f_ID;
        }

        public void setF_ID(String f_ID) {
            this.f_ID = f_ID;
        }

        public String getF_Title() {
            return f_Title;
        }

        public void setF_Title(String f_Title) {
            this.f_Title = f_Title;
        }
    }

    public class GetNoticeList {
        private String n_ID;
        private String n_Title;

        public String getN_AddTime() {
            return n_AddTime;
        }

        public void setN_AddTime(String n_AddTime) {
            this.n_AddTime = n_AddTime;
        }

        private String n_AddTime;

        public String getN_Title() {
            return n_Title;
        }

        public void setN_Title(String n_Title) {
            this.n_Title = n_Title;
        }

        public String getN_ID() {
            return n_ID;
        }

        public void setN_ID(String n_ID) {
            this.n_ID = n_ID;
        }


    }

    public class P_N_Details {
        private String date;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        private String content;


    }

    public class Identity{
        private String dname;
        private String jsj;
        private String tname;

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getJsj() {
            return jsj;
        }

        public void setJsj(String jsj) {
            this.jsj = jsj;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }




    }

    public class EWallet{
        private String qclls;
        private String state;
        private String amount;

        public String getQgetdate() {
            return qgetdate;
        }

        public void setQgetdate(String qgetdate) {
            this.qgetdate = qgetdate;
        }

        private String qgetdate;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        private String balance;

        public String getQdate() {
            return qdate;
        }

        public void setQdate(String qdate) {
            this.qdate = qdate;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getQclls() {
            return qclls;
        }

        public void setQclls(String qclls) {
            this.qclls = qclls;
        }

        private String qdate;



    }

    public class LeaveDetails{
        private String id;
        private String cname;
        private String tname;
        private String starttime;
        private String endtime;
        private String state;
        private String reason;
        private String addtime;

        public String getLid() {
            return lid;
        }

        public void setLid(String lid) {
            this.lid = lid;
        }

        private String lid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }

    public class ThdPayDetails{
        private String pnum;
        private String pname;
        private String pamount;
        private String pdate;
        private String pbalance;
        private String psname;
        private String pgetdate;

        public String getPgetdate() {
            return pgetdate;
        }

        public void setPgetdate(String pgetdate) {
            this.pgetdate = pgetdate;
        }


        public String getPbalance() {
            return pbalance;
        }

        public void setPbalance(String pbalance) {
            this.pbalance = pbalance;
        }

        public String getPsname() {
            return psname;
        }

        public void setPsname(String psname) {
            this.psname = psname;
        }



        public String getPnum() {
            return pnum;
        }

        public void setPnum(String pnum) {
            this.pnum = pnum;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getPamount() {
            return pamount;
        }

        public void setPamount(String pamount) {
            this.pamount = pamount;
        }

        public String getPdate() {
            return pdate;
        }

        public void setPdate(String pdate) {
            this.pdate = pdate;
        }
    }

    public class ADAddress{
        private String name;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }


    public List<ThdPayDetails> getThdPayDetailsList() {
        return thdPayDetailsList;
    }

    public void setThdPayDetailsList(List<ThdPayDetails> thdPayDetailsList) {
        this.thdPayDetailsList = thdPayDetailsList;
    }
    public List<LeaveDetails> getLeaveDetailsList() {
        return leaveDetailsList;
    }

    public void setLeaveDetailsList(List<LeaveDetails> leaveDetailsList) {
        this.leaveDetailsList = leaveDetailsList;
    }

    public List<EWallet> geteWalletList() {
        return eWalletList;
    }

    public void seteWalletList(List<EWallet> eWalletList) {
        this.eWalletList = eWalletList;
    }


    public List<Identity> getIdentityList() {
        return identityList;
    }

    public void setIdentityList(List<Identity> identityList) {
        this.identityList = identityList;
    }

    public List<Subsidy> getSubsidyList() {
        return subsidyList;
    }

    public void setSubsidyList(List<Subsidy> subsidyList) {
        this.subsidyList = subsidyList;
    }

    public List<Details> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<Details> detailsList) {
        this.detailsList = detailsList;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<GetReceivable> getReceivableList() {
        return receivableList;
    }

    public void setReceivableList(List<GetReceivable> receivableList) {
        this.receivableList = receivableList;
    }

    public List<GetNoticeList> getNoticeListList() {
        return noticeListList;
    }

    public void setNoticeListList(List<GetNoticeList> noticeListList) {
        this.noticeListList = noticeListList;
    }




    public List<P_N_Details> getP_n_detailsList() {
        return p_n_detailsList;
    }

    public void setP_n_detailsList(List<P_N_Details> p_n_detailsList) {
        this.p_n_detailsList = p_n_detailsList;
    }



    public List<GetFAQ> getFaqList() {
        return faqList;

    }

    public void setFaqList(List<GetFAQ> faqList) {
        this.faqList = faqList;
    }

    public List<TradeSummary> getTradeSummaryList() {
        return tradeSummaryList;
    }

    public void setTradeSummaryList(List<TradeSummary> tradeSummaryList) {
        this.tradeSummaryList = tradeSummaryList;
    }

    private List<TradeSummary> tradeSummaryList;


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    public String getCard_info() {
        return card_info;
    }

    public void setCard_info(String card_info) {
        this.card_info = card_info;
    }

    public String getCard_state() {
        return card_state;
    }

    public void setCard_state(String card_state) {
        this.card_state = card_state;
    }

    public String getCard_recharge() {
        return card_recharge;
    }

    public void setCard_recharge(String card_recharge) {
        this.card_recharge = card_recharge;
    }

    public String getCard_subsidy() {
        return card_subsidy;
    }

    public void setCard_subsidy(String card_subsidy) {
        this.card_subsidy = card_subsidy;
    }

    public String getCard_times() {
        return card_times;
    }

    public void setCard_times(String card_times) {
        this.card_times = card_times;
    }

    public String getCard_start() {
        return card_start;
    }

    public void setCard_start(String card_start) {
        this.card_start = card_start;
    }

    public String getCard_end() {
        return card_end;
    }

    public void setCard_end(String card_end) {
        this.card_end = card_end;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCnumber() {
        return cnumber;
    }

    public void setCnumber(String cnumber) {
        this.cnumber = cnumber;
    }
}
