package com.traffic.report.repository;

import com.traffic.report.model.IDUserActivity;
import com.traffic.report.model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DatabaseTrafficInfoRepository extends JpaRepository<UserActivity, IDUserActivity> {
    /*
    Select User activity belong to filter host, count and time stamp
    @param hostName: facebook, youtube,...
    @param timeStamp: yyyy-MM
     */
    @Query(value = "Select * " +
            "from user_activity " +
            "where user_id = ?1 " +
            "and url like %?2% " +
            "and user_activity.\"date\" like ?3%",
            nativeQuery = true)
    List<UserActivity> selectUserActivityBelongToFilter(String userid, String hostName, String yearMonth);
}
