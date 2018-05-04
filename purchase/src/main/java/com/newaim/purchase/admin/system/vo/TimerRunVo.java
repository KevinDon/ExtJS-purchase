package com.newaim.purchase.admin.system.vo;

import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;

public class TimerRunVo implements Serializable{

	private Integer countMessageNew;
	private Integer countEmailNew;
	private Integer countTaskNew;
	@JsonMoney
	private BigDecimal audToRmb;
	@JsonMoney
	private BigDecimal audToUsd;

    public Integer getCountMessageNew() {
        return countMessageNew;
    }

    public void setCountMessageNew(Integer countMessageNew) {
        this.countMessageNew = countMessageNew;
    }

    public Integer getCountEmailNew() {
        return countEmailNew;
    }

    public void setCountEmailNew(Integer countEmailNew) {
        this.countEmailNew = countEmailNew;
    }

    public Integer getCountTaskNew() {
        return countTaskNew;
    }

    public void setCountTaskNew(Integer countTaskNew) {
        this.countTaskNew = countTaskNew;
    }

    public BigDecimal getAudToRmb() {
        return audToRmb;
    }

    public void setAudToRmb(BigDecimal audToRmb) {
        this.audToRmb = audToRmb;
    }

    public BigDecimal getAudToUsd() {
        return audToUsd;
    }

    public void setAudToUsd(BigDecimal audToUsd) {
        this.audToUsd = audToUsd;
    }
}