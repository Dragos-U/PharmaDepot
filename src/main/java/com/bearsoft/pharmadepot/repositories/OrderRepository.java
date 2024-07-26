package com.bearsoft.pharmadepot.repositories;

import com.bearsoft.pharmadepot.models.domain.entities.PharmacyOrder;
import com.bearsoft.pharmadepot.models.domain.enums.MedType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<PharmacyOrder, Long> {

    @Query("""
            SELECT COUNT(po.id), SUM(oi.quantity * p.price), AVG(oi.quantity * p.price)
            FROM PharmacyOrder po
            JOIN po.pharmacy ph
            JOIN po.orderItems oi
            JOIN oi.product p
            WHERE ph.name = :pharmacyName
            AND EXTRACT(MONTH FROM po.localDate) = :month
            AND EXTRACT(YEAR FROM po.localDate) = :year
            """)
    List<Object[]> findOrdersByPharmacyAndMonth(@Param("pharmacyName") String pharmacyName, @Param("year") int year, @Param("month") int month);

    @Query("""
            SELECT c.medType, COUNT(po.id)
            FROM PharmacyOrder po
            JOIN po.pharmacy ph
            JOIN po.orderItems oi
            JOIN oi.product p
            JOIN p.category c
            WHERE ph.name = :pharmacyName
            AND c.medType = :category
            AND EXTRACT(YEAR FROM po.localDate) = :year
            GROUP BY c.medType
            """)
    List<Object[]> findOrdersByPharmacyAndCategory(@Param("pharmacyName") String pharmacyName, @Param("year") int year, @Param("category") MedType category);

    @Query("""
           SELECT ph.name, SUM(oi.quantity * p.price)
           FROM PharmacyOrder po
           JOIN po.pharmacy ph
           JOIN po.orderItems oi
           JOIN oi.product p
           WHERE EXTRACT(YEAR from po.localDate) = :year
           GROUP BY ph.name
           ORDER BY SUM(oi.quantity * p.price) DESC
           """)
    List<Object[]> findTopPharmacyByYear(@Param("year") int year);
}
