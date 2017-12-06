package org.activiti.myExplorer.model;

import java.io.Serializable;
import java.util.Collection;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class ProcessInstReturn implements Serializable {

	private static final long serialVersionUID = 1L;

	@JSONField(name = "RetCode")
	private RetCode retCode;

	@JSONField(name = "RetVal")
	private String retVal;

	private EndCode isEnd;

	@JSONField(name = "DataRows")
	private Collection<ExecutionReturn> executionReturn;

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

	public Collection<ExecutionReturn> getExecutionReturn() {
		return executionReturn;
	}

	public void setExecutionReturn(Collection<ExecutionReturn> executionReturn) {
		this.executionReturn = executionReturn;
	}

	public EndCode getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(EndCode isEnd) {
		this.isEnd = isEnd;
	}

}
