package com.example.chatapp.db.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "grp_msg_read_status")
public class GrpMsgReadStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "create_at", nullable = false)
    private Timestamp createAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grp_msg_id", nullable = false)
    private Message grpMsg;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grp_usr_id", nullable = false)
    private AppUser grpUsr;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grp_id", nullable = false)
    private Group grp;

    @Column(name = "read_at")
    private Timestamp readAt;

}