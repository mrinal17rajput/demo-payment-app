package com.demo.account.payment.repository;

import com.demo.account.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query(
            value = "select * " +
                    "from Payment p " +
                    "join Account a " +
                    "on ((p.from_Account_id = a.id or p.to_Account_id = a.id ) and a.account_no = :accountNo) " +
                    "order by p.last_Update desc limit 10",
            nativeQuery = true)
    public List<Payment> findByAccountNo(@Param("accountNo") Long accountNo);

    public List<Payment> findByLastUpdateBetween(Date startDate, Date endDate);

}
