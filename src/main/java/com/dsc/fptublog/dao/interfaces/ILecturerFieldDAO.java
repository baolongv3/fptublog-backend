package com.dsc.fptublog.dao.interfaces;

import com.dsc.fptublog.entity.LecturerFieldEntity;
import org.jvnet.hk2.annotations.Contract;

import java.sql.SQLException;
import java.util.List;

@Contract
public interface ILecturerFieldDAO {

    public List<LecturerFieldEntity> getByLecturerId(String lecturerId) throws SQLException;
}