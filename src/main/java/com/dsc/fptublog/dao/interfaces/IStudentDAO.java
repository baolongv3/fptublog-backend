package com.dsc.fptublog.dao.interfaces;

import com.dsc.fptublog.entity.AccountEntity;
import com.dsc.fptublog.entity.StudentEntity;
import org.jvnet.hk2.annotations.Contract;

import java.sql.SQLException;
import java.util.List;

@Contract
public interface IStudentDAO {

    List<StudentEntity> getAll() throws SQLException;

    StudentEntity createFromAccount(AccountEntity account, String majorId) throws SQLException;

    StudentEntity insertByAccountIdAndMajorIdAndSchoolYear(String accountId, String majorId, short schoolYear) throws SQLException;

    StudentEntity getByAccount(AccountEntity account) throws SQLException;

    boolean updateStudent(StudentEntity studentEntity) throws SQLException;

    boolean deleteStudentById(String id) throws SQLException;

}
