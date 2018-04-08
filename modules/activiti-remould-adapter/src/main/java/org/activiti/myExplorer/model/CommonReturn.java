package org.activiti.myExplorer.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author 李萌
 * @date 2018年1月5日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class CommonReturn implements Serializable {

	private static final long serialVersionUID = 1L;

	public CommonReturn(Object dataRows) {
		this.retCode = RetCode.SUCCESS;
		this.dataRows = dataRows;
	}

	public CommonReturn(Object dataRows, RetCode retCode) {
		this.retCode = retCode;
		this.dataRows = dataRows;
	}

	public CommonReturn(RetCode retCode, String retVal) {
		this.retCode = retCode;
		this.retVal = retVal;
	}

	public CommonReturn(Object dataRows, RetCode retCode, String retVal) {
		this.retCode = retCode;
		this.retVal = retVal;
		this.dataRows = dataRows;
	}

	@JSONField(name = "RetCode")
	private RetCode retCode;

	@JSONField(name = "RetVal")
	private String retVal;

	@JSONField(name = "DataRows")
	private Object dataRows;

	public RetCode getRetCode() {
		return retCode;
	}

	public void setRetCode(RetCode retCode) {
		this.retCode = retCode;
	}

	public String getRetVal() {
		return retVal;
	}

	public void setRetVal(String retVal) {
		this.retVal = retVal;
	}

	public Object getDataRows() {
		return dataRows;
	}

	public void setDataRows(Object dataRows) {
		this.dataRows = dataRows;
	}

}
