package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.GrpMsgReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrpMsgReadStatusRepo extends JpaRepository<GrpMsgReadStatus, Long> {
    List<GrpMsgReadStatus> findByGrpIdAndGrpUsrId(Long grpId, Long grpUsrId);
    List<GrpMsgReadStatus> findByGrpMsgIdAndGrpUsrId(Long grpMsgId, Long grpUsrId);
}
