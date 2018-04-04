package org.activiti.account.service;

import java.util.Collection;

import org.activiti.account.mapper.ResetPasswordLogMapper;
import org.activiti.account.persist.Account;
import org.activiti.account.persist.ResetPasswordLog;
import org.activiti.myExplorer.pojoHelper.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordLogService extends ServiceSupport<ResetPasswordLog> implements ResetPasswordLogMapper {

	@Autowired
	private ResetPasswordLogMapper mapper;

	@Override
	public ResetPasswordLog select(Object id) {
		return mapper.select(id);
	}

	@Override
	public Collection<ResetPasswordLog> selectAll(ResetPasswordLog t) {
		return mapper.selectAll(t);
	}

	@Override
	public ResetPasswordLog selectOne(ResetPasswordLog t) {
		return mapper.selectOne(t);
	}

	@Override
	public void insert(ResetPasswordLog t) {
		mapper.insert(t);
	}

	@Override
	public int update(ResetPasswordLog t) {
		return mapper.update(t);
	}

	@Override
	public int updatePersistent(ResetPasswordLog t) {
		return mapper.updatePersistent(t);
	}

	@Override
	public int delete(ResetPasswordLog t) {
		return mapper.delete(t);
	}

	@Override
	public int count(ResetPasswordLog t) {
		return mapper.count(t);
	}

	public void loadAccount(Account account, ResetPasswordLog resetPasswordLog) {
		account.removeAllResetPasswordLog();
		resetPasswordLog.setAccount(account);
		account.setResetPasswordLog(mapper.selectAll(resetPasswordLog));
	}
}
