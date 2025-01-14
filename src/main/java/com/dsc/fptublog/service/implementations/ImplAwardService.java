package com.dsc.fptublog.service.implementations;

import com.dsc.fptublog.dao.interfaces.IAwardDAO;
import com.dsc.fptublog.dao.interfaces.ILecturerStudentAwardDAO;
import com.dsc.fptublog.database.ConnectionWrapper;
import com.dsc.fptublog.entity.AwardEntity;
import com.dsc.fptublog.entity.LecturerStudentAwardEntity;
import com.dsc.fptublog.model.AwardModel;
import com.dsc.fptublog.service.interfaces.IAwardService;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequestScoped
public class ImplAwardService implements IAwardService {

    @Inject
    private ConnectionWrapper connectionWrapper;

    @Inject
    private IAwardDAO awardDAO;

    @Inject
    private ILecturerStudentAwardDAO lecturerStudentAwardDAO;

    @Override
    public List<AwardEntity> getAwards() throws SQLException {
        List<AwardEntity> result = null;

        try {
            connectionWrapper.beginTransaction();

            result = awardDAO.getAllAwards();
            if (result == null) {
                result = Collections.emptyList();
            }

            connectionWrapper.commit();
        } finally {
            connectionWrapper.close();
        }

        return result;
    }

    @Override
    public AwardEntity getAward(String id) throws SQLException {
        AwardEntity result = null;

        try {
            connectionWrapper.beginTransaction();

            result = awardDAO.getById(id);

            connectionWrapper.commit();
        } finally {
            connectionWrapper.close();
        }

        return result;
    }

    @Override
    public boolean giveAward(LecturerStudentAwardEntity lecturerStudentAward) throws SQLException {
        boolean result = false;

        try {
            connectionWrapper.beginTransaction();

            result = lecturerStudentAwardDAO.insertByLecturerStudentAward(lecturerStudentAward);

            connectionWrapper.commit();
        } catch (SQLException ex) {
            connectionWrapper.rollback();
            throw ex;
        } finally {
            connectionWrapper.close();
        }

        return result;
    }

    @Override
    public List<AwardModel> getAllAwardOfStudent(String studentId) throws SQLException {
        List<AwardModel> result = null;

        try {
            connectionWrapper.beginTransaction();

            List<LecturerStudentAwardEntity> LecturerStudentAwardList =
                    lecturerStudentAwardDAO.getByStudentId(studentId);
            if (LecturerStudentAwardList == null) {
                result = Collections.emptyList();
            } else {
                List<String> awardIdList = LecturerStudentAwardList.stream()
                        .map(LecturerStudentAwardEntity::getAwardId)
                        .collect(Collectors.toList());
                List<AwardEntity> awardList = awardDAO.getByAwardIdList(awardIdList);
                if (awardList != null) {
                    // Count
                    Map<AwardEntity, Integer> tmpMap = new HashMap<>();
                    awardList.forEach(award -> tmpMap.put(award, tmpMap.getOrDefault(award, 0) + 1));

                    // Convert map to list
                    result = new ArrayList<>();
                    for (Map.Entry<AwardEntity, Integer> entry : tmpMap.entrySet()) {
                        AwardEntity award = entry.getKey();
                        int count = entry.getValue();
                        result.add(new AwardModel(award.getId(), award.getName(), award.getIconUrl(),
                                award.getPoint(), count));
                    }
                } else {
                    result = Collections.emptyList();
                }
            }
            connectionWrapper.commit();
        } finally {
            connectionWrapper.close();
        }

        return result;
    }


    @Override
    public LecturerStudentAwardEntity deleteAwardOfStudent(String id, String lecturerId, String studentId)
            throws SQLException {
        LecturerStudentAwardEntity result = null;

        try {
            connectionWrapper.beginTransaction();

            LecturerStudentAwardEntity oldAward = lecturerStudentAwardDAO.getById(id);
            if (oldAward == null) {
                return null;
            }

            if (!oldAward.getLecturerId().equals(lecturerId) || !oldAward.getStudentId().equals(studentId)) {
                return null;
            }

            if (lecturerStudentAwardDAO.deleteById(id)) {
                result = oldAward;
            }

            connectionWrapper.commit();
        } catch (SQLException ex) {
            connectionWrapper.rollback();
            throw ex;
        } finally {
            connectionWrapper.close();
        }

        return result;
    }

}
